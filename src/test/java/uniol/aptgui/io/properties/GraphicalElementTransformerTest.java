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

package uniol.aptgui.io.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

import uniol.aptgui.document.graphical.edges.GraphicalArc;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

public class GraphicalElementTransformerTest {

	GraphicalElementTransformer transformer = new GraphicalElementTransformer();

	@Test
	public void testApplyNode() {
		GraphicalState node = new GraphicalState();
		node.setCenter(new Point(1, 2));
		node.setColor(new Color(0x11, 0x22, 0x33));
		assertThat(transformer.apply(node), is(equalTo("pos 1,2; col #112233")));
	}

	@Test
	public void testApplyEdge() {
		GraphicalArc edge = new GraphicalArc(mock(GraphicalState.class), mock(GraphicalState.class));
		edge.setColor(new Color(0x11, 0x22, 0x33));
		edge.addBreakpoint(new Point(1, 2));
		edge.addBreakpoint(new Point(3, 4));
		assertThat(transformer.apply(edge), is(equalTo("bp 1,2 3,4; col #112233")));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
