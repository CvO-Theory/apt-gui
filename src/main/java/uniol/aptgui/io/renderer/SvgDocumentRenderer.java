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

package uniol.aptgui.io.renderer;

import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;

import com.google.inject.Inject;

import uniol.apt.io.renderer.RenderException;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.Viewport;

/**
 * Renders a document to a SVG vector image.
 */
public class SvgDocumentRenderer implements DocumentRenderer {

	private final RenderingOptions renderingOptions;

	@Inject
	public SvgDocumentRenderer(RenderingOptions renderingOptions) {
		this.renderingOptions = renderingOptions;
	}

	@Override
	public void render(Document<?> document, File file) throws RenderException, IOException {
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		org.w3c.dom.Document xmlDoc = domImpl.createDocument(svgNS, "svg", null);
		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(xmlDoc);
		svgGenerator.setFont(Font.decode("Arial"));

		// Prepare viewport object so that the image has no unnecessary borders.
		Rectangle bounds = document.getBounds();
		Viewport viewport = new Viewport(document.getViewport());
		viewport.setWidth((int) bounds.getWidth());
		viewport.setHeight((int) bounds.getHeight());
		viewport.zoomFit(bounds, renderingOptions.getExportDocumentBorderSize());

		// Draw document with SVG generator.
		Viewport original = document.getViewport();
		document.setViewport(viewport);
		document.draw(svgGenerator, renderingOptions);
		document.setViewport(original);

		svgGenerator.setSVGCanvasSize(bounds.getSize());

		// Save to file.
		svgGenerator.stream(file.getAbsolutePath());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
