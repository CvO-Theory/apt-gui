/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2016 Jonas Prellberg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package uniol.aptgui.swing.actions.base;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.eventbus.EventBus;

import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.document.Document;

/**
 * Action that allows to set an attribute on the current Document selection.
 *
 * @param <T>
 *                trait interface that all elements in the selection have to
 *                implement
 * @param <U>
 *                attribute type
 */
@SuppressWarnings("serial")
public abstract class SetSimpleAttributeAction<T, U> extends DocumentAction {

	private final String name;
	private final String inputMessage;
	protected Document<?> document;

	public SetSimpleAttributeAction(String name, String inputMessage, Application app, EventBus eventBus) {
		super(app, eventBus);
		this.name = name;
		this.inputMessage = inputMessage;
		eventBus.register(this);
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		setEnabled(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		document = app.getActiveDocument();

		// Action should only be enabled when this action is applicable
		// for the document's selection. Therefore there is no further
		// check if the selected elements really are instances of the
		// trait interface.
		List<T> selection = new ArrayList<>();
		selection.addAll((Collection<? extends T>) document.getSelection());

		String initialValue = getInitialValue(selection);
		String newValue = showInputDialog(initialValue);

		// If newValue is null the user cancelled the input.
		if (newValue == null || initialValue.equals(newValue)) {
			return;
		}

		Command cmd = createCommand(document, selection, newValue);
		if (cmd != null) {
			app.getHistory().execute(cmd);
		}
	}

	/**
	 * Creates the command to execute this action. Must be overwritten in
	 * subclasses to provide the actual implementation. Implementors should
	 * return null if no command should be executed.
	 *
	 * @param document
	 *                the document that will be modified
	 * @param selection
	 *                list of selected elements that will be modified
	 * @param userInput
	 *                String entered by the user
	 * @return Command or null
	 */
	protected abstract Command createCommand(Document<?> document, List<T> selection, String userInput);

	protected String showInputDialog(String initialValue) {
		return app.getMainWindow().showInputDialog(name, inputMessage, initialValue);
	}

	private String getInitialValue(List<T> selection) {
		U value = null;

		for (T elem : selection) {
			if (value == null) {
				value = getAttribute(elem);
			} else if (!value.equals(getAttribute(elem))) {
				return "<multiple-values>";
			}
		}

		return String.valueOf(value);
	}

	protected abstract U getAttribute(T element);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
