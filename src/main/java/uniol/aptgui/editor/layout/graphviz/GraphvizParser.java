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

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uniol.apt.io.parser.ParseException;

/**
 * Parse dot's -Tplain output.
 */
public class GraphvizParser {

	public static class Node {
		private final String id;
		private final Point position;

		public Node(String id, Point position) {
			this.id = id;
			this.position = position;
		}

		public String getId() {
			return id;
		}

		public Point getPosition() {
			return position;
		}
	}

	public static class Edge {
		private final String source;
		private final String target;
		private final List<Point> breakpoints;

		public Edge(String source, String target, List<Point> breakpoints) {
			this.source = source;
			this.target = target;
			this.breakpoints = breakpoints;
		}

		public String getSource() {
			return source;
		}

		public String getTarget() {
			return target;
		}

		public List<Point> getBreakpoints() {
			return breakpoints;
		}
	}

	private int width;
	private int height;
	private final Set<Node> nodes = new HashSet<>();
	private final Set<Edge> edges = new HashSet<>();

	public GraphvizParser(BufferedReader reader) throws IOException, ParseException {
		String line = "";

		while (!line.equals("stop")) {
			line = reader.readLine();
			if (line == null) {
				throw new ParseException("Premature end of input");
			}

			String[] words = line.split(" ");
			switch (words[0]) {
				case "stop":
					break;

				case "graph":
					handleGraph(words);
					break;

				case "node":
					handleNode(words);
					break;

				case "edge":
					handleEdge(words);
					break;
			}
		}
	}

	private void handleGraph(String[] words) throws ParseException {
		if (words.length < 4) {
			throw new ParseException("Invalid 'graph' line");
		}
		width = parseDoubleAndRound(words[2]);
		height = parseDoubleAndRound(words[3]);
	}

	private void handleNode(String[] words) throws ParseException {
		if (words.length < 4) {
			throw new ParseException("Invalid 'node' line");
		}
		String node = words[1];
		int x = parseDoubleAndRound(words[2]);
		int y = parseDoubleAndRound(words[3]);
		nodes.add(new Node(node, new Point(x, y)));
	}

	private void handleEdge(String[] words) throws ParseException {
		if (words.length < 6) {
			throw new ParseException("Invalid 'edge' line");
		}
		String source = words[1];
		String target = words[2];
		int numPoints = parseInteger(words[3]);

		if (words.length < 4 + numPoints * 2) {
			throw new ParseException("Invalid 'edge' line");
		}

		List<Point> breakpoints = new ArrayList<>(numPoints);
		for (int i = 0; i < numPoints; i++) {
			int x = parseDoubleAndRound(words[4 + i*2]);
			int y = parseDoubleAndRound(words[4 + i*2 + 1]);
			breakpoints.add(new Point(x, y));
		}

		edges.add(new Edge(source, target, breakpoints));
	}


	private static int parseInteger(String s) throws ParseException {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new ParseException(e);
		}
	}

	private static double parseDouble(String s) throws ParseException {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new ParseException(e);
		}
	}

	private static int parseDoubleAndRound(String s) throws ParseException {
		long result = Math.round(parseDouble(s));
		if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) {
			throw new ParseException("Value out of range for integer: " + s);
		}
		return (int) result;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Set<Node> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	public Set<Edge> getEdges() {
		return Collections.unmodifiableSet(edges);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
