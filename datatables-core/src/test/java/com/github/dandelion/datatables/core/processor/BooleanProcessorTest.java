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

public class BooleanProcessorTest {

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
		processor = new BooleanProcessor();
	}
	
	@Test
	public void should_set_null_when_value_is_null() {
		processor.process(TableConfig.AJAX_DEFERRENDER, null, tableConfiguration);
		assertThat(TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() {
		processor.process(TableConfig.AJAX_DEFERRENDER, "", tableConfiguration);
		assertThat(TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_set_true_when_value_is_true() throws Exception{
		processor.process(TableConfig.AJAX_DEFERRENDER, "true", tableConfiguration);
		assertThat(TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration)).isTrue();
	}
	
	@Test
	public void should_set_false_when_value_is_false() throws Exception{
		processor.process(TableConfig.AJAX_DEFERRENDER, "false", tableConfiguration);
		assertThat(TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration)).isFalse();
	}
	
	@Test
	public void should_set_false_when_using_a_wrong_value() throws Exception{
		processor.process(TableConfig.AJAX_DEFERRENDER, "someString", tableConfiguration);
		assertThat(TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration)).isFalse();
	}
}