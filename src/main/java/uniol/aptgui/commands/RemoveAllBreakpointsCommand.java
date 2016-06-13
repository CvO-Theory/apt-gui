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

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;

public class RemoveAllBreakpointsCommand extends Command {

	private final Document<?> document;
	private final GraphicalEdge edge;
	private List<Point> oldBreakpoints;

	public RemoveAllBreakpointsCommand(Document<?> document, GraphicalEdge edge) {
		this.document = document;
		this.edge = edge;
	}

	@Override
	public String getName() {
		return "Remove All Breakpoints";
	}

	@Override
	public void execute() {
		oldBreakpoints = new ArrayList<>(edge.getBreakpoints());
		edge.removeAllBreakpoints();
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		for (Point bp : oldBreakpoints) {
			edge.addBreakpoint(bp);
		}
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
