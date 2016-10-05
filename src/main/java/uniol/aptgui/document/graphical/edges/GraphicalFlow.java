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

package uniol.aptgui.document.graphical.edges;

import uniol.aptgui.document.graphical.nodes.GraphicalNode;

public class GraphicalFlow extends GraphicalEdge {

	protected Integer multiplicity;

	public GraphicalFlow(GraphicalNode source, GraphicalNode target) {
		super(source, target);
	}

	public Integer getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Integer multiplicity) {
		this.multiplicity = multiplicity;
		setLabel(String.valueOf(multiplicity));
	}

	@Override
	public String toUserString() {
		return "Flow from " + source.toUserString() + " to " + target.toUserString();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
