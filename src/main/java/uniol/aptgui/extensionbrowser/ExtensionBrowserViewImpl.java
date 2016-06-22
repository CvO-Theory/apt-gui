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

package uniol.aptgui.extensionbrowser;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.mainwindow.WindowRef;
import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.extensiontable.ExtensionTable;
import uniol.aptgui.swing.extensiontable.ExtensionTableModel;

@SuppressWarnings("serial")
public class ExtensionBrowserViewImpl extends JPanelView<ExtensionBrowserPresenter> implements ExtensionBrowserView {

	private final JLabel documentText;
	private final JComboBox<WindowRef> documentComboBox;
	private final JLabel elementText;
	private final JComboBox<GraphicalElement> elementComboBox;
	private final JLabel extensionsText;
	private final ExtensionTable extensionTable;

	private boolean ignoreSelectionEvents;

	public ExtensionBrowserViewImpl() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(300, 400));

		documentText = new JLabel("Documents");
		documentText.setAlignmentX(Component.LEFT_ALIGNMENT);

		documentComboBox = new JComboBox<>();
		documentComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		documentComboBox.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, documentComboBox.getPreferredSize().height));
		documentComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreSelectionEvents && e.getStateChange() == ItemEvent.SELECTED) {
					getPresenter().onDocumentSelectionChanged((WindowRef) e.getItem());
				}
			}
		});

		elementText = new JLabel("Elements of the selected document");
		elementText.setAlignmentX(Component.LEFT_ALIGNMENT);

		elementComboBox = new JComboBox<>();
		elementComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		elementComboBox.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, elementComboBox.getPreferredSize().height));
		elementComboBox.setRenderer(new DefaultListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				GraphicalElement elem = (GraphicalElement) value;
				return super.getListCellRendererComponent(list, elem.toUserString(), index, isSelected, cellHasFocus);
			}
		});
		elementComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!ignoreSelectionEvents && e.getStateChange() == ItemEvent.SELECTED) {
					getPresenter().onElementSelectionChanged((GraphicalElement) e.getItem());
				}
			}
		});

		extensionsText = new JLabel("Extensions of the selected element");
		extensionsText.setAlignmentX(Component.LEFT_ALIGNMENT);

		extensionTable = new ExtensionTable();

		JScrollPane scrollPane = new JScrollPane(extensionTable);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(documentText);
		add(Box.createVerticalStrut(2));
		add(documentComboBox);
		add(Box.createVerticalStrut(5));
		add(elementText);
		add(Box.createVerticalStrut(2));
		add(elementComboBox);
		add(Box.createVerticalStrut(5));
		add(extensionsText);
		add(Box.createVerticalStrut(2));
		add(scrollPane);
	}

	@Override
	public void setExtensionTableModel(ExtensionTableModel tableModel) {
		extensionTable.setModel(tableModel);
		extensionTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		extensionTable.getColumnModel().getColumn(1).setPreferredWidth(60);
	}

	@Override
	public void setDocumentList(List<WindowRef> documentWindowReferences) {
		ignoreSelectionEvents = true;
		documentComboBox.removeAllItems();
		for (WindowRef ref : documentWindowReferences) {
			documentComboBox.addItem(ref);
		}
		ignoreSelectionEvents = false;
	}

	@Override
	public void setElementList(List<GraphicalElement> graphicalElements) {
		ignoreSelectionEvents = true;
		elementComboBox.removeAllItems();
		for (GraphicalElement elem : graphicalElements) {
			elementComboBox.addItem(elem);
		}
		ignoreSelectionEvents = false;
	}

	@Override
	public void selectDocument(WindowRef refToSelect) {
		ignoreSelectionEvents = true;
		documentComboBox.setSelectedItem(refToSelect);
		ignoreSelectionEvents = false;
	}

	@Override
	public void selectElement(GraphicalElement elemToSelect) {
		ignoreSelectionEvents = true;
		elementComboBox.setSelectedItem(elemToSelect);
		ignoreSelectionEvents = false;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
