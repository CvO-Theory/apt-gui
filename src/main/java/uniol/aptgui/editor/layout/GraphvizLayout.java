/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2016 Jonas Prellberg
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

package uniol.aptgui.editor.layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import java.awt.Point;

import uniol.apt.io.parser.ParseException;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.layout.graphviz.GraphvizDocument;
import uniol.aptgui.editor.layout.graphviz.GraphvizParser;
import uniol.aptgui.editor.layout.graphviz.GraphvizRenderer;

/**
 * Simple layout algorithm that uses Graphviz to layout items.
 */
public class GraphvizLayout implements Layout {

	private final Application application;
	private final String graphvizCommand;

	public GraphvizLayout(Application application, String graphvizCommand) {
		this.application = application;
		this.graphvizCommand = graphvizCommand;
	}

	@Override
	public void applyTo(Document<?> document, int x0, int y0, int x1, int y1) {
		// TODO: This ignores EditingOptions. How could they be integrated?
		GraphvizDocument graphvizDoc = new GraphvizDocument(document);
		try {
			doLayout(graphvizDoc);
		} catch (IOException | ParseException e) {
			application.getMainWindow().showException("Layout exception", e);
		}
	}

	@Override
	public String getName() {
		return "Graphviz";
	}

	private void doLayout(GraphvizDocument document) throws IOException, ParseException {
		Process graphviz = new ProcessBuilder(graphvizCommand, "-Tplain")
			.redirectError(ProcessBuilder.Redirect.INHERIT)
			.start();
		try {
			// Write the input into DOT
			try (OutputStream stream = graphviz.getOutputStream();
					Writer osw = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
					Writer buf = new BufferedWriter(osw)) {
				new GraphvizRenderer().writeDot(buf, document);
				buf.close();
			}

			// Parse its output
			GraphvizParser parser;
			try (InputStream stream = graphviz.getInputStream();
					Reader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
					BufferedReader buf = new BufferedReader(isr)) {
				parser = new GraphvizParser(buf);
			}
			applyLayout(document, parser);
		} finally {
			graphviz.destroy();
		}
	}

	private void applyLayout(GraphvizDocument document, GraphvizParser parser) throws ParseException {
		int height = parser.getHeight();
		for (GraphvizParser.Node node : parser.getNodes()) {
			GraphicalNode graphical = document.getNodeByID(node.getId());
			if (graphical == null) {
				throw new ParseException("Could not find node with id " + node.getId());
			}
			graphical.setCenter(transformPoint(height, node.getPosition()));
		}

		for (GraphvizParser.Edge edge : parser.getEdges()) {
			GraphicalEdge graphical = document.getEdgeByIDs(edge.getSource(), edge.getTarget());
			if (graphical == null) {
				throw new ParseException("Could not find edge from " + edge.getSource() + " to " + edge.getTarget());
			}

			// First, remove all breakpoints
			while (graphical.getBreakpointCount() != 0) {
				graphical.removeBreakpoint(0);
			}

			// Then, add new ones
			for (Point point : edge.getBreakpoints()) {
				graphical.addBreakpoint(transformPoint(height, point));
			}

			// And finally remove unnecessary breakpoints that Graphviz might have added
			for (int i = 0; i < graphical.getBreakpointCount(); i++) {
				if (!graphical.isBreakpointNecessary(i)) {
					graphical.removeBreakpoint(i);
					i--;
				}
			}
		}
	}

	/**
	 * Transforms a point from the graphviz coordinate system (origin
	 * bottom-left) to the application coordinate system (origin top-left).
	 *
	 * @param height
	 *                graphviz coordinate system height
	 * @param point
	 *                graphviz point
	 * @return application point
	 */
	private Point transformPoint(int height, Point point) {
		return new Point((int) point.getX(), height - (int) point.getY());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
