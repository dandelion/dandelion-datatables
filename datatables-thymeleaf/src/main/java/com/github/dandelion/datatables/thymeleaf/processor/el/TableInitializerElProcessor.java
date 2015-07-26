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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.util.RequestUtils;

/**
 * <p>
 * Element processor applied to the HTML <tt>table</tt> tag.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class TableInitializerElProcessor extends AbstractElProcessor {

   public TableInitializerElProcessor(IElementNameProcessorMatcher matcher) {
      super(matcher);
   }

   @Override
   public int getPrecedence() {
      return DataTablesDialect.DT_HIGHEST_PRECEDENCE;
   }

   @Override
   protected ProcessorResult doProcessElement(Arguments arguments, Element element, HttpServletRequest request,
         HttpServletResponse response, HtmlTable htmlTable) {

      String tableId = element.getAttributeValue("id");

      if (tableId != null) {

         String confGroup = (String) RequestUtils.getFromRequest(DataTablesDialect.INTERNAL_CONF_GROUP, request);

         HtmlTable newHtmlTable = new HtmlTable(tableId, request, response, confGroup);
         request.setAttribute(TableConfiguration.class.getCanonicalName(), newHtmlTable.getTableConfiguration());

         // Add a default header row
         newHtmlTable.addHeaderRow();

         // Store the htmlTable POJO as a request attribute, so that all the
         // others following HTML tags can access it and particularly the
         // "finalizing div"
         RequestUtils.storeInRequest(DataTablesDialect.INTERNAL_BEAN_TABLE, newHtmlTable, request);

         // The table node is also saved in the request, to be easily accessed
         // later
         RequestUtils.storeInRequest(DataTablesDialect.INTERNAL_NODE_TABLE, element, request);

         // Map used to store the table local configuration
         RequestUtils.storeInRequest(DataTablesDialect.INTERNAL_BEAN_TABLE_STAGING_OPTIONS,
               new HashMap<Option<?>, Object>(), request);

         // The HTML needs to be updated
         processMarkup(element);

         return ProcessorResult.OK;
      }
      else {
         throw new DandelionException("The 'id' attribute is required by Dandelion-Datatables.");
      }
   }

   /**
    * <p>
    * The HTML markup needs to be updated for several reasons:
    * </p>
    * <ul>
    * <li>First for housekeeping: all Dandelion-Datatables attributes must be
    * removed before the table is displayed</li>
    * <li>Markers are applied on {@code thead} and {@code tbody} elements in
    * order to limit the scope of application of the processors, thus avoiding
    * any conflict with native HTML tables</li>
    * <li>A "finalizing {@code div}" must be added after the HTML {@code table}
    * tag in order to finalize the Dandelion-Datatables configuration. The
    * {@code div} will be removed in its corresponding processor, i.e.
    * {@link TableFinalizerElProcessor}</li>
    * </ul>
    * 
    * @param element
    *           The {@code table} tag.
    */
   private void processMarkup(Element element) {

      // Housekeeping
      element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":table");

      // Markers on THEAD and TBODY tags
      for (Node child : element.getChildren()) {

         if (child != null && child instanceof Element) {

            Element childTag = (Element) child;
            String childTagName = childTag.getNormalizedName();

            if (childTagName.equals("thead") || childTagName.equals("tbody")) {
               childTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");
            }

            childTag.setProcessable(true);
         }
      }

      // "Finalizing div"
      Element div = new Element("div");
      div.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":tmp", "internalUse");
      div.setRecomputeProcessorsImmediately(true);
      element.getParent().insertAfter(element, div);
   }
}