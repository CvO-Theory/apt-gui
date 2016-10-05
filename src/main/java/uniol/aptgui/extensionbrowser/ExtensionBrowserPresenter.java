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

package uniol.aptgui.extensionbrowser;

import uniol.aptgui.Presenter;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.mainwindow.WindowRef;

public interface ExtensionBrowserPresenter extends Presenter<ExtensionBrowserView> {

	// ACTIONS

	/**
	 * Shows the given document in the extension browser.
	 *
	 * @param document
	 *                document
	 */
	void select(Document<?> document);

	/**
	 * Shows the given graphical element of the given document in the
	 * extension browser.
	 *
	 * @param document
	 *                document
	 * @param element
	 *                graphical element inside of document
	 */
	void select(Document<?> document, GraphicalElement element);

	// VIEW EVENTS

	/**
	 * Called by the view when the document selection changes.
	 *
	 * @param selection
	 *                new selection
	 */
	void onDocumentSelectionChanged(WindowRef selection);

	/**
	 * Called by the view when the element selection changes.
	 *
	 * @param selection
	 *                new selection
	 */
	void onElementSelectionChanged(GraphicalElement selection);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
