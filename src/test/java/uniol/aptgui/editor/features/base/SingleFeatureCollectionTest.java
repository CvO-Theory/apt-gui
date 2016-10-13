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

package uniol.aptgui.editor.features.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SingleFeatureCollectionTest {

	SingleFeatureCollection col = new SingleFeatureCollection();
	Feature feature0 = mock(Feature.class);
	Feature feature1 = mock(Feature.class);
	Feature feature2 = mock(Feature.class);

	@Test
	public void testActivation() {
		col.put(FeatureId.PN_CREATE_FLOW, feature0);
		col.put(FeatureId.PN_CREATE_PLACE, feature1);
		col.put(FeatureId.PN_CREATE_TRANSITION, feature2);
		assertThat(col.getActiveFeature(), is(nullValue()));
		col.setActive(FeatureId.PN_CREATE_FLOW);
		assertThat(col.getActiveFeature(), is(feature0));
		verify(feature0).onActivated();
		reset(feature0, feature1, feature2);
		col.setActive(FeatureId.PN_CREATE_PLACE);
		assertThat(col.getActiveFeature(), is(feature1));
		verify(feature0).onDeactivated();
		verify(feature1).onActivated();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
