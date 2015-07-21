package com.github.dandelion.datatables.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.core.option.TableConfigurationFactory;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.StringProcessor;

import static org.assertj.core.api.Assertions.assertThat;

public class StringProcessorTest {

	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected HttpServletRequest request;
	protected Map<Option<?>, Object> confToBeApplied;

	@Before
	public void setup() {
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));
		confToBeApplied = new HashMap<Option<?>, Object>();
		tableConfiguration = TableConfigurationFactory.newInstance("tableId", request, null);
		columnConfiguration = new ColumnConfiguration();
	}

	@Test
	public void should_update_the_entry_with_null_when_using_an_empty_string() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "");
		OptionProcessor processor = new StringProcessor();
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration);
		processor.process(pc);
		assertThat(entry.getValue()).isNull();
	}
	
	@Test
	public void should_update_the_entry_with_the_same_string() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "someString");
		OptionProcessor processor = new StringProcessor();
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration);
		processor.process(pc);
		assertThat(entry.getValue()).isEqualTo("someString");
	}
	
	@Test
	public void should_update_the_table_entry_with_the_same_string_and_update_active_bundles() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "bundleToAdd#someString");
		OptionProcessor processor = new StringProcessor(true);
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration, true);
		processor.process(pc);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundleToAdd");
	}
	
	@Test
	public void should_update_the_column_entry_with_the_same_string_and_update_active_bundles_with_one_bundle() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "bundleToAdd#someString");
		OptionProcessor processor = new StringProcessor(true);
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration, true);
		processor.process(pc);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundleToAdd");
	}
	
	@Test
	public void should_update_the_column_entry_with_the_same_string_and_update_active_bundles_with_multiple_bundles() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, " bundle1,bundle2#someString");
		OptionProcessor processor = new StringProcessor(true);
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration, true);
		processor.process(pc);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundle1", "bundle2");
	}
	
	@Test(expected = DandelionException.class)
	public void should_throw_an_exception_when_using_a_wrong_format() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "bundleToAdd#");
		OptionProcessor processor = new StringProcessor(true);
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration, true);
		processor.process(pc);
	}
	
	@Test(expected = DandelionException.class)
	public void should_throw_an_exception_when_using_a_wrong_format2() throws Exception{
		Entry<Option<?>, Object> entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_SOURCE, "#someString");
		OptionProcessor processor = new StringProcessor(true);
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration, true);
		processor.process(pc);
	}
}