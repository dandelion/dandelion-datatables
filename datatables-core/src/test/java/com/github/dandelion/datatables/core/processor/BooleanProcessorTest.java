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
	public void should_set_true_when_value_is_true() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "true");
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(true);
	}
	
	@Test
	public void should_set_false_when_value_is_false() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "false");
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(false);
	}
	
	@Test
	public void should_set_false_when_using_a_wrong_value() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_DEFERRENDER, "wrongValue");
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(false);
	}
}