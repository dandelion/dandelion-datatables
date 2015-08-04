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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.dandelion.datatables.core.generator.DTConstants;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Test the mapping of DataTables URL parameters to {@link DatatablesCriterias}
 * when using server-side processing.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class DatatablesCriteriasTest {

	private MockHttpServletRequest request;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void createMainGenerator() {
		request = new MockHttpServletRequest();
	}

	@Test
	public void should_return_null_criterias_when_null_request() {
		request = null;

		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The HTTP request cannot be null");

		DatatablesCriterias.getFromRequest(request);
	}

	@Test
	public void should_return_non_null_criterias_when_request_is_not_null() {

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getStart()).isEqualTo(-1);
		assertThat(criterias.getLength()).isEqualTo(-1);
		assertThat(criterias.getDraw()).isEqualTo(-1);
		assertThat(criterias.getColumnDefs()).isEmpty();
		assertThat(criterias.getSortedColumnDefs()).isEmpty();
	}
	
	@Test
	public void should_return_criterias_when_single_column_defined() {
	    
	    request.addParameter(DTConstants.DT_I_DRAW, "1");
        request.addParameter(DTConstants.DT_I_START, "0");
        request.addParameter(DTConstants.DT_I_LENGTH, "10");
        request.addParameter("columns[0][data]", "prop1");
        
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        
        assertThat(criterias).isNotNull();
        assertThat(criterias.getSearch()).isNull();
        assertThat(criterias.getStart()).isEqualTo(0);
        assertThat(criterias.getLength()).isEqualTo(10);
        assertThat(criterias.getDraw()).isEqualTo(1);
        
        assertThat(criterias.getColumnDefs()).hasSize(1);
        assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
        assertThat(criterias.getColumnDefs().get(0).isSortable()).isFalse();
        assertThat(criterias.getColumnDefs().get(0).isSorted()).isFalse();
        assertThat(criterias.getColumnDefs().get(0).isSearchable()).isFalse();
        assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();
        
        assertThat(criterias.getSortedColumnDefs()).isEmpty();
	}

	@Test
	public void should_return_criterias_when_sorting_disabled_and_filtering_disabled() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");
		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[1][data]", "prop2");

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getStart()).isEqualTo(0);
		assertThat(criterias.getLength()).isEqualTo(10);
		assertThat(criterias.getDraw()).isEqualTo(1);

		assertThat(criterias.getColumnDefs()).hasSize(2);
		assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getColumnDefs().get(0).isSortable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isSearchable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();

		assertThat(criterias.getColumnDefs().get(1).getName()).isEqualTo("prop2");
		assertThat(criterias.getColumnDefs().get(1).isSortable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isSearchable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();

		assertThat(criterias.getSortedColumnDefs()).isEmpty();
	}

	@Test
	public void should_return_criterias_when_sorting_enabled_and_filtering_disabled() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");
		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][orderable]", "true");
		request.addParameter("order[0][column]", "0");
		request.addParameter("order[0][dir]", "asc");

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias).isNotNull();
		assertThat(criterias.getSearch()).isNull();
		assertThat(criterias.getStart()).isEqualTo(0);
		assertThat(criterias.getLength()).isEqualTo(10);
		assertThat(criterias.getDraw()).isEqualTo(1);

		assertThat(criterias.getColumnDefs()).hasSize(2);
		assertThat(criterias.getColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getColumnDefs().get(0).isSortable()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).isSorted()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).isSearchable()).isFalse();
		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isFalse();

		assertThat(criterias.getColumnDefs().get(1).getName()).isEqualTo("prop2");
		assertThat(criterias.getColumnDefs().get(1).isSortable()).isTrue();
		assertThat(criterias.getColumnDefs().get(1).isSorted()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isSearchable()).isFalse();
		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();

		assertThat(criterias.getSortedColumnDefs()).hasSize(1);
		assertThat(criterias.getSortedColumnDefs().get(0).getName()).isEqualTo("prop1");
		assertThat(criterias.getSortedColumnDefs().get(0).isSortable()).isTrue();
		assertThat(criterias.getSortedColumnDefs().get(0).isSorted()).isTrue();
		assertThat(criterias.getSortedColumnDefs().get(0).isSearchable()).isFalse();
		assertThat(criterias.getSortedColumnDefs().get(0).isFiltered()).isFalse();
	}

	@Test
	public void should_have_one_filterable_column() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		request.addParameter("order[0][column]", "0");
		request.addParameter("order[0][dir]", "asc");

		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.hasOneSearchableColumn()).isTrue();
	}

	@Test
	public void should_have_one_filtered_column() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "search");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		request.addParameter("order[0][column]", "0");
		request.addParameter("order[0][dir]", "asc");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.hasOneFilteredColumn()).isTrue();

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isEqualTo("search");
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isNull();

		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();
	}

	@Test
	public void should_have_multiple_filtered_columns() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");
		
		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "search-term-column-1");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		request.addParameter("columns[1][search][value]", "search-term-column-2");
		
		request.addParameter("columns[2][data]", "prop3");
		request.addParameter("columns[2][searchable]", "true");
		request.addParameter("columns[2][orderable]", "true");
		
		request.addParameter("order[0][column]", "0");
		request.addParameter("order[0][dir]", "asc");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.hasOneFilteredColumn()).isTrue();

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isEqualTo("search-term-column-1");
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isNull();

		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(1).getSearch()).isEqualTo("search-term-column-2");
		assertThat(criterias.getColumnDefs().get(1).getSearchFrom()).isNull();
		assertThat(criterias.getColumnDefs().get(1).getSearchTo()).isNull();

		assertThat(criterias.getColumnDefs().get(2).isFiltered()).isFalse();
	}

	@Test
	public void should_split_search_terms_when_using_from_only() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "left~");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isEqualTo("left");
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isNull();
	}

	@Test
	public void should_split_search_terms_when_using_to_only() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "~right");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isEqualTo("right");

		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();
	}

	@Test
	public void should_split_search_terms_when_using_from_and_to() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "left~right");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isEqualTo("left");
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isEqualTo("right");

		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();
	}

	@Test
	public void should_have_an_empty_search_field_when_range_is_empty() {

		request.addParameter(DTConstants.DT_I_DRAW, "1");
		request.addParameter(DTConstants.DT_I_START, "0");
		request.addParameter(DTConstants.DT_I_LENGTH, "10");

		request.addParameter("columns[0][data]", "prop1");
		request.addParameter("columns[0][searchable]", "true");
		request.addParameter("columns[0][orderable]", "true");
		request.addParameter("columns[0][search][value]", "~");

		request.addParameter("columns[1][data]", "prop2");
		request.addParameter("columns[1][searchable]", "true");
		request.addParameter("columns[1][orderable]", "true");
		
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);

		assertThat(criterias.getColumnDefs().get(0).isFiltered()).isTrue();
		assertThat(criterias.getColumnDefs().get(0).getSearch()).isEmpty();
		assertThat(criterias.getColumnDefs().get(0).getSearchFrom()).isNull();
		assertThat(criterias.getColumnDefs().get(0).getSearchTo()).isNull();

		assertThat(criterias.getColumnDefs().get(1).isFiltered()).isFalse();
	}
}