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
import java.util.Set;

import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.RemovePnElementsCommand;
import uniol.aptgui.commands.RemoveTsElementsCommand;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;

/**
 * Action that deletes all currently selected elements in the currently active
 * document.
 */
@SuppressWarnings("serial")
public class DeleteElementsAction extends DocumentAction {

	@Inject
	public DeleteElementsAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Delete";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconDelete());
		putValue(SHORT_DESCRIPTION, name);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		if (document != null) {
			Set<GraphicalElement> selection = document.getSelection();
			Command cmd = null;
			if (document instanceof PnDocument) {
				cmd = new RemovePnElementsCommand((PnDocument) document, selection);
			} else if (document instanceof TsDocument) {
				cmd = new RemoveTsElementsCommand((TsDocument) document, selection);
			}
			app.getHistory().execute(cmd);
		}
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return !document.getSelection().isEmpty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
