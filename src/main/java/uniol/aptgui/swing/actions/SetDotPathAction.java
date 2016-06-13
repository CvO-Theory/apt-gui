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
import uniol.aptgui.editor.layout.LayoutOptions;

@SuppressWarnings("serial")
public class SetDotPathAction extends AbstractAction {

	private static final String ACTION_NAME = "Set Graphviz dot Path";

	private final Application app;
	private final LayoutOptions layoutOptions;

	@Inject
	public SetDotPathAction(Application app, LayoutOptions layoutOptions) {
		this.app = app;
		this.layoutOptions = layoutOptions;
		putValue(NAME, ACTION_NAME);
		putValue(SHORT_DESCRIPTION, ACTION_NAME);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String input = app.getMainWindow().showInputDialog(ACTION_NAME,
				"Enter the path to the Graphviz dot executable file:",
				layoutOptions.getGraphvizDotPath());
		if (input != null && !input.trim().isEmpty()) {
			layoutOptions.setGraphvizDotPath(input);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
