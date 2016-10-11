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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;

import uniol.aptgui.Application;

public class HistoryTest {

	Application application;
	EventBus eventBus;
	History history;
	Command basicUndoableCommand;

	@Before
	public void setUp() {
		application = mock(Application.class);
		eventBus = mock(EventBus.class);
		history = new History(application, eventBus);
		basicUndoableCommand  = mock(Command.class);
		when(basicUndoableCommand.canUndo()).thenReturn(true);
	}

	@Test
	public void testExecute() {
		history.execute(basicUndoableCommand);
		verify(basicUndoableCommand).execute();
		// TODO No idea why the following does not work correctly
		// verify(eventBus).post(any(HistoryChangedEvent.class));
	}

	@Test
	public void testUndoRedo() {
		history.execute(basicUndoableCommand);
		assertThat(history.canUndo(), is(equalTo(true)));
		assertThat(history.getNextUndoCommand(), is(equalTo(basicUndoableCommand)));
		history.undo();
		verify(basicUndoableCommand).undo();
		assertThat(history.canUndo(), is(equalTo(false)));
		assertThat(history.canRedo(), is(equalTo(true)));
		assertThat(history.getNextRedoCommand(), is(equalTo(basicUndoableCommand)));
		history.redo();
		verify(basicUndoableCommand).redo();
		assertThat(history.canRedo(), is(equalTo(false)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
