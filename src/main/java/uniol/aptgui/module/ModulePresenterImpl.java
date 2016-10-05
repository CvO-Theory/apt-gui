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
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.module.Module;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.module.impl.ModuleUtils;
import uniol.apt.module.impl.Parameter;
import uniol.apt.ui.ParametersTransformer;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.events.ModuleExecutedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowType;

public class ModulePresenterImpl extends AbstractPresenter<ModulePresenter, ModuleView> implements ModulePresenter {

	private final Application application;
	private final ParametersTransformer parametersTransformer;
	private final ModuleRunner moduleRunner;

	private WindowId windowId;
	private Module module;

	private Map<String, Class<?>> allParameters;
	private Map<String, Class<?>> requiredParameters;

	/**
	 * Future giving access to the module execution happening in another
	 * thread.
	 */
	private Future<Map<String, Object>> moduleFuture;

	@Inject
	public ModulePresenterImpl(ModuleView view, Application application,
			ParametersTransformer parametersTransformer, ModuleRunner moduleRunner) {
		super(view);
		this.application = application;
		this.parametersTransformer = parametersTransformer;
		this.moduleRunner = moduleRunner;
		application.getEventBus().register(this);
	}

	@Override
	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
		allParameters = toParameterTypeMap(ModuleUtils.getAllParameters(module));
		requiredParameters = toParameterTypeMap(ModuleUtils.getParameters(module));

		view.setParameters(allParameters);
		view.setDescription(module.getLongDescription());
		view.setPNWindowRefProvider(new WindowRefProviderImpl(application, WindowType.PETRI_NET));
		view.setTSWindowRefProvider(new WindowRefProviderImpl(application, WindowType.TRANSITION_SYSTEM));
	}

	/**
	 * Transforms a list of module parameters into a map that matches
	 * parameter name to the value's expected type.
	 *
	 * @param moduleParameters
	 *                parameters of an APT module
	 * @return map of parameter names to types
	 */
	private Map<String, Class<?>> toParameterTypeMap(List<Parameter> moduleParameters) {
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
	 * @param viewParameterValues
	 *                map of parameter names to the values input by the
	 *                user; these will be proxy objects in some cases such
	 *                as WindowRef objects for PetriNet or TransitionSystem
	 * @return map of parameter names to correctly typed values
	 * @throws ModuleException
	 */
	private Map<String, Object> fromViewParameterValues(Map<String, Object> viewParameterValues)
			throws ModuleException {
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
			// Unwrap window references to their
			// model object (PetriNet or TransitionSystem)
			WindowRef ref = (WindowRef) value;
			return ref.getDocument().getModel();
		} else if (value instanceof String) {
			// Transform everything else from the
			// string representation to its model object
			return parametersTransformer.transform(value.toString(), targetClass);
		} else {
			// Should not happen because the PropertyTable should
			// always return Strings in the non-special cases
			throw new AssertionError();
		}
	}

	/**
	 * Transforms a map containing module return values to a map where the
	 * values are replaced with the proper view proxy objects.
	 *
	 * @param moduleReturnValues
	 * @return
	 */
	private Map<String, Object> toViewReturnValues(Map<String, Object> moduleReturnValues) {
		Map<String, Object> viewReturnValues = new LinkedHashMap<>();
		for (String paramName : moduleReturnValues.keySet()) {
			Object value = moduleReturnValues.get(paramName);
			if (value != null) {
				viewReturnValues.put(paramName, modelToView(value));
			}
		}
		return viewReturnValues;
	}

	private Object modelToView(Object value) {
		if (value instanceof PetriNet) {
			Document<?> doc = new PnDocument((PetriNet) value);
			doc.setHasUnsavedChanges(true);
			WindowRef ref = openDocument(doc);
			return ref;
		} else if (value instanceof TransitionSystem) {
			Document<?> doc = new TsDocument((TransitionSystem) value);
			doc.setHasUnsavedChanges(true);
			WindowRef ref = openDocument(doc);
			return ref;
		} else {
			return value.toString();
		}
	}

	@Override
	public void onRunButtonClicked() {
		try {
			Map<String, Object> paramValues = fromViewParameterValues(view.getParameterValues());
			// Make sure all required parameters are filled in
			for (String name : requiredParameters.keySet()) {
				if (paramValues.get(name) == null) {
					view.showErrorTooFewParameters();
					return;
				}
			}

			// Module is executed on another thread.
			moduleFuture = invokeModule(paramValues);
			view.setModuleRunning(true);
			// Invoke new thread that blocks until results are
			// available.
			displayResultsWhenFinished();
		} catch (Exception e) {
			application.getMainWindow().showException("Module exception", e);
		}
	}

	private Future<Map<String, Object>> invokeModule(final Map<String, Object> paramValues) {
		return application.getExecutorService().submit(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				Map<String, Object> returnValues = moduleRunner.run(module, paramValues);
				return returnValues;
			}
		});
	}

	private void displayResultsWhenFinished() {
		application.getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				waitForModuleExecution();
			}
		});
	}

	/**
	 * Blocks until the module future result is available and then shows the
	 * module results in the view.
	 */
	private void waitForModuleExecution() {
		try {
			final Map<String, Object> filledReturnValues = moduleFuture.get();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					showModuleResults(filledReturnValues);
					application.getEventBus().post(
							new ModuleExecutedEvent(ModulePresenterImpl.this, module));
				}
			});
		} catch (InterruptedException | CancellationException ex) {
			// Ignore these two types of exceptions.
		} catch (final Exception ex) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					application.getMainWindow().showException("Module execution exception", ex);
				}
			});
		} finally {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					view.setModuleRunning(false);
				}
			});
		}
	}

	private void showModuleResults(Map<String, Object> filledReturnValues) {
		view.setReturnValues(toViewReturnValues(filledReturnValues));
		view.showResultsPane();
	}

	private WindowRef openDocument(Document<?> document) {
		WindowId id = application.openDocument(document);
		WindowRef ref = new WindowRef(id, document);
		return ref;
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		// Unset window chosen as a parameter if it was closed
		WindowRef ref = new WindowRef(e.getWindowId(), application.getDocument(e.getWindowId()));
		view.unsetParameterValue(ref);

		// Cancel module execution when parent window closes
		if (e.getWindowId() == windowId && moduleFuture != null) {
			moduleFuture.cancel(true);
		}
	}

	@Override
	public void focusWindow(WindowRef ref) {
		// If the referenced window is still open, focus it.
		if (application.getDocument(ref.getWindowId()) != null) {
			application.focusWindow(ref.getWindowId());
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
