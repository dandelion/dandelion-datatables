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

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import com.github.dandelion.datatables.core.configuration.ConfigurationLoader;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Plain old HTML <code>table</code> tag.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlTable extends HtmlTag {

	// Internal attributes
	private HtmlCaption caption;
	private List<HtmlRow> head = new LinkedList<HtmlRow>();
	private List<HtmlRow> body = new LinkedList<HtmlRow>();
	private List<HtmlRow> foot = new LinkedList<HtmlRow>();
	private TableConfiguration tableConfiguration;
	private String randomId;

	public HtmlTable(String id, HttpServletRequest request) {
		this.tag = "table";
		this.randomId = StringUtils.getRamdomNumber();
		this.id = id;
		tableConfiguration = TableConfiguration.getInstance(request);
		tableConfiguration.setTableId(id);
	}

	public HtmlTable(String id, HttpServletRequest request, String groupName) {
		this.tag = "table";
		this.randomId = StringUtils.getRamdomNumber();
		this.id = id;
		tableConfiguration = TableConfiguration.getInstance(request, groupName);
		tableConfiguration.setTableId(id);
	}

	public HtmlTable(String id) {
		this.tag = "table";
		this.randomId = StringUtils.getRamdomNumber();
		this.id = id;
	}

	public HtmlTable(String id, HttpServletRequest request, String groupName, Map<String, String> dynamicAttributes) {
		this.tag = "table";
		this.randomId = StringUtils.getRamdomNumber();
		this.id = id;
		this.dynamicAttributes = dynamicAttributes;
		tableConfiguration = TableConfiguration.getInstance(request, groupName);
		tableConfiguration.setTableId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuilder toHtml() {
		if("fadein".equals(this.tableConfiguration.getExtraAppear())){
			addCssStyle("display:none;");
		}
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
		html.append(writeAttribute("id", this.id));
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

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public TableConfiguration getTableConfiguration() {
		return tableConfiguration;
	}

	public void setTableConfiguration(TableConfiguration tableConfiguration) {
		this.tableConfiguration = tableConfiguration;
	}

	/**
	 * HtmlTable builder, allowing you to build a table in an export controller
	 * for example.
	 */
	public static class Builder<T> {

		private String id;
		private List<T> data;
		private LinkedList<HtmlColumn> headerColumns = new LinkedList<HtmlColumn>();
		private HttpServletRequest request;
		private ExportConf exportConf;

		public Builder(String id, List<T> data, HttpServletRequest request) {
			this.id = id;
			this.data = data;
			this.request = request;
		}

		// Table configuration
		
		public Builder<T> column(String property) {
			HtmlColumn column = new HtmlColumn(true, "");

			column.getColumnConfiguration().setProperty(property);
			column.getColumnConfiguration().setTitle(property);
			headerColumns.add(column);
			return this;
		}

		public Builder<T> title(String title) {
			headerColumns.getLast().getColumnConfiguration().setTitle(title);
			return this;
		}

		public Builder<T> format(String pattern) {
			headerColumns.getLast().getColumnConfiguration().setFormat(pattern);
			return this;
		}

		public Builder<T> defaultContent(String defaultContent) {
			headerColumns.getLast().getColumnConfiguration().setDefaultValue(defaultContent);
			return this;
		}
		
		public Builder<T> configureExport(ExportConf exportConf) {
			this.exportConf = exportConf;
			return this;
		}

		public HtmlTable build() {
			return new HtmlTable(this);
		}
	}

	/**
	 * Private constructor used by the Builder to build a HtmlTable in a fluent
	 * way.
	 * 
	 * @param builder
	 */
	private <T> HtmlTable(Builder<T> builder) {

		this.tag = "table";
		this.id = builder.id;
		this.tableConfiguration = TableConfiguration
				.getInstance(builder.request, ConfigurationLoader.DEFAULT_GROUP_NAME);
		
		this.tableConfiguration.setExportConfs(new HashSet<ExportConf>(Arrays.asList(builder.exportConf)));
		
		if(builder.data != null && builder.data.size() > 0){
			this.tableConfiguration.setInternalObjectType(builder.data.get(0).getClass().getSimpleName());
		}
		else{
			this.tableConfiguration.setInternalObjectType("???");
		}
		
		addHeaderRow();

		for (HtmlColumn column : builder.headerColumns) {
			column.setContent(new StringBuilder(column.getColumnConfiguration().getTitle()));
			getLastHeaderRow().addColumn(column);
		}

		if (builder.data != null) {

			for (T o : builder.data) {

				addRow();
				for (HtmlColumn headerColumn : builder.headerColumns) {

					Object content = null;
					try {

						content = PropertyUtils.getNestedProperty(o, headerColumn.getColumnConfiguration().getProperty().toString().trim());

						// If a format exists, we format the property
						if (StringUtils.isNotBlank(headerColumn.getColumnConfiguration().getFormat()) && content != null) {

							MessageFormat messageFormat = new MessageFormat(headerColumn.getColumnConfiguration().getFormat());
							content = messageFormat.format(new Object[] { content });
						} else if (StringUtils.isBlank(headerColumn.getColumnConfiguration().getFormat()) && content != null) {
							content = content.toString();
						} else {
							if (StringUtils.isNotBlank(headerColumn.getColumnConfiguration().getDefaultValue())) {
								content = headerColumn.getColumnConfiguration().getDefaultValue().trim();

							}
						}
					} catch (NestedNullException e) {
						if (StringUtils.isNotBlank(headerColumn.getColumnConfiguration().getDefaultValue())) {
							content = headerColumn.getColumnConfiguration().getDefaultValue().trim();
						}
					} catch (IllegalAccessException e) {
						content = "";
					} catch (InvocationTargetException e) {
						content = "";
					} catch (NoSuchMethodException e) {
						content = "";
					} catch (IllegalArgumentException e) {
						content = "";
					}

					getLastBodyRow().addColumn(String.valueOf(content));
				}
			}
		}
	}
}