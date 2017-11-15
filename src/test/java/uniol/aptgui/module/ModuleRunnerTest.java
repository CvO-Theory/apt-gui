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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uniol.apt.module.InterruptibleModule;
import uniol.apt.module.Module;
import uniol.apt.module.ModuleInput;
import uniol.apt.module.ModuleInputSpec;
import uniol.apt.module.ModuleOutput;
import uniol.apt.module.ModuleOutputSpec;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.util.interrupt.ThreadStatusInterrupter;


public class ModuleRunnerTest {

	@Test
	public void testParametersAndReturnValues() throws ModuleException {
		// Setup module
		Module module = mock(Module.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ModuleInputSpec spec = invocation.getArgument(0);
				spec.addParameter("p1", String.class, "");
				spec.addOptionalParameterWithoutDefault("p2", Integer.class, "");
				spec.addOptionalParameterWithDefault("p3", Integer.class, 1, "1", "");
				return null;
			}
		}).when(module).require(any(ModuleInputSpec.class));
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				ModuleOutputSpec spec = invocation.getArgument(0);
				spec.addReturnValue("r1", String.class);
				spec.addReturnValue("r2", String.class);
				spec.addReturnValue("r3", String.class);
				spec.addReturnValue("r4", String.class);
				return null;
			}
		}).when(module).provide(any(ModuleOutputSpec.class));
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				// (1) Test module input
				ModuleInput input = invocation.getArgument(0);
				assertThat(input.getParameter("p1", String.class), is(equalTo("v1")));
				assertThat(input.getParameter("p2", Integer.class), is(nullValue()));
				assertThat(input.getParameter("p3", Integer.class), is(equalTo(1)));

				ModuleOutput output = invocation.getArgument(1);
				output.setReturnValue("r1", String.class, "r1");
				output.setReturnValue("r2", String.class, "r2");
				output.setReturnValue("r3", String.class, "r3");
				output.setReturnValue("r4", String.class, "r4");
				return null;
			}
		}).when(module).run(any(ModuleInput.class), any(ModuleOutput.class));

		// Setup parameter inputs
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("p1", "v1");

		// Run module; this tests if (optional) parameters are handled correctly at (1)
		ModuleRunner moduleRunner = new ModuleRunner();
		Map<String, Object> result = moduleRunner.run(module, parameterValues);

		// Assert correct return value order
		assertThat(result.keySet(), contains("r1", "r2", "r3", "r4"));
	}

	@Test
	public void testModuleInterruption() throws ModuleException {
		InterruptibleModule module = mock(InterruptibleModule.class);
		ModuleRunner moduleRunner = new ModuleRunner();
		moduleRunner.run(module, new HashMap<String, Object>());
		verify(module).run(any(ModuleInput.class), any(ModuleOutput.class));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
