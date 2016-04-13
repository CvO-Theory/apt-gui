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

package uniol.aptgui.mainwindow.toolbar;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.editor.tools.ToolId;
import uniol.aptgui.swing.JToolBarView;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.OpenAction;
import uniol.aptgui.swing.actions.PnCreateFlowToolAction;
import uniol.aptgui.swing.actions.PnCreatePlaceToolAction;
import uniol.aptgui.swing.actions.PnCreateTransitionToolAction;
import uniol.aptgui.swing.actions.PnSelectionToolAction;
import uniol.aptgui.swing.actions.SaveAction;
import uniol.aptgui.swing.actions.SaveAllAction;
import uniol.aptgui.swing.actions.TsCreateArcToolAction;
import uniol.aptgui.swing.actions.TsCreateStateToolAction;
import uniol.aptgui.swing.actions.TsSelectionToolAction;

@SuppressWarnings("serial")
public class ToolbarViewImpl extends JToolBarView<ToolbarPresenter> implements ToolbarView {

	// GENERAL BUTTONS
	private final JButton newPetriNet;
	private final JButton newTransitionSystem;
	private final JButton open;
	private final JButton save;
	private final JButton saveAll;

	// PN BUTTONS
	private final ButtonGroup pnToolGroup;
	private final JToggleButton pnSelectionTool;
	private final JToggleButton pnCreatePlaceTool;
	private final JToggleButton pnCreateTransitionTool;
	private final JToggleButton pnCreateFlowTool;

	// TS BUTTONS
	private final ButtonGroup tsToolGroup;
	private final JToggleButton tsSelectionTool;
	private final JToggleButton tsCreateStateTool;
	private final JToggleButton tsCreateArcTool;

	@Inject
	public ToolbarViewImpl(Injector injector) {
		setFloatable(false);

		// GENERAL BUTTONS
		newPetriNet = new JButton(injector.getInstance(NewPetriNetAction.class));
		newTransitionSystem = new JButton(injector.getInstance(NewTransitionSystemAction.class));
		open = new JButton(injector.getInstance(OpenAction.class));
		save = new JButton(injector.getInstance(SaveAction.class));
		saveAll = new JButton(injector.getInstance(SaveAllAction.class));

		open.setHideActionText(true);
		save.setHideActionText(true);
		saveAll.setHideActionText(true);

		add(newPetriNet);
		add(newTransitionSystem);
		add(open);
		add(save);
		add(saveAll);

		addSeparator();

		// PN BUTTONS
		pnToolGroup = new ButtonGroup();
		pnSelectionTool = new JToggleButton(injector.getInstance(PnSelectionToolAction.class));
		pnCreatePlaceTool = new JToggleButton(injector.getInstance(PnCreatePlaceToolAction.class));
		pnCreateTransitionTool = new JToggleButton(injector.getInstance(PnCreateTransitionToolAction.class));
		pnCreateFlowTool = new JToggleButton(injector.getInstance(PnCreateFlowToolAction.class));

		pnSelectionTool.setHideActionText(true);
		pnCreatePlaceTool.setHideActionText(true);
		pnCreateTransitionTool.setHideActionText(true);
		pnCreateFlowTool.setHideActionText(true);

		addToToolbarAndGroup(pnSelectionTool, pnToolGroup);
		addToToolbarAndGroup(pnCreatePlaceTool, pnToolGroup);
		addToToolbarAndGroup(pnCreateTransitionTool, pnToolGroup);
		addToToolbarAndGroup(pnCreateFlowTool, pnToolGroup);

		// TS BUTTONS
		tsToolGroup = new ButtonGroup();
		tsSelectionTool = new JToggleButton(injector.getInstance(TsSelectionToolAction.class));
		tsCreateStateTool = new JToggleButton(injector.getInstance(TsCreateStateToolAction.class));
		tsCreateArcTool = new JToggleButton(injector.getInstance(TsCreateArcToolAction.class));

		tsSelectionTool.setHideActionText(true);
		tsCreateStateTool.setHideActionText(true);
		tsCreateArcTool.setHideActionText(true);

		addToToolbarAndGroup(tsSelectionTool, tsToolGroup);
		addToToolbarAndGroup(tsCreateStateTool, tsToolGroup);
		addToToolbarAndGroup(tsCreateArcTool, tsToolGroup);
	}

	private void addToToolbarAndGroup(AbstractButton button, ButtonGroup group) {
		add(button);
		group.add(button);
	}

	@Override
	public void setPetriNetToolsVisible(boolean visible) {
		pnSelectionTool.setVisible(visible);
		pnCreatePlaceTool.setVisible(visible);
		pnCreateTransitionTool.setVisible(visible);
		pnCreateFlowTool.setVisible(visible);
	}

	@Override
	public void setTransitionSystemToolsVisible(boolean visible) {
		tsSelectionTool.setVisible(visible);
		tsCreateStateTool.setVisible(visible);
		tsCreateArcTool.setVisible(visible);
	}

	@Override
	public void setActiveTool(ToolId tool) {
		switch (tool) {
		case PN_CREATE_FLOW:
			pnCreateFlowTool.doClick();
			break;
		case PN_CREATE_PLACE:
			pnCreatePlaceTool.doClick();
			break;
		case PN_CREATE_TRANSITION:
			pnCreateTransitionTool.doClick();
			break;
		case PN_SELECTION:
			pnSelectionTool.doClick();
			break;
		case TS_CREATE_ARC:
			tsCreateArcTool.doClick();
			break;
		case TS_CREATE_STATE:
			tsCreateStateTool.doClick();
			break;
		case TS_SELECTION:
			tsSelectionTool.doClick();
			break;
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120