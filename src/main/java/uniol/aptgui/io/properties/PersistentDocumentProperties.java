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

import com.google.common.base.Function;

import uniol.apt.adt.extension.Extensible;
import uniol.apt.adt.extension.ExtensionProperty;
import uniol.apt.adt.extension.IExtensible;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;

/**
 * Helper class to parse and render persistent properties for every element in a
 * document.
 */
public class PersistentDocumentProperties {

	private final Document<?> doc;

	public PersistentDocumentProperties(Document<?> doc) {
		this.doc = doc;
	}

	/**
	 * Turns all properties of GraphicalElements with associated model
	 * elements into their string representation and sets them as an
	 * extension on the model element. This extension will be written to a
	 * file when saved using an APT renderer.
	 *
	 * @param transformer
	 *                function that generates the string representation of a
	 *                GraphicalElement
	 */
	public void renderPersistentModelExtensions(Function<GraphicalElement, String> transformer) {
		for (GraphicalElement elem : doc.getGraphicalElements()) {
			String representation = transformer.apply(elem);
			IExtensible extensible = doc.getAssociatedModelElement(elem);
			if (!representation.isEmpty()) {
				extensible.putExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT, representation,
						ExtensionProperty.WRITE_TO_FILE);
			} else {
				extensible.removeExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT);
			}
		}
	}

	/**
	 * Iterates over all elements and tries to retrieve the persistent
	 * graphical extension. If it exists, the GraphicalElement associated
	 * with the model element gets updated according to the extension's
	 * value. If it does not exist, nothing happens.
	 */
	public void parsePersistentModelExtensions() {
		for (GraphicalElement elem : doc.getGraphicalElements()) {
			Extensible modelElem = doc.getAssociatedModelElement(elem);
			PersistentProperties pp = new PersistentProperties(modelElem);
			pp.applyAll(elem);
		}
	}

	/**
	 * Removes all persistent extensions from the model elements of the
	 * given document.
	 */
	public void removePersistentModelExtensions() {
		for (GraphicalElement elem : doc.getGraphicalElements()) {
			IExtensible extensible = doc.getAssociatedModelElement(elem);
			extensible.removeExtension(GraphicalElement.EXTENSION_KEY_PERSISTENT);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
