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

package uniol.aptgui.mainwindow;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.module.Module;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.EditorPresenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.DocumentListener;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.events.DocumentChangedEvent;
import uniol.aptgui.events.DocumentSelectionChangedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowOpenedEvent;
import uniol.aptgui.mainwindow.menu.MenuPresenter;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.module.ModulePresenter;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;
import uniol.aptgui.window.WindowListener;
import uniol.aptgui.window.WindowPresenter;
import uniol.aptgui.window.external.ExternalWindowPresenter;
import uniol.aptgui.window.internal.InternalWindowPresenter;

public class MainWindowPresenterImpl extends AbstractPresenter<MainWindowPresenter, MainWindowView>
		implements MainWindowPresenter {

	/**
	 * Main application title.
	 */
	private static final String TITLE = "APT";

	/**
	 * When a new window is created its starting position is offset from the
	 * currently active document by this much.
	 */
	private static final int WINDOW_CREATION_CASCADE_OFFSET = 30;

	/**
	 * Reference to the global application instance.
	 */
	private final Application application;

	/**
	 * Reference to the global event bus instance.
	 */
	private final EventBus eventBus;

	/**
	 * Reference to the injector to create new InternalWindowPresenters.
	 */
	private final Injector injector;

	/**
	 * Map that contains all internal windows by id.
	 */
	private final Map<WindowId, InternalWindowPresenter> internalWindows;

	/**
	 * Map that contains all external windows by id.
	 */
	private final Map<WindowId, ExternalWindowPresenter> externalWindows;

	/**
	 * Toolbar presenter.
	 */
	@SuppressWarnings("unused")
	private final ToolbarPresenter toolbar;

	/**
	 * Main menu presenter.
	 */
	private final MenuPresenter menu;

	/**
	 * Module browser window id.
	 */
	private WindowId moduleBrowserWindowId;

	/**
	 * Listener for Document events. It updates the menu so that an
	 * up-to-date window list can be shown and also broadcasts selection
	 * change events so that actions can be enabled or disabled correctly.
	 */
	private final DocumentListener documentListener = new DocumentListener() {
		@Override public void onSelectionChanged(Document<?> source) {
			eventBus.post(new DocumentSelectionChangedEvent(source));
		}
		@Override public void onDocumentDirty(Document<?> source) {
		}
		@Override
		public void onDocumentChanged(Document<?> source) {
			updateWindowMenu();
			updateMainTitle();
			eventBus.post(new DocumentChangedEvent(source));
		}
	};

	/**
	 * Listener for window events that updates the document size when the
	 * window size changes.
	 */
	private final WindowListener windowListener = new WindowListener() {
		@Override
		public void windowResized(WindowId id, int width, int height) {
			Document<?> document = application.getDocument(id);
			assert document != null;
			document.getViewport().setWidth(width);
			document.getViewport().setHeight(height);
		}
	};

	@Inject
	public MainWindowPresenterImpl(
		MainWindowView view,
		Application application,
		EventBus eventBus,
		Injector injector,
		ToolbarPresenter toolbar,
		MenuPresenter menu
	) {
		super(view);
		this.application = application;
		this.eventBus = eventBus;
		this.injector = injector;
		this.internalWindows = new HashMap<>();
		this.externalWindows = new HashMap<>();
		this.toolbar = toolbar;
		this.menu = menu;

		view.setToolbar(toolbar.getView());
		view.setMenu(menu.getView());
		view.setTitle(TITLE);

		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		updateMainTitle();
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		updateMainTitle();
	}

	@Override
	public void show() {
		getView().setVisible(true);
	}

	@Override
	public void close() {
		getView().close();
		for (ExternalWindowPresenter extWindow : externalWindows.values()) {
			extWindow.close();
		}
	}

	@Override
	public void removeWindow(WindowId id) {
		WindowPresenter window = null;
		if (internalWindows.containsKey(id)) {
			window = removeInternalWindow(id);
		} else if (externalWindows.containsKey(id)) {
			window = removeExternalWindow(id);
		}

		if (window != null) {
			Document<?> document = application.getDocument(id);
			if (document != null) {
				document.removeListener(documentListener);
				window.removeWindowListener(windowListener);
			}
			updateWindowMenu();
			eventBus.post(new WindowClosedEvent(id));
		}
	}

	private WindowPresenter removeInternalWindow(WindowId id) {
		WindowPresenter intWindow = internalWindows.remove(id);
		view.removeInternalWindow(intWindow.getView());
		intWindow.close();
		return intWindow;
	}

	private WindowPresenter removeExternalWindow(WindowId id) {
		WindowPresenter extWindow = externalWindows.remove(id);
		extWindow.close();
		return extWindow;
	}

	@Override
	public void onCloseButtonClicked() {
		application.close();
	}

	@Override
	public void showInternalWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		view.addInternalWindow(window.getView());

		// Set window starting position in relation to currently active window.
		WindowId activeId = application.getActiveWindow();
		if (activeId != null && activeId != id && isInternalWindow(activeId)) {
			// Offset window from currently active window.
			InternalWindowPresenter active = internalWindows.get(activeId);
			Point activePos = active.getPosition();
			window.setPosition(
				activePos.x + WINDOW_CREATION_CASCADE_OFFSET,
				activePos.y + WINDOW_CREATION_CASCADE_OFFSET
			);
		} else {
			// Default starting position
			window.setPosition(WINDOW_CREATION_CASCADE_OFFSET, WINDOW_CREATION_CASCADE_OFFSET);
		}

		updateWindowMenu();
		eventBus.post(new WindowOpenedEvent(id));
	}

	@Override
	public void focus(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		window.focus();
	}

	@Override
	public void createDocumentEditorWindow(WindowId id, Document<?> document) {
		EditorPresenter editor = injector.getInstance(EditorPresenter.class);
		editor.setDocument(document);
		editor.setWindowId(id);

		InternalWindowPresenter iwp = createInternalWindow(id, editor);
		iwp.addWindowListener(windowListener);

		document.addListener(documentListener);
	}

	@Override
	public void showModuleBrowser() {
		if (!internalWindows.containsKey(moduleBrowserWindowId)) {
			moduleBrowserWindowId = new WindowId(WindowType.MODULE_BROWSER);
			ModuleBrowserPresenter moduleBrowser = injector.getInstance(ModuleBrowserPresenter.class);
			InternalWindowPresenter iwp = createInternalWindow(moduleBrowserWindowId, moduleBrowser);
			iwp.setTitle("Module Browser");
			showInternalWindow(moduleBrowserWindowId);
		}

		focus(moduleBrowserWindowId);
	}

	@Override
	public void openModuleWindow(Module module) {
		WindowId id = new WindowId(WindowType.MODULE);

		ModulePresenter modulePresenter = injector.getInstance(ModulePresenter.class);
		modulePresenter.setModule(module);
		modulePresenter.setWindowId(id);

		InternalWindowPresenter iwp = createInternalWindow(id, modulePresenter);
		iwp.setTitle(module.getTitle());

		showInternalWindow(id);
		focus(id);
	}

	@Override
	public WindowId createDocumentWindowId(Document<?> document) {
		if (document instanceof PnDocument) {
			return new WindowId(WindowType.PETRI_NET);
		}
		if (document instanceof TsDocument) {
			return new WindowId(WindowType.TRANSITION_SYSTEM);
		}
		assert false;
		return null;
	}

	@Override
	public String getWindowTitle(WindowId id) {
		if (internalWindows.containsKey(id)) {
			return internalWindows.get(id).getDisplayedTitle();
		} else {
			return externalWindows.get(id).getDisplayedTitle();
		}
	}

	/**
	 * Creates a new InternalWindowPresenter with the given Presenter as its
	 * content presenter.
	 *
	 * @param id
	 *                the WindowId
	 * @param contentPresenter
	 *                the content/child presenter
	 * @return a new InternalWindowPresenter
	 */
	private InternalWindowPresenter createInternalWindow(WindowId id, Presenter<?> contentPresenter) {
		InternalWindowPresenter window = injector.getInstance(InternalWindowPresenter.class);
		window.setWindowId(id);
		window.setContentPresenter(contentPresenter);
		internalWindows.put(id, window);
		return window;
	}

	/**
	 * Creates a new ExternalWindowPresenter with the given Presenter as its
	 * content presenter.
	 *
	 * @param id
	 *                the WindowId
	 * @param contentPresenter
	 *                the content/child presenter
	 * @return a new InternalWindowPresenter
	 */
	private ExternalWindowPresenter createExternalWindow(WindowId id, Presenter<?> contentPresenter) {
		ExternalWindowPresenter window = injector.getInstance(ExternalWindowPresenter.class);
		window.setWindowId(id);
		window.setContentPresenter(contentPresenter);
		externalWindows.put(id, window);
		return window;
	}

	/**
	 * Updates the Menu presenter with the current set of windows.
	 */
	private void updateWindowMenu() {
		menu.setInternalWindows(internalWindows.keySet());
	}

	/**
	 * Updates the main window title so that the currently active internal
	 * window title is a part of it.
	 */
	private void updateMainTitle() {
		WindowId active = application.getActiveWindow();
		if (active != null) {
			view.setTitle(getWindowTitle(active) + " - " + TITLE);
		} else {
			view.setTitle(TITLE);
		}
	}

	@Override
	public void cascadeWindows() {
		view.cascadeInternalWindows();
	}

	@Override
	public void showException(String title, Exception exception) {
		JOptionPane.showMessageDialog(getDialogParent(),
				exception.getClass().getName() + System.lineSeparator() + exception.getMessage(),
				title,
				JOptionPane.ERROR_MESSAGE);
		exception.printStackTrace();
	}

	@Override
	public void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(getDialogParent(),
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String showInputDialog(String title, String prompt, String defaultValue) {
		return (String) JOptionPane.showInputDialog(getDialogParent(), prompt, title,
				JOptionPane.QUESTION_MESSAGE, null, null, defaultValue);
	}

	@Override
	public Component getDialogParent() {
		WindowId active = application.getActiveWindow();
		if (externalWindows.containsKey(active)) {
			return (Component) externalWindows.get(active).getView();
		} else if (internalWindows.containsKey(active)) {
			return (Component) internalWindows.get(active).getView();
		} else {
			return (Component) view;
		}
	}

	@Override
	public void transformToExternalWindow(WindowId id, Point origin) {
		InternalWindowPresenter intWindow = internalWindows.remove(id);
		view.removeInternalWindow(intWindow.getView());
		intWindow.close();
		ExternalWindowPresenter extWindow = createExternalWindow(id, intWindow.getContentPresenter());
		extWindow.setPosition(origin.x, origin.y);
	}

	@Override
	public void transformToInternalWindow(WindowId id) {
		ExternalWindowPresenter extWindow = externalWindows.remove(id);
		extWindow.close();

		createInternalWindow(id, extWindow.getContentPresenter());
		showInternalWindow(id);
		focus(id);
	}

	@Override
	public boolean isInternalWindow(WindowId id) {
		return internalWindows.containsKey(id);
	}

	@Override
	public Rectangle getMainWindowBounds() {
		return view.getBounds();
	}

	@Override
	public void unfocusAllInternalWindows() {
		view.unfocusAllInternalWindows();
	}

	@Override
	public void tileEditorWindows() {
		List<InternalWindowPresenter> editors = new ArrayList<>();
		for (InternalWindowPresenter iwp : internalWindows.values()) {
			if (iwp.getWindowId().getType().isEditorWindow()) {
				editors.add(iwp);
			}
		}

		if (editors.isEmpty()) {
			return;
		}

		Rectangle bounds = view.getDesktopPaneBounds();
		int rows = (int) Math.floor(Math.sqrt(editors.size()));
		int cols = (int) Math.ceil(Math.sqrt(editors.size()));
		if (rows * cols < editors.size()) {
			rows += 1;
		}
		int windowWidth = bounds.width / cols;
		int windowHeight = bounds.height / rows;

		Iterator<InternalWindowPresenter> iter = editors.iterator();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (!iter.hasNext()) {
					return;
				}

				InternalWindowPresenter iwp = iter.next();
				int x = col * windowWidth;
				int y = row * windowHeight;
				iwp.setBounds(x, y, windowWidth, windowHeight);
			}
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
