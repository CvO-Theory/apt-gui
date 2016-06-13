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

package uniol.aptgui.editor.layout;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * Saves additional information for necessary for layout.
 */
public class LayoutOptions {

	private static final String PREF_KEY_DOT_PATH = "graphvizDotPath";

	/**
	 * Creates a LayoutOptions object from saved user preferences or default
	 * values if no user preferences exist.
	 *
	 * @return configured LayoutOptions object
	 */
	public static LayoutOptions fromUserPreferences() {
		// Create with default values
		LayoutOptions lo = new LayoutOptions();

		// Retrieve preferences
		Preferences prefs = Preferences.userNodeForPackage(LayoutOptions.class);
		String dot = prefs.get(PREF_KEY_DOT_PATH, lo.getGraphvizDotPath());

		// Configure object
		lo.setGraphvizDotPath(dot);
		return lo;
	}

	private String graphvizDotPath;

	/**
	 * Creates a LayoutOptions object configured with default values.
	 */
	public LayoutOptions() {
		this.graphvizDotPath = "dot";
	}

	/**
	 * Saves the attributes of this LayoutOptions object to the user
	 * preferences store.
	 */
	public void saveToUserPreferences() {
		Preferences prefs = Preferences.userNodeForPackage(LayoutOptions.class);
		prefs.put(PREF_KEY_DOT_PATH, graphvizDotPath);
	}

	public String getGraphvizDotPath() {
		return graphvizDotPath;
	}

	public File getGraphvizDotPathAsFile() {
		return new File(graphvizDotPath);
	}

	public void setGraphvizDotPath(String graphvizDotPath) {
		this.graphvizDotPath = graphvizDotPath;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
