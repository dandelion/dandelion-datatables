package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.jsp.DomPhantomJsTest;

public class HtmlGenerationTest extends DomPhantomJsTest {

	@Test
    public void should_generate_table() {
        goTo("/table_default.jsp");
        System.out.println(driver.getPageSource());
        assertThat(text("table")).hasSize(1);
        assertThat(text("script")).hasSize(1);
    }
	
	@Test
    public void should_generate_empty_table() {
        goTo("/empty_table_default.jsp");
        System.out.println(driver.getPageSource());
        assertThat(text("table")).hasSize(1);
        assertThat(text("script")).hasSize(1);
    }
}
