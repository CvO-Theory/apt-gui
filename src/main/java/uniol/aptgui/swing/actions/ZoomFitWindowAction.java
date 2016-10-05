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

import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.swing.actions.base.DocumentAction;

@SuppressWarnings("serial")
public class ZoomFitWindowAction extends DocumentAction {

	/**
	 * Space between the window border and the bounding box after the zoom
	 * fit was performed.
	 */
	private static final int BUFFER_SIZE = 20;

	@Inject
	public ZoomFitWindowAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Zoom to Fit Window";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl 0"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		assert document != null;

		document.getViewport().zoomFit(document.getBounds(), BUFFER_SIZE);
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
