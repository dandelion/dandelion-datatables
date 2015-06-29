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

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.github.dandelion.core.util.UrlUtils;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.core.util.ConfigUtils;

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
 * Example usage:
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
 * @since 0.1.0
 */
public class TableTag extends AbstractTableTag {

   private static final long serialVersionUID = 4528524566511084446L;

   /**
    * Initializes a new staging configuration map to be applied to the
    * {@link TableConfiguration} instance.
    */
   public TableTag() {
      stagingConf = new HashMap<Option<?>, Object>();
   }

   public int doStartTag() throws JspException {

      iterationNumber = 1; // Just used to identify the first row (header)
      request = (HttpServletRequest) pageContext.getRequest();
      response = (HttpServletResponse) pageContext.getResponse();

      table = new HtmlTable(id, request, response, confGroup, dynamicAttributes);

      // The table data are loaded using an AJAX source
      if ("AJAX".equals(this.dataSourceType)) {

         this.table.addHeaderRow();

         return EVAL_BODY_BUFFERED;
      }
      // The table data are loaded using a DOM source (Collection)
      else if ("DOM".equals(this.dataSourceType)) {

         this.table.addHeaderRow();

         return processIteration();
      }

      // Never reached
      return SKIP_BODY;
   }

   public int doAfterBody() throws JspException {

      iterationNumber++;

      return processIteration();
   }

   public int doEndTag() throws JspException {

      // At this point, all setters have been called and the staging
      // configuration map should have been filled with user configuration
      // The user configuration can now be applied to the default
      // configuration
      ConfigUtils.applyStagingOptions(stagingConf, table);

      // Once all configuration are merged, they can be processed
      ConfigUtils.processOptions(table);

      // The table is being exported
      if (ExportUtils.isTableBeingExported(request, table)) {
         return setupExport();
      }
      // The table must be generated and displayed
      else {
         ConfigUtils.storeTableInRequest(request, table);
         return setupHtmlGeneration();
      }
   }

   public void setData(Collection<Object> data) {
      this.dataSourceType = "DOM";
      this.data = data;

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
    * 
    * <p>
    * May be a runtime expression.
    * </p>
    * 
    * @param url
    */
   public void setUrl(String url) {
      String processedUrl = UrlUtils.getProcessedUrl(url, (HttpServletRequest) pageContext.getRequest(),
            (HttpServletResponse) pageContext.getResponse());
      stagingConf.put(DatatableOptions.AJAX_SOURCE, processedUrl);
      this.dataSourceType = "AJAX";
      this.url = url;
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
      stagingConf.put(DatatableOptions.CSS_THEME, theme);
   }

   public void setThemeOption(String themeOption) {
      stagingConf.put(DatatableOptions.CSS_THEMEOPTION, themeOption);
   }

   public void setAppear(String appear) {
      stagingConf.put(DatatableOptions.FEATURE_APPEAR, appear);
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

   public void setFilterTrigger(String filterTrigger) {
      stagingConf.put(DatatableOptions.FEATURE_FILTER_TRIGGER, filterTrigger);
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