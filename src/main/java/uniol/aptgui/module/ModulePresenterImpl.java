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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.PetriNetOrTransitionSystem;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.module.Module;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.module.impl.ModuleInvoker;
import uniol.apt.module.impl.ModuleUtils;
import uniol.apt.module.impl.Parameter;
import uniol.apt.module.impl.ReturnValue;
import uniol.apt.ui.impl.AptParametersTransformer;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.events.ModuleExecutedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowType;
import uniol.aptgui.swing.parametertable.PropertyTableModel;
import uniol.aptgui.swing.parametertable.PropertyType;
import uniol.aptgui.swing.parametertable.WindowRef;

public class ModulePresenterImpl extends AbstractPresenter<ModulePresenter, ModuleView> implements ModulePresenter {

	private final Application application;

	private WindowId windowId;
	private Module module;

	private List<Parameter> parameters;
	private List<Parameter> allParameters;
	private List<ReturnValue> returnValues;

	private PropertyTableModel parameterTableModel;
	private PropertyTableModel resultTableModel;

	private Future<List<Object>> moduleFuture;

	@Inject
	public ModulePresenterImpl(ModuleView view, Application application) {
		super(view);
		this.application = application;
		application.getEventBus().register(this);
	}

	@Override
	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
		parameters = ModuleUtils.getParameters(module);
		allParameters = ModuleUtils.getAllParameters(module);
		returnValues = ModuleUtils.getReturnValues(module);

		parameterTableModel = new PropertyTableModel("Parameter", "Value", allParameters.size());
		parameterTableModel.setEditable(true);
		for (int i = 0; i < allParameters.size(); i++) {
			Parameter param = allParameters.get(i);
			parameterTableModel.setProperty(i, PropertyType.fromModelType(param.getKlass()),
					param.getName());
		}

