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

package uniol.aptgui.editor.features;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.editor.EditorView;
import uniol.aptgui.editor.features.base.Feature;

/**
 * Feature that provides a context menu with actions that modify the object
 * under the cursor.
 */
public class ContextMenuFeature extends Feature {

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's viewport object.
	 */
	private final Viewport viewport;

	/**
	 * Reference to the editor view this feature belongs to.
	 */
	private final EditorView view;

	public ContextMenuFeature(Document<?> document, EditorView view) {
		this.document = document;
		this.viewport = document.getViewport();
		this.view = view;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Only listen for the RMB.
		if (e.getButton() != MouseEvent.BUTTON3) {
			return;
		}

		Point modelPosition = viewport.transformInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
		if (elem instanceof GraphicalElement) {
			if (!document.getSelection().contains(elem)) {
				document.clearSelection();
				document.addToSelection(elem);
			}
			document.setLastSelectionPosition(modelPosition);
			document.fireSelectionChanged();
			document.fireDocumentDirty();
			view.showPopupMenu(e.getX(), e.getY());
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
