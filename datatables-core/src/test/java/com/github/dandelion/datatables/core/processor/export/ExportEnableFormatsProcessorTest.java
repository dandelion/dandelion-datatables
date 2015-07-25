/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.core.processor.export;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.github.dandelion.core.option.DefaultOptionProcessingContext;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.option.OptionProcessor;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.MapEntry;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportConf.Orientation;
import com.github.dandelion.datatables.core.export.HttpMethod;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.processor.export.ExportEnabledFormatProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ExportEnableFormatsProcessorTest extends TableProcessorBaseTest {

	@Override
	public OptionProcessor getProcessor() {
		return new ExportEnabledFormatProcessor();
	}

	@Test
	public void should_enable_csv_export_with_default_configuration() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.EXPORT_ENABLED_FORMATS, "csv");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfigurations()).hasSize(1);
		
		ExportConf csvExportConf = tableConfiguration.getExportConfigurations().get("csv");
		assertThat(csvExportConf.getFormat()).isEqualTo("csv");
		assertThat(csvExportConf.getFileName()).isEqualTo("export-csv-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(csvExportConf.getMimeType()).isEqualTo("text/csv");
		assertThat(csvExportConf.getLabel()).isEqualTo("CSV");
		assertThat(csvExportConf.getIncludeHeader()).isTrue();
		assertThat(csvExportConf.getUrl()).isEqualTo("?dtt=f&dtf=csv&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(csvExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(csvExportConf.getAutoSize()).isTrue();
		assertThat(csvExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_CSV_CLASS);
		assertThat(csvExportConf.getFileExtension()).isEqualTo("csv");
		assertThat(csvExportConf.getOrientation()).isNull();
		
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.EXPORT_ENABLED_FORMATS, "csv  ");
		pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfigurations()).hasSize(1);
		
		assertThat(csvExportConf.getFormat()).isEqualTo("csv");
		assertThat(csvExportConf.getFileName()).isEqualTo("export-csv-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(csvExportConf.getMimeType()).isEqualTo("text/csv");
		assertThat(csvExportConf.getLabel()).isEqualTo("CSV");
		assertThat(csvExportConf.getIncludeHeader()).isTrue();
		assertThat(csvExportConf.getUrl()).isEqualTo("?dtt=f&dtf=csv&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(csvExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(csvExportConf.getAutoSize()).isTrue();
		assertThat(csvExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_CSV_CLASS);
		assertThat(csvExportConf.getFileExtension()).isEqualTo("csv");
	}
	
	@Test
	public void should_enable_csv_and_pdf_export() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.EXPORT_ENABLED_FORMATS, "csv,pdf");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfigurations()).hasSize(2);
		
		ExportConf csvExportConf = tableConfiguration.getExportConfigurations().get("csv");
		assertThat(csvExportConf.getFormat()).isEqualTo("csv");
		assertThat(csvExportConf.getFileName()).isEqualTo("export-csv-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(csvExportConf.getMimeType()).isEqualTo("text/csv");
		assertThat(csvExportConf.getLabel()).isEqualTo("CSV");
		assertThat(csvExportConf.getIncludeHeader()).isTrue();
		assertThat(csvExportConf.getUrl()).isEqualTo("?dtt=f&dtf=csv&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(csvExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(csvExportConf.getAutoSize()).isTrue();
		assertThat(csvExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_CSV_CLASS);
		assertThat(csvExportConf.getFileExtension()).isEqualTo("csv");
		assertThat(csvExportConf.getOrientation()).isNull();
		
		ExportConf pdfExportConf = tableConfiguration.getExportConfigurations().get("pdf");
		assertThat(pdfExportConf.getFormat()).isEqualTo("pdf");
		assertThat(pdfExportConf.getFileName()).isEqualTo("export-pdf-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(pdfExportConf.getMimeType()).isEqualTo("application/pdf");
		assertThat(pdfExportConf.getLabel()).isEqualTo("PDF");
		assertThat(pdfExportConf.getIncludeHeader()).isTrue();
		assertThat(pdfExportConf.getUrl()).isEqualTo("?dtt=f&dtf=pdf&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(pdfExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(pdfExportConf.getAutoSize()).isTrue();
		assertThat(pdfExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_PDF_CLASS);
		assertThat(pdfExportConf.getFileExtension()).isEqualTo("pdf");
		assertThat(pdfExportConf.getOrientation()).isEqualTo(Orientation.LANDSCAPE);
	}
	
	@Test
	public void should_enable_csv_and_pdf_export_with_malformatted_string() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.EXPORT_ENABLED_FORMATS, "csv, pdf  ");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfigurations()).hasSize(2);
		
		ExportConf csvExportConf = tableConfiguration.getExportConfigurations().get("csv");
		assertThat(csvExportConf.getFormat()).isEqualTo("csv");
		assertThat(csvExportConf.getFileName()).isEqualTo("export-csv-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(csvExportConf.getMimeType()).isEqualTo("text/csv");
		assertThat(csvExportConf.getLabel()).isEqualTo("CSV");
		assertThat(csvExportConf.getIncludeHeader()).isTrue();
		assertThat(csvExportConf.getUrl()).isEqualTo("?dtt=f&dtf=csv&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(csvExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(csvExportConf.getAutoSize()).isTrue();
		assertThat(csvExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_CSV_CLASS);
		assertThat(csvExportConf.getFileExtension()).isEqualTo("csv");
		assertThat(csvExportConf.getOrientation()).isNull();
		
		ExportConf pdfExportConf = tableConfiguration.getExportConfigurations().get("pdf");
		assertThat(pdfExportConf.getFormat()).isEqualTo("pdf");
		assertThat(pdfExportConf.getFileName()).isEqualTo("export-pdf-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(pdfExportConf.getMimeType()).isEqualTo("application/pdf");
		assertThat(pdfExportConf.getLabel()).isEqualTo("PDF");
		assertThat(pdfExportConf.getIncludeHeader()).isTrue();
		assertThat(pdfExportConf.getUrl()).isEqualTo("?dtt=f&dtf=pdf&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(pdfExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(pdfExportConf.getAutoSize()).isTrue();
		assertThat(pdfExportConf.getExportClass()).isEqualTo(ExportConf.DEFAULT_PDF_CLASS);
		assertThat(pdfExportConf.getFileExtension()).isEqualTo("pdf");
		assertThat(pdfExportConf.getOrientation()).isEqualTo(Orientation.LANDSCAPE);
	}
	
	@Test
	public void should_enable_totally_custom_export() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.EXPORT_ENABLED_FORMATS, "myformat");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
				
		assertThat(tableConfiguration.getExportConfigurations()).hasSize(1);
		
		ExportConf myFormatExportConf = tableConfiguration.getExportConfigurations().get("myformat");
		assertThat(myFormatExportConf.getFormat()).isEqualTo("myformat");
		assertThat(myFormatExportConf.getFileName()).isEqualTo("export-myformat-" + new SimpleDateFormat("yyyymmDD").format(new GregorianCalendar().getTime()));
		assertThat(myFormatExportConf.getMimeType()).isNull();
		assertThat(myFormatExportConf.getLabel()).isEqualTo("MYFORMAT");
		assertThat(myFormatExportConf.getIncludeHeader()).isTrue();
		assertThat(myFormatExportConf.getUrl()).isEqualTo("?dtt=f&dtf=myformat&dti=fakeId&dtp=y&" + WebConstants.DANDELION_ASSET_FILTER_STATE + "=false");
		assertThat(myFormatExportConf.getMethod()).isEqualTo(HttpMethod.GET);
		assertThat(myFormatExportConf.getAutoSize()).isTrue();
		assertThat(myFormatExportConf.getExportClass()).isNull();
		assertThat(myFormatExportConf.getFileExtension()).isEqualTo("myformat");
		assertThat(myFormatExportConf.getOrientation()).isNull();
	}
}