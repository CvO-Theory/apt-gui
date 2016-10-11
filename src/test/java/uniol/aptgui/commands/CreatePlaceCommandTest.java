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

package uniol.aptgui.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;

public class CreatePlaceCommandTest {

	CreatePlaceCommand cmd;
	PetriNet model;
	PnDocument document;
	GraphicalPlace graphical;

	@Before
	public void setUp() {
		model = new PetriNet();
		document = new PnDocument(model);
		graphical = mock(GraphicalPlace.class);
		cmd = new CreatePlaceCommand(document, graphical);
	}

	@Test
	public void testExecute() {
		cmd.execute();
		assertThat(model.getNodes(), hasSize(1));
		Place s = model.getPlaces().iterator().next();
		verify(graphical).setId(s.getId());
		assertThat(document.getGraphicalElements(), hasSize(1));
		GraphicalPlace gs = (GraphicalPlace) document.getGraphicalElements().iterator().next();
		assertThat(gs, is(equalTo(graphical)));
		assertThat((Place) document.getAssociatedModelElement(gs), is(equalTo(s)));
	}

	@Test
	public void testUndo() {
		cmd.execute();
		cmd.undo();
		assertThat(model.getNodes(), is(empty()));
		assertThat(document.getGraphicalElements(), is(empty()));
	}

	@Test
	public void testRedo() {
		cmd.execute();
		Place original = model.getPlaces().iterator().next();
		cmd.undo();
		cmd.redo();
		assertThat(model.getNodes(), hasSize(1));
		Place redo = model.getPlaces().iterator().next();
		assertThat(original.getId(), is(equalTo(redo.getId())));
		assertThat(document.getGraphicalElements(), hasSize(1));
		GraphicalPlace gs = (GraphicalPlace) document.getGraphicalElements().iterator().next();
		assertThat(gs, is(equalTo(graphical)));
		assertThat((Place) document.getAssociatedModelElement(gs), is(equalTo(redo)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
