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

import uniol.aptgui.View;
import uniol.aptgui.mainwindow.WindowRefProvider;
import uniol.aptgui.swing.parametertable.PropertyTableModel;

public interface ModuleView extends View<ModulePresenter> {

	/**
	 * Sets the module description.
	 *
	 * @param description
	 *                module description
	 */
	void setDescription(String description);

	/**
	 * Sets the provider for window references of Petri nets.
	 *
	 * @param refProvider
	 *                the provider
	 */
	void setPNWindowRefProvider(WindowRefProvider refProvider);

	/**
	 * Sets the provider for window references of transition systems.
	 *
	 * @param refProvider
	 *                the provider
	 */
	void setTSWindowRefProvider(WindowRefProvider refProvider);

	/**
	 * Set the table model for displaying parameters and reading user input.
	 *
	 * @param parameterTableModel
	 */
	void setParameterTableModel(PropertyTableModel parameterTableModel);

	/**
	 * Sets the table model for displaying results.
	 *
	 * @param resultTableModel
	 */
	void setResultTableModel(PropertyTableModel resultTableModel);

	/**
	 * Shows an error message that tells the user that not all necessary
	 * parameters were supplied.
	 */
	void showErrorTooFewParameters();

	/**
	 * Switches the view to the results pane.
	 */
	void showResultsPane();

	/**
	 * Updates the visual appearance and enabled status of several elements
	 * according to the running status of the module.
	 *
	 * @param running
	 *                true, if the module is currently executing
	 */
	void setModuleRunning(boolean running);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
