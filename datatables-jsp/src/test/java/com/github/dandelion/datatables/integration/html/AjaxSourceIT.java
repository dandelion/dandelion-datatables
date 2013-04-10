package com.github.dandelion.datatables.integration.html;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the HTML markup generation using an AJAX source.
 *
 * @author Thibault Duchateau
 */
public class AjaxSourceIT extends DomBaseIT {

	@Test
	public void should_generate_table_markup() throws IOException, Exception {
		goTo("/ajax/table.jsp");

		assertThat(getTable()).hasSize(1);
		assertThat(getTable().find("thead")).hasSize(1);
		assertThat(getTable().find("tbody")).hasSize(1);
		
		// By default, paging is set to 10
		assertThat(getTable().find("tbody").find("tr")).hasSize(10);
		
		// Let's look at the cells in the second tr
		assertThat(getTable().find("tbody").find("tr", 1).find("td", 0).getText()).isEqualTo("2");
		assertThat(getTable().find("tbody").find("tr", 1).find("td", 1).getText()).isEqualTo("Vanna");
		assertThat(getTable().find("tbody").find("tr", 1).find("td", 2).getText()).isEqualTo("Salas");
		assertThat(getTable().find("tbody").find("tr", 1).find("td", 3).getText()).isEqualTo("Denny");
		assertThat(getTable().find("tbody").find("tr", 1).find("td", 4).getText()).isEqualTo("bibendum.fermentum.metus@ante.ca");
		
		// A script tag must be generated
		assertThat(getHtmlBody().find("script")).hasSize(1);
	}
	

	@Test
	public void should_render_empty_cell() throws IOException, Exception {
		goTo("/ajax/table.jsp");

		// I know that the 4th cell of the first row must be empty (City is null in the data source)
		assertThat(getTable().find("tbody").findFirst("tr").find("td", 3).getText()).isEqualTo("");
	}
	
	@Test
	public void should_render_default_value_in_cell() throws IOException, Exception {
		goTo("/ajax/table_default_values.jsp");

		// I know that the 4th cell of the first row must be empty (City is null in the data source)
		assertThat(getTable().find("tbody").findFirst("tr").find("td", 3).getText()).isEqualTo("default value");
	}
}
