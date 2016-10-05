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

import java.util.LinkedHashMap;
import java.util.Map;

import uniol.apt.module.InterruptibleModule;
import uniol.apt.module.Module;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.module.exception.ModuleInvocationException;
import uniol.apt.module.impl.ModuleInputImpl;
import uniol.apt.module.impl.ModuleOutputImpl;
import uniol.apt.module.impl.ModuleUtils;
import uniol.apt.module.impl.OptionalParameter;
import uniol.apt.module.impl.Parameter;
import uniol.apt.module.impl.ReturnValue;
import uniol.apt.util.interrupt.ThreadStatusInterrupter;

/**
 * Executes modules and makes them listen to thread interrupts if possible.
 *
 * @author Jonas Prellberg
 *
 */
public class ModuleRunner {

	/**
	 * Run the given module synchronously. Accepts a parameter mapping from
	 * name to value (of the correct type!) and returns a mapping from
	 * return value names to their actual value.
	 *
	 * @param module
	 *                module to execute
	 * @param parameterValues
	 *                map of parameter names to their values
	 * @return map of return value names to their values
	 * @throws ModuleException
	 *                 thrown when an exception occurs during module
	 *                 execution
	 */
	public Map<String, Object> run(Module module, Map<String, Object> parameterValues) throws ModuleException {
		final ModuleInputImpl input = ModuleUtils.getModuleInput(module);
		final ModuleOutputImpl output = ModuleUtils.getModuleOutput(module);

		// Prepare required parameters
		for (Parameter param : ModuleUtils.getParameters(module)) {
			if (!parameterValues.containsKey(param.getName())) {
				String msg = "Missing required module argument " + param.getName();
				throw new ModuleInvocationException(msg);
			}
			Object paramValue = parameterValues.get(param.getName());
			input.setParameter(param.getName(), paramValue);
		}

		// Prepare optional parameters
		for (OptionalParameter<?> param : ModuleUtils.getOptionalParameters(module)) {
			if (parameterValues.containsKey(param.getName())) {
				// Optional parameter was supplied.
				Object paramValue = parameterValues.get(param.getName());
				input.setParameter(param.getName(), paramValue);
			} else {
				// Use default value if no value was supplied.
				input.setParameter(param.getName(), param.getDefaultValue());
			}
		}

		// Run module
		if (module instanceof InterruptibleModule) {
			InterruptibleModule interruptibleModule = (InterruptibleModule) module;
			interruptibleModule.run(input, output, new ThreadStatusInterrupter());
		} else {
			module.run(input, output);
		}

		// Collect return values
		Map<String, Object> results = new LinkedHashMap<>();
		for (ReturnValue returnValue : ModuleUtils.getReturnValues(module)) {
			Object value = output.getValue(returnValue.getName());
			if (value != null) {
				results.put(returnValue.getName(), value);
			}
		}

		return results;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
