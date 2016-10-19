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

package uniol.aptgui.editor.features;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uniol.aptgui.editor.features.MouseEventUtil.leftClickAt;

import java.awt.Point;
import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;

import uniol.apt.adt.extension.IExtensible;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.Application;
import uniol.aptgui.commands.History;
import uniol.aptgui.document.EditingOptions;
import uniol.aptgui.document.TsDocument;
import uniol.aptgui.document.graphical.GraphicalElement;
import uniol.aptgui.document.graphical.edges.GraphicalArc;
import uniol.aptgui.document.graphical.nodes.GraphicalState;

/**
 * Tests the selection tool. This is kind of an integration tests as Document
 * and History are not mocked.
 */
public class SelectionToolTest {

	SelectionTool selectionTool;
	TsDocument document;
	History history;

	GraphicalState gs1;
	Point ps1;

	GraphicalState gs2;
	Point ps2;

	GraphicalArc ga1;
	Point pbp1;

	@Before
	public void setUp() {
		ps1 = new Point(0, 0);
		ps2 = new Point(200, 0);
		pbp1 = new Point(50, 0);

		gs1 = new GraphicalState();
		gs1.setCenter(new Point(ps1));
		gs2 = new GraphicalState();
		gs2.setCenter(new Point(ps2));
		ga1 = new GraphicalArc(gs1, gs2);
		ga1.addBreakpoint(new Point(pbp1));

		document = new TsDocument(new TransitionSystem());
		// Fake-add states and arc with model elements so that they get
		// returned to the the selection tool
		document.add(gs1, mock(IExtensible.class));
		document.add(gs2, mock(IExtensible.class));
		document.add(ga1, mock(IExtensible.class));

		history = new History(mock(Application.class), mock(EventBus.class));
		EditingOptions eo = mock(EditingOptions.class);
		when(eo.isSnapToGridEnabled()).thenReturn(false);

		selectionTool = new SelectionTool(document, history, eo);
		// Pre-activate for all tests
		selectionTool.onActivated();
	}

	@Test
	public void testSelect() {
		simulateClick(ps1, false);
		assertThat(document.getSelection(), contains((GraphicalElement) gs1));
		// Clicking on another element de-selects the first one
		simulateClick(ps2, false);
		assertThat(document.getSelection(), contains((GraphicalElement) gs2));
	}

	@Test
	public void testSelectWithCtrl() {
		simulateClick(ps1, false);
		simulateClick(pbp1, true);
		simulateClick(ps2, true);
		assertThat(document.getSelection(), containsInAnyOrder(gs1, gs2, ga1));
	}

	@Test
	public void testSelectWithFrame() {
		Point topLeft = add(ps1, new Point(-50, -50));
		Point btmRight = add(ps2, new Point(50, 50));
		simulateDrag(topLeft, btmRight);
		assertThat(document.getSelection(), containsInAnyOrder(gs1, gs2, ga1));
	}

	@Test
	public void testDeselectWithCtrl() {
		simulateClick(ps1, false);
		simulateClick(ps1, true);
		assertThat(document.getSelection(), is(empty()));
	}

	@Test
	public void testDragSingleNode() {
		simulateClick(ps1, false);
		Point target = midpoint(ps1, ps2);
		simulateDrag(ps1, target);
		assertThat(gs1.getCenter(), is(equalTo(target)));
	}

	@Test
	public void testDragSelection() {
		simulateClick(ps1, false);
		simulateClick(ps2, true);
		Point offset = new Point(100, 50);
		simulateDrag(ps2, add(ps2, offset));
		assertThat(gs1.getCenter(), is(equalTo(add(ps1, offset))));
		assertThat(gs2.getCenter(), is(equalTo(add(ps2, offset))));
	}

	@Test
	public void testDragSelectionFailure() {
		simulateClick(ps1, false);
		Point midpoint = midpoint(ps1, ps2);
		simulateClick(midpoint, true);
		Point offset = new Point(100, 50);
		// Try dragging the edge; this should fail
		simulateDrag(midpoint, add(midpoint, offset));
		assertThat(gs1.getCenter(), is(equalTo(ps1)));
	}

	@Test
	public void testDragCreateBreakpoint() {
		Point source = midpoint(ps1, ps2);
		Point target = add(source, new Point(0, 100));
		simulateDrag(source, target);
		assertThat(ga1.getBreakpoints(), contains(pbp1, target));
	}

	@Test
	public void testDragBreakpoint() {
		Point target = add(pbp1, new Point(0, 100));
		simulateDrag(pbp1, target);
		assertThat(ga1.getBreakpoints(), contains(target));
	}

	@Test
	public void testDragBreakpointWithSelection() {
		// Select both nodes and the edge
		simulateClick(ps1, false);
		simulateClick(ps2, true);
		simulateClick(midpoint(ps1, ps2), true);
		Point offset = new Point(100, 50);
		simulateDrag(ps2, add(ps2, offset));
		assertThat(ga1.getBreakpoints(), contains(add(pbp1, offset)));
	}

	private void simulateClick(Point pos, boolean ctrlPressed) {
		MouseEvent evt = leftClickAt(pos, ctrlPressed);
		selectionTool.mousePressed(evt);
		selectionTool.mouseReleased(evt);
	}

	private void simulateDrag(Point from, Point to) {
		Point midpoint = midpoint(from, to);
		selectionTool.mousePressed(leftClickAt(from, false));
		selectionTool.mouseDragged(leftClickAt(midpoint, false));
		selectionTool.mouseDragged(leftClickAt(to, false));
		selectionTool.mouseReleased(leftClickAt(to, false));
	}

	private Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	private Point add(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
