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

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.module.Module;
import uniol.apt.module.impl.ModuleUtils;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.events.ModuleExecutedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowType;

public class ModulePresenterImpl extends AbstractPresenter<ModulePresenter, ModuleView> implements ModulePresenter {

	private final Application application;
	private final ModuleRunner moduleRunner;
	private final ParameterHelper parameterHelper;
	private final ResultHelper resultHelper;

	private WindowId windowId;
	private Module module;

	private Map<String, Class<?>> allParameters;
	private Map<String, Class<?>> requiredParameters;

	private ParameterTableModelAdapter paramAdapter;
	private ResultTableModelAdapter resultAdapter;

	/**
	 * Future giving access to the module execution happening in another
	 * thread.
	 */
	private Future<Map<String, Object>> moduleFuture;

	@Inject
	public ModulePresenterImpl(ModuleView view, Application application, ModuleRunner moduleRunner,
			ParameterHelper parameterHelper, ResultHelper resultHelper) {
		super(view);
		this.application = application;
		this.moduleRunner = moduleRunner;
		this.parameterHelper = parameterHelper;
		this.resultHelper = resultHelper;
		application.getEventBus().register(this);
	}

	@Override
	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
		allParameters = parameterHelper.toParameterTypeMap(ModuleUtils.getAllParameters(module));
		requiredParameters = parameterHelper.toParameterTypeMap(ModuleUtils.getParameters(module));
		paramAdapter = new ParameterTableModelAdapter();
		paramAdapter.setParameters(allParameters);

		view.setParameterTableModel(paramAdapter.getModel());
		view.setDescription(module.getLongDescription());
		view.setPNWindowRefProvider(new WindowRefProviderImpl(application, WindowType.PETRI_NET));
		view.setTSWindowRefProvider(new WindowRefProviderImpl(application, WindowType.TRANSITION_SYSTEM));
	}

	@Override
	public void onRunButtonClicked() {
		try {
			Map<String, Object> paramValues = parameterHelper.fromViewParameterValues(allParameters,
					paramAdapter.getParameterValues());
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

	private void showModuleResults(Map<String, Object> returnValues) {
		Map<String, Object> viewReturnValues = resultHelper.toViewReturnValues(returnValues);
		resultAdapter = new ResultTableModelAdapter();
		resultAdapter.setReturnValues(viewReturnValues);
		view.setResultTableModel(resultAdapter.getModel());
		view.showResultsPane();
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		// Unset window chosen as a parameter if it was closed
		WindowRef ref = new WindowRef(e.getWindowId(), application.getDocument(e.getWindowId()));
		paramAdapter.unsetParameterValue(ref);

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
