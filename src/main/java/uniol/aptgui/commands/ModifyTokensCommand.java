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

/**
 * Modifies the token count of a place by adding a (positive or negative) value
 * to it.
 */
public class ModifyTokensCommand extends Command {

	private final PnDocument pnDocument;
	private final GraphicalPlace graphicalPlace;
	private final Place place;
	private final long modification;

	public ModifyTokensCommand(PnDocument pnDocument, GraphicalPlace graphicalPlace, long modification) {
		this.pnDocument = pnDocument;
		this.graphicalPlace = graphicalPlace;
		this.place = pnDocument.getAssociatedModelElement(graphicalPlace);
		this.modification = modification;
	}

	@Override
	public void execute() {
		long newValue = place.getInitialToken().getValue() + modification;
		place.setInitialToken(newValue);
		graphicalPlace.setTokens(newValue);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		long newValue = place.getInitialToken().getValue() - modification;
		place.setInitialToken(newValue);
		graphicalPlace.setTokens(newValue);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Modify Token Count by " + modification;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
