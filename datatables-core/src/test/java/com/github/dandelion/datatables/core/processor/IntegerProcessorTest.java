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
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;

public class IntegerProcessorTest {
	
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
		processor = new IntegerProcessor();
	}
	
	@Test
	public void should_set_1_when_value_is_1() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_PIPESIZE, "1");
		processor.process(entry, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(1);
	}
	
	@Test(expected = ConfigurationProcessingException.class)
	public void should_throw_an_exception_when_not_using_an_integer() throws Exception{
		Entry<ConfigToken<?>, Object> entry = new MapEntry<ConfigToken<?>, Object>(TableConfig.AJAX_PIPESIZE, "number");
		processor.process(entry, tableConfiguration);
	}
}