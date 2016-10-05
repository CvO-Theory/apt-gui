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

package uniol.aptgui.commands;

import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.document.Document;
import uniol.aptgui.document.graphical.traits.HasLabel;

public class SetLabelCommand extends SetAttributeCommand<HasLabel, String> {

	public SetLabelCommand(Document<?> document, List<HasLabel> elements, String newValue) {
		super(document, elements, newValue);
	}

	@Override
	public String getName() {
		return "Set Label";
	}

	@Override
	protected List<String> getAttributeValues(List<HasLabel> elements) {
		List<String> oldValues = new ArrayList<>();
		for (HasLabel e : elements) {
			oldValues.add(e.getLabel());
		}
		return oldValues;
	}

	@Override
	protected Class<?> getModelAttributeClass() {
		return String.class;
	}

	@Override
	protected String getModelAttributeSetterName() {
		return "setLabel";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
