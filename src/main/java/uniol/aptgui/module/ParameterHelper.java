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
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import uniol.apt.adt.PetriNetOrTransitionSystem;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.module.impl.Parameter;
import uniol.apt.ui.ParametersTransformer;
import uniol.aptgui.mainwindow.WindowRef;

/**
 * Provides helper methods to work with module parameters.
 *
 * @author Jonas Prellberg
 *
 */
public class ParameterHelper {

	private final ParametersTransformer parametersTransformer;

	@Inject
	public ParameterHelper(ParametersTransformer parametersTransformer) {
		this.parametersTransformer = parametersTransformer;
	}

	/**
	 * Transforms a list of module parameters into a map that matches
	 * parameter name to the value's expected type.
	 *
	 * @param moduleParameters
	 *                parameters of an APT module
	 * @return map of parameter names to types
	 */
	public Map<String, Class<?>> toParameterTypeMap(List<Parameter> moduleParameters) {
		Map<String, Class<?>> viewParameters = new LinkedHashMap<>();
		for (Parameter param : moduleParameters) {
			viewParameters.put(param.getName(), param.getKlass());
		}
		return viewParameters;
	}

	/**
	 * Transforms a map containing parameter values from the view to a map
	 * containing parameter values that are of the correct model types.
	 *
	 * @param allParameters
	 *                map of all parameters to their expected model classes
	 * @param viewParameterValues
	 *                map of parameter names to the values input by the
	 *                user; these will be proxy objects in some cases such
	 *                as WindowRef objects for PetriNet or TransitionSystem
	 * @return map of parameter names to correctly typed values
	 * @throws ModuleException
	 */
	public Map<String, Object> fromViewParameterValues(Map<String, Class<?>> allParameters,
			Map<String, Object> viewParameterValues) throws ModuleException {
		Map<String, Object> modelParameters = new LinkedHashMap<>();
		for (String paramName : viewParameterValues.keySet()) {
			Object value = viewParameterValues.get(paramName);
			Class<?> targetClass = allParameters.get(paramName);
			modelParameters.put(paramName, viewToModel(value, targetClass));
		}
		return modelParameters;
	}

	private Object viewToModel(Object value, Class<?> targetClass) throws ModuleException {
		if (targetClass.isAssignableFrom(value.getClass())) {
			return value;
		} else if (value instanceof WindowRef) {
			// Unwrap window references to their model object
			// (PetriNet, TransitionSystem, PetriNetOrTransitionSystem)
			return windowRefToModel((WindowRef) value, targetClass);
		} else if (value instanceof String) {
			// Transform everything else from the string
			// representation to its model object
			return parametersTransformer.transform(value.toString(), targetClass);
		} else {
			// Should not happen because the PropertyTable should
			// always return Strings in the non-special cases
			throw new AssertionError();
		}
	}

	private Object windowRefToModel(WindowRef ref, Class<?> targetClass) {
		if (PetriNetOrTransitionSystem.class.equals(targetClass)) {
			Object model = ref.getDocument().getModel();
			if (model instanceof TransitionSystem) {
				return new PetriNetOrTransitionSystem((TransitionSystem) model);
			} else {
				return new PetriNetOrTransitionSystem((PetriNet) model);
			}
		} else {
			return ref.getDocument().getModel();
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
