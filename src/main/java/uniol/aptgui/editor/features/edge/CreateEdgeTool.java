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

package uniol.aptgui.editor.features.edge;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Viewport;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.document.graphical.special.InvisibleNode;
import uniol.aptgui.editor.features.base.HoverEffectFeature;

/**
 * Base class for tools that allow the creation of edges.
 * <p>
 * The edge creation process goes as follows: The user first clicks a node. This
 * starts the creation process. Subsequent clicks on the canvas create break
 * points in the edge. If the user clicks another valid node, the creation
 * process is finalized.
 *
 * @param <T> document type
 * @param <U> edge type that gets created
 */
public abstract class CreateEdgeTool<T extends Document<?>, U extends GraphicalEdge> extends HoverEffectFeature {

	/**
	 * Document this tool operates on.
	 */
	protected final T document;

	/**
	 * Reference to the Document's viewport object.
	 */
	protected final Viewport viewport;

	/**
	 * True, while the creation is in progress, i.e. the user did not finish
	 * the creation process.
	 */
	protected boolean creating;

	/**
	 * Edge source element.
	 */
	protected GraphicalNode graphicalSource;

	/**
	 * Edge target element.
	 */
	protected GraphicalNode graphicalTarget;

	/**
	 * Edge element to be inserted.
	 */
	protected U graphicalEdge;

	public CreateEdgeTool(T document) {
		this.document = document;
		this.viewport = document.getViewport();
		this.creating = false;
	}

	/**
	 * Creates a new instance of this tool's graphical edge type.
	 *
	 * @param source
	 *                source node
	 * @param target
	 *                target node
	 * @return
	 */
	protected abstract U createGraphicalEdge(GraphicalNode source, GraphicalNode target);

	/**
	 * Returns true if the edge being created can end in the given element.
	 * This may for example be dependent on the source and target type.
	 *
	 * @param target
	 *                the candidate target element
	 * @return true, if the element is a valid target for the edge
	 */
	protected abstract boolean isValidTarget(GraphicalElement target);

	/**
	 * Commits the creation of the given edge to the history.
	 *
	 * @param edge
	 *                the edge to create
	 */
	protected abstract void commitEdgeCreation(U edge);

	@Override
	public void onDeactivated() {
		// Stop creation when the tool is deactivated.
		creating = false;

		// Hide GraphicalEdge.
		if (graphicalEdge != null) {
			document.remove(graphicalEdge);
			document.fireDocumentDirty();
			graphicalEdge = null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			// Cancel current action
			onDeactivated();
			onActivated();
			return;
		}

		Point modelPosition = viewport.transformInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);

		if (!creating && elem instanceof GraphicalNode) {
			// Clicked first node, therefore start creation.
			creating = true;
			graphicalSource = (GraphicalNode) elem;
			graphicalTarget = new InvisibleNode();
			graphicalTarget.setCenter(modelPosition);
			graphicalEdge = createGraphicalEdge(graphicalSource, graphicalTarget);
			document.add(graphicalEdge);
		} else if (creating) {
			// Clicked second node. If possible, stop creation.
			if (elem != null && isValidTarget(elem)) {
				creating = false;
				document.remove(graphicalEdge);
				graphicalTarget = (GraphicalNode) elem;
				graphicalEdge.setTarget(graphicalTarget);
				commitEdgeCreation(graphicalEdge);
				graphicalEdge = null;
				removeHoverEffects();
			}
			// Clicked anywhere else, therefore add breakpoint.
			if (elem == null) {
				graphicalEdge.addBreakpoint(modelPosition);
			}
		}

		document.fireDocumentDirty();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (creating) {
			Point modelPosition = viewport.transformInverse(e.getPoint());
			// Move invisible target.
			graphicalTarget.setCenter(modelPosition);
			// Highlight elements based on validity.
			GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
			setHoverEffects(elem);
			document.fireDocumentDirty();
		}
	}

	@Override
	protected void removeHoverEffects(GraphicalElement hoverElement) {
		hoverElement.setHighlightedSuccess(false);
		hoverElement.setHighlightedError(false);
	}

	@Override
	protected void applyHoverEffects(GraphicalElement hoverElement) {
		if (isValidTarget(hoverElement)) {
			hoverElement.setHighlightedSuccess(true);
		} else {
			hoverElement.setHighlightedError(true);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
