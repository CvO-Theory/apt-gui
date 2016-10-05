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

package uniol.aptgui.document;

import java.util.prefs.Preferences;

/**
 * Saves additional information used to define editor behavior.
 */
public class EditingOptions {

	private static final String PREF_KEY_SNAP = "snapToGridEnabled";
	private static final String PREF_KEY_GRID_SPACING = "gridSpacing";

	/**
	 * Creates a EditingOptions object from saved user preferences or
	 * default values if no user preferences exist.
	 *
	 * @return configured EditingOptions object
	 */
	public static EditingOptions fromUserPreferences() {
		// Create EditingOptions with default values
		EditingOptions eo = new EditingOptions();

		// Retrieve preferences
		Preferences prefs = Preferences.userNodeForPackage(EditingOptions.class);
		boolean s = prefs.getBoolean(PREF_KEY_SNAP, eo.isSnapToGridEnabled());
		int gridSpacing = prefs.getInt(PREF_KEY_GRID_SPACING, eo.getGridSpacing());

		// Configure EditingOptions object
		eo.setSnapToGridEnabled(s);
		eo.setGridSpacing(gridSpacing);
		return eo;
	}

	private boolean snapToGridEnabled;
	private int gridSpacing;

	/**
	 * Creates a EditingOptions object configured with default values.
	 */
	public EditingOptions() {
		this.snapToGridEnabled = true;
		this.gridSpacing = 50;
	}

	/**
	 * Saves the attributes of this EditingOptions object to the user
	 * preferences store.
	 */
	public void saveToUserPreferences() {
		Preferences prefs = Preferences.userNodeForPackage(EditingOptions.class);
		prefs.putBoolean(PREF_KEY_SNAP, snapToGridEnabled);
		prefs.putInt(PREF_KEY_GRID_SPACING, gridSpacing);
	}

	public boolean isSnapToGridEnabled() {
		return snapToGridEnabled;
	}

	public void setSnapToGridEnabled(boolean snapToGridEnabled) {
		this.snapToGridEnabled = snapToGridEnabled;
	}

	public boolean toggleSnapToGridEnabled() {
		snapToGridEnabled = !snapToGridEnabled;
		return snapToGridEnabled;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
