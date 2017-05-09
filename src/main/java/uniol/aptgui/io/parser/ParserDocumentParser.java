/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2017 Uli Schlachter
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

package uniol.aptgui.io.parser;

import java.io.File;
import java.io.IOException;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.io.parser.Parser;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.io.FileType;

/**
 * Parses documents via a given parser
 */
public class ParserDocumentParser<G> implements DocumentParser {

	private final Class<G> klass;
	private final Parser<G> parser;

	public ParserDocumentParser(Class<G> klass, Parser<G> parser) {
		this.klass = klass;
		this.parser = parser;

		assert klass.equals(PetriNet.class) || klass.equals(TransitionSystem.class);
	}

	@Override
	public Document<?> parse(File file) throws ParseException, IOException {
		G object = parser.parseFile(file);
		Document<?> result = null;
		if (PetriNet.class.equals(klass)) {
			result = new PnDocument((PetriNet) object);
			result.setFileType(FileType.PETRI_NET);
		}
		if (TransitionSystem.class.equals(klass)) {
			result = new TsDocument((TransitionSystem) object);
			result.setFileType(FileType.TRANSITION_SYSTEM);
		}
		result.setFile(file);
		return result;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
