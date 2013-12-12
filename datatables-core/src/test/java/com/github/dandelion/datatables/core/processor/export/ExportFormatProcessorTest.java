package com.github.dandelion.datatables.core.processor.export;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.processor.TableProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

public class ExportFormatProcessorTest extends TableProcessorBaseTest{

	@Override
	public TableProcessor getProcessor() {
		return new ExportFormatProcessor();
	}

	@Test
	public void should_configure_the_csv_init_export_with_a_custom_export_filename() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_FILENAME;
		exportFilenameToken.setPropertyName("global.export.myformat.filename");
		processor.process(exportFilenameToken, "my-custom-filename.myformat", tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getFileName()).isEqualTo("my-custom-filename.myformat");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_filename() {
		ConfigToken<?> exportFilenameToken = TableConfig.EXPORT_FILENAME;
		exportFilenameToken.setPropertyName("global.export.myformat.filename");
		processor.process(exportFilenameToken, "my-custom-filename.myformat", tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getFileName()).isEqualTo("my-custom-filename.myformat");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_label() {
		ConfigToken<?> exportLabelToken = TableConfig.EXPORT_LABEL;
		exportLabelToken.setPropertyName("global.export.myformat.label");
		processor.process(exportLabelToken, "my-custom-label", tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getLabel()).isEqualTo("my-custom-label");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_class() {
		ConfigToken<?> exportClassToken = TableConfig.EXPORT_CLASS;
		exportClassToken.setPropertyName("global.export.myformat.class");
		processor.process(exportClassToken, "com.github.dandelion.datatables.core.export.CsvExport", tableConfiguration);
		
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getExportClass()).isEqualTo("com.github.dandelion.datatables.core.export.CsvExport");
	}
}
