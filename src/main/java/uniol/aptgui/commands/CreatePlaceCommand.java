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

import uniol.apt.adt.pn.Place;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;

public class CreatePlaceCommand extends Command {

	private final PnDocument pnDocument;
	private final GraphicalPlace graphicalPlace;
	private Place pnPlace;

	public CreatePlaceCommand(PnDocument pnDocument, GraphicalPlace place) {
		this.pnDocument = pnDocument;
		this.graphicalPlace = place;
	}

	@Override
	public void execute() {
		if (pnPlace != null) {
			pnPlace = pnDocument.getModel().createPlace(pnPlace);
		} else {
			pnPlace = pnDocument.getModel().createPlace();
		}
		graphicalPlace.setId(pnPlace.getId());
		pnDocument.add(graphicalPlace, pnPlace);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		pnDocument.getModel().removePlace(pnPlace);
		pnDocument.remove(graphicalPlace);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Create Place";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
