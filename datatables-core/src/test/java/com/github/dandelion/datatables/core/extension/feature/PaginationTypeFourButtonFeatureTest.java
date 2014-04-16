package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class PaginationTypeFourButtonFeatureTest  extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() {
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new PaginationTypeFourButtonFeature())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).includes(entry(DTConstants.DT_PAGINATION_TYPE, "four_button"));
	}
}
