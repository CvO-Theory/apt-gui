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

package uniol.aptgui.swing.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.google.common.io.Files;

import uniol.apt.io.parser.Parser;

/**
 * File filter that accepts files with the extension specified by a parser.
 */
public class ParserFileFilter extends FileFilter {

	private final String name;
	private final Parser<?> parser;

	/**
	 * Constructs a new file filter that accepts files with the extension
	 * specified by the given parser. The filter is prefixed with the given
	 * name for easier identification by the user.
	 *
	 * @param name
	 *                filter name
	 * @param parser
	 *                parser whose file extensions will be accepted
	 */
	public ParserFileFilter(String name, Parser<?> parser) {
		this.name = name;
		this.parser = parser;
	}

	/**
	 * Returns the default extension string of this file filter.
	 *
	 * @return the default extension string of this file filter
	 */
	public String getDefaultExtension() {
		return parser.getFileExtensions().get(0);
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String ext = Files.getFileExtension(f.getAbsolutePath());
		return parser.getFileExtensions().contains(ext);
	}

	@Override
	public String getDescription() {
		return String.format("%s %s %s", parser.getFormat(), name, parser.getFileExtensions());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
