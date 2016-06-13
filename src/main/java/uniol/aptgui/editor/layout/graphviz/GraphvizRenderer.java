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

import java.io.IOException;
import java.io.Writer;

import uniol.apt.util.Pair;

/**
 * Write a GraphvizDocument into the DOT file format.
 */
public class GraphvizRenderer {

	private static final String FILE_PREAMBLE = "digraph G {\n  splines=polyline;\n  node [fixedsize=true, width=60, height=60];\n";
	private static final String FILE_POSTAMBLE = "}";

	public void writeDot(Writer writer, GraphvizDocument document) throws IOException {
		writer.append(FILE_PREAMBLE);

		// Write all nodes
		for (String nodeID : document.getNodeIDs()) {
			writer.append("\"");
			writer.append(nodeID);
			writer.append("\";\n");
		}

		// Write all edges
		for (Pair<String, String> edge : document.getEdgeIDs()) {
			String source = edge.getFirst();
			String target = edge.getSecond();

			writer.append("\"");
			writer.append(source);
			writer.append("\" -> \"");
			writer.append(target);
			writer.append("\";\n");
		}

		writer.append(FILE_POSTAMBLE);
	}
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
