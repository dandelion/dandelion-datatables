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
package com.github.dandelion.datatables.core.model;

import java.util.ArrayList;
import java.util.List;

import com.github.dandelion.datatables.core.constants.FilterType;

/**
 * Plain old HTML <code>td</code> and <code>th</code> tags.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlColumn extends HtmlTag {

	private String uid;
	private Boolean isHeaderColumn;
	private String cssCellClass;
	private String cssCellStyle;
	private String content;
	private Boolean sortable;
	private String sortDirection;
	private String sortInit;
	private String property;
	private String defaultValue;
	private Boolean filterable;
	private Boolean searchable;
	private String renderFunction;

	/**
	 * <p>
	 * Type of filtering for the current column.
	 * 
	 * <p>
	 * Default value is <code>FilterType.INPUT</code>.
	 */
	private FilterType filterType;
	private String filterCssClass;
	private String filterPlaceholder;
	// TODO remove instanciation
	private List<DisplayType> enabledDisplayTypes = new ArrayList<DisplayType>();

	public HtmlColumn() {
		init();
		this.isHeaderColumn = false;
	};

	public HtmlColumn(Boolean isHeader) {
		init();
		this.isHeaderColumn = isHeader;
	};

	public HtmlColumn(Boolean isHeader, String content) {
		init();
		this.isHeaderColumn = isHeader;
		this.content = content;
	}

	/**
	 * Default values
	 */
	private void init() {
		// Sortable
		this.sortable = true;

		// Filterable
		this.filterable = false;

		// Searchable
		this.searchable = true;
		
		// FilterType
		this.filterType = FilterType.INPUT;

		// DisplayType default value : all display types are added
		for (DisplayType type : DisplayType.values()) {
			this.enabledDisplayTypes.add(type);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer toHtml() {
		StringBuffer html = new StringBuffer();
		if (this.isHeaderColumn) {
			html.append("<th");

			if (this.cssClass != null) {
				html.append(" class=\"");
				html.append(this.cssClass);
				html.append("\"");
			}

			if (this.cssStyle != null) {
				html.append(" style=\"");
				html.append(this.cssStyle);
				html.append("\"");
			}
		} else {
			html.append("<td");

			if (this.cssCellClass != null) {
				html.append(" class=\"");
				html.append(this.cssCellClass);
				html.append("\"");
			}

			if (this.cssCellStyle != null) {
				html.append(" style=\"");
				html.append(this.cssCellStyle);
				html.append("\"");
			}
		}

		html.append(">");
		if (this.content != null) {
			html.append(content);
		}

		if (this.isHeaderColumn) {
			html.append("</th>");
		} else {
			html.append("</td>");
		}

		return html;
	}

	public Boolean isHeaderColumn() {
		return isHeaderColumn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCssCellClass() {
		return this.cssCellClass;
	}

	public HtmlColumn setCssCellClass(String cssCellClass) {
		this.cssCellClass = cssCellClass;
		return this;
	}

	public HtmlColumn setCssCellStyle(String cssCellStyle) {
		this.cssCellStyle = cssCellStyle;
		return this;
	}

	public Boolean isSortable() {
		return this.sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(Boolean filterable) {
		this.filterable = filterable;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public String getFilterCssClass() {
		return filterCssClass;
	}

	public void setFilterCssClass(String filterCssClass) {
		this.filterCssClass = filterCssClass;
	}

	public String getFilterPlaceholder() {
		return filterPlaceholder;
	}

	public void setFilterPlaceholder(String filterPlaceholder) {
		this.filterPlaceholder = filterPlaceholder;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortInit() {
		return sortInit;
	}

	public void setSortInit(String sortInit) {
		this.sortInit = sortInit;
	}

	public List<DisplayType> getEnabledDisplayTypes() {
		return enabledDisplayTypes;
	}

	public void setEnabledDisplayTypes(List<DisplayType> enabledDisplayTypes) {
		this.enabledDisplayTypes = enabledDisplayTypes;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return "HtmlColumn [uid=" + uid + ", isHeaderColumn=" + isHeaderColumn + ", cssCellClass="
				+ cssCellClass + ", cssCellStyle=" + cssCellStyle + ", content=" + content
				+ ", sortable=" + sortable + ", sortDirection=" + sortDirection + ", sortInit="
				+ sortInit + ", property=" + property + ", defaultValue=" + defaultValue
				+ ", filterable=" + filterable + ", filterType=" + filterType + ", filterCssClass="
				+ filterCssClass + ", filterPlaceholder=" + filterPlaceholder
				+ ", enabledDisplayTypes=" + enabledDisplayTypes + ", rendererFunction="
				+ renderFunction + "]";
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public String getRenderFunction() {
		return renderFunction;
	}

	public void setRenderFunction(String rendererFunction) {
		this.renderFunction = rendererFunction;
	}
}