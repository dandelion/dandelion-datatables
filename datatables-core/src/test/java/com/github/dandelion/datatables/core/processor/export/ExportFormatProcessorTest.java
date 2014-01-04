package com.github.dandelion.datatables.core.processor.export;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.processor.ConfigurationProcessor;
import com.github.dandelion.datatables.core.processor.MapEntry;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

public class ExportFormatProcessorTest extends TableProcessorBaseTest{

	@Override
	public ConfigurationProcessor getProcessor() {
		return new ExportFormatProcessor();
	}

	@Test
	public void should_configure_the_csv_init_export_with_a_custom_export_filename() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_FILENAME;
		exportFilenameToken.setPropertyName("global.export.csv.filename");
		entry = new MapEntry<ConfigToken<?>, Object>(exportFilenameToken, "my-custom-filename.csv");
		processor.process(entry, tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("csv"));
		assertThat(tableConfiguration.getExportConfiguration().get("csv").getFileName()).isEqualTo("my-custom-filename.csv");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_filename() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_FILENAME;
		exportFilenameToken.setPropertyName("global.export.myformat.filename");
		entry = new MapEntry<ConfigToken<?>, Object>(exportFilenameToken, "my-custom-filename.myformat");
		processor.process(entry, tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getFileName()).isEqualTo("my-custom-filename.myformat");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_label() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_LABEL;
		exportFilenameToken.setPropertyName("global.export.myformat.label");
		entry = new MapEntry<ConfigToken<?>, Object>(exportFilenameToken, "my-custom-label");
		processor.process(entry, tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getLabel()).isEqualTo("my-custom-label");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_class() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_CLASS;
		exportFilenameToken.setPropertyName("global.export.myformat.class");
		entry = new MapEntry<ConfigToken<?>, Object>(exportFilenameToken, "com.github.dandelion.datatables.core.export.CsvExport");
		processor.process(entry, tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getExportClass()).isEqualTo("com.github.dandelion.datatables.core.export.CsvExport");
	}
}
