package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class AjaxFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_the_given_ajax_source() {
		TableConfig.AJAX_SOURCE.setIn(table.getTableConfiguration(), "/ajaxSource");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new AjaxFeature())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(0);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_B_DEFER_RENDER, true),
				entry(DTConstants.DT_S_AJAXDATAPROP, ""),
				entry(DTConstants.DT_S_AJAX_SOURCE, "/ajaxSource"));
		assertThat(mainConfig.get(CallbackType.INIT.getName()).toString())
		.isEqualTo(new JavascriptFunction("oTable_fakeId.fnAdjustColumnSizing(true);", CallbackType.INIT.getArgs()).toString());
	}
}
