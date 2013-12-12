package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public class StringBuilderProcessorTest {

	protected TableProcessor processor;
	protected TableConfiguration tableConfiguration;
	protected HttpServletRequest request;
	protected Map<ConfigToken<?>, Object> confToBeApplied;

	@Before
	public void setup() {
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		confToBeApplied = new HashMap<ConfigToken<?>, Object>();
		tableConfiguration = new TableConfiguration(confToBeApplied, request);
		processor = new StringBuilderProcessor();
	}

	@Test
	public void should_set_null_when_value_is_null() {
		processor.process(TableConfig.CSS_CLASS, null, tableConfiguration);
		assertThat(TableConfig.CSS_CLASS.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() {
		processor.process(TableConfig.CSS_CLASS, "", tableConfiguration);
		assertThat(TableConfig.CSS_CLASS.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_set_a_string_true_when_value_is_a_string() throws Exception{
		processor.process(TableConfig.CSS_CLASS, "someString", tableConfiguration);
		assertThat(TableConfig.CSS_CLASS.valueFrom(tableConfiguration).toString()).isEqualTo("someString");
	}
}