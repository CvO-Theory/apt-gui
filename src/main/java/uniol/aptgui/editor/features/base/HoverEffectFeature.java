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

package uniol.aptgui.editor.features.base;

import uniol.aptgui.document.graphical.GraphicalElement;

/**
 * Abstract base class for all features that need to display highlight effects
 * when hovering over elements. It provides convenience methods that make it
 * easy to apply or remove highlights correctly.
 */
public abstract class HoverEffectFeature extends Feature {

	/**
	 * Saves element under cursor to apply hover effects.
	 */
	protected GraphicalElement hoverElement;

	/**
	 * Correctly and efficiently updates hover effects. Must be called by
	 * subclasses when the mouse moves.
	 *
	 * @param currentHoverElem
	 *                the element that is currently at the cursor position
	 */
	protected void setHoverEffects(GraphicalElement currentHoverElem) {
		// Do nothing if the element didn't change.
		if (hoverElement == currentHoverElem) {
			return;
		}
		// Reset old hover effects.
		removeHoverEffects();
		// Update element.
		hoverElement = currentHoverElem;
		// Apply new hover effects.
		applyHoverEffects();
	}

	/**
	 * Updates hover effect on the currently highlighted element.
	 */
	protected void updateHoverEffects() {
		if (hoverElement != null) {
			removeHoverEffects(hoverElement);
			applyHoverEffects(hoverElement);
		}
	}

	/**
	 * Removes hover effects from currently highlighted element.
	 */
	protected void removeHoverEffects() {
		if (hoverElement != null) {
			removeHoverEffects(hoverElement);
		}
	}

	/**
	 * Applies hover effects to the currently highlighted element.
	 */
	protected void applyHoverEffects() {
		if (hoverElement != null) {
			applyHoverEffects(hoverElement);
		}
	}

	/**
	 * Called by the super class when new hover effects need to be applied.
	 *
	 * @param hoverElement
	 *                element to apply effects to
	 */
	protected abstract void applyHoverEffects(GraphicalElement hoverElement);

	/**
	 * Called by the super class when all hover effects that were set should
	 * be removed.
	 *
	 * @param hoverElement
	 *                element to remove effects from
	 */
	protected abstract void removeHoverEffects(GraphicalElement hoverElement);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
