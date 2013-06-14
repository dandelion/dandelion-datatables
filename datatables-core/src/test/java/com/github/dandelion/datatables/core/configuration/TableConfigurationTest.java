package com.github.dandelion.datatables.core.configuration;

import static org.fest.assertions.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.theme.Bootstrap2Theme;
import com.github.dandelion.datatables.core.extension.theme.Theme;

public class TableConfigurationTest {

	private HttpServletRequest request;

	@Before
	public void setup() throws BadConfigurationException {
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
	}
	
	@Test
	public void should_init_configuration_from_properties() {
		TableConfiguration tableConfiguration = TableConfiguration.getInstance(request);

		assertThat(tableConfiguration.getFeatureInfo()).isNull();
		assertThat(tableConfiguration.getMainAggregatorEnable()).isEqualTo(false);
		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(5);

		// Export configurations
		assertThat(tableConfiguration.getExportConfs()).isNull();
		assertThat(tableConfiguration.getExportLinkPositions()).contains(ExportLinkPosition.TOP_RIGHT);
		assertThat(tableConfiguration.getExportClass(ExportType.XLS)).isEqualTo(
				"com.github.dandelion.datatables.extras.export.poi.XlsExport");
		assertThat(tableConfiguration.getExportClass(ExportType.CSV)).isEqualTo(
				"com.github.dandelion.datatables.core.export.CsvExport");
		assertThat(tableConfiguration.getPluginFixedHeader()).isEqualTo(false);
	}

	@Test
	public void should_override_global_configuration_with_specific_programmatically() {
		TableConfiguration tableConfiguration = TableConfiguration.getInstance(request);

		tableConfiguration.setMainCompressorEnable(true).setFeaturePaginationType(PaginationType.INPUT)
				.setExtraTheme(new Bootstrap2Theme()).setAjaxPipeSize(12);

		assertThat(tableConfiguration.getFeatureInfo()).isNull();
		assertThat(tableConfiguration.getMainCompressorEnable()).isEqualTo(true);
		assertThat(tableConfiguration.getFeaturePaginationType()).isEqualTo(PaginationType.INPUT);
		assertThat(tableConfiguration.getExtraTheme()).isEqualTo(Theme.BOOTSTRAP2.getInstance());
		assertThat(tableConfiguration.getAjaxPipeSize()).isEqualTo(12);
	}

//	@Test
//	public void test() throws IOException {
//		InputStream in = this.getClass().getResourceAsStream("datatables-test.properties");
//		Properties p = new Properties();
//		p.load(in);
//		String mystr = p.getProperty("global.main.base.package");
//	}
}