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

package uniol.aptgui;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import uniol.apt.module.AptModuleRegistry;
import uniol.apt.module.ModuleRegistry;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.EditorPresenter;
import uniol.aptgui.editor.EditorPresenterImpl;
import uniol.aptgui.editor.EditorView;
import uniol.aptgui.editor.EditorViewImpl;
import uniol.aptgui.editor.document.EditingOptions;
import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.layout.Layout;
import uniol.aptgui.editor.layout.LayoutOptions;
import uniol.aptgui.editor.layout.PreservingRandomLayout;
import uniol.aptgui.extensionbrowser.ExtensionBrowserPresenter;
import uniol.aptgui.extensionbrowser.ExtensionBrowserPresenterImpl;
import uniol.aptgui.extensionbrowser.ExtensionBrowserView;
import uniol.aptgui.extensionbrowser.ExtensionBrowserViewImpl;
import uniol.aptgui.io.renderer.DocumentRendererFactory;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.MainWindowPresenterImpl;
import uniol.aptgui.mainwindow.MainWindowView;
import uniol.aptgui.mainwindow.MainWindowViewImpl;
import uniol.aptgui.mainwindow.menu.MenuPresenter;
import uniol.aptgui.mainwindow.menu.MenuPresenterImpl;
import uniol.aptgui.mainwindow.menu.MenuView;
import uniol.aptgui.mainwindow.menu.MenuViewImpl;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenterImpl;
import uniol.aptgui.mainwindow.toolbar.ToolbarView;
import uniol.aptgui.mainwindow.toolbar.ToolbarViewImpl;
import uniol.aptgui.module.ModulePresenter;
import uniol.aptgui.module.ModulePresenterImpl;
import uniol.aptgui.module.ModuleView;
import uniol.aptgui.module.ModuleViewImpl;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenterImpl;
import uniol.aptgui.modulebrowser.ModuleBrowserView;
import uniol.aptgui.modulebrowser.ModuleBrowserViewImpl;
import uniol.aptgui.window.external.ExternalWindowPresenter;
import uniol.aptgui.window.external.ExternalWindowPresenterImpl;
import uniol.aptgui.window.external.ExternalWindowView;
import uniol.aptgui.window.external.ExternalWindowViewImpl;
import uniol.aptgui.window.internal.InternalWindowPresenter;
import uniol.aptgui.window.internal.InternalWindowPresenterImpl;
import uniol.aptgui.window.internal.InternalWindowView;
import uniol.aptgui.window.internal.InternalWindowViewImpl;

public class DependenyModule extends AbstractModule {

	@Override
	protected void configure() {
		// Singletons
		bind(Application.class).to(ApplicationImpl.class).in(Singleton.class);
		bind(History.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(DocumentRendererFactory.class).in(Singleton.class);

		// Instance bindings
		bind(RenderingOptions.class).toInstance(getRenderingOptionsInstance());
		bind(EditingOptions.class).toInstance(getEditingOptionsInstance());
		bind(LayoutOptions.class).toInstance(getLayoutOptionsInstance());
		bind(ModuleRegistry.class).toInstance(AptModuleRegistry.INSTANCE);

		// Normal bindings
		bind(MainWindowPresenter.class).to(MainWindowPresenterImpl.class);
		bind(MainWindowView.class).to(MainWindowViewImpl.class);
		bind(MenuPresenter.class).to(MenuPresenterImpl.class);
		bind(MenuView.class).to(MenuViewImpl.class);
		bind(ExternalWindowPresenter.class).to(ExternalWindowPresenterImpl.class);
		bind(ExternalWindowView.class).to(ExternalWindowViewImpl.class);
		bind(InternalWindowPresenter.class).to(InternalWindowPresenterImpl.class);
		bind(InternalWindowView.class).to(InternalWindowViewImpl.class);
		bind(EditorPresenter.class).to(EditorPresenterImpl.class);
		bind(EditorView.class).to(EditorViewImpl.class);
		bind(ToolbarPresenter.class).to(ToolbarPresenterImpl.class);
		bind(ToolbarView.class).to(ToolbarViewImpl.class);
		bind(ExtensionBrowserPresenter.class).to(ExtensionBrowserPresenterImpl.class);
		bind(ExtensionBrowserView.class).to(ExtensionBrowserViewImpl.class);
		bind(ModuleBrowserPresenter.class).to(ModuleBrowserPresenterImpl.class);
		bind(ModuleBrowserView.class).to(ModuleBrowserViewImpl.class);
		bind(ModulePresenter.class).to(ModulePresenterImpl.class);
		bind(ModuleView.class).to(ModuleViewImpl.class);
		bind(Layout.class).to(PreservingRandomLayout.class);
	}

	private RenderingOptions getRenderingOptionsInstance() {
		return RenderingOptions.fromUserPreferences();
	}

	private EditingOptions getEditingOptionsInstance() {
		return EditingOptions.fromUserPreferences();
	}

	private LayoutOptions getLayoutOptionsInstance() {
		return LayoutOptions.fromUserPreferences();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
