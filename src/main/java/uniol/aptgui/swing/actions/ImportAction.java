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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;
import uniol.aptgui.swing.filechooser.AptFileChooser;
import uniol.aptgui.swing.filechooser.AptFileChooserFactory;

@SuppressWarnings("serial")
public class ImportAction extends AbstractAction {
	private final Application app;
	private final AptFileChooserFactory aptFileChooserFactory;

	@Inject
	public ImportAction(Application app, AptFileChooserFactory aptFileChooserFactory) {
		this.app = app;
		this.aptFileChooserFactory = aptFileChooserFactory;
		String name = "Import...";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconImport());
		putValue(SHORT_DESCRIPTION, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AptFileChooser fc = aptFileChooserFactory.importChooser();
		int res = fc.showOpenDialog((Component) app.getMainWindow().getView());
		if (res == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			app.openFile(file, fc.getSelectedFileDocumentParser());
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
