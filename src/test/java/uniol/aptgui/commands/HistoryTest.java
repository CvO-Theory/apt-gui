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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.eventbus.EventBus;

import uniol.aptgui.Application;

public class HistoryTest {

	Application application;
	EventBus eventBus;
	History history;
	Command cmd0;
	Command cmd1;
	Command cmd2;

	@Before
	public void setUp() {
		application = mock(Application.class);
		eventBus = mock(EventBus.class);
		history = new History(application, eventBus);
		cmd0 = createUndoableCommandMock();
		cmd1 = createUndoableCommandMock();
		cmd2 = createUndoableCommandMock();
	}

	private Command createUndoableCommandMock() {
		Command cmd = mock(Command.class);
		when(cmd.canUndo()).thenReturn(true);
		return cmd;
	}

	@Test
	public void testExecute() {
		history.execute(cmd0);
		verify(cmd0).execute();
		// TODO No idea why the following does not work correctly
		// verify(eventBus).post(any(HistoryChangedEvent.class));
	}

	@Test
	public void testUndoRedo() {
		history.execute(cmd0);
		assertThat(history.canUndo(), is(equalTo(true)));
		assertThat(history.getNextUndoCommand(), is(equalTo(cmd0)));
		history.undo();
		verify(cmd0).undo();
		assertThat(history.canUndo(), is(equalTo(false)));
		assertThat(history.canRedo(), is(equalTo(true)));
		assertThat(history.getNextRedoCommand(), is(equalTo(cmd0)));
		history.redo();
		verify(cmd0).redo();
		assertThat(history.canRedo(), is(equalTo(false)));
	}

	@Test
	public void testMergeExecute() {
		history.execute(cmd0);
		history.mergeExecute("test", cmd1);
		history.mergeExecute("test", cmd2);
		InOrder inOrder = inOrder(cmd0, cmd1, cmd2);
		inOrder.verify(cmd0).execute();
		inOrder.verify(cmd1).execute();
		inOrder.verify(cmd2).execute();
	}

	@Test
	public void testMergeExecuteUndoRedo() {
		history.execute(cmd0);
		history.mergeExecute("test", cmd1);
		history.mergeExecute("test", cmd2);
		assertThat(history.canUndo(), is(equalTo(true)));
		history.undo();
		assertThat(history.canUndo(), is(equalTo(false)));
		assertThat(history.canRedo(), is(equalTo(true)));
		InOrder inOrder = inOrder(cmd2, cmd1, cmd0);
		inOrder.verify(cmd2).undo();
		inOrder.verify(cmd1).undo();
		inOrder.verify(cmd0).undo();
		reset(cmd0, cmd1, cmd2);
		history.redo();
		assertThat(history.canUndo(), is(equalTo(true)));
		assertThat(history.canRedo(), is(equalTo(false)));
		inOrder = inOrder(cmd0, cmd1, cmd2);
		inOrder.verify(cmd0).redo();
		inOrder.verify(cmd1).redo();
		inOrder.verify(cmd2).redo();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
