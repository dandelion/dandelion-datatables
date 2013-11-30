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
package com.github.dandelion.datatables.core.export;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.mock.Mock;
import com.github.dandelion.datatables.core.mock.Person;

/**
 * Base class used to unit test export.
 *
 * @author Thibault Duchateau
 */
public class ExportTest {

	protected HtmlTable table;
	protected ByteArrayOutputStream baos;
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	private HttpServletRequest request;
	private MockHttpServletResponse response;
	
	@Before
	public void setup(){
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
	}
	
	public void initDefaultTable(){
		
		// Data
		table = new HtmlTable("dummyId", request, response);
		table.addFooterRow();
		table.addHeaderRow();
		table.getLastHeaderRow().addColumn("Id");
		table.getLastHeaderRow().addColumn("FirstName");
		table.getLastHeaderRow().addColumn("LastName");
		table.getLastHeaderRow().addColumn("City");
		table.getLastHeaderRow().addColumn("Mail");
		for(Person person : Mock.persons){
			table.addRow();
			table.getLastBodyRow().addColumn(String.valueOf(person.getId()));
			table.getLastBodyRow().addColumn(person.getFirstName());
			table.getLastBodyRow().addColumn(person.getLastName());
			table.getLastBodyRow().addColumn(person.getAddress() != null ? person.getAddress().getTown().getName() : "");
			table.getLastBodyRow().addColumn(person.getMail());
		}
		
		table.getTableConfiguration().setInternalObjectType(Mock.persons.get(0).getClass().getSimpleName());
	}
	
	public void initTable(){
	
		// Data
		table = new HtmlTable("dummyId", request, response);
		table.addFooterRow();
		table.addHeaderRow();
		table.getLastHeaderRow().addColumn("Id");
		table.getLastHeaderRow().addColumn("FirstName");
		table.getLastHeaderRow().addColumn("LastName", DisplayType.HTML);
		table.getLastHeaderRow().addColumn("City");
		table.getLastHeaderRow().addColumn("Mail");
		for(Person person : Mock.persons){
			table.addRow();
			table.getLastBodyRow().addColumn(String.valueOf(person.getId()));
			table.getLastBodyRow().addColumn(person.getFirstName());
			table.getLastBodyRow().addColumn(person.getLastName(), DisplayType.HTML);
			table.getLastBodyRow().addColumn(person.getAddress() != null ? person.getAddress().getTown().getName() : "");
			table.getLastBodyRow().addColumn(person.getMail());
		}
		
		table.getTableConfiguration().setInternalObjectType(Mock.persons.get(0).getClass().getSimpleName());
	}
	
	public void configureExport(ExportConf exportConf){
		if(table.getTableConfiguration().getExportConfs() == null){
			table.getTableConfiguration().setExportConfs(new HashSet<ExportConf>());
		}
		table.getTableConfiguration().getExportConfs().add(exportConf);
	}
	
	public void processExport(DatatablesExport export) {
		baos = new ByteArrayOutputStream();
		export.initExport(table);
		export.processExport(baos);
	}
}