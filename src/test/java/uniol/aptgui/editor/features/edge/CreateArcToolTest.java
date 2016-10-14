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

package uniol.aptgui.editor.features.edge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uniol.aptgui.editor.features.MouseEventUtil.leftClickAt;
import static uniol.aptgui.editor.features.MouseEventUtil.rightClickAt;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uniol.aptgui.commands.CreateArcCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalArc;
import uniol.aptgui.document.graphical.nodes.GraphicalState;
import uniol.aptgui.editor.EditorView;
import uniol.aptgui.editor.features.edge.CreateArcTool;

/**
 * This test covers most of the other edge tool functionalities as well since
 * they share an abstract base class.
 */
public class CreateArcToolTest {

	CreateArcTool createArcTool;
	TsDocument document;
	History history;
	EditorView editorView;
	GraphicalState s1;
	GraphicalState s2;

	// Gets filled during test by doAnswer stub
	GraphicalArc edge;

	@Before
	public void setUp() {
		s1 = mock(GraphicalState.class);
		s2 = mock(GraphicalState.class);
		document = mock(TsDocument.class);
		when(document.getViewport()).thenReturn(new Viewport());
		when(document.getGraphicalElementAt(new Point(0, 0))).thenReturn(s1);
		when(document.getGraphicalElementAt(new Point(100, 0))).thenReturn(s2);
		when(document.getGraphicalElementAt(new Point(200, 0))).thenReturn(null);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				edge = invocation.getArgument(0);
				return null;
			}
		}).when(document).add(any(GraphicalElement.class));
		history = mock(History.class);
		editorView = mock(EditorView.class);
		when(editorView.showArcLabelInputDialog()).thenReturn("a");
		createArcTool = new CreateArcTool(document, history, editorView);
	}

	@Test
	public void testOnDeactivated() {
		createArcTool.onActivated();
		createArcTool.mouseClicked(leftClickAt(0, 0));
		createArcTool.onDeactivated();
		verify(document).remove(any(GraphicalArc.class));
	}

	@Test
	public void testCreateArc() {
		createArcTool.onActivated();
		createArcTool.mouseClicked(leftClickAt(0, 0));
		verify(document).add(any(GraphicalArc.class));
		createArcTool.mouseClicked(leftClickAt(100, 0));
		verify(history).execute(any(CreateArcCommand.class));
		assertThat((GraphicalState) edge.getSource(), is(equalTo(s1)));
		assertThat((GraphicalState) edge.getTarget(), is(equalTo(s2)));
	}

	@Test
	public void testCreateArcSelfLoop() {
		createArcTool.onActivated();
		createArcTool.mouseClicked(leftClickAt(0, 0));
		createArcTool.mouseClicked(leftClickAt(0, 0));
		verify(history).execute(any(CreateArcCommand.class));
		assertThat((GraphicalState) edge.getSource(), is(equalTo(s1)));
		assertThat((GraphicalState) edge.getTarget(), is(equalTo(s1)));
	}

	@Test
	public void testCreateArcBreakpoints() {
		createArcTool.onActivated();
		createArcTool.mouseClicked(leftClickAt(0, 0));
		createArcTool.mouseClicked(leftClickAt(200, 0));
		createArcTool.mouseClicked(leftClickAt(300, 0));
		createArcTool.mouseClicked(leftClickAt(100, 0));
		verify(history).execute(any(CreateArcCommand.class));
		assertThat(edge.getBreakpoints(), contains(new Point(200, 0), new Point(300, 0)));
	}

	@Test
	public void testCreateArcCancel() {
		createArcTool.onActivated();
		createArcTool.mouseClicked(leftClickAt(0, 0));
		createArcTool.mouseClicked(rightClickAt(100, 0));
		verifyZeroInteractions(history);
		verify(document).remove(edge);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
