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

import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.Place;
import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.SetTokensCommand;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.SetSimpleAttributeAction;

/**
 * Action that sets the amount of tokens on a place.
 */
@SuppressWarnings("serial")
public class SetTokensAction extends SetSimpleAttributeAction<GraphicalPlace, Long> {

	@Inject
	public SetTokensAction(Application app, EventBus eventBus) {
		super("Set Tokens", "New token count:", app, eventBus);
		putValue(SMALL_ICON, Resource.getIconTokens());
	}

	@Override
	protected Command createCommand(Document<?> document, List<GraphicalPlace> selection, String userInput) {
		try {
			Long value = Long.valueOf(userInput);
			return new SetTokensCommand((PnDocument) document, selection, value);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	@Override
	protected Long getAttribute(GraphicalPlace element) {
		PnDocument pnDoc = (PnDocument) document;
		Marking marking = pnDoc.getModel().getInitialMarking();
		Place place = pnDoc.getAssociatedModelElement(element);
		return marking.getToken(place).getValue();
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return GraphicalPlace.class.isAssignableFrom(commonBaseTestClass);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
