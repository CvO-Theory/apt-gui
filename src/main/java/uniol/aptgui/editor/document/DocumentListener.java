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

package uniol.aptgui.editor.document;

import uniol.aptgui.editor.document.graphical.GraphicalElement;

public interface DocumentListener {

	/**
	 * Called when the document changes visually.
	 */
	void onDocumentDirty();

	/**
	 * Called when the document changes structurally.
	 */
	void onDocumentChanged();

	/**
	 * Called when the selection in this Document changes.
	 *
	 * @param commonBaseClass
	 *                most specific base class that all selected elements
	 *                are assignable to or null if nothing is selected
	 */
	void onSelectionChanged(Class<? extends GraphicalElement> commonBaseClass);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
