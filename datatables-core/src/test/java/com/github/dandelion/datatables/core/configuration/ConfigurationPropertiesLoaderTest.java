package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;

public class ConfigurationPropertiesLoaderTest {

	private AbstractConfigurationLoader confLoader;

	// TODO unit test for groups are missing
	
	@Before
	public void setup() throws BadConfigurationException {
		confLoader = DatatablesConfigurator.getInstance().getConfLoader();
	}

	@Test
	public void should_load_staging_global_configuration_from_properties() throws BadConfigurationException {
		confLoader.loadDefaultConfiguration();

		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo("");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_AGGREGATORENABLE)).isEqualTo("false");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.AJAX_PIPESIZE)).isEqualTo("5");
	}

	@Test
	public void should_load_staging_custom_configuration_using_system_property() throws BadConfigurationException {
		String testPath = this.getClass().getClassLoader()
				.getResource("com/github/dandelion/datatables/core/configuration/datatables-test.properties").getPath();
		System.setProperty(SystemConstants.DANDELION_DT_CONF, testPath);

		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);

		// Default configuration from global file
		assertThat(confLoader.getStagingConfiguration().get(Configuration.EXPORT_LINKS)).isEqualTo("top_right");

		// Custom configuration from custom file
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_URL)).isEqualTo("myCustomBaseUrl");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo(
				"my.custom.package");
	}

	@Test
	public void should_not_load_staging_custom_configuration_using_wrong_system_property() throws BadConfigurationException {
		System.setProperty(SystemConstants.DANDELION_DT_CONF, "wrong/path");

		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);

		// All configurations must be the same as default ones
		assertThat(confLoader.getStagingConfiguration().get(Configuration.EXPORT_LINKS)).isEqualTo("top_right");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_URL)).isEqualTo("");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo("");
	}

	@Test
	public void should_load_stating_custom_configuration_using_classpath() throws BadConfigurationException {
		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);

		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo(
				"my.custom.package");
	}
}