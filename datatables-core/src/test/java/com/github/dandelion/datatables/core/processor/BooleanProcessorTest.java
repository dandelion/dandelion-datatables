package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public class BooleanProcessorTest {

	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected HttpServletRequest request;
	protected Map<ConfigToken<?>, Object> confToBeApplied;

	@Before
	public void setup() {
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		confToBeApplied = new HashMap<ConfigToken<?>, Object>();
		tableConfiguration = new TableConfiguration(confToBeApplied, request);
		columnConfiguration = new ColumnConfiguration();
	}
	
	@Test
	public void should_update_the_table_entry_with_true() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "true");
		ConfigurationProcessor processor = new BooleanProcessor();
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(true);
	}
	
	@Test
	public void should_update_the_column_entry_with_true() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "true");
		ConfigurationProcessor processor = new BooleanProcessor();
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(true);
	}
	
	@Test
	public void should_update_the_table_entry_with_false() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "false");
		ConfigurationProcessor processor = new BooleanProcessor();
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(false);
	}
	
	@Test
	public void should_update_the_table_entry_with_false_when_using_a_wrong_value() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "wrongValue");
		ConfigurationProcessor processor = new BooleanProcessor();
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(false);
	}
}