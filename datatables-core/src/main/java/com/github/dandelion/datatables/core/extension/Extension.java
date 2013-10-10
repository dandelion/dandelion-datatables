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
package com.github.dandelion.datatables.core.extension;

import java.util.List;

import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.generator.AbstractConfigurationGenerator;
import com.github.dandelion.datatables.core.generator.ColumnFilteringGenerator;
import com.github.dandelion.datatables.core.generator.MainGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Common interface for all extensions. An extension can be a plugin
 * (e.g. Scroller, ColReorder), a feature (e.g. Bootstrap pagination type,
 * filtering add-on) or a theme (e.g. Bootstrap 2 theme).
 * <p>
 * An extension can be composed of :
 * <ul>
 * <li>one or more JsResource, i.e. Javascript code externalized in a file</li>
 * <li>one or more CssResource, i.e. CSS code externalized in a file</li>
 * <li>one or more Parameter, i.e. one or more specific DataTables parameters
 * that will be used during the DataTables initialization</li>
 * <li>an AbstractConfigurationGenerator if the extension needs its own
 * configuration generator. The one used for the main DataTables configuration
 * is the {@link MainGenerator}. You can also take a look at the
 * {@link ColumnFilteringGenerator} to see the configuration generated for the
 * Column Filtering add-on.</li>
 * <li>a potential Javascript function name that will be called after DataTables
 * initialization. <br>
 * Example : columnFilter <blockquote>
 * 
 * <pre>
 * oTable_myTableId = $('#myTableId').dataTable(oTable_myTableId_params).columnFilter({...});
 * </pre>
 * 
 * </blockquote></li>
 * <li>specific Javascript snippets to add in the main JS resource, i.e. the
 * resource that contains the DataTables initilization Javascript code. You can
 * add snippet at multiple locations in this file thanks to the following
 * attributes :
 * <ul>
 * <li>beforeAll</li>
 * <li>beforeStartDocumentReady</li>
 * <li>afterStartDocumentReady</li>
 * <li>beforeEndDocumentReady</li>
 * <li>afterAll</li>
 * </ul>
 * These attributes can be visualized in the following Javascript snippet :
 * <blockquote>
 * 
 * <pre>
 * => <b>BEFOREALL</b>
 * var oTable_tableId;
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
 */
public interface Extension {

    /**
     * Returns the extension's name.
     */
	public String getName();

    /**
     * Set the extension up.
     * <p>
     * The HtmlTable object is available if a particular configuration is
     * needed.
     * </p>
     *
     * @param table
     *            The HTML table.
     */
    public void setupWrapper(HtmlTable table) throws ExtensionLoadingException;

    public StringBuilder getBeforeAll();

    public StringBuilder getAfterAll();

    public StringBuilder getAfterStartDocumentReady();

    public StringBuilder getBeforeEndDocumentReady();

    public List<JsResource> getJsResources();

    public void setJsResources(List<JsResource> jsResources);

    public List<CssResource> getCssResources();

    public void setCssResources(List<CssResource> cssResources);

    public List<Parameter> getConfs();

    public void setConfs(List<Parameter> confs);

    public void addJsResource(JsResource resource);

    public void addCssResource(CssResource resource);

    public void addParameter(Parameter conf);

    public AbstractConfigurationGenerator getConfigGenerator();

    public void setConfigGenerator(AbstractConfigurationGenerator configGenerator);

    public Boolean getAppendRandomNumber();

    public void setAppendRandomNumber(Boolean appendRandomNumber);

    public void appendToBeforeAll(String beforeAll);

    public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady);

    public void appendToAfterStartDocumentReady(String afterStartDocumentReady);

    public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady);

    public void appendToAfterAll(String afterAll);

    public String getFunction();

    public void setFunction(String function);
}
