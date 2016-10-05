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

package uniol.aptgui.module;

import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowRefProvider;
import uniol.aptgui.mainwindow.WindowType;

public class WindowRefProviderImpl implements WindowRefProvider {

	private final Application application;
	private final WindowType filter;

	public WindowRefProviderImpl(Application application, WindowType filter) {
		this.application = application;
		this.filter = filter;
	}

	@Override
	public List<WindowRef> getWindowReferences() {
		List<WindowRef> refs = new ArrayList<>();

		for (WindowId id : application.getDocumentWindows()) {
			if (id.getType() == filter) {
				Document<?> doc = application.getDocument(id);
				refs.add(new WindowRef(id, doc));
			}
		}

		return refs;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
