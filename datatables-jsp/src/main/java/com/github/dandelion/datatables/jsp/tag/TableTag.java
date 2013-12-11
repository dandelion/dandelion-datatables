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
package com.github.dandelion.datatables.jsp.tag;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.core.asset.wrapper.impl.DelegatedLocationWrapper;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.generator.javascript.JavascriptGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.UrlUtils;

/**
 * <p>
 * Tag used to generate a HTML table.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class TableTag extends AbstractTableTag {
	private static final long serialVersionUID = 4528524566511084446L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableTag.class);

	public TableTag(){
		stagingConf = new HashMap<ConfigToken<?>, Object>();
	}
	
	/**
	 * {@inheritDoc}
	 * @throws JspException 
	 */
	public int doStartTag() throws JspException {
		
		table = new HtmlTable(id, (HttpServletRequest) pageContext.getRequest(),
				(HttpServletResponse) pageContext.getResponse(), confGroup, dynamicAttributes);

		TableConfig.applyConfiguration(stagingConf, table.getTableConfiguration());
		
		// Just used to identify the first row (header)
		iterationNumber = 1;

		// The table data are loaded using AJAX source
		if ("AJAX".equals(this.loadingType)) {

			this.table.addHeaderRow();
			this.table.addRow();

			return EVAL_BODY_BUFFERED;
		}
		// The table data are loaded using a DOM source (Collection)
		else if ("DOM".equals(this.loadingType)) {

			this.table.addHeaderRow();
			
			return processIteration();
		}

		// Never reached
		return SKIP_BODY;
	}

	/**
	 * {@inheritDoc}
	 */
	public int doAfterBody() throws JspException {

		this.iterationNumber++;

		return processIteration();
	}

	/**
	 * {@inheritDoc}
	 */
	public int doEndTag() throws JspException {

		// The table is being exported
		if (UrlUtils.isTableBeingExported(pageContext.getRequest(), table)) {
			return setupExport();
		}
		// The table must be generated and displayed
		else {
			return setupHtmlGeneration();
		}
	}

	/**
	 * Set up the export properties, before the filter intercepts the response.
	 * 
	 * @return allways SKIP_PAGE, because the export filter will override the
	 *         response with the exported data instead of displaying the page.
	 * @throws JspException
	 *             if something went wrong during export.
	 */
	private int setupExport() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		String currentExportType = ExportUtils.getCurrentExportType(request);

		this.table.getTableConfiguration().setExporting(true);
		this.table.getTableConfiguration().setCurrentExportFormat(currentExportType);
		
		try {
			// Call the export delegate
			ExportDelegate exportDelegate = new ExportDelegate(table, request);
			exportDelegate.launchExport();

		} catch (ExportException e) {
			logger.error("Something went wront with the Dandelion export configuration.");
			throw new JspException(e);
		} 

		response.reset();

		return SKIP_PAGE;
	}

	/**
	 * Set up the HTML table generation.
	 * 
	 * @return allways EVAL_PAGE to keep evaluating the page.
	 * @throws JspException
	 *             if something went wrong during the processing.
	 */
	private int setupHtmlGeneration() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		JsResource jsResource = null;
		
		this.table.getTableConfiguration().setExporting(false);

		try {
			// Init the web resources generator
			WebResourceGenerator contentGenerator = new WebResourceGenerator(table);
	
			// Generate the web resources (JS, CSS) and wrap them into a
			// WebResources POJO
			jsResource = contentGenerator.generateWebResources();
			logger.debug("Web content generated successfully");

			// Scope update
			AssetsRequestContext.get(request)
				.addScopes(Scope.DDL_DT.getScopeName())
				.addScopes(Scope.DATATABLES.getScopeName())
				.addParameter("dandelion-datatables", DelegatedLocationWrapper.DELEGATED_CONTENT_PARAM,
							DatatablesConfigurator.getJavascriptGenerator(), false);
			
			// Buffering generated Javascript
			JavascriptGenerator javascriptGenerator = AssetsRequestContext.get(request).getParameterValue("dandelion-datatables", DelegatedLocationWrapper.DELEGATED_CONTENT_PARAM);
			javascriptGenerator.addResource(jsResource);
			
			// HTML generation
			pageContext.getOut().println(this.table.toHtml());

		} catch (IOException e) {
			logger.error("Something went wront with the datatables tag");
			throw new JspException(e);
		} 
		catch (ExtensionLoadingException e) {
			logger.error("Something went wront during the extension loading");
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}


	/**
	 * TODO
	 */
	public void release() {
		super.release();
	}
	
	public void setAutoWidth(Boolean autoWidth) {
		stagingConf.put(TableConfig.FEATURE_AUTOWIDTH, autoWidth);
	}

	public void setDeferRender(String deferRender) {
		stagingConf.put(TableConfig.AJAX_DEFERRENDER, deferRender);
	}

	public void setFilter(Boolean filterable) {
		stagingConf.put(TableConfig.FEATURE_FILTERABLE, filterable);
	}

	public void setInfo(Boolean info) {
		stagingConf.put(TableConfig.FEATURE_INFO, info);
	}

	public void setPaginate(Boolean paginate) {
		stagingConf.put(TableConfig.FEATURE_PAGINATE, paginate);
	}

	public void setLengthChange(Boolean lengthChange) {
		stagingConf.put(TableConfig.FEATURE_LENGTHCHANGE, lengthChange);
	}

	public void setProcessing(Boolean processing) {
		stagingConf.put(TableConfig.AJAX_PROCESSING, processing);
	}

	public void setServerSide(Boolean serverSide) {
		stagingConf.put(TableConfig.AJAX_SERVERSIDE, serverSide);
	}
	
	public void setPaginationType(String paginationType) {
		stagingConf.put(TableConfig.FEATURE_PAGINATIONTYPE, paginationType);
	}

	public void setSort(Boolean sort) {
		stagingConf.put(TableConfig.FEATURE_SORT, sort);
	}

	public void setStateSave(String stateSave) {
		stagingConf.put(TableConfig.FEATURE_STATESAVE, stateSave);
	}

	public void setFixedHeader(String fixedHeader) {
		stagingConf.put(TableConfig.PLUGIN_FIXEDHEADER, fixedHeader);
	}

	public void setScroller(String scroller) {
		stagingConf.put(TableConfig.PLUGIN_SCROLLER, scroller);
	}

	public void setColReorder(String colReorder) {
		stagingConf.put(TableConfig.PLUGIN_COLREORDER, colReorder);
	}

	public void setScrollY(String scrollY) {
		stagingConf.put(TableConfig.FEATURE_SCROLLY, scrollY);
	}

	public void setScrollCollapse(String scrollCollapse) {
		stagingConf.put(TableConfig.FEATURE_SCROLLCOLLAPSE, scrollCollapse);
	}

	public void setScrollX(String scrollX) {
		stagingConf.put(TableConfig.FEATURE_SCROLLX, scrollX);
	}

	public void setScrollXInner(String scrollXInner) {
		stagingConf.put(TableConfig.FEATURE_SCROLLXINNER, scrollXInner);
	}

	public void setFixedPosition(String fixedPosition) {
		stagingConf.put(TableConfig.PLUGIN_FIXEDPOSITION, fixedPosition);
	}

	public void setOffsetTop(Integer fixedOffsetTop) {
		stagingConf.put(TableConfig.PLUGIN_FIXEDOFFSETTOP, fixedOffsetTop);
	}

	public void setExport(String export) {
		stagingConf.put(TableConfig.EXPORT_ENABLED_FORMATS, export);
	}

	public String getLoadingType() {
		return this.loadingType;
	}

	public void setUrl(String url) {
		stagingConf.put(TableConfig.AJAX_SOURCE, url);
		this.loadingType = "AJAX";
		this.url = url;
	}

	public void setJqueryUI(String jqueryUI) {
		stagingConf.put(TableConfig.FEATURE_JQUERYUI, jqueryUI);
	}

	public void setPipelining(String pipelining) {
		stagingConf.put(TableConfig.AJAX_PIPELINING, pipelining);
	}
	
	public void setPipeSize(Integer pipeSize){
		stagingConf.put(TableConfig.AJAX_PIPESIZE, pipeSize);
	}
	
	public void setExportLinks(String exportLinks) {
		stagingConf.put(TableConfig.EXPORT_LINK_POSITIONS, exportLinks);
	}

	public void setTheme(String theme) {
		stagingConf.put(TableConfig.CSS_THEME, theme);
	}

	public void setThemeOption(String themeOption) {
		stagingConf.put(TableConfig.CSS_THEMEOPTION, themeOption);
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setAppear(String appear) {
		stagingConf.put(TableConfig.FEATURE_APPEAR, appear);
	}
	
	public void setLengthMenu(String lengthMenu){
		stagingConf.put(TableConfig.FEATURE_LENGTHMENU, lengthMenu);
	}
	
	public void setCssStripes(String cssStripesClasses){
		stagingConf.put(TableConfig.CSS_STRIPECLASSES, cssStripesClasses);
	}
	
	public void setServerData(String serverData) {
		stagingConf.put(TableConfig.AJAX_SERVERDATA, serverData);
	}

	public void setServerParams(String serverParams) {
		stagingConf.put(TableConfig.AJAX_SERVERPARAM, serverParams);
	}

	public void setServerMethod(String serverMethod) {
		stagingConf.put(TableConfig.AJAX_SERVERMETHOD, serverMethod);
	}

	public void setDisplayLength(Integer displayLength) {
		stagingConf.put(TableConfig.FEATURE_DISPLAYLENGTH, displayLength);
	}

	public void setDom(String dom) {
		stagingConf.put(TableConfig.FEATURE_DOM, dom);
	}

	public void setExt(String extensions){
		stagingConf.put(TableConfig.MAIN_EXTENSION_NAMES, extensions);	
	}
	
	public void setConfGroup(String confGroup) {
		this.confGroup = confGroup;
	}

	public void setData(Collection<Object> data) {
		this.loadingType = "DOM";
		this.data = data;

		Collection<Object> dataTmp = (Collection<Object>) data;
		if (dataTmp != null && dataTmp.size() > 0) {
			iterator = dataTmp.iterator();
		} else {
			iterator = null;
			currentObject = null; 
		}
	}
	
	public void setRowIdBase(String rowIdBase) {
		this.rowIdBase = rowIdBase;
	}

	public void setRowIdPrefix(String rowIdPrefix) {
		this.rowIdPrefix = rowIdPrefix;
	}

	public void setRowIdSufix(String rowIdSufix) {
		this.rowIdSufix = rowIdSufix;
	}
	
	public void setCssStyle(String cssStyle) {
		stagingConf.put(TableConfig.CSS_STYLE, cssStyle);
	}

	public void setCssClass(String cssClass) {
		stagingConf.put(TableConfig.CSS_CLASS, cssClass);
	}
	
	public void setFilterPlaceholder(String filterPlaceholder) {
		stagingConf.put(TableConfig.FEATURE_FILTER_PLACEHOLDER, filterPlaceholder);
	}
}