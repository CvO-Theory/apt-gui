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

import uniol.apt.adt.pn.Place;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.History;
import uniol.aptgui.commands.ModifyTokensCommand;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.editor.features.base.HoverEffectFeature;

/**
 * Tool that allows the user to click on a Petri net place to add or remove
 * tokens.
 */
public class ModifyTokensTool extends HoverEffectFeature {

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

	public ModifyTokensTool(PnDocument document, History history) {
		this.document = document;
		this.viewport = document.getViewport();
		this.history = history;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		long modification = 0;
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (!e.isControlDown()) {
				modification = +1;
			} else {
				modification = -1;
			}
		}

		Point modelPoint = viewport.transformInverse(e.getPoint());
		GraphicalElement element = document.getGraphicalElementAt(modelPoint);
		if (modification != 0 && element instanceof GraphicalPlace && canModifyTokens(element, modification)) {
			Command cmd = new ModifyTokensCommand(document, (GraphicalPlace) element, modification);
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
		if (hoverElement instanceof GraphicalPlace) {
			hoverElement.setHighlightedSuccess(true);
		}
	}

	@Override
	protected void removeHoverEffects(GraphicalElement hoverElement) {
		hoverElement.setHighlightedSuccess(false);
	}

	private boolean canModifyTokens(GraphicalElement element, long mod) {
		Place place = document.getAssociatedModelElement(element);
		long tokens = place.getInitialToken().getValue() + mod;
		return 0 <= tokens && tokens <= Long.MAX_VALUE;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
