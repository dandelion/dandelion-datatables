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
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.aggregator.ResourceAggregator;
import com.github.dandelion.datatables.core.cache.AssetCache;
import com.github.dandelion.datatables.core.compressor.ResourceCompressor;
import com.github.dandelion.datatables.core.constants.CdnConstants;
import com.github.dandelion.datatables.core.constants.ResourceType;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.exception.DataNotFoundException;
import com.github.dandelion.datatables.core.exception.ExportException;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportProperties;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.model.CssResource;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.core.model.JsResource;
import com.github.dandelion.datatables.core.model.WebResources;
import com.github.dandelion.datatables.core.properties.PropertiesLoader;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.core.util.ResourceHelper;

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

	/**
	 * TODO
	 */
	public int doStartTag() throws JspException {
		// Just used to identify the first row (header)
		iterationNumber = 1;

		// Init the table with its DOM id and a generated random number
		table = new HtmlTable(id, ResourceHelper.getRamdomNumber());
		table.setCurrentUrl(RequestHelper.getCurrentUrl((HttpServletRequest) pageContext
				.getRequest()));

		try {
			// Load table properties
			PropertiesLoader.load(this.table);
		} catch (BadConfigurationException e) {
			throw new JspException("Unable to load Dandelion configuration");
		}

		// The table data are loaded using AJAX source
		if ("AJAX".equals(this.loadingType)) {

			this.table.addFooterRow();
			this.table.addHeaderRow();
			
			// Same domain AJAX request
			this.table.setDatasourceUrl(RequestHelper.getDatasourceUrl(url, pageContext.getRequest()));				
					
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
	 * TODO
	 */
	public int doAfterBody() throws JspException {

		this.iterationNumber++;

		return processIteration();
	}

	/**
	 * TODO
	 */
	public int doEndTag() throws JspException {

		try{
			// Update the HtmlTable POJO configuration with the attributes
			registerBasicConfiguration();			

			// Update the HtmlTable POJO with the export configuration
			registerExportConfiguration();			
		}
		catch (BadConfigurationException e){
			throw new JspException(e);
		}

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
		exportProperties.setExportConf(table.getExportConfMap().get(currentExportType));
		exportProperties.setFileName(table.getExportConfMap().get(currentExportType).getFileName());

		this.table.setExportProperties(exportProperties);
		this.table.setExporting(true);

		try {
			// Call the export delegate
			ExportDelegate exportDelegate = new ExportDelegate(table, exportProperties, request);
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
		String baseUrl = RequestHelper.getBaseUrl(pageContext.getRequest());
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		WebResources webResources = null;
		
		this.table.setExporting(false);

		// Register all activated modules
		registerPlugins();

		// Register all activated features
		registerFeatures();

		// Register theme if activated
		registerTheme();

		try {
			// First we check if the DataTables configuration already exist in the cache
			String keyToTest = RequestHelper.getCurrentUrl(request) + "|" + this.table.getId();

			if(!AssetCache.cache.containsKey(keyToTest)){
				logger.debug("No asset for the key {}. Generating...", keyToTest);
				
				// Init the web resources generator
				WebResourceGenerator contentGenerator = new WebResourceGenerator();
	
				// Generate the web resources (JS, CSS) and wrap them into a
				// WebResources POJO
				webResources = contentGenerator.generateWebResources(this.table);
				logger.debug("Web content generated successfully");
				
				AssetCache.cache.put(keyToTest, webResources);
				logger.debug("Cache updated with new web resources");
			}
			else{
				logger.debug("Asset(s) already exist, retrieving content from cache...");

				webResources = (WebResources) AssetCache.cache.get(keyToTest);
			}

			// Aggregation
			if (this.table.getTableProperties().isAggregatorEnable()) {
				logger.debug("Aggregation enabled");
				ResourceAggregator.processAggregation(webResources, table);
			}

			// Compression
			if (this.table.getTableProperties().isCompressorEnable()) {
				logger.debug("Compression enabled");
				ResourceCompressor.processCompression(webResources, table);
			}

			// <link> HTML tag generation
			if (this.table.getCdn()) {
				generateLinkTag(CdnConstants.CDN_DATATABLES_CSS);
			}
			for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {
				if(entry.getValue().getType().equals(ResourceType.EXTERNAL)){
					generateLinkTag(entry.getValue().getLocation());
				}
				else{
					generateLinkTag(baseUrl + "/datatablesController/" + entry.getKey() + "?id=" + this.table.getId() + "&c=" + RequestHelper.getCurrentUrl(request));
				}
			}

			// HTML generation
			pageContext.getOut().println(this.table.toHtml());

			// <script> HTML tag generation
			if (this.table.getCdn()) {
				generateScriptTag(CdnConstants.CDN_DATATABLES_JS_MIN);
			}
			for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {
				generateScriptTag(baseUrl + "/datatablesController/" + entry.getKey() + "?id=" + this.table.getId() + "&c=" + RequestHelper.getCurrentUrl(request));
			}
			// Main Javascript file
			generateScriptTag(baseUrl + "/datatablesController/" + webResources.getMainJsFile().getName() + "?id=" + this.table.getId() + "&c=" + RequestHelper.getCurrentUrl(request) + "&t=main");

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
}