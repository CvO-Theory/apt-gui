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

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowRef;

/**
 * Provides helper methods to work with module results.
 */
public class ResultHelper {

	private final Application app;

	@Inject
	public ResultHelper(Application app) {
		this.app = app;
	}

	/**
	 * Transforms a map containing module return values to a map where the
	 * values are replaced with the proper view proxy objects.
	 *
	 * @param moduleReturnValues
	 *                map of return value names to their values as model
	 *                objects
	 * @return map of return value names to their values as view proxy
	 *         objects
	 */
	public Map<String, Object> toViewReturnValues(Map<String, Object> moduleReturnValues) {
		Map<String, Object> viewReturnValues = new LinkedHashMap<>();
		for (String paramName : moduleReturnValues.keySet()) {
			Object value = moduleReturnValues.get(paramName);
			if (value != null) {
				viewReturnValues.put(paramName, modelToView(value));
			}
		}
		return viewReturnValues;
	}

	private Object modelToView(Object value) {
		if (value instanceof PetriNet) {
			Document<?> doc = new PnDocument((PetriNet) value);
			doc.setHasUnsavedChanges(true);
			WindowRef ref = openDocument(doc);
			return ref;
		} else if (value instanceof TransitionSystem) {
			Document<?> doc = new TsDocument((TransitionSystem) value);
			doc.setHasUnsavedChanges(true);
			WindowRef ref = openDocument(doc);
			return ref;
		} else {
			return value.toString();
		}
	}

	private WindowRef openDocument(Document<?> document) {
		WindowId id = app.openDocument(document);
		WindowRef ref = new WindowRef(id, document);
		return ref;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
