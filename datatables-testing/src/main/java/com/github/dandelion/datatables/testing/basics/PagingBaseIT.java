package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class PagingBaseIT extends BaseIT {

	@Test
	public void should_limit_display_length() throws IOException, Exception {
        goToPage("basics/display_length");

		assertThat(getTable().find("tbody").find("tr")).hasSize(40);
	}
}
