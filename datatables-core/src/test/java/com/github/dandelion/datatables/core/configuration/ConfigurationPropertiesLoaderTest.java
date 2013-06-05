package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.AbstractConfigurationLoader;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;

public class ConfigurationPropertiesLoaderTest {

	// TODO unit test for groups are missing
	
	@Test
	public void should_load_staging_global_configuration_from_properties() throws BadConfigurationException {
		AbstractConfigurationLoader confLoader = new DatatablesConfigurator().getConfLoader();
		confLoader.loadDefaultConfiguration();

		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo("");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_AGGREGATORENABLE)).isEqualTo("false");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.AJAX_PIPESIZE)).isEqualTo("5");
	}

	@Test
	public void should_load_staging_custom_configuration_using_system_property() throws BadConfigurationException {
		System.setProperty(SystemConstants.DANDELION_DT_CONF, new File(
				"src/test/java/com/github/dandelion/datatables/core/configuration/datatables-test.properties")
				.getAbsolutePath());

		AbstractConfigurationLoader confLoader = new DatatablesConfigurator().getConfLoader();
		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);

		// Default configuration from global file
		assertThat(confLoader.getStagingConfiguration().get(Configuration.EXPORT_LINKS)).isEqualTo("top_right");

		// Custom configuration from custom file
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_URL)).isEqualTo("myCustomBaseUrl");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo(
				"my.custom.package");
	}

	@Test(expected = BadConfigurationException.class)
	public void should_not_load_staging_custom_configuration_using_wrong_system_property() throws BadConfigurationException {
		System.setProperty(SystemConstants.DANDELION_DT_CONF, "wrong/path");

		AbstractConfigurationLoader confLoader = new DatatablesConfigurator().getConfLoader();
		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);
	}

	@Test
	public void should_load_stating_custom_configuration_using_classpath() throws BadConfigurationException {
		AbstractConfigurationLoader confLoader = new DatatablesConfigurator().getConfLoader();
		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);

		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo(
				"my.custom.package");
	}
	
	@AfterClass
	public static void afterClass(){
		System.clearProperty(SystemConstants.DANDELION_DT_CONF);
	}
}