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
package com.github.dandelion.datatables.core.asset;


/**
 * <p>
 * POJO for a JS file, which will be served by the Dandelion-datatables servlet to the
 * client.
 * <p>
 * Only the JsResource (or main JS file) which handles the main DataTables
 * configuration is organized. Several attributes are used :
 * 
 * <ul>
 * <li>beforeAll</li>
 * <li>beforeStartDocumentReady</li>
 * <li>afterStartDocumentReady</li>
 * <li>beforeEndDocumentReady</li>
 * <li>afterAll</li>
 * <li>dataTablesConf</li>
 * <li>dataTablesExtra</li>
 * <li>dataTablesExtraConf</li>
 * </ul>
 * These attributes can be visualized in the following Javascript snippet :
 * 
 * <pre>
 * => <b>BEFOREALL</b>
 * var oTable_tableId;
 * var oTable_tableId_params = {<b>DATATABLESCONF</b>};
 * => <b>BEFORESTARTDOCUMENTREADY</b>
 * $(document).ready(function(){
 *    => <b>AFTERSTARTDOCUMENTREADY</b>
 *    oTable_myTableId = $('#myTableId').dataTable(oTable_myTableId_params).<b>DATATABLESEXTRA</b>({<b>DATATABLESEXTRACONF</b>});
 *    => <b>BEFOREENDDOCUMENTREADY</b>
 * });
 * => <b>AFTERALL</b>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class JsResource {

	private static final String INDENTATION = "   ";

	/**
	 * Name of the JS file.
	 */
	private String name;

	/**
	 * Content, i.e. Javascript code.
	 */
	private String content;

	/**
	 * Location of the JS file. Used if multiple files must be merged (e.g. from
	 * classpath) before being served by the servlet.
	 */
	private String location;

	/**
	 * Table to initialize. Only used if the current JsResource has the MAIN
	 * ResourceType.
	 */
	private String tableId;

	/**
	 * Type of the JS resource.
	 */
	private ResourceType type;

	private StringBuilder beforeAll;
	private StringBuilder beforeStartDocumentReady;
	private StringBuilder afterStartDocumentReady;
	private StringBuilder beforeEndDocumentReady;
	private StringBuilder afterAll;
	private StringBuilder dataTablesConf;
	private StringBuilder dataTablesExtra;
	private StringBuilder dataTablesExtraConf;

	/**
	 * Public default constructor.
	 */
	public JsResource() {
	}

	public JsResource(ResourceType type, String name) {
		this.type = type;
		this.name = name;
	}

	public JsResource(ResourceType type, String name, String location) {
		this.type = type;
		this.name = name;
		this.location = location;
	}

	/**
	 * <p>
	 * Returns the content of the Js resource, i.e. the Javascript code used by
	 * DataTables.
	 * 
	 * @return the Javascript code contained in the current Js resource.
	 */
	public String getContent() {

		StringBuilder retval = new StringBuilder();

		switch (type) {
		case MAIN:

			if (this.beforeAll != null) {
				retval.append(this.beforeAll);
				retval.append(";\n");
			}

			retval.append("var oTable_");
			retval.append(this.tableId);
			retval.append(";\n");

			retval.append("var oTable_");
			retval.append(this.tableId);
			retval.append("_params = ");
			retval.append(this.dataTablesConf);
			retval.append(";\n");

			if (this.beforeStartDocumentReady != null) {
				retval.append(this.beforeStartDocumentReady);
			}

			retval.append("$(document).ready(function(){\n");
			if (this.afterStartDocumentReady != null) {
				retval.append("\n");
				retval.append(INDENTATION);
				retval.append(this.afterStartDocumentReady);
			}
			
			// DataTables initialization
			retval.append(INDENTATION);
			retval.append("oTable_");
			retval.append(this.tableId);
			retval.append(" = $('#");
			retval.append(this.tableId);
			retval.append("').dataTable(oTable_");
			retval.append(this.tableId);
			retval.append("_params)");
			if (this.dataTablesExtra != null) {
				retval.append(".");
				retval.append(this.dataTablesExtra);
				retval.append("(");
				if (this.dataTablesExtraConf != null) {
					retval.append(this.dataTablesExtraConf);
				}
				retval.append(")");
			}
			retval.append(";");


			if (this.beforeEndDocumentReady != null) {
				retval.append("\n");
				retval.append(INDENTATION);
				retval.append(this.beforeEndDocumentReady);
			}

			retval.append("\n});");

			if (this.afterAll != null) {
				retval.append("\n");
				retval.append(this.afterAll);
			}

			break;

		case EXTENSION:
			retval.append(this.content);
			break;

		case PLUGIN:
			retval.append(this.content);
			break;

		case AGGREGATE:
			retval.append(this.content);
			break;

		case MINIMIFIED:
			retval.append(this.content);
			break;

		default:
			retval.append(this.content);
			break;
		}

		return retval.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public StringBuilder getBeforeAll() {
		return beforeAll;
	}

	public void appendToBeforeAll(String beforeAll) {
		if (this.beforeAll == null) {
			this.beforeAll = new StringBuilder();
		}
		this.beforeAll.append(beforeAll);
	}

	public StringBuilder getBeforeStartDocumentReady() {
		return beforeStartDocumentReady;
	}

	public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady) {
		if (this.beforeStartDocumentReady == null) {
			this.beforeStartDocumentReady = new StringBuilder();
		}
		this.beforeStartDocumentReady.append(beforeStartDocumentReady);
	}

	public StringBuilder getAfterStartDocumentReady() {
		return afterStartDocumentReady;
	}

	public void appendToAfterStartDocumentReady(String afterStartDocumentReady) {
		if (this.afterStartDocumentReady == null) {
			this.afterStartDocumentReady = new StringBuilder();
		}
		this.afterStartDocumentReady.append(afterStartDocumentReady);
	}

	public StringBuilder getBeforeEndDocumentReady() {
		return beforeEndDocumentReady;
	}

	public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady) {
		if (this.beforeEndDocumentReady == null) {
			this.beforeEndDocumentReady = new StringBuilder();
		}
		this.beforeEndDocumentReady.append(beforeEndDocumentReady);
	}

	public StringBuilder getAfterAll() {
		return afterAll;
	}

	public void appendToAfterAll(String afterAll) {
		if (this.afterAll == null) {
			this.afterAll = new StringBuilder();
		}
		this.afterAll.append(afterAll);
	}

	public StringBuilder getDataTablesConf() {
		return dataTablesConf;
	}

	public void appendToDataTablesConf(String dataTablesConf) {
		if (this.dataTablesConf == null) {
			this.dataTablesConf = new StringBuilder();
		}
		this.dataTablesConf.append(dataTablesConf);
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public StringBuilder getDataTablesExtra() {
		return dataTablesExtra;
	}

	public void appendToDataTablesExtra(String dataTablesExtra) {
		if (this.dataTablesExtra == null) {
			this.dataTablesExtra = new StringBuilder();
		}
		this.dataTablesExtra.append(dataTablesExtra);
	}

	public StringBuilder getDataTablesExtraConf() {
		return dataTablesExtraConf;
	}

	public void appendToDataTablesExtraConf(String dataTablesExtraConf) {
		if (this.dataTablesExtraConf == null) {
			this.dataTablesExtraConf = new StringBuilder();
		}
		this.dataTablesExtraConf.append(dataTablesExtraConf);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
}