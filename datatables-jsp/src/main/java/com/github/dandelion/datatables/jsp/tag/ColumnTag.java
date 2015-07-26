/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.util.OptionUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.config.ConfigLoader;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
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
 * 
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
 * 
 * @author Thibault Duchateau
 * @author Enrique Ruiz
 * @since 0.1.0
 */
public class ColumnTag extends BodyTagSupport implements DynamicAttributes {

   private static final long serialVersionUID = -7564020161222133531L;

   private static Logger logger = LoggerFactory.getLogger(ColumnTag.class);

   /**
    * Maps holding the staging configuration to apply to the column.
    */
   private Map<Option<?>, Object> stagingOptions;
   private Map<Option<?>, Extension> stagingExtensions;

   /**
    * Title of the column.
    */
   private String title;

   /**
    * Title key of the column, used in combination with a configured message
    * resolver.
    */
   private String titleKey;

   /**
    * Name of the property to be extracted from the data source.
    */
   private String property;

   /**
    * Text to be displayed when the property of the data source is null.
    */
   private String defaultValue;

   /**
    * CSS style to be applied on each cell.
    */
   private String cssCellStyle;

   /**
    * CSS class(es) to be applied on each cell.
    */
   private String cssCellClass;

   /**
    * MessageFormat to be applied to the property of the data source (DOM source
    * only).
    */
   private String format;

   /**
    * List of format where the contents of the column must be displayed
    */
   private String display;

   /**
    * Whether XML characters should be escaped
    */
   private boolean escapeXml = true;

   private HttpServletRequest request;

   /**
    * The map of dynamic attributes that will be set as-is on the table tag.
    */
   private Map<String, String> dynamicAttributes;

   private HtmlColumn headerColumn;

   /**
    * <p>
    * Initializes a new staging options map and a new staging extension map to
    * be applied to the {@link ColumnConfiguration} instance.
    * </p>
    */
   public ColumnTag() {
      this.stagingOptions = new HashMap<Option<?>, Object>();
      this.stagingExtensions = new HashMap<Option<?>, Extension>();
   }

   @Override
   public int doStartTag() throws JspException {

      this.request = (HttpServletRequest) this.pageContext.getRequest();

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
      if (parent != null) {

         // On the first iteration, a header cell must be added
         if (parent.isFirstIteration()) {
            this.headerColumn = new HtmlColumn(true, null, this.dynamicAttributes, this.display);
            this.request.setAttribute(ColumnConfiguration.class.getCanonicalName(),
                  this.headerColumn.getColumnConfiguration());
         }

         // When using a DOM source, the 'property' attribute has precedence
         // over the body, so we don't even evaluate it
         if (StringUtils.isNotBlank(this.property)) {
            return SKIP_BODY;
         }
         else {
            return EVAL_BODY_BUFFERED;
         }
      }

      throw new JspException("The tag 'column' must be inside the 'table' tag.");
   }

   /**
    * <p>
    * Configure the current {@link HtmlColumn}.
    * </p>
    * <p>
    * Note that when using an AJAX source, since there is only one iteration, it
    * just adds a header column to the last added header {@link HtmlRow}. When
    * using a DOM source, a header {@link HtmlColumn} is added at the first
    * iteration and a {@link HtmlColumn} is added for each iteration.
    * </p>
    */
   @Override
   public int doEndTag() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      // A header column must be added at the first iteration, regardless the
      // data source type (DOM or AJAX)
      if (parent.isFirstIteration()) {

         // The 'title' attribute has precedence over 'titleKey'
         String columnTitle = StringUtils.escape(this.escapeXml, this.title);

         // If the 'titleKey' attribute is used, the column's title must be
         // retrieved from the current ResourceBundle
         if (columnTitle == null && this.titleKey != null) {
            if (parent.getTable().getTableConfiguration().getMessageResolver() != null) {
               columnTitle = parent.getTable().getTableConfiguration().getMessageResolver().getResource(this.titleKey,
                     StringUtils.escape(this.escapeXml, this.property), this.pageContext);
            }
            else {
               columnTitle = MessageResolver.UNDEFINED_KEY + titleKey + MessageResolver.UNDEFINED_KEY;
               logger.warn(
                     "You cannot use the 'titleKey' attribute if no message resolver is configured. Please take a look at the {} property in the configuration reference.",
                     ConfigLoader.I18N_MESSAGE_RESOLVER);
            }
         }

         if (TableTag.SOURCE_DOM.equals(parent.getDataSourceType())) {
            addDomHeaderColumn(columnTitle);
         }
         else if (TableTag.SOURCE_AJAX.equals(parent.getDataSourceType())) {
            addAjaxHeaderColumn(true, columnTitle);
            return EVAL_PAGE;
         }
      }

