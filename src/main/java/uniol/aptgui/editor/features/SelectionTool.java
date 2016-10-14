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
import uniol.aptgui.editor.features.base.Feature;

/**
 * The selection tool gives the user the ability to select elements, move them
 * and modify edge paths.
 */
public class SelectionTool extends Feature {

	private static enum DragType {
		NONE, SELECTION, BREAKPOINT, CREATE_BREAKPOINT
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
	 * Differentiates on what element the drag began.
	 */
	private DragType dragType;

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
		this.dragType = DragType.NONE;
		this.dragSource = null;
		this.breakpointCreated = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Only listen for LMB clicks.
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Point modelPosition = viewport.transformInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition, true);

		if (elem instanceof GraphicalNode && editingOptions.isSnapToGridEnabled()) {
			GraphicalNode node = (GraphicalNode) elem;
			dragSource = new Point(node.getCenter());
		} else {
			dragSource = viewport.transformInverse(e.getPoint());
		}

		if (elem instanceof GraphicalEdge) {
			dragType = DragType.CREATE_BREAKPOINT;
		} else {
			selectElementAt(modelPosition, e.isControlDown());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragType == DragType.NONE) {
			return;
		}

		if (dragType == DragType.BREAKPOINT && !translateBreakpointCommand.isIdentity()) {
			translateBreakpointCommand.unapplyTranslation();
			if (breakpointCreated) {
				history.mergeExecute("Create and Move Breakpoint", translateBreakpointCommand);
			} else {
				history.execute(translateBreakpointCommand);
			}
		}
		if (dragType == DragType.SELECTION && !translateElementsCommand.isIdentity()) {
			translateElementsCommand.unapplyTranslation();
			history.execute(translateElementsCommand);
		}
		// If the mouse was not dragged since CREATE_BREAKPOINT was set, simply select the edge.
		if (dragType == DragType.CREATE_BREAKPOINT) {
			Point modelPosition = viewport.transformInverse(e.getPoint());
			selectElementAt(modelPosition, e.isControlDown());
		}

		dragType = DragType.NONE;
		breakpointCreated = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragType == DragType.NONE) {
			return;
		}

		Point dragTarget;
		if (editingOptions.isSnapToGridEnabled()) {
			dragTarget = snapToGridView(e.getPoint());
		} else {
			dragTarget = viewport.transformInverse(e.getPoint());
		}

		int dx = dragTarget.x - dragSource.x;
		int dy = dragTarget.y - dragSource.y;

		switch (dragType) {
		case CREATE_BREAKPOINT:
			dragCreateBreakpoint(dragSource);
			// Fall-through!
		case BREAKPOINT:
			translateBreakpointCommand.unapplyTranslation();
			translateBreakpointCommand.translate(dx, dy);
			translateBreakpointCommand.applyTranslation();
			break;
		case SELECTION:
			if (!GraphicalEdge.class.isAssignableFrom(document.getSelectionCommonBaseClass())) {
				// Only translate elements if the selection is not only edges.
				translateElementsCommand.unapplyTranslation();
				translateElementsCommand.setTranslation(dx, dy);
				translateElementsCommand.applyTranslation();
			}
			break;
		default:
			assert false;
		}
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
	private void dragCreateBreakpoint(Point modelPosition) {
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
				dragType = DragType.BREAKPOINT;
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

			// Set drag type to selection.
			dragType = DragType.SELECTION;
			translateElementsCommand = new TranslateElementsCommand(document, document.getSelection());
		} else {
			dragType = DragType.NONE;
			document.clearSelection();
			document.fireSelectionChanged();
			document.fireDocumentDirty();
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
