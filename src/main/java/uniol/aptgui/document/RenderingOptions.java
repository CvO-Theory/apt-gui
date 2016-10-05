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
 * Saves additional information used during the document drawing process.
 */
public class RenderingOptions {

	private static final String PREF_KEY_STATE = "stateIdLabelVisible";
	private static final String PREF_KEY_PLACE = "placeIdLabelVisible";
	private static final String PREF_KEY_TRANSITION = "transitionIdLabelVisible";
	private static final String PREF_KEY_GRID = "gridVisible";
	private static final String PREF_KEY_EXP_BORDER = "exportDocumentBorderSize";
	private static final String PREF_KEY_EXP_MAGNIFICATION = "exportBitmapMagnification";

	/**
	 * Creates a RenderingOptions object from saved user preferences or
	 * default values if no user preferences exist.
	 *
	 * @return configured RenderingOptions object
	 */
	public static RenderingOptions fromUserPreferences() {
		// Create RenderingOptions with default values
		RenderingOptions ro = new RenderingOptions();

		// Retrieve preferences
		Preferences prefs = Preferences.userNodeForPackage(RenderingOptions.class);
		boolean s = prefs.getBoolean(PREF_KEY_STATE, ro.isStateIdLabelVisible());
		boolean p = prefs.getBoolean(PREF_KEY_PLACE, ro.isPlaceIdLabelVisible());
		boolean t = prefs.getBoolean(PREF_KEY_TRANSITION, ro.isTransitionIdLabelVisible());
		boolean g = prefs.getBoolean(PREF_KEY_GRID, ro.isGridVisible());
		int expBorder = prefs.getInt(PREF_KEY_EXP_BORDER, ro.getExportDocumentBorderSize());
		int expMagnification = prefs.getInt(PREF_KEY_EXP_MAGNIFICATION, ro.getExportBitmapMagnification());

		// Configure RenderingOptions object
		ro.setStateIdLabelVisible(s);
		ro.setPlaceIdLabelVisible(p);
		ro.setTransitionIdLabelVisible(t);
		ro.setGridVisible(g);
		ro.setExportDocumentBorderSize(expBorder);
		ro.setExportBitmapMagnification(expMagnification);
		return ro;
	}

	private boolean stateIdLabelVisible;
	private boolean placeIdLabelVisible;
	private boolean transitionIdLabelVisible;
	private boolean gridVisible;
	private int exportDocumentBorderSize;
	private int exportBitmapMagnification;

	/**
	 * Creates a RenderingOptions object configured with default values.
	 */
	public RenderingOptions() {
		this.stateIdLabelVisible = true;
		this.placeIdLabelVisible = true;
		this.transitionIdLabelVisible = true;
		this.gridVisible = false;
		this.exportDocumentBorderSize = 20;
		this.exportBitmapMagnification = 3;
	}

	/**
	 * Saves the attributes of this RenderingOptions object to the user
	 * preferences store.
	 */
	public void saveToUserPreferences() {
		Preferences prefs = Preferences.userNodeForPackage(RenderingOptions.class);
		prefs.putBoolean(PREF_KEY_STATE, stateIdLabelVisible);
		prefs.putBoolean(PREF_KEY_PLACE, placeIdLabelVisible);
		prefs.putBoolean(PREF_KEY_TRANSITION, transitionIdLabelVisible);
		prefs.putBoolean(PREF_KEY_GRID, gridVisible);
	}

	public boolean isStateIdLabelVisible() {
		return stateIdLabelVisible;
	}

	public void setStateIdLabelVisible(boolean stateIdLabelVisible) {
		this.stateIdLabelVisible = stateIdLabelVisible;
	}

	public boolean toggleStateIdLabelVisible() {
		stateIdLabelVisible = !stateIdLabelVisible;
		return stateIdLabelVisible;
	}

	public boolean isPlaceIdLabelVisible() {
		return placeIdLabelVisible;
	}

	public void setPlaceIdLabelVisible(boolean placeIdLabelVisible) {
		this.placeIdLabelVisible = placeIdLabelVisible;
	}

	public boolean togglePlaceIdLabelVisible() {
		placeIdLabelVisible = !placeIdLabelVisible;
		return placeIdLabelVisible;
	}

	public boolean isTransitionIdLabelVisible() {
		return transitionIdLabelVisible;
	}

	public void setTransitionIdLabelVisible(boolean transitionIdLabelVisible) {
		this.transitionIdLabelVisible = transitionIdLabelVisible;
	}

	public boolean toggleTransitionIdLabelVisible() {
		transitionIdLabelVisible = !transitionIdLabelVisible;
		return transitionIdLabelVisible;
	}

	public boolean isGridVisible() {
		return gridVisible;
	}

	public void setGridVisible(boolean gridVisible) {
		this.gridVisible = gridVisible;
	}

	public boolean toggleGridVisible() {
		gridVisible = !gridVisible;
		return gridVisible;
	}

	public int getExportDocumentBorderSize() {
		return exportDocumentBorderSize;
	}

	public void setExportDocumentBorderSize(int exportDocumentBorderSize) {
		this.exportDocumentBorderSize = exportDocumentBorderSize;
	}

	public int getExportBitmapMagnification() {
		return exportBitmapMagnification;
	}

	public void setExportBitmapMagnification(int exportBitmapMagnification) {
		this.exportBitmapMagnification = exportBitmapMagnification;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
