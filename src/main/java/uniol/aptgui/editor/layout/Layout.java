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

package uniol.aptgui.editor.layout;

import uniol.aptgui.document.Document;

/**
 * Interface for layout algorithms that can be applied to documents. A layout
 * algorithm is supposed to arrange the graphical elements of the document
 * within the given layout area.
 */
public interface Layout {

	/**
	 * Applies this layout algorithm to the given document.
	 *
	 * @param document
	 *                the document that should be laid out
	 * @param x0
	 *                lower x-axis bound of layout area
	 * @param y0
	 *                lower y-axis bound of layout area
	 * @param x1
	 *                upper x-axis bound of layout area
	 * @param y1
	 *                upper y-axis bound of layout area
	 * @throws LayoutException
	 *                 Wrapper around exceptions that may occur during the
	 *                 layout process
	 */
	public void applyTo(Document<?> document, int x0, int y0, int x1, int y1) throws LayoutException;

	/**
	 * Returns a user-friendly name of this layout algorithm.
	 *
	 * @return a user-friendly name of this layout algorithm
	 */
	public String getName();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
