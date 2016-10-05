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

package uniol.aptgui.editor.features.node;

import uniol.aptgui.commands.CreatePlaceCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;

/**
 * Tool that creates places in Petri net documents.
 */
public class CreatePlaceTool extends CreateNodeTool<PnDocument, GraphicalPlace> {

	private final History history;

	public CreatePlaceTool(PnDocument document, History history, EditingOptions editingOptions) {
		super(document, editingOptions);
		this.history = history;
	}

	@Override
	protected GraphicalPlace createGraphicalNode() {
		return new GraphicalPlace();
	}

	@Override
	protected void commitNodeCreation(GraphicalPlace node) {
		history.execute(new CreatePlaceCommand(document, node));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
