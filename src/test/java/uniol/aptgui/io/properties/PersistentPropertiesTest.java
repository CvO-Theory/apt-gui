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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Point;

import org.junit.Test;

import uniol.apt.adt.extension.Extensible;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;

public class PersistentPropertiesTest {

	Extensible extensible;

	public void setUp(String propertyString) {
		extensible = mock(Extensible.class);
		when(extensible.hasExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT)).thenReturn(true);
		when(extensible.getExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT)).thenReturn(propertyString);
	}

	@Test
	public void testParseNodeProperties() {
		GraphicalNode node = mock(GraphicalNode.class);
		setUp("pos 1,2; col #112233");

		PersistentProperties pp = new PersistentProperties(extensible);
		pp.applyAll(node);

		verify(node).setCenter(new Point(1, 2));
		verify(node).setColor(new Color(0x11, 0x22, 0x33));
	}

	@Test
	public void testParseEdgeProperties() {
		GraphicalEdge edge = mock(GraphicalEdge.class);
		setUp("bp 1,2 3,4; col #112233");

		PersistentProperties pp = new PersistentProperties(extensible);
		pp.applyAll(edge);

		verify(edge).addBreakpoint(new Point(1, 2));
		verify(edge).addBreakpoint(new Point(3, 4));
		verify(edge).setColor(new Color(0x11, 0x22, 0x33));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
