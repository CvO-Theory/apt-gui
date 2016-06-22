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

import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowRefProvider;

/**
 * Allows to group multiple WindowRefProviders into a single one.
 */
public class CompoundWindowRefProvider implements WindowRefProvider {

	private final List<WindowRefProvider> providers = new ArrayList<>();

	public void addProvider(WindowRefProvider provider) {
		providers.add(provider);
	}

	public void removeProvider(WindowRefProvider provider) {
		providers.remove(provider);
	}

	@Override
	public List<WindowRef> getWindowReferences() {
		List<WindowRef> refs = new ArrayList<>();
		for (WindowRefProvider provider : providers) {
			refs.addAll(provider.getWindowReferences());
		}
		return refs;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
