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

import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalArc;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

/**
 * Command that removes a set of GraphicalElements from a transition system document and
 * also updates the underlying model.
 */
public class RemoveTsElementsCommand extends Command {

	private final TsDocument document;
	private final Set<GraphicalElement> elements;

	private final TransitionSystem model;
	private final Map<Arc, GraphicalArc> removedArcs;
	private final Map<State, GraphicalState> removedStates;

	public RemoveTsElementsCommand(TsDocument document, Set<GraphicalElement> elements) {
		this.document = document;
		this.elements = new HashSet<>(elements);
		model = document.getModel();
		removedArcs = new HashMap<>();
		removedStates = new HashMap<>();
	}

	@Override
	public void execute() {
		assert document.getGraphicalElements().containsAll(elements);

		for (GraphicalElement elem : elements) {
			if (elem instanceof GraphicalArc) {
				GraphicalArc graphicalArc = (GraphicalArc) elem;
				Arc arc = document.getAssociatedModelElement(graphicalArc);
				removeArc(arc, graphicalArc);
			} else if (elem instanceof GraphicalState) {
				removeState((GraphicalState) elem);
			} else {
				assert false;
			}
		}

		document.fireSelectionChanged();
		document.fireDocumentChanged(true);
	}

	private void removeArc(Arc arc, GraphicalArc graphicalArc) {
		model.removeArc(arc);
		document.remove(graphicalArc);
		removedArcs.put(arc, graphicalArc);
	}

	private void removeState(GraphicalState graphicalState) {
		State state = document.getAssociatedModelElement(graphicalState);

		// Remove all flows that are connected to this node.
		Set<Arc> affectedArcs = new HashSet<>();
		affectedArcs.addAll(model.getPostsetEdges(state));
		affectedArcs.addAll(model.getPresetEdges(state));
		for (Arc arc : affectedArcs) {
			GraphicalArc graphicalArc =  Document.getGraphicalExtension(arc);
			removeArc(arc, graphicalArc);
		}

		// Remove node itself.
		document.remove(graphicalState);
		model.removeState(state);
		removedStates.put(state, graphicalState);
	}

	@Override
	public void undo() {
		// Restore nodes first so that flows referencing them can be added successfully.
		for (Entry<State, GraphicalState> entry : removedStates.entrySet()) {
			State state = model.createState(entry.getKey());
			document.add(entry.getValue(), state);

			if (entry.getValue().isInitialState()) {
				model.setInitialState(state);
			}
		}

		// Restore flows.
		for (Entry<Arc, GraphicalArc> entry : removedArcs.entrySet()) {
			Arc arc = model.createArc(entry.getKey());
			document.add(entry.getValue(), arc);
		}

		document.fireDocumentChanged(true);
	}

	@Override
	public void redo() {
		removedArcs.clear();
		removedStates.clear();
		super.redo();
	}

	@Override
	public String getName() {
		return "Remove";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
