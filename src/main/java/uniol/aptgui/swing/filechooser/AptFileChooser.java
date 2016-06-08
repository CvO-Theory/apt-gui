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

package uniol.aptgui.swing.filechooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uniol.apt.io.parser.impl.AptLTSParser;
import uniol.apt.io.parser.impl.AptPNParser;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.io.FileType;

@SuppressWarnings("serial")
public class AptFileChooser extends JFileChooser {

	private static FileFilter filterPn = new ParserFileFilter("Petri Net", new AptPNParser());
	private static FileFilter filterPnNoLayout = new ParserFileFilter("Petri Net, only structure", new AptPNParser());
	private static FileFilter filterTs = new ParserFileFilter("Transition system", new AptLTSParser());
	private static FileFilter filterTsNoLayout = new ParserFileFilter("Transition system, only structure",
			new AptLTSParser());
	private static FileFilter filterSvg = new FileNameExtensionFilter("SVG vector image", "svg");
	private static FileFilter filterPng = new FileNameExtensionFilter("PNG raster image", "png");

	private static Map<FileFilter, FileType> fileTypeMapping;

	static {
		fileTypeMapping = new HashMap<>();
		fileTypeMapping.put(filterPn, FileType.PETRI_NET);
		fileTypeMapping.put(filterPnNoLayout, FileType.PETRI_NET_ONLY_STRUCTURE);
		fileTypeMapping.put(filterTs, FileType.TRANSITION_SYSTEM);
		fileTypeMapping.put(filterTsNoLayout, FileType.TRANSITION_SYSTEM_ONLY_STRUCTURE);
		fileTypeMapping.put(filterSvg, FileType.SVG);
		fileTypeMapping.put(filterPng, FileType.PNG);
	}

	/**
	 * Creates a new file chooser that allows the user to save the given
	 * document to a file of the correct type.
	 *
	 * @param document
	 *                document to save
	 * @return file chooser set-up for save action
	 */
	public static AptFileChooser saveChooser(Document<?> document) {
		AptFileChooser fc = new AptFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		if (document instanceof PnDocument) {
			fc.addChoosableFileFilter(filterPn);
			fc.addChoosableFileFilter(filterPnNoLayout);
			fc.setFileFilter(filterPn);
		} else if (document instanceof TsDocument) {
			fc.addChoosableFileFilter(filterTs);
			fc.addChoosableFileFilter(filterTsNoLayout);
			fc.setFileFilter(filterTs);
		}
		File defaultSave = new File(toValidFileName(document.getName()));
		fc.setSelectedFile(defaultSave);
		fc.setDialogTitle("Save " + document.getName());
		return fc;
	}

	/**
	 * Creates a new file chooser that allows the user to open documents.
	 *
	 * @return file chooser set-up for open action
	 */
	public static AptFileChooser openChooser() {
		AptFileChooser fc = new AptFileChooser();
		fc.addChoosableFileFilter(filterPn);
		fc.addChoosableFileFilter(filterTs);
		fc.setFileFilter(fc.getAcceptAllFileFilter());
		return fc;
	}

	/**
	 * Creates a new file chooser that allows the user to export documents
	 * into other formats.
	 *
	 * @param document
	 *                document to export
	 * @return file chooser set-up for export action
	 */
	public static AptFileChooser exportChooser(Document<?> document) {
		AptFileChooser fc = new AptFileChooser();
		fc.addChoosableFileFilter(filterSvg);
		fc.addChoosableFileFilter(filterPng);
		fc.setAcceptAllFileFilterUsed(false);
		File defaultSave = new File(toValidFileName(document.getName()));
		fc.setSelectedFile(defaultSave);
		fc.setDialogTitle("Export " + document.getName());
		return fc;
	}

	/**
	 * Turns the given string into a string that is allowed as a file name,
	 * i.e. special characters are removed or replaced.
	 *
	 * @param str
	 *                input string that may contain chars that are not
	 *                allowed in file names
	 * @return output string that can be used as a file name
	 */
	private static String toValidFileName(String str) {
		return str.replaceAll("[^a-zA-Z0-9.-]", "_");
	}

	/**
	 * Force creation through static factory methods.
	 */
	private AptFileChooser() {
		// Empty
	}

	/**
	 * Returns the selected file while making sure that it has the correct
	 * extension for the selected file type. I.e. the extension is added if
	 * it is not present.
	 *
	 * @return the selected file
	 */
	public File getSelectedFileWithExtension() {
		File file = getSelectedFile();
		FileFilter filter = getFileFilter();

		if (filter instanceof ParserFileFilter) {
			ParserFileFilter pFilter = (ParserFileFilter) filter;
			if (!pFilter.accept(file)) {
				file = new File(file.getAbsolutePath() + "." + pFilter.getDefaultExtension());
			}
		} else if (filter instanceof FileNameExtensionFilter) {
			FileNameExtensionFilter extFilter = (FileNameExtensionFilter) filter;
			if (!extFilter.accept(file)) {
				file = new File(file.getAbsolutePath() + "." + extFilter.getExtensions()[0]);
			}
		}

		return file;
	}

	/**
	 * Returns the file type that was selected by the user.
	 *
	 * @return the file type that was selected by the user
	 */
	public FileType getSelectedFileType() {
		if (fileTypeMapping.containsKey(getFileFilter())) {
			return fileTypeMapping.get(getFileFilter());
		} else {
			return FileType.ANY;
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
