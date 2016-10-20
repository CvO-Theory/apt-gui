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

package uniol.aptgui.document.graphical.edges;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;

/**
 * Abstract base class for all graphical elements that are edge-like. Each
 * edge-like element has a source and target graphical element and optional
 * breakpoints in between that further specify the path the edge takes.
 */
public abstract class GraphicalEdge extends GraphicalElement {

	private static final double SELECTION_DISTANCE = 10;

	protected GraphicalNode source;
	protected GraphicalNode target;
	protected List<Point> breakpoints;
	protected String label;

	public GraphicalEdge(GraphicalNode source, GraphicalNode target) {
		assert source != null;
		assert target != null;
		this.source = source;
		this.target = target;
		this.breakpoints = new ArrayList<>();
		this.label = "";
	}

	@Override
	public boolean canDraw() {
		return true;
	}

	/**
	 * Returns the source node of this edge.
	 *
	 * @return the source node of this edge
	 */
	public GraphicalNode getSource() {
		return source;
	}

	/**
	 * Sets the source node of this edge.
	 *
	 * @param source
	 *                the source node of this edge
	 */
	public void setSource(GraphicalNode source) {
		this.source = source;
	}

	/**
	 * Returns the target node of this edge.
	 *
	 * @return the target node of this edge
	 */
	public GraphicalNode getTarget() {
		return target;
	}

	/**
	 * Sets the target node of this edge.
	 *
	 * @param target
	 *                the source node of this edge
	 */
	public void setTarget(GraphicalNode target) {
		this.target = target;
	}

	/**
	 * Returns the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label
	 *
	 * @param label
	 *                the label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	protected void drawImpl(Graphics2D graphics, RenderingOptions renderingOptions) {
		List<Point> path = getPath();
		drawPathWithArrowhead(graphics, path);
		drawLabel(graphics, path, label);
		if (selected) {
			drawSelectionMarkers(graphics, path);
		}
	}

	@Override
	public boolean coversPoint(Point point) {
		return getSegmentIndexAt(point) != -1;
	}

	@Override
	public Rectangle getBounds() {
		// Prevent width or height from being 0 as this messes with Rectangle#contains
		Rectangle bounds = new Rectangle(source.getCenter(), new Dimension(1, 1));
		for (Point bp : breakpoints) {
			bounds = bounds.union(new Rectangle(bp));
		}
		return bounds.union(new Rectangle(target.getCenter()));
	}

	/**
	 * Returns the breakpoint with the given index. Attention: A reference
	 * is returned, so any modifications will be mirrored by the
	 * GraphicalEdge.
	 *
	 * @param index
	 *                breakpoint index
	 * @return the breakpoint at the index
	 */
	public Point getBreakpoint(int index) {
		return breakpoints.get(index);
	}

	/**
	 * Returns an unmodifiable view of the breakpoint list.
	 *
	 * @return an unmodifiable view of the breakpoint list
	 */
	public List<Point> getBreakpoints() {
		return Collections.unmodifiableList(breakpoints);
	}

	/**
	 * Removes all breakpoints.
	 */
	public void removeAllBreakpoints() {
		breakpoints.clear();
	}

	/**
	 * Returns the amount of breakpoints this edge has.
	 *
	 * @return the amount of breakpoints
	 */
	public int getBreakpointCount() {
		return breakpoints.size();
	}

