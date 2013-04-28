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
import java.util.List;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.constants.Direction;
import com.github.dandelion.datatables.core.feature.FilterType;

/**
 * Plain old HTML <code>td</code> and <code>th</code> tags.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class HtmlColumn extends HtmlTagWithContent {

	private String uid;
	private Boolean isHeaderColumn;
	private StringBuilder cssCellClass;
	private StringBuilder cssCellStyle;
	private Boolean sortable;
	private List<String> sortDirections;
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
		setHeaderColumn(false);
	};

	public HtmlColumn(DisplayType displayType) {
		init();
		this.enabledDisplayTypes.clear();
		this.enabledDisplayTypes.add(displayType);
		setHeaderColumn(false);
	};
	
	public HtmlColumn(Boolean isHeader) {
		init();
		setHeaderColumn(isHeader);
	};

	public HtmlColumn(Boolean isHeader, String content) {
		init();
		setHeaderColumn(isHeader);
		if(content != null) {
			setContent(new StringBuilder(content));
		}
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
		this.enabledDisplayTypes.add(DisplayType.ALL);
	}
	
	@Override
	protected StringBuilder getHtmlAttributes() {
		StringBuilder html = new StringBuilder();
		html.append(writeAttribute("id", this.id));
		if (this.isHeaderColumn) {
			html.append(writeAttribute("class", this.cssClass));
			html.append(writeAttribute("style", this.cssStyle));
		} else {
			html.append(writeAttribute("class", this.cssCellClass));
			html.append(writeAttribute("style", this.cssCellStyle));
		}
		return html;
	}
	
	private void setHeaderColumn(Boolean isHeaderColumn) {
		this.isHeaderColumn = isHeaderColumn;
		if(this.isHeaderColumn) {
			this.tag = "th";
		} else {
			this.tag = "td";
		}
	}

	public Boolean isHeaderColumn() {
		return isHeaderColumn;
	}

	public StringBuilder getCssCellClass() {
		return cssClass;
	}

	public void setCssCellClass(StringBuilder cssCellClass) {
		this.cssCellClass = cssCellClass;
	}

	public StringBuilder getCssCellStyle() {
		return cssCellStyle;
	}

	public void setCssCellStyle(StringBuilder cssCellStyle) {
		this.cssCellStyle = cssCellStyle;
	}

	public void addCssCellClass(String cssCellClass) {
		if(this.cssCellClass == null) {
			this.cssCellClass = new StringBuilder();
		} else {
			this.cssCellClass.append(CLASS_SEPARATOR);
		}
		this.cssCellClass.append(cssCellClass);
	}

	public void addCssCellStyle(String cssCellStyle) {
		if(this.cssCellStyle == null) {
			this.cssCellStyle = new StringBuilder();
		} else {
			this.cssCellStyle.append(CSS_SEPARATOR);
		}
		this.cssCellStyle.append(cssCellStyle);
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

	public List<String> getSortDirections() {
		return sortDirections;
	}

	public void setSortDirections(List<String> sortDirections) {
		this.sortDirections = sortDirections;
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
				+ ", sortable=" + sortable + ", sortDirections=" + sortDirections + ", sortInit="
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