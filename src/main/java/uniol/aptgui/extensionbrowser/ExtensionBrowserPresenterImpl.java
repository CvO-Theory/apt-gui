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

package uniol.aptgui.extensionbrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.extension.Extensible;
import uniol.apt.util.Pair;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.events.DocumentChangedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowOpenedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.swing.extensiontable.ExtensionTableModel;

public class ExtensionBrowserPresenterImpl extends AbstractPresenter<ExtensionBrowserPresenter, ExtensionBrowserView>
		implements ExtensionBrowserPresenter {

	private final Application application;
	private ExtensionTableModel extensionTableModel;

	private WindowRef documentSelection;
	private GraphicalElement elementSelection;

	@Inject
	public ExtensionBrowserPresenterImpl(ExtensionBrowserView view, Application application, EventBus eventBus) {
		super(view);
		this.application = application;
		eventBus.register(this);
	}

	@Subscribe
	public void onWindowOpenedEvent(WindowOpenedEvent e) {
		if (e.getWindowId().getType().isEditorWindow()) {
			Document<?> doc = application.getDocument(e.getWindowId());
			if (documentSelection != null) {
				// Keep current selection
				updateDocumentList(documentSelection.getDocument());
			} else {
				// Select new window
				updateDocumentList(doc);
			}
		}
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		if (e.getWindowId().getType().isEditorWindow()) {
			if (documentSelection != null && documentSelection.getWindowId() == e.getWindowId()) {
				// Clear selection if the selected document was
				// closed
				documentSelection = null;
			}
			updateDocumentList(null);
		}
	}

	@Subscribe
	public void onDocumentChangedEvent(DocumentChangedEvent e) {
		if (documentSelection.getDocument() == e.getDocument()) {
			updateElementList();
		}
	}

	private void updateDocumentList(Document<?> selection) {
		Set<WindowId> docWindows = application.getDocumentWindows();
		List<WindowRef> refList = new ArrayList<>();
		for (WindowId docId : docWindows) {
			// Add to ref list
			Document<?> doc = application.getDocument(docId);
			WindowRef ref = new WindowRef(docId, doc);
			refList.add(ref);
			// Select first document if none yet selected
			if (documentSelection == null || selection == doc) {
				documentSelection = ref;
				elementSelection = null;
			}
		}
		view.setDocumentList(refList);
		view.selectDocument(documentSelection);
		updateElementList();
	}

	@Override
	public void select(Document<?> document) {
		updateDocumentList(document);
	}

	@Override
	public void select(Document<?> document, GraphicalElement element) {
		assert document.getGraphicalElements().contains(element);
		updateDocumentList(document);
		elementSelection = element;
		updateElementList();
	}

	@Override
	public void onDocumentSelectionChanged(WindowRef selection) {
		documentSelection = selection;
		elementSelection = null;
		updateElementList();
	}

	private void updateElementList() {
		Document<?> doc = documentSelection.getDocument();
		List<GraphicalElement> elements = new ArrayList<>(doc.getGraphicalElements());
		if (elementSelection == null && !elements.isEmpty()) {
			elementSelection = elements.get(0);
		}
		view.setElementList(elements);
		view.selectElement(elementSelection);
		updateExtensionTable();
	}

	@Override
	public void onElementSelectionChanged(GraphicalElement selection) {
		elementSelection = selection;
		updateExtensionTable();
	}

	private void updateExtensionTable() {
		if (documentSelection != null && elementSelection != null) {
			Document<?> doc = documentSelection.getDocument();
			Extensible extensible = doc.getAssociatedModelElement(elementSelection);
			List<Pair<String, Object>> extensions = new ArrayList<>();
			// Remove GraphicalElement extension before showing them
			for (Pair<String, Object> ext : extensible.getExtensions()) {
				if (!ext.getFirst().equals(GraphicalElement.EXTENSION_KEY)
						&& !ext.getFirst().equals(GraphicalElement.EXTENSION_KEY_PERSISTENT)) {
					extensions.add(ext);
				}
			}
			extensionTableModel = new ExtensionTableModel(extensions);
			view.setExtensionTableModel(extensionTableModel);
		} else {
			view.setExtensionTableModel(new ExtensionTableModel());
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
