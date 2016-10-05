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

import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.edges.GraphicalArc;

public class CreateArcCommand extends Command {

	private final TsDocument tsDocument;
	private final State source;
	private final State target;
	private final String label;
	private final GraphicalArc graphialArc;

	private Arc tsArc;

	public CreateArcCommand(
			TsDocument document,
			State source,
			State target,
			String label,
			GraphicalArc graphialArc
	) {
		this.tsDocument = document;
		this.source = source;
		this.target = target;
		this.label = label;
		this.graphialArc = graphialArc;
	}

	@Override
	public void execute() {
		tsArc = tsDocument.getModel().createArc(source, target, label);
		tsDocument.add(graphialArc, tsArc);
		tsDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		tsDocument.getModel().removeArc(tsArc);
		tsDocument.remove(graphialArc);
		tsDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Create Arc";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
