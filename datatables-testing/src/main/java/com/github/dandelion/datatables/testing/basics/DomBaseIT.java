package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class DomBaseIT extends BaseIT {

	@Test
	public void should_apply_css_using_dom() throws IOException, Exception {
		goToPage("basics/dom_dom");

		assertThat(find("div.dataTables_wrapper").find("div.dataTables_filter")).hasSize(0);
	}
}