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

package uniol.aptgui.swing.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.commands.AddBreakpointCommand;
import uniol.aptgui.commands.Command;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.swing.actions.base.DocumentAction;

/**
 * Action that adds a new breakpoint to an edge.
 */
@SuppressWarnings("serial")
public class AddBreakpointAction extends DocumentAction {

	@Inject
	public AddBreakpointAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Add Breakpoint";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		if (document != null) {
			Set<GraphicalElement> selection = document.getSelection();

			// This action is only enabled if the following is
			// possible.
			GraphicalEdge edge = (GraphicalEdge) selection.iterator().next();

			Point selectionPos = document.getLastSelectionPosition();

			Command cmd = new AddBreakpointCommand(document, edge, selectionPos);
			app.getHistory().execute(cmd);
		}
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return document.getSelection().size() == 1 && GraphicalEdge.class.isAssignableFrom(commonBaseTestClass);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
