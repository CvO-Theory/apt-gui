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

package uniol.aptgui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.RenderingOptions;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.editor.layout.Layout;
import uniol.aptgui.editor.layout.LayoutOptions;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.io.parser.AptParser;
import uniol.aptgui.io.parser.DocumentParser;
import uniol.aptgui.io.renderer.DocumentRenderer;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.actions.SaveAction;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final EventBus eventBus;
	private final History history;
	private final RenderingOptions renderingOptions;
	private final EditingOptions editingOptions;
	private final LayoutOptions layoutOptions;
	private final SaveAction saveAction;

	/**
	 * Standard layout algorithm that gets applied when no layout information is available.
	 */
	private final Layout layout;

	/**
	 * ExecutorService for parallel task such as module executions.
	 */
	private final ExecutorService executor;

	/**
	 * Map from WindowIds to document objects. Each editor window should be
	 * in this map. Other windows must not be in this map.
	 */
	private final Map<WindowId, Document<?>> documents;

	/**
	 * Currently focused window id.
	 */
	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(
			MainWindowPresenter mainWindow,
			EventBus eventBus,
			History history,
			RenderingOptions renderingOptions,
			EditingOptions editingOptions,
			LayoutOptions layoutOptions,
			Layout defaultLayout,
			SaveAction saveAction) {
		this.mainWindow = mainWindow;
		this.eventBus = eventBus;
		this.history = history;
		this.documents = new HashMap<>();
		this.renderingOptions = renderingOptions;
		this.editingOptions = editingOptions;
		this.layoutOptions = layoutOptions;
		this.layout = defaultLayout;
		this.saveAction = saveAction;
		this.executor = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		activeWindow = e.getWindowId();
	}

	@Override
	public WindowId newPetriNet() {
		PetriNet pn = new PetriNet();
		Document<?> document = new PnDocument(pn);
		String name = mainWindow.showInputDialog("New Petri Net", "Petri net name:", "unnamed");
		if (name != null) {
			document.setName(name);
			return openDocument(document);
		}
		return null;
	}

	@Override
	public boolean closeWindow(WindowId id) {
		if (id.getType().isEditorWindow()) {
			Document<?> doc = getDocument(id);
			if (doc.hasUnsavedChanges() && askSaveDocument(id, getDocument(id))) {
				return false;
			}
		}

		if (activeWindow == id) {
			activeWindow = null;
		}
		mainWindow.removeWindow(id);
		documents.remove(id);
		return true;
	}

	@Override
	public void show() {
		mainWindow.show();
	}

	@Override
	public WindowId getActiveWindow() {
		return activeWindow;
	}

	@Override
	public WindowId newTransitionSystem() {
		TransitionSystem ts = new TransitionSystem();
		Document<?> document = new TsDocument(ts);
		String name = mainWindow.showInputDialog("New Transition System", "Transition system name:", "unnamed");
		if (name != null) {
			document.setName(name);
			return openDocument(document);
		}
		return null;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public History getHistory() {
		return history;
	}

	@Override
	public WindowId openFile(File file) {
		return openFile(file, new AptParser());
	}

	@Override
	public WindowId openFile(File file, DocumentParser parser) {
		try {
			return openDocument(parser.parse(file));
		} catch (ParseException|IOException e) {
			mainWindow.showException("Open Error", e);
			return null;
		}
	}

	@Override
	public MainWindowPresenter getMainWindow() {
		return mainWindow;
	}

	@Override
	public WindowId openDocument(Document<?> document) {
		if (document.getName().isEmpty()) {
			document.setName("unnamed");
		}

		WindowId id = mainWindow.createDocumentWindowId(document);
		documents.put(id, document);

		mainWindow.createDocumentEditorWindow(id, document);
		mainWindow.showInternalWindow(id);

		if (!document.hasCompleteLayout()) {
			// Apply standard layout.
			document.applyLayout(layout);
		}

		document.setVisible(true);
		mainWindow.focus(id);
		return id;
	}

	@Override
	public Document<?> getDocument(WindowId id) {
		return documents.get(id);
	}

	@Override
	public void saveToFile(Document<?> document, File file, DocumentRenderer renderer) {
		assert document != null;
		try {
			renderer.render(document, file);
		} catch (Exception e) {
			mainWindow.showException("Save Error", e);
		}
	}

	@Override
	public void openModule(Module module) {
		mainWindow.openModuleWindow(module);
	}

	@Override
	public void openModuleBrowser() {
		mainWindow.showModuleBrowser();
	}

	@Override
	public Set<WindowId> getDocumentWindows() {
		return Collections.unmodifiableSet(documents.keySet());
	}

	@Override
	public Collection<Document<?>> getDocuments() {
		return Collections.unmodifiableCollection(documents.values());
	}

	@Override
	public Document<?> getActiveDocument() {
		return documents.get(activeWindow);
	}

	@Override
	public void focusWindow(WindowId id) {
		mainWindow.focus(id);
	}

	@Override
	public String getWindowTitle(WindowId id) {
		return mainWindow.getWindowTitle(id);
	}

	/**
	 * Asks the user if he wants to save the given document and opens the
	 * save dialog if he does. Returns if the cancel button was clicked.
	 *
	 * @param id
	 *                window id of the document
	 * @param document
	 *                document in question
	 * @return true, if the user choose cancel
	 */
	private boolean askSaveDocument(WindowId id, Document<?> document) {
		Component parentComponent = (Component) getMainWindow().getView();
		int res = JOptionPane.showOptionDialog(parentComponent,
				"Do you want to save your changes to '" + getWindowTitle(id) + "'?",
				"Save changes?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (res == JOptionPane.CANCEL_OPTION) {
			return true;
		}
		if (res == JOptionPane.YES_OPTION) {
			focusWindow(id);
			saveAction.actionPerformed(null);
		}
		return false;
	}

	@Override
	public void closeNow() {
		layoutOptions.saveToUserPreferences();
		renderingOptions.saveToUserPreferences();
		editingOptions.saveToUserPreferences();
		mainWindow.close();
		executor.shutdownNow();
	}

	@Override
	public void close() {
		boolean abortExit = false;
		// Copy ids so that external windows (which when closed become internal windows)
		// don't mess up the process
		Set<WindowId> ids = new HashSet<>(documents.keySet());
		for (WindowId id : ids) {
			Document<?> document = documents.get(id);
			if (document.hasUnsavedChanges()) {
				if (!closeWindow(id)) {
					abortExit = true;
					break;
				}
			}
		}

		if (!abortExit) {
			closeNow();
		}
	}

	@Override
	public RenderingOptions getRenderingOptions() {
		return renderingOptions;
	}

	@Override
	public ExecutorService getExecutorService() {
		return executor;
	}

	@Override
	public EditingOptions getEditingOptions() {
		return editingOptions;
	}

	@Override
	public LayoutOptions getLayoutOptions() {
		return layoutOptions;
	}

	@Override
	public void openExtensionBrowser(Document<?> document) {
		mainWindow.showExtensionBrowser().select(document);
	}

	@Override
	public void openExtensionBrowser(Document<?> document, GraphicalElement elem) {
		mainWindow.showExtensionBrowser().select(document, elem);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
