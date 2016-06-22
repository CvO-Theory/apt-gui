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

package uniol.aptgui.editor.document.graphical.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.document.graphical.traits.HasLabel;

public class GraphicalTransition extends GraphicalNode implements HasLabel {

	private static final int RADIUS = 20;
	private static final int ID_OFFSET = 7;

	protected String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Point getBoundaryIntersection(Point point) {
		return getSquareBoundaryIntersection(center, RADIUS, point);
	}

	@Override
	public boolean coversPoint(Point point) {
		if (!super.coversPoint(point)) {
			return false;
		}
		int minX = center.x - RADIUS;
		int maxX = center.x + RADIUS;
		int minY = center.y - RADIUS;
		int maxY = center.y + RADIUS;
		return minX <= point.x && point.x <= maxX && minY <= point.y && point.y <= maxY;
	}

	@Override
	protected void drawShape(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawSquare(graphics, center, 20);
		if (label != null) {
			drawCenteredString(graphics, center, label);
		}
	}

	@Override
	protected void drawId(Graphics2D graphics, RenderingOptions renderingOptions) {
		if (renderingOptions.isTransitionIdLabelVisible()) {
			Point idLabelPosition = new Point(center.x + RADIUS + ID_OFFSET, center.y - RADIUS - ID_OFFSET);
			drawCenteredString(graphics, idLabelPosition, id);
		}
	}

	@Override
	protected void drawSelectionMarkers(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawSelectionMarkers(graphics, center, RADIUS + 2);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(
			center.x - RADIUS,
			center.y - RADIUS - ID_OFFSET,
			2 * RADIUS + ID_OFFSET,
			2 * RADIUS + ID_OFFSET
		);
	}

	@Override
	public String toUserString() {
		return getId() + " (Transition)";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
