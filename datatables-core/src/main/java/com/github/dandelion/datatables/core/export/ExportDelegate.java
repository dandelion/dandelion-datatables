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
package com.github.dandelion.datatables.core.export;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.util.ClassUtils;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.web.filter.DatatablesFilter;

/**
 * Delegate class in charge of preparing the export content.
 * 
 * @author Thibault Duchateau
 */
public class ExportDelegate {

   // Logger
   private static Logger logger = LoggerFactory.getLogger(ExportDelegate.class);

   private HtmlTable htmlTable;
   private HttpServletRequest request;

   public ExportDelegate(HtmlTable htmlTable, HttpServletRequest request) {
      this.htmlTable = htmlTable;
      this.request = request;
   }

   /**
    * Prepares the export by processing the passed export class and setting
    * request attributes that will be used by the {@link DatatablesFilter} to
    * render the export.
    */
   public void prepareExport() {

      // The stream containing the export content
      OutputStream stream = new ByteArrayOutputStream();

      // Get the current export type
      String exportFormat = htmlTable.getTableConfiguration().getCurrentExportFormat();
      ExportConf exportConf = htmlTable.getTableConfiguration().getExportConfigurations().get(exportFormat);

      String exportClassName = exportConf.getExportClass();
      if (exportClassName == null) {
         throw new DandelionException("No export class has been configured for the '" + exportFormat
               + "' format. Please configure it before exporting.");
      }
      logger.debug("Selected export class: {}", exportClassName);

      // Check that the class can be instantiated
      if (!ClassUtils.isPresent(exportClassName)) {
         StringBuilder sb = new StringBuilder("Unable to export in the ");
         sb.append(exportFormat);
         sb.append(" format because either the export class '");
         sb.append(exportClassName);
         sb.append("' or some other librairies ");
         sb.append(" imported in the export class is not present in the classpath.");
         sb.append("Did you forget to add a dependency?");
         throw new DandelionException(sb.toString());
      }

      // Instantiates the export class and processes the export
      Class<?> exportClass = null;
      Object obj = null;
      try {
         exportClass = ClassUtils.getClass(exportClassName);
         obj = ClassUtils.getNewInstance(exportClass);
      }
      catch (ClassNotFoundException e) {
         throw new DandelionException("Unable to load the class '" + exportClassName + "'", e);
      }
      catch (InstantiationException e) {
         throw new DandelionException("Unable to instanciate the class '" + exportClassName + "'", e);
      }
      catch (IllegalAccessException e) {
         throw new DandelionException("Unable to access the class '" + exportClassName + "'", e);
      }

      ((DatatablesExport) obj).initExport(htmlTable);
      ((DatatablesExport) obj).processExport(stream);

      // Fill the request so that the filter will intercept it and
      // override the response with the export content
      request.setAttribute(ExportUtils.DDL_DT_REQUESTATTR_EXPORT_CONTENT,
            ((ByteArrayOutputStream) stream).toByteArray());
      request.setAttribute(ExportUtils.DDL_DT_REQUESTATTR_EXPORT_CONF, exportConf);
   }
}