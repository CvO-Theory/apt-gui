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
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

public class CreateStateCommand extends Command {

	private final TsDocument tsDocument;
	private final GraphicalState graphicalState;
	private State tsState;

	public CreateStateCommand(TsDocument document, GraphicalState graphicalState) {
		this.tsDocument = document;
		this.graphicalState = graphicalState;
	}

	private boolean hasInitialState() {
		// Of course there is no "hasInitialState()"...
		try {
			tsDocument.getModel().getInitialState();
			return true;
		} catch (StructureException e) {
			return false;
		}
	}

	@Override
	public void execute() {
		if (tsState != null) {
			tsState = tsDocument.getModel().createState(tsState);
		} else {
			tsState = tsDocument.getModel().createState();
		}
		if (!hasInitialState()) {
			tsDocument.getModel().setInitialState(tsState);
		}
		graphicalState.setId(tsState.getId());
		tsDocument.add(graphicalState, tsState);
		tsDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		tsDocument.getModel().removeState(tsState);
		tsDocument.remove(graphicalState);
		tsDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Create State";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
