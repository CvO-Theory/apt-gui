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

package uniol.aptgui.swing.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.commands.SetLabelCommand;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.traits.HasLabel;

@SuppressWarnings("serial")
public class SetLabelAction extends AbstractAction {

	private final Application app;

	@Inject
	public SetLabelAction(Application app) {
		this.app = app;
		String name = "Set label";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();

		Component parentComponent = (Component) app.getMainWindow().getView();
		String message = "Label string:";
		String title = "Set label";
		String initialSelectionValue = getInitialLabelValue(document);
		String newLabel = (String) JOptionPane.showInputDialog(parentComponent, message, title,
				JOptionPane.QUESTION_MESSAGE, null, null, initialSelectionValue);

		// res is null if the user cancelled the input.
		if (newLabel == null) {
			return;
		}

		app.getHistory().execute(new SetLabelCommand(document, getSelectedHasLabelElements(document), newLabel));
	}

	private List<HasLabel> getSelectedHasLabelElements(Document<?> document) {
		List<HasLabel> result = new ArrayList<>();
		for (GraphicalElement elem : document.getSelection()) {
			if (elem instanceof HasLabel) {
				HasLabel labelElem = (HasLabel) elem;
				result.add(labelElem);
			}
		}
		return result;
	}

	private String getInitialLabelValue(Document<?> document) {
		String label = "";

		for (HasLabel labelElem : getSelectedHasLabelElements(document)) {
			if (label == "") {
				label = labelElem.getLabel();
			} else if (!label.equals(labelElem.getLabel())) {
				return "<multiple-values>";
			}
		}

		return label;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