      // At this point, only DOM sources are concerned
      if (parent.getCurrentObject() != null) {

         String columnContent = null;
         // The 'property' attribute has precedence over the body of the
         // column tag
         if (StringUtils.isNotBlank(this.property)) {
            columnContent = getColumnContent();
         }
         // No 'property' attribute is used but a body is set instead
         else if (getBodyContent() != null) {
            columnContent = getBodyContent().getString().trim().replaceAll("[\n\r]", "");
         }

         addDomBodyColumn(columnContent);
      }

      return EVAL_PAGE;
   }

   /**
    * <p>
    * Adds a header column to the last head row when using a DOM source.
    * </p>
    * 
    * @param content
    *           Contents of the <code>th</code> cell.
    * @throws JspException
    */
   private void addDomHeaderColumn(String content) throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      if (content != null) {
         this.headerColumn.setContent(new StringBuilder(content));
      }

      // At this point, all setters have been called and both the staging
      // configuration map and staging extension map should have been filled
      // with user configuration
      // The user configuration can now be applied to the default
      // configuration
      this.headerColumn.getColumnConfiguration().getOptions().putAll(this.stagingOptions);
      this.headerColumn.getColumnConfiguration().getStagingExtension().putAll(this.stagingExtensions);

      // Once all configuration are merged, they can be processed
      OptionUtils.processOptions(this.headerColumn.getColumnConfiguration().getOptions(), this.request);

      parent.getTable().getLastHeaderRow().addColumn(this.headerColumn);
   }

   /**
    * <p>
    * Adds a body column to the last body row when using a DOM source.
    * </p>
    * 
    * @param content
    *           Content of the <code>td</code> cell.
    * @throws JspException
    */
   private void addDomBodyColumn(String content) throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      HtmlColumn bodyColumn = new HtmlColumn(false, content, this.dynamicAttributes, this.display);

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
    * Adds a header column to the table when using AJAX source.
    * </p>
    * <p>
    * Column are always marked as "header" using an AJAX source.
    * </p>
    * 
    * @param isHeader
    * @param content
    */
   private void addAjaxHeaderColumn(Boolean isHeader, String content) throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      if (content != null) {
         this.headerColumn.setContent(new StringBuilder(content));
      }

      if (StringUtils.isNotBlank(this.defaultValue)) {
         this.headerColumn.getColumnConfiguration().getOptions().put(DatatableOptions.DEFAULTVALUE, this.defaultValue);
      }
      else {
         this.headerColumn.getColumnConfiguration().getOptions().put(DatatableOptions.DEFAULTVALUE, "");
      }

      // At this point, all setters have been called and both the staging
      // configuration map and staging extension map should have been filled
      // with user configuration
      // The user configuration can now be applied to the default
      // configuration
      this.headerColumn.getColumnConfiguration().getOptions().putAll(this.stagingOptions);
      this.headerColumn.getColumnConfiguration().getStagingExtension().putAll(this.stagingExtensions);

      // Once all configuration are merged, they can be processed
      OptionUtils.processOptions(this.headerColumn.getColumnConfiguration().getOptions(), this.request);

      parent.getTable().getLastHeaderRow().addColumn(this.headerColumn);
   }

   /**
    * <p>
    * Returns the column content when using a DOM source.
    * </p>
    * 
    * @return the content to be displayed in the column.
    * @throws JspException
    *            if something went wrong during the access to the bean's
    *            property or during the message formatting.
    */
   private String getColumnContent() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      if (StringUtils.isNotBlank(this.property) && parent.getCurrentObject() != null) {

         Object propertyValue = null;
         try {
            propertyValue = PropertyUtils.getNestedProperty(parent.getCurrentObject(), this.property.trim());

            // If a MessageFormat exists, we use it to format the property
            if (StringUtils.isNotBlank(this.format) && propertyValue != null) {

               MessageFormat messageFormat = new MessageFormat(this.format);
               return messageFormat.format(new Object[] { propertyValue });
            }
            else if (StringUtils.isBlank(this.format) && propertyValue != null) {
               return StringUtils.escape(propertyValue.toString());
            }
            else {
               if (StringUtils.isNotBlank(this.defaultValue)) {
                  return this.defaultValue.trim();

               }
            }
         }
         catch (NestedNullException e) {
            if (StringUtils.isNotBlank(defaultValue)) {
               return defaultValue.trim();
            }
         }
         catch (IllegalAccessException e) {
            throw new JspException("Unable to get the value for the given property: \"" + this.property + "\"", e);
         }
         catch (InvocationTargetException e) {
            throw new JspException("Unable to get the value for the given property: \"" + this.property + "\"", e);
         }
         catch (NoSuchMethodException e) {
            throw new JspException("Unable to get the value for the given property: \"" + this.property + "\"", e);
         }
         catch (IllegalArgumentException e) {
            logger.error("Wrong MessageFormat pattern : {}", format);
            return propertyValue.toString();
         }
      }
      else {
         return "";
      }

      return "";
   }

   @Override
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
    * </p>
    * <p>
    * The dynamic attribute must not conflict with other attributes and must
    * have a valid type.
    * </p>
    * 
    * @param localName
    *           Name of the dynamic attribute.
    * @param value
    *           Value of the dynamic attribute.
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
         throw new IllegalArgumentException(
               "The attribute " + localName + " won't be added to the table. Only string values are accepted.");
      }
   }

   public HtmlColumn getHeaderColumn() {
      return this.headerColumn;
   }

   public Map<Option<?>, Object> getStagingConf() {
      return stagingOptions;
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

   public void setName(String name) {
      stagingOptions.put(DatatableOptions.NAME, name);
   }

   public void setProperty(String property) {
      // For DOM sources
      this.property = property;

      // For AJAX sources
      stagingOptions.put(DatatableOptions.PROPERTY, property);
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public void setCssStyle(String cssStyle) {
      this.stagingOptions.put(DatatableOptions.CSSSTYLE, cssStyle);
   }

   public void setCssClass(String cssClass) {
      this.stagingOptions.put(DatatableOptions.CSSCLASS, cssClass);
   }

   public void setSortable(Boolean sortable) {
      this.stagingOptions.put(DatatableOptions.SORTABLE, sortable);
   }

   public void setCssCellStyle(String cssCellStyle) {
      // For DOM sources
      this.cssCellStyle = cssCellStyle;

      // For AJAX sources
      this.stagingOptions.put(DatatableOptions.CSSCELLSTYLE, cssCellStyle);
   }

   public void setCssCellClass(String cssCellClass) {
      // For DOM sources
      this.cssCellClass = cssCellClass;

      // For AJAX sources
      this.stagingOptions.put(DatatableOptions.CSSCELLCLASS, cssCellClass);
   }

   public void setFilterable(Boolean filterable) {
      this.stagingOptions.put(DatatableOptions.FILTERABLE, filterable);
      this.stagingExtensions.put(DatatableOptions.FILTERABLE, new FilteringFeature());
   }

   public void setSearchable(Boolean searchable) {
      this.stagingOptions.put(DatatableOptions.SEARCHABLE, searchable);
   }

   public void setVisible(Boolean visible) {
      this.stagingOptions.put(DatatableOptions.VISIBLE, visible);
   }

   public void setFilterType(String filterType) {
      this.stagingOptions.put(DatatableOptions.FILTERTYPE, filterType);
   }

   public void setFilterValues(String filterValues) {
      this.stagingOptions.put(DatatableOptions.FILTERVALUES, filterValues);
   }

   public void setFilterPlaceholder(String filterPlaceholder) {
      this.stagingOptions.put(DatatableOptions.FILTERPLACEHOLDER, filterPlaceholder);
   }

   public void setSortDirection(String sortDirection) {
      this.stagingOptions.put(DatatableOptions.SORTDIRECTION, sortDirection);
   }

   public void setSortInitDirection(String sortInitDirection) {
      this.stagingOptions.put(DatatableOptions.SORTINITDIRECTION, sortInitDirection);
   }

   public void setSortInitOrder(String sortInitOrder) {
      this.stagingOptions.put(DatatableOptions.SORTINITORDER, sortInitOrder);
   }

   public void setDisplay(String display) {
      this.display = display;
   }

   public void setDefault(String defaultValue) {
      // For DOM sources
      this.defaultValue = defaultValue;

      // For AJAX sources
      this.stagingOptions.put(DatatableOptions.DEFAULTVALUE, defaultValue);
   }

   public void setRenderFunction(String renderFunction) {
      this.stagingOptions.put(DatatableOptions.RENDERFUNCTION, renderFunction);
   }

   public void setSelector(String selector) {
      this.stagingOptions.put(DatatableOptions.SELECTOR, selector);
   }

   public void setSortType(String sortType) {
      this.stagingOptions.put(DatatableOptions.SORTTYPE, sortType);
   }

   public void setId(String id) {
      this.stagingOptions.put(DatatableOptions.ID, id);
   }
}