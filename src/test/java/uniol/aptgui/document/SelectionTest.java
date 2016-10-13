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

package uniol.aptgui.document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.document.graphical.nodes.GraphicalState;
import uniol.aptgui.document.graphical.nodes.GraphicalTransition;

public class SelectionTest {

	Selection selection = new Selection();
	GraphicalElement elem0 = mock(GraphicalElement.class);
	GraphicalElement elem1 = mock(GraphicalTransition.class);

	@Test
	public void testAddToSelection() {
		assertThat(selection.getSelection(), is(empty()));
		selection.addToSelection(elem0);
		assertThat(selection.getSelection(), contains(elem0));
		verify(elem0).setSelected(true);
	}

	@Test
	public void testRemoveFromSelection() {
		selection.addToSelection(elem0);
		selection.addToSelection(elem1);
		reset(elem0, elem1);
		selection.removeFromSelection(elem0);
		assertThat(selection.getSelection(), contains(elem1));
		verify(elem0).setSelected(false);
		verify(elem1, never()).setSelected(anyBoolean());
	}

	@Test
	public void testClearSelection() {
		selection.addToSelection(elem0);
		selection.addToSelection(elem1);
		selection.clearSelection();
		verify(elem0).setSelected(false);
		verify(elem1).setSelected(false);
		assertThat(selection.getSelection(), is(empty()));
	}

	@Test
	public void testToggleSelection1() {
		when(elem0.isSelected()).thenReturn(false);
		selection.toggleSelection(elem0);
		verify(elem0).setSelected(true);
		assertThat(selection.getSelection(), contains(elem0));
	}

	@Test
	public void testToggleSelection2() {
		when(elem0.isSelected()).thenReturn(true);
		selection.toggleSelection(elem0);
		verify(elem0).setSelected(false);
		assertThat(selection.getSelection(), is(empty()));
	}

	@Test
	public void testGetCommonBaseEmpty() {
		assertThat(selection.getCommonBaseClass(), is(nullValue()));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetCommonBaseSpecific() {
		selection.addToSelection(new GraphicalPlace());
		assertThat(selection.getCommonBaseClass(), is(equalTo((Class) GraphicalPlace.class)));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetCommonBaseNodes() {
		selection.addToSelection(new GraphicalPlace());
		selection.addToSelection(new GraphicalState());
		assertThat(selection.getCommonBaseClass(), is(equalTo((Class) GraphicalNode.class)));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetCommonBaseElements() {
		selection.addToSelection(new GraphicalPlace());
		selection.addToSelection(new GraphicalState());
		selection.addToSelection(new GraphicalFlow(mock(GraphicalNode.class), mock(GraphicalNode.class)));
		assertThat(selection.getCommonBaseClass(), is(equalTo((Class) GraphicalElement.class)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
