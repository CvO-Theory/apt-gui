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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import uniol.aptgui.commands.AddBreakpointCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.commands.TranslateBreakpointCommand;
import uniol.aptgui.commands.TranslateElementsCommand;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.Viewport;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.document.graphical.special.Frame;
import uniol.aptgui.editor.features.base.Feature;

/**
 * The selection tool gives the user the ability to select elements, move them
 * and modify edge paths.
 */
public class SelectionTool extends Feature {

	private static enum State {
		INIT, DRAG_SELECTION, DRAG_BREAKPOINT, DRAG_FRAME, SELECT_EDGE_OR_MODIFY_BREAKPOINT
	}

	/**
	 * History reference.
	 */
	private final History history;

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's viewport object.
	 */
	private final Viewport viewport;

	/**
	 * Reference to the global editing options object.
	 */
	private final EditingOptions editingOptions;

	/**
	 * State of the SelectionTool state machine.
	 */
	private State state;

	/**
	 * Model position when a drag started.
	 */
	private Point dragSource;

	/**
	 * Command object that will be submitted to the history when an element
	 * translation finishes.
	 */
	private TranslateElementsCommand translateElementsCommand;

	/**
	 * Command object that will be submitted to the history when a
	 * breakpoint translation finishes.
	 */
	private TranslateBreakpointCommand translateBreakpointCommand;

	/**
	 * Flag that is set when a breakpoint has been created by dragging an
	 * edge at a position where no breakpoint existed before.
	 */
	private boolean breakpointCreated;

	/**
	 * Graphical object that represents the selection frame.
	 */
	private Frame selectionFrame;

	/**
	 * Creates a new SelectionTool that operates on the given document.
	 *
	 * @param document
	 *                document being modified
	 * @param history
	 *                history to send commands to
	 * @param editingOptions
	 *                settings that influence the tool's behavior
	 */
	public SelectionTool(Document<?> document, History history, EditingOptions editingOptions) {
		this.document = document;
		this.viewport = document.getViewport();
		this.history = history;
		this.editingOptions = editingOptions;

	}

	@Override
	public void onActivated() {
		this.state = State.INIT;
		this.dragSource = null;
		this.breakpointCreated = false;
		this.selectionFrame = new Frame();
		document.add(selectionFrame);
	}

	@Override
	public void onDeactivated() {
		document.remove(selectionFrame);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Only listen for LMB clicks.
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Point modelPosition = viewport.transformInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition, true);

