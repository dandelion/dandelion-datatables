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
package com.github.dandelion.datatables.jsp.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.extension.feature.FilteringFeature;
import com.github.dandelion.datatables.core.html.HtmlLink;
import com.github.dandelion.datatables.core.html.HtmlScript;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * <p>
 * Abstract class which contains :
 * <ul>
 * <li>all the boring technical stuff needed by Java tags (setters for all Table
 * tag attributes)</li>
 * <li>helper methods used to init the table</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public abstract class AbstractTableTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 4788079931487986884L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractTableTag.class);

	/**
	 * Map holding the staging configuration to apply to the table.
	 */
	protected Map<Configuration, Object> localConf = null;
	
	/**
	 * First way to populate the table: using a Collection previously set in
	 * request.
	 */
	protected Object data;
	
	/**
	 * Second way to populate the table: using the URL that returns data.
	 */
	protected String url;
	
	/**
	 * Name that has been assigned with the <code>row</code> attribute for the
	 * iterated object set in the page context.
	 */
	protected String row;

	// Tag attributes
	protected String id;
	protected String rowIdBase;
	protected String rowIdPrefix;
	protected String rowIdSufix;
	protected Map<String, String> dynamicAttributes;

	// Basic features
	protected String footer;

	// Internal common attributes
	protected Integer iterationNumber;
	protected HtmlTable table;
	protected Iterator<Object> iterator;
	protected Object currentObject;
	protected String loadingType;
	protected String confGroup;
	
	/**
	 * Register activated features with the table.
	 */
	protected void registerFeatures() throws JspException {

		// Filtering feature activation
		if (table.hasOneFilterableColumn()) {

			table.getTableConfiguration().registerFeature(new FilteringFeature());
		}
	}


	/**
	 * Process the iteration over the data (only for DOM source).
	 * 
	 * @return EVAL_BODY_BUFFERED if some data remain in the Java Collection,
	 *         SKIP_BODY otherwise.
	 * @throws JspException
	 *             if something went wrong during the row id generation.
	 */
	protected int processIteration() throws JspException {

		if ("DOM".equals(this.loadingType)) {
			Integer retval = null;
			
			if(iterator != null && iterator.hasNext()){
				Object object = iterator.next();
				
				this.setCurrentObject(object);
				localConf.put(Configuration.INTERNAL_OBJECTTYPE, object.getClass().getSimpleName());
				
				if (row != null) {
					pageContext.setAttribute(row, object);
					pageContext.setAttribute(row + "_rowIndex", iterationNumber);
				}

				String rowId = getRowId();
				if (StringUtils.isNotBlank(rowId)) {
					this.table.addRow(rowId);
				} else {
					this.table.addRow();
				}
				retval = EVAL_BODY_BUFFERED;
			}
			else{
				retval = SKIP_BODY;
			}
			
			if(isFirstIteration()){
				retval = EVAL_BODY_BUFFERED;
			}
			
			return retval;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * Return the current export type asked by the user on export link click.
	 * 
	 * @return An enum corresponding to the type of export.
	 */
	protected ExportType getCurrentExportType() {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		// Get the URL parameter used to identify the export type
		String exportTypeString = request.getParameter(
				ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE).toString();

		// Convert it to the corresponding enum
		ExportType exportType = ExportType.findByUrlParameter(Integer.parseInt(exportTypeString));

		return exportType;
	}

	/**
	 * Return the row id using prefix, base and suffix. Prefix and sufix are
	 * just prepended and appended strings. Base is extracted from the current
	 * iterated object.
	 * 
	 * @return return the row id using prefix, base and suffix.
	 * @throws JspException
	 *             is the rowIdBase doesn't have a corresponding property
	 *             accessor method.
	 */
	protected String getRowId() throws JspException {

		StringBuilder rowId = new StringBuilder();

		if (StringUtils.isNotBlank(this.rowIdPrefix)) {
			rowId.append(this.rowIdPrefix);
		}

		if (StringUtils.isNotBlank(this.rowIdBase)) {
			try {
				Object propertyValue = PropertyUtils.getNestedProperty(this.currentObject, this.rowIdBase);
				rowId.append(propertyValue != null ? propertyValue : "");
			} catch (IllegalAccessException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			} catch (InvocationTargetException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			} catch (NoSuchMethodException e) {
				logger.error("Unable to get the value for the given rowIdBase {}", this.rowIdBase);
				throw new JspException(e);
			}
		}

		if (StringUtils.isNotBlank(this.rowIdSufix)) {
			rowId.append(this.rowIdSufix);
		}

		return rowId.toString();
	}

	/**
	 * Generate and write a new HTML link tag.
	 * 
	 * @param href
	 * @throws IOException
	 */
	protected void generateLinkTag(String href) throws IOException {
		pageContext.getOut().println(new HtmlLink(href).toHtml().toString());
	}

	/**
	 * Generate and write a new HTML script tag.
	 * 
	 * @param href
	 * @throws IOException
	 */
	protected void generateScriptTag(String src) throws IOException {
		pageContext.getOut().println(new HtmlScript(src).toHtml().toString());
	}

	/** Getters and setters for all attributes */

	public HtmlTable getTable() {
		return this.table;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public Boolean isFirstIteration() {
		return this.iterationNumber.equals(1);
	}

	public Integer getIterationNumber() {
		return this.iterationNumber;
	}

	public Object getCurrentObject() {
		return this.currentObject;
	}

	public void setCurrentObject(Object currentObject) {
		this.currentObject = currentObject;
	}

	public void setId(String id) {
		this.id = id;
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