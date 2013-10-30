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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.jsp.extension.feature.FilteringFeature;

/**
 * <p>
 * Abstract class which contains :
 * <ul>
 * <li>all the boring technical stuff needed by Java tags (setters for all Column
 * tag attributes)</li>
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

	/**
	 * Map holding the staging configuration to apply to the column.
	 */
	protected Map<Configuration, Object> stagingConf;
	
	// Tag attributes
	protected String title;
	protected String titleKey;
	protected String property;
	protected String defaultValue;
	protected String cssStyle;
	protected String cssCellStyle;
	protected String cssClass;
	protected String cssCellClass;
	protected String format;
	protected String display;
	protected Map<String, String> dynamicAttributes;

	/**
	 * <p>
	 * Adds a head column to the last head row whren using a DOM source.
	 * 
	 * @param content
	 *            Content of the <code>th</code> cell.
	 * @throws JspException
	 */
	protected void addDomHeadColumn(String content) throws JspException {
		
		// Get the parent tag to access the HtmlTable
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		// TODO For sake of consistency, cssClass and cssStyle attributes should
		// be handled directly via the ColumnConfiguration
		HtmlColumn headColumn = new HtmlColumn(true, content, dynamicAttributes, display);
		if (StringUtils.isNotBlank(this.cssClass)) {
			headColumn.setCssClass(new StringBuilder(this.cssClass));
		}
		if (StringUtils.isNotBlank(this.cssStyle)) {
			headColumn.setCssStyle(new StringBuilder(this.cssStyle));
		}
		
		try {
			Configuration.applyColumnConfiguration(headColumn.getColumnConfiguration(), parent.getTable().getTableConfiguration(), stagingConf);
		} catch (ConfigurationProcessingException e) {
			throw new JspException(e);
		} catch (ConfigurationLoadingException e) {
			throw new JspException(e);
		}
		
		// TODO The FilteringFeature cannot be registered in a dedicated
		// core processor because it's an abstract feature. Implementations only
		// exist in datatables-jsp and datatables-thymeleaf, not in
		// datatables-core
		if(headColumn.getColumnConfiguration().getFilterable()){
			parent.getTable().getTableConfiguration().registerExtension(new FilteringFeature());
		}
		
		parent.getTable().getLastHeaderRow().addColumn(headColumn);
	}
	
	/**
	 * <p>
	 * Adds a body column to the last body row when using a DOM source.
	 * 
	 * @param content
	 *            Content of the <code>td</code> cell.
	 * @param content
	 * @throws JspException
	 */
	protected void addDomBodyColumn(String content) throws JspException {
		
		// Get the parent tag to access the HtmlTable
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);
		
		HtmlColumn bodyColumn = new HtmlColumn(false, content, dynamicAttributes, display);

		if (StringUtils.isNotBlank(this.cssCellClass)) {
			bodyColumn.addCssCellClass(this.cssCellClass);
		}
		if (StringUtils.isNotBlank(this.cssCellStyle)) {
			bodyColumn.addCssCellStyle(this.cssCellStyle);
		}
		
		parent.getTable().getLastBodyRow().addColumn(bodyColumn);
	}

	
	/**
	 * <p>
	 * Add a head column to the table when using AJAX source.
	 * <p>
	 * Column are always marked as "header" using an AJAX source.
	 * 
	 * @param isHeader
	 * @param content
	 */
	protected void addAjaxColumn(Boolean isHeader, String content) throws JspException {

		// Get the parent tag to access the HtmlTable
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		HtmlColumn headColumn = new HtmlColumn(true, content, dynamicAttributes);
		if (StringUtils.isNotBlank(this.cssClass)) {
			headColumn.setCssClass(new StringBuilder(this.cssClass));
		}
		if (StringUtils.isNotBlank(this.cssStyle)) {
			headColumn.setCssStyle(new StringBuilder(this.cssStyle));
		}
		headColumn.getColumnConfiguration().setDefaultValue(StringUtils.isNotBlank(defaultValue) ? defaultValue : "");
		
		try {
			Configuration.applyColumnConfiguration(headColumn.getColumnConfiguration(), parent.getTable().getTableConfiguration(), stagingConf);
		} catch (ConfigurationProcessingException e) {
			throw new JspException(e);
		} catch (ConfigurationLoadingException e) {
			throw new JspException(e);
		}
		
		// TODO The FilteringFeature cannot be registered in a dedicated
		// processor because it's an abstract feature. Implementations only
		// exist in datatables-jsp and datatables-thymeleaf
		if(headColumn.getColumnConfiguration().getFilterable()){
			parent.getTable().getTableConfiguration().registerExtension(new FilteringFeature());
		}
				
		parent.getTable().getLastHeaderRow().addColumn(headColumn);
	}
	
	
	/**
	 * <p>Return the column content following some rules.
	 * <p>TODO
	 * @return the content of the column.
	 * @throws JspException
	 */
	protected String getColumnContent() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

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

	public void setUid(String uid) {
		stagingConf.put(Configuration.COLUMN_UID, uid);
	}

	public void setProperty(String property) {
		this.property = property;
		stagingConf.put(Configuration.COLUMN_PROPERTY, property);
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setSortable(Boolean sortable) {
		stagingConf.put(Configuration.COLUMN_SORTABLE, sortable);
	}

	public void setCssCellStyle(String cssCellStyle) {
		this.cssCellStyle = cssCellStyle;
	}

	public void setCssCellClass(String cssCellClass) {
		this.cssCellClass = cssCellClass;
	}

	public void setFilterable(Boolean filterable) {
		stagingConf.put(Configuration.COLUMN_FILTERABLE, filterable);
	}

	public void setSearchable(Boolean searchable) {
		stagingConf.put(Configuration.COLUMN_SEARCHABLE, searchable);
	}

	public void setVisible(Boolean visible) {
		stagingConf.put(Configuration.COLUMN_VISIBLE, visible);
	}
	
	public void setFilterType(String filterType) {
		stagingConf.put(Configuration.COLUMN_FILTERTYPE, filterType);
	}

	public void setFilterValues(String filterValues) {
		stagingConf.put(Configuration.COLUMN_FILTERVALUES, filterValues);
	}

	public void setFilterCssClass(String filterCssClass) {
		stagingConf.put(Configuration.COLUMN_FILTERCSSCLASS, filterCssClass);
	}

	public void setFilterPlaceholder(String filterPlaceholder) {
		stagingConf.put(Configuration.COLUMN_FILTERPLACEHOLDER, filterPlaceholder);
	}

	public void setSortDirection(String sortDirection) {
		stagingConf.put(Configuration.COLUMN_SORTDIRECTION, sortDirection);
	}

	public void setSortInit(String sortInit) {
		stagingConf.put(Configuration.COLUMN_SORTINIT, sortInit);
	}

	public void setDisplay(String display) {
//		stagingConf.put(Configuration.COLUMN_DISPLAY, display);
		this.display = display;
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
		stagingConf.put(Configuration.COLUMN_DEFAULTVALUE, defaultValue);
	}
	
	public void setRenderFunction(String renderFunction) {
		stagingConf.put(Configuration.COLUMN_RENDERFUNCTION, renderFunction);
	}

	public void setFormat(String format) {
		this.format = format;
		stagingConf.put(Configuration.COLUMN_FORMAT, format);
	}

	public void setSelector(String selector) {
		stagingConf.put(Configuration.COLUMN_SELECTOR, selector);
	}

	public void setSortType(String sortType) {
		stagingConf.put(Configuration.COLUMN_SORTTYPE, sortType);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titleKey) {
		this.title = titleKey;
	}
	
	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
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