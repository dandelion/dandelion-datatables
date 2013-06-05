package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public class StringProcessorTest extends ProcessorBaseTest {

	@Override
	public Processor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setAjaxServerData", new Class[]{ String.class });
		return new StringProcessor(setter);
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.process(null, tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getAjaxServerData()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.process("", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getAjaxServerData()).isNull();
	}
	
	@Test
	public void should_set_a_string_true_when_value_is_a_string() throws Exception{
		processor.process("aJsFunctionName", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getAjaxServerData()).isEqualTo("aJsFunctionName");
	}
}