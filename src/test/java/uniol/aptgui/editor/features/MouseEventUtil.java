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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Utility class for easy testing with MouseEvents.
 */
public class MouseEventUtil {

	/**
	 * Returns a mocked {@link MouseEvent} that will report a left-click at
	 * the given position.
	 *
	 * @param x
	 *                x position
	 * @param y
	 *                y position
	 * @return the mouse event
	 */
	public static MouseEvent leftClickAt(int x, int y) {
		return clickAt(MouseEvent.BUTTON1, x, y);
	}

	/**
	 * Returns a mocked {@link MouseEvent} that will report a right-click at
	 * the given position.
	 *
	 * @param x
	 *                x position
	 * @param y
	 *                y position
	 * @return the mouse event
	 */
	public static MouseEvent rightClickAt(int x, int y) {
		return clickAt(MouseEvent.BUTTON2, x, y);
	}

	private static MouseEvent clickAt(int type, int x, int y) {
		MouseEvent leftClick = mock(MouseEvent.class);
		when(leftClick.getButton()).thenReturn(type);
		when(leftClick.getPoint()).thenReturn(new Point(x, y));
		return leftClick;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
