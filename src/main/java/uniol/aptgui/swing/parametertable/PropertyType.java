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

import uniol.apt.adt.PetriNetOrTransitionSystem;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.mainwindow.WindowRef;

public enum PropertyType {

	PETRI_NET(PetriNet.class, WindowRef.class),
	TRANSITION_SYSTEM(TransitionSystem.class, WindowRef.class),
	PETRI_NET_OR_TRANSITION_SYSTEM(PetriNetOrTransitionSystem.class, WindowRef.class),
	BOOLEAN(Boolean.class, Boolean.class),
	OTHER(Object.class, String.class);

	private final Class<?> modelType;
	private final Class<?> proxyType;

	private PropertyType(Class<?> modelType, Class<?> proxyType) {
		this.modelType = modelType;
		this.proxyType = proxyType;
	}

	public Class<?> getModelType() {
		return modelType;
	}

	public Class<?> getProxyType() {
		return proxyType;
	}

	public static PropertyType fromModelType(Class<?> modelClass) {
		for (PropertyType pt : PropertyType.values()) {
			if (pt.getModelType().equals(modelClass)) {
				return pt;
			}
		}
		return OTHER;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
