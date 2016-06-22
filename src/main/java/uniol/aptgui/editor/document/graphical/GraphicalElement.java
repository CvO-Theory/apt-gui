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

package uniol.aptgui.editor.document.graphical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import uniol.aptgui.editor.document.RenderingOptions;

/**
 * Base class for graphical elements of a document. It contains basic properties
 * such as colors or highlight states.
 */
public abstract class GraphicalElement {

	public static final String EXTENSION_KEY = "uniol.aptgui.editor.document.graphical.GraphicalElement";
	public static final String EXTENSION_KEY_PERSISTENT = "properties";

	private static final Color HIGHLIGHT_COLOR = Color.BLUE;
	private static final Color HIGHLIGHT_COLOR_ERROR = Color.RED;
	private static final Color HIGHLIGHT_COLOR_SUCCESS = Color.GREEN;

	protected Color color;
	protected boolean highlighted;
	protected boolean highlightedError;
	protected boolean highlightedSuccess;
	protected boolean visible;
	protected boolean selected;

	public GraphicalElement() {
		this.color = Color.BLACK;
		this.visible = true;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isHighlightedError() {
		return highlightedError;
	}

	public void setHighlightedError(boolean highlightedError) {
		this.highlightedError = highlightedError;
	}

	public boolean isHighlightedSuccess() {
		return highlightedSuccess;
	}

	public void setHighlightedSuccess(boolean highlightedSuccess) {
		this.highlightedSuccess = highlightedSuccess;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Draws this element using the given graphics object. The
	 * RenderingOptions object may influence what is drawn or how it is
	 * drawn.
	 *
	 * @param graphics
	 *                graphics object
	 * @param renderingOptions
	 *                settings that influence the rendering
	 */
	public void draw(Graphics2D graphics, RenderingOptions renderingOptions) {
		if (!visible) {
			return;
		}

		if (highlightedError) {
			graphics.setColor(HIGHLIGHT_COLOR_ERROR);
		} else if (highlightedSuccess) {
			graphics.setColor(HIGHLIGHT_COLOR_SUCCESS);
		} else if (highlighted) {
			graphics.setColor(HIGHLIGHT_COLOR);
		} else {
			graphics.setColor(color);
		}
	}

	public abstract boolean coversPoint(Point point);

	/**
	 * Draws selection markers around the given center point with radius
	 * distance.
	 *
	 * @param graphics
	 * @param center
	 * @param radius
	 */
	protected static void drawSelectionMarkers(Graphics2D graphics, Point center, int radius) {
		int len = 5;

		Point topLeft = new Point(center.x - radius, center.y - radius);
		Point topRight = new Point(center.x + radius, center.y - radius);
		Point bottomLeft = new Point(center.x - radius, center.y + radius);
		Point bottomRight = new Point(center.x + radius, center.y + radius);

		graphics.drawLine(topLeft.x, topLeft.y, topLeft.x + len, topLeft.y);
		graphics.drawLine(topLeft.x, topLeft.y, topLeft.x, topLeft.y + len);

		graphics.drawLine(topRight.x, topRight.y, topRight.x - len, topRight.y);
		graphics.drawLine(topRight.x, topRight.y, topRight.x, topRight.y + len);

		graphics.drawLine(bottomLeft.x, bottomLeft.y, bottomLeft.x + len, bottomLeft.y);
		graphics.drawLine(bottomLeft.x, bottomLeft.y, bottomLeft.x, bottomLeft.y - len);

		graphics.drawLine(bottomRight.x, bottomRight.y, bottomRight.x - len, bottomRight.y);
		graphics.drawLine(bottomRight.x, bottomRight.y, bottomRight.x, bottomRight.y - len);
	}

	/**
	 * Returns an axis-aligned minimum bounding box that contains this
	 * element.
	 *
	 * @return an axis-aligned minimum bounding box that contains this
	 *         element
	 */
	public abstract Rectangle getBounds();

	/**
	 * Returns true if all necessary attribute for drawing this element have
	 * been set.
	 *
	 * @return true if all necessary attribute for drawing this element have
	 *         been set
	 */
	public abstract boolean canDraw();

	/**
	 * Returns a user-readable string representation of this element.
	 *
	 * @return a user-readable string representation of this element
	 */
	public String toUserString() {
		return toString();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
