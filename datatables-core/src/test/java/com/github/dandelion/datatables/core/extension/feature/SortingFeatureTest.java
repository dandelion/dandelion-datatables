package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class SortingFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_alt_string() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.ALT_STRING.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_ALT_STRING));
	}
	
	@Test
	public void shoud_load_the_extension_with_anti_the() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.ANTI_THE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_ANTI_THE));
	}
	
	@Test
	public void shoud_load_the_extension_with_currency() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.CURRENCY.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_CURRENCY));
	}
	
	@Test
	public void shoud_load_the_extension_with_date() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.DATE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_DATE_UK));
	}
	
	@Test
	public void shoud_load_the_extension_with_filesize() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.FILESIZE.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_FILESIZE));
	}
	
	@Test
	public void shoud_load_the_extension_with_formatted_numbers() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.FORMATTED_NUMBERS.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_FORMATTED_NUMBER));
	}
	
	@Test
	public void shoud_load_the_extension_with_natural() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, SortType.NATURAL.getName());
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_NATURAL));
	}
	
	@Test
	public void shoud_load_the_extension_with_a_custom_sortType() {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().set(ColumnConfig.SORTTYPE, "custom-sort-type");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(0);
	}
}