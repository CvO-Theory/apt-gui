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

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;

/**
 * Command that sets the drawing color for graphical elements.
 */
public class SetColorCommand extends Command {

	private final Document<?> document;
	private final Set<GraphicalElement> selection;
	private final Color newColor;
	private Map<GraphicalElement, Color> oldColors;

	public SetColorCommand(Document<?> document, Set<GraphicalElement> selection, Color newColor) {
		this.document = document;
		this.selection = new HashSet<>(selection);
		this.newColor = newColor;
	}

	@Override
	public String getName() {
		return "Set Color";
	}

	@Override
	public void execute() {
		oldColors = new HashMap<>();
		for (GraphicalElement elem : selection) {
			oldColors.put(elem, elem.getColor());
			elem.setColor(newColor);
			// Make color change immediately visible even if user's cursor is still above the element.
			elem.setHighlighted(false);
		}
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		for (GraphicalElement elem : selection) {
			elem.setColor(oldColors.get(elem));
		}
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
