package com.github.dandelion.datatables.core.extension.feature;

import static com.github.dandelion.datatables.core.util.JavascriptUtils.INDENT;
import static com.github.dandelion.datatables.core.util.JavascriptUtils.NEWLINE;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class AjaxReloadFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_add_a_scope_and_append_a_default_function_to_BeforeEndDocumentReady() {
		
		AjaxReloadFeature ajaxReloadFeature = new AjaxReloadFeature();
		String mySelector = "#myReloadSelector";
		StringBuilder js = new StringBuilder(NEWLINE);
		js.append("$('").append(mySelector).append("').bind('click', function() {").append(NEWLINE).append(INDENT).append(INDENT);
		js.append("oTable_").append(table.getId()).append(".fnReloadAjax();").append(NEWLINE).append(INDENT);
		js.append("});").append(NEWLINE);
		TableConfig.AJAX_RELOAD_SELECTOR.setIn(table.getTableConfiguration(), mySelector);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(ajaxReloadFeature)));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).contains(
				Scope.DDL_DT_AJAX_RELOAD.getScopeName());
		assertThat(ajaxReloadFeature.getBeforeEndDocumentReady().toString()).isEqualTo(js.toString());
	}
	
	@Test
	public void shoud_add_a_scope_and_append_a_custom_function_to_BeforeEndDocumentReady() {
		AjaxReloadFeature ajaxReloadFeature = new AjaxReloadFeature();
		String mySelector = "#myReloadSelector";
		String myFunction = "myFunction";
		StringBuilder js = new StringBuilder(NEWLINE);
		js.append("$('").append(mySelector).append("').bind('click', function() {").append(NEWLINE).append(INDENT).append(INDENT);
		js.append(myFunction).append("();").append(NEWLINE).append(INDENT);
		js.append("});").append(NEWLINE);
		TableConfig.AJAX_RELOAD_SELECTOR.setIn(table.getTableConfiguration(), mySelector);
		TableConfig.AJAX_RELOAD_FUNCTION.setIn(table.getTableConfiguration(), myFunction);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(ajaxReloadFeature)));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).contains(
				Scope.DDL_DT_AJAX_RELOAD.getScopeName());
		assertThat(ajaxReloadFeature.getBeforeEndDocumentReady().toString()).isEqualTo(js.toString());
	}
}
