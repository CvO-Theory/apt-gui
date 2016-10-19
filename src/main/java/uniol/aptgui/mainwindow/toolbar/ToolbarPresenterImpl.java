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

package uniol.aptgui.mainwindow.toolbar;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.editor.features.base.FeatureId;
import uniol.aptgui.events.ToolSelectedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;

public class ToolbarPresenterImpl extends AbstractPresenter<ToolbarPresenter, ToolbarView> implements ToolbarPresenter {

	private final EventBus eventBus;
	private final Application application;
	private FeatureId activePnTool;
	private FeatureId activeTsTool;

	@Inject
	public ToolbarPresenterImpl(ToolbarView view, Application application, EventBus eventBus) {
		super(view);
		this.eventBus = eventBus;
		this.application = application;
		this.activePnTool = FeatureId.SELECTION;
		this.activeTsTool = FeatureId.SELECTION;

		this.eventBus.register(this);
		view.setPetriNetToolsVisible(false);
		view.setTransitionSystemToolsVisible(false);
	}

	@Subscribe
	public void onWindowFocusLostEvent(WindowFocusLostEvent e) {
		// Don't hide toolbar if an external window lost the focus.
		if (application.getMainWindow().isInternalWindow(e.getWindowId())) {
			view.setPetriNetToolsVisible(false);
			view.setTransitionSystemToolsVisible(false);
		}
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		switch (e.getWindowId().getType()) {
		case PETRI_NET:
			view.setTransitionSystemToolsVisible(false);
			view.setPetriNetToolsVisible(true);
			view.setActiveTool(activePnTool, true);
			break;
		case TRANSITION_SYSTEM:
			view.setPetriNetToolsVisible(false);
			view.setTransitionSystemToolsVisible(true);
			view.setActiveTool(activeTsTool, false);
			break;
		default:
			break;
		}
	}

	@Subscribe
	public void onToolSelectedEvent(ToolSelectedEvent e) {
		if (application.getActiveDocument() instanceof PnDocument) {
			activePnTool = e.getSelectionId();
		} else {
			activeTsTool = e.getSelectionId();
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
