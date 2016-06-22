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

package uniol.aptgui.swing.parametertable;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import uniol.aptgui.mainwindow.WindowRefProvider;

/**
 * Table that allows to use window references in editors and also allows for
 * different types in each row.
 */
@SuppressWarnings("serial")
public class PropertyTable extends JTable {

	private PropertyTableModel dataModel;

	private WindowRefEditor pnEditor;
	private WindowRefEditor tsEditor;

	private CompoundWindowRefProvider compoundProvider;
	private WindowRefProvider pnProvider;
	private WindowRefProvider tsProvider;
	private WindowRefEditor pnOrTsEditor;

	public PropertyTable() {
		compoundProvider = new CompoundWindowRefProvider();
		pnOrTsEditor = new WindowRefEditor(compoundProvider);
	}

	public void setPetriNetWindowRefProvider(WindowRefProvider refProvider) {
		if (pnProvider != null) {
			compoundProvider.removeProvider(pnProvider);
		}
		pnProvider = refProvider;
		pnEditor = new WindowRefEditor(refProvider);
		compoundProvider.addProvider(refProvider);
	}

	public void setTransitionSystemWindowRefProvider(WindowRefProvider refProvider) {
		if (tsProvider != null) {
			compoundProvider.removeProvider(tsProvider);
		}
		tsProvider = refProvider;
		tsEditor = new WindowRefEditor(refProvider);
		compoundProvider.addProvider(refProvider);
	}

	public void setModel(PropertyTableModel dataModel) {
		this.dataModel = dataModel;
		super.setModel(dataModel);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 1 && dataModel != null) {
			int modelRow = convertRowIndexToModel(row);
			PropertyType type = dataModel.getPropertyTypeAt(modelRow);
			switch (type) {
			case PETRI_NET:
				return pnEditor;
			case TRANSITION_SYSTEM:
				return tsEditor;
			case PETRI_NET_OR_TRANSITION_SYSTEM:
				return pnOrTsEditor;
			default:
				return getDefaultEditor(type.getProxyType());
			}
		}

		return super.getCellEditor(row, column);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
