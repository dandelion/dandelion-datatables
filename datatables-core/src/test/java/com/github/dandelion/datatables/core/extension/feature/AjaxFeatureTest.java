package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class AjaxFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_the_given_ajax_source() {
		DatatableOptions.AJAX_SOURCE.setIn(table.getTableConfiguration(), "/ajaxSource");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new AjaxFeature())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(0);
		assertThat(mainConfig).contains(
				entry(DTConstants.DT_B_DEFER_RENDER, true),
				entry(DTConstants.DT_S_AJAXDATAPROP, ""),
				entry(DTConstants.DT_S_AJAX_SOURCE, "/ajaxSource"));
		assertThat(mainConfig.get(CallbackType.INIT.getName()).toString())
		.isEqualTo(new JavascriptFunction("oTable_fakeId.fnAdjustColumnSizing(true);", CallbackType.INIT.getArgs()).toString());
	}
}
