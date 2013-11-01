package com.github.dandelion.datatables.core.extension.plugin;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

public class FixedHeaderPluginTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_default_configuration() throws ExtensionLoadingException {
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"top\":true});");
	}

	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_top() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedPosition("top");
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"top\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_bottom() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedPosition("bottom");
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"bottom\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_right() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedPosition("right");
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"right\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_RIGHT() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedPosition("RIGHT");
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"right\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_left() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedPosition("left");
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"left\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_offsetTop() throws ExtensionLoadingException {
		
		table.getTableConfiguration().setPluginFixedOffsetTop(30);
		extensionLoader.load(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetsRequestContext.get(table.getTableConfiguration().getRequest()).getScopes(true)).hasSize(1);
		assertThat(mainJsFile.getBeforeEndDocumentReady().toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"offsetTop\":30,\"top\":true});");
	}
}