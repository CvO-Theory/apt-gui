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

package uniol.aptgui.editor.features;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.Transition;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.FireTransitionCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.nodes.GraphicalTransition;
import uniol.aptgui.editor.features.base.HoverEffectFeature;

/**
 * Tool that allows the user to click on a Petri net transition to fire it.
 */
public class FireTransitionTool extends HoverEffectFeature {

	/**
	 * Document this tool operates on.
	 */
	protected final PnDocument document;

	/**
	 * Reference to the Document's viewport object.
	 */
	protected final Viewport viewport;

	/**
	 * History object for command execution.
	 */
	protected final History history;

	public FireTransitionTool(PnDocument document, History history) {
		this.document = document;
		this.viewport = document.getViewport();
		this.history = history;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Point modelPoint = viewport.transformInverse(e.getPoint());
		GraphicalElement element = document.getGraphicalElementAt(modelPoint);
		if (element instanceof GraphicalTransition && isTransitionFireable(element)) {
			Command cmd = new FireTransitionCommand(document, (GraphicalTransition) element);
			history.execute(cmd);
			// Force update of hover effects because the state can
			// change without the user having to move the mouse.
			updateHoverEffects();
			document.fireDocumentDirty();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point modelPoint = viewport.transformInverse(e.getPoint());
		GraphicalElement element = document.getGraphicalElementAt(modelPoint);
		setHoverEffects(element);
		document.fireDocumentDirty();
	}

	@Override
	protected void applyHoverEffects(GraphicalElement hoverElement) {
		if (hoverElement instanceof GraphicalTransition) {
			hoverElement.setHighlightedSuccess(isTransitionFireable(hoverElement));
			hoverElement.setHighlightedError(!isTransitionFireable(hoverElement));
		}
	}

	@Override
	protected void removeHoverEffects(GraphicalElement hoverElement) {
		hoverElement.setHighlightedSuccess(false);
		hoverElement.setHighlightedError(false);
	}

	private boolean isTransitionFireable(GraphicalElement element) {
		Marking marking = document.getModel().getInitialMarking();
		Transition transition = document.getAssociatedModelElement(element);
		return transition.isFireable(marking);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
