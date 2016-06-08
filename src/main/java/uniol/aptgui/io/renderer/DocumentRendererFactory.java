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

import uniol.aptgui.io.FileType;

/**
 * Factory that returns a document renderer for a given file type.
 */
public class DocumentRendererFactory {

	/**
	 * Factory method that returns a DocumentRenderer implementation for a
	 * given file type.
	 *
	 * @param fileType
	 *                file type the document should be saved in
	 * @return fitting DocumentRenderer implementation
	 */
	public static DocumentRenderer get(FileType fileType) {
		switch (fileType) {
		case PETRI_NET:
			return new PnDocumentRenderer();
		case PETRI_NET_ONLY_STRUCTURE:
			return new PnStructureDocumentRenderer();
		case TRANSITION_SYSTEM:
			return new TsDocumentRenderer();
		case TRANSITION_SYSTEM_ONLY_STRUCTURE:
			return new TsStructureDocumentRenderer();
		default:
			throw new AssertionError("Requested DocumentRenderer for unsupported file type: " + fileType);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
