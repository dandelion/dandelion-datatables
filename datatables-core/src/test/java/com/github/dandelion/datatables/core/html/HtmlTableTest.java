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
package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

public class HtmlTableTest {

	private HtmlTable table;
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	private HttpServletRequest request;
	
	@Before
	public void createHtmlTable() {
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		table = new HtmlTable("tableId", request);
	}

	@Test
	public void should_generate_table_with_an_unallowed_character_in_the_id() {
		table = new HtmlTable("table-id", request);
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"table-id\"><thead></thead><tbody></tbody></table>");
		assertThat(table.getId()).isEqualTo("tableid");
	}
	
	@Test
	public void should_generate_table_with_id() {
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_one_class() {
		table.getTableConfiguration().addCssClass("aClass");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\" class=\"aClass\"><thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_several_classes() {
		table.getTableConfiguration().addCssClass("oneClass");
		table.getTableConfiguration().addCssClass("twoClass");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\" class=\"oneClass twoClass\"><thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_one_style() {
		table.getTableConfiguration().addCssStyle("border:1px");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\" style=\"border:1px\"><thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_several_styles() {
		table.getTableConfiguration().addCssStyle("border:1px");
		table.getTableConfiguration().addCssStyle("align:center");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\" style=\"border:1px;align:center\"><thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_caption() {
		HtmlCaption caption = new HtmlCaption();
		caption.setTitle("title");
		table.setCaption(caption);
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\">" + caption.toHtml() + "<thead></thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_head_rows() {
		HtmlRow header1 = table.addHeaderRow();
		header1.setId("head1");
		HtmlRow header2 = table.addHeaderRow();
		header2.setId("head2");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead>" + header1.toHtml() + header2.toHtml() + "</thead><tbody></tbody></table>");
	}

	@Test
	public void should_generate_table_with_body_rows() {
		HtmlRow body1 = table.addRow("body1");
		HtmlRow body2 = table.addRow();
		body2.setId("body2");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead></thead><tbody>" + body1.toHtml() + body2.toHtml() + "</tbody></table>");
	}

	@Test
	public void should_generate_table_with_body_rows_2() {
		HtmlRow body3 = new HtmlRow("body3");
		HtmlRow body4 = new HtmlRow("body4");
		table.addRows(body3, body4);
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead></thead><tbody>" + body3.toHtml() + body4.toHtml() + "</tbody></table>");
	}

	@Test
	public void should_generate_table_with_foot_rows() {
		HtmlRow foot1 = table.addFooterRow();
		foot1.setId("foot1");
		HtmlRow foot2 = table.addFooterRow();
		foot2.setId("foot2");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead></thead><tbody></tbody><tfoot>" + foot1.toHtml() + foot2.toHtml() + "</tfoot></table>");
	}

	@Test
	public void should_generate_table_with_outstanding_rows() {
		HtmlRow header1 = table.addHeaderRow();
		header1.setId("head1");
		HtmlRow header2 = table.addHeaderRow();
		header2.setId("head2");
		HtmlRow body1 = table.addRow("body1");
		HtmlRow body2 = table.addRow("body2");
		HtmlRow foot1 = table.addFooterRow();
		foot1.setId("foot1");
		HtmlRow foot2 = table.addFooterRow();
		foot2.setId("foot2");
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\">" + //
				"<thead>" + header1.toHtml() + header2.toHtml() + "</thead>" + //
				"<tbody>" + body1.toHtml() + body2.toHtml() + "</tbody>" + //
				"<tfoot>" + foot1.toHtml() + foot2.toHtml() + "</tfoot>" + //
				"</table>");
	}
	
	@Test
	public void should_get_last_rows(){
		HtmlRow header1 = table.addHeaderRow();
		header1.setId("head1");
		HtmlRow header2 = table.addHeaderRow();
		header2.setId("head2");
		table.addRow("body1");
		HtmlRow body2 = table.addRow("body2");
		HtmlRow foot1 = table.addFooterRow();
		foot1.setId("foot1");
		HtmlRow foot2 = table.addFooterRow();
		foot2.setId("foot2");

		assertThat(table.getLastHeaderRow()).isEqualTo(header2);
		assertThat(table.getLastBodyRow()).isEqualTo(body2);
		assertThat(table.getLastFooterRow()).isEqualTo(foot2);
	}
}