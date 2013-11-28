package com.github.dandelion.datatables.core.processor.generic;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.processor.GenericProcessor;
import com.github.dandelion.datatables.core.processor.GenericProcessorBaseTest;

public class BooleanProcessorTest extends GenericProcessorBaseTest {

	@Override
	public GenericProcessor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setFeatureInfo", new Class[]{ Boolean.class });
		return new BooleanProcessor(setter);
	}
	
	@Test
	public void should_set_null_when_value_is_null() {
		processor.processConfiguration(null, tableConfiguration);
		assertThat(tableConfiguration.getFeatureInfo()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() {
		processor.processConfiguration("", tableConfiguration);
		assertThat(tableConfiguration.getFeatureInfo()).isNull();
	}
	
	@Test
	public void should_set_true_when_value_is_true() throws Exception{
		processor.processConfiguration("true", tableConfiguration);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(true);
	}
	
	@Test
	public void should_set_false_when_value_is_false() throws Exception{
		processor.processConfiguration("false", tableConfiguration);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(false);
	}
	
	@Test
	public void should_set_false_when_using_a_wrong_value() throws Exception{
		processor.processConfiguration("wrongValue", tableConfiguration);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(false);
	}
}