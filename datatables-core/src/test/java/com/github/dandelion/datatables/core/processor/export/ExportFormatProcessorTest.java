package com.github.dandelion.datatables.core.processor.export;

import org.junit.Test;

import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.export.ExportFormatProcessor;
import com.github.dandelion.datatables.core.processor.MapEntry;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportFormatProcessorTest extends TableProcessorBaseTest{

	@Override
	public OptionProcessor getProcessor() {
		return new ExportFormatProcessor();
	}

	@Test
	public void should_configure_the_csv_init_export_with_a_custom_export_filename() {
		Option<?> option = DatatableOptions.EXPORT_FILENAME;
		option.setUserName("global.export.csv.filename");
		entry = new MapEntry<Option<?>, Object>(option, "my-custom-filename.csv");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfiguration().containsKey("csv"));
		assertThat(tableConfiguration.getExportConfiguration().get("csv").getFileName()).isEqualTo("my-custom-filename.csv");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_filename() {
		Option<?> option = DatatableOptions.EXPORT_FILENAME;
		option.setUserName("global.export.myformat.filename");
		entry = new MapEntry<Option<?>, Object>(option, "my-custom-filename.myformat");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getFileName()).isEqualTo("my-custom-filename.myformat");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_label() {
		Option<?> option = DatatableOptions.EXPORT_LABEL;
		option.setUserName("global.export.myformat.label");
		entry = new MapEntry<Option<?>, Object>(option, "my-custom-label");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getLabel()).isEqualTo("my-custom-label");
	}
	
	@Test
	public void should_init_export_with_a_custom_export_class() {
		Option<?> option = DatatableOptions.EXPORT_CLASS;
		option.setUserName("global.export.myformat.class");
		entry = new MapEntry<Option<?>, Object>(option, "com.github.dandelion.datatables.core.export.CsvExport");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfiguration().containsKey("myformat"));
		assertThat(tableConfiguration.getExportConfiguration().get("myformat").getExportClass()).isEqualTo("com.github.dandelion.datatables.core.export.CsvExport");
	}
}
