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

package uniol.aptgui.document.graphical.special;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;

/**
 * Invisible graphical node that is used temporarily during edge creation.
 */
public class InvisibleNode extends GraphicalNode {

	/**
	 * Creates a new invisible node.
	 */
	public InvisibleNode() {
		setVisible(false);
	}

	@Override
	public Point getBoundaryIntersection(Point point) {
		return center;
	}

	@Override
	public boolean coversPoint(Point point) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void drawShape(Graphics2D graphics, RenderingOptions renderingOptions) {
		// Empty
	}

	@Override
	protected void drawId(Graphics2D graphics, RenderingOptions renderingOptions) {
		// Empty
	}

	@Override
	protected void drawSelectionMarkers(Graphics2D graphics, RenderingOptions renderingOptions) {
		// Empty
	}

	@Override
	public Rectangle getBounds() {
		throw new UnsupportedOperationException();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
