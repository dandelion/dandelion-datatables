package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.config.DatatableBundles;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.HttpMethod;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportFeatureTest extends AbstractExtensionTest {

	private ExportFeature exportFeature;

	@Test
	public void shoud_handle_filter_export() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setUrl("/myExportUrl");
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));

		assertThat(exportFeature.getBeforeAll()).isNull();
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).isEmpty();
		assertThat(mainConfig.get(DTConstants.DT_DOM)).isEqualTo("lEfrtip");
	}
	
	@Test
	public void shoud_handle_controller_export_with_GET_and_no_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl");
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).isEmpty();
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){window.location=\"/myExportUrl?\" + decodeURIComponent($.param(oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings())).replace(/\\+/g,' '));}");
	}
	
	@Test
	public void shoud_handle_controller_export_with_GET_existing_params_and_no_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl?existingParam=val");
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).isEmpty();
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){window.location=\"/myExportUrl?existingParam=val&\" + decodeURIComponent($.param(oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings())).replace(/\\+/g,' '));}");
	}
	
	@Test
	public void shoud_handle_controller_export_with_POST_and_no_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl");
		exportConf.setMethod(HttpMethod.POST);
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).contains(DatatableBundles.DDL_DT_EXPORT.getBundleName());
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){$.download('/myExportUrl', decodeURIComponent($.param(oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings())).replace(/\\+/g,' '),'POST'));}");
	}
	
	@Test
	public void shoud_handle_controller_export_with_GET_and_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl");
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERPARAM, "param1=val1&param2=val2");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).isEmpty();
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){var aoData = oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings());param1=val1&param2=val2(aoData);window.location=\"/myExportUrl?\" + decodeURIComponent($.param(aoData)).replace(/\\+/g,' ');}");
	}
	
	@Test
	public void shoud_handle_controller_export_with_GET_existing_params_and_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl?existingParam=val");
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERPARAM, "param1=val1&param2=val2");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).isEmpty();
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){var aoData = oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings());param1=val1&param2=val2(aoData);window.location=\"/myExportUrl?existingParam=val&\" + decodeURIComponent($.param(aoData)).replace(/\\+/g,' ');}");
	}
	
	@Test
	public void shoud_handle_controller_export_with_POST_and_extra_param() {
		
		exportFeature = new ExportFeature();
		
		ExportConf exportConf = new ExportConf("csv");
		exportConf.setHasCustomUrl(true);
		exportConf.setUrl("/myExportUrl");
		exportConf.setMethod(HttpMethod.POST);
		exportConf.setLabel("CSV");
		table.getTableConfiguration().getExportConfiguration().put("csv", exportConf);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERPARAM, "param1=val1&param2=val2");
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(exportFeature)));
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).contains(DatatableBundles.DDL_DT_EXPORT.getBundleName());
		assertThat(exportFeature.getBeforeAll().toString()).isEqualTo("function ddl_dt_launch_export_fakeId_csv(){var aoData = oTable_fakeId.oApi._fnAjaxParameters(oTable_fakeId.fnSettings());param1=val1&param2=val2(aoData);$.download('/myExportUrl', decodeURIComponent($.param(aoData)).replace(/\\+/g,' '),'POST');}");
	}
}