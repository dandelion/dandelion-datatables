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
package com.github.dandelion.datatables.core.configuration;

import java.util.List;

import com.github.dandelion.datatables.core.constants.Direction;
import com.github.dandelion.datatables.core.export.ColumnElement;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.extension.feature.SortType;

/**
 * Contains the column configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ColumnConfiguration {

	private String uid;
	private String title;
	private String titleKey;
	private String property;
	private List<ColumnElement> columnElements;
	private String defaultValue;
	private Boolean sortable = true;
	private String format;
	private List<Direction> sortDirections;
	private String sortInit;
	private SortType sortType;
	private Boolean filterable = false;
	private Boolean searchable = true;
	private Boolean visible = true;
	private FilterType filterType = FilterType.INPUT;
	private String filterValues;
	private String filterCssClass = "";
	private String filterPlaceholder = "";
	private String renderFunction;
	private String selector;

	// Only used in AJAX mode
	private String cssCellClass;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public List<Direction> getSortDirections() {
		return sortDirections;
	}

	public void setSortDirections(List<Direction> sortDirections) {
		this.sortDirections = sortDirections;
	}

	public String getSortInit() {
		return sortInit;
	}

	public void setSortInit(String sortInit) {
		this.sortInit = sortInit;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public Boolean getFilterable() {
		return filterable;
	}

	public void setFilterable(Boolean filterable) {
		this.filterable = filterable;
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public String getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(String filterValues) {
		this.filterValues = filterValues;
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

	public String getRenderFunction() {
		return renderFunction;
	}

	public void setRenderFunction(String renderFunction) {
		this.renderFunction = renderFunction;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public List<ColumnElement> getColumnElements() {
		return columnElements;
	}

	public void setColumnElements(List<ColumnElement> columnElements) {
		this.columnElements = columnElements;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCssCellClass() {
		return cssCellClass;
	}

	public void setCssCellClass(String cssCellClass) {
		this.cssCellClass = cssCellClass;
	}

	@Override
	public String toString() {
		return "ColumnConfiguration [uid=" + uid + ", title=" + title + ", titleKey=" + titleKey + ", property="
				+ property + ", columnElements=" + columnElements + ", defaultValue=" + defaultValue + ", sortable="
				+ sortable + ", sortDirections=" + sortDirections + ", sortInit=" + sortInit + ", sortType=" + sortType
				+ ", filterable=" + filterable + ", searchable=" + searchable + ", visible=" + visible
				+ ", filterType=" + filterType + ", filterValues=" + filterValues + ", filterCssClass="
				+ filterCssClass + ", filterPlaceholder=" + filterPlaceholder + ", renderFunction=" + renderFunction
				+ ", selector=" + selector + "]";
	}
}