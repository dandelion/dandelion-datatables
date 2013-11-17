package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class PipeliningFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_the_given_ajax_source() throws ExtensionLoadingException {
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new PipeliningFeature())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainConfig).includes(entry(DTConstants.DT_FN_SERVERDATA, new JavascriptSnippet("fnDataTablesPipeline")));
		// TODO test the pipe size
	}
	
	@Test
	public void shoud_load_the_extension_with_a_custom_pipesize() throws ExtensionLoadingException {
		
		// TODO test a custom pipe size
	}
}
