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
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.aggregator.ResourceAggregator;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.cache.AssetCache;
import com.github.dandelion.datatables.core.compressor.ResourceCompressor;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.constants.CdnConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.exception.DataNotFoundException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.DandelionUtils;
import com.github.dandelion.datatables.core.util.RequestHelper;

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

	private final static char[] DISALLOWED_CHAR = {'-'};
	
	public TableTag(){
		localConf = new HashMap<Configuration, Object>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {
		
		// We must ensure that the chosen table id doesn't contain any of the
		// disallowed character because a Javascript variable will be created
		// using this name
		if(StringUtils.containsAny(id, DISALLOWED_CHAR)){
			throw new JspException("The 'id' attribute cannot contain one of the following characters: " + String.valueOf(DISALLOWED_CHAR));
		}
		
		// Init the table with its DOM id and a generated random number
		table = new HtmlTable(id, (HttpServletRequest) pageContext.getRequest(), confGroup);
		Configuration.applyConfiguration(table.getTableConfiguration(), localConf);
		
		// Just used to identify the first row (header)
		iterationNumber = 1;

		// The table data are loaded using AJAX source
		if ("AJAX".equals(this.loadingType)) {

			this.table.addFooterRow();
			this.table.addHeaderRow();
			
			this.table.addRow();

			return EVAL_BODY_BUFFERED;
		}
		// The table data are loaded using a DOM source (Collection)
		else if ("DOM".equals(this.loadingType)) {
			this.table.addFooterRow();
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
		if (RequestHelper.isTableBeingExported(pageContext.getRequest(), table)) {
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

		// Init the export properties
		ExportProperties exportProperties = new ExportProperties();

		ExportType currentExportType = getCurrentExportType();

		exportProperties.setCurrentExportType(currentExportType);
		exportProperties.setExportConf(table.getTableConfiguration().getExportConfMap().get(currentExportType));
		exportProperties.setFileName(table.getTableConfiguration().getExportConfMap().get(currentExportType).getFileName());

		this.table.getTableConfiguration().setExportProperties(exportProperties);
		this.table.getTableConfiguration().setExporting(true);

		try {
			// Call the export delegate
			ExportDelegate exportDelegate = new ExportDelegate(table, exportProperties, request);
			exportDelegate.launchExport();

		} catch (ExportException e) {
			logger.error("Something went wront with the Dandelion export configuration.");
			throw new JspException(e);
		} catch (BadConfigurationException e) {
			logger.error("Something went wront with the Dandelion configuration.");
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
		WebResources webResources = null;
		
		this.table.getTableConfiguration().setExporting(false);

		// TODO retirer cette fonction
		// Register all activated features
		registerFeatures();

		try {
			// First we check if the DataTables configuration already exist in the cache
			String keyToTest = RequestHelper.getCurrentURIWithParameters(request) + "|" + table.getId();

			if(DandelionUtils.isDevModeEnabled() || !AssetCache.cache.containsKey(keyToTest)){
				logger.debug("No asset for the key {}. Generating...", keyToTest);
				
				// Init the web resources generator
				WebResourceGenerator contentGenerator = new WebResourceGenerator();
	
				// Generate the web resources (JS, CSS) and wrap them into a
				// WebResources POJO
				webResources = contentGenerator.generateWebResources(table);
				logger.debug("Web content generated successfully");
				
				AssetCache.cache.put(keyToTest, webResources);
				logger.debug("Cache updated with new web resources");
			}
			else{
				logger.debug("Asset(s) already exist, retrieving content from cache...");

				webResources = (WebResources) AssetCache.cache.get(keyToTest);
			}

			// Aggregation
			if (table.getTableConfiguration().getMainAggregatorEnable() != null && table.getTableConfiguration().getMainAggregatorEnable()) {
				logger.debug("Aggregation enabled");
				ResourceAggregator.processAggregation(webResources, table);
			}

			// Compression
			if (table.getTableConfiguration().getMainCompressorEnable() != null && table.getTableConfiguration().getMainCompressorEnable()) {
				logger.debug("Compression enabled");
				ResourceCompressor.processCompression(webResources, table);
			}

			System.out.println("table.getTableConfiguration() = " + table.getTableConfiguration());
			System.out.println("cdn = " + table.getTableConfiguration().getExtraCdn());
			// <link> HTML tag generation
			if (table.getTableConfiguration().getExtraCdn()) {
				generateLinkTag(CdnConstants.CDN_DATATABLES_CSS);
			}
			for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {
				if(entry.getValue().getType().equals(ResourceType.EXTERNAL)){
					generateLinkTag(entry.getValue().getLocation());
				}
				else{
					String src = RequestHelper.getAssetSource(entry.getKey(), this.table, request, false);
					generateLinkTag(src);
				}
			}

			// HTML generation
			pageContext.getOut().println(this.table.toHtml());

			// <script> HTML tag generation
			if (table.getTableConfiguration().getExtraCdn()) {
				generateScriptTag(CdnConstants.CDN_DATATABLES_JS_MIN);
			}
			for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {
				String src = RequestHelper.getAssetSource(entry.getKey(), this.table, request, false);
				generateScriptTag(src);
			}
			// Main Javascript file
			String src = RequestHelper.getAssetSource(webResources.getMainJsFile().getName(), this.table, request, true);
			generateScriptTag(src);

		} catch (IOException e) {
			logger.error("Something went wront with the datatables tag");
			throw new JspException(e);
		} catch (CompressionException e) {
			logger.error("Something went wront with the compressor.");
			throw new JspException(e);
		} catch (BadConfigurationException e) {
			logger.error("Something went wront with the Dandelion configuration. Please check your Dandelion.properties file");
			throw new JspException(e);
		} catch (DataNotFoundException e) {
			logger.error("Something went wront with the data provider.");
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}


	/**
	 * TODO
	 */
	public void release() {
		// TODO Auto-generated method stub
		super.release();

		// TODO
	}
	
	public void setAutoWidth(Boolean autoWidth) {
		localConf.put(Configuration.FEATURE_AUTOWIDTH, autoWidth);
	}

	public void setDeferRender(String deferRender) {
		localConf.put(Configuration.AJAX_DEFERRENDER, deferRender);
	}

	public void setFilter(Boolean filterable) {
		localConf.put(Configuration.FEATURE_FILTERABLE, filterable);
	}

	public void setInfo(Boolean info) {
		localConf.put(Configuration.FEATURE_INFO, info);
	}

	public void setPaginate(Boolean paginate) {
		localConf.put(Configuration.FEATURE_PAGINATE, paginate);
	}

	public void setLengthChange(Boolean lengthChange) {
		localConf.put(Configuration.FEATURE_LENGTHCHANGE, lengthChange);
	}

	public void setProcessing(Boolean processing) {
		localConf.put(Configuration.AJAX_PROCESSING, processing);
	}

	public void setServerSide(Boolean serverSide) {
		localConf.put(Configuration.AJAX_SERVERSIDE, serverSide);
	}
	
	public void setPaginationType(String paginationType) {
		localConf.put(Configuration.FEATURE_PAGINATIONTYPE, paginationType);
	}

	public void setSort(Boolean sort) {
		localConf.put(Configuration.FEATURE_SORT, sort);
	}

	public void setStateSave(String stateSave) {
		localConf.put(Configuration.FEATURE_STATESAVE, stateSave);
	}

	public void setFixedHeader(String fixedHeader) {
		localConf.put(Configuration.PLUGIN_FIXEDHEADER, fixedHeader);
	}

	public void setScroller(String scroller) {
		localConf.put(Configuration.PLUGIN_SCROLLER, scroller);
	}

	public void setColReorder(String colReorder) {
		localConf.put(Configuration.PLUGIN_COLREORDER, colReorder);
	}

	public void setScrollY(String scrollY) {
		localConf.put(Configuration.FEATURE_SCROLLY, scrollY);
	}

	public void setScrollCollapse(String scrollCollapse) {
		localConf.put(Configuration.FEATURE_SCROLLCOLLAPSE, scrollCollapse);
	}

	public void setFixedPosition(String fixedPosition) {
		localConf.put(Configuration.PLUGIN_FIXEDPOSITION, fixedPosition);
	}

	public void setLabels(String labels) {
		localConf.put(Configuration.EXTRA_LABELS, labels);
	}

	public void setOffsetTop(Integer fixedOffsetTop) {
		localConf.put(Configuration.PLUGIN_FIXEDOFFSETTOP, fixedOffsetTop);
	}

	public void setCdn(Boolean cdn) {
		localConf.put(Configuration.EXTRA_CDN, cdn);
	}

	public void setExport(String export) {
		localConf.put(Configuration.EXPORT_TYPES, export);
	}

	public String getLoadingType() {
		return this.loadingType;
	}

	public void setUrl(String url) {
		localConf.put(Configuration.AJAX_SOURCE, url);
		this.loadingType = "AJAX";
		this.url = url;
	}

	public void setJqueryUI(String jqueryUI) {
		localConf.put(Configuration.FEATURE_JQUERYUI, jqueryUI);
	}

	public void setPipelining(String pipelining) {
		localConf.put(Configuration.AJAX_PIPELINING, pipelining);
	}
	
	public void setPipeSize(Integer pipeSize){
		localConf.put(Configuration.AJAX_PIPESIZE, pipeSize);
	}
	
	public void setExportLinks(String exportLinks) {
		localConf.put(Configuration.EXPORT_LINKS, exportLinks);
	}

	public void setTheme(String theme) {
		localConf.put(Configuration.EXTRA_THEME, theme);
	}

	public void setThemeOption(String themeOption) {
		localConf.put(Configuration.EXTRA_THEMEOPTION, themeOption);
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setAppear(String appear) {
		localConf.put(Configuration.EXTRA_APPEAR, appear);
	}
	
	public void setLengthMenu(String lengthMenu){
		localConf.put(Configuration.FEATURE_LENGTHMENU, lengthMenu);
	}
	
	public void setCssStripes(String cssStripesClasses){
		localConf.put(Configuration.CSS_STRIPECLASSES, cssStripesClasses);
	}
	
	public void setServerData(String serverData) {
		localConf.put(Configuration.AJAX_SERVERDATA, serverData);
	}

	public void setServerParams(String serverParams) {
		localConf.put(Configuration.AJAX_SERVERPARAM, serverParams);
	}

	public void setServerMethod(String serverMethod) {
		localConf.put(Configuration.AJAX_SERVERMETHOD, serverMethod);
	}

	public void setDisplayLength(Integer displayLength) {
		localConf.put(Configuration.FEATURE_DISPLAYLENGTH, displayLength);
	}

	public void setDom(String dom) {
		localConf.put(Configuration.FEATURE_DOM, dom);
	}

	public void setFeatures(String customFeatures) {
		localConf.put(Configuration.EXTRA_CUSTOMFEATURES, customFeatures);
	}

	public void setPlugins(String customPlugins) {
		localConf.put(Configuration.EXTRA_CUSTOMPLUGINS, customPlugins);
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
			// TODO afficher un message d'erreur
			// TODO afficher une alerte javascript
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
		localConf.put(Configuration.CSS_STYLE, cssStyle);
	}

	public void setCssClass(String cssClass) {
		localConf.put(Configuration.CSS_CLASS, cssClass);
	}
}