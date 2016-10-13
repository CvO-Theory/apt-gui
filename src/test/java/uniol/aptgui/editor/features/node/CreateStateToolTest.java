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

package uniol.aptgui.editor.features.node;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uniol.aptgui.editor.features.MouseEventUtil.leftClickAt;

import org.junit.Before;
import org.junit.Test;

import uniol.aptgui.commands.CreateStateCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

/**
 * This test covers most of the other node tool functionalities as well since
 * they share an abstract base class.
 */
public class CreateStateToolTest {

	CreateStateTool createStateTool;
	TsDocument document;
	History history;

	@Before
	public void setUp() {
		document = mock(TsDocument.class);
		when(document.getViewport()).thenReturn(new Viewport());
		history = mock(History.class);
		EditingOptions eo = mock(EditingOptions.class);
		when(eo.isSnapToGridEnabled()).thenReturn(false);
		createStateTool = new CreateStateTool(document, history, eo);
	}

	@Test
	public void testOnActivated() {
		createStateTool.onActivated();
		verify(document).add(any(GraphicalState.class));
	}

	@Test
	public void testOnDeactivated() {
		createStateTool.onActivated();
		createStateTool.onDeactivated();
		verify(document).remove(any(GraphicalState.class));
	}

	@Test
	public void testCreateState() {
		createStateTool.onActivated();
		createStateTool.mouseClicked(leftClickAt(2, 3));
		verify(history).execute(any(CreateStateCommand.class));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
