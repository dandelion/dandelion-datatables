package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.jsp.ITDomPhantomJs;

/**
 * Test the basic Features of Dandelion-Datatables.
 *
 * @author Thibault Duchateau
 */
public class ITHandlingNullValues extends ITDomPhantomJs {

	@Test
	public void should_render_empty_cell() throws IOException, Exception {
		goTo("/htmlGeneration/table_default.jsp");

		// I know that the 4th cell of the first row must be empty (City is null in the data source)
		assertThat(getTable().find("tbody").findFirst("tr").find("td", 3).getText()).isEqualTo("");
	}
	
	@Test
	public void should_render_default_value_in_cell() throws IOException, Exception {
		goTo("/table_default_values.jsp");

		// I know that the 4th cell of the first row must be empty (City is null in the data source)
		assertThat(getTable().find("tbody").findFirst("tr").find("td", 3).getText()).isEqualTo("default value");
	}
}
