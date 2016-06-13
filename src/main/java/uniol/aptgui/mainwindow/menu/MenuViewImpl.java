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

package uniol.aptgui.mainwindow.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.module.Module;
import uniol.aptgui.Application;
import uniol.aptgui.editor.document.EditingOptions;
import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.JMenuBarView;
import uniol.aptgui.swing.actions.CascadeWindowsAction;
import uniol.aptgui.swing.actions.DeleteElementsAction;
import uniol.aptgui.swing.actions.DotLayoutAction;
import uniol.aptgui.swing.actions.ExitAction;
import uniol.aptgui.swing.actions.ExportAction;
import uniol.aptgui.swing.actions.ImportAction;
import uniol.aptgui.swing.actions.ModuleAction;
import uniol.aptgui.swing.actions.ModuleBrowserAction;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.OpenAction;
import uniol.aptgui.swing.actions.RandomLayoutAction;
import uniol.aptgui.swing.actions.RedoAction;
import uniol.aptgui.swing.actions.RenameDocumentAction;
import uniol.aptgui.swing.actions.SaveAction;
import uniol.aptgui.swing.actions.SaveAllAction;
import uniol.aptgui.swing.actions.SaveAsAction;
import uniol.aptgui.swing.actions.SetColorAction;
import uniol.aptgui.swing.actions.SetGridSpacingAction;
import uniol.aptgui.swing.actions.SetGridVisibleAction;
import uniol.aptgui.swing.actions.SetInitialStateAction;
import uniol.aptgui.swing.actions.SetLabelAction;
import uniol.aptgui.swing.actions.SetPlaceIdLabelVisibleAction;
import uniol.aptgui.swing.actions.SetSnapToGridAction;
import uniol.aptgui.swing.actions.SetStateIdLabelVisibleAction;
import uniol.aptgui.swing.actions.SetTokensAction;
import uniol.aptgui.swing.actions.SetTransitionIdLabelVisibleAction;
import uniol.aptgui.swing.actions.ShowWindowAction;
import uniol.aptgui.swing.actions.UndoAction;
import uniol.aptgui.swing.actions.ZoomDecreaseAction;
import uniol.aptgui.swing.actions.ZoomFitWindowAction;
import uniol.aptgui.swing.actions.ZoomIncreaseAction;

@SuppressWarnings("serial")
public class MenuViewImpl extends JMenuBarView<MenuPresenter> implements MenuView {

	private final JMenu fileMenu;
	private final JMenuItem newPn;
	private final JMenuItem newTs;
	private final JMenuItem open;
	private final JMenuItem save;
	private final JMenuItem saveAs;
	private final JMenuItem saveAll;
	private final JMenuItem import_;
	private final JMenuItem export;
	private final JMenuItem exit;

	private final JMenu editMenu;
	private final JMenuItem undo;
	private final JMenuItem redo;
	private final JMenuItem snapToGrid;
	private final JMenuItem setColor;
	private final JMenuItem setLabel;
	private final JMenuItem setTokens;
	private final JMenuItem setInitialState;
	private final JMenuItem delete;

	private final JMenu documentMenu;
	private final JMenuItem renameDocument;
	private final JMenuItem layoutRandom;
	private final JMenuItem layoutDot;

	private final JMenu moduleMenu;
	private final JMenuItem moduleBrowser;
	private final JMenuItem recentlyUsedHeader;
	private final List<JMenuItem> recentlyUsedModulesMenuItems;

	private final JMenu viewMenu;
	private final JMenuItem zoomIn;
	private final JMenuItem zoomOut;
	private final JMenuItem zoomFitWindow;
	private final JMenuItem showIdLabelsState;
	private final JMenuItem showIdLabelsPlace;
	private final JMenuItem showIdLabelsTransition;
	private final JMenuItem showGrid;
	private final JMenuItem setGridSpacing;

	private final JMenu windowMenu;
	private final JMenuItem cascadeEditorWindows;
	private final JMenuItem openWindowsHeader;
	private final List<JMenuItem> openWindowsMenuItems;

