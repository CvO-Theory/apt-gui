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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.io.FileType;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;
import uniol.aptgui.swing.filechooser.AptFileChooser;

@SuppressWarnings("serial")
public class SaveAction extends DocumentAction {

	@Inject
	public SaveAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Save";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconSaveFile());
		putValue(SHORT_DESCRIPTION, name);
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WindowId activeWindow = app.getActiveWindow();
		Document<?> document = app.getDocument(activeWindow);
		if (shouldShowSaveDialog(document)) {
			handleSaveDialogInteraction(document);
		} else {
			app.saveToFile(document, document.getFile(), document.getFileType());
		}
	}

	private void handleSaveDialogInteraction(Document<?> document) {
		AptFileChooser fc = AptFileChooser.saveChooser(document);
		Component parent = (Component) app.getMainWindow().getView();

		if (fc.performInteraction(parent)) {
			File file = fc.getSelectedFileWithExtension();
			FileType type = fc.getSelectedFileType();
			app.saveToFile(document, file, type);
		}
	}

	/**
	 * Returns true if a save dialog should be shown to the user.
	 *
	 * @param document
	 *                the document to save
	 * @return true if a save dialog should be shown to the user
	 */
	protected boolean shouldShowSaveDialog(Document<?> document) {
		return document.getFile() == null;
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return document.hasUnsavedChanges();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
