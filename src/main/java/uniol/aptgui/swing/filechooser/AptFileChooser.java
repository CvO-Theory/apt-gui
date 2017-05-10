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

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.LTSParsers;
import uniol.apt.io.parser.PNParsers;
import uniol.apt.io.parser.Parser;
import uniol.apt.io.parser.ParserNotFoundException;
import uniol.apt.io.parser.Parsers;
import uniol.apt.io.parser.impl.AptLTSParser;
import uniol.apt.io.parser.impl.AptPNParser;
import uniol.apt.io.renderer.LTSRenderers;
import uniol.apt.io.renderer.PNRenderers;
import uniol.apt.io.renderer.Renderer;
import uniol.apt.io.renderer.RendererNotFoundException;
import uniol.apt.io.renderer.Renderers;
import uniol.aptgui.document.Document;
import uniol.aptgui.document.PnDocument;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.io.parser.AptParser;
import uniol.aptgui.io.parser.DocumentParser;
import uniol.aptgui.io.parser.ParserDocumentParser;
import uniol.aptgui.io.renderer.DocumentRenderer;
import uniol.aptgui.io.renderer.PnDocumentRenderer;
import uniol.aptgui.io.renderer.PnStructureDocumentRenderer;
import uniol.aptgui.io.renderer.PngDocumentRenderer;
import uniol.aptgui.io.renderer.RendererDocumentRenderer;
import uniol.aptgui.io.renderer.SvgDocumentRenderer;
import uniol.aptgui.io.renderer.TsDocumentRenderer;
import uniol.aptgui.io.renderer.TsStructureDocumentRenderer;

@SuppressWarnings("serial")
public class AptFileChooser extends JFileChooser implements AptFileChooserFactory {

	private static FileFilter filterPn = new ParserFileFilter("Petri Net", new AptPNParser());
	private static FileFilter filterPnNoLayout = new ParserFileFilter("Petri Net, only structure", new AptPNParser());
	private static FileFilter filterTs = new ParserFileFilter("Transition system", new AptLTSParser());
	private static FileFilter filterTsNoLayout = new ParserFileFilter("Transition system, only structure",
			new AptLTSParser());
	private static FileFilter filterSvg = new FileNameExtensionFilter("SVG vector image", "svg");
	private static FileFilter filterPng = new FileNameExtensionFilter("PNG raster image", "png");

	private static final String PREF_KEY_INIT_DIRECTORY = "initialFileChooserDirectory";

	private final Map<FileFilter, DocumentRenderer> rendererMapping;
	private final Map<FileFilter, DocumentParser> parserMapping;

	private AptFileChooser(Map<FileFilter, DocumentRenderer> rendererMapping,
			Map<FileFilter, DocumentParser> parserMapping) {
		this.rendererMapping = rendererMapping;
		this.parserMapping = parserMapping;
	}

	@Inject
	public AptFileChooser(PnDocumentRenderer pnRenderer,
			PnStructureDocumentRenderer pnStructureRenderer,
			TsDocumentRenderer tsRenderer,
			TsStructureDocumentRenderer tsStructureRenderer,
			PngDocumentRenderer pngRenderer,
			SvgDocumentRenderer svgRenderer,
			AptParser aptParser) {
		setCurrentDirectory(getInitialDirectory());

		this.rendererMapping = new HashMap<>();
		rendererMapping.put(filterPn, pnRenderer);
		rendererMapping.put(filterPnNoLayout, pnStructureRenderer);
		rendererMapping.put(filterTs, tsRenderer);
		rendererMapping.put(filterTsNoLayout, tsStructureRenderer);
		rendererMapping.put(filterSvg, svgRenderer);
		rendererMapping.put(filterPng, pngRenderer);

		this.parserMapping = new HashMap<>();
		parserMapping.put(filterPn, aptParser);
		parserMapping.put(filterTs, aptParser);
	}

