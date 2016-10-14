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

import java.util.Map;
import java.util.Map.Entry;

import uniol.aptgui.swing.parametertable.PropertyTableModel;

/**
 * Adapter that allows easy interaction with a PropertyTableModel for displaying
 * return values.
 */
public class ResultTableModelAdapter {

	private PropertyTableModel resultsTableModel;

	/**
	 * Sets the given return values. The values should be the correct proxy
	 * types, i.e. WindowRefs for PetriNets or TransitionSystems.
	 *
	 * @param returnValues
	 *                map of return value names to their values
	 */
	public void setReturnValues(Map<String, Object> returnValues) {
		resultsTableModel = new PropertyTableModel("Result", "Value", returnValues.size());
		int row = 0;
		for (Entry<String, Object> rv : returnValues.entrySet()) {
			resultsTableModel.setProperty(row, Object.class, rv.getKey(), rv.getValue());
			row += 1;
		}
	}

	/**
	 * Returns the underlying table model that is being created when
	 * {@link #setReturnValues(Map)} is called.
	 *
	 * @return the table model or null if {@link #setReturnValues(Map)} has
	 *         never been called
	 */
	public PropertyTableModel getModel() {
		return resultsTableModel;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
