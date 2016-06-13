/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2016 Jonas Prellberg
 * Copyright (C) 2016 Uli Schlachter
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
import java.awt.event.KeyEvent;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.commands.ApplyLayoutCommand;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.layout.GraphvizLayout;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;

@SuppressWarnings("serial")
public class DotLayoutAction extends DocumentAction {

	private final GraphvizLayout graphvizLayout;

	@Inject
	public DotLayoutAction(Application app, EventBus eventBus, Application application) {
		super(app, eventBus);
		this.graphvizLayout = new GraphvizLayout(application, "dot");
		String name = "Graphviz dot Layout";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, Resource.getIconLayout());
		putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		setEnabled(false);
		eventBus.register(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		if (document != null) {
			app.getHistory().execute(new ApplyLayoutCommand(document, graphvizLayout));
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
