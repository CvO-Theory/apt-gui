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

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Node;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.graphical.edges.GraphicalFlow;

public class CreateFlowCommand extends Command {

	private final PnDocument pnDocument;
	private final Node source;
	private final Node target;
	private final int multiplicity;
	private final GraphicalFlow graphialFlow;

	private Flow pnFlow;

	public CreateFlowCommand(
			PnDocument document,
			Node source,
			Node target,
			int multiplicity,
			GraphicalFlow graphialFlow
	) {
		this.pnDocument = document;
		this.source = source;
		this.target = target;
		this.multiplicity = multiplicity;
		this.graphialFlow = graphialFlow;
	}

	@Override
	public void execute() {
		pnFlow = pnDocument.getModel().createFlow(source, target, multiplicity);
		pnDocument.add(graphialFlow, pnFlow);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		pnDocument.getModel().removeFlow(pnFlow);
		pnDocument.remove(graphialFlow);
		pnDocument.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Create Flow";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
