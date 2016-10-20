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

package uniol.aptgui.editor.features.base;

/**
 * Enumeration to identify features.
 */
public enum FeatureId {

	// Features
	ZOOM, SELECTION, HOVER, CONTEXT_MENU,

	// General tools
	VIEWPORT,

	// PN specific tools
	PN_CREATE_PLACE, PN_CREATE_TRANSITION, PN_CREATE_FLOW, PN_FIRE_TRANSITION,

	// TS specific tools
	TS_CREATE_STATE, TS_CREATE_ARC;

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
