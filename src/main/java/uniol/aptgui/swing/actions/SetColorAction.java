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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.SetColorCommand;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;

/**
 * Action that sets the color of graphical elements.
 */
@SuppressWarnings("serial")
public class SetColorAction extends DocumentAction {

	private static final JColorChooser COLOR_CHOOSER = new JColorChooser();

	@Inject
	public SetColorAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Set Color";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, Resource.getIconColor());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		Set<GraphicalElement> selection = document.getSelection();
		Color result = getUserColorChoice(selection);
		if (result != null) {
			Command cmd = new SetColorCommand(document, selection, result);
			app.getHistory().execute(cmd);
		}
	}

	private Color getUserColorChoice(Set<GraphicalElement> selection) {
		COLOR_CHOOSER.setColor(getInitialValue(selection));
		ColorTracker ok = new ColorTracker(COLOR_CHOOSER);
		JDialog dialog = JColorChooser.createDialog(
				app.getMainWindow().getDialogParent(),
				"Set Color", true, COLOR_CHOOSER, ok, null);
	        dialog.setVisible(true); // Blocking call
	        return ok.getColor();
	}

	private Color getInitialValue(Set<GraphicalElement> selection) {
		Color initialValue = null;
		for (GraphicalElement elem : selection) {
			if (initialValue == null) {
				initialValue = elem.getColor();
			} else if (!initialValue.equals(elem.getColor())) {
				return Color.BLACK;
			}
		}
		return initialValue;
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return !document.getSelection().isEmpty();
	}

	private static class ColorTracker implements ActionListener {
		private final JColorChooser chooser;
		private Color color;

		public ColorTracker(JColorChooser c) {
			chooser = c;
		}

		public void actionPerformed(ActionEvent e) {
			color = chooser.getColor();
		}

		public Color getColor() {
			return color;
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
