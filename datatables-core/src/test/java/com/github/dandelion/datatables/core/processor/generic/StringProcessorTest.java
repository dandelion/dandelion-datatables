package com.github.dandelion.datatables.core.processor.generic;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.processor.GenericProcessor;
import com.github.dandelion.datatables.core.processor.GenericProcessorBaseTest;

public class StringProcessorTest extends GenericProcessorBaseTest {

	@Override
	public GenericProcessor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setAjaxServerData", new Class[]{ String.class });
		return new StringProcessor(setter);
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.processConfiguration(null, tableConfiguration);
		assertThat(tableConfiguration.getAjaxServerData()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.processConfiguration("", tableConfiguration);
		assertThat(tableConfiguration.getAjaxServerData()).isNull();
	}
	
	@Test
	public void should_set_a_string_true_when_value_is_a_string() throws Exception{
		processor.processConfiguration("aJsFunctionName", tableConfiguration);
		assertThat(tableConfiguration.getAjaxServerData()).isEqualTo("aJsFunctionName");
	}
}