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
package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.HttpMethod;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.Parameter.Mode;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlHyperlink;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.TableConfiguration;

/**
 * <p>
 * Extension used to generate export links, depending on the export
 * configurations stored in the {@link TableConfiguration} instance.
 * </p>
 * <p>
 * All export links are generated inside a dedicated {@link ExtraHtml} with the
 * uid {@code E}.
 * </p>
 * <p>
 * If the {@link DatatableOptions#FEATURE_DOM} is not present in the
 * {@link TableConfiguration} instance, the export links container will be
 * inserted with the following configuration: {@code lEfrtip}. Otherwise, the
 * container must be added manually thanks to the
 * {@link DatatableOptions#FEATURE_DOM} feature.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see DatatableOptions#EXPORT_ENABLED_FORMATS
 * @see DatatableOptions#FEATURE_DOM
 */
public class ExportFeature extends AbstractExtension {

   public static final String EXTENSION_NAME = "export";
   private static final String TOOLBAR_PREXIX = "fg-toolbar ui-toolbar ui-widget-header ui-helper-clearfix ui-corner-";
   
   private HtmlTable table;

   @Override
   public String getExtensionName() {
      return EXTENSION_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      this.table = table;

      // If the export has been configured to be triggered with a POST, PUT or
      // DELETE HTTP method, a custom plugin must be added to the page
      for (ExportConf exportConf : table.getTableConfiguration().getExportConfiguration().values()) {
         if (exportConf.getMethod().equals(HttpMethod.POST) || exportConf.getMethod().equals(HttpMethod.PUT)
               || exportConf.getMethod().equals(HttpMethod.DELETE)) {
            addBundle(DatatableBundles.JQUERY_DOWNLOAD);
         }
      }

      String exportContainerStyle = DatatableOptions.EXPORT_CONTAINER_STYLE.valueFrom(table.getTableConfiguration());
      String exportContainerClass = DatatableOptions.EXPORT_CONTAINER_CLASS.valueFrom(table.getTableConfiguration());

      // In order to be easily positioned around the table, a DataTable
      // feature is created
      ExtraHtml extraHtml = new ExtraHtml();
      extraHtml.setUid("E");
      extraHtml.setContainer("div");
      extraHtml.setCssClass("dataTables_export "
            + (StringUtils.isNotBlank(exportContainerClass) ? exportContainerClass : ""));
      extraHtml.setCssStyle(StringUtils.isNotBlank(exportContainerStyle) ? exportContainerStyle : "float: right;");

      String dom = DatatableOptions.FEATURE_DOM.valueFrom(table.getTableConfiguration());
      if (table.getTableConfiguration().getConfigurations().containsKey(DatatableOptions.CSS_THEME)
            && table.getTableConfiguration().getConfigurations().get(DatatableOptions.CSS_THEME).getClass()
                  .getSimpleName().equals("JQueryUITheme")) {
         addParameter(
               DTConstants.DT_DOM,
               "<'" + TOOLBAR_PREXIX + "-tr'lEfr>t<'" + TOOLBAR_PREXIX + "-br'ip>",
               Mode.OVERRIDE);
      }
      else if (StringUtils.isBlank(dom)) {
         addParameter(DTConstants.DT_DOM, "lEfrtip", Mode.OVERRIDE);
      }

      StringBuilder content = new StringBuilder();
      HtmlHyperlink link = null;

      // A HTML link is generated for each ExportConf bean
      for (ExportConf conf : table.getTableConfiguration().getExportConfiguration().values()) {

         link = new HtmlHyperlink();

         if (conf.getCssClass() != null) {
            link.setCssClass(conf.getCssClass());
         }

         if (conf.getCssStyle() != null) {
            link.setCssStyle(conf.getCssStyle());
            link.addCssStyle(";margin-left:2px;");
         }
         else {
            link.addCssStyle("margin-left:2px;");
         }

         if (conf.hasCustomUrl()) {
            link.setOnclick(getOnclick(conf));
         }
         else {
            link.setHref(conf.getUrl());
         }
         link.addContent(conf.getLabel());
         content.append(link.toHtml());
      }
      extraHtml.setContent(content.toString());

      // Once created, the extraHtml is transformed into Javascript and
      // appended in the DataTables configuration
      appendToAfterStartDocumentReady(extraHtml.getJavascript().toString());
   }

   private String getOnclick(ExportConf exportConf) {

      String oTableId = "oTable_" + table.getId();
      StringBuilder params = new StringBuilder();

      StringBuilder exportFuncName = new StringBuilder("ddl_dt_launch_export_");
      exportFuncName.append(table.getId());
      exportFuncName.append("_");
      exportFuncName.append(exportConf.getFormat());

      StringBuilder exportFunc = new StringBuilder("function ");
      exportFunc.append(exportFuncName.toString());
      exportFunc.append("(){");

      params.append(oTableId).append(".ajax.params()");

      // HTTP GET
      if (exportConf.getMethod().equals(HttpMethod.GET)) {
         exportFunc.append("window.location=\"").append(exportConf.getUrl());
         if (exportConf.getUrl().contains("?")) {
            exportFunc.append("&");
         }
         else {
            exportFunc.append("?");
         }

         // Parameters should be decoded because jQuery.param() uses
         // .serialize() which encodes the URL
         exportFunc.append("\" + decodeURIComponent($.param(").append(params.toString())
               .append(")).replace(/\\+/g,' ');");
      }
      // HTTP POST/PUT/DELETE
      else {
         exportFunc.append("$.download('").append(exportConf.getUrl()).append("', decodeURIComponent($.param(")
               .append(params.toString()).append(")).replace(/\\+/g,' '),'").append(exportConf.getMethod())
               .append("');");
      }

      exportFunc.append("}");

      appendToBeforeAll(exportFunc.toString());

      return exportFuncName.append("();").toString();
   }
}