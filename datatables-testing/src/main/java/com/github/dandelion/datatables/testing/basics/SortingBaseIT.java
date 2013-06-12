package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class SortingBaseIT extends BaseIT {

	@Test
	public void should_disable_sorting_only_on_the_first_column_using_dom() throws IOException, Exception {
		goToPage("basics/sorting_disabled_dom");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_disabled");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 2).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 3).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 4).getAttribute("class")).isEqualTo("sorting");
	}
	
	@Test
	public void should_init_sort_using_dom() throws IOException, Exception {
		goToPage("basics/sorting_init_dom");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_control_direction_using_dom() throws IOException, Exception {
		goToPage("basics/sorting_direction_dom");
		
		click(getTable().find("thead").find("th", 0));
		click(getTable().find("thead").find("th", 0));
		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		
		click(getTable().find("thead").find("th", 1));
		click(getTable().find("thead").find("th", 1));
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_disable_sorting_only_on_the_first_column_using_ajax() throws IOException, Exception {
		goToPage("basics/sorting_disabled_ajax");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_disabled");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 2).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 3).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 4).getAttribute("class")).isEqualTo("sorting");
	}
	
	@Test
	public void should_init_sort_using_ajax() throws IOException, Exception {
		goToPage("basics/sorting_init_ajax");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_control_direction_using_ajax() throws IOException, Exception {
		goToPage("basics/sorting_direction_ajax");
		
		click(getTable().find("thead").find("th", 0));
		click(getTable().find("thead").find("th", 0));
		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		
		click(getTable().find("thead").find("th", 1));
		click(getTable().find("thead").find("th", 1));
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
}
