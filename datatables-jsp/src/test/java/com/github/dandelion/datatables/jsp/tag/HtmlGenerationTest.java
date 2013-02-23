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
		goTo("/table_default.jsp");
		assertThat(getTable()).hasSize(1);
		assertThat(getTable().find("thead")).hasSize(1);
		assertThat(getTable().find("tbody")).hasSize(1);
		assertThat(getTable().find("tbody").find("tr")).hasSize(2);
	}

	@Test
	public void should_generate_script_tag() {
		goTo("/table_default.jsp");
		FluentWebElement body = findFirst("body");
		assertThat(body.find("script")).hasSize(1);
	}
}
