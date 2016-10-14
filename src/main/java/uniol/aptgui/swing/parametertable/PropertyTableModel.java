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

import javax.swing.table.AbstractTableModel;

/**
 * Table model for property tables where each row can have a different type.
 */
@SuppressWarnings("serial")
public class PropertyTableModel extends AbstractTableModel {

	private String[] columnNames;
	private Class<?>[] rowTypes;
	private Object[][] content;
	private boolean editable;

	public PropertyTableModel(String nameHeading, String valueHeading, int rows) {
		this.columnNames = new String[2];
		this.columnNames[0] = nameHeading;
		this.columnNames[1] = valueHeading;
		this.rowTypes = new Class<?>[rows];
		this.content = new Object[rows][2];
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Returns the property type at the given row.
	 *
	 * @param rowIndex
	 *                row of the property
	 * @return the property's type
	 */
	public Class<?> getPropertyTypeAt(int rowIndex) {
		return rowTypes[rowIndex];
	}

	/**
	 * Returns the property name at the given row.
	 *
	 * @param rowIndex
	 *                row of the property
	 * @return the property's name
	 */
	public String getPropertyNameAt(int rowIndex) {
		return (String) content[rowIndex][0];
	}

	/**
	 * Returns the property value at the given row.
	 *
	 * @param rowIndex
	 *                row of the property
	 * @return the property's value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getPropertyValueAt(int rowIndex) {
		return (T) content[rowIndex][1];
	}

	/**
	 * Sets the property at the given row to the given type and specifies
	 * the name. The default value for the property is set to null.
	 *
	 * @param rowIndex
	 *                row of the property to modify
	 * @param type
	 *                new property type
	 * @param name
	 *                new property name
	 */
	public void setProperty(int rowIndex, Class<?> type, String name) {
		setProperty(rowIndex, type, name, null);
	}

	/**
	 * Sets the property at the given row to the given type and specifies
	 * the name and value.
	 *
	 * @param rowIndex
	 *                row of the property to modify
	 * @param type
	 *                new property type
	 * @param name
	 *                new property name
	 * @param value
	 *                new property value
	 */
	public void setProperty(int rowIndex, Class<?> type, String name, Object value) {
		rowTypes[rowIndex] = type;
		content[rowIndex][0] = name;
		content[rowIndex][1] = value;
	}

	/**
	 * Sets the value of a property.
	 *
	 * @param rowIndex
	 *                row of the property to modify
	 * @param value
	 *                new property value
	 */
	public void setPropertyValue(int rowIndex, Object value) {
		content[rowIndex][1] = value;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		content[rowIndex][columnIndex] = aValue;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return editable && columnIndex == 1;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return content.length;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return content[rowIndex][columnIndex];
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
