package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.testing.BaseIT;
import com.github.dandelion.datatables.testing.utils.Constants;

public abstract class FeatureEnablementBaseIT extends BaseIT {

	@Test
	public void should_disable_paging() throws Exception {
		goToPage("basics/disable_paging");

		System.out.println("currentUrl = " + driver.getCurrentUrl());
		System.out.println(driver.getPageSource());
		assertThat(find("#" + Constants.TABLE_ID + "_length")).hasSize(0);

		// If paging is disabled, the entire collection is displayed
		assertThat(getTable().find("tbody").find("tr")).hasSize(Mock.persons.size());
	}
	
	@Test
	public void should_disable_filtering() throws Exception {
		goToPage("basics/disable_filtering");

		// If paging is disabled, the entire collection is displayed
		assertThat(find("#" + Constants.TABLE_ID + "_filter")).hasSize(0);
	}

	@Test
	public void should_disable_info() throws Exception {
		goToPage("basics/disable_info");

		// If paging is disabled, the entire collection is displayed
		assertThat(find("#" + Constants.TABLE_ID + "_info")).hasSize(0);
	}

	@Test
	public void should_disable_sorting() throws Exception {
		goToPage("basics/disable_sorting");

		// If paging is disabled, the entire collection is displayed
		assertThat(getTable().find("tbody").find(".sorting")).hasSize(0);
		assertThat(getTable().find("tbody").find(".sorting_desc")).hasSize(0);
		assertThat(getTable().find("tbody").find(".sorting_asc")).hasSize(0);
	}
}