package com.github.dandelion.datatables.core.extension.theme;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;

public class Bootstrap2ThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() throws ExtensionLoadingException {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(2);
		assertThat(mainConfig).hasSize(3);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString()),
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")),
				entry(DTConstants.DT_DOM, "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>"));
	}

	@Test
	public void shoud_load_the_extension_with_tablecloth() throws ExtensionLoadingException {
		table.getTableConfiguration().setCssThemeOption(ThemeOption.TABLECLOTH);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(3);
		assertThat(mainConfig).hasSize(3);
		assertThat(mainConfig).includes(entry(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString()));
		assertThat(mainConfig).includes(entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")));
	}

	@Test(expected = ExtensionLoadingException.class)
	public void shoud_not_load_the_extension_with_wrong_option() throws ExtensionLoadingException {
		table.getTableConfiguration().setCssThemeOption(ThemeOption.CUPERTINO);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));
	}

	@Test
	public void shoud_load_the_extension_with_a_custom_domfeature() throws ExtensionLoadingException {
		table.getTableConfiguration().setFeatureDom("lft");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap2Theme())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(2);
		assertThat(mainConfig).hasSize(2);
		assertThat(mainConfig).includes(
				entry(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString()),
				entry(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")));
	}
}
