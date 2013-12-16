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

import org.junit.Test;

import com.github.dandelion.datatables.integration.ThymeleafBaseIT;

/**
 * Test the filtering options.
 *
 * @author Thibault Duchateau
 */
public class FilteringIT extends ThymeleafBaseIT {

	@Test
	public void should_filter_data_when_using_an_input_field() {
		goToPage("basics/filtering/filtering_with_input", true);
		
		// Now we test the input that filters data
		fill(getTable().find("tfoot").find("input", 0)).with("vanna");
		
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 0).getText()).isEqualTo("2");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 1).getText()).isEqualTo("Vanna");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 2).getText()).isEqualTo("Salas");
		assertThat(getTable().find("tbody").find("tr", 0).find("td", 4).getText()).isEqualTo("bibendum.fermentum.metus@ante.ca");
	}
	
	@Test
	public void should_generate_select_in_the_footer() {
		goToPage("basics/filtering/filtering_with_select", true);
		
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
	
	@Test
	public void should_filter_data_when_using_an_extra_form() {
		goToPage("basics/filtering/filtering_with_extra_form", true);
		
		assertThat(find("#firstNameFilter").find("span.filter_column")).hasSize(1);
		assertThat(find("#firstNameFilter").find("span.filter_column").find("select.dandelion_select_filter")).hasSize(1);
		assertThat(find("#lastNameFilter").find("span.filter_column")).hasSize(1);
		assertThat(find("#lastNameFilter").find("span.filter_column").find("select.dandelion_select_filter")).hasSize(1);
		assertThat(find("#cityFilter").find("span.filter_column")).hasSize(1);
		assertThat(find("#cityFilter").find("span.filter_column").find("select.dandelion_select_filter")).hasSize(1);
	}
	
	@Test
	public void should_generate_a_filtering_listbox_with_predefined_values(){
		goToPage("basics/filtering/filtering_with_select_with_predefined_values");
		
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select")).hasSize(1);
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select").find("option")).hasSize(4);
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select").find("option", 0).getText()).isEqualTo("LastName");
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select").find("option", 1).getText()).isEqualTo("val1");
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select").find("option", 2).getText()).isEqualTo("val2");
		assertThat(getTable().find("tfoot").find("tr", 0).find("th", 2).find("select").find("option", 3).getText()).isEqualTo("val3");
	}
}
