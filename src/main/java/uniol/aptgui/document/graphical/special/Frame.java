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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.graphical.GraphicalElement;

/**
 * Graphical element that is a simple rectangle.
 */
public class Frame extends GraphicalElement {

	private Rectangle rect;

	public Frame() {
		setColor(Color.BLACK);
		setVisible(false);
	}

	/**
	 * Sets position and dimension of the frame.
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setRectangle(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public void draw(Graphics2D graphics, RenderingOptions renderingOptions) {
		if (!visible) {
			return;
		}
		super.draw(graphics, renderingOptions);
		graphics.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
	}

	@Override
	public Rectangle getBounds() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean coversPoint(Point point) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canDraw() {
		return rect != null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
