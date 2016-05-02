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

import uniol.aptgui.editor.document.graphical.traits.HasTokens;

public class GraphicalPlace extends GraphicalNode implements HasTokens {

	private static final int RADIUS = 20;
	protected long tokens;

	public long getTokens() {
		return tokens;
	}

	public void setTokens(long tokens) {
		this.tokens = tokens;
	}

	@Override
	public Point getBoundaryIntersection(Point point) {
		return getCircleBoundaryIntersection(center, RADIUS, point);
	}

	@Override
	public boolean coversPoint(Point point) {
		return super.coversPoint(point) && center.distance(point.x, point.y) < RADIUS;
	}

	@Override
	protected void drawShape(Graphics2D graphics) {
		drawCircle(graphics, center, RADIUS);
		// TODO draw tokens as points if it is a low number
		drawCenteredString(graphics, center, String.valueOf(tokens));
	}

	@Override
	protected void drawId(Graphics2D graphics) {
		Point idLabelPosition = new Point(center.x + RADIUS + 7, center.y - RADIUS - 7);
		drawCenteredString(graphics, idLabelPosition, id);
	}

	@Override
	protected void drawSelectionMarkers(Graphics2D graphics) {
		drawSelectionMarkers(graphics, center, RADIUS + 2);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
