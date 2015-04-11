package com.github.dandelion.datatables.core.extension.theme;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class Bootstrap2ThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_default_configuration() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(1);
		assertThat(mainConfig).contains(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JsSnippet("[]")));
	}

	@Test
	public void shoud_load_the_extension_with_a_custom_domfeature() {
		DatatableOptions.FEATURE_DOM.setIn(table.getTableConfiguration(), "lft");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(1);
		assertThat(mainConfig).contains(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JsSnippet("[]")));
	}
	
	@Test
	public void shoud_load_the_extension_with_paging_enabled() {
		DatatableOptions.FEATURE_PAGEABLE.setIn(table.getTableConfiguration(), true);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(2);
		assertThat(mainConfig).hasSize(2);
		assertThat(mainConfig).contains(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JsSnippet("[]")),
				entry(DTConstants.DT_PAGINGTYPE, "bootstrap_simple"));
	}
}
