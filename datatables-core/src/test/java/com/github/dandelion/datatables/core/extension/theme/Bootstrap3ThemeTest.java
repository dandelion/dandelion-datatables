package com.github.dandelion.datatables.core.extension.theme;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class Bootstrap3ThemeTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() throws ExtensionLoadingException {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new Bootstrap3Theme())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(2);
		assertThat(mainConfig).hasSize(0);
	}
}
