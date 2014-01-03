/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.util.HashMap;

import javax.servlet.jsp.JspException;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.jsp.extension.feature.FilteringFeature;

/**
 * <p>
 * JSP tag used for creating a table column.
 * 
 * <p>
 * Note that this tag supports dynamic attributes with only string values. See
 * {@link #setDynamicAttribute(String, String, Object)} below.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class ColumnTag extends AbstractColumnTag {

	private static final long serialVersionUID = -8928415196287387948L;

	/**
	 * Initializes a new staging configuration map and a new staging extension
	 * map to be applied to the {@link ColumnConfiguration} instance.
	 */
	public ColumnTag(){
		stagingConf = new HashMap<ConfigToken<?>, Object>();
		stagingExtension = new HashMap<ConfigToken<?>, Extension>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {
		
		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
		if(parent != null){
			return EVAL_BODY_BUFFERED;
		}
		
		throw new JspException("The tag 'column' must be inside the 'table' tag.");
	}

	/**
	 * <p>
	 * Configure the current {@link HtmlColumn}.
	 * 
	 * <p>
	 * Note that when using an AJAX source, since there is only one iteration,
	 * it just adds a header column to the last added header {@link HtmlRow}.
	 * When using a DOM source, a header {@link HtmlColumn} is added at the
	 * first iteration and a {@link HtmlColumn} is added for each iteration.
	 */
	public int doEndTag() throws JspException {
		
		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		// A header column must be added at the first iteration, regardless the
		// data source type (DOM or AJAX)
		if(parent.isFirstIteration()){

			// The 'title' attribute has precedence over 'titleKey'
			String columnTitle = StringUtils.escape(this.escapeXml, this.title);
			
			// If the 'titleKey' attribute is used, the column's title must be
			// retrieved from the current ResourceBundle
			if(columnTitle == null && titleKey != null){
				if(parent.getTable().getTableConfiguration().getInternalMessageResolver() != null){
					columnTitle = parent.getTable().getTableConfiguration().getInternalMessageResolver()
							.getResource(titleKey, StringUtils.escape(this.escapeXml, this.property), pageContext);
				}
				else{
					columnTitle = MessageResolver.UNDEFINED_KEY + titleKey + MessageResolver.UNDEFINED_KEY;
					logger.warn(
							"You cannot use the 'titleKey' attribute if no message resolver is configured. Please take a look at the {} property in the configuration reference.",
							TableConfig.I18N_MESSAGE_RESOLVER.getPropertyName());
				}
			}
			
			if ("DOM".equals(parent.getDataSourceType())) {
				addDomHeadColumn(columnTitle);
			}
			else if ("AJAX".equals(parent.getDataSourceType())) {
				addAjaxColumn(true, columnTitle);
				return EVAL_PAGE;
			}
		}

		// At this point, only DOM sources are concerned 
		if(parent.getCurrentObject() != null){

			String columnContent = null;
			// The 'property' attribute has precedence over the body of the
			// column tag
			if (getBodyContent() == null) {
				columnContent = getColumnContent();
			}
			// No 'property' attribute is used but a body is set instead
			else{
				columnContent = getBodyContent().getString().trim().replaceAll("[\n\r]", "");
			}
			addDomBodyColumn(columnContent);
		}

		return EVAL_PAGE;
	}
	
	public void setTitle(String titleKey) {
		this.title = titleKey;
	}
	
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
	
	public void setEscapeXml(boolean escapeXml) {
		this.escapeXml = escapeXml;
	}
	
	public void setUid(String uid) {
		stagingConf.put(ColumnConfig.UID, uid);
	}

	public void setName(String name) {
		stagingConf.put(ColumnConfig.NAME, name);
	}
	
	public void setProperty(String property) {
		// For DOM sources
		this.property = property;
		
		// For AJAX sources
		stagingConf.put(ColumnConfig.PROPERTY, property);
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setCssStyle(String cssStyle) {
		stagingConf.put(ColumnConfig.CSSSTYLE, cssStyle);
	}

	public void setCssClass(String cssClass) {
		stagingConf.put(ColumnConfig.CSSCLASS, cssClass);
	}

	public void setSortable(Boolean sortable) {
		stagingConf.put(ColumnConfig.SORTABLE, sortable);
	}

	public void setCssCellStyle(String cssCellStyle) {
		// For DOM sources
		this.cssCellStyle = cssCellStyle;
		
		// For AJAX sources
		stagingConf.put(ColumnConfig.CSSCELLSTYLE, cssCellStyle);
	}

	public void setCssCellClass(String cssCellClass) {
		// For DOM sources
		this.cssCellClass = cssCellClass;
		
		// For AJAX sources
		stagingConf.put(ColumnConfig.CSSCELLCLASS, cssCellClass);
	}

	public void setFilterable(Boolean filterable) {
		stagingConf.put(ColumnConfig.FILTERABLE, filterable);
		stagingExtension.put(ColumnConfig.FILTERABLE, new FilteringFeature());
	}

	public void setSearchable(Boolean searchable) {
		stagingConf.put(ColumnConfig.SEARCHABLE, searchable);
	}

	public void setVisible(Boolean visible) {
		stagingConf.put(ColumnConfig.VISIBLE, visible);
	}
	
	public void setFilterType(String filterType) {
		stagingConf.put(ColumnConfig.FILTERTYPE, filterType);
	}

	public void setFilterValues(String filterValues) {
		stagingConf.put(ColumnConfig.FILTERVALUES, filterValues);
	}

	public void setFilterCssClass(String filterCssClass) {
		stagingConf.put(ColumnConfig.FILTERCSSCLASS, filterCssClass);
	}

	public void setFilterPlaceholder(String filterPlaceholder) {
		stagingConf.put(ColumnConfig.FILTERPLACEHOLDER, filterPlaceholder);
	}

	public void setSortDirection(String sortDirection) {
		stagingConf.put(ColumnConfig.SORTDIRECTION, sortDirection);
	}

	public void setSortInitDirection(String sortInitDirection) {
		stagingConf.put(ColumnConfig.SORTINITDIRECTION, sortInitDirection);
	}

	public void setSortInitOrder(String sortInitOrder) {
		stagingConf.put(ColumnConfig.SORTINITORDER, sortInitOrder);
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}

	public void setDefault(String defaultValue) {
		// For DOM sources
		this.defaultValue = defaultValue;
		
		// For AJAX sources
		stagingConf.put(ColumnConfig.DEFAULTVALUE, defaultValue);
	}
	
	public void setRenderFunction(String renderFunction) {
		stagingConf.put(ColumnConfig.RENDERFUNCTION, renderFunction);
	}

	public void setSelector(String selector) {
		stagingConf.put(ColumnConfig.SELECTOR, selector);
	}

	public void setSortType(String sortType) {
		stagingConf.put(ColumnConfig.SORTTYPE, sortType);
	}

	public void setFilterMinLength(Integer filterMinLength) {
		stagingConf.put(ColumnConfig.FILTERMINLENGTH, filterMinLength);
	}
	
	public void setId(String id) {
		stagingConf.put(ColumnConfig.ID, id);
	}
}