		view.setDescription(module.getLongDescription());
		view.setPetriNetWindowRefProvider(new WindowRefProviderImpl(application, WindowType.PETRI_NET));
		view.setTransitionSystemWindowRefProvider(
				new WindowRefProviderImpl(application, WindowType.TRANSITION_SYSTEM));
		view.setParameterTableModel(parameterTableModel);
	}

	@Override
	public void onRunButtonClicked() {
		try {
			Object[] paramValues = getParameterValues();
			// Make sure all parameters are filled in.
			if (paramValues.length < parameters.size()) {
				view.showErrorTooFewParameters();
				return;
			}
			// Module is executed on another thread.
			moduleFuture = invokeModule(paramValues);
			view.setModuleRunning(true);
			// Invoke new thread that blocks until results are available.
			displayResultsWhenFinished();
		} catch (ModuleException e) {
			view.showErrorModuleException(e.getMessage());
			e.printStackTrace();
		}
	}

	private Future<List<Object>> invokeModule(final Object[] paramValues) {
		return application.getExecutorService().submit(new Callable<List<Object>>() {
			@Override
			public List<Object> call() throws Exception {
				ModuleInvoker invoker = new ModuleInvoker();
				List<Object> filledReturnValues = invoker.invoke(module, paramValues);
				return filledReturnValues;
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
	 * Returns an array of non-null parameter values as model objects.
	 *
	 * @return
	 * @throws ModuleException
	 */
	private Object[] getParameterValues() throws ModuleException {
		List<Object> result = new ArrayList<>();
		for (int row = 0; row < parameterTableModel.getRowCount(); row++) {
			if (parameterTableModel.getPropertyValueAt(row) != null) {
				result.add(getParameterValueAt(row));
			}
		}
		return result.toArray();
	}

	/**
	 * Returns the parameter value from the parameter table.
	 *
	 * @param row
	 *                parameter row
	 * @return parameter value as model object
	 * @throws ModuleException
	 */
	private Object getParameterValueAt(int row) throws ModuleException {
		PropertyType type = parameterTableModel.getPropertyTypeAt(row);
		WindowRef ref;
		switch (type) {
		case PETRI_NET:
			ref = parameterTableModel.getPropertyValueAt(row);
			if (ref.getDocument() instanceof PnDocument) {
				PetriNet pn = (PetriNet) ref.getDocument().getModel();
				return new PetriNet(pn);
			}
		case TRANSITION_SYSTEM:
			ref = parameterTableModel.getPropertyValueAt(row);
			if (ref.getDocument() instanceof TsDocument) {
				TransitionSystem ts = (TransitionSystem) ref.getDocument().getModel();
				return new TransitionSystem(ts);
			}
		case PETRI_NET_OR_TRANSITION_SYSTEM:
			ref = parameterTableModel.getPropertyValueAt(row);
			if (ref.getDocument() instanceof PnDocument) {
				PetriNet pn = (PetriNet) ref.getDocument().getModel();
				return new PetriNetOrTransitionSystem(pn);
			}
			if (ref.getDocument() instanceof TsDocument) {
				TransitionSystem ts = (TransitionSystem) ref.getDocument().getModel();
				return new PetriNetOrTransitionSystem(ts);
			}
			throw new AssertionError();
		default:
			String value = parameterTableModel.getPropertyValueAt(row);
			Object modelValue = AptParametersTransformer.INSTANCE.transform(value, allParameters.get(row).getKlass());
			return modelValue;
		}
	}

	/**
	 * Blocks until the module future result is available and then shows the
	 * module results in the view.
	 */
	private void waitForModuleExecution() {
		try {
			final List<Object> filledReturnValues = moduleFuture.get();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					showModuleResults(filledReturnValues);
					application.getEventBus().post(new ModuleExecutedEvent(ModulePresenterImpl.this, module));
				}
			});
		} catch (InterruptedException | CancellationException ex) {
			// Ignore these two types of exceptions.
		} catch (final Exception ex) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					view.showErrorModuleException(ex.getMessage());
					ex.printStackTrace();
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

	private void showModuleResults(List<Object> filledReturnValues) {
		// Filter null-results.
		List<String> nonNullReturnNames = new ArrayList<>();
		List<Object> nonNullReturnValues = new ArrayList<>();
		for (int row = 0; row < filledReturnValues.size(); row++) {
			Object retVal = filledReturnValues.get(row);
			if (retVal != null) {
				String name = returnValues.get(row).getName();
				nonNullReturnNames.add(name);
				nonNullReturnValues.add(retVal);
			}
		}

		// Fill table model.
		resultTableModel = new PropertyTableModel("Result", "Value", nonNullReturnValues.size());
		for (int i = 0; i < nonNullReturnValues.size(); i++) {
			Object retVal = nonNullReturnValues.get(i);
			Class<?> retClass = retVal.getClass();
			String name = nonNullReturnNames.get(i);
			PropertyType type = PropertyType.fromModelType(retClass);

			// Transform model objects to their proxy counterparts
			// for display.
			switch (type) {
			case PETRI_NET: {
				PetriNet pn = (PetriNet) retVal;
				Document<?> doc = new PnDocument(pn);
				WindowRef ref = openDocument(doc);
				resultTableModel.setProperty(i, type, name, ref);
				break;
			}
			case TRANSITION_SYSTEM: {
				TransitionSystem ts = (TransitionSystem) retVal;
				Document<?> doc = new TsDocument(ts);
				WindowRef ref = openDocument(doc);
				resultTableModel.setProperty(i, type, name, ref);
				break;
			}
			default:
				String proxy = retVal.toString();
				resultTableModel.setProperty(i, type, name, proxy);
				break;
			}
		}

		view.setResultTableModel(resultTableModel);
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
		for (int row = 0; row < parameterTableModel.getRowCount(); row++) {
			PropertyType type = parameterTableModel.getPropertyTypeAt(row);
			if (type == PropertyType.PETRI_NET || type == PropertyType.TRANSITION_SYSTEM) {
				WindowRef ref = parameterTableModel.getPropertyValueAt(row);
				if (ref != null && ref.getWindowId() == e.getWindowId()) {
					parameterTableModel.setPropertyValue(row, null);
				}
			}
		}

		// Cancel module execution when parent window closes
		if (e.getWindowId() == windowId) {
			moduleFuture.cancel(true);
		}
	}

	@Override
	public void onResultsTableDoubleClick(int modelRow) {
		PropertyType type = resultTableModel.getPropertyTypeAt(modelRow);
		if (type == PropertyType.PETRI_NET || type == PropertyType.TRANSITION_SYSTEM) {
			WindowRef ref = resultTableModel.getPropertyValueAt(modelRow);
			// If the referenced window is still open, focus it.
			if (application.getDocument(ref.getWindowId()) != null) {
				application.focusWindow(ref.getWindowId());
			}
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
