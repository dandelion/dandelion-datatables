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
package com.github.dandelion.datatables.core.extension;

import java.util.List;

import com.github.dandelion.datatables.core.extension.feature.AbstractFilteringFeature;
import com.github.dandelion.datatables.core.extension.feature.PagingTypeBootstrapSimpleFeature;
import com.github.dandelion.datatables.core.extension.plugin.ColReorderPlugin;
import com.github.dandelion.datatables.core.extension.plugin.ScrollerPlugin;
import com.github.dandelion.datatables.core.extension.theme.Bootstrap2Theme;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Common interface for all extensions. An extension can be a plugin (e.g.
 * {@link ScrollerPlugin}, {@link ColReorderPlugin}), a feature (e.g.
 * {@link PagingTypeBootstrapSimpleFeature}, {@link AbstractFilteringFeature}
 * add-on) or a theme (e.g. {@link Bootstrap2Theme}).
 * <p>
 * 
 * An extension can be composed of :
 * <ul>
 * <li>one or more JsResource, i.e. Javascript code externalized in a file</li>
 * <li>one or more CssResource, i.e. CSS code externalized in a file</li>
 * <li>one or more Parameter, i.e. one or more specific DataTables parameters
 * that will be used during the DataTables initialization</li>
 * <li>afterAll</li>
 * </ul>
 * TODO These attributes can be visualized in the following Javascript snippet :
 * <blockquote>
 * 
 * <pre>
 * => <b>BEFOREALL</b>
 * var oTable_tableId = $('#myTableId');
 * var oTable_tableId_params = {...};
 * => <b>BEFORESTARTDOCUMENTREADY</b>
 * $(document).ready(function(){
 *    => <b>AFTERSTARTDOCUMENTREADY</b>
 *    oTable_myTableId = $('#myTableId').dataTable(oTable_myTableId_params);
 *    => <b>BEFOREENDDOCUMENTREADY</b>
 * });
 * => <b>AFTERALL</b>
 * </pre>
 * 
 * </blockquote></li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 * @see ExtensionLoader
 * @see ExtensionProcessor
 */
public interface Extension {

   /**
    * <p>
    * Returns the extension's name.
    */
   public String getExtensionName();

   /**
    * <p>
    * Sets up the extension.
    * <p>
    * The HtmlTable object is available if a particular configuration is needed.
    * 
    * @param table
    *           The HTML table.
    */
   public void setupWrapper(HtmlTable table);

   /**
    * @return the Javascript code to be inserted at the
    *         {@link InsertMode#BEFOREALL} placeholder.
    */
   public StringBuilder getBeforeAll();

   /**
    * @return the Javascript code to be inserted at the
    *         {@link InsertMode#AFTERALL} placeholder.
    */
   public StringBuilder getAfterAll();

   /**
    * @return the Javascript code to be inserted at the
    *         {@link InsertMode#BEFORESTARTDOCUMENTREADY} placeholder.
    */
   public StringBuilder getBeforeStartDocumentReady();

   /**
    * @return the Javascript code to be inserted at the
    *         {@link InsertMode#AFTERSTARTDOCUMENTREADY} placeholder.
    */
   public StringBuilder getAfterStartDocumentReady();

   /**
    * @return the Javascript code to be inserted at the
    *         {@link InsertMode#BEFOREENDDOCUMENTREADY} placeholder.
    */
   public StringBuilder getBeforeEndDocumentReady();

   public List<Parameter> getParameters();

   public void addParameter(Parameter conf);

   public void appendToBeforeAll(String beforeAll);

   public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady);

   public void appendToAfterStartDocumentReady(String afterStartDocumentReady);

   public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady);

   public void appendToAfterAll(String afterAll);
}
