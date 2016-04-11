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

import uniol.aptgui.commands.History;
import uniol.aptgui.editor.EditorView;
import uniol.aptgui.editor.EditorViewImpl;
import uniol.aptgui.editor.PnEditorPresenter;
import uniol.aptgui.editor.PnEditorPresenterImpl;
import uniol.aptgui.editor.TsEditorPresenter;
import uniol.aptgui.editor.TsEditorPresenterImpl;
import uniol.aptgui.internalwindow.InternalWindowPresenter;
import uniol.aptgui.internalwindow.InternalWindowPresenterImpl;
import uniol.aptgui.internalwindow.InternalWindowView;
import uniol.aptgui.internalwindow.InternalWindowViewImpl;
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

public class DependenyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Application.class).to(ApplicationImpl.class).in(Singleton.class);
		bind(MainWindowPresenter.class).to(MainWindowPresenterImpl.class);
		bind(MainWindowView.class).to(MainWindowViewImpl.class);
		bind(MenuPresenter.class).to(MenuPresenterImpl.class);
		bind(MenuView.class).to(MenuViewImpl.class);
		bind(InternalWindowPresenter.class).to(InternalWindowPresenterImpl.class);
		bind(InternalWindowView.class).to(InternalWindowViewImpl.class);
		bind(PnEditorPresenter.class).to(PnEditorPresenterImpl.class);
		bind(TsEditorPresenter.class).to(TsEditorPresenterImpl.class);
		bind(EditorView.class).to(EditorViewImpl.class);
		bind(ToolbarPresenter.class).to(ToolbarPresenterImpl.class);
		bind(ToolbarView.class).to(ToolbarViewImpl.class);
		bind(History.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
