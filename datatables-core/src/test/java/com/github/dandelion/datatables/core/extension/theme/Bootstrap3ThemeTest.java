package com.github.dandelion.datatables.core.extension.theme;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class Bootstrap3ThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_default_configuration() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap3Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(1);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")));
	}

	@Test
	public void shoud_load_the_extension_with_a_custom_domfeature() {
		TableConfig.FEATURE_DOM.setIn(table.getTableConfiguration(), "lft");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap3Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(1);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")));
	}
	
	@Test
	public void shoud_load_the_extension_with_paging_enabled() {
		TableConfig.FEATURE_PAGEABLE.setIn(table.getTableConfiguration(), true);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap3Theme())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).hasSize(2);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")),
				entry(DTConstants.DT_PAGINATION_TYPE, "bootstrap"));
	}
}
