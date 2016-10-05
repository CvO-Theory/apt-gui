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

import java.awt.Point;

import com.google.inject.Inject;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.features.ToolUtil;

/**
 * Simple layout algorithm that randomly scatters nodes across the layout area.
 */
public class RandomLayout implements Layout {

	private final EditingOptions editingOptions;

	@Inject
	public RandomLayout(EditingOptions editingOptions) {
		this.editingOptions = editingOptions;
	}

	/**
	 * Returns a random integer between min and max (both inclusive).
	 *
	 * @param min
	 *                minimum value
	 * @param max
	 *                maximum value
	 * @return random integer in [min, max]
	 */
	private static int randomInt(int min, int max) {
		return (int) (min + (max - min + 1) * Math.random());
	}

	@Override
	public void applyTo(Document<?> document, int x0, int y0, int x1, int y1) {
		for (GraphicalElement elem : document.getGraphicalElements()) {
			if (elem instanceof GraphicalNode) {
				// Only nodes are positioned directly.
				GraphicalNode node = (GraphicalNode) elem;
				applyTo(node, x0, y0, x1, y1);
			} else if (elem instanceof GraphicalEdge) {
				// Straighten all edges.
				GraphicalEdge edge = (GraphicalEdge) elem;
				edge.removeAllBreakpoints();
			}
		}
	}

	protected void applyTo(GraphicalNode node, int x0, int y0, int x1, int y1) {
		int x = randomInt(x0, x1);
		int y = randomInt(y0, y1);
		node.setCenter(getCenter(x, y));
	}

	/**
	 * Returns a point either at the given x,y coordinates or the nearest
	 * grid point depending on snap to grid status.
	 *
	 * @param x
	 *                x coordinate
	 * @param y
	 *                y coordinate
	 * @return center point
	 */
	private Point getCenter(int x, int y) {
		Point p = new Point(x, y);
		if (editingOptions.isSnapToGridEnabled()) {
			return ToolUtil.snapToGrid(p, editingOptions.getGridSpacing());
		} else {
			return p;
		}
	}

	@Override
	public String getName() {
		return "Random";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
