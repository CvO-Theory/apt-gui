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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Node;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;

/**
 * Command that removes a set of GraphicalElements from a Petri net document and
 * also updates the underlying model.
 */
public class RemovePnElementsCommand extends Command {

	private final PnDocument document;
	private final Set<GraphicalElement> elements;

	private final PetriNet model;
	private final Map<Flow, GraphicalFlow> removedFlows;
	private final Map<Node, GraphicalNode> removedNodes;

	public RemovePnElementsCommand(PnDocument document, Set<GraphicalElement> elements) {
		this.document = document;
		this.elements = new HashSet<>(elements);
		model = document.getModel();
		removedFlows = new HashMap<>();
		removedNodes = new HashMap<>();
	}

	@Override
	public void execute() {
		assert document.getGraphicalElements().containsAll(elements);

		for (GraphicalElement elem : elements) {
			if (elem instanceof GraphicalFlow) {
				GraphicalFlow graphicalFlow = (GraphicalFlow) elem;
				Flow flow = document.getAssociatedModelElement(graphicalFlow);
				removeFlow(flow, graphicalFlow);
			} else if (elem instanceof GraphicalNode) {
				removeNode((GraphicalNode) elem);
			} else {
				assert false;
			}
		}

		document.fireSelectionChanged();
		document.fireDocumentChanged(true);
	}

	private void removeFlow(Flow flow, GraphicalFlow graphicalFlow) {
		model.removeFlow(flow);
		document.remove(graphicalFlow);
		removedFlows.put(flow, graphicalFlow);
	}

	private void removeNode(GraphicalNode graphicalNode) {
		Node node = document.getAssociatedModelElement(graphicalNode);

		// Remove all flows that are connected to this node.
		Set<Flow> affectedFlows = new HashSet<>();
		affectedFlows.addAll(model.getPostsetEdges(node));
		affectedFlows.addAll(model.getPresetEdges(node));
		for (Flow flow : affectedFlows) {
			GraphicalFlow graphicalFlow = Document.getGraphicalExtension(flow);
			removeFlow(flow, graphicalFlow);
		}

		// Remove node itself.
		document.remove(graphicalNode);
		model.removeNode(node);
		removedNodes.put(node, graphicalNode);
	}

	@Override
	public void undo() {
		// Restore nodes first so that flows referencing them can be added successfully.
		for (Entry<Node, GraphicalNode> entry : removedNodes.entrySet()) {
			GraphicalNode graphicalNode = entry.getValue();
			if (entry.getKey() instanceof Transition) {
				Transition transition = (Transition) entry.getKey();
				transition = model.createTransition(transition);
				document.add(graphicalNode, transition);
			} else if (entry.getKey() instanceof Place) {
				Place place = (Place) entry.getKey();
				place = model.createPlace(place);
				document.add(graphicalNode, place);
			} else {
				assert false;
			}
		}

		// Restore flows.
		for (Entry<Flow, GraphicalFlow> entry : removedFlows.entrySet()) {
			Flow flow = model.createFlow(entry.getKey());
			document.add(entry.getValue(), flow);
		}

		document.fireDocumentChanged(true);
	}

	@Override
	public void redo() {
		removedFlows.clear();
		removedNodes.clear();
		super.redo();
	}

	@Override
	public String getName() {
		return "Remove";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
