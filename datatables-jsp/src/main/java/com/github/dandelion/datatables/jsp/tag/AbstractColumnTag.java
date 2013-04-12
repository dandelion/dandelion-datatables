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
package com.github.dandelion.datatables.jsp.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.feature.FilterType;
import com.github.dandelion.datatables.core.html.HtmlColumn;

/**
 * <p>
 * Abstract class which contains :
 * <ul>
 * <li>all the boring technical stuff needed by Java tags (getters and setters
 * for all Column tag attributes)</li>
 * <li>helper methods used to manipulate the columns</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public abstract class AbstractColumnTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractColumnTag.class);

	// Tag attributes
	protected String uid;
	protected String title;
	protected String property;
	protected String defaultValue;
	protected String cssStyle;
	protected String cssCellStyle;
	protected String cssClass;
	protected String cssCellClass;
	protected Boolean sortable;
	protected String sortDirection;
	protected String sortInit;
	protected Boolean filterable = false;
	protected Boolean searchable;
	protected String filterType;
	protected String filterCssClass = "";
	protected String filterPlaceholder = "";
	protected String display;
	protected String renderFunction;

	/**
	 * Add a column to the table when using DOM source.
	 * 
	 * @param isHeader
	 * @param content
	 */
	protected void addDomColumn(Boolean isHeader, String content) throws JspException {

		// Get the parent tag to access the HtmlTable
		AbstractTableTag parent = (AbstractTableTag) getParent();

		// Init the column
		HtmlColumn column = new HtmlColumn(isHeader, content);
		if (StringUtils.isNotBlank(this.uid)) {
			column.setUid(this.uid);
		}

		// Default value for column's content
		if(this.defaultValue != null){
			column.setDefaultValue(this.defaultValue);
		}
		
		// Sortable
		if (this.sortable != null) {
			column.setSortable(this.sortable);
		}

		// Searchable
		if (this.searchable != null) {
			column.setSearchable(this.searchable);
		}
		
		// Enabled display types
		if (StringUtils.isNotBlank(this.display)) {
			List<DisplayType> enabledDisplayTypes = new ArrayList<DisplayType>();
			String[] displayTypes = this.display.trim().toUpperCase().split(",");

			for (String displayType : displayTypes) {
				try {
					enabledDisplayTypes.add(DisplayType.valueOf(displayType));
				} catch (IllegalArgumentException e) {
					logger.error("{} is not a valid value among {}. Please choose a valid one.",
							displayType, DisplayType.values());
					throw new JspException(e);
				}
			}

			column.setEnabledDisplayTypes(enabledDisplayTypes);
		}

		// Non-header columns
		if (!isHeader) {
			if (StringUtils.isNotBlank(this.cssCellClass)) {
				column.setCssCellClass(this.cssCellClass);
			}
			if (StringUtils.isNotBlank(this.cssCellStyle)) {
				column.setCssCellStyle(this.cssCellStyle);
			}

			parent.getTable().getLastBodyRow().addColumn(column);
		}
		// Header columns
		else {
			if (StringUtils.isNotBlank(this.cssClass)) {
				column.setCssClass(new StringBuffer(this.cssClass));
			}
			if (StringUtils.isNotBlank(this.cssStyle)) {
				column.setCssStyle(new StringBuffer(this.cssStyle));
			}

			column.setSortDirection(this.sortDirection);
			column.setSortInit(this.sortInit);
			column.setFilterable(this.filterable);

			if (StringUtils.isNotBlank(this.filterType)) {

				FilterType filterType = null;
				try {
					filterType = FilterType.valueOf(this.filterType.toUpperCase().trim());
				} catch (IllegalArgumentException e) {
					logger.error("{} is not a valid value among {}. Please choose a valid one.",
							filterType, FilterType.values());
					throw new JspException(e);
				}
				column.setFilterType(filterType);
			}

			column.setFilterCssClass(this.filterCssClass);
			column.setFilterPlaceholder(this.filterPlaceholder);

			parent.getTable().getLastHeaderRow().addColumn(column);
		}
	}

	
	/**
	 * <p>
	 * Add a column to the table when using AJAX source.
	 * <p>
	 * Column are always marked as "header" using an AJAX source.
	 * 
	 * @param isHeader
	 * @param content
	 */
	protected void addAjaxColumn(Boolean isHeader, String content) throws JspException {

		// Get the parent tag to access the HtmlTable
		AbstractTableTag parent = (AbstractTableTag) getParent();

		HtmlColumn column = new HtmlColumn(true, this.title);

		// All display types are added
		for (DisplayType type : DisplayType.values()) {
			column.getEnabledDisplayTypes().add(type);
		}
		column.setProperty(property);
		
		column.setDefaultValue(StringUtils.isNotBlank(defaultValue) ? defaultValue : "");
		
		if(StringUtils.isNotBlank(this.renderFunction)){
			column.setRenderFunction(this.renderFunction);
		}
		// Sorting
		column.setSortable(this.sortable);
		column.setSortDirection(this.sortDirection);
		column.setSortInit(this.sortInit);

		// Filtering
		column.setFilterable(this.filterable);
		column.setSearchable(searchable);
		
		// Styling
		if(StringUtils.isNotBlank(cssClass)){
			column.setCssClass(new StringBuffer(this.cssClass));
		}
		if(StringUtils.isNotBlank(cssStyle)){
			column.setCssStyle(new StringBuffer(this.cssStyle));
		}
		if (StringUtils.isNotBlank(this.cssCellClass)) {
			column.setCssCellClass(this.cssCellClass);
		}
		
		// Using AJAX source, since there is only one iteration on the body,
		// we add a column in the header
		parent.getTable().getLastHeaderRow().addColumn(column);
	}
	
	protected String getColumnContent() throws JspException {

		TableTag parent = (TableTag) getParent();

		if (StringUtils.isNotBlank(property)) {

			try {
				Object propertyValue = PropertyUtils.getNestedProperty(parent.getCurrentObject(), this.property.trim());
				if(propertyValue != null){
					return propertyValue.toString();
				}
				else 
					if(StringUtils.isNotBlank(defaultValue)){
					return defaultValue.trim();
				}
			} catch (NestedNullException e) {
				if(StringUtils.isNotBlank(defaultValue)){
					return defaultValue.trim();
				}
			} 
			catch (IllegalAccessException e) {
				logger.error("Unable to get the value for the given property {}", this.property);
				throw new JspException(e);
			} catch (InvocationTargetException e) {
				logger.error("Unable to get the value for the given property {}", this.property);
				throw new JspException(e);
			} catch (NoSuchMethodException e) {
				logger.error("Unable to get the value for the given property {}", this.property);
				throw new JspException(e);
			}
		}
		
		return "";
	}

	/** Getters and setters */

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid(String uid) {
		return this.uid;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getCssCellStyle() {
		return cssCellStyle;
	}

	public void setCssCellStyle(String cssCellStyle) {
		this.cssCellStyle = cssCellStyle;
	}

	public String getCssCellClass() {
		return cssCellClass;
	}

	public void setCssCellClass(String cssCellClass) {
		this.cssCellClass = cssCellClass;
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
	
	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
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

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getRenderFunction() {
		return renderFunction;
	}

	public void setRenderFunction(String renderFunction) {
		this.renderFunction = renderFunction;
	}
}