	/**
	 * Returns the closest breakpoint index to the given point that is
	 * within the selection distance.
	 *
	 * @param point
	 *                test position
	 * @return the closest breakpoint index or -1 if no breakpoint is close
	 *         enough
	 */
	public int getClosestBreakpointIndex(Point point) {
		for (int i = 0; i < breakpoints.size(); i++) {
			Point p = breakpoints.get(i);
			if (p.distance(point.x, point.y) < SELECTION_DISTANCE) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Add the given breakpoint as the last breakpoint.
	 *
	 * @param breakpoint
	 *                the new breakpoint in model coordinates
	 * @return the index of the new breakpoint
	 */
	public int addBreakpoint(Point breakpoint) {
		breakpoints.add(breakpoint);
		return breakpoints.size() - 1;
	}

	/**
	 * Adds the given breakpoint at the given index position.
	 *
	 * @param index
	 *                index of the new breakpoint
	 * @param breakpoint
	 *                breakpoint to add
	 */
	public void addBreakpoint(int index, Point breakpoint) {
		breakpoints.add(index, breakpoint);
	}

	/**
	 * Add the given breakpoint to the closest path segment or don't add it
	 * at all if there is no segment close enough.
	 *
	 * @param breakpoint
	 *                the new breakpoint
	 * @return the index of the new breakpoint or -1 if it was not inserted
	 */
	public int addBreakpointToClosestSegment(Point breakpoint) {
		int pathIndex = getSegmentIndexAt(breakpoint);
		if (pathIndex != -1) {
			// SourceNode position is also part of the path, so
			// subtract 1.
			int insertionIndex = pathIndex - 1;
			breakpoints.add(insertionIndex, breakpoint);
			return insertionIndex;
		}
		return -1;
	}

	/**
	 * Removes the breakpoint at the given index.
	 *
	 * @param index
	 *                index of the breakpoint to remove
	 * @return the breakpoint that was previously at the given index
	 */
	public Point removeBreakpoint(int index) {
		return breakpoints.remove(index);
	}

	/**
	 * Translates the position of all breakpoints by the given x and y
	 * difference.
	 *
	 * @param dx
	 *                movement in x-direction
	 * @param dy
	 *                movement in y-direction
	 */
	public void translateBreakpoints(int dx, int dy) {
		for (Point p : breakpoints) {
			p.translate(dx, dy);
		}
	}

	/**
	 * Returns if the breakpoint at the given index is necessary, in the
	 * sense that it does not lie on a straight line between the breakpoint
	 * before and after the specified one with a margin of error.
	 *
	 * @param breakpointIndex index of the breakpoint that should be tested
	 * @return false, if the breakpoint is unnecessary and could potentially be removed
	 */
	public boolean isBreakpointNecessary(int breakpointIndex) {
		assert 0 <= breakpointIndex && breakpointIndex < breakpoints.size();

		Point curr = breakpoints.get(breakpointIndex);
		Point prev, next;
		if (breakpointIndex == 0) {
			prev = source.getCenter();
		} else {
			prev = breakpoints.get(breakpointIndex - 1);
		}
		if (breakpointIndex == breakpoints.size() - 1) {
			next = target.getCenter();
		} else {
			next = breakpoints.get(breakpointIndex + 1);
		}

		// Use law of cosines to get angle between the two line segments meeting at the given breakpoint.
		double a = sqrt(pow(curr.x - next.x, 2) + pow(curr.y - next.y, 2));
		double b = sqrt(pow(curr.x - prev.x, 2) + pow(curr.y - prev.y, 2));
		double c = sqrt(pow(prev.x - next.x, 2) + pow(prev.y - next.y, 2));
		double angle = acos((pow(a, 2) + pow(b, 2) - pow(c, 2)) / (2 * a * b));

		return angle < (PI - 0.15);
	}

	/**
	 * Returns a zero-based index that identifies the segment closest to the
	 * given point. The segment ends at path[segmentId].
	 *
	 * @param point
	 *                test position
	 * @return the segment index or -1 if the point is not near a segment
	 */
	protected int getSegmentIndexAt(Point point) {
		List<Point> path = getPath();
		Point start = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Point end = path.get(i);
			Line2D line = new Line2D.Float(start.x, start.y, end.x, end.y);
			if (line.ptSegDist(point.x, point.y) < SELECTION_DISTANCE) {
				return i;
			}
			start = end;
		}
		return -1;
	}

	/**
	 * Returns a list of points that describe this edge's path when
	 * interpolating linearly between them.
	 *
	 * @return list of points that describe this edge's path
	 */
	protected List<Point> getPath() {
		List<Point> path = new ArrayList<>();

		// Find first and last breakpoints or valid substitutes for boundary intersection computations.
		Point first, last;
		if (breakpoints.isEmpty()) {
			first = target.getCenter();
			last = source.getCenter();
		} else {
			first = breakpoints.get(0);
			last = breakpoints.get(breakpoints.size() - 1);
		}

		// Collect path Points.
		path.add(source.getBoundaryIntersection(first));
		path.addAll(breakpoints);
		path.add(target.getBoundaryIntersection(last));

		assert path.size() >= 2;
		return path;
	}

	/**
	 * Draws a label at a suitable position along the given path.
	 *
	 * @param graphics
	 *                graphics object to paint with
	 * @param path
	 *                edge path
	 * @param label
	 *                the label string to draw
	 */
	private static void drawLabel(Graphics2D graphics, List<Point> path, String label) {
		Point labelPoint = getLabelPoint(path.get(path.size() - 2), path.get(path.size() - 1));
		graphics.drawString(label, labelPoint.x, labelPoint.y);
	}

	/**
	 * Returns the position where the label should be drawn.
	 *
	 * @param segmentSource
	 *                source point of the segment that the label should be
	 *                drawn next to
	 * @param segmentDestination
	 *                destination point of the segment that the label should
	 *                be drawn next to
	 * @return label position
	 */
	private static Point getLabelPoint(Point segmentSource, Point segmentDestination) {
		final Point labelPoint = new Point();
		labelPoint.x = segmentSource.x + (segmentDestination.x - segmentSource.x) * 2 / 3;
		labelPoint.y = segmentSource.y + (segmentDestination.y - segmentSource.y) * 2 / 3 - 3;
		return labelPoint;
	}

	/**
	 * Draws selection markers that indicate the selection state to the
	 * user.
	 *
	 * @param graphics
	 *                graphics object to paint with
	 */
	private static void drawSelectionMarkers(Graphics2D graphics, List<Point> path) {
		for (Point p : path) {
			drawSelectionMarkers(graphics, p, 5);
		}
	}

	/**
	 * Draws a linear path between the given points that ends with an
	 * arrowhead.
	 *
	 * @param graphics
	 *                graphics context used for drawing
	 * @param path
	 *                list of points along which the path is drawn
	 */
	public static void drawPathWithArrowhead(Graphics2D graphics, List<Point> path) {
		assert path.size() >= 2;
		drawPath(graphics, path);
		drawArrowhead(
			graphics,
			path.get(path.size() - 2),
			path.get(path.size() - 1)
		);
	}

	/**
	 * Draws a linear path between the given points.
	 *
	 * @param graphics
	 *                graphics context used for drawing
	 * @param path
	 *                list of points along which the path is drawn
	 */
	public static void drawPath(Graphics2D graphics, List<Point> path) {
		if (path.isEmpty()) {
			return;
		}
		Point previous = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Point current = path.get(i);
			graphics.drawLine(previous.x, previous.y, current.x, current.y);
			previous = current;
		}
	}

	/**
	 * Draws an arrowhead that ends in the given target point. It's size is
	 * not affected by the distance between source and target. Therefore the
	 * source is only useful to specify the arrowhead's orientation.
	 *
	 * @param graphics
	 *                graphics context used for drawing
	 * @param source
	 *                source of the linear connection at the end of which
	 *                the arrowhead will be drawn
	 * @param target
	 *                target of the linear connection at the end of which
	 *                the arrowhead will be drawn
	 */
	public static void drawArrowhead(Graphics2D graphics, Point source, Point target) {
		int x = target.x;
		int y = target.y;
		int i1 = 12;
		int i2 = 6;
		double aDir = Math.atan2(source.x - target.x, source.y - target.y);

		Polygon arrowhead = new Polygon();
		arrowhead.addPoint(x, y);
		arrowhead.addPoint(x + xCor(i1, aDir + 0.5), y + yCor(i1, aDir + 0.5));
		arrowhead.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
		arrowhead.addPoint(x + xCor(i1, aDir - 0.5), y + yCor(i1, aDir - 0.5));
		arrowhead.addPoint(x, y);

		// make the arrowhead solid
		graphics.setStroke(new BasicStroke(1f));
		graphics.drawPolygon(arrowhead);
		graphics.fillPolygon(arrowhead);
	}

	private static int yCor(int len, double dir) {
		return (int) (len * Math.cos(dir));
	}

	private static int xCor(int len, double dir) {
		return (int) (len * Math.sin(dir));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
