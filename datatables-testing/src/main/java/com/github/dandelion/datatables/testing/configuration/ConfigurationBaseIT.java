package com.github.dandelion.datatables.testing.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class ConfigurationBaseIT extends BaseIT {

	@Test
	public void should_use_overriden_global_configuration() throws Exception {
		goToPage("configuration/global_overriden");
		assertThat(getTable().getAttribute("class")).contains("myClass");
	}
	
	@Test
	public void should_use_group1_configuration() throws Exception {
		goToPage("configuration/enable_custom_group");
		assertThat(getTable().getAttribute("class")).contains("my-group1-class");
	}
}
