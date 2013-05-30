package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class DatatablesConfiguratorTest {

	@Test
	public void should_use_properties_loader_by_default() {
		DatatablesConfigurator configurator = DatatablesConfigurator.getInstance();
		assertThat(configurator.getConfLoader()).isInstanceOf(ConfigurationPropertiesLoader.class);
	}
}