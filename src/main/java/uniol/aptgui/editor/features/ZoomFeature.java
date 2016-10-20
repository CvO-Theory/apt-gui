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

import java.awt.event.MouseWheelEvent;

import uniol.aptgui.document.Document;
import uniol.aptgui.editor.features.base.Feature;

/**
 * The zoom feature gives the user the ability to scale the view.
 */
public class ZoomFeature extends Feature {

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Creates a new zoom feature that operates on the given document.
	 *
	 * @param document
	 *                document whose transform will be modified by this
	 *                feature
	 */
	public ZoomFeature(Document<?> document) {
		this.document = document;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() > 0) {
			document.getViewport().decreaseScale(e.getWheelRotation());
		} else {
			document.getViewport().increaseScale(-e.getWheelRotation());
		}
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
