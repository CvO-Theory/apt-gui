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

package uniol.aptgui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.DocumentListener;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.editor.features.ContextMenuFeature;
import uniol.aptgui.editor.features.FireTransitionTool;
import uniol.aptgui.editor.features.HoverFeature;
import uniol.aptgui.editor.features.SelectionTool;
import uniol.aptgui.editor.features.ViewportTool;
import uniol.aptgui.editor.features.ZoomFeature;
import uniol.aptgui.editor.features.base.Feature;
import uniol.aptgui.editor.features.base.FeatureCollection;
import uniol.aptgui.editor.features.base.FeatureId;
import uniol.aptgui.editor.features.base.SingleFeatureCollection;
import uniol.aptgui.editor.features.edge.CreateArcTool;
import uniol.aptgui.editor.features.edge.CreateFlowTool;
import uniol.aptgui.editor.features.node.CreatePlaceTool;
import uniol.aptgui.editor.features.node.CreateStateTool;
import uniol.aptgui.editor.features.node.CreateTransitionTool;
import uniol.aptgui.events.ToolSelectedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;
import uniol.aptgui.events.WindowOpenedEvent;
import uniol.aptgui.mainwindow.WindowId;

public class EditorPresenterImpl extends AbstractPresenter<EditorPresenter, EditorView>
		implements EditorPresenter, DocumentListener {

	private final Application application;

	private final FeatureCollection features;
	private final SingleFeatureCollection tools;

	private WindowId windowId;
	private Document<?> document;

	@Inject
	public EditorPresenterImpl(EditorView view, Application application) {
		super(view);
		this.application = application;
		this.features = new FeatureCollection();
		this.tools = new SingleFeatureCollection();

		view.addMouseEventListener(features);
		view.addMouseEventListener(tools);
		application.getEventBus().register(this);
	}

	@Subscribe
	public void onToolSelectedEvent(ToolSelectedEvent e) {
		if (application.getActiveWindow() != windowId) {
			return;
		}

		features.setListening(false);
		tools.setListening(false);
		tools.setActive(e.getSelectionId());
		tools.setListening(true);
		features.setListening(true);
		Feature tool = tools.getActiveFeature();
		view.setCursor(tool.getCursor());
		view.repaint();
	}

	@Subscribe
	public void onWindowOpenedEvent(WindowOpenedEvent e) {
		if (e.getWindowId() != windowId) {
			return;
		}
		document.getViewport().setWidth(view.getCanvasWidth());
		document.getViewport().setHeight(view.getCanvasHeight());
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		if (e.getWindowId() != windowId) {
			return;
		}
		tools.onActivated();
	}

	@Subscribe
	public void onWindowFocusLostEvent(WindowFocusLostEvent e) {
		if (e.getWindowId() != windowId) {
			return;
		}
		tools.onDeactivated();
	}

	@Override
	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setDocument(Document<?> document) {
		if (document != null) {
			document.removeListener(this);
		}

		this.document = document;
		this.document.addListener(this);

		features.onDeactivated();
		features.put(FeatureId.HOVER, new HoverFeature(document));
		features.put(FeatureId.ZOOM, new ZoomFeature(document));
		features.put(FeatureId.CONTEXT_MENU, new ContextMenuFeature(document, view));
		features.onActivated();
		features.setListening(true);

		History hist = application.getHistory();
		EditingOptions eo = application.getEditingOptions();
		tools.clear();
		if (document instanceof PnDocument) {
			PnDocument pnDocument = (PnDocument) document;
			tools.put(FeatureId.VIEWPORT, new ViewportTool(document));
			tools.put(FeatureId.SELECTION, new SelectionTool(document, hist, eo));
			tools.put(FeatureId.PN_CREATE_PLACE, new CreatePlaceTool(pnDocument, hist, eo));
			tools.put(FeatureId.PN_CREATE_TRANSITION, new CreateTransitionTool(pnDocument, hist, eo));
			tools.put(FeatureId.PN_CREATE_FLOW, new CreateFlowTool(pnDocument, hist));
			tools.put(FeatureId.PN_FIRE_TRANSITION, new FireTransitionTool(pnDocument, hist));
		} else if (document instanceof TsDocument) {
			TsDocument tsDocument = (TsDocument) document;
			tools.put(FeatureId.VIEWPORT, new ViewportTool(document));
			tools.put(FeatureId.SELECTION, new SelectionTool(document, hist, eo));
			tools.put(FeatureId.TS_CREATE_STATE, new CreateStateTool(tsDocument, hist, eo));
			tools.put(FeatureId.TS_CREATE_ARC, new CreateArcTool(tsDocument, hist, view));
		}
		tools.setListening(true);
	}

	@Override
	public void onPaint(Graphics2D graphics) {
		if (document.isVisible()) {
			EditingOptions eo = application.getEditingOptions();
			RenderingOptions ro = application.getRenderingOptions();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (ro.isGridVisible()) {
				drawGrid(graphics, eo.getGridSpacing());
			}
			document.draw(graphics, ro);
		}
	}

	@SuppressWarnings("unused")
	private void drawBoundingBox(Graphics2D graphics) {
		Rectangle bounds = document.getBounds();
		Point tl = document.getViewport().transform(new Point(bounds.x, bounds.y));
		double scale = document.getViewport().getScale();
		graphics.drawRect(tl.x, tl.y, (int)(bounds.width * scale), (int)(bounds.height * scale));
	}

	private void drawGrid(Graphics2D graphics, int gridSpacing) {
		// Compute area where grid lines need to be drawn
		Point viewportTopLeft = document.getViewport().getTopLeft();
		Point topLeft = document.getViewport().transformInverse(viewportTopLeft);
		topLeft.x = topLeft.x - gridSpacing - topLeft.x % gridSpacing;
		topLeft.y = topLeft.y - gridSpacing - topLeft.y % gridSpacing;

		Point viewportBottomRight = document.getViewport().getBottomRight();
		Point bottomRight = document.getViewport().transformInverse(viewportBottomRight);
		bottomRight.x = bottomRight.x + gridSpacing - bottomRight.x % gridSpacing;
		bottomRight.y = bottomRight.y + gridSpacing - bottomRight.y % gridSpacing;

		// Set graphics transform
		AffineTransform original = graphics.getTransform();
		graphics.transform(document.getViewport().getAffineTransform());
		// Set grid color
		graphics.setColor(Color.GRAY);

		// Draw vertical grid lines
		for (int x = topLeft.x; x < bottomRight.x; x += gridSpacing) {
			graphics.drawLine(x, topLeft.y, x, bottomRight.y);
		}
		// Draw horizontal grid lines
		for (int y = topLeft.y; y < bottomRight.y; y += gridSpacing) {
			graphics.drawLine(topLeft.x, y, bottomRight.x, y);
		}

		// Reset graphics transform
		graphics.setTransform(original);
	}

	@Override
	public void onDocumentDirty(Document<?> source) {
		if (document.isVisible()) {
			getView().repaint();
		}
	}

	@Override
	public void onSelectionChanged(Document<?> source) {
		// Do nothing.
	}

	@Override
	public void onDocumentChanged(Document<?> source) {
		// No other action necessary except a repaint.
		onDocumentDirty(source);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
