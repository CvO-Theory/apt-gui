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

package uniol.aptgui.window;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.Presenter;
import uniol.aptgui.View;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.DocumentListener;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;
import uniol.aptgui.mainwindow.WindowId;

public abstract class WindowPresenterImpl<P extends Presenter<V> & WindowPresenter, V extends View<P> & WindowView>
		extends AbstractPresenter<P, V> implements WindowPresenter {

	/**
	 * List of listeners.
	 */
	protected final List<WindowListener> listeners;

	/**
	 * Main application reference.
	 */
	protected final Application application;

	/**
	 * EventBus reference.
	 */
	protected final EventBus eventBus;

	/**
	 * Title of this window as set programmatically.
	 */
	protected String title;

	/**
	 * Title of this window as seen by the user.
	 */
	protected String computedTitle;

	/**
	 * This window's id.
	 */
	protected WindowId id;

	/**
	 * Child presenter that is embedded inside this window.
	 */
	protected Presenter<?> contentPresenter;

	/**
	 * Document listener that get's used when this window contains a
	 * document editor to track document title changes and update the window
	 * title.
	 */
	protected DocumentListener titleChangeListener = new DocumentListener() {
		@Override public void onSelectionChanged(Document<?> source) {}
		@Override public void onDocumentDirty(Document<?> source) {}
		@Override
		public void onDocumentChanged(Document<?> source) {
			updateTitle();
		}
	};

	@SuppressWarnings("unchecked")
	@Inject
	public WindowPresenterImpl(WindowView view, Application application, EventBus eventBus) {
		super((V) view);
		this.listeners = new ArrayList<>();
		this.application = application;
		this.eventBus = eventBus;
		this.title = "";
		setPadding(3);
	}

	@Override
	public void setContentPresenter(Presenter<?> presenter) {
		contentPresenter = presenter;
		getView().setContent(presenter.getView());
	}

	@Override
	public Presenter<?> getContentPresenter() {
		return contentPresenter;
	}

	@Override
	public void close() {
		view.dispose();
	}

	@Override
	public void setWindowId(WindowId id) {
		// Remove old listener.
		if (this.id != null) {
			Document<?> document = application.getDocument(id);
			if (document != null) {
				document.removeListener(titleChangeListener);
			}
		}

		// Add listener so that the title gets updated when the document changes.
		Document<?> document = application.getDocument(id);
		if (document != null) {
			document.addListener(titleChangeListener);
		}

		this.id = id;
		view.setIcon(id.getType());
		updateTitle();
	}

	@Override
	public WindowId getWindowId() {
		return id;
	}

	@Override
	public void onActivated() {
		eventBus.post(new WindowFocusGainedEvent(id));
	}

	@Override
	public void focus() {
		view.focus();
	}

	@Override
	public void setPadding(int padding) {
		view.setPadding(padding);
	}

	private void updateTitle() {
		if (!title.isEmpty()) {
			computedTitle = title;
			view.setTitle(computedTitle);
			return;
		}

		Document<?> document = application.getDocument(id);
		if (document != null) {
			computedTitle = "";
			if (document.hasUnsavedChanges()) {
				computedTitle += "*";
			}

			if (document.getFile() != null) {
				computedTitle += String.format("%s (%s) (%s)", document.getName(), id.toString(), document.getFile());
			} else {
				computedTitle += String.format("%s (%s)", document.getName(), id.toString());
			}

			view.setTitle(computedTitle);
			return;
		}
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		updateTitle();
	}

	@Override
	public String getDisplayedTitle() {
		return computedTitle;
	}

	@Override
	public void addWindowListener(WindowListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeWindowListener(WindowListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void onWindowResized(int width, int height) {
		for (WindowListener listener : listeners) {
			listener.windowResized(id, width, height);
		}
	}

	@Override
	public void onDeactivated() {
		eventBus.post(new WindowFocusLostEvent(id));
	}

	@Override
	public Point getPosition() {
		return view.getPosition();
	}

	@Override
	public void setPosition(int x, int y) {
		view.ignoreNextWindowMovedEvent();
		view.setPosition(x, y);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		view.ignoreNextWindowMovedEvent();
		view.setBounds(x, y, width, height);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