	@Override
	public AptFileChooser saveChooser(Document<?> document) {
		AptFileChooser fc = new AptFileChooser(rendererMapping, parserMapping);
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

	@Override
	public AptFileChooser openChooser() {
		AptFileChooser fc = new AptFileChooser(rendererMapping, parserMapping);
		fc.addChoosableFileFilter(filterPn);
		fc.addChoosableFileFilter(filterTs);
		fc.setFileFilter(fc.getAcceptAllFileFilter());
		return fc;
	}

	@Override
	public AptFileChooser importChooser() {
		Map<FileFilter, DocumentParser> mapping = new HashMap<>(parserMapping);
		AptFileChooser fc = new AptFileChooser(rendererMapping, mapping);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(filterPn);
		fc.addChoosableFileFilter(filterTs);
		addParsers(fc, mapping, PNParsers.INSTANCE, PetriNet.class, "Petri net");
		addParsers(fc, mapping, LTSParsers.INSTANCE, TransitionSystem.class, "Transition system");
		return fc;
	}

	private <T> void addParsers(AptFileChooser fc,
			Map<FileFilter, DocumentParser> mapping,
			Parsers<T> parsers,
			Class<T> klass,
			String name) {
		for (String format : parsers.getSupportedFormats()) {
			Parser<T> parser;
			try {
				parser = parsers.getParser(format);
			} catch (ParserNotFoundException ex) {
				throw new RuntimeException("Format " + format + " is supported, but unsupported!?", ex);
			}
			FileFilter filter = new ParserFileFilter(name, parser);
			mapping.put(filter, new ParserDocumentParser<T>(klass, parser));
			fc.addChoosableFileFilter(filter);
		}
	}

	@Override
	public AptFileChooser exportChooser(Document<?> document) {
		Map<FileFilter, DocumentRenderer> mapping = new HashMap<>(rendererMapping);
		AptFileChooser fc = new AptFileChooser(mapping, parserMapping);
		fc.addChoosableFileFilter(filterSvg);
		fc.addChoosableFileFilter(filterPng);
		fc.setAcceptAllFileFilterUsed(false);
		if (document instanceof PnDocument) {
			addRenderers(fc, mapping, PNRenderers.INSTANCE, PetriNet.class, "Petri net");
		} else if (document instanceof TsDocument) {
			addRenderers(fc, mapping, LTSRenderers.INSTANCE, TransitionSystem.class, "Transition system");
		}
		File defaultSave = new File(toValidFileName(document.getName()));
		fc.setSelectedFile(defaultSave);
		fc.setDialogTitle("Export " + document.getName());
		return fc;
	}

	private <T> void addRenderers(AptFileChooser fc,
			Map<FileFilter, DocumentRenderer> mapping,
			Renderers<T> renderers,
			Class<T> klass,
			String name) {
		for (String format : renderers.getSupportedFormats()) {
			Renderer<T> renderer;
			try {
				renderer = renderers.getRenderer(format);
			} catch (RendererNotFoundException ex) {
				throw new RuntimeException("Format " + format + " is supported, but unsupported!?", ex);
			}
			FileFilter filter = new RendererFileFilter(name, renderer);
			mapping.put(filter, new RendererDocumentRenderer<T>(klass, renderer));
			fc.addChoosableFileFilter(filter);
		}
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
	 * Returns the initial directory for a file chooser.
	 * @return directory File object
	 */
	private File getInitialDirectory() {
		Preferences prefs = Preferences.userNodeForPackage(AptFileChooser.class);
		String path = prefs.get(PREF_KEY_INIT_DIRECTORY, System.getProperty("user.home"));
		return new File(path);
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

		if (file == null) {
			return null;
		}

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
	 * Returns the document renderer that was selected by the user.
	 *
	 * @return the document renderer that was selected by the user
	 */
	public DocumentRenderer getSelectedFileDocumentRenderer() {
		return rendererMapping.get(getFileFilter());
	}

	/**
	 * Returns the document parser that was selected by the user.
	 *
	 * @return the document parser that was selected by the user
	 */
	public DocumentParser getSelectedFileDocumentParser() {
		return parserMapping.get(getFileFilter());
	}

	@Override
	public int showOpenDialog(Component parent) throws HeadlessException {
		int res = super.showOpenDialog(parent);
		if (res == APPROVE_OPTION) {
			saveSelectedDirectory();
		}
		return res;
	}

	@Override
	public int showSaveDialog(Component parent) throws HeadlessException {
		int res = super.showSaveDialog(parent);
		if (res == APPROVE_OPTION) {
			saveSelectedDirectory();
		}
		return res;
	}

	/**
	 * Shows the save file chooser. The user can select a file path and name
	 * that should be saved to. If it already exists the user will be asked
	 * if he wants to overwrite that file. Returns true if the user (still) wants
	 * to save a file.
	 *
	 * @param dialogParent
	 *                parent component for the dialog
	 * @return true, if the save should be performed; false, if the process
	 *         was cancelled by the user
	 */
	public boolean performSaveInteraction(Component dialogParent) {
		int fcRes;
		while ((fcRes = showSaveDialog(dialogParent)) == JFileChooser.APPROVE_OPTION
				&& getSelectedFileWithExtension().exists()) {
			int ow = askOverwrite(getSelectedFileWithExtension(), dialogParent);
			if (ow == JOptionPane.YES_OPTION) {
				return true;
			} else if (ow == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		return fcRes == JFileChooser.APPROVE_OPTION;
	}

	private int askOverwrite(File file, Component dialogParent) {
		return JOptionPane.showConfirmDialog(dialogParent,
			"A file with the name '" + file.getName() + "' already exists. Do you want to overwrite it?",
			"Overwrite existing file?", JOptionPane.YES_NO_CANCEL_OPTION);
	}

	private void saveSelectedDirectory() {
		Preferences prefs = Preferences.userNodeForPackage(AptFileChooser.class);
		prefs.put(PREF_KEY_INIT_DIRECTORY, getCurrentDirectory().getAbsolutePath());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
