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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.asset.generator.js.jquery.JQueryJsContentGenerator;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.util.OptionUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.util.UrlUtils;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.DatatableComponent;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.generator.DatatableJQueryContent;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.TableConfiguration;

/**
 * <p>
 * JSP tag used for creating HTML tables.
 * </p>
 * 
 * <p>
 * Note that this tag supports dynamic attributes with only string values. See
 * {@link #setDynamicAttribute(String, String, Object)} below.
 * </p>
 * 
 * <p>
 * Usage example:
 * </p>
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
 * @author Enrique Ruiz
 * @since 0.1.0
 */
public class TableTag extends BodyTagSupport implements DynamicAttributes {

   private static final long serialVersionUID = 8770764721128212100L;

   private static Logger logger = LoggerFactory.getLogger(TableTag.class);

   public static final String SOURCE_DOM = "DOM";
   public static final String SOURCE_AJAX = "AJAX";

   /**
    * The table id.
    */
   private String id;

   /**
    * Name that has been assigned for the iterated object set in the page
    * context.
    */
   private String row;

   /**
    * Name of the configuration group to be applied to the table.
    */
   private String confGroup;

   /**
    * Used when an id is to be assigned to each row.
    */
   private String rowIdBase;
   private String rowIdPrefix;
   private String rowIdSuffix;
   private Object currentObject;

   /**
    * The iteration number when using a DOM source. Just used to identify the
    * first row (table header).
    */
   private Integer iterationNumber;

   /**
    * Whether XML characters should be escaped.
    */
   private boolean escapeXml = true;

   /**
    * Map containing the staging configuration to be applied to the table at the
    * end of the tag processing.
    */
   private Map<Option<?>, Object> stagingConf;

   /**
    * The table to be built.
    */
   private HtmlTable table;

   /**
    * The iterator to be used to feed the table with the provided DOM source.
    */
   private Iterator<Object> iterator;

   /**
    * The type of data source configured in the tag. Depending on this type, the
    * tag won't behaviour in the same way.
    */
   private String dataSourceType;

   /**
    * The map of dynamic attributes that will be set as-is on the table tag.
    */
   private Map<String, String> dynamicAttributes;

   /**
    * The current request.
    */
   private HttpServletRequest request;

   /**
    * The current response.
    */
   private HttpServletResponse response;

   /**
    * <p>
    * Initialize a new map intended to store the staging configuration to be
    * applied to table and columns.
    * </p>
    */
   public TableTag() {
      this.stagingConf = new HashMap<Option<?>, Object>();
   }

   @Override
   public int doStartTag() throws JspException {
      this.iterationNumber = 1; // Just used to identify the first row (header)
      this.request = (HttpServletRequest) this.pageContext.getRequest();
      this.response = (HttpServletResponse) this.pageContext.getResponse();

      this.table = new HtmlTable(this.id, this.request, this.response, this.confGroup, this.dynamicAttributes);
      this.request.setAttribute(TableConfiguration.class.getCanonicalName(), this.table.getTableConfiguration());

      // The table data are loaded using an AJAX source
      if (SOURCE_AJAX.equals(this.dataSourceType)) {

         this.table.addHeaderRow();

         return EVAL_BODY_BUFFERED;
      }
      // The table data are loaded using a DOM source (Collection)
      else if (SOURCE_DOM.equals(this.dataSourceType)) {

         this.table.addHeaderRow();

         return processIteration();
      }

      // Never reached
      return SKIP_BODY;
   }

   @Override
   public int doAfterBody() throws JspException {

      this.iterationNumber++;

      return processIteration();
   }