		if (elem instanceof GraphicalNode) {
			// Set the correct dragSource
			if (editingOptions.isSnapToGridEnabled()) {
				GraphicalNode node = (GraphicalNode) elem;
				dragSource = new Point(node.getCenter());
			} else {
				dragSource = modelPosition;
			}
			state = State.INIT;
			// Select the element
			selectElementAt(modelPosition, e.isControlDown());
		} else if (elem instanceof GraphicalEdge) {
			dragSource = modelPosition;
			state = State.SELECT_EDGE_OR_MODIFY_BREAKPOINT;
		} else if (elem == null) {
			// Prepare for selection with a frame
			dragSource = modelPosition;
			state = State.DRAG_FRAME;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point modelPosition = viewport.transformInverse(e.getPoint());

		if (state == State.DRAG_SELECTION && !translateElementsCommand.isIdentity()) {
			// If the selection was dragged, commit the translation
			translateElementsCommand.unapplyTranslation();
			history.execute(translateElementsCommand);
			state = State.INIT;
		} else if (state == State.DRAG_BREAKPOINT && !translateBreakpointCommand.isIdentity()) {
			// If a breakpoint was dragged, commit the translation
			translateBreakpointCommand.unapplyTranslation();
			if (breakpointCreated) {
				history.mergeExecute("Create and Move Breakpoint", translateBreakpointCommand);
			} else {
				history.execute(translateBreakpointCommand);
			}
			// Reset state
			breakpointCreated = false;
			state = State.INIT;
		} else if (state == State.SELECT_EDGE_OR_MODIFY_BREAKPOINT) {
			// If the mouse was not dragged since the state is still
			// SELECT_EDGE_OR_CREATE_BREAKPOINT, simply select the
			// edge
			selectElementAt(modelPosition, e.isControlDown());
			state = State.INIT;
		} else if (state == State.DRAG_FRAME) {
			clearSelection();
			Rectangle frame = getSelectionFrameRect(dragSource, modelPosition);
			for (GraphicalElement elem : document.getGraphicalElements()) {
				Rectangle bounds = elem.getBounds();
				if (frame.contains(bounds)) {
					document.addToSelection(elem);
				}
			}
			selectionFrame.setVisible(false);
			document.fireSelectionChanged();
			document.fireDocumentDirty();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point modelPosition = viewport.transformInverse(e.getPoint());

		Point dragTarget;
		if (editingOptions.isSnapToGridEnabled()) {
			dragTarget = snapToGridView(e.getPoint());
		} else {
			dragTarget = modelPosition;
		}
		int dx = dragTarget.x - dragSource.x;
		int dy = dragTarget.y - dragSource.y;


		GraphicalElement elem = document.getGraphicalElementAt(dragSource, true);
		if ((state == State.INIT && document.getSelection().contains(elem) && elem instanceof GraphicalNode)
				|| state == State.DRAG_SELECTION) {
			// Only translate elements when grabbed by a node
			translateElementsCommand.unapplyTranslation();
			translateElementsCommand.setTranslation(dx, dy);
			translateElementsCommand.applyTranslation();
			state = State.DRAG_SELECTION;
		} else if (state == State.DRAG_BREAKPOINT) {
			translateBreakpointCommand.unapplyTranslation();
			translateBreakpointCommand.translate(dx, dy);
			translateBreakpointCommand.applyTranslation();
		} else if (state == State.SELECT_EDGE_OR_MODIFY_BREAKPOINT) {
			prepareBreakpoint(dragSource);
			state = State.DRAG_BREAKPOINT;
		} else if (state == State.DRAG_FRAME) {
			Rectangle frame = getSelectionFrameRect(dragSource, modelPosition);
			selectionFrame.setRectangle(frame);
			selectionFrame.setVisible(true);
			document.fireDocumentDirty();
		}
	}

	private Rectangle getSelectionFrameRect(Point p1, Point p2) {
		int x = Math.min(p1.x, p2.x);
		int y = Math.min(p1.y, p2.y);
		int width = Math.abs(p1.x - p2.x);
		int height = Math.abs(p1.y - p2.y);
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Returns a point in model coordinates that was possibly snapped to
	 * grid.
	 *
	 * @param mousePosition
	 *                mouse position in view coordinates
	 * @return point in model coordinates that was possibly snapped to grid
	 */
	private Point snapToGridView(Point mousePosition) {
		Point modelPosition = viewport.transformInverse(mousePosition);
		Point snappedModelPosition = ToolUtil.snapToGrid(modelPosition, editingOptions.getGridSpacing());
		return snappedModelPosition;
	}

	/**
	 * Creates or retrieves a breakpoint at the given model position and
	 * sets it up for translation using mouse dragging.
	 *
	 * @param modelPosition
	 *                model position of a breakpoint (retrieval) or near a
	 *                breakpoint (creation)
	 */
	private void prepareBreakpoint(Point modelPosition) {
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
		if (elem instanceof GraphicalEdge) {
			GraphicalEdge edge = (GraphicalEdge) elem;
			// Try to retrieve breakpoint at given position.
			int bpIndex = edge.getClosestBreakpointIndex(modelPosition);
			// If it does not exist, create one.
			if (bpIndex == -1) {
				AddBreakpointCommand cmd = new AddBreakpointCommand(document, edge, modelPosition);
				history.execute(cmd);
				bpIndex = cmd.getNewBreakpointIndex();
				breakpointCreated = true;
			}
			// If a breakpoint existed or was created, set it up for
			// dragging.
			if (bpIndex != -1) {
				dragSource = new Point(edge.getBreakpoint(bpIndex));
				translateBreakpointCommand = new TranslateBreakpointCommand(document, edge, bpIndex);
			}
		}
	}

	/**
	 * Selects the element at the given model position. Depending on
	 * toggleModifier, the clicked element gets added to/removed from the
	 * selection or the selection is replaced.
	 *
	 * @param modelPosition
	 * @param toggleModifier
	 */
	private void selectElementAt(Point modelPosition, boolean toggleModifier) {
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition, true);

		if (elem != null) {
			if (toggleModifier) {
				// If CTRL was pressed, add to/remove from
				// selection.
				document.toggleSelection(elem);
			} else if (!document.getSelection().contains(elem)) {
				// If the clicked element did not belong to the
				// selection, replace the selection.
				document.clearSelection();
				document.addToSelection(elem);
			}

			document.setLastSelectionPosition(modelPosition);
			document.fireSelectionChanged();
			document.fireDocumentDirty();
			translateElementsCommand = new TranslateElementsCommand(document, document.getSelection());
		}
	}

	private void clearSelection() {
		document.clearSelection();
		document.fireSelectionChanged();
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
