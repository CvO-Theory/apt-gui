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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.mainwindow.WindowRefProvider;
import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.parametertable.PropertyTable;
import uniol.aptgui.swing.parametertable.PropertyTableModel;

@SuppressWarnings("serial")
public class ModuleViewImpl extends JPanelView<ModulePresenter> implements ModuleView {

	private static final int WIDTH = 250;
	private static final int HEIGHT = 350;

	private JTextArea descriptionPane;
	private JScrollPane descriptionScrollPane;

	private JTabbedPane tabbedPane;
	private JPanel parametersContainer;
	private PropertyTable parametersTable;
	@SuppressWarnings("unused")
	private PropertyTableModel parametersTableModel;

	private JPanel resultsContainer;
	private PropertyTable resultsTable;
	private PropertyTableModel resultsTableModel;

	private JPanel bottomPanel;
	private JLabel progressSpinner;
	private JButton runButton;

	public ModuleViewImpl() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setupDescriptionPane();
		setupTabbedPane();
		setupBottomPane();
	}

	private void setupDescriptionPane() {
		descriptionPane = new JTextArea();
		descriptionPane.setEditable(false);
		descriptionPane.setLineWrap(true);
		descriptionPane.setWrapStyleWord(true);
		descriptionPane.setMargin(new Insets(2, 2, 2, 2));
		descriptionScrollPane = new JScrollPane(descriptionPane);
		descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane.setPreferredSize(new Dimension(WIDTH, 100));
		add(descriptionScrollPane, BorderLayout.PAGE_START);
	}

	private void setupParametersContainer() {
		parametersTable = new PropertyTable();
		parametersTable.setFillsViewportHeight(true);
		parametersTable.putClientProperty("terminateEditOnFocusLost", true);

		parametersContainer = new JPanel();
		parametersContainer.setLayout(new BorderLayout());
		parametersContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		parametersContainer.add(new JScrollPane(parametersTable), BorderLayout.CENTER);
	}

	private void setupResultsContainer() {
		resultsTable = new PropertyTable();
		resultsTable.setFillsViewportHeight(true);
		resultsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int viewRow = resultsTable.getSelectedRow();
					int modelRow = resultsTable.convertRowIndexToModel(viewRow);
					Object value = resultsTableModel.getPropertyValueAt(modelRow);
					if (value instanceof WindowRef) {
						getPresenter().focusWindow((WindowRef) value);
					}
				}
			}
		});

		resultsContainer = new JPanel();
		resultsContainer.setLayout(new BorderLayout());
		resultsContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		resultsContainer.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
	}

	private void setupTabbedPane() {
		setupParametersContainer();
		setupResultsContainer();

		tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		tabbedPane.addTab("Parameters", parametersContainer);
		tabbedPane.addTab("Results", resultsContainer);

		add(tabbedPane, BorderLayout.CENTER);
	}

	private void setupBottomPane() {
		progressSpinner = new JLabel();
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPresenter().onRunAbortButtonClicked();
			}
		});
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(progressSpinner);
		bottomPanel.add(Box.createHorizontalStrut(5));
		bottomPanel.add(runButton);

		add(bottomPanel, BorderLayout.PAGE_END);
	}

	@Override
	public void setModuleRunning(boolean running) {
		if (running) {
			runButton.setText("Abort");
			progressSpinner.setIcon(Resource.getIconSpinner());
		} else {
			runButton.setText("Run");
			progressSpinner.setIcon(null);
		}

		parametersTable.setEnabled(!running);
	}

	@Override
	public void setDescription(String description) {
		descriptionPane.setText(description);
		descriptionPane.setCaretPosition(0);
	}

	@Override
	public void showErrorTooFewParameters() {
		JOptionPane.showMessageDialog(this, "Too few parameters supplied.", "Cannot run module",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void setPNWindowRefProvider(WindowRefProvider refProvider) {
		parametersTable.setPetriNetWindowRefProvider(refProvider);
	}

	@Override
	public void setTSWindowRefProvider(WindowRefProvider refProvider) {
		parametersTable.setTransitionSystemWindowRefProvider(refProvider);
	}

	@Override
	public void showResultsPane() {
		tabbedPane.setSelectedIndex(1);
	}

	@Override
	public void setParameterTableModel(PropertyTableModel parameterTableModel) {
		this.parametersTableModel = parameterTableModel;
		this.parametersTable.setModel(parameterTableModel);
	}

	@Override
	public void setResultTableModel(PropertyTableModel resultTableModel) {
		this.resultsTableModel = resultTableModel;
		this.resultsTable.setModel(resultTableModel);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
