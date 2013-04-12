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

package com.github.dandelion.datatables.integration.theme;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.github.dandelion.datatables.integration.DomBaseIT;

/**
 * Test the Bootstrap2 theme feature.
 *
 * @author Thibault Duchateau
 */
public class Bootstrap2IT extends DomBaseIT {

	@Test
	public void should_generate_table_markup_using_dom_source() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_dom");
	
		StringBuffer baseHref = new StringBuffer("http://");
		baseHref.append(SERVER_HOST);
		baseHref.append(":");
		baseHref.append(SERVER_PORT);
		baseHref.append("/datatablesController/datatables-bootstrap2.css?id=");
		baseHref.append(TABLE_ID);
		baseHref.append("&c=http%3A%2F%2F");
		baseHref.append(SERVER_HOST);
		baseHref.append("%3A");
		baseHref.append(SERVER_PORT);
		baseHref.append("%2Fthymeleaf%2Fthemes%2Fbootstrap2_dom");
		
		// Custom Bootstrap CSS must exist
		assertThat(getHtmlBody().findFirst("link").getAttribute("href")).isEqualTo(baseHref.toString());
		
		// Looking for the paging div
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div.paging_bootstrap ")).hasSize(1);
	}
	
	
	@Test
	public void should_generate_table_markup_using_ajax_source() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_ajax");
	
		StringBuffer baseHref = new StringBuffer("http://");
		baseHref.append(SERVER_HOST);
		baseHref.append(":");
		baseHref.append(SERVER_PORT);
		baseHref.append("/datatablesController/datatables-bootstrap2.css?id=");
		baseHref.append(TABLE_ID);
		baseHref.append("&c=http%3A%2F%2F");
		baseHref.append(SERVER_HOST);
		baseHref.append("%3A");
		baseHref.append(SERVER_PORT);
		baseHref.append("%2Fthymeleaf%2Fthemes%2Fbootstrap2_ajax");
		
		// Custom Bootstrap CSS must exist
		assertThat(getHtmlBody().findFirst("link").getAttribute("href")).isEqualTo(baseHref.toString());
		
		// Looking for the paging div
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div.paging_bootstrap ")).hasSize(1);
	}
	
//	@Test
	public void should_generate_bottom_right_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_bottom_right_link");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("style"))).isEqualTo("float:right;");
	}
	
//	@Test
	public void should_generate_bottom_middle_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_bottom_middle_link");
		
		System.out.println(driver.getPageSource());
		FluentWebElement divExport = find("#" + TABLE_ID + "_wrapper").findFirst("div.span6").findFirst("div");
		
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("style"))).isEqualTo("float:left;margin-left:10px;");
	}
	
//	@Test
	public void should_generate_bottom_left_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_bottom_left_link");
		
		FluentWebElement divExport = find("#" + TABLE_ID + "_wrapper").findFirst("div.span6").findFirst("div");
		
		
		assertThat(divExport.getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(divExport.getAttribute("style"))).isEqualTo("float:left;margin-right:10px;");
	}
	
	@Test
	public void should_generate_top_right_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_top_right_link");
		
		FluentWebElement divExport = find("#" + TABLE_ID + "_wrapper").find("div.span6", 1).findFirst("div");
		
		assertThat(divExport.getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(divExport.getAttribute("style"))).isEqualTo("float:right;");
	}
	
	@Test
	public void should_generate_top_middle_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_top_middle_link");
		
		FluentWebElement divExport = find("#" + TABLE_ID + "_wrapper").find("div.span6", 1).findFirst("div");
		
		assertThat(divExport.getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(divExport.getAttribute("style"))).isEqualTo("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_top_left_link() throws IOException, Exception {
		goTo("/thymeleaf/themes/bootstrap2_top_left_link");
		
		FluentWebElement divExport = find("#" + TABLE_ID + "_wrapper").findFirst("div.span6").findFirst("div");
		
		assertThat(divExport.getAttribute("class")).isEqualTo("dandelion_dataTables_export");
		assertThat(StringUtils.trimAllWhitespace(divExport.getAttribute("style"))).isEqualTo("float:left;margin-right:10px;");
	}
}