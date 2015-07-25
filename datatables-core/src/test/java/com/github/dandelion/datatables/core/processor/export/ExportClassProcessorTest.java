package com.github.dandelion.datatables.core.processor.export;

import org.junit.Test;

import com.github.dandelion.core.option.DefaultOptionProcessingContext;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.option.OptionProcessor;
import com.github.dandelion.datatables.core.MapEntry;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.HttpMethod;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.processor.export.ExportClassProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportClassProcessorTest extends TableProcessorBaseTest {

   @Override
   public OptionProcessor getProcessor() {
      return new ExportClassProcessor();
   }

   @Test
   public void should_configure_the_csv_init_export_with_a_custom_export_filename() {
      Option<?> option = DatatableOptions.EXPORT_CSV_CLASS;
      entry = new MapEntry<Option<?>, Object>(option, "com.github.dandelion.MyExportClass");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request,
            processor.isBundleGraphUpdatable());
      processor.process(pc);

      assertThat(tableConfiguration.getExportConfigurations().containsKey("csv"));
      assertThat(tableConfiguration.getExportConfigurations()).doesNotContainKey("pdf");
      assertThat(tableConfiguration.getExportConfigurations()).doesNotContainKey("xml");
      assertThat(tableConfiguration.getExportConfigurations()).doesNotContainKey("xls");
      assertThat(tableConfiguration.getExportConfigurations()).doesNotContainKey("xlsx");

      ExportConf ec = tableConfiguration.getExportConfigurations().get("csv");

      // Updated
      assertThat(ec.getExportClass()).isEqualTo("com.github.dandelion.MyExportClass");

      // Default
      assertThat(ec.getAutoSize()).isEqualTo(true);
      assertThat(ec.getCssClass()).isNull();
      assertThat(ec.getCssStyle()).isNull();
      assertThat(ec.getFileExtension()).isEqualTo("csv");
      assertThat(ec.getFileName()).matches("export-csv-.*");
      assertThat(ec.getFormat()).isEqualTo("csv");
      assertThat(ec.getIncludeHeader()).isEqualTo(true);
      assertThat(ec.getLabel()).isEqualTo("CSV");
      assertThat(ec.getMethod()).isEqualTo(HttpMethod.GET);
      assertThat(ec.getMimeType()).isEqualTo("text/csv");
      assertThat(ec.getOrientation()).isNull();
      assertThat(ec.getUrl()).isNull();
   }
}
