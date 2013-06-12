package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class VisibleBaseIT extends BaseIT {

	@Test
	public void should_hide_the_last_column_using_dom() {
		goToPage("basics/visible_dom");
		
		assertThat(getTable().find("thead").find("th")).hasSize(4);
		assertThat(getTable().find("tbody").findFirst("tr").find("td")).hasSize(4);
	}
	
	@Test
	public void should_hide_the_last_column_using_ajax() {
		goToPage("basics/visible_ajax");
		
		assertThat(getTable().find("thead").find("th")).hasSize(4);
		assertThat(getTable().find("tbody").findFirst("tr").find("td")).hasSize(4);
	}
}
