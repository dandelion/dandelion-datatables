/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.core.ajax;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.dandelion.datatables.core.constants.DTConstants;

/**
 * <p>
 * Test the mapping of DataTables URL parameters to {@link DatatablesCriterias}
 * when using server-side processing.
 * 
 * @author Thibault Duchateau
 */
public class DatatablesCriteriasTest {

	private MockHttpServletRequest request;

	@Before
	public void createMainGenerator() {
		request = new MockHttpServletRequest();
	}

	@Test
	public void should_return_null_criterias_when_null_request() {
		request = null;

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNull();
	}

	@Test
	public void should_return_non_null_criterias_when_request_is_not_null() {

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getDisplayStart()).isEqualTo(-1);
		assertThat(criterias.getDisplaySize()).isEqualTo(-1);
		assertThat(criterias.getInternalCounter()).isEqualTo(-1);
		assertThat(criterias.getColumnDefs()).isEmpty();
		assertThat(criterias.getSortingColumnDefs()).isEmpty();
	}

	@Test
	public void should_return_criterias_when_sorting_disabled_and_filtering_disabled() {

		request.addParameter(DTConstants.DT_S_ECHO, "1");
		request.addParameter(DTConstants.DT_I_COLUMNS, "2");
		request.addParameter(DTConstants.DT_I_DISPLAY_START, "0");
		request.addParameter(DTConstants.DT_I_DISPLAY_LENGTH, "10");
		request.addParameter(DTConstants.DT_M_DATA_PROP + 0, "prop1");
		request.addParameter(DTConstants.DT_M_DATA_PROP + 1, "prop2");

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getDisplayStart()).isEqualTo(0);
		assertThat(criterias.getDisplaySize()).isEqualTo(10);
		assertThat(criterias.getInternalCounter()).isEqualTo(1);

		assertThat(criterias.getColumnDefs()).hasSize(2);
		assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getColumnDefs().get(0).isSortable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFilterable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();

		assertThat(criterias.getColumnDefs().get(1).getName()).isEqualTo("prop2");
		assertThat(criterias.getColumnDefs().get(1).isSortable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFilterable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();

		assertThat(criterias.getSortingColumnDefs()).isEmpty();
	}

	@Test
	public void should_return_criterias_when_sorting_enabled_and_filtering_disabled() {

		request.addParameter(DTConstants.DT_S_ECHO, "1");
		request.addParameter(DTConstants.DT_I_COLUMNS, "2");
		request.addParameter(DTConstants.DT_I_DISPLAY_START, "0");
		request.addParameter(DTConstants.DT_I_DISPLAY_LENGTH, "10");
		request.addParameter(DTConstants.DT_M_DATA_PROP + 0, "prop1");
		request.addParameter(DTConstants.DT_B_SORTABLE + 0, "true");
		request.addParameter(DTConstants.DT_M_DATA_PROP + 1, "prop2");
		request.addParameter(DTConstants.DT_B_SORTABLE + 1, "true");
		request.addParameter(DTConstants.DT_I_SORTING_COLS, "1");
		request.addParameter(DTConstants.DT_I_SORT_COL + "0", "0");
		request.addParameter(DTConstants.DT_S_SORT_DIR + "0", "asc");

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getDisplayStart()).isEqualTo(0);
		assertThat(criterias.getDisplaySize()).isEqualTo(10);
		assertThat(criterias.getInternalCounter()).isEqualTo(1);

		assertThat(criterias.getColumnDefs()).hasSize(2);
		assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getColumnDefs().get(0).isSortable()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFilterable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();

		assertThat(criterias.getColumnDefs().get(1).getName()).isEqualTo("prop2");
		assertThat(criterias.getColumnDefs().get(1).isSortable()).isTrue();
		assertThat(criterias.getColumnDefs().get(1).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFilterable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();

		assertThat(criterias.getSortingColumnDefs()).hasSize(1);
		assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getColumnDefs().get(0).isSortable()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFilterable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();
	}
}