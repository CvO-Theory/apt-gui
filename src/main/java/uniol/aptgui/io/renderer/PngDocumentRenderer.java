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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.inject.Inject;

import uniol.apt.io.renderer.RenderException;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.Viewport;

/**
 * Renders a document to a PNG bitmap.
 */
public class PngDocumentRenderer implements DocumentRenderer {

	private final RenderingOptions renderingOptions;

	@Inject
	public PngDocumentRenderer(RenderingOptions renderingOptions) {
		this.renderingOptions = renderingOptions;
	}

	@Override
	public void render(Document<?> document, File file) throws RenderException, IOException {
		// Prepare viewport object so that the image has no unnecessary borders.
		Rectangle bounds = document.getBounds();
		int magnification = renderingOptions.getExportBitmapMagnification();
		int imgWidth = bounds.width * magnification;
		int imgHeight = bounds.height * magnification;
		Viewport viewport = new Viewport(document.getViewport());
		viewport.setWidth(imgWidth);
		viewport.setHeight(imgHeight);
		viewport.zoomFit(bounds, renderingOptions.getExportDocumentBorderSize());

		// Create bitmap.
		BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);

		// Configure graphics object for pretty output.
		Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
		imageGraphics.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);

		// Set prepared viewport on document for drawing.
		Viewport original = document.getViewport();
		document.setViewport(viewport);
		document.draw(imageGraphics, renderingOptions);
		document.setViewport(original);

		// Write to file.
		ImageIO.write(bufferedImage, "png", file);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
