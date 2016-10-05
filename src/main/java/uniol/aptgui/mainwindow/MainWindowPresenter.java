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

import uniol.apt.module.Module;
import uniol.aptgui.Presenter;
import uniol.aptgui.document.Document;
import uniol.aptgui.extensionbrowser.ExtensionBrowserPresenter;

public interface MainWindowPresenter extends Presenter<MainWindowView> {

	/// ACTIONS ///

	/**
	 * Shows the main window.
	 */
	void show();

	/**
	 * Closes the main window.
	 */
	void close();

	/**
	 * Creates a new WindowId for the given document.
	 *
	 * @param document
	 *                a PnDocument or a TsDocument
	 * @return a new WindowId
	 */
	WindowId createDocumentWindowId(Document<?> document);

	/**
	 * Creates a new internal window with a document editor as the content
	 * presenter.
	 *
	 * @param id
	 *                window id to use
	 * @param document
	 *                document to edit
	 */
	void createDocumentEditorWindow(WindowId id, Document<?> document);

	/**
	 * Adds the window identified by the given id to the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void showInternalWindow(WindowId id);

	/**
	 * Removes the window identified by the given id from the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void removeWindow(WindowId id);

	/**
	 * Focuses and brings the window identified by the given id to the front
	 * of the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void focus(WindowId id);

	/**
	 * Opens and brings the extension browser to front.
	 *
	 * @return the extension browser presenter
	 */
	ExtensionBrowserPresenter showExtensionBrowser();

	/**
	 * Opens and brings the module browser window to front.
	 */
	void showModuleBrowser();

	/**
	 * Opens a window that allows to run the given module.
	 *
	 * @param module
	 *                module whose settings should be displayed in the
	 *                window
	 */
	void openModuleWindow(Module module);

	/**
	 * Returns the title of the window with the given id.
	 *
	 * @param id
	 *                window id
	 * @return window title
	 */
	String getWindowTitle(WindowId id);

	/**
	 * Shows a dialog that asks for user input.
	 *
	 * @param title
	 *                the dialog's title
	 * @param prompt
	 *                the prompt text
	 * @param defaultValue
	 *                the default value of the input text field
	 * @return the user input or null if the dialog was cancelled
	 */
	String showInputDialog(String title, String prompt, String defaultValue);

	/**
	 * Displays a message box with the given title and message.
	 *
	 * @param title
	 *                dialog title
	 * @param message
	 *                message contents
	 */
	void showMessage(String title, String message);

	/**
	 * Shows a dialog containing the exception message.
	 *
	 * @param title
	 *                dialog title
	 * @param exception
	 *                exception that will be shown to the user
	 */
	void showException(String title, Exception exception);

	/**
	 * Returns a suitable dialog parent component depending on the active
	 * window.
	 *
	 * @return a suitable dialog parent component
	 */
	Component getDialogParent();

	/**
	 * Cascades the internal windows.
	 */
	void cascadeWindows();

	/**
	 * Arranges the internal editor windows in tiles.
	 */
	void tileEditorWindows();

	/**
	 * Transforms an internal window to an external window.
	 *
	 * @param id
	 *                the window to transform
	 * @param origin
	 *                window position
	 */
	void transformToExternalWindow(WindowId id, Point origin);

	/**
	 * Transforms an external window to an internal window.
	 *
	 * @param id
	 *                the window to transform
	 */
	void transformToInternalWindow(WindowId id);

	/**
	 * Returns if the window with the given id is an internal window.
	 *
	 * @param id
	 *                window id
	 * @return true if the window is internal; false if the window is
	 *         external
	 */
	boolean isInternalWindow(WindowId id);

	/**
	 * Returns the bounds of the main window.
	 *
	 * @return the bounds of the main window
	 */
	Rectangle getMainWindowBounds();

	/**
	 * Unfocuses all internal windows.
	 */
	void unfocusAllInternalWindows();

	/// VIEW EVENTS ///

	/**
	 * Called by the view when the close button is clicked.
	 */
	void onCloseButtonClicked();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
