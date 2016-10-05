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

package uniol.aptgui.commands;

import java.util.HashSet;
import java.util.Set;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;

/**
 * Command that allows to translate (move) graphical elements.
 */
public class TranslateElementsCommand extends Command {

	private final Document<?> document;
	private final Set<GraphicalElement> elements;
	private int deltaX;
	private int deltaY;

	public TranslateElementsCommand(Document<?> document, Set<GraphicalElement> elements) {
		this.document = document;
		this.elements = new HashSet<>(elements);
		this.deltaX = 0;
		this.deltaY = 0;
	}

	/**
	 * Returns true if the translation value is 0 on both axes.
	 *
	 * @return true if this is the identity translation
	 */
	public boolean isIdentity() {
		return deltaX == 0 && deltaY == 0;
	}

	/**
	 * Sets the translation in x and y direction.
	 * <p>
	 * This method does not apply any translation to GraphicalElements but
	 * instead only updates the internal delta x and y variables.
	 *
	 * @param dx
	 *                new x axis translation
	 * @param dy
	 *                new y axis translation
	 */
	public void setTranslation(int dx, int dy) {
		this.deltaX = dx;
		this.deltaY = dy;
	}

	/**
	 * Applies the set translation values to the graphical elements.
	 */
	public void applyTranslation() {
		applyTranslation(deltaX, deltaY);
	}

	/**
	 * Applies the inverse of the set translation values to the graphical
	 * elements.
	 */
	public void unapplyTranslation() {
		applyTranslation(-deltaX, -deltaY);
	}

	@Override
	public String getName() {
		return "Translate Elements";
	}

	@Override
	public void execute() {
		applyTranslation();
	}

	@Override
	public void undo() {
		unapplyTranslation();
	}

	private void applyTranslation(int dx, int dy) {
		for (GraphicalElement elem : elements) {
			if (elem instanceof GraphicalNode) {
				GraphicalNode node = (GraphicalNode) elem;
				node.translate(dx, dy);
			}
			if (elem instanceof GraphicalEdge) {
				GraphicalEdge edge = (GraphicalEdge) elem;
				edge.translateBreakpoints(dx, dy);
			}
		}
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