   @Override
   public int doEndTag() throws JspException {

      // At this point, all setters have been called and the staging
      // configuration map should have been filled with user configuration
      // The user configuration can now be applied to the default
      // configuration
      this.table.getTableConfiguration().getOptions().putAll(this.stagingConf);

      // Once all configuration are merged, they can be processed
      OptionUtils.processOptions(this.table.getTableConfiguration().getOptions(), this.request);

      // The table is being exported
      if (ExportUtils.isTableBeingExported(this.request, this.table)) {
         return setupExport();
      }
      // The table must be generated and displayed
      else {

         if (request.getAttribute(DatatableComponent.DDL_DT_REQUESTATTR_TABLES) == null) {
            List<HtmlTable> htmlTables = new ArrayList<HtmlTable>();
            htmlTables.add(table);
            request.setAttribute(DatatableComponent.DDL_DT_REQUESTATTR_TABLES, htmlTables);
         }
         else {
            List<HtmlTable> htmlTables = (List<HtmlTable>) request
                  .getAttribute(DatatableComponent.DDL_DT_REQUESTATTR_TABLES);
            htmlTables.add(table);
         }

         // ConfigUtils.storeTableInRequest(this.request, this.table);
         return setupHtmlGeneration();
      }
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
    * Process the iteration over the collection of data.
    * 
    * <p>
    * Note that no iteration is required when using an AJAX source.
    * 
    * @return {@code EVAL_BODY_BUFFERED} if some data remain in the Java
    *         Collection, {@code SKIP_BODY} otherwise.
    * @throws JspException
    *            if something went wrong during the row id generation.
    */
   private int processIteration() throws JspException {

      // DOM source
      if (SOURCE_DOM.equals(this.dataSourceType)) {
         Integer retval = null;

         if (this.iterator != null && this.iterator.hasNext()) {
            Object object = this.iterator.next();

            this.setCurrentObject(object);
            this.stagingConf.put(DatatableOptions.INTERNAL_OBJECTTYPE, object.getClass().getSimpleName());

            if (this.row != null) {
               this.pageContext.setAttribute(this.row, object);
               this.pageContext.setAttribute(this.row + "_rowIndex", this.iterationNumber);
            }

            String rowId = getRowId();
            if (StringUtils.isNotBlank(rowId)) {
               this.table.addRow(rowId);
            }
            else {
               this.table.addRow();
            }
            retval = EVAL_BODY_BUFFERED;
         }
         else {
            retval = SKIP_BODY;
         }

         if (isFirstIteration()) {
            retval = EVAL_BODY_BUFFERED;
         }

         return retval;
      }
      // AJAX source
      // No iteration is performed using an AJAX source. The table creation is
      // delegated to DataTables.
      else {
         return SKIP_BODY;
      }
   }

   /**
    * <p>
    * Return the row id using prefix, base and suffix. Prefix and sufix are just
    * prepended and appended strings. Base is extracted from the current
    * iterated object.
    * </p>
    * 
    * @return the row id using prefix, base and suffix.
    * @throws JspException
    *            is the rowIdBase doesn't have a corresponding property accessor
    *            method.
    */
   private String getRowId() throws JspException {

      StringBuilder rowId = new StringBuilder();

      if (StringUtils.isNotBlank(this.rowIdPrefix)) {
         rowId.append(StringUtils.escape(this.escapeXml, this.rowIdPrefix));
      }

      if (StringUtils.isNotBlank(this.rowIdBase)) {
         try {
            Object propertyValue = PropertyUtils.getNestedProperty(this.currentObject,
                  StringUtils.escape(this.escapeXml, this.rowIdBase));
            rowId.append(propertyValue != null ? propertyValue : "");
         }
         catch (IllegalAccessException e) {
            throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
         }
         catch (InvocationTargetException e) {
            throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
         }
         catch (NoSuchMethodException e) {
            throw new JspException("Unable to get the value for the given rowIdBase " + this.rowIdBase, e);
         }
      }

      if (StringUtils.isNotBlank(this.rowIdSuffix)) {
         rowId.append(StringUtils.escape(this.escapeXml, this.rowIdSuffix));
      }

      return rowId.toString();
   }

   /**
    * <p>
    * Set up the HTML table generation.
    * </p>
    * 
    * @return allways EVAL_PAGE to keep evaluating the page.
    * @throws JspException
    *            if something went wrong during the processing.
    */
   private int setupHtmlGeneration() throws JspException {

      this.table.getTableConfiguration().setExporting(false);

      // Generate the JavaScript code according to the table and its
      // configuration
      DatatableJQueryContent datatableContent = new DatatableJQueryContent(this.table);

      // Get the existing JavaScript generator or create it if it doesn't exist
      JQueryJsContentGenerator javascriptGenerator = (JQueryJsContentGenerator) AssetRequestContext.get(this.request)
            .getGenerator(DatatableComponent.COMPONENT_NAME);

      if (javascriptGenerator == null) {
         javascriptGenerator = new JQueryJsContentGenerator(datatableContent);
      }
      else {
         javascriptGenerator.appendContent(datatableContent);
      }

      // Update the asset request context with the enabled bundles and
      // Javascript generator
      AssetRequestContext.get(this.request).addBundles(DatatableBundles.DDL_DT)
            .addGenerator(DatatableComponent.COMPONENT_NAME, javascriptGenerator);

      try {
         this.pageContext.getOut().println(this.table.toHtml());
      }
      catch (IOException e) {
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
    *            if something went wrong during export.
    */
   private int setupExport() throws JspException {

      String currentExportType = ExportUtils.getCurrentExportType(request);

      this.table.getTableConfiguration().setExporting(true);
      this.table.getTableConfiguration().setCurrentExportFormat(currentExportType);

      try {
         // Call the export delegate
         ExportDelegate exportDelegate = new ExportDelegate(table, request);
         exportDelegate.prepareExport();

      }
      catch (DandelionException e) {
         logger.error("Something went wront with the Dandelion export configuration.");
         throw new JspException(e);
      }

      this.response.reset();

      return SKIP_PAGE;
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
    *           Name of the dynamic attribute.
    * @param value
    *           Value of the dynamic attribute.
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
         throw new IllegalArgumentException(
               "The attribute " + localName + " won't be added to the table. Only string values are accepted.");
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

   public Map<Option<?>, Object> getStagingConf() {
      return stagingConf;
   }

   public void setCurrentObject(Object currentObject) {
      this.currentObject = currentObject;
   }

   public String getDataSourceType() {
      return this.dataSourceType;
   }

   public void setData(Collection<Object> data) {
      this.dataSourceType = SOURCE_DOM;

      Collection<Object> dataTmp = (Collection<Object>) data;
      if (dataTmp != null && dataTmp.size() > 0) {
         iterator = dataTmp.iterator();
      }
      else {
         iterator = null;
         currentObject = null;
      }
   }

   /**
    * <p>
    * May be a runtime expression.
    * </p>
    * 
    * @param url
    */
   public void setUrl(String url) {
      String processedUrl = UrlUtils.getProcessedUrl(url, (HttpServletRequest) this.pageContext.getRequest(),
            (HttpServletResponse) this.pageContext.getResponse());
      this.stagingConf.put(DatatableOptions.AJAX_SOURCE, processedUrl);
      this.dataSourceType = SOURCE_AJAX;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setRow(String row) {
      this.row = row;
   }

   public void setConfGroup(String confGroup) {
      this.confGroup = confGroup;
   }

   public void setRowIdBase(String rowIdBase) {
      this.rowIdBase = rowIdBase;
   }

   public void setRowIdPrefix(String rowIdPrefix) {
      this.rowIdPrefix = rowIdPrefix;
   }

   public void setRowIdSuffix(String rowIdSuffix) {
      this.rowIdSuffix = rowIdSuffix;
   }

   public void setEscapeXml(boolean escapeXml) {
      this.escapeXml = escapeXml;
   }

   public void setAutoWidth(boolean autoWidth) {
      stagingConf.put(DatatableOptions.FEATURE_AUTOWIDTH, autoWidth);
   }

   public void setDeferRender(String deferRender) {
      stagingConf.put(DatatableOptions.AJAX_DEFERRENDER, deferRender);
   }

   public void setDeferLoading(String deferLoading) {
      stagingConf.put(DatatableOptions.AJAX_DEFERLOADING, deferLoading);
   }

   public void setFilterable(boolean filterable) {
      stagingConf.put(DatatableOptions.FEATURE_FILTERABLE, filterable);
   }

   public void setInfo(boolean info) {
      stagingConf.put(DatatableOptions.FEATURE_INFO, info);
   }

   public void setPageable(boolean pageable) {
      stagingConf.put(DatatableOptions.FEATURE_PAGEABLE, pageable);
   }

   public void setLengthChange(boolean lengthChange) {
      stagingConf.put(DatatableOptions.FEATURE_LENGTHCHANGE, lengthChange);
   }

   public void setProcessing(boolean processing) {
      stagingConf.put(DatatableOptions.FEATURE_PROCESSING, processing);
   }

   public void setServerSide(boolean serverSide) {
      stagingConf.put(DatatableOptions.AJAX_SERVERSIDE, serverSide);
   }

   public void setPagingType(String pagingType) {
      stagingConf.put(DatatableOptions.FEATURE_PAGINGTYPE, pagingType);
   }

   public void setSortable(boolean sortable) {
      stagingConf.put(DatatableOptions.FEATURE_SORTABLE, sortable);
   }

   public void setStateSave(String stateSave) {
      stagingConf.put(DatatableOptions.FEATURE_STATESAVE, stateSave);
   }

   public void setScrollY(String scrollY) {
      stagingConf.put(DatatableOptions.FEATURE_SCROLLY, scrollY);
   }

   public void setScrollCollapse(String scrollCollapse) {
      stagingConf.put(DatatableOptions.FEATURE_SCROLLCOLLAPSE, scrollCollapse);
   }

   public void setScrollX(String scrollX) {
      stagingConf.put(DatatableOptions.FEATURE_SCROLLX, scrollX);
   }

   public void setScrollXInner(String scrollXInner) {
      stagingConf.put(DatatableOptions.FEATURE_SCROLLXINNER, scrollXInner);
   }

   public void setFixedPosition(String fixedPosition) {
      stagingConf.put(DatatableOptions.PLUGIN_FIXEDPOSITION, fixedPosition);
   }

   public void setOffsetTop(int fixedOffsetTop) {
      stagingConf.put(DatatableOptions.PLUGIN_FIXEDOFFSETTOP, fixedOffsetTop);
   }

   public void setExport(String export) {
      stagingConf.put(DatatableOptions.EXPORT_ENABLED_FORMATS, export);
   }

   public void setExportStyle(String exportContainerStyle) {
      stagingConf.put(DatatableOptions.EXPORT_CONTAINER_STYLE, exportContainerStyle);
   }

   public void setExportClass(String exportContainerClass) {
      stagingConf.put(DatatableOptions.EXPORT_CONTAINER_CLASS, exportContainerClass);
   }

   public void setJqueryUI(String jqueryUI) {
      stagingConf.put(DatatableOptions.FEATURE_JQUERYUI, jqueryUI);
   }

   public void setPipelining(String pipelining) {
      stagingConf.put(DatatableOptions.AJAX_PIPELINING, pipelining);
   }

   public void setPipeSize(int pipeSize) {
      stagingConf.put(DatatableOptions.AJAX_PIPESIZE, pipeSize);
   }

   public void setReloadSelector(String reloadSelector) {
      stagingConf.put(DatatableOptions.AJAX_RELOAD_SELECTOR, reloadSelector);
   }

   public void setReloadFunction(String reloadFunction) {
      stagingConf.put(DatatableOptions.AJAX_RELOAD_FUNCTION, reloadFunction);
   }

   public void setTheme(String theme) {
      System.out.println("theme=" + theme);
      stagingConf.put(DatatableOptions.CSS_THEME, theme);
   }

   public void setThemeOption(String themeOption) {
      stagingConf.put(DatatableOptions.CSS_THEMEOPTION, themeOption);
   }

   public void setLengthMenu(String lengthMenu) {
      stagingConf.put(DatatableOptions.FEATURE_LENGTHMENU, lengthMenu);
   }

   public void setCssStripes(String cssStripesClasses) {
      stagingConf.put(DatatableOptions.CSS_STRIPECLASSES, cssStripesClasses);
   }

   public void setAjaxParams(String ajaxParams) {
      stagingConf.put(DatatableOptions.AJAX_PARAMS, ajaxParams);
   }

   public void setDisplayLength(int displayLength) {
      stagingConf.put(DatatableOptions.FEATURE_DISPLAYLENGTH, displayLength);
   }

   public void setFilterDelay(int filterDelay) {
      stagingConf.put(DatatableOptions.FEATURE_FILTER_DELAY, filterDelay);
   }

   public void setFilterSelector(String filterSelector) {
      stagingConf.put(DatatableOptions.FEATURE_FILTER_SELECTOR, filterSelector);
   }

   public void setFilterClearSelector(String filterClearSelector) {
      stagingConf.put(DatatableOptions.FEATURE_FILTER_CLEAR_SELECTOR, filterClearSelector);
   }

   public void setDom(String dom) {
      stagingConf.put(DatatableOptions.FEATURE_DOM, dom);
   }

   public void setExt(String extensions) {
      stagingConf.put(DatatableOptions.MAIN_EXTENSION_NAMES, extensions);
   }

   public void setCssStyle(String cssStyle) {
      stagingConf.put(DatatableOptions.CSS_STYLE, cssStyle);
   }

   public void setCssClass(String cssClass) {
      stagingConf.put(DatatableOptions.CSS_CLASS, cssClass);
   }

   public void setFilterPlaceholder(String filterPlaceholder) {
      stagingConf.put(DatatableOptions.FEATURE_FILTER_PLACEHOLDER, filterPlaceholder);
   }
}