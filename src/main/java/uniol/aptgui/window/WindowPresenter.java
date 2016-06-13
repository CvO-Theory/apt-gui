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

import uniol.aptgui.Presenter;
import uniol.aptgui.View;
import uniol.aptgui.mainwindow.WindowId;

public interface WindowPresenter {

	/// ACTIONS ///

	/**
	 * Returns the associated view.
	 *
	 * @return the associated view.
	 */
	View<?> getView();

	/**
	 * Closes this window.
	 */
	void close();

	/**
	 * Sets the content presenter associated with this window. Its view will
	 * be shown inside this window.
	 *
	 * @param presenter
	 *                the new content presenter
	 */
	void setContentPresenter(Presenter<?> presenter);

	/**
	 * Returns the content presenter associated with this window.
	 *
	 * @return the content presenter associated with this window
	 */
	Presenter<?> getContentPresenter();

	/**
	 * Sets the WindowId associated with this window.
	 *
	 * @param id
	 *                the new WindowId
	 */
	void setWindowId(WindowId id);

	/**
	 * Sets the window title.
	 *
	 * @param title
	 *                the new title
	 */
	void setTitle(String title);

	/**
	 * Returns the window title that is actually visible to the user. It may
	 * contain more information than was set using setTitle.
	 *
	 * @return the window title
	 */
	String getDisplayedTitle();

	/**
	 * Returns the WindowId associated with this window.
	 *
	 * @return the WindowId associated with this window
	 */
	WindowId getWindowId();

	/**
	 * Focuses this window.
	 */
	void focus();

	/**
	 * Sets the size in pixels of the padding area around the content
	 * presenter view.
	 *
	 * @param padding
	 *                padding size in pixels
	 */
	void setPadding(int padding);

	/**
	 * Adds a listener that gets notified about window events.
	 *
	 * @param listener
	 *                the listener
	 */
	void addWindowListener(WindowListener listener);

	/**
	 * Removes a listener.
	 *
	 * @param listener
	 *                the listener
	 */
	void removeWindowListener(WindowListener listener);

	/**
	 * Returns the position of this window in relation to its parent
	 * container.
	 *
	 * @return position of the window
	 */
	Point getPosition();

	/**
	 * Sets the position of this window in relation to its parent container.
	 *
	 * @param x
	 *                x position
	 * @param y
	 *                y position
	 */
	void setPosition(int x, int y);

	/**
	 * Sets the window bounds.
	 *
	 * @param x
	 *                x position in relation to parent
	 * @param y
	 *                y position in relation to parent
	 * @param width
	 *                window width
	 * @param height
	 *                window height
	 */
	void setBounds(int x, int y, int width, int height);

	/// VIEW EVENTS ///

	/**
	 * Called by the view when the close button of the window was clicked.
	 */
	void onCloseButtonClicked();

	/**
	 * Called by the view when the window receives focus.
	 */
	void onActivated();

	/**
	 * Called by the view when the window loses focus.
	 */
	void onDeactivated();

	/**
	 * Called by the view when the window has been resized.
	 *
	 * @param width
	 *                width of the content pane
	 * @param height
	 *                height of the content pane
	 */
	void onWindowResized(int width, int height);

	/**
	 * Called by the view when the window has been moved.
	 *
	 * @param newX
	 *                new x position
	 * @param newY
	 *                new y position
	 */
	void onWindowMoved(int newX, int newY);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
