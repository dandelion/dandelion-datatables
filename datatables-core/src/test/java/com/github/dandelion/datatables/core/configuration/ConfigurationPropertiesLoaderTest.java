package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;

public class ConfigurationPropertiesLoaderTest {

//	private ConfigurationPropertiesLoader propertiesLoader;
//	private TableConfiguration tableConfiguration;
	private HttpServletRequest request;
	private AbstractConfigurationLoader confLoader;
	
	@Before
	public void setup() throws BadConfigurationException {
		// mock ServletContext
		MockServletContext mockServletContext = new MockServletContext();

		// mock PageContext
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		
		confLoader = DatatablesConfigurator.getInstance().getConfLoader();
		
//		tableConfiguration = TableConfiguration.getInstance(request);
//		propertiesLoader = new ConfigurationPropertiesLoader();
//		propertiesLoader.loadDefaultConfiguration();
//		propertiesLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);
	}

	@Test
	public void should_load_staging_global_configuration_from_properties() throws BadConfigurationException {
		confLoader.loadDefaultConfiguration();
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo("");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_AGGREGATORENABLE)).isEqualTo("false");
		assertThat(confLoader.getStagingConfiguration().get(Configuration.AJAX_PIPESIZE)).isEqualTo("5");
	}

	@Test
	public void should_load_staging_specific_configuration_from_properties() throws BadConfigurationException {
		confLoader.loadDefaultConfiguration();
		confLoader.loadSpecificConfiguration(TableConfiguration.DEFAULT_GROUP_NAME);
		
		assertThat(confLoader.getStagingConfiguration().get(Configuration.MAIN_BASE_PACKAGE)).isEqualTo("my.custom.package");
	}
	
	
	@Test
	public void should_init_table_configuration() {

//		propertiesLoader.doInitTableConfiguration("global", tableConfiguration);
//
//		assertThat(tableConfiguration.getFeatureInfo()).isNull();
//		assertThat(tableConfiguration.getMainAggregatorEnable()).isEqualTo(false);
//		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(5);
//		assertThat(tableConfiguration.getExportClass(ExportType.CSV)).isEqualTo("com.github.dandelion.datatables.core.export.CsvExport");
//		assertThat(tableConfiguration.getPluginFixedHeader()).isEqualTo(false);
	}

	@Test
	public void should_override_default_configuration_with_specific() {
//		propertiesLoader.getProperties().put("global.main.compressor.enable", "true");
//		propertiesLoader.getProperties().put("global.feature.paginationType", "input");
//		propertiesLoader.getProperties().put("global.extra.theme", "bootstrap2");
//		propertiesLoader.getProperties().put("global.ajax.pipeSize", "12");
//		
//		propertiesLoader.doInitTableConfiguration("global", tableConfiguration);
//
//		assertThat(tableConfiguration.getFeaturePaginationType()).isEqualTo(PaginationType.input);
//		assertThat(tableConfiguration.getExtraTheme()).isEqualTo(Theme.BOOTSTRAP2);
//		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(12);
	}
	
	@Test
	public void should_not_load_any_properties_group_if_no_group_is_defined() {		
//		propertiesLoader.doInitTableConfiguration("fakeGroup", tableConfiguration);
//
//		
//		assertThat(tableConfiguration.getFeaturePaginationType()).isEqualTo(PaginationType.input);
//		assertThat(tableConfiguration.getExtraTheme()).isEqualTo(Theme.BOOTSTRAP2);
//		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(12);
	}
}