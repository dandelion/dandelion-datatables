package com.github.dandelion.datatables.core.extension.plugin;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class ColReorderPluginTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_default_configuration() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new ColReorderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainConfig).includes(entry(DTConstants.DT_DOM, "R"));
	}

	@Test
	public void shoud_load_the_extension_with_already_configured_dom() {
		table.getTableConfiguration().setFeatureDom("lfr");
		mainConfig.put(DTConstants.DT_DOM, "lfr");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new ColReorderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainConfig).includes(entry(DTConstants.DT_DOM, "Rlfr"));
	}
}