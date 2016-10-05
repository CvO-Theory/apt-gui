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
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.events.DocumentChangedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowOpenedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.Resource;

@SuppressWarnings("serial")
public class SaveAllAction extends AbstractAction {

	private final Application app;
	private final SaveAction saveAction;

	@Inject
	public SaveAllAction(Application app, EventBus eventBus, SaveAction saveAction) {
		this.app = app;
		this.saveAction = saveAction;
		String name = "Save All";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconSaveAll());
		putValue(SHORT_DESCRIPTION, name);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift S"));
		setEnabled(false);
		eventBus.register(this);
	}

	@Subscribe
	public void onDocumentChangedEvent(DocumentChangedEvent e) {
		updateEnabledStatus();
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		updateEnabledStatus();
	}

	@Subscribe
	public void onWindowOpenedEvent(WindowOpenedEvent e) {
		updateEnabledStatus();
	}

	private void updateEnabledStatus() {
		for (Document<?> document : app.getDocuments()) {
			if (document.hasUnsavedChanges()) {
				setEnabled(true);
				return;
			}
		}
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Set<WindowId> windowIds = new HashSet<>(app.getDocumentWindows());
		for (WindowId id : windowIds) {
			Document<?> document = app.getDocument(id);
			if (document.hasUnsavedChanges()) {
				saveDocument(id, document);
			}
		}
	}

	private void saveDocument(WindowId id, Document<?> document) {
		app.focusWindow(id);
		saveAction.actionPerformed(null);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
