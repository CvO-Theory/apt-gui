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

import uniol.apt.adt.ts.State;
import uniol.aptgui.commands.CreateArcCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalArc;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.nodes.GraphicalState;
import uniol.aptgui.editor.EditorView;

/**
 * Tool that allows the user to create a new arc between states in a transition
 * system.
 */
public class CreateArcTool extends CreateEdgeTool<TsDocument, GraphicalArc> {

	private final History history;
	private final EditorView view;

	public CreateArcTool(TsDocument document, History history, EditorView view) {
		super(document);
		this.history = history;
		this.view = view;
	}

	@Override
	protected GraphicalArc createGraphicalEdge(GraphicalNode source, GraphicalNode target) {
		GraphicalArc arc = new GraphicalArc(source, target);
		return arc;
	}

	@Override
	protected void commitEdgeCreation(GraphicalArc edge) {
		String label = view.showArcLabelInputDialog();

		if (label != null) {
			State source = document.getAssociatedModelElement(edge.getSource());
			State target = document.getAssociatedModelElement(edge.getTarget());
			CreateArcCommand cmd = new CreateArcCommand(document, source, target, label, edge);
			history.execute(cmd);
		}
	}

	@Override
	protected boolean isValidTarget(GraphicalElement target) {
		return graphicalSource instanceof GraphicalState && target instanceof GraphicalState;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
