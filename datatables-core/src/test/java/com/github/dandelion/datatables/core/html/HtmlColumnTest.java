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
import org.junit.Before;
import org.junit.Test;
import com.github.dandelion.datatables.core.asset.DisplayType;

public class HtmlColumnTest extends HtmlTagWithContentTest {

	private HtmlColumn column;

	@Before
	@Override
	public void createHtmlTag(){
		tag = column = new HtmlColumn(true);
	}

	public void createHtmlCellTag(){
		tag = column = new HtmlColumn();
	}

	@Test
	public void should_contain_all_display_types() {
		assertThat(column.getEnabledDisplayTypes()).contains(DisplayType.ALL);
	}

	@Test
	public void should_contain_only_specified_display_types() {
		column = new HtmlColumn(DisplayType.CSV);
		assertThat(column.getEnabledDisplayTypes()).contains(DisplayType.CSV);
		assertThat(column.isHeaderColumn()).isFalse();
	}

	@Test
	public void should_create_header_column_with_id() {
		column = new HtmlColumn(true, "content");
		column.getColumnConfiguration().setId("fakeId");
		assertThat(column.isHeaderColumn()).isTrue();
		assertThat(column.toHtml().toString()).isEqualTo(
				"<" + column.getTag() + " id=\"" + column.getColumnConfiguration().getId() + "\">content</"
						+ column.getTag() + ">");
	}
	
	@Test
	public void should_create_header_column_with_content() {
		column = new HtmlColumn(true, "content");
		assertThat(column.isHeaderColumn()).isTrue();
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + ">content</" + column.getTag() + ">");
	}

	@Test
	public void should_create_column_with_content() {
		column = new HtmlColumn(false, "content");
		assertThat(column.isHeaderColumn()).isFalse();
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + ">content</" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_one_class() {
		createHtmlCellTag();
		column.addCssCellClass("aClass");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " class=\"aClass\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_several_classes() {
		createHtmlCellTag();
		column.addCssCellClass("oneClass");
		column.addCssCellClass("twoClass");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " class=\"oneClass twoClass\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_one_style() {
		createHtmlCellTag();
		column.addCssCellStyle("border:1px");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " style=\"border:1px\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_several_styles() {
		createHtmlCellTag();
		column.addCssCellStyle("border:1px");
		column.addCssCellStyle("align:center");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " style=\"border:1px;align:center\"></" + column.getTag() + ">");
	}
}