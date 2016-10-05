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

import java.util.ArrayList;
import java.util.List;

import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Token;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;

/**
 * Command that sets the token count for a selection of GraphicalPlaces.
 */
public class SetTokensCommand extends Command {

	private final PnDocument document;
	private final List<GraphicalPlace> elements;
	private final long newTokenCount;
	private List<Long> oldTokenCounts;

	public SetTokensCommand(PnDocument document, List<GraphicalPlace> elements, long newTokenCount) {
		this.document = document;
		this.elements = elements;
		this.newTokenCount = newTokenCount;
	}

	@Override
	public String getName() {
		return "Set Tokens";
	}

	protected List<Long> getAttributeValues(List<GraphicalPlace> elements) {
		Marking marking = document.getModel().getInitialMarking();
		List<Long> oldValues = new ArrayList<>();
		for (GraphicalPlace graphicalPlace : elements) {
			Place place = document.getAssociatedModelElement(graphicalPlace);
			Token token = marking.getToken(place);
			oldValues.add(token.getValue());
		}
		return oldValues;
	}

	@Override
	public void execute() {
		oldTokenCounts = getAttributeValues(elements);
		Marking marking = document.getModel().getInitialMarking();
		for (GraphicalPlace graphicalPlace : elements) {
			Place place = document.getAssociatedModelElement(graphicalPlace);
			marking = marking.setTokenCount(place, Token.valueOf(newTokenCount));
		}
		document.getModel().setInitialMarking(marking);
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		Marking marking = document.getModel().getInitialMarking();
		for (int i = 0; i < elements.size(); i++) {
			GraphicalPlace graphicalPlace = elements.get(i);
			Long oldTokenCount = oldTokenCounts.get(i);
			Place place = document.getAssociatedModelElement(graphicalPlace);
			marking = marking.setTokenCount(place, Token.valueOf(oldTokenCount));
		}
		document.getModel().setInitialMarking(marking);
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
