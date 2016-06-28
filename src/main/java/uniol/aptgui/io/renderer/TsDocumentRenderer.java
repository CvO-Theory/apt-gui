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

import java.io.File;
import java.io.IOException;

import uniol.apt.io.renderer.RenderException;
import uniol.apt.io.renderer.impl.AptLTSRenderer;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.io.FileType;
import uniol.aptgui.io.properties.GraphicalElementTransformer;
import uniol.aptgui.io.properties.PersistentDocumentProperties;

/**
 * Renders transition systems with layout and other visual properties persisted
 * in the file.
 */
public class TsDocumentRenderer implements DocumentRenderer {

	private final GraphicalElementTransformer transformer = new GraphicalElementTransformer();

	@Override
	public void render(Document<?> document, File file) throws RenderException, IOException {
		assert document instanceof TsDocument;
		document.setFile(file);
		document.setFileType(FileType.TRANSITION_SYSTEM);
		document.fireDocumentChanged(false);
		processPersistentExtensions(new PersistentDocumentProperties(document));
		callRenderer((TsDocument) document, file);
	}

	private void processPersistentExtensions(PersistentDocumentProperties pdp) {
		pdp.renderPersistentModelExtensions(transformer);
	}

	private void callRenderer(TsDocument tsDocument, File file) throws RenderException, IOException {
		AptLTSRenderer renderer = new AptLTSRenderer();
		renderer.renderFile(tsDocument.getModel(), file);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
