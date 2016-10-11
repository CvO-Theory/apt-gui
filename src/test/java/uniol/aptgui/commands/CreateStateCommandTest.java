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

import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

public class CreateStateCommandTest {

	CreateStateCommand cmd;
	TransitionSystem model;
	TsDocument document;
	GraphicalState graphical;

	@Before
	public void setUp() {
		model = new TransitionSystem();
		document = new TsDocument(model);
		graphical = mock(GraphicalState.class);
		cmd = new CreateStateCommand(document, graphical);
	}

	@Test
	public void testExecute() {
		cmd.execute();
		assertThat(model.getNodes(), hasSize(1));
		State s = model.getNodes().iterator().next();
		assertThat(model.getInitialState(), is(equalTo(s)));
		verify(graphical).setId(s.getId());
		assertThat(document.getGraphicalElements(), hasSize(1));
		GraphicalState gs = (GraphicalState) document.getGraphicalElements().iterator().next();
		assertThat(gs, is(equalTo(graphical)));
		assertThat((State) document.getAssociatedModelElement(gs), is(equalTo(s)));
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
		State original = model.getNodes().iterator().next();
		cmd.undo();
		cmd.redo();
		assertThat(model.getNodes(), hasSize(1));
		State redo = model.getNodes().iterator().next();
		assertThat(original.getId(), is(equalTo(redo.getId())));
		assertThat(document.getGraphicalElements(), hasSize(1));
		GraphicalState gs = (GraphicalState) document.getGraphicalElements().iterator().next();
		assertThat(gs, is(equalTo(graphical)));
		assertThat((State) document.getAssociatedModelElement(gs), is(equalTo(redo)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
