package com.github.dandelion.datatables.integration.ajax;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dandelion.datatables.integration.JspContextRunner;
import com.github.dandelion.datatables.testing.BaseIT;
import com.github.dandelion.datatables.testing.utils.JspTest;

/**
 * Test the HTML markup generation using an AJAX source and other features in
 * the same table.
 * 
 * @author Thibault Duchateau
 */
@RunWith(JspContextRunner.class)
@JspTest
public class MultipleFeaturesIT extends BaseIT {

	@Test
	public void should_generate_table_with_ajax_source_and_callback() throws Exception {
		goToPage("ajax/table_with_callback");

		String js = getConfigurationFromPage("ajax/table_with_callback").getContent();

		assertThat(js).contains("\"fnInitComplete\":function(oSettings,json){oTable_myTableId.fnAdjustColumnSizing(true);myInitCallback(oSettings,json);},");
	}
}
