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

package uniol.aptgui.io.properties;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import uniol.apt.adt.extension.Extensible;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

/**
 * Parser for the persistent extensions that save graphical and layout
 * properties. The parsed values are saved and can be applied to
 * GraphicalElements with the various apply methods.
 */
public class PersistentProperties {

	/**
	 * A map of property names to property values.
	 */
	private final Map<String, String> properties;

	/**
	 * Creates a new PersistentProperties object filled with properties
	 * retrieved from the extensions of the given extensible model object.
	 *
	 * @param extensible
	 *                model object
	 */
	public PersistentProperties(Extensible extensible) {
		this.properties = new HashMap<>();
		if (extensible.hasExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT)) {
			Object extension = extensible.getExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT);
			String repr = ((String) extension).trim().toLowerCase();
			if (!repr.isEmpty()) {
				parseRepresentation(repr);
			}
		}
	}

	/**
	 * Returns true if there are no properties to be applied.
	 *
	 * @return true if there are no properties to be applied
	 */
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	/**
	 * Parses the string that is the value of the persistent extension
	 * "property".
	 *
	 * @param repr
	 *                "property" extension value
	 */
	private void parseRepresentation(String repr) {
		String[] propertiesSplit = repr.split(";");
		for (String prop : propertiesSplit) {
			String[] keyVal = prop.trim().split("\\s+", 2);
			if (keyVal.length == 2) {
				this.properties.put(keyVal[0], keyVal[1]);
			} else {
				throw new MalformedPersistentPropertyException(
						"Properties need to be separated from their value by whitespace.");
			}
		}
	}

	/**
	 * Applies all attributes that are applicable to the given element.
	 *
	 * @param elem
	 *                graphical element
	 */
	public void applyAll(GraphicalElement elem) {
		applyColor(elem);
		if (elem instanceof GraphicalNode) {
			applyPosition((GraphicalNode) elem);
		} else if (elem instanceof GraphicalEdge) {
			applyBreakpoints((GraphicalEdge) elem);
		}
	}

	/**
	 * Sets breakpoints in the given edge element.
	 *
	 * @param elem
	 *                edge
	 */
	public void applyBreakpoints(GraphicalEdge elem) {
		if (properties.containsKey("bp")) {
			String prop = properties.get("bp").trim();
			String[] coordinates = prop.split("\\s+");
			for (String coord : coordinates) {
				Point breakpoint = parsePoint(coord);
				elem.addBreakpoint(breakpoint);
			}
		}
	}

	/**
	 * Sets the position of the given node element.
	 *
	 * @param elem
	 *                node
	 */
	public void applyPosition(GraphicalNode elem) {
		if (properties.containsKey("pos")) {
			elem.setCenter(parsePoint(properties.get("pos")));
		}
	}

	private Point parsePoint(String point) {
		try {
			String[] xy = point.trim().split(",", 2);
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			return new Point(x, y);
		} catch (Throwable t) {
			throw new MalformedPersistentPropertyException("Malformed position property '" + point + "'",
					t);
		}
	}

	/**
	 * Sets the color of the given graphical element.
	 *
	 * @param elem
	 *                graphical element
	 */
	public void applyColor(GraphicalElement elem) {
		if (properties.containsKey("col")) {
			String colorString = properties.get("col").trim();
			elem.setColor(parseColor(colorString));
		}
	}

	private Color parseColor(String color) {
		try {
			return Color.decode(color.trim());
		} catch (Throwable t) {
			throw new MalformedPersistentPropertyException("Malformed color property '" + color + "'", t);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
