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

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.EventBus;

import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.EditingOptions;
import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.layout.LayoutOptions;
import uniol.aptgui.io.FileType;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

/**
 * Main application interface. Contains high-level functions and provides
 * references to application-wide objects.
 */
public interface Application {

	/**
	 * Returns the application-wide event bus.
	 *
	 * @return the application-wide event bus
	 */
	EventBus getEventBus();

	/**
	 * Returns the application-wide command history which enables undo-redo
	 * functionality.
	 *
	 * @return the application-wide command history
	 */
	History getHistory();

	/**
	 * Returns the main window presenter.
	 *
	 * @return the main window presenter
	 */
	MainWindowPresenter getMainWindow();

	/**
	 * Returns the application-wide rendering options object.
	 *
	 * @return the application-wide rendering options object
	 */
	RenderingOptions getRenderingOptions();

	/**
	 * Returns the application-wide editing options object.
	 *
	 * @return the application-wide editing options object
	 */
	EditingOptions getEditingOptions();

	/**
	 * Returns the application-wide layout options object.
	 *
	 * @return the application-wide layout options object
	 */
	LayoutOptions getLayoutOptions();

	/**
	 * Returns the application-wide executor service that allows to submit
	 * tasks to be run in threads.
	 *
	 * @return the application-wide executor service
	 */
	ExecutorService getExecutorService();

	/**
	 * Shows the main window.
	 */
	void show();

	/**
	 * Closes the application and all its windows without asking about
	 * unsaved documents.
	 */
	void closeNow();

	/**
	 * Begins to close the application. For each unsaved document the user
	 * will be asked if he wants to save it.
	 */
	void close();

	/**
	 * Creates a new, empty Petri net editor window.
	 *
	 * @return the window id or null if the process was cancelled
	 */
	WindowId newPetriNet();

	/**
	 * Creates a new, empty transition system editor window.
	 *
	 * @return the window id or null if the process was cancelled
	 */
	WindowId newTransitionSystem();

	/**
	 * Tries to close the window with the given id. The user may abort this
	 * process (e.g. when choosing cancel after being asked if he wants to
	 * save his document before closing).
	 *
	 * @param id
	 *                window id
	 * @return true, if the window was closed
	 */
	boolean closeWindow(WindowId id);

	/**
	 * Focuses and brings the window with the given id to the front,
	 *
	 * @param id
	 *                window id
	 */
	void focusWindow(WindowId id);

	/**
	 * Returns the title of the window with the given id.
	 *
	 * @param id
	 *                id of the window
	 * @return title of the window
	 */
	String getWindowTitle(WindowId id);

	/**
	 * Returns the window id of the window that currently has the focus.
	 *
	 * @return window id or null if no window is active
	 */
	WindowId getActiveWindow();

	/**
	 * Returns the ids of all windows that are document editors as an
	 * unmodifiable set.
	 *
	 * @return the ids of all windows that are document editors
	 */
	Set<WindowId> getDocumentWindows();

	/**
	 * Returns an unmodifiable collection of all opened documents.
	 *
	 * @return an unmodifiable collection of all opened documents
	 */
	Collection<Document<?>> getDocuments();

	/**
	 * Returns the document attached to the window with the given id.
	 *
	 * @param id
	 *                window id
	 * @return the document or null if the id is not associated with an
	 *         editor window
	 */
	Document<?> getDocument(WindowId id);

	/**
	 * Returns the focused editor's document.
	 *
	 * @return the document or null if no editor is focused.
	 */
	Document<?> getActiveDocument();

	/**
	 * Opens the given file and shows its contents in an editor window.
	 *
	 * @param file
	 *                file to open
	 * @return id of the opened editor window or null if the file could not
	 *         be opened
	 */
	WindowId openFile(File file);

	/**
	 * Saves the given document to a file. The file will be set as the
	 * document's default save file.
	 *
	 * @param document
	 *                the document to save
	 * @param file
	 *                file to save to
	 * @param type
	 *                type of the saved file
	 */
	void saveToFile(Document<?> document, File file, FileType type);

	/**
	 * Opens a window that allows to use the given module.
	 *
	 * @param module
	 *                an apt module
	 */
	void openModule(Module module);

	/**
	 * Opens the module browser which allows the user to search for modules
	 * and open them.
	 */
	void openModuleBrowser();

	/**
	 * Opens the extension browser and pre-selects the given document.
	 *
	 * @param document
	 *                document to show in extension browser
	 */
	void openExtensionBrowser(Document<?> document);

	/**
	 * Opens the extension browser and pre-selects the given document and
	 * graphical element.
	 *
	 * @param document
	 *                document to show in extension browser
	 * @param elem
	 *                graphical element to show in extension browser
	 */
	void openExtensionBrowser(Document<?> document, GraphicalElement elem);

	/**
	 * Opens the given document in an editor window and returns the window
	 * id.
	 *
	 * @param document
	 *                document to open
	 * @return if of the opened editor window
	 */
	WindowId openDocument(Document<?> document);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
