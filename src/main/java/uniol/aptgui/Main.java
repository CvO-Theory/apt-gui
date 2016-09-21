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

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Application entry-point.
 */
public class Main implements Runnable {

	private final String[] commandLineArgs;

	public Main(String[] commandLineArgs) {
		this.commandLineArgs = commandLineArgs;
	}

	@Override
	public void run() {
		setLookAndFeel();

		Injector injector = Guice.createInjector(new DependenyModule());
		Application app = injector.getInstance(Application.class);
		app.show();

		// Open files if any were supplied at the command line
		for (String filename : commandLineArgs) {
			File file = new File(filename);
			app.openFile(file);
		}
	}

	private void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main(args));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
