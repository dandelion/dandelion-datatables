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
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.asset.web.WebConstants;
import com.github.dandelion.datatables.core.export.CsvExport;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.HtmlTableBuilder;
import com.github.dandelion.datatables.core.mock.Mock;
import com.github.dandelion.datatables.core.mock.Person;

public class HtmlTableBuilderTest {

	private HtmlTable table;
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	private HttpServletRequest request;
	private ExportConf fakeExportConf;
	
	@Before
	public void setup(){
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));
		fakeExportConf = new ExportConf.Builder("csv")
			.header(true)
			.exportClass(new CsvExport())
			.build();
	}
	
	@Test
	public void should_have_only_one_column(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("id").title("Id")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(Mock.persons.size());
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(1);
		assertThat(table.getHeadRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("Id");
	}
	
	@Test
	public void should_have_only_one_column_with_formatted_content(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("id", "=> {0}").title("Id")
				.build();
		
		assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("=> 1");
		assertThat(table.getBodyRows().get(1).getColumns().get(0).getContent().toString()).isEqualTo("=> 2");
	}
	
	@Test
	public void should_have_only_one_column_with_a_property_and_a_string(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("id").and("content").title("Id")
				.build();
		
		assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("1content");
		assertThat(table.getBodyRows().get(1).getColumns().get(0).getContent().toString()).isEqualTo("2content");
	}
	
	@Test
	public void should_have_only_one_column_with_two_properties_and_a_string(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("id").and("content").andProperty("id").title("Id")
				.build();
		
		assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("1content1");
		assertThat(table.getBodyRows().get(1).getColumns().get(0).getContent().toString()).isEqualTo("2content2");
	}
	
	@Test
	public void should_have_only_one_column_with_default_value(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("address.town.name").title("Town")
				.build();
		
		assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEmpty();
		assertThat(table.getBodyRows().get(1).getColumns().get(0).getContent().toString()).isEqualTo("Denny");
	}
	
	@Test
	public void should_have_two_columns(){
		table = new HtmlTableBuilder<Person>().newBuilder("tableId", Mock.persons, request, fakeExportConf)
				.column().fillWithProperty("id").title("Id")
				.column().fillWithProperty("firstName").title("FirstName")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(1000);
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(2);
		assertThat(table.getHeadRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo("Id");
		assertThat(table.getHeadRows().get(0).getColumns().get(1).getContent().toString()).isEqualTo("FirstName");
	}
}