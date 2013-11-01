package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

/**
 * 
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class FilteringPlaceholderBaseIT extends BaseIT {

	@Test
	public void should_not_generate_input_field_using_none() throws Exception {
		goToPage("basics/filtering/filtering_with_input_with_none");

		assertThat(getTable().find("tfoot")).hasSize(0);
		assertThat(getTable().find("thead").find("tr")).hasSize(1);
	}
	
	@Test
	public void should_generate_input_field_in_the_footer_using_default_placeholder() throws Exception {
		goToPage("basics/filtering/filtering_with_input_with_default_value");

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
	public void should_generate_input_field_in_the_footer() throws Exception {
		goToPage("basics/filtering/filtering_with_input_with_foot");

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
	public void should_generate_input_field_after_the_head() throws Exception {
		goToPage("basics/filtering/filtering_with_input_with_headafter");
		
		// A second row in the thead must be added
		assertThat(getTable().find("thead").find("tr")).hasSize(2);
		assertThat(getTable().find("thead").find("tr", 1).find("th")).hasSize(5);
		assertThat(getTable().find("tfoot")).hasSize(0);
		
		FluentWebElement firstHeadRow = getTable().find("thead").find("tr", 0);
		FluentWebElement secondHeadRow = getTable().find("thead").find("tr", 1);
		
		// All other footer cells must have the same content as the header ones
		assertThat(secondHeadRow.find("th", 0).getValue()).isEqualTo(firstHeadRow.find("th", 0).getValue());
		assertThat(secondHeadRow.find("th", 1).find("span")).hasSize(1);
		assertThat(secondHeadRow.find("th", 1).find("span").getAttribute("class")).contains("filter_column filter_text");
		assertThat(secondHeadRow.find("th", 2).getValue()).isEqualTo(firstHeadRow.find("th", 2).getValue());
		assertThat(secondHeadRow.find("th", 3).getValue()).isEqualTo(firstHeadRow.find("th", 3).getValue());
		assertThat(secondHeadRow.find("th", 4).getValue()).isEqualTo(firstHeadRow.find("th", 4).getValue());
	}
	
	@Test
	public void should_generate_input_field_before_the_head() throws Exception {
		goToPage("basics/filtering/filtering_with_input_with_headbefore");
		
		// A second row in the thead must be added
		assertThat(getTable().find("thead").find("tr")).hasSize(2);
		assertThat(getTable().find("thead").find("tr", 1).find("th")).hasSize(5);
		assertThat(getTable().find("tfoot")).hasSize(0);
		
		FluentWebElement firstHeadRow = getTable().find("thead").find("tr", 0);
		FluentWebElement secondHeadRow = getTable().find("thead").find("tr", 1);
		
		// All other footer cells must have the same content as the header ones
		assertThat(firstHeadRow.find("th", 0).getValue()).isEqualTo(secondHeadRow.find("th", 0).getValue());
		assertThat(firstHeadRow.find("th", 1).find("span")).hasSize(1);
		assertThat(firstHeadRow.find("th", 1).find("span").getAttribute("class")).contains("filter_column filter_text");
		assertThat(firstHeadRow.find("th", 2).getValue()).isEqualTo(secondHeadRow.find("th", 2).getValue());
		assertThat(firstHeadRow.find("th", 3).getValue()).isEqualTo(secondHeadRow.find("th", 3).getValue());
		assertThat(firstHeadRow.find("th", 4).getValue()).isEqualTo(secondHeadRow.find("th", 4).getValue());
	}
}