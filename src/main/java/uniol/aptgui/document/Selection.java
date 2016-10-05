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

package uniol.aptgui.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uniol.aptgui.document.graphical.GraphicalElement;

public class Selection {

	/**
	 * A set of selected elements.
	 */
	private final Set<GraphicalElement> selection = new HashSet<>();

	/**
	 * Returns an unmodifiable set of all selected elements.
	 *
	 * @return unmodifiable view into the selection set
	 */
	public Set<GraphicalElement> getSelection() {
		return Collections.unmodifiableSet(selection);
	}

	/**
	 * Toggles selection status on the element. If it was previously
	 * unselected, it will be selected afterwards and the other way around.
	 *
	 * @param elem
	 *                element to toggle selection for
	 */
	public void toggleSelection(GraphicalElement elem) {
		if (elem.isSelected()) {
			removeFromSelection(elem);
		} else {
			addToSelection(elem);
		}
	}

	/**
	 * Adds the given element to the selection.
	 *
	 * @param elem
	 *                newly selected element
	 */
	public void addToSelection(GraphicalElement elem) {
		selection.add(elem);
		elem.setSelected(true);
	}

	/**
	 * Removes the given element from the selection.
	 *
	 * @param elem
	 *                the element to unselect
	 */
	public void removeFromSelection(GraphicalElement elem) {
		selection.remove(elem);
		elem.setSelected(false);
	}

	/**
	 * Clears the current selection.
	 */
	public void clearSelection() {
		for (GraphicalElement ge : selection) {
			ge.setSelected(false);
		}
		selection.clear();
	}

	/**
	 * Returns the most specific base class that all selected elements are
	 * assignable to.
	 *
	 * @return common base class of the current selection or null, if
	 *         nothing is selected
	 */
	public Class<? extends GraphicalElement> getCommonBaseClass() {
		if (selection.isEmpty()) {
			return null;
		}

		// Randomly select one element's class.
		Class<? extends GraphicalElement> commonBase = selection.iterator().next().getClass();
		for (GraphicalElement elem : selection) {
			if (!commonBase.isAssignableFrom(elem.getClass())) {
				commonBase = findCommonBase(commonBase, elem.getClass());
			}
		}
		return commonBase;
	}

	/**
	 * Finds the nearest common base class for the two given classes.
	 *
	 * @param class1
	 * @param class2
	 * @return
	 */
	private Class<? extends GraphicalElement> findCommonBase(
			Class<? extends GraphicalElement> class1,
			Class<? extends GraphicalElement> class2) {
		List<Class<? extends GraphicalElement>> parents1 = getParents(class1);
		List<Class<? extends GraphicalElement>> parents2 = getParents(class2);
		parents1.retainAll(parents2);
		return parents1.get(0);
	}

	/**
	 * Returns a list of superclasses from specific to more general stopping
	 * at GraphicalElement.
	 *
	 * @return non-empty list of ancestors
	 */
	@SuppressWarnings("unchecked")
	private List<Class<? extends GraphicalElement>> getParents(Class<? extends GraphicalElement> clazz) {
		List<Class<? extends GraphicalElement>> parents = new ArrayList<>();
		Class<? extends GraphicalElement> superclass = clazz;
		do {
			superclass = (Class<? extends GraphicalElement>) superclass.getSuperclass();
			parents.add(superclass);
		} while (superclass != GraphicalElement.class);

		assert !parents.isEmpty();
		return parents;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
