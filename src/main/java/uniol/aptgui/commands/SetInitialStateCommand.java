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

import uniol.apt.adt.exception.StructureException;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

public class SetInitialStateCommand extends Command {

	private final TsDocument document;
	private final GraphicalState element;
	private State previousInitialState;

	public SetInitialStateCommand(TsDocument document, GraphicalState element) {
		this.document = document;
		this.element = element;
	}

	@Override
	public String getName() {
		return "Set Initial State";
	}

	@Override
	public void execute() {
		TransitionSystem ts = document.getModel();
		try {
			previousInitialState = ts.getInitialState();
		} catch (StructureException e) {
		}
		State newInitialState = document.getAssociatedModelElement(element);
		ts.setInitialState(newInitialState);
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		TransitionSystem ts = document.getModel();
		ts.setInitialState(previousInitialState);
		document.fireDocumentChanged(true);
	}

	@Override
	public boolean canUndo() {
		return previousInitialState != null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
