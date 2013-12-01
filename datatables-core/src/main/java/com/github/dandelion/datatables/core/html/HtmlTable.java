/*
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dandelion.datatables.core.configuration.TableConfiguration;

/**
 * Plain old HTML <code>table</code> tag.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlTable extends HtmlTag {

	// Internal attributes
	private String originalId;
	private HtmlCaption caption;
	private List<HtmlRow> head = new LinkedList<HtmlRow>();
	private List<HtmlRow> body = new LinkedList<HtmlRow>();
	private List<HtmlRow> foot = new LinkedList<HtmlRow>();
	private TableConfiguration tableConfiguration;

	public HtmlTable(String id, HttpServletRequest request, HttpServletResponse response) {
		this.tag = "table";
		this.originalId = id;
		this.id = processId(id);
		tableConfiguration = TableConfiguration.getInstance(request);
		tableConfiguration.setTableId(id);
	}

	public HtmlTable(String id, HttpServletRequest request, HttpServletResponse response, String groupName) {
		this.tag = "table";
		this.originalId = id;
		this.id = processId(id);
		tableConfiguration = TableConfiguration.getInstance(request, groupName);
		tableConfiguration.setTableId(id);
	}

	public HtmlTable(String id) {
		this.tag = "table";
		this.originalId = id;
		this.id = processId(id);
	}

	public HtmlTable(String id, HttpServletRequest request, HttpServletResponse response, String groupName, Map<String, String> dynamicAttributes) {
		this.tag = "table";
		this.originalId = id;
		this.id = processId(id);
		this.dynamicAttributes = dynamicAttributes;
		tableConfiguration = TableConfiguration.getInstance(request, groupName);
		tableConfiguration.setTableId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuilder toHtml() {
		StringBuilder html = new StringBuilder();
		html.append(getHtmlOpeningTag());
		html.append(getHtmlHeader());
		html.append(getHtmlBody());
		html.append(getHtmlFooter());
		html.append(getHtmlClosingTag());
		return html;
	}

	private StringBuilder getHtmlHeader() {
		StringBuilder html = new StringBuilder();
		if (this.caption != null) {
			html.append(this.caption.toHtml());
		}
		html.append("<thead>");
		for (HtmlRow row : this.head) {
			html.append(row.toHtml());
		}
		html.append("</thead>");
		return html;
	}

	private StringBuilder getHtmlBody() {
		StringBuilder html = new StringBuilder();
		html.append("<tbody>");
		for (HtmlRow row : this.body) {
			html.append(row.toHtml());
		}
		html.append("</tbody>");
		return html;
	}

	private StringBuilder getHtmlFooter() {
		StringBuilder html = new StringBuilder();
		if (!this.foot.isEmpty()) {
			html.append("<tfoot>");
			for (HtmlRow row : this.foot) {
				html.append(row.toHtml());
			}

			html.append("</tfoot>");
		}
		return html;
	}

	protected StringBuilder getHtmlAttributes() {
		StringBuilder html = new StringBuilder();
		html.append(writeAttribute("id", this.originalId));
		html.append(writeAttribute("class", this.tableConfiguration.getCssClass()));
		html.append(writeAttribute("style", this.tableConfiguration.getCssStyle()));
		return html;
	}

	public HtmlCaption getCaption() {
		return caption;
	}

	public void setCaption(HtmlCaption caption) {
		this.caption = caption;
	}

	public List<HtmlRow> getHeadRows() {
		return head;
	}

	public List<HtmlRow> getBodyRows() {
		return body;
	}

	public HtmlRow addHeaderRow() {
		HtmlRow row = new HtmlRow();
		this.head.add(row);
		return row;
	}

	public HtmlRow addRow() {
		HtmlRow row = new HtmlRow();
		this.body.add(row);
		return row;
	}

	public HtmlRow addFooterRow() {
		HtmlRow row = new HtmlRow();
		this.foot.add(row);
		return row;
	}

	public HtmlRow addRow(String rowId) {
		HtmlRow row = new HtmlRow(rowId);
		this.body.add(row);
		return row;
	}

	public HtmlTable addRows(HtmlRow... rows) {
		for (HtmlRow row : rows) {
			this.body.add(row);
		}
		return this;
	}

	public HtmlRow getLastFooterRow() {
		return ((LinkedList<HtmlRow>) this.foot).getLast();
	}

	public HtmlRow getFirstHeaderRow() {
		return ((LinkedList<HtmlRow>) this.head).getFirst();
	}
	
	public HtmlRow getLastHeaderRow() {
		return ((LinkedList<HtmlRow>) this.head).getLast();
	}

	public HtmlRow getLastBodyRow() {
		return ((LinkedList<HtmlRow>) this.body).getLast();
	}

	public void addCssStyle(String cssStyle) {
		if(this.tableConfiguration.getCssStyle() == null) {
			this.tableConfiguration.setCssStyle(new StringBuilder());
		} else {
			this.tableConfiguration.getCssStyle().append(CSS_SEPARATOR);
		}
		
		this.tableConfiguration.getCssStyle().append(cssStyle);
	}
	
	public void addCssClass(String cssClass) {
		if(this.tableConfiguration.getCssClass() == null) {
			this.tableConfiguration.setCssClass(new StringBuilder());
		} else {
			this.tableConfiguration.getCssClass().append(CLASS_SEPARATOR);
		}
		
		this.tableConfiguration.getCssClass().append(cssClass);
	}
	
	public HtmlColumn getColumnHeadByUid(String uid) {
		for (HtmlRow row : this.head) {
			for (HtmlColumn column : row.getColumns()) {
				if (column.isHeaderColumn() != null && column.isHeaderColumn()
						&& column.getColumnConfiguration().getUid() != null
						&& column.getColumnConfiguration().getUid().equals(uid)) {
					return column;
				}
			}

		}
		return null;
	}

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public void setTableConfiguration(TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;
	}
	
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	private String processId(String id){
		return id.replaceAll("[^A-Za-z0-9 ]", "");
	}
}