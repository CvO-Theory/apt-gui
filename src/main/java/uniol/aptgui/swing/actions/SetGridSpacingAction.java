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

package uniol.aptgui.swing.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.EditingOptions;

@SuppressWarnings("serial")
public class SetGridSpacingAction extends AbstractAction {

	private final Application app;

	@Inject
	public SetGridSpacingAction(Application app) {
		this.app = app;
		String name = "Set Grid Spacing";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditingOptions eo = app.getEditingOptions();
		String defaultValue = String.valueOf(eo.getGridSpacing());
		String input = app.getMainWindow().showInputDialog("Set Grid Spacing",
				"New width and height between grid lines:", defaultValue);
		if (input == null) {
			return;
		}

		try {
			int newValue = Integer.valueOf(input);
			if (newValue > 0) {
				eo.setGridSpacing(newValue);
				for (Document<?> doc : app.getDocuments()) {
					doc.fireDocumentDirty();
				}
			} else {
				showInvalidInputMessage();
			}
		} catch (NumberFormatException e1) {
			showInvalidInputMessage();
		}
	}

	private void showInvalidInputMessage() {
		app.getMainWindow().showMessage("Invalid Input", "Grid spacing must be a positive integer value.");
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
