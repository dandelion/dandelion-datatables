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
package com.github.dandelion.datatables.testing.export;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

/**
 * Base integration test for the export links generation.
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public abstract class ExportLinksBaseIT extends BaseIT {
	
	@Test
	public void should_generate_export_markup() throws Exception {
		goToPage("export/default_csv_link");
		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
	}
	
	@Test
	public void should_generate_csv_link_with_custom_url() throws Exception {
		goToPage("export/custom_csv_url");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("onclick")).isEqualTo("ddl_dt_launch_export_CSV()");

		String js = getConfigurationFromPage("export/custom_csv_url").getContent();
		assertThat(js).contains("function ddl_dt_launch_export_CSV(){window.location='/context/customCsvUrl?' + $.param(oTable_myTableId.oApi._fnAjaxParameters(oTable_myTableId.fnSettings()));};");
	}
	
	@Test
	public void should_generate_pdf_link_with_custom_url() throws Exception {
		goToPage("export/custom_pdf_url");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("PDF");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("onclick")).isEqualTo("ddl_dt_launch_export_PDF()");

		String js = getConfigurationFromPage("export/custom_pdf_url").getContent();
		assertThat(js).contains("function ddl_dt_launch_export_PDF(){window.location='/context/customPdfUrl?' + $.param(oTable_myTableId.oApi._fnAjaxParameters(oTable_myTableId.fnSettings()));};");
	}
	
	@Test
	public void should_generate_xls_link_with_custom_url() throws Exception {
		goToPage("export/custom_xls_url");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("XLS");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("onclick")).isEqualTo("ddl_dt_launch_export_XLS()");

		String js = getConfigurationFromPage("export/custom_xls_url").getContent();
		assertThat(js).contains("function ddl_dt_launch_export_XLS(){window.location='/context/customXlsUrl?' + $.param(oTable_myTableId.oApi._fnAjaxParameters(oTable_myTableId.fnSettings()));};");
	}
	
	@Test
	public void should_generate_xlsx_link_with_custom_url() throws Exception {
		goToPage("export/custom_xlsx_url");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("XLSX");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("onclick")).isEqualTo("ddl_dt_launch_export_XLSX()");

		String js = getConfigurationFromPage("export/custom_xlsx_url").getContent();
		assertThat(js).contains("function ddl_dt_launch_export_XLSX(){window.location='/context/customXlsxUrl?' + $.param(oTable_myTableId.oApi._fnAjaxParameters(oTable_myTableId.fnSettings()));};");
	}
	
	@Test
	public void should_generate_xml_link_with_custom_url() throws Exception {
		goToPage("export/custom_xml_url");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("XML");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("onclick")).isEqualTo("ddl_dt_launch_export_XML()");

		String js = getConfigurationFromPage("export/custom_xml_url").getContent();
		assertThat(js).contains("function ddl_dt_launch_export_XML(){window.location='/context/customXmlUrl?' + $.param(oTable_myTableId.oApi._fnAjaxParameters(oTable_myTableId.fnSettings()));};");
	}
}