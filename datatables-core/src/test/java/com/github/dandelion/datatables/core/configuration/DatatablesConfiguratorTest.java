package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.ConfigurationPropertiesLoader;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.constants.SystemConstants;

public class DatatablesConfiguratorTest {

	@Test
	public void should_use_properties_loader_by_default() {
		DatatablesConfigurator configurator = new DatatablesConfigurator();
		assertThat(configurator.getConfLoader()).isInstanceOf(ConfigurationPropertiesLoader.class);
	}
	
	@Test
	public void should_use_another_configutor_using_system_property(){
		System.setProperty(SystemConstants.DANDELION_DT_CONF_CLASS, "com.github.dandelion.datatables.core.configuration.ConfigurationFakeLoader");
		assertThat(new DatatablesConfigurator().getConfLoader()).isInstanceOf(ConfigurationFakeLoader.class);
		System.clearProperty(SystemConstants.DANDELION_DT_CONF_CLASS);
	}
}