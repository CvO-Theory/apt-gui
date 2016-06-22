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

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowRefProvider;

@SuppressWarnings("serial")
public class WindowRefEditor extends DefaultCellEditor {

	private final WindowRefProvider provider;

	public WindowRefEditor(WindowRefProvider provider) {
		super(new JComboBox<WindowRef>());
		this.provider = provider;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
			int column) {
		@SuppressWarnings("unchecked")
		JComboBox<WindowRef> comboBox = (JComboBox<WindowRef>) editorComponent;
		comboBox.removeAllItems();
		for (WindowRef ref : provider.getWindowReferences()) {
			comboBox.addItem(ref);
		}
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
