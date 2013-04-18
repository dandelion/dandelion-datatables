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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.github.dandelion.datatables.core.asset.DisplayType;

/**
 * Plain old HTML <code>tr</code> tag.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlRow extends HtmlTag {

	/**
	 * List of columns (<code>td</code> tag) inside the row (<code>tr</code>
	 * tag)
	 */
	private List<HtmlColumn> columns = new LinkedList<HtmlColumn>();

	public HtmlRow() {
		this.tag = "tr";
	}

	public HtmlRow(String id) {
		this.tag = "tr";
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuilder toHtml() {
		StringBuilder html = new StringBuilder();
		html.append(getHtmlOpeningTag());
		html.append(getHtmlColumns());
		html.append(getHtmlClosingTag());
		return html;
	}
	
	private StringBuilder getHtmlColumns() {
		StringBuilder html = new StringBuilder();
		for (HtmlColumn column : this.columns) {
			if (column.getEnabledDisplayTypes().contains(DisplayType.HTML)) {
				html.append(column.toHtml());
			}
		}
		return html;
	}

	public List<HtmlColumn> getColumns() {
		return this.columns;
	}

	public void setColumns(List<HtmlColumn> columns) {
		this.columns = columns;
	}

	public HtmlColumn addHeaderColumn(HtmlColumn headerColumn){
		this.columns.add(headerColumn);
		return headerColumn;
	}
	
	public HtmlColumn addHeaderColumn(String columnContent) {
		HtmlColumn newColumn = new HtmlColumn(true, columnContent);
		this.columns.add(newColumn);
		return newColumn;
	}

	public HtmlColumn addColumn(HtmlColumn column) {
		this.columns.add(column);
		return column;
	}

	public HtmlColumn addColumn(String columnContent) {
		HtmlColumn newColumn = new HtmlColumn(false, columnContent);
		this.columns.add(newColumn);
		return newColumn;
	}

	public HtmlRow addHeaderColumns(String... columns) {
		for (String columnContent : columns) {
			addHeaderColumn(columnContent);
		}
		return this;
	}

	public List<HtmlColumn> getHeaderColumns() {
		List<HtmlColumn> retval = new ArrayList<HtmlColumn>();
		for (HtmlColumn column : columns) {
		        if (column.isHeaderColumn()) {
		                retval.add(column);
		        }
		}
		return retval;
	}

	public HtmlRow addColumns(String... columns) {
		for (String columnContent : columns) {
			addColumn(columnContent);
		}
		return this;
	}
	
	public HtmlColumn getLastColumn() {
		return ((LinkedList<HtmlColumn>) this.columns).getLast();
	}

	@Override
	public String toString() {
		return "HtmlRow [columns=" + columns + "]";
	}
}