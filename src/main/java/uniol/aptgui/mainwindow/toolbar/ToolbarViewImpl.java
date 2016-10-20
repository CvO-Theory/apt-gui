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

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.editor.features.base.FeatureId;
import uniol.aptgui.swing.JToolBarView;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.OpenAction;
import uniol.aptgui.swing.actions.RedoAction;
import uniol.aptgui.swing.actions.SaveAction;
import uniol.aptgui.swing.actions.SaveAllAction;
import uniol.aptgui.swing.actions.UndoAction;
import uniol.aptgui.swing.actions.ZoomDecreaseAction;
import uniol.aptgui.swing.actions.ZoomIncreaseAction;
import uniol.aptgui.swing.actions.tools.PnCreateFlowToolAction;
import uniol.aptgui.swing.actions.tools.PnCreatePlaceToolAction;
import uniol.aptgui.swing.actions.tools.PnCreateTransitionToolAction;
import uniol.aptgui.swing.actions.tools.PnFireTransitionToolAction;
import uniol.aptgui.swing.actions.tools.PnModifyTokensToolAction;
import uniol.aptgui.swing.actions.tools.SelectionToolAction;
import uniol.aptgui.swing.actions.tools.TsCreateArcToolAction;
import uniol.aptgui.swing.actions.tools.TsCreateStateToolAction;
import uniol.aptgui.swing.actions.tools.ViewportToolAction;

@SuppressWarnings("serial")
public class ToolbarViewImpl extends JToolBarView<ToolbarPresenter> implements ToolbarView {

	// GENERAL BUTTONS
	private final JButton newPetriNet;
	private final JButton newTransitionSystem;
	private final JButton open;
	private final JButton save;
	private final JButton saveAll;
	private final JButton undo;
	private final JButton redo;
	private final JButton zoomIn;
	private final JButton zoomOut;

	// PN AND TS GROUPS
	private final ButtonGroup pnToolGroup;
	private final ButtonGroup tsToolGroup;

	// PN BUTTONS
	private final JToggleButton pnViewportTool;
	private final JToggleButton pnSelectionTool;
	private final JToggleButton pnCreatePlaceTool;
	private final JToggleButton pnCreateTransitionTool;
	private final JToggleButton pnCreateFlowTool;
	private final JToggleButton pnModifyTokensTool;
	private final JToggleButton pnFireTransitionTool;

	// TS BUTTONS
	private final JToggleButton tsViewportTool;
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
		undo = new JButton(injector.getInstance(UndoAction.class));
		redo = new JButton(injector.getInstance(RedoAction.class));
		zoomIn = new JButton(injector.getInstance(ZoomIncreaseAction.class));
		zoomOut = new JButton(injector.getInstance(ZoomDecreaseAction.class));

		newPetriNet.setHideActionText(true);
		newTransitionSystem.setHideActionText(true);
		open.setHideActionText(true);
		save.setHideActionText(true);
		saveAll.setHideActionText(true);
		undo.setHideActionText(true);
		redo.setHideActionText(true);
		zoomIn.setHideActionText(true);
		zoomOut.setHideActionText(true);

		add(newPetriNet);
		add(newTransitionSystem);
		add(open);
		add(save);
		add(saveAll);

		addSeparator();

		add(undo);
		add(redo);

		addSeparator();

		add(zoomIn);
		add(zoomOut);

		addSeparator();

		// PN AND TS GROUPS
		pnToolGroup = new ButtonGroup();
		tsToolGroup = new ButtonGroup();

		// PN BUTTONS
		pnViewportTool = new JToggleButton(injector.getInstance(ViewportToolAction.class));
		pnSelectionTool = new JToggleButton(injector.getInstance(SelectionToolAction.class));
		pnCreatePlaceTool = new JToggleButton(injector.getInstance(PnCreatePlaceToolAction.class));
		pnCreateTransitionTool = new JToggleButton(injector.getInstance(PnCreateTransitionToolAction.class));
		pnCreateFlowTool = new JToggleButton(injector.getInstance(PnCreateFlowToolAction.class));
		pnModifyTokensTool = new JToggleButton(injector.getInstance(PnModifyTokensToolAction.class));
		pnFireTransitionTool = new JToggleButton(injector.getInstance(PnFireTransitionToolAction.class));
		setToolButton(pnViewportTool, pnToolGroup);
		setToolButton(pnSelectionTool, pnToolGroup);
		setToolButton(pnCreatePlaceTool, pnToolGroup);
		setToolButton(pnCreateTransitionTool, pnToolGroup);
		setToolButton(pnCreateFlowTool, pnToolGroup);
		setToolButton(pnModifyTokensTool, pnToolGroup);
		setToolButton(pnFireTransitionTool, pnToolGroup);

		// TS BUTTONS
		tsViewportTool = new JToggleButton(injector.getInstance(ViewportToolAction.class));
		tsSelectionTool = new JToggleButton(injector.getInstance(SelectionToolAction.class));
		tsCreateStateTool = new JToggleButton(injector.getInstance(TsCreateStateToolAction.class));
		tsCreateArcTool = new JToggleButton(injector.getInstance(TsCreateArcToolAction.class));
		setToolButton(tsViewportTool, tsToolGroup);
		setToolButton(tsSelectionTool, tsToolGroup);
		setToolButton(tsCreateStateTool, tsToolGroup);
		setToolButton(tsCreateArcTool, tsToolGroup);
	}

	private void setToolButton(AbstractButton button, ButtonGroup group) {
		button.setHideActionText(true);
		group.add(button);
		add(button);
	}

	@Override
	public void setPetriNetToolsVisible(boolean visible) {
		pnViewportTool.setVisible(visible);
		pnSelectionTool.setVisible(visible);
		pnCreatePlaceTool.setVisible(visible);
		pnCreateTransitionTool.setVisible(visible);
		pnCreateFlowTool.setVisible(visible);
		pnModifyTokensTool.setVisible(visible);
		pnFireTransitionTool.setVisible(visible);
	}

	@Override
	public void setTransitionSystemToolsVisible(boolean visible) {
		tsViewportTool.setVisible(visible);
		tsSelectionTool.setVisible(visible);
		tsCreateStateTool.setVisible(visible);
		tsCreateArcTool.setVisible(visible);
	}

	@Override
	public void setActiveTool(FeatureId tool, boolean isPnTool) {
		switch (tool) {
		case VIEWPORT:
			if (isPnTool) {
				pnViewportTool.doClick();
			} else {
				pnSelectionTool.doClick();
			}
			break;
		case SELECTION:
			if (isPnTool) {
				pnSelectionTool.doClick();
			} else {
				tsSelectionTool.doClick();
			}
			break;
		case PN_CREATE_FLOW:
			pnCreateFlowTool.doClick();
			break;
		case PN_CREATE_PLACE:
			pnCreatePlaceTool.doClick();
			break;
		case PN_CREATE_TRANSITION:
			pnCreateTransitionTool.doClick();
			break;
		case PN_MODIFY_TOKENS:
			pnModifyTokensTool.doClick();
			break;
		case PN_FIRE_TRANSITION:
			pnFireTransitionTool.doClick();
			break;
		case TS_CREATE_ARC:
			tsCreateArcTool.doClick();
			break;
		case TS_CREATE_STATE:
			tsCreateStateTool.doClick();
			break;
		default:
			break;
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
