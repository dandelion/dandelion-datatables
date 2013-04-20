package com.github.dandelion.datatables.integration.ajax;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the HTML markup generation using an AJAX source and other features in
 * the same table.
 * 
 * @author Thibault Duchateau
 */
public class MultipleFeaturesIT extends DomBaseIT {

	@Test
	public void should_generate_table_with_ajax_source_and_callback() throws IOException, Exception {
		goTo("/ajax/table_with_callback.jsp");

		String js = getConfigurationFromPage("ajax/table_with_callback.jsp").getContent();
		
		assertThat(js).contains("\"fnInitComplete\":function(oSettings,json){oTable_myTableId.fnAdjustColumnSizing(true);myInitCallback(oSettings,json);},");
	}
}