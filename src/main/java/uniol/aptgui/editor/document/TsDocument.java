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

package uniol.aptgui.editor.document;

import java.util.HashMap;
import java.util.Map;

import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;

public class TsDocument extends Document<TransitionSystem> {

	public TsDocument(TransitionSystem ts) {
		setModel(ts);
		setTitle(ts.getName());

		Map<State, GraphicalState> stateMap = new HashMap<>();
		for (State state : ts.getNodes()) {
			GraphicalState elem = new GraphicalState();
			state.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			stateMap.put(state, elem);
			add(elem, state);
		}
		for (Arc arc : ts.getEdges()) {
			GraphicalState source = stateMap.get(arc.getSource());
			GraphicalState target = stateMap.get(arc.getTarget());
			GraphicalArc elem = new GraphicalArc(source, target);
			arc.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			add(elem, arc);
		}
	}
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
