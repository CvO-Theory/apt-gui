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

package uniol.aptgui.document.graphical.nodes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import uniol.aptgui.document.RenderingOptions;

/**
 * Graphical element for Petri net places.
 */
public class GraphicalPlace extends GraphicalNode {

	private static final int RADIUS = 20;
	private static final int ID_OFFSET = 7;
	private static final long SMALL_TOKEN_THRESHOLD = 5;
	private static final int TOKEN_DOT_RADIUS = 2;

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
	protected void drawShape(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawCircle(graphics, center, RADIUS);
		if (tokens <= SMALL_TOKEN_THRESHOLD) {
			drawTokens(graphics);
		} else {
			drawCenteredString(graphics, center, String.valueOf(tokens));
		}
	}

	/**
	 * Draws tokens as dots in the correct layout.
	 *
	 * @param graphics
	 *                graphics object
	 */
	private void drawTokens(Graphics2D graphics) {
		int offset = RADIUS / 3;
		Point topLeft = new Point(center.x - offset, center.y - offset);
		Point topRight = new Point(center.x + offset, center.y - offset);
		Point topCenter = new Point(center.x, center.y - offset);
		Point btmLeft = new Point(center.x - offset, center.y + offset);
		Point btmRight = new Point(center.x + offset, center.y + offset);
		Point centerLeft = new Point(center.x - offset, center.y);
		Point centerRight = new Point(center.x + offset, center.y);
		if (tokens == 1) {
			drawTokenDot(graphics, center);
		} else if (tokens == 2) {
			drawTokenDot(graphics, centerLeft);
			drawTokenDot(graphics, centerRight);
		} else if (tokens == 3) {
			drawTokenDot(graphics, btmLeft);
			drawTokenDot(graphics, btmRight);
			drawTokenDot(graphics, topCenter);
		} else if (tokens == 4) {
			drawTokenDot(graphics, btmLeft);
			drawTokenDot(graphics, btmRight);
			drawTokenDot(graphics, topLeft);
			drawTokenDot(graphics, topRight);
		} else if (tokens == 5) {
			drawTokenDot(graphics, btmLeft);
			drawTokenDot(graphics, btmRight);
			drawTokenDot(graphics, topLeft);
			drawTokenDot(graphics, topRight);
			drawTokenDot(graphics, center);
		}
	}

	/**
	 * Draws a token as a dot at the given position.
	 *
	 * @param graphics
	 *                graphics object
	 * @param at
	 *                position to draw the dot at
	 */
	private void drawTokenDot(Graphics2D graphics, Point at) {
		graphics.fillOval(
			at.x - TOKEN_DOT_RADIUS,
			at.y - TOKEN_DOT_RADIUS,
			2 * TOKEN_DOT_RADIUS,
			2 * TOKEN_DOT_RADIUS
		);
	}

	@Override
	protected void drawId(Graphics2D graphics, RenderingOptions renderingOptions) {
		if (renderingOptions.isPlaceIdLabelVisible()) {
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
		return getId() + " (Place)";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
