package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.jsp.DomPhantomJsTest;

public class HtmlGenerationTest extends DomPhantomJsTest {

	@Test
    public void should_generate_table_markup() throws InterruptedException {
        goTo("/table_default.jsp");
        
        assertThat(text("table")).hasSize(1);
        assertThat(text("script")).hasSize(3);

        // DataTables initialization
        executeScript("$('#myTableId').dataTable();");

        // Dandelion-datatables must generate a table element
        assertThat(getTable()).hasSize(1);
        
        // 2 persons to display => 2 row in the tbody
        assertThat(getTable().find("tbody").find("tr")).hasSize(2);

        System.out.println(driver.getPageSource());
    }
	
	@Test
    public void should_generate_script_tag() {
        goTo("/table_default.jsp");
        assertThat(text("script")).hasSize(3);
    }
	
//	@Test
//    public void should_generate_empty_table() {
//        goTo("/empty_table_default.jsp");
//        assertThat(text("table")).hasSize(1);
//        assertThat(text("script")).hasSize(1);
////        executeScript("alert('toto');");
//        System.out.println(driver.getPageSource());
//    }
}
