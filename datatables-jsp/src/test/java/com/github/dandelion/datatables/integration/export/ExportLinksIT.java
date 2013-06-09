/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

package com.github.dandelion.datatables.integration.export;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.util.StringUtils;

import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the basic Features of Dandelion-Datatables.
 *
 * @author Thibault Duchateau
 */
public class ExportLinksIT extends DomBaseIT {

	@Test
	public void should_generate_export_markup() throws IOException, Exception {
		goTo("/export/default_csv_link.jsp");

		assertThat(find("div.dandelion_dataTables_export")).hasSize(1);
	}
	
	@Test
	public void should_generate_default_csv_link() throws IOException, Exception {
		goTo("/export/default_csv_link.jsp");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href")).isEqualTo("http://" + SERVER_HOST + ":" + SERVER_PORT + "/export/default_csv_link.jsp?dtt=1&dti=myTableId");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("style"))).isEqualTo("float:right;");
	}
	
	@Test
	public void should_generate_csv_link_with_one_existing_url_parameter() throws IOException, Exception {
		goTo("/export/default_csv_link.jsp?param1=val1");

		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href"))
			.isEqualTo("http://" + SERVER_HOST + ":" + SERVER_PORT + "/export/default_csv_link.jsp?param1=val1&dtt=1&dti=myTableId");
	}
	
	@Test
	public void should_generate_csv_link_with_multiple_existing_url_parameters() throws IOException, Exception {
		goTo("/export/default_csv_link.jsp?param1=val1&param2=val2");

		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href"))
			.isEqualTo("http://" + SERVER_HOST + ":" + SERVER_PORT + "/export/default_csv_link.jsp?param1=val1&param2=val2&dtt=1&dti=myTableId");
	}
	
//	@Test
	public void should_generate_custom_csv_link(){
		goTo("/export/custom_csv_link.jsp");
		
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("class")).contains("myClass");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("style")).contains("myStyle");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href")).isEqualTo(
				"/export/custom_csv_link.jsp?dtt=1&amp;dti=myTableId");
	}
}
