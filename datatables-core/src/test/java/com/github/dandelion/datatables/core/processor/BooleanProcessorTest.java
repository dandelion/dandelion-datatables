package com.github.dandelion.datatables.core.processor;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public class BooleanProcessorTest extends ProcessorBaseTest {

	@Override
	public Processor getProcessor() throws Exception {
		Method setter = TableConfiguration.class.getMethod("setFeatureInfo", new Class[]{ Boolean.class });
		return new BooleanProcessor(setter);
	}
	
	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.process(null, tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureInfo()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.process("", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureInfo()).isNull();
	}
	
	@Test
	public void should_set_true_when_value_is_true() throws Exception{
		processor.process("true", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(true);
	}
	
	@Test
	public void should_set_false_when_value_is_false() throws Exception{
		processor.process("false", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(false);
	}
	
	@Test
	public void should_set_false_when_using_a_wrong_value() throws Exception{
		processor.process("wrongValue", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureInfo()).isEqualTo(false);
	}
}