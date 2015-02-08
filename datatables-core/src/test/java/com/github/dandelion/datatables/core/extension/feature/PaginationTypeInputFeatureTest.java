package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.generator.DTConstants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class PaginationTypeInputFeatureTest  extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension() {
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new PagingTypeInputFeature())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(mainConfig).contains(entry(DTConstants.DT_PAGINGTYPE, "input"));
	}
}
