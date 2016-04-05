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

package uniol.aptgui.gui.editor.graphicalelements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uniol.apt.adt.extension.IExtensible;
import uniol.aptgui.gui.editor.layout.Layout;

public abstract class Document {

	private List<DocumentListener> listeners;

	protected int width;
	protected int height;

	protected int translationX = 0;
	protected int translationY = 0;
	protected double scale = 1.0;

	public Document() {
		this(400, 300);
	}

	public Document(int width, int height) {
		this.listeners = new ArrayList<>();
		this.width = width;
		this.height = height;
	}

	public void addListener(DocumentListener listener) {
		listeners.add(listener);
	}

	public void fireDocumentDirty() {
		for (DocumentListener l : listeners) {
			l.onDocumentDirty();
		}
	}

	public abstract void applyLayout(Layout layout);

	public void drawDocument(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform originalTransform = graphics.getTransform();
		graphics.translate(translationX, translationY);
		graphics.scale(scale, scale);
		draw(graphics);
		graphics.setTransform(originalTransform);
	}

	/**
	 * Transforms the given point (in view coordinates) into
	 * model coordinates.
	 *
	 * @param point
	 *                the original point in view coordinates
	 * @return the transformed point in model coordinates
	 */
	public Point transformViewToModel(Point point) {
		try {
			AffineTransform tx = new AffineTransform();
			tx.translate(translationX, translationY);
			tx.scale(scale, scale);
			Point2D p2d = tx.inverseTransform(point, null);
			return point2DtoPoint(p2d);
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	/**
	 * Transforms the given point (in model coordinates) into
	 * view coordinates.
	 *
	 * @param point
	 *                the original point in model coordinates
	 * @return the transformed point in view coordinates
	 */
	public Point transformModelToView(Point point) {
		AffineTransform tx = new AffineTransform();
		tx.translate(translationX, translationY);
		tx.scale(scale, scale);
		Point2D p2d = tx.transform(point, null);
		return point2DtoPoint(p2d);
	}

	private Point point2DtoPoint(Point2D p2d) {
		return new Point((int) p2d.getX(), (int) p2d.getY());
	}

	protected abstract void draw(Graphics2D graphics);

	@SuppressWarnings("unchecked")
	protected static <T> T getGraphicalExtension(IExtensible obj) {
		return (T) obj.getExtension(GraphicalElement.EXTENSION_KEY);
	}

	public void translateView(int dx, int dy) {
		translationX += dx;
		translationY += dy;
	}

	public void scaleView(double scale) {
		this.scale = this.scale * scale;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTranslationX() {
		return translationX;
	}

	public void setTranslationX(int translationX) {
		this.translationX = translationX;
	}

	public int getTranslationY() {
		return translationY;
	}

	public void setTranslationY(int translationY) {
		this.translationY = translationY;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Returns the GraphicalElement that covers the given point or null if
	 * there is no element at that position.
	 *
	 * @param point point in model coordinates
	 * @return
	 */
	public abstract GraphicalElement getElementAt(Point point);

	public GraphicalElement getElementAtViewCoordinates(Point cursor) {
		return getElementAt(transformViewToModel(cursor));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
