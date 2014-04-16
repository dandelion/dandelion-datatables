package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

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
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;

public class StringProcessorTest {

	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected HttpServletRequest request;
	protected Map<ConfigToken<?>, Object> confToBeApplied;

	@Before
	public void setup() {
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));
		confToBeApplied = new HashMap<ConfigToken<?>, Object>();
		tableConfiguration = new TableConfiguration(confToBeApplied, request);
		columnConfiguration = new ColumnConfiguration();
	}

	@Test
	public void should_update_the_entry_with_null_when_using_an_empty_string() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "");
		ConfigurationProcessor processor = new StringProcessor();
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isNull();
	}
	
	@Test
	public void should_update_the_entry_with_the_same_string() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "someString");
		ConfigurationProcessor processor = new StringProcessor();
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("someString");
	}
	
	@Test
	public void should_update_the_table_entry_with_the_same_string_and_update_active_bundles() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "bundleToAdd#someString");
		ConfigurationProcessor processor = new StringProcessor(true);
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundleToAdd");
	}
	
	@Test
	public void should_update_the_column_entry_with_the_same_string_and_update_active_bundles_with_one_bundle() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "bundleToAdd#someString");
		ConfigurationProcessor processor = new StringProcessor(true);
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundleToAdd");
	}
	
	@Test
	public void should_update_the_column_entry_with_the_same_string_and_update_active_bundles_with_multiple_bundles() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, " bundle1,bundle2#someString");
		ConfigurationProcessor processor = new StringProcessor(true);
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("someString");
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundle1", "bundle2");
	}
	
	@Test(expected = ConfigurationProcessingException.class)
	public void should_throw_an_exception_when_using_a_wrong_format() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "bundleToAdd#");
		ConfigurationProcessor processor = new StringProcessor(true);
		processor.process(entry, tableConfiguration);
	}
	
	@Test(expected = ConfigurationProcessingException.class)
	public void should_throw_an_exception_when_using_a_wrong_format2() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_SERVERDATA, "#someString");
		ConfigurationProcessor processor = new StringProcessor(true);
		processor.process(entry, tableConfiguration);
	}
}