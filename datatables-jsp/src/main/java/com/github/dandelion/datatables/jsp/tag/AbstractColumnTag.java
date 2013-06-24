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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.constants.Direction;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.util.StringUtils;

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
public abstract class AbstractColumnTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 1L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractColumnTag.class);

	// Tag attributes
	protected String uid;
	protected String title;
	protected String titleKey;
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
	protected Boolean visible;
	protected String filterType;
	protected String filterCssClass = "";
	protected String filterPlaceholder = "";
	protected String display;
	protected String renderFunction;
	protected String format;
	protected String selector;
	protected Map<String, String> dynamicAttributes;

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
		HtmlColumn column = new HtmlColumn(isHeader, content, dynamicAttributes);
		
		// UID
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
		
		// Visible
		if (this.visible!= null) {
			column.setVisible(this.visible);
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
				column.addCssCellClass(this.cssCellClass);
			}
			if (StringUtils.isNotBlank(this.cssCellStyle)) {
				column.addCssCellStyle(this.cssCellStyle);
			}

			parent.getTable().getLastBodyRow().addColumn(column);
		}
		// Header columns
		else {
			if (StringUtils.isNotBlank(this.cssClass)) {
				column.setCssClass(new StringBuilder(this.cssClass));
			}
			if (StringUtils.isNotBlank(this.cssStyle)) {
				column.setCssStyle(new StringBuilder(this.cssStyle));
			}

			// Sort direction
			if (StringUtils.isNotBlank(sortDirection)) {
				List<String> sortDirections = new ArrayList<String>();
				String[] sortDirectionArray = sortDirection.trim().toUpperCase().split(",");

				for (String direction : sortDirectionArray) {
					try {
						sortDirections.add(Direction.valueOf(direction).getValue());
					} catch (IllegalArgumentException e) {
						logger.error("{} is not a valid value among {}. Please choose a valid one.",
								direction, Direction.values());
						throw new JspException(e);
					}
				}

				column.setSortDirections(sortDirections);
			}
			
			column.setSortInit(this.sortInit);
			column.setFilterable(this.filterable);

			if(StringUtils.isNotBlank(this.selector)){
				column.setSelector(this.selector);
			}
			
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

		HtmlColumn column = new HtmlColumn(true, this.title, dynamicAttributes);

		// UID
		if (StringUtils.isNotBlank(this.uid)) {
			column.setUid(this.uid);
		}
				
		column.setProperty(property);
		
		column.setDefaultValue(StringUtils.isNotBlank(defaultValue) ? defaultValue : "");
		
		if(StringUtils.isNotBlank(this.renderFunction)){
			column.setRenderFunction(this.renderFunction);
		}
		// Sorting
		if(sortable != null){
			column.setSortable(this.sortable);
		}
		if(sortDirection != null){
			
			// Sort direction
			if (StringUtils.isNotBlank(sortDirection)) {
				List<String> sortDirections = new ArrayList<String>();
				String[] sortDirectionArray = sortDirection.trim().toUpperCase().split(",");

				for (String direction : sortDirectionArray) {
					try {
						sortDirections.add(Direction.valueOf(direction).getValue());
					} catch (IllegalArgumentException e) {
						logger.error("{} is not a valid value among {}. Please choose a valid one.",
								direction, Direction.values());
						throw new JspException(e);
					}
				}

				column.setSortDirections(sortDirections);
			}
		}
		if(sortInit != null){
			column.setSortInit(this.sortInit);
		}

		// Filtering
		if(filterable != null){
			column.setFilterable(filterable);
		}
		
		if(searchable != null){
			column.setSearchable(searchable);
		}

		if(StringUtils.isNotBlank(this.selector)){
			column.setSelector(this.selector);
		}
		
		// Visible
		if (this.visible!= null) {
			column.setVisible(this.visible);
		}
		
		// Styling
		if(StringUtils.isNotBlank(cssClass)){
			column.setCssClass(new StringBuilder(this.cssClass));
		}
		if(StringUtils.isNotBlank(cssStyle)){
			column.setCssStyle(new StringBuilder(this.cssStyle));
		}
		
		// Exporting
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
		
		// Using AJAX source, since there is only one iteration on the body,
		// we add a column in the header
		parent.getTable().getLastHeaderRow().addColumn(column);
	}
	
	
	/**
	 * TODO
	 * @return
	 * @throws JspException
	 */
	protected String getColumnContent() throws JspException {

		TableTag parent = (TableTag) getParent();

		if (StringUtils.isNotBlank(property) && parent.getCurrentObject() != null) {

			Object propertyValue = null;
			try {
				propertyValue = PropertyUtils.getNestedProperty(parent.getCurrentObject(), this.property.trim());

				// If a format exists, we format the property
				if(StringUtils.isNotBlank(format) && propertyValue != null){
					
					MessageFormat messageFormat = new MessageFormat(format);
					return messageFormat.format(new Object[]{propertyValue});
				}
				else if(StringUtils.isBlank(format) && propertyValue != null){
					return propertyValue.toString();
				}
				else{
					if(StringUtils.isNotBlank(defaultValue)){
						return defaultValue.trim();
					
					}
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
			} catch (IllegalArgumentException e) {
	            logger.error("Wrong MessageFormat pattern : {}", format);
	            return propertyValue.toString();
	        }
		}
		else{
			return "";
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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * Get the map of dynamic attributes.
	 */
	protected Map<String, String> getDynamicAttributes() {
		return this.dynamicAttributes;
        }

	/** {@inheritDoc} */
	public void setDynamicAttribute(String uri, String localName, Object value ) 
		throws JspException {
		if (this.dynamicAttributes == null) {
			this.dynamicAttributes = new HashMap<String, String>();
		}
		if (!isValidDynamicAttribute(localName, value)) {
			throw new IllegalArgumentException("Attribute "
				.concat(localName).concat("=\"")
				.concat(String.valueOf(value))
				.concat("\" is not allowed"));
		}

		// Accept String values only, because we haven't knowledge
		// about how to transform Object to String
		if(value instanceof String) {
		    dynamicAttributes.put(localName, (String) value);
		}
	}

	/**
	 * Whether the given name-value pair is a valid dynamic attribute.
	 */
	protected boolean isValidDynamicAttribute(String localName, Object value) {
		return true;
	}
}