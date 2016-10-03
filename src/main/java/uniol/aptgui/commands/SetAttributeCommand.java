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

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;

/***
 * This class allows to set an attribute for a set of elements. It takes a list
 * of GraphicalElements (typed as T), fetches the associated model elements and
 * sets the attribute on the model elements.
 *
 * @param <T>
 *                trait interface that all elements have to implement
 * @param <U>
 *                attribute type
 */
public abstract class SetAttributeCommand<T, U> extends Command {

	private final Document<?> document;
	private final List<T> elements;
	private final U newValue;
	private final List<U> oldValues;

	public SetAttributeCommand(Document<?> document, List<T> elements, U newValue) {
		this.document = document;
		this.elements = new ArrayList<>(elements);
		this.newValue = newValue;
		this.oldValues = getAttributeValues(elements);
	}

	protected abstract List<U> getAttributeValues(List<T> elements);

	protected abstract String getModelAttributeSetterName();

	protected abstract Class<?> getModelAttributeClass();

	@Override
	public void execute() {
		for (T e : elements) {
			invokeAttributeSetter(e, newValue);
		}
		document.fireDocumentChanged(true);
	}

	protected void invokeAttributeSetter(T elem, U arg) {
		String setter = getModelAttributeSetterName();
		Object modelElem = document.getAssociatedModelElement((GraphicalElement) elem);
		try {
			modelElem.getClass().getMethod(setter, getModelAttributeClass()).invoke(modelElem, arg);
		} catch (Exception e) {
			String errMsg = String.format("%s#%s could not be invoked.", modelElem.getClass().getName(),
					setter);
			throw new AssertionError(errMsg, e);
		}
	}

	@Override
	public void undo() {
		for (int i = 0; i < elements.size(); i++) {
			invokeAttributeSetter(elements.get(i), oldValues.get(i));
		}
		document.fireDocumentChanged(true);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
