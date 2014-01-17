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
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlColumn;

/**
 * <p>
 * Superclass of {@link ColumnTag} containing:
 * <ul>
 * <li>tag attributes declaration (note that all the corresponding setters are
 * in the {@link ColumnTag}</li>
 * <li>helper attributes and methods used to initialize the column</li>
 * </ul>
 * 
 * <p>
 * Note that this tag supports dynamic attributes with only string values. See
 * {@link #setDynamicAttribute(String, String, Object)} below.
 * 
 * @author Thibault Duchateau
 * @author Enrique Ruiz
 * @since 0.1.0
 */
public abstract class AbstractColumnTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 1L;

	// Logger
	protected static Logger logger = LoggerFactory.getLogger(ColumnTag.class);

	/**
	 * Map holding the staging configuration to apply to the column.
	 */
	protected Map<ConfigToken<?>, Object> stagingConf;
	protected Map<ConfigToken<?>, Extension> stagingExtension;

	/**
	 * Tag attributes
	 */
	// Title of the column
	protected String title;

	// Title key of the column (used in combination with a configured message
	// resolver)
	protected String titleKey;

	// Name of the property to be extracted from data source
	protected String property;

	// Value to be displayed when the data source is null
	protected String defaultValue;

	// CSS style to be applied on each cell
	protected String cssCellStyle;
	
	// CSS class(es) to be applied on each cell
	protected String cssCellClass;
	
	// MessageFormat to be applied to the property (DOM source only)
	protected String format;

	// List of format where the column's content must be displayed
	protected String display;

	// Whether XML characters should be escaped
	protected boolean escapeXml = true;

	/**
	 * Internal attributes
	 */
	protected Map<String, String> dynamicAttributes;

	/**
	 * <p>
	 * Adds a head column to the last head row when using a DOM source.
	 * 
	 * @param content
	 *            Content of the <code>th</code> cell.
	 * @throws JspException
	 */
	protected void addDomHeadColumn(String content) throws JspException {

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		HtmlColumn headColumn = new HtmlColumn(true, content, dynamicAttributes, display);

		// At this point, all setters have been called and both the staging
		// configuration map and staging extension map should have been filled
		// with user configuration
		// The user configuration can now be applied to the default
		// configuration
		ColumnConfig.applyConfiguration(stagingConf, stagingExtension, headColumn);
		
		// Once all configuration are merged, they can be processed
		ColumnConfig.processConfiguration(headColumn, parent.getTable());

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

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		HtmlColumn bodyColumn = new HtmlColumn(false, content, dynamicAttributes, display);

		// Note that these attributes are not handled via the ColumnConfig
		// object because a ColumnConfiguration is only attached to header
		// columns
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
	 * Add a header column to the table when using AJAX source.
	 * <p>
	 * Column are always marked as "header" using an AJAX source.
	 * 
	 * @param isHeader
	 * @param content
	 */
	protected void addAjaxColumn(Boolean isHeader, String content) throws JspException {

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		HtmlColumn headColumn = new HtmlColumn(true, content, dynamicAttributes);

		ColumnConfig.DEFAULTVALUE.setIn(headColumn.getColumnConfiguration(),
				StringUtils.isNotBlank(defaultValue) ? defaultValue : "");

		// At this point, all setters have been called and both the staging
		// configuration map and staging extension map should have been filled
		// with user configuration
		// The user configuration can now be applied to the default
		// configuration
		ColumnConfig.applyConfiguration(stagingConf, stagingExtension, headColumn);
		
		// Once all configuration are merged, they can be processed
		ColumnConfig.processConfiguration(headColumn, parent.getTable());

		parent.getTable().getLastHeaderRow().addColumn(headColumn);
	}

	/**
	 * <p>
	 * Returns the column content when using a DOM source.
	 * 
	 * @return the content to be displayed in the column.
	 * @throws JspException
	 *             if something went wrong during the access to the bean's
	 *             property or during the message formatting.
	 */
	protected String getColumnContent() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		if (StringUtils.isNotBlank(property) && parent.getCurrentObject() != null) {

			Object propertyValue = null;
			try {
				propertyValue = PropertyUtils.getNestedProperty(parent.getCurrentObject(), this.property.trim());

				// If a MessageFormat exists, we use it to format the property
				if (StringUtils.isNotBlank(format) && propertyValue != null) {

					MessageFormat messageFormat = new MessageFormat(format);
					return messageFormat.format(new Object[] { propertyValue });
				} else if (StringUtils.isBlank(format) && propertyValue != null) {
					return propertyValue.toString();
				} else {
					if (StringUtils.isNotBlank(defaultValue)) {
						return defaultValue.trim();

					}
				}
			} catch (NestedNullException e) {
				if (StringUtils.isNotBlank(defaultValue)) {
					return defaultValue.trim();
				}
			} catch (IllegalAccessException e) {
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
		} else {
			return "";
		}

		return "";
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
					"The 'class' attribute is not allowed. Please use the 'cssClass' or the 'cssCellClass' attribute instead.");
		}
		if (localName.equals("style")) {
			throw new IllegalArgumentException(
					"The 'style' attribute is not allowed. Please use the 'cssStyle' or the 'cssCellStyle' attribute instead.");
		}
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("The attribute " + localName
					+ " won't be added to the table. Only string values are accepted.");
		}
	}
}