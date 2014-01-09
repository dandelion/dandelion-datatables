package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class ServerSideFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_the_given_ajax_source() throws ExtensionLoadingException {
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new ServerSideFeature())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(0);
		assertThat(mainConfig.get(CallbackType.INIT.getName()).toString())
		.isEqualTo(new JavascriptFunction("oTable_fakeId.fnAdjustColumnSizing(true);", CallbackType.INIT.getArgs()).toString());
	}
}
