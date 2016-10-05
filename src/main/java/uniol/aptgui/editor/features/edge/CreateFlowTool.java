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

package uniol.aptgui.editor.features.edge;

import java.awt.Cursor;

import uniol.apt.adt.pn.Node;
import uniol.aptgui.commands.CreateFlowCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.document.graphical.nodes.GraphicalTransition;
import uniol.aptgui.swing.Resource;

/**
 * Tool that allows the user to create a new flow between nodes in a Petri net.
 */
public class CreateFlowTool extends CreateEdgeTool<PnDocument, GraphicalFlow> {

	private final Cursor cursor = Resource.getCursorCreateEdge();
	private final History history;

	public CreateFlowTool(PnDocument document, History history) {
		super(document);
		this.history = history;
	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}

	@Override
	protected GraphicalFlow createGraphicalEdge(GraphicalNode source, GraphicalNode target) {
		GraphicalFlow flow = new GraphicalFlow(source, target);
		return flow;
	}

	@Override
	protected void commitEdgeCreation(GraphicalFlow edge) {
		Node source = document.getAssociatedModelElement(edge.getSource());
		Node target = document.getAssociatedModelElement(edge.getTarget());
		history.execute(new CreateFlowCommand(document, source, target, 1, edge));
	}

	@Override
	protected boolean isValidTarget(GraphicalElement target) {
		boolean typesCorrect = (graphicalSource instanceof GraphicalPlace && target instanceof GraphicalTransition)
				    || (graphicalSource instanceof GraphicalTransition && target instanceof GraphicalPlace);
		return graphicalSource != target && typesCorrect;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
