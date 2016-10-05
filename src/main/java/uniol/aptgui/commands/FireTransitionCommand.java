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

import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Transition;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalTransition;

public class FireTransitionCommand extends Command {

	private final PnDocument pnDocument;
	private final GraphicalTransition graphicalTransition;
	private Marking oldMarking;
	private Transition transition;

	public FireTransitionCommand(PnDocument pnDocument, GraphicalTransition transition) {
		this.pnDocument = pnDocument;
		this.graphicalTransition = transition;
	}

	@Override
	public void execute() {
		PetriNet pn = pnDocument.getModel();
		oldMarking = pn.getInitialMarking();
		transition = pnDocument.getAssociatedModelElement(graphicalTransition);

		assert transition.isFireable(oldMarking);
		Marking newMarking = transition.fire(oldMarking);
		pn.setInitialMarking(newMarking);

		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		PetriNet pn = pnDocument.getModel();
		pn.setInitialMarking(oldMarking);

		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Fire Transition";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
