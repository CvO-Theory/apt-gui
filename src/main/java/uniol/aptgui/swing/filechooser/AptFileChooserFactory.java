/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2017 Uli Schlachter
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

package uniol.aptgui.swing.filechooser;

import uniol.aptgui.document.Document;

public interface AptFileChooserFactory {
	/**
	 * Creates a new file chooser that allows the user to open documents.
	 *
	 * @return file chooser set-up for open action
	 */
	public AptFileChooser openChooser();

	/**
	 * Creates a new file chooser that allows the user to save the given
	 * document to a file of the correct type.
	 *
	 * @param document
	 *                document to save
	 * @return file chooser set-up for save action
	 */
	public AptFileChooser saveChooser(Document<?> document);

	/**
	 * Creates a new file chooser that allows the user to export documents
	 * into other formats.
	 *
	 * @param document
	 *                document to export
	 * @return file chooser set-up for export action
	 */
	public AptFileChooser exportChooser(Document<?> document);
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
