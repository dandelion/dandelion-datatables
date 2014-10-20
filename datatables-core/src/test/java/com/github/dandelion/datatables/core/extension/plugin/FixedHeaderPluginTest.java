package com.github.dandelion.datatables.core.extension.plugin;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static com.github.dandelion.core.asset.generator.js.jquery.JQueryContentPlaceholder.BEFORE_END_DOCUMENT_READY;

import static org.assertj.core.api.Assertions.assertThat;

public class FixedHeaderPluginTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_default_configuration() {
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"top\":true});");
	}

	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_top() {
		DatatableOptions.PLUGIN_FIXEDPOSITION.setIn(table.getTableConfiguration(), "top");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"top\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_bottom() {
		DatatableOptions.PLUGIN_FIXEDPOSITION.setIn(table.getTableConfiguration(), "bottom");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"bottom\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_right() {
		DatatableOptions.PLUGIN_FIXEDPOSITION.setIn(table.getTableConfiguration(), "right");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"right\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_RIGHT() {
		DatatableOptions.PLUGIN_FIXEDPOSITION.setIn(table.getTableConfiguration(), "RIGHT");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"right\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_fixedPosition_to_left() {
		DatatableOptions.PLUGIN_FIXEDPOSITION.setIn(table.getTableConfiguration(), "left");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"left\":true});");
	}
	
	@Test
	public void shoud_load_the_extension_with_offsetTop() {
		DatatableOptions.PLUGIN_FIXEDOFFSETTOP.setIn(table.getTableConfiguration(), 30);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new FixedHeaderPlugin())));

		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(1);
		assertThat(datatableContent.getPlaceholderContent().get(BEFORE_END_DOCUMENT_READY).toString()).contains(
				"new FixedHeader(oTable_" + table.getId() + ",{\"offsetTop\":30,\"top\":true});");
	}
}