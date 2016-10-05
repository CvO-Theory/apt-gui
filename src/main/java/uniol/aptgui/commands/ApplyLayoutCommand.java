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

import uniol.aptgui.document.Document;
import uniol.aptgui.editor.layout.Layout;

public class ApplyLayoutCommand extends Command {

	private final Document<?> document;
	private final Layout layout;

	public ApplyLayoutCommand(Document<?> document, Layout layout) {
		this.document = document;
		this.layout = layout;
	}

	@Override
	public void execute() {
		document.applyLayout(layout);
		document.fireDocumentChanged(true);
	}

	@Override
	public void undo() {
		// Empty
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public String getName() {
		return "Apply " + layout.getName() + " Layout";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
