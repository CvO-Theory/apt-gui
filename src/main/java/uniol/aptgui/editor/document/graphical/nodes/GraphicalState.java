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
import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;

/**
 * Graphical representation class of LTS states.
 */
public class GraphicalState extends GraphicalNode {

	private static final int RADIUS = 20;
	private static final int INITIAL_STATE_ARROW_LENGTH = 30;

	protected boolean isInitialState;

	/**
	 * Returns if this state is an initial state of the LTS.
	 *
	 * @return if this state is an initial state of the LTS
	 */
	public boolean isInitialState() {
		return isInitialState;
	}

	/**
	 * Sets if this state is an initial state of the LTS.
	 *
	 * @param isInitialState
	 *                true, if this state should be an initial state
	 */
	public void setInitialState(boolean isInitialState) {
		this.isInitialState = isInitialState;
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
		if (isInitialState) {
			Point outside = new Point(center.x - RADIUS - INITIAL_STATE_ARROW_LENGTH, center.y);
			Point onBoundary = getBoundaryIntersection(outside);
			List<Point> path = new ArrayList<>();
			path.add(outside);
			path.add(onBoundary);
			GraphicalEdge.drawPathWithArrowhead(graphics, path);
		}
	}

	@Override
	protected void drawId(Graphics2D graphics, RenderingOptions renderingOptions) {
		if (renderingOptions.isStateIdLabelVisible()) {
			drawCenteredString(graphics, center, id);
		}
	}

	@Override
	protected void drawSelectionMarkers(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawSelectionMarkers(graphics, center, RADIUS + 2);
	}

	@Override
	public Rectangle getBounds() {
		if (isInitialState) {
			return new Rectangle(
				center.x - RADIUS - INITIAL_STATE_ARROW_LENGTH,
				center.y - RADIUS,
				2 * RADIUS + INITIAL_STATE_ARROW_LENGTH,
				2 * RADIUS
			);
		} else {
			return new Rectangle(
				center.x - RADIUS,
				center.y - RADIUS,
				2 * RADIUS,
				2 * RADIUS
			);
		}
	}

	@Override
	public String toUserString() {
		return getId() + " (State)";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
