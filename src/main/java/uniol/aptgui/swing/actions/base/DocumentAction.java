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

import javax.swing.AbstractAction;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.events.DocumentChangedEvent;
import uniol.aptgui.events.DocumentSelectionChangedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;

/**
 * Action super-class for all actions that should only be enabled if a document
 * editor is active and optionally if a certain document state is met. Override
 * {@link checkEnabled} to provide the optional state check.
 */
@SuppressWarnings("serial")
public abstract class DocumentAction extends AbstractAction {

	protected final Application app;

	public DocumentAction(Application app, EventBus eventBus) {
		this.app = app;
		setEnabled(false);
		eventBus.register(this);
	}

	@Subscribe
	public void onDocumentSelectionChangedEvent(DocumentSelectionChangedEvent e) {
		setEnabled(checkEnabled(e.getDocument()));
	}

	@Subscribe
	public void onDocumentChangedEvent(DocumentChangedEvent e) {
		setEnabled(checkEnabled(e.getDocument()));
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		// Disable action, if the last document editor was closed.
		if (app.getDocumentWindows().isEmpty()) {
			setEnabled(false);
		}
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		Document<?> document = app.getDocument(e.getWindowId());
		if (document != null) {
			setEnabled(checkEnabled(document));
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Returns if this action should be enabled given the document's
	 * selection state.
	 *
	 * @param document
	 *                document whose selection will be examined to decide if
	 *                this action should be enabled
	 * @return true, if this action should be enabled for the given document
	 */
	private boolean checkEnabled(Document<?> document) {
		Class<? extends GraphicalElement> commonBase = document.getSelectionCommonBaseClass();
		// Compare traits with Object if commonBase is null, since it
		// will not throw an exception but return false for the
		// isAssignableFrom calls as expected.
		Class<?> testClass = (commonBase == null) ? Object.class : commonBase;

		return checkEnabled(document, testClass);
	}

	/**
	 * Called by the superclass when the document selection changes. This
	 * method must return true, if the action should still be enabled.
	 *
	 * @param document
	 *                the document which contains the selection
	 * @param commonBaseTestClass
	 *                base class of selection with null replaced by Object
	 * @return true, if the action is applicable for the given selection
	 */
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return true;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
