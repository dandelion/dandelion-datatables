package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.theme.Bootstrap2Theme;
import com.github.dandelion.datatables.core.theme.Theme;

public class TableConfigurationTest {

	private ConfigurationPropertiesLoader propertiesLoader;
	private TableConfiguration tableConfiguration;
	private HttpServletRequest request;

	@Before
	public void setup() throws BadConfigurationException {
		// mock ServletContext
		MockServletContext mockServletContext = new MockServletContext();

		// mock PageContext
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();

//		propertiesLoader = new ConfigurationPropertiesLoader();
//		propertiesLoader.loadDefaultConfiguration();
//		propertiesLoader.loadSpecificConfiguration();
	}

	@Test
	public void should_init_configuration_from_properties() {
		tableConfiguration = TableConfiguration.getInstance(request);

		assertThat(tableConfiguration.getFeatureInfo()).isNull();
		assertThat(tableConfiguration.getMainAggregatorEnable()).isEqualTo(false);
		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(5);
		assertThat(tableConfiguration.getExportClass(ExportType.CSV)).isEqualTo(
				"com.github.dandelion.datatables.core.export.CsvExport");
		assertThat(tableConfiguration.getPluginFixedHeader()).isEqualTo(false);
	}

	// @Test
	// public void should_init_table_configuration() {
	//
	// propertiesLoader.doInitTableConfiguration("global", tableConfiguration);
	//
	// assertThat(tableConfiguration.getFeatureInfo()).isNull();
	// assertThat(tableConfiguration.getMainAggregatorEnable()).isEqualTo(false);
	// assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(5);
	// assertThat(tableConfiguration.getExportClass(ExportType.CSV)).isEqualTo("com.github.dandelion.datatables.core.export.CsvExport");
	// assertThat(tableConfiguration.getPluginFixedHeader()).isEqualTo(false);
	// }

	@Test
	public void should_override_global_configuration_with_specific_programmatically() {
		tableConfiguration = TableConfiguration.getInstance(request);

		tableConfiguration.setMainCompressorEnable(true).setFeaturePaginationType(PaginationType.input)
				.setExtraTheme(new Bootstrap2Theme()).setAjaxPipeSize(12);

		assertThat(tableConfiguration.getFeatureInfo()).isNull();
		assertThat(tableConfiguration.getMainCompressorEnable()).isEqualTo(true);
		assertThat(tableConfiguration.getFeaturePaginationType()).isEqualTo(PaginationType.input);
		assertThat(tableConfiguration.getExtraTheme()).isEqualTo(Theme.BOOTSTRAP2.getInstance());
		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(12);
	}

//	@Test
//	public void should_not_create_properties_group_by_default() {
//		tableConfiguration = TableConfiguration.getInstance(request, "fakeGroup");
//
//		assertThat(tableConfiguration.getFeatureInfo()).isNull();
//		assertThat(tableConfiguration.getMainAggregatorEnable()).isNull();
//		assertThat(tableConfiguration.getAjaxPipeSize()).isNull();
//		assertThat(tableConfiguration.getExportClass(ExportType.CSV)).isNull();
//		assertThat(tableConfiguration.getPluginFixedHeader()).isNull();
//	}

	@Test
	public void test() throws IOException {
		InputStream in = this.getClass().getResourceAsStream("datatables-test.properties");
		Properties p = new Properties();
		p.load(in);
		String mystr = p.getProperty("global.main.base.package");
		System.out.println(mystr);
	}
}