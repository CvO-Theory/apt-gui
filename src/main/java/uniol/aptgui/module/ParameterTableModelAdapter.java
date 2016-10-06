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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import uniol.apt.module.exception.ModuleException;
import uniol.aptgui.swing.parametertable.PropertyTableModel;

/**
 * Adapter that makes allows easy interaction with a PropertyTableModel for
 * displaying parameters.
 *
 * @author Jonas Prellberg
 *
 */
public class ParameterTableModelAdapter {

	private PropertyTableModel parametersTableModel;

	/**
	 * Sets the given module parameters so that the user can input their
	 * values in a table.
	 *
	 * @param parameters
	 *                parameters to add
	 */
	public void setParameters(Map<String, Class<?>> parameters) {
		parametersTableModel = new PropertyTableModel("Parameter", "Value", parameters.size());
		parametersTableModel.setEditable(true);

		int row = 0;
		for (Entry<String, Class<?>> param : parameters.entrySet()) {
			parametersTableModel.setProperty(row, param.getValue(), param.getKey());
			row += 1;
		}
	}

	/**
	 * Returns an map of non-null parameter values as proxy objects such as
	 * WindowRef.
	 *
	 * @return map of parameter name to its value
	 * @throws ModuleException
	 */
	public Map<String, Object> getParameterValues() throws ModuleException {
		Map<String, Object> result = new HashMap<>();
		for (int row = 0; row < parametersTableModel.getRowCount(); row++) {
			if (parametersTableModel.getPropertyValueAt(row) != null) {
				String name = parametersTableModel.getPropertyNameAt(row);
				Object value = parametersTableModel.getPropertyValueAt(row);
				result.put(name, value);
			}
		}
		return result;
	}

	/**
	 * Unsets any value that equals the given value in the parameter table.
	 * The parameter itself will stay.
	 *
	 * @param value
	 *                value to look for
	 */
	public void unsetParameterValue(Object value) {
		for (int row = 0; row < parametersTableModel.getRowCount(); row++) {
			if (value.equals(parametersTableModel.getPropertyValueAt(row))) {
				parametersTableModel.setPropertyValue(row, null);
			}
		}
	}

	public PropertyTableModel getModel() {
		return parametersTableModel;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
