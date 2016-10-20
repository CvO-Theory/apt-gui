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

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.graphical.GraphicalElement;

/**
 * Abstract base class for all node-like GraphicalElements.
 */
public abstract class GraphicalNode extends GraphicalElement {

	protected String id;
	protected Point center;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Translates the center point by the given amount in x and y direction.
	 *
	 * @param dx
	 *                movement in x direction
	 * @param dy
	 *                movement in y direction
	 */
	public void translate(int dx, int dy) {
		center.x += dx;
		center.y += dy;
	}

	@Override
	public boolean canDraw() {
		return center != null;
	}

	@Override
	public boolean coversPoint(Point point) {
		return center != null;
	}

	@Override
	protected void drawImpl(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawShape(graphics, renderingOptions);
		if (id != null) {
			drawId(graphics, renderingOptions);
		}
		if (selected) {
			drawSelectionMarkers(graphics, renderingOptions);
		}
	}

	protected abstract void drawShape(Graphics2D graphics, RenderingOptions renderingOptions);

	protected abstract void drawId(Graphics2D graphics, RenderingOptions renderingOptions);

	protected abstract void drawSelectionMarkers(Graphics2D graphics, RenderingOptions renderingOptions);

	/**
	 * Returns the intersection point of this GraphicalNode's boundary with
	 * a line from the given point to this GraphicalNode's center.
	 *
	 * @param point
	 *                any point outside of the node's boundary
	 * @return intersection point of a linear connection between the node's
	 *         center and the given point
	 */
	public abstract Point getBoundaryIntersection(Point point);

	protected static Point getCircleBoundaryIntersection(Point center, int radius, Point test) {
		double deltaCenterTestX = (center.x - test.x);
		double deltaCenterTestY = (center.y - test.y);
		double alpha = atan(deltaCenterTestY / deltaCenterTestX);
		double deltaX = cos(alpha) * radius;
		double deltaY = sin(alpha) * radius;

		if (deltaCenterTestX >= 0) {
			deltaX = -deltaX;
			deltaY = -deltaY;
		}

		int x = (int) (center.x + deltaX);
		int y = (int) (center.y + deltaY);
		return new Point(x, y);
	}

	protected static Point getSquareBoundaryIntersection(Point center, int radius, Point test) {
		double deltaCenterTestX = (center.x - test.x);
		double deltaCenterTestY = (center.y - test.y);
		double alpha = atan(deltaCenterTestY / deltaCenterTestX);
		double deltaX, deltaY, distance;

		if (alpha <= -Math.PI / 4 || alpha > Math.PI / 4) {
			distance = radius / Math.sin(alpha);
			deltaX = distance * Math.cos(alpha);
			deltaY = radius;

			if (deltaCenterTestY > 0) {
				deltaX = -deltaX;
				deltaY = -deltaY;
			}
		} else {
			distance = radius / Math.cos(alpha);
			deltaX = radius;
			deltaY = distance * Math.sin(alpha);

			if (deltaCenterTestX > 0) {
				deltaX = -deltaX;
				deltaY = -deltaY;
			}
		}

		int x = (int) (center.x + deltaX);
		int y = (int) (center.y + deltaY);
		return new Point(x, y);
	}

	protected static void drawCircle(Graphics2D graphics, Point center, int radius) {
		graphics.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	protected static void drawSquare(Graphics2D graphics, Point center, int radius) {
		graphics.drawRect(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	protected static void drawCenteredString(Graphics2D graphics, Point center, String string) {
		FontMetrics metrics = graphics.getFontMetrics();
		int xOffset = metrics.stringWidth(string) / 2;
		int yOffset = metrics.getAscent() / 2;
		graphics.drawString(string, center.x - xOffset, center.y + yOffset);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
