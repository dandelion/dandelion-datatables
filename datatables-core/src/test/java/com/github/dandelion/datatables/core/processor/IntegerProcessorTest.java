package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.AttributeProcessingException;

public class IntegerProcessorTest extends ProcessorBaseTest {
	
	@Override
	public Processor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setFeatureDisplayLength", new Class[]{ Integer.class });
		return new IntegerProcessor(setter);
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.process(null, tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.process("", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isNull();
	}
	
	@Test
	public void should_set_1_when_value_is_1() throws Exception{
		processor.process("1", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isEqualTo(new Integer(1));
	}
	
	@Test(expected = AttributeProcessingException.class)
	public void should_throw_an_exception_when_not_using_an_integer() throws Exception{
		processor.process("a", tableConfiguration, confToBeApplied);
	}
}