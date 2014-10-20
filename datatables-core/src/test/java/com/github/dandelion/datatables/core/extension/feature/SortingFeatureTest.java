package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class SortingFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_alt_string() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.ALT_STRING.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_ALT_STRING));
	}
	
	@Test
	public void shoud_load_the_extension_with_anti_the() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.ANTI_THE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_ANTI_THE));
	}
	
	@Test
	public void shoud_load_the_extension_with_currency() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.CURRENCY.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_CURRENCY));
	}
	
	@Test
	public void shoud_load_the_extension_with_date() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.DATE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_DATE_UK));
	}
	
	@Test
	public void shoud_load_the_extension_with_filesize() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.FILESIZE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_FILESIZE));
	}
	
	@Test
	public void shoud_load_the_extension_with_formatted_numbers() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.FORMATTED_NUMBERS.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_FORMATTED_NUMBER));
	}
	
	@Test
	public void shoud_load_the_extension_with_natural() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.NATURAL.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(1);
		assertThat(bundles.contains(DatatableBundles.DDL_DT_SORTING_NATURAL));
	}
	
	@Test
	public void shoud_load_the_extension_with_a_custom_sortType() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, "custom-sort-type");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> bundles = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true));
		assertThat(bundles).hasSize(0);
	}
}