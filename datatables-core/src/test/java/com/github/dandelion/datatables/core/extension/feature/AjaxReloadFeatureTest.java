package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class AjaxReloadFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_add_a_bundle_and_append_a_default_function_to_BeforeEndDocumentReady() {
		
		AjaxReloadFeature ajaxReloadFeature = new AjaxReloadFeature();
		String mySelector = "#myReloadSelector";
		StringBuilder js = new StringBuilder();
		js.append("$('").append(mySelector).append("').bind('click', function() {");
		js.append("oTable_").append(table.getId()).append(".ajax.reload();");
		js.append("});");
		DatatableOptions.AJAX_RELOAD_SELECTOR.setIn(table.getTableConfiguration().getOptions(), mySelector);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(ajaxReloadFeature)));

		assertThat(ajaxReloadFeature.getBeforeEndDocumentReady().toString()).isEqualTo(js.toString());
	}
	
	@Test
	public void shoud_add_a_bundle_and_append_a_custom_function_to_BeforeEndDocumentReady() {
		AjaxReloadFeature ajaxReloadFeature = new AjaxReloadFeature();
		String mySelector = "#myReloadSelector";
		String myFunction = "myFunction";
		StringBuilder js = new StringBuilder();
		js.append("$('").append(mySelector).append("').bind('click', function() {");
		js.append(myFunction).append("();");
		js.append("});");
		DatatableOptions.AJAX_RELOAD_SELECTOR.setIn(table.getTableConfiguration().getOptions(), mySelector);
		DatatableOptions.AJAX_RELOAD_FUNCTION.setIn(table.getTableConfiguration().getOptions(), myFunction);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(ajaxReloadFeature)));

		assertThat(ajaxReloadFeature.getBeforeEndDocumentReady().toString()).isEqualTo(js.toString());
	}
}