	@Inject
	public MenuViewImpl(Injector injector, RenderingOptions renderingOptions, EditingOptions editingOptions) {
		// File
		fileMenu = new JMenu("File");
		newPn = new JMenuItem(injector.getInstance(NewPetriNetAction.class));
		newTs = new JMenuItem(injector.getInstance(NewTransitionSystemAction.class));
		open = new JMenuItem(injector.getInstance(OpenAction.class));
		save = new JMenuItem(injector.getInstance(SaveAction.class));
		saveAs = new JMenuItem(injector.getInstance(SaveAsAction.class));
		saveAll = new JMenuItem(injector.getInstance(SaveAllAction.class));
		import_ = new JMenuItem(injector.getInstance(ImportAction.class));
		export = new JMenuItem(injector.getInstance(ExportAction.class));
		exit = new JMenuItem(injector.getInstance(ExitAction.class));

		// Edit
		editMenu = new JMenu("Edit");
		undo = new JMenuItem(injector.getInstance(UndoAction.class));
		redo = new JMenuItem(injector.getInstance(RedoAction.class));
		snapToGrid = new JCheckBoxMenuItem(injector.getInstance(SetSnapToGridAction.class));
		setColor = new JMenuItem(injector.getInstance(SetColorAction.class));
		setLabel = new JMenuItem(injector.getInstance(SetLabelAction.class));
		setTokens = new JMenuItem(injector.getInstance(SetTokensAction.class));
		setInitialState = new JMenuItem(injector.getInstance(SetInitialStateAction.class));
		delete = new JMenuItem(injector.getInstance(DeleteElementsAction.class));

		snapToGrid.setSelected(editingOptions.isSnapToGridEnabled());

		// Document
		documentMenu = new JMenu("Document");
		renameDocument = new JMenuItem(injector.getInstance(RenameDocumentAction.class));
		layoutRandom = new JMenuItem(injector.getInstance(RandomLayoutAction.class));
		layoutDot = new JMenuItem(injector.getInstance(DotLayoutAction.class));

		// Modules
		moduleMenu = new JMenu("Modules");
		moduleBrowser = new JMenuItem(injector.getInstance(ModuleBrowserAction.class));
		recentlyUsedHeader = new JMenuItem("Recently Used");
		recentlyUsedHeader.setEnabled(false);
		recentlyUsedModulesMenuItems = new ArrayList<>();

		// View
		viewMenu = new JMenu("View");
		zoomIn = new JMenuItem(injector.getInstance(ZoomIncreaseAction.class));
		zoomOut = new JMenuItem(injector.getInstance(ZoomDecreaseAction.class));
		zoomFitWindow = new JMenuItem(injector.getInstance(ZoomFitWindowAction.class));
		showIdLabelsState = new JCheckBoxMenuItem(injector.getInstance(SetStateIdLabelVisibleAction.class));
		showIdLabelsPlace = new JCheckBoxMenuItem(injector.getInstance(SetPlaceIdLabelVisibleAction.class));
		showIdLabelsTransition = new JCheckBoxMenuItem(injector.getInstance(SetTransitionIdLabelVisibleAction.class));
		showGrid = new JCheckBoxMenuItem(injector.getInstance(SetGridVisibleAction.class));
		setGridSpacing = new JMenuItem(injector.getInstance(SetGridSpacingAction.class));

		showIdLabelsState.setSelected(renderingOptions.isStateIdLabelVisible());
		showIdLabelsPlace.setSelected(renderingOptions.isPlaceIdLabelVisible());
		showIdLabelsTransition.setSelected(renderingOptions.isTransitionIdLabelVisible());
		showGrid.setSelected(renderingOptions.isGridVisible());

		// Windows
		windowMenu = new JMenu("Windows");
		cascadeEditorWindows = new JMenuItem(injector.getInstance(CascadeWindowsAction.class));
		openWindowsHeader = new JMenuItem("Currently Opened Windows");
		openWindowsHeader.setEnabled(false);
		openWindowsMenuItems = new ArrayList<>();

		setupFileMenu();
		setupEditMenu();
		setupDocumentMenu();
		setupModuleMenu();
		setupViewMenu();
		setupWindowMenu();
	}

	private void setupFileMenu() {
		add(fileMenu);

		fileMenu.add(newPn);
		fileMenu.add(newTs);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(saveAll);
		fileMenu.addSeparator();
		fileMenu.add(import_);
		fileMenu.add(export);
		fileMenu.addSeparator();
		fileMenu.add(exit);
	}

	private void setupEditMenu() {
		add(editMenu);

		editMenu.add(undo);
		editMenu.add(redo);
		editMenu.addSeparator();
		editMenu.add(snapToGrid);
		editMenu.addSeparator();
		editMenu.add(setColor);
		editMenu.add(setLabel);
		editMenu.add(setTokens);
		editMenu.add(setInitialState);
		editMenu.add(delete);
	}

	private void setupDocumentMenu() {
		add(documentMenu);

		documentMenu.add(renameDocument);
		documentMenu.add(layoutRandom);
		documentMenu.add(layoutDot);

	}

	private void setupModuleMenu() {
		add(moduleMenu);

		moduleMenu.add(moduleBrowser);
		moduleMenu.addSeparator();
		moduleMenu.add(recentlyUsedHeader);
	}

	private void setupViewMenu() {
		add(viewMenu);

		viewMenu.add(zoomIn);
		viewMenu.add(zoomOut);
		viewMenu.add(zoomFitWindow);
		viewMenu.addSeparator();
		viewMenu.add(showIdLabelsState);
		viewMenu.add(showIdLabelsPlace);
		viewMenu.add(showIdLabelsTransition);
		viewMenu.addSeparator();
		viewMenu.add(showGrid);
		viewMenu.add(setGridSpacing);
	}

	private void setupWindowMenu() {
		add(windowMenu);

		windowMenu.add(cascadeEditorWindows);
		windowMenu.addSeparator();
		windowMenu.add(openWindowsHeader);
	}

	@Override
	public void setRecentlyUsedModule(Application app, List<Module> recentlyUsedModules) {
		// Remove old menu entries.
		for (JMenuItem item : recentlyUsedModulesMenuItems) {
			moduleMenu.remove(item);
		}
		recentlyUsedModulesMenuItems.clear();
		// Set new menu entries.
		for (Module module : recentlyUsedModules) {
			ModuleAction action = new ModuleAction(app, module);
			JMenuItem item = new JMenuItem(action);
			recentlyUsedModulesMenuItems.add(item);
			moduleMenu.add(item);
		}
	}

	@Override
	public void setWindows(Application app, List<WindowId> windows) {
		// Remove old menu entries.
		for (JMenuItem item : openWindowsMenuItems) {
			windowMenu.remove(item);
		}
		openWindowsMenuItems.clear();
		// Set new menu entries.
		for (WindowId windowId : windows) {
			ShowWindowAction action = new ShowWindowAction(app, windowId);
			JMenuItem item = new JMenuItem(action);
			openWindowsMenuItems.add(item);
			windowMenu.add(item);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
