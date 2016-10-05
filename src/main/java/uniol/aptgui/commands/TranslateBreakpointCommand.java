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

import java.awt.Point;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;

/**
 * Command that translates a breakpoint that belongs to a graphical edge. If a
 * breakpoint becomes unnecessary due to the translation it is removed.
 */
public class TranslateBreakpointCommand extends Command {

	/**
	 * Document that is operated on.
	 */
	private final Document<?> document;

	/**
	 * GraphicalEdge that is operated on.
	 */
	private final GraphicalEdge edge;

	/**
	 * Breakpoint that will be translated.
	 */
	private final int breakpointIndex;

	/**
	 * X-axis translation.
	 */
	private int deltaX;

	/**
	 * Y-axis translation.
	 */
	private int deltaY;

	/**
	 * Index of the breakpoint that was removed or -1 if no breakpoint was
	 * removed.
	 */
	private int removedBreakpointIndex;

	/**
	 * Breakpoint that was removed or null if no breakpoint was removed.
	 */
	private Point removedBreakpoint;

	/**
	 * Creates a new command to translate the given breakpoint in the given
	 * edge in the given document.
	 *
	 * @param document
	 * @param edge
	 * @param breakpointIndex
	 */
	public TranslateBreakpointCommand(Document<?> document, GraphicalEdge edge, int breakpointIndex) {
		this.document = document;
		this.edge = edge;
		this.breakpointIndex = breakpointIndex;
		this.deltaX = 0;
		this.deltaY = 0;
		this.removedBreakpointIndex = -1;
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
	public void translate(int dx, int dy) {
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

	/**
	 * Applies the given translation to the breakpoint (changes the
	 * document).
	 *
	 * @param dx
	 *                x-axis translation
	 * @param dy
	 *                y-axis translation
	 */
	private void applyTranslation(int dx, int dy) {
		Point bp = edge.getBreakpoint(breakpointIndex);
		bp.translate(dx, dy);
		document.fireDocumentChanged(true);
	}

	@Override
	public String getName() {
		return "Translate Breakpoint";
	}

	@Override
	public void execute() {
		applyTranslation();
		// Test previous, current and next breakpoint for necessity.
		int minBp = Math.max(0, breakpointIndex - 1);
		int maxBp = Math.min(edge.getBreakpointCount() - 1, breakpointIndex + 1);
		for (int i = minBp; i <= maxBp; i++) {
			if (!edge.isBreakpointNecessary(i)) {
				removedBreakpointIndex = i;
				removedBreakpoint = edge.removeBreakpoint(i);
				break;
			}
		}
	}

	@Override
	public void redo() {
		applyTranslation();
		if (removedBreakpointIndex >= 0) {
			edge.removeBreakpoint(removedBreakpointIndex);
		}
	}

	@Override
	public void undo() {
		if (removedBreakpointIndex >= 0) {
			edge.addBreakpoint(removedBreakpointIndex, removedBreakpoint);
		}
		unapplyTranslation();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
