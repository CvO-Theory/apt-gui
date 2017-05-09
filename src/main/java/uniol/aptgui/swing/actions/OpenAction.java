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
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.filechooser.AptFileChooser;
import uniol.aptgui.swing.filechooser.AptFileChooserFactory;

@SuppressWarnings("serial")
public class OpenAction extends AbstractAction {
	private final Application app;
	private final AptFileChooserFactory aptFileChooserFactory;

	@Inject
	public OpenAction(Application app, AptFileChooserFactory aptFileChooserFactory) {
		this.app = app;
		this.aptFileChooserFactory = aptFileChooserFactory;
		String name = "Open File...";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconOpenFile());
		putValue(SHORT_DESCRIPTION, name);
		putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AptFileChooser fc = aptFileChooserFactory.openChooser();
		int res = fc.showOpenDialog((Component) app.getMainWindow().getView());
		if (res == JFileChooser.APPROVE_OPTION) {
			app.openFile(fc.getSelectedFile());
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
