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

package uniol.aptgui.swing.moduletable;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import uniol.apt.module.Module;

/**
 * Table model for the module browser.
 */
@SuppressWarnings("serial")
public class ModuleTableModel extends AbstractTableModel {

	private String[] columnNames = { "Module", "Category", "Description" };
	private List<Module> modules;

	public ModuleTableModel(List<Module> modules) {
		this.modules = modules;
	}

	@Override
	public int getRowCount() {
		return modules.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return modules.get(rowIndex).getName();
		}
		if (columnIndex == 1) {
			return modules.get(rowIndex).getCategories()[0];
		}
		if (columnIndex == 2) {
			return modules.get(rowIndex).getShortDescription();
		}
		return null;
	}

	public Module getModuleAt(int rowIndex) {
		return modules.get(rowIndex);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
