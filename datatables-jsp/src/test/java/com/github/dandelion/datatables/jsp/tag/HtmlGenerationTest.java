package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;

import com.github.dandelion.datatables.jsp.DomPhantomJsTest;

/**
 * Test the HTML markup generation.
 *
 * @author Thibault Duchateau
 */
public class HtmlGenerationTest extends DomPhantomJsTest {

	@Test
	public void should_generate_table_markup() throws IOException, Exception {
		goTo("/htmlGeneration/table_default.jsp");
		assertThat(getTable()).hasSize(1);
		assertThat(getTable().find("thead")).hasSize(1);
		assertThat(getTable().find("tbody")).hasSize(1);
		
		// By default, paging is set to 10
		assertThat(getTable().find("tbody").find("tr")).hasSize(10);
	}

	@Test
	public void should_generate_script_tag() {
		goTo("/htmlGeneration/table_default.jsp");
		FluentWebElement body = findFirst("body");
		assertThat(body.find("script")).hasSize(1);
	}
	
	@Test
	public void when_emptylist_should_not_generate_anything() {
		goTo("/htmlGeneration/table_with_empty_collection.jsp");
		assertThat(getTable()).hasSize(0);
		System.out.println(driver.getPageSource());
	}
	
	@Test
	public void when_nulllist_should_not_generate_anything() {
		goTo("/htmlGeneration/table_with_null_collection.jsp");
		assertThat(getTable()).hasSize(0);
		System.out.println(driver.getPageSource());
	}
}
