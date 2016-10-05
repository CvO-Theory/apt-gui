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

import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.SetMultiplicityCommand;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.SetSimpleAttributeAction;

/**
 * Action that sets the multiplicity of a flow.
 */
@SuppressWarnings("serial")
public class SetMultiplicityAction extends SetSimpleAttributeAction<GraphicalFlow, Integer> {

	@Inject
	public SetMultiplicityAction(Application app, EventBus eventBus) {
		super("Set Multiplicity", "New multiplicity:", app, eventBus);
		putValue(SMALL_ICON, Resource.getIconMultiplicity());
	}

	@Override
	protected Command createCommand(Document<?> document, List<GraphicalFlow> selection, String userInput) {
		try {
			Integer value = Integer.valueOf(userInput);
			return new SetMultiplicityCommand(document, selection, value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	protected Integer getAttribute(GraphicalFlow element) {
		return element.getMultiplicity();
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return GraphicalFlow.class.isAssignableFrom(commonBaseTestClass);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
