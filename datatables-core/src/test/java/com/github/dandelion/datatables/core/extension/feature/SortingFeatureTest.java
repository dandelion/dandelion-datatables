package com.github.dandelion.datatables.core.extension.feature;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class SortingFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_alt_string() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.ALT_STRING);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_ALT_STRING));
	}
	
	@Test
	public void shoud_load_the_extension_with_anti_the() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.ANTI_THE);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_ANTI_THE));
	}
	
	@Test
	public void shoud_load_the_extension_with_currency() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.CURRENCY);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_CURRENCY));
	}
	
	@Test
	public void shoud_load_the_extension_with_date() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.DATE);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_DATE_UK));
	}
	
	@Test
	public void shoud_load_the_extension_with_filesize() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.FILESIZE);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_FILESIZE));
	}
	
	@Test
	public void shoud_load_the_extension_with_formatted_numbers() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.FORMATTED_NUMBERS);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_FORMATTED_NUMBER));
	}
	
	@Test
	public void shoud_load_the_extension_with_natural() throws ExtensionLoadingException {
		table.getLastHeaderRow().getColumns().get(0).getColumnConfiguration().setSortType(SortType.NATURAL);
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new SortingFeature())));

		List<String> scopes = Arrays.asList(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true));
		assertThat(scopes).hasSize(1);
		assertThat(scopes.contains(Scope.DDL_DT_SORTING_NATURAL));
	}
}