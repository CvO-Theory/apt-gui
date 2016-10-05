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

import java.util.List;

import uniol.aptgui.View;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.swing.extensiontable.ExtensionTableModel;

public interface ExtensionBrowserView extends View<ExtensionBrowserPresenter> {

	/**
	 * Sets a list of open document windows using WindowRef objects.
	 *
	 * @param documentWindowReferences
	 *                references to all document editor windows
	 */
	void setDocumentList(List<WindowRef> documentWindowReferences);

	/**
	 * Sets a list of graphical elements.
	 *
	 * @param graphicalElements
	 *                list of graphical elements
	 */
	void setElementList(List<GraphicalElement> graphicalElements);

	/**
	 * Selects the given document reference. Listeners will not be notified.
	 *
	 * @param refToSelect
	 *                new selection
	 */
	void selectDocument(WindowRef refToSelect);

	/**
	 * Selects the given graphical element. Listeners will not be notified.
	 *
	 * @param elemToSelect
	 *                new selection
	 */
	void selectElement(GraphicalElement elemToSelect);

	/**
	 * Sets the extension table model which contains all data that can be
	 * displayed in the extension browser table.
	 */
	void setExtensionTableModel(ExtensionTableModel tableModel);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
