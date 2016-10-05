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

package uniol.aptgui.document;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.Node;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Token;
import uniol.apt.adt.pn.Transition;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.document.graphical.nodes.GraphicalTransition;

public class PnDocument extends Document<PetriNet> {

	public PnDocument(PetriNet pn) {
		setModel(pn);
		setName(pn.getName());

		Map<Node, GraphicalNode> nodeMap = new HashMap<>();
		for (Place place : pn.getPlaces()) {
			GraphicalPlace elem = new GraphicalPlace();
			elem.setId(place.getId());
			nodeMap.put(place, elem);
			add(elem, place);
		}
		for (Transition transition : pn.getTransitions()) {
			GraphicalTransition elem = new GraphicalTransition();
			elem.setId(transition.getId());
			nodeMap.put(transition, elem);
			add(elem, transition);
		}
		for (Flow flow : pn.getEdges()) {
			GraphicalNode source = nodeMap.get(flow.getSource());
			GraphicalNode target = nodeMap.get(flow.getTarget());
			GraphicalFlow elem = new GraphicalFlow(source, target);
			add(elem, flow);
		}
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		getModel().setName(name);
	}

	@Override
	public void draw(Graphics2D graphics, RenderingOptions renderingOptions) {
		Marking marking = getModel().getInitialMarking();
		for (Place place : getModel().getPlaces()) {
			GraphicalPlace elem = getGraphicalExtension(place);
			Token token = marking.getToken(place);
			elem.setTokens(token.getValue());
		}
		for (Transition transition : getModel().getTransitions()) {
			GraphicalTransition elem = getGraphicalExtension(transition);
			elem.setLabel(transition.getLabel());
		}
		for (Flow flow : getModel().getEdges()) {
			GraphicalFlow elem = getGraphicalExtension(flow);
			elem.setMultiplicity(flow.getWeight());
		}
		super.draw(graphics, renderingOptions);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
