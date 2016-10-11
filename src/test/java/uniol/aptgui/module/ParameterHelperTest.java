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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uniol.apt.adt.PetriNetOrTransitionSystem;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.ui.ParametersTransformer;
import uniol.aptgui.document.Document;
import uniol.aptgui.mainwindow.WindowRef;

public class ParameterHelperTest {

	/**
	 * Tests that a parameter of type PetriNetOrTransitionSystem is properly
	 * transformed from the view proxy value.
	 */
	@Test
	public void testPnOrTsParameter() throws ModuleException {
		WindowRef windowRef = mock(WindowRef.class);
		when(windowRef.getDocument()).thenAnswer(new Answer<Document<?>>() {
			@Override
			public Document<?> answer(InvocationOnMock invocation) throws Throwable {
				@SuppressWarnings("unchecked")
				Document<PetriNet> doc = mock(Document.class);
				when(doc.getModel()).thenReturn(new PetriNet());
				return doc;
			}
		});

		Map<String, Class<?>> allParameters = new HashMap<>();
		allParameters.put("pnOrTs", PetriNetOrTransitionSystem.class);
		Map<String, Object> viewParameterValues = new HashMap<>();
		viewParameterValues.put("pnOrTs", windowRef);

		ParameterHelper helper = new ParameterHelper(mock(ParametersTransformer.class));
		Map<String, Object> modelValues = helper.fromViewParameterValues(allParameters, viewParameterValues);
		assertThat(modelValues, hasEntry(equalTo("pnOrTs"), instanceOf(PetriNetOrTransitionSystem.class)));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
