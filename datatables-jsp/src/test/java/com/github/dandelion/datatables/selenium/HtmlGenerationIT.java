package com.github.dandelion.datatables.selenium;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;


/**
 * Test the HTML markup generation.
 *
 * @author Thibault Duchateau
 */
public class HtmlGenerationIT extends DomBaseIT {

	@Test
	public void should_generate_table_markup() throws IOException, Exception {
		goTo("/htmlGeneration/table_default.jsp");
		
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
	}

	@Test
	public void should_generate_script_tag() {
		goTo("/htmlGeneration/table_default.jsp");
		FluentWebElement body = findFirst("body");
		assertThat(body.find("script")).hasSize(1);
	}
	
//	@Test
	public void when_emptylist_should_not_generate_anything() {
		goTo("/htmlGeneration/table_with_empty_collection.jsp");
		System.out.println(driver.getPageSource());
		assertThat(getTable()).hasSize(0);
		System.out.println(driver.getPageSource());
	}
	
//	@Test
	public void when_nulllist_should_not_generate_anything() {
		goTo("/htmlGeneration/table_with_null_collection.jsp");
		System.out.println(driver.getPageSource());
		assertThat(getTable()).hasSize(0);
		System.out.println(driver.getPageSource());
	}
}
