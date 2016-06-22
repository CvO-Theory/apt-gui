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

package uniol.aptgui.swing.extensiontable;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uniol.apt.util.Pair;

/**
 * Table model for the extension browser. It has two columns: The first contains
 * the extension name, the second contains the extension value.
 */
@SuppressWarnings("serial")
public class ExtensionTableModel extends AbstractTableModel {

	private String[] columnNames = { "Name", "Value" };
	private List<Pair<String, Object>> extensions;

	public ExtensionTableModel() {
		this.extensions = Collections.emptyList();
	}

	public ExtensionTableModel(List<Pair<String, Object>> extensions) {
		this.extensions = extensions;
	}

	@Override
	public int getRowCount() {
		return extensions.size();
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
			return extensions.get(rowIndex).getFirst();
		}
		if (columnIndex == 1) {
			return extensions.get(rowIndex).getSecond();
		}
		return null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
