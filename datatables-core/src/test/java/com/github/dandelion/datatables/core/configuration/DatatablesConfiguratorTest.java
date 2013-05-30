package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.dandelion.datatables.core.constants.SystemConstants;

public class DatatablesConfiguratorTest {

	@Before
	public void setup(){
		System.setProperty(SystemConstants.DANDELION_DT_CONF_CLASS, "");
	}
	
	@Test
	public void should_use_properties_loader_by_default() {
		DatatablesConfigurator configurator = DatatablesConfigurator.getInstance();
		assertThat(configurator.getConfLoader()).isInstanceOf(ConfigurationPropertiesLoader.class);
	}
	
	@Test
	public void should_use_another_configutor_using_system_property(){
		System.setProperty(SystemConstants.DANDELION_DT_CONF_CLASS, "com.github.dandelion.datatables.core.configuration.FakeConfigurationLoader");
		assertThat(DatatablesConfigurator.getInstance().getConfLoader()).isInstanceOf(AbstractConfigurationLoader.class);
	}
}