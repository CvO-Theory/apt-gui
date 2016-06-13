/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2016 Uli Schlachter
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

package uniol.aptgui.editor.layout.graphviz;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import uniol.apt.util.Pair;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

/**
 * Document state for GraphViz interaction.
 */
public class GraphvizDocument {

	// Mapping between nodes and their names as given to graphviz
	private final BidiMap<GraphicalNode, String> nodes = new DualHashBidiMap<>();

	// Mapping between node names and the (unique) edge connecting these nodes
	private final Map<Pair<String, String>, GraphicalEdge> edges = new HashMap<>();

	public GraphvizDocument(Document<?> document) {
		// Assign names to nodes
		int counter = 0;
		for (GraphicalElement elem : document.getGraphicalElements()) {
			if (elem instanceof GraphicalNode) {
				GraphicalNode node = (GraphicalNode) elem;
				String id = String.format("s%d", counter++);
				nodes.put(node, id);
			}
		}

		// Save edges
		for (GraphicalElement elem : document.getGraphicalElements()) {
			if (elem instanceof GraphicalEdge) {
				GraphicalEdge edge = (GraphicalEdge) elem;
				String source = nodes.get(edge.getSource());
				String target = nodes.get(edge.getTarget());
				assert source != null;
				assert target != null;

				Pair<String, String> pair = new Pair<>(source, target);
				Object old = edges.put(pair, edge);
				assert old == null;
			}
		}
	}

	public GraphicalNode getNodeByID(String id) {
		return nodes.getKey(id);
	}

	public String getNameOfNode(GraphicalNode node) {
		return nodes.get(node);
	}

	public Collection<String> getNodeIDs() {
		return nodes.values();
	}

	public GraphicalEdge getEdgeByIDs(String source, String target) {
		return edges.get(new Pair<String, String>(source, target));
	}

	public Set<Pair<String, String>> getEdgeIDs() {
		Set<Pair<String, String>> result = new HashSet<>();
		for (GraphicalEdge edge : edges.values()) {
			String source = getNameOfNode(edge.getSource());
			String target = getNameOfNode(edge.getTarget());
			assert source != null;
			assert target != null;

			Pair<String, String> pair = new Pair<>(source, target);
			boolean added = result.add(pair);
			assert added;
		}
		return result;
	}
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
