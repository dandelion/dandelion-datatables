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

import com.github.dandelion.datatables.core.mock.Mock;
import com.github.dandelion.datatables.core.mock.Person;

/**
 * >> builder <<
 *
 * @author Thibault Duchateau
 */
public class HtmlTableBuilderTest {

	private HtmlTable table;
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	private HttpServletRequest request;
	
	@Before
	public void setup(){
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
	}
	
	@Test
	public void should_generate_markup_using_full_source(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(1000);
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(5);
	}
	
	@Test
	public void should_generate_markup_using_null_source(){
		table = new HtmlTable.Builder<Person>("tableId", null, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getBodyRows().size()).isEqualTo(0);
		assertThat(table.getHeadRows().get(0).getColumns().size()).isEqualTo(5);
		assertThat(table.toHtml().toString()).isEqualTo("<table id=\"tableId\"><thead><tr><th>id</th><th>firstName</th><th>lastName</th><th>address.town.name</th><th>mail</th></tr></thead><tbody></tbody></table>");
	}
	
	
	@Test
	public void should_set_column_title(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id").title("Id")
				.column("firstName").title("FirstName")
				.column("lastName").title("LastName")
				.column("address.town.name").title("City")
				.column("mail").title("Mail")
				.build();
		
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(0).getColumnConfiguration().getTitle()).isEqualTo("Id");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(1).getColumnConfiguration().getTitle()).isEqualTo("FirstName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(2).getColumnConfiguration().getTitle()).isEqualTo("LastName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(3).getColumnConfiguration().getTitle()).isEqualTo("City");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(4).getColumnConfiguration().getTitle()).isEqualTo("Mail");
	}
	
	@Test
	public void should_set_default_column_title(){
		table = new HtmlTable.Builder<Person>("tableId", Mock.persons, request)
				.column("id")
				.column("firstName")
				.column("lastName")
				.column("address.town.name")
				.column("mail")
				.build();
		
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(0).getColumnConfiguration().getTitle()).isEqualTo("id");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(1).getColumnConfiguration().getTitle()).isEqualTo("firstName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(2).getColumnConfiguration().getTitle()).isEqualTo("lastName");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(3).getColumnConfiguration().getTitle()).isEqualTo("address.town.name");
		assertThat(table.getHeadRows().get(0).getHeaderColumns().get(4).getColumnConfiguration().getTitle()).isEqualTo("mail");
	}
}