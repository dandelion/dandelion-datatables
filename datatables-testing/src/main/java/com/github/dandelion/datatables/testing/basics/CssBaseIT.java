package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class CssBaseIT extends BaseIT {

	@Test
	public void should_apply_css_stripe_classes_using_dom() throws IOException, Exception {
		goToPage("basics/css_stripe_classes");

		assertThat(getTable().find("tbody").find("tr", 0).getAttribute("class")).isEqualTo("class1");
		assertThat(getTable().find("tbody").find("tr", 1).getAttribute("class")).isEqualTo("class2");
		assertThat(getTable().find("tbody").find("tr", 2).getAttribute("class")).isEqualTo("class1");
		assertThat(getTable().find("tbody").find("tr", 3).getAttribute("class")).isEqualTo("class2");
	}
}
