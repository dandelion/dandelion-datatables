package com.github.dandelion.datatables.core.processor.generic;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.processor.GenericProcessor;
import com.github.dandelion.datatables.core.processor.GenericProcessorBaseTest;

public class IntegerProcessorTest extends GenericProcessorBaseTest {
	
	@Override
	public GenericProcessor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setFeatureDisplayLength", new Class[]{ Integer.class });
		return new IntegerProcessor(setter);
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.processConfiguration(null, tableConfiguration);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.processConfiguration("", tableConfiguration);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isNull();
	}
	
	@Test
	public void should_set_1_when_value_is_1() throws Exception{
		processor.processConfiguration("1", tableConfiguration);
		assertThat(tableConfiguration.getFeatureDisplayLength()).isEqualTo(new Integer(1));
	}
	
	@Test(expected = ConfigurationProcessingException.class)
	public void should_throw_an_exception_when_not_using_an_integer() throws Exception{
		processor.processConfiguration("a", tableConfiguration);
	}
}