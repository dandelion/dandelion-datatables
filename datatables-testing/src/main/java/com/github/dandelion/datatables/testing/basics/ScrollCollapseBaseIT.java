package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;
import com.github.dandelion.datatables.testing.utils.Constants;

public class ScrollCollapseBaseIT extends BaseIT {

	@Test
	public void should_disable_scroll_collapse() {
		goToPage("basics/disable_scroll_collapse");

		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find(".dataTables_scrollBody").getAttribute("style")).contains("height: 1000px");
	}
	
	@Test
	public void should_enable_scroll_collapse() {
		goToPage("basics/enable_scroll_collapse");

		assertThat(find("#" + Constants.TABLE_ID + "_wrapper").find(".dataTables_scrollBody").getAttribute("style"))//
			.matches(".*height: [0-9]*px.*")//
			.doesNotContain("height: 1000px");
	}
}
