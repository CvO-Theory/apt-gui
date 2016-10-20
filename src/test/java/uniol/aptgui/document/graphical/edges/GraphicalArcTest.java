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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Point;
import java.awt.Rectangle;

import org.junit.Before;
import org.junit.Test;

import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

/**
 * This mostly tests GraphicalEdge because GraphicalArc has no new logic
 * compared to its base class.
 */
public class GraphicalArcTest {

	GraphicalNode source;
	GraphicalNode target;
	GraphicalEdge edge;

	@Before
	public void setUp() {
		source = new GraphicalState();
		source.setCenter(new Point(0, 0));
		target = new GraphicalState();
		target.setCenter(new Point(500, 500));
		edge = new GraphicalArc(source, target);
	}

	@Test
	public void testCoversPoint1() {
		assertThat(edge.coversPoint(new Point(50, 50)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(100, 100)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(400, 400)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(0, 400)), is(equalTo(false)));
	}

	@Test
	public void testCoversPoint2() {
		edge.addBreakpoint(new Point(500, 0));
		assertThat(edge.coversPoint(new Point(100, 0)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(500, 100)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(500, 400)), is(equalTo(true)));
		assertThat(edge.coversPoint(new Point(400, 400)), is(equalTo(false)));
	}

	@Test
	public void testGetBounds1() {
		Rectangle r1 = new Rectangle(0, 0, 500, 500);
		assertThat(edge.getBounds(), is(equalTo(r1)));
	}

	@Test
	public void testGetBounds2() {
		target.setCenter(new Point(500, 0));
		// The edge is now axis-aligned
		Rectangle r1 = new Rectangle(0, 0, 500, 1);
		assertThat(edge.getBounds(), is(equalTo(r1)));
	}

	@Test
	public void testGetClosestBreakpointIndex() {
		edge.addBreakpoint(new Point(100, 0));
		assertThat(edge.getClosestBreakpointIndex(new Point(103, 3)), is(equalTo(0)));
	}

	@Test
	public void testAddBreakpointToClosestSegment() {
		assertThat(edge.addBreakpointToClosestSegment(new Point(100, 103)), is(equalTo(0)));
		assertThat(edge.addBreakpointToClosestSegment(new Point(200, 203)), is(equalTo(1)));
		assertThat(edge.addBreakpointToClosestSegment(new Point(150, 147)), is(equalTo(1)));
		assertThat(edge.addBreakpointToClosestSegment(new Point(500, 0)), is(equalTo(-1)));
		assertThat(edge.getBreakpoints(), hasSize(3));
	}

	@Test
	public void testIsBreakpointNecessary() {
		int bp = edge.addBreakpoint(new Point(200, 0));
		assertThat(edge.isBreakpointNecessary(bp), is(equalTo(true)));
		target.setCenter(new Point(500, 0));
		assertThat(edge.isBreakpointNecessary(bp), is(equalTo(false)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
