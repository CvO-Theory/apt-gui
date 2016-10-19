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

package uniol.aptgui.swing;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

public class Resource {

	private static Cursor getCursor(String path) {
		path = "/cursors/" + path;
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL url = Resource.class.getResource(path);
		Image image = tk.getImage(url);
		return tk.createCustomCursor(image, new Point(0, 0), path);
	}

	public static Cursor getCursorCreateEdge() {
		return getCursor("Arc32.gif");
	}

	private static ImageIcon getIcon(String path) {
		path = "/icons/" + path;
		URL url = Resource.class.getResource(path);
		return new ImageIcon(url);
	}

	public static ImageIcon getIconApplication() {
		return getIcon("Application32.png");
	}

	public static ImageIcon getIconArc() {
		return getIcon("Arc16.gif");
	}

	public static ImageIcon getIconColor() {
		return getIcon("Color16.gif");
	}

	public static ImageIcon getIconDelete() {
		return getIcon("Delete16.gif");
	}

	public static ImageIcon getIconExport() {
		return getIcon("Export16.gif");
	}

	public static ImageIcon getIconFireTransition() {
		return getIcon("FireTransition16.gif");
	}

	public static ImageIcon getIconHand() {
		return getIcon("Hand16.gif");
	}

	public static ImageIcon getIconFlow() {
		return getIconArc();
	}

	public static ImageIcon getIconImport() {
		return getIcon("Import16.gif");
	}

	public static ImageIcon getIconInitialState() {
		return getIcon("InitialState16.gif");
	}

	public static ImageIcon getIconLabel() {
		return getIcon("Label16.gif");
	}

	public static ImageIcon getIconLayout() {
		return getIcon("Layout16.gif");
	}

	public static ImageIcon getIconMultiplicity() {
		return getIcon("Multiplicity16.gif");
	}

	public static ImageIcon getIconNewFile() {
		return getIcon("New16.gif");
	}

	public static ImageIcon getIconOpenFile() {
		return getIcon("Open16.gif");
	}

	public static ImageIcon getIconPetriNetDocument() {
		return getIcon("NewPn16.gif");
	}

	public static ImageIcon getIconPlace() {
		return getIcon("Place16.gif");
	}

	public static ImageIcon getIconPlaceId() {
		return getIcon("PlaceId16.gif");
	}

	public static ImageIcon getIconRedo() {
		return getIcon("Redo16.gif");
	}

	public static ImageIcon getIconSaveAll() {
		return getIcon("SaveAll16.gif");
	}

	public static ImageIcon getIconSaveFile() {
		return getIcon("Save16.gif");
	}

	public static ImageIcon getIconSaveFileAs() {
		return getIcon("SaveAs16.gif");
	}

	public static ImageIcon getIconSelect() {
		return getIcon("Select16.gif");
	}

	public static ImageIcon getIconSpinner() {
		return getIcon("Spinner16.gif");
	}

	public static ImageIcon getIconState() {
		return getIconPlace();
	}

	public static ImageIcon getIconStateId() {
		return getIcon("StateId16.gif");
	}

	public static ImageIcon getIconTokens() {
		return getIcon("Tokens16.gif");
	}

	public static ImageIcon getIconTransition() {
		return getIcon("Transition16.gif");
	}

	public static ImageIcon getIconTransitionId() {
		return getIcon("TransitionId16.gif");
	}

	public static ImageIcon getIconTransitionSystemDocument() {
		return getIcon("NewTs16.gif");
	}

	public static ImageIcon getIconUndo() {
		return getIcon("Undo16.gif");
	}

	public static ImageIcon getIconZoomIn() {
		return getIcon("ZoomIn16.gif");
	}

	public static ImageIcon getIconZoomOut() {
		return getIcon("ZoomOut16.gif");
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
