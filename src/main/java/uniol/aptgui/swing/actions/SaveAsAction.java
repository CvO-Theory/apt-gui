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

import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.io.renderer.DocumentRendererFactory;
import uniol.aptgui.swing.Resource;

@SuppressWarnings("serial")
public class SaveAsAction extends SaveAction {

	@Inject
	public SaveAsAction(Application app, EventBus eventBus, DocumentRendererFactory documentRendererFactory) {
		super(app, eventBus, documentRendererFactory);
		String name = "Save As...";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconSaveFileAs());
		putValue(SHORT_DESCRIPTION, name);
		putValue(MNEMONIC_KEY, null);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt S"));
	}

	@Override
	protected boolean shouldShowSaveDialog(Document<?> document) {
		return true;
	}

	@Override
	protected boolean checkEnabled(Document<?> document, Class<?> commonBaseTestClass) {
		return true;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
