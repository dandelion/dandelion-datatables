/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.locator.impl.DelegateLocator;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.configuration.DatatableBundles;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.generator.javascript.JavascriptGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Superclass of {@link TableTag} containing:
 * <ul>
 * <li>tag attributes declaration (note that all the corresponding setters are
 * in the {@link TableTag}</li>
 * <li>helper attributes and methods used to initialize the table</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @author Enrique Ruiz
 * @since 0.1.0
 */
public abstract class AbstractTableTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 4788079931487986884L;

	// Logger
	protected static Logger logger = LoggerFactory.getLogger(TableTag.class);

	/**
	 * Tag attributes
	 */
	// Table id
	protected String id;

	// First way to populate the table: using a Collection
	protected Object data;

	// Second way to populate the table: using the URL that returns data
	protected String url;

	// Name that has been assigned for the iterated object set in the page
	// context
	protected String row;

	// Name of the configuration group to be applied to the table
	protected String confGroup;

	// Used when an id is to be assigned to each row
	protected String rowIdBase;
	protected String rowIdPrefix;
	protected String rowIdSuffix;

	// Whether XML characters should be escaped
	protected boolean escapeXml = true;
	
	/**
	 * Internal attributes
	 */
	// Map containing the staging configuration to be applied to the table at
	// the end of the tag processing
	protected Map<ConfigToken<?>, Object> stagingConf;
	protected Integer iterationNumber;
	protected HtmlTable table;
	protected Iterator<Object> iterator;
	protected Object currentObject;
	protected String dataSourceType;
	protected Map<String, String> dynamicAttributes;
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	/**
	 * <p>
	 * Process the iteration over the collection of data.
	 * 
	 * <p>
	 * Note that no iteration is required when using an AJAX source.
	 * 
	 * @return {@code EVAL_BODY_BUFFERED} if some data remain in the Java
	 *         Collection, {@code SKIP_BODY} otherwise.
	 * @throws JspException
	 *             if something went wrong during the row id generation.
	 */
	protected int processIteration() throws JspException {

		if ("DOM".equals(this.dataSourceType)) {
			Integer retval = null;

			if (iterator != null && iterator.hasNext()) {
				Object object = iterator.next();

				this.setCurrentObject(object);
				stagingConf.put(TableConfig.INTERNAL_OBJECTTYPE, object.getClass().getSimpleName());

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
			} else {
				retval = SKIP_BODY;
			}

			if (isFirstIteration()) {
				retval = EVAL_BODY_BUFFERED;
			}

			return retval;
		} else {
			return SKIP_BODY;
		}
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
			rowId.append(StringUtils.escape(this.escapeXml, this.rowIdPrefix));
		}

		if (StringUtils.isNotBlank(this.rowIdBase)) {
			try {
				Object propertyValue = PropertyUtils.getNestedProperty(this.currentObject,
						StringUtils.escape(this.escapeXml, this.rowIdBase));
				rowId.append(propertyValue != null ? propertyValue : "");
			} catch (IllegalAccessException e) {
				throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
			} catch (InvocationTargetException e) {
				throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
			} catch (NoSuchMethodException e) {
				throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
			}
		}

		if (StringUtils.isNotBlank(this.rowIdSuffix)) {
			rowId.append(StringUtils.escape(this.escapeXml, this.rowIdSuffix));
		}

		return rowId.toString();
	}

	/**
	 * Set up the HTML table generation.
	 * 
	 * @return allways EVAL_PAGE to keep evaluating the page.
	 * @throws JspException
	 *             if something went wrong during the processing.
	 */
	protected int setupHtmlGeneration() throws JspException {

		JsResource jsResource = null;

		this.table.getTableConfiguration().setExporting(false);

		try {
			// Init the web resources generator
			WebResourceGenerator contentGenerator = new WebResourceGenerator(table);

			// Generate the web resources (JS, CSS) and wrap them into a
			// WebResources POJO
			jsResource = contentGenerator.generateWebResources();
			logger.debug("Web content generated successfully");

			// Asset stack update
			AssetRequestContext
					.get(request)
					.addBundles(DatatableBundles.DDL_DT)
					.addBundles(DatatableBundles.DATATABLES)
					.addParameter("dandelion-datatables", DelegateLocator.DELEGATED_CONTENT_PARAM,
							DatatablesConfigurator.getJavascriptGenerator(), false);

			// Buffering generated Javascript
			JavascriptGenerator javascriptGenerator = AssetRequestContext.get(request).getParameterValue(
					"dandelion-datatables", DelegateLocator.DELEGATED_CONTENT_PARAM);
			javascriptGenerator.addResource(jsResource);

			// HTML generation
			pageContext.getOut().println(this.table.toHtml());

		} catch (IOException e) {
			throw new JspException("Unable to generate the HTML markup for the table " + id, e);
		}

		return EVAL_PAGE;
	}

	/**
	 * Set up the export properties, before the filter intercepts the response.
	 * 
	 * @return always {@code SKIP_PAGE}, because the export filter will override
	 *         the response with the exported data instead of displaying the
	 *         page.
	 * @throws JspException
	 *             if something went wrong during export.
	 */
	protected int setupExport() throws JspException {

		String currentExportType = ExportUtils.getCurrentExportType(request);

		this.table.getTableConfiguration().setExporting(true);
		this.table.getTableConfiguration().setCurrentExportFormat(currentExportType);

		try {
			// Call the export delegate
			ExportDelegate exportDelegate = new ExportDelegate(table, request);
			exportDelegate.prepareExport();

		} catch (ExportException e) {
			logger.error("Something went wront with the Dandelion export configuration.");
			throw new JspException(e);
		}

		response.reset();

		return SKIP_PAGE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {

		validateDynamicAttribute(localName, value);

		if (this.dynamicAttributes == null) {
			this.dynamicAttributes = new HashMap<String, String>();
		}

		this.dynamicAttributes.put(localName, (String) value);
	}
	
	/**
	 * <p>
	 * Validates the passed dynamic attribute.
	 * 
	 * <p>
	 * The dynamic attribute must not conflict with other attributes and must
	 * have a valid type.
	 * 
	 * @param localName
	 *            Name of the dynamic attribute.
	 * @param value
	 *            Value of the dynamic attribute.
	 */
	private void validateDynamicAttribute(String localName, Object value) {
		if (localName.equals("class")) {
			throw new IllegalArgumentException(
					"The 'class' attribute is not allowed. Please use the 'cssClass' attribute instead.");
		}
		if (localName.equals("style")) {
			throw new IllegalArgumentException(
					"The 'style' attribute is not allowed. Please use the 'cssStyle' attribute instead.");
		}
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("The attribute " + localName
					+ " won't be added to the table. Only string values are accepted.");
		}
	}

	public HtmlTable getTable() {
		return this.table;
	}

	public boolean isFirstIteration() {
		return this.iterationNumber.equals(1);
	}

	public int getIterationNumber() {
		return this.iterationNumber;
	}

	public Object getCurrentObject() {
		return this.currentObject;
	}

	public void setCurrentObject(Object currentObject) {
		this.currentObject = currentObject;
	}

	public String getDataSourceType() {
		return this.dataSourceType;
	}
}