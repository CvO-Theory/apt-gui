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

import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;

public class RemoveBreakpointCommand extends Command {

	private final Document<?> document;
	private final GraphicalEdge edge;
	private final int bpIndex;
	private Point oldBreakpoint;

	public RemoveBreakpointCommand(Document<?> document, GraphicalEdge edge, int bpIndex) {
		this.document = document;
		this.edge = edge;
		this.bpIndex = bpIndex;
	}

	@Override
	public String getName() {
		return "Remove Breakpoint";
	}

	@Override
	public void execute() {
		oldBreakpoint = edge.getBreakpoint(bpIndex);
		edge.removeBreakpoint(bpIndex);
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		edge.addBreakpoint(bpIndex, oldBreakpoint);
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
