/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

package com.github.dandelion.datatables.integration.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the filtering options.
 *
 * @author Thibault Duchateau
 */
public class FilteringIT extends DomBaseIT {

	@Test
	public void should_generate_input_field_in_the_footer() throws IOException, Exception {
		goTo("/basics/filtering_with_input.jsp");

		// A tfoot tag must be generated
		assertThat(getTable().find("tfoot")).hasSize(1);
		
		// The column flagged as filterable as a default input field
		assertThat(getTable().find("tfoot").find("input")).hasSize(2);
		
		// All other footer cells must have the same content as the header ones
		assertThat(getTable().find("thead").find("th", 0).getValue()).isEqualTo(getTable().find("tfoot").find("th", 0).getValue());
		assertThat(getTable().find("thead").find("th", 2).getValue()).isEqualTo(getTable().find("tfoot").find("th", 2).getValue());
		assertThat(getTable().find("thead").find("th", 3).getValue()).isEqualTo(getTable().find("tfoot").find("th", 3).getValue());
		assertThat(getTable().find("thead").find("th", 4).getValue()).isEqualTo(getTable().find("tfoot").find("th", 4).getValue());
	}
	
	@Test
	public void should_filter_data_when_using_an_input_field() throws IOException, Exception {
		goTo("/basics/filtering_with_input.jsp");
		
		// Now we test the input that filters data
		fill(getTable().find("tfoot").find("input", 0)).with("vanna");
		
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 0).getText()).isEqualTo("2");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 1).getText()).isEqualTo("Vanna");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 2).getText()).isEqualTo("Salas");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 4).getText()).isEqualTo("bibendum.fermentum.metus@ante.ca");
	}
	
	@Test
	public void should_generate_select_in_the_footer() throws IOException, Exception {
		goTo("/basics/filtering_with_select.jsp");
		
		// A tfoot tag must be generated
		assertThat(getTable().find("tfoot")).hasSize(1);
				
		// Now we test that drop down lists exist in the footer
		assertThat(getTable().find("tfoot").find("span")).hasSize(2);
		assertThat(getTable().find("tfoot").find("select")).hasSize(2);
		
		// All drop down list must contain as many options than the data source, minus duplicates and null values
		assertThat(getTable().find("tfoot").find("select", 0).find("option")).hasSize(688);
		assertThat(getTable().find("tfoot").find("select", 1).find("option")).hasSize(649);
		
		// All other footer cells must have the same content as the header ones
		assertThat(getTable().find("thead").find("th", 0).getValue()).isEqualTo(getTable().find("tfoot").find("th", 0).getValue());
		assertThat(getTable().find("thead").find("th", 3).getValue()).isEqualTo(getTable().find("tfoot").find("th", 3).getValue());
		assertThat(getTable().find("thead").find("th", 4).getValue()).isEqualTo(getTable().find("tfoot").find("th", 4).getValue());
	}
	
//	@Test
	public void should_filter_data_when_using_a_drop_down_list() throws IOException, Exception {
		goTo("/basics/filtering_with_select.jsp");
		
		fill(getTable().find("tfoot").find("select", 0)).with("Aaron");
	}
	
	@Test
	public void should_filter_data_when_using_an_extra_form() throws IOException, Exception {
		goTo("/basics/filtering_with_extra_form.jsp");
		
		fill(find("#cityFilter").findFirst("input")).with("North Las Vegas");
		
		assertThat(getTable().find("tbody").find("tr")).hasSize(2);
	}
}
