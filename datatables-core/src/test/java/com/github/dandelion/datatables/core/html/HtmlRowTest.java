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

import org.junit.Before;
import org.junit.Test;

import com.github.dandelion.datatables.core.export.ReservedFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlRowTest {

	private HtmlRow row;
	private HtmlColumn headerColumn;
	private HtmlColumn column1;
	private HtmlColumn column2;

	@Before
	public void createHtmlTag() {
		 row = new HtmlRow();
	}

	@Test
	public void should_generate_row_with_id() {
		row = new HtmlRow("myId");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + " id=\"myId\"></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_one_column() {
		row.addColumn("ColumnContent");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><td>ColumnContent</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_several_columns() {
		row.addColumns("ColumnContent1", "ColumnContent2");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><td>ColumnContent1</td><td>ColumnContent2</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_one_header_column() {
		row.addHeaderColumn("ColumnContent");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnContent</th></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_several_header_columns() {
		row.addHeaderColumns("ColumnContent1", "ColumnContent2");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnContent1</th><th>ColumnContent2</th></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_composed_columns() {
		populateColumns();
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnHeaderContent</th><td>ColumnContent1</td><td>ColumnContent2</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_without_column_without_HTML_DisplayType() {
		row.addColumn(new HtmlColumn(ReservedFormat.CSV));
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "></" + row.getTag() + ">");
	}
	
	@Test
	public void should_get_header_columns() {
		populateColumns();
		assertThat(row.getHeaderColumns()).containsExactly(headerColumn);
	}
	
	@Test
	public void should_get_last_column() {
		populateColumns();
		assertThat(row.getLastColumn()).isEqualTo(column2);
	}

	private void populateColumns() {
		headerColumn = new HtmlColumn(true, "ColumnHeaderContent");
		column1 = new HtmlColumn(false, "ColumnContent1");
		column2 = new HtmlColumn(false, "ColumnContent2");
		row.addHeaderColumn(headerColumn);
		row.addColumn(column1);
		row.addColumn(column2);
	}
}