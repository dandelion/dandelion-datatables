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
package com.github.dandelion.datatables.core.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.exception.DataNotFoundException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.html.HtmlDiv;
import com.github.dandelion.datatables.core.html.HtmlHyperlink;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.JsonIndentingWriter;
import com.github.dandelion.datatables.core.util.NameConstants;
import com.github.dandelion.datatables.core.util.ResourceHelper;

/**
 * <p>
 * Class in charge of web resources generation.
 * 
 * <p>
 * The generated JSON (DataTables configuration) is pretty printed using a
 * custom writer written by Elad Tabak.
 * 
 * @author Thibault Duchateau
 */
public class WebResourceGenerator {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(WebResourceGenerator.class);

	// Custom writer used to pretty JSON
	private Writer writer = new JsonIndentingWriter();

	/**
	 * The DataTables configuration generator.
	 */
	private static MainGenerator configGenerator;

	/**
	 * <p>
	 * Main method which generated the web resources (js and css files).
	 * 
	 * @param pageContext
	 *            Context of the servlet.
	 * @param table
	 *            Table from which the configuration is extracted.
	 * @return A string corresponding to the Javascript code to return to the
	 *         JSP.
	 * @throws DataNotFoundException
	 *             if the web service URL is wrong (only for AJAX datasource)
	 * @throws IOException
	 * @throws CompressionException
	 * @throws BadConfigurationException
	 */
	public WebResources generateWebResources(HtmlTable table)
			throws DataNotFoundException, CompressionException, IOException,
			BadConfigurationException {

		// Bean which stores all needed web resources (js, css)
		WebResources webResources = new WebResources();

		// Init the "configuration" map with the table informations
		// The configuration may be updated depending on the user's choices
		configGenerator = new MainGenerator();
		Map<String, Object> mainConf = configGenerator.generateConfig(table);

		/**
		 * Main configuration file building
		 */
		// We need to append a randomUUID in case of multiple tables exists in
		// the same JSP
		String tableId = table.getRandomId();
		JsResource mainJsFile = new JsResource(ResourceType.MAIN, NameConstants.DT_MAIN_JS
				+ tableId + ".js");
		mainJsFile.setTableId(table.getId());

		/**
		 * Extra files management
		 */
		if (table.getExtraFiles() != null && !table.getExtraFiles().isEmpty()) {
			extraFileManagement(mainJsFile, table);
		}

		/**
		 * Extension loading
		 */
		ExtensionLoader extensionLoader = new ExtensionLoader(table, mainJsFile, mainConf, webResources);		
		extensionLoader.load(table.getPlugins());
		extensionLoader.load(table.getFeatures());
		if(table.getTheme() != null){
			extensionLoader.load(Arrays.asList(table.getTheme()));			
		}
		
		/**
		 * Extra configuration management
		 */
		if(table.getExtraConfs() != null){
			extraConfManagement(mainJsFile, mainConf, table);			
		}

		/**
		 * Main configuration generation
		 */
		// Allways pretty prints the JSON
		JSONValue.writeJSONString(mainConf, writer);
					
		mainJsFile.appendToDataTablesConf(writer.toString());

		/**
		 * Export management
		 */
		if (table.isExportable()) {
			exportManagement(table, mainJsFile);
		}

		/**
		 * Table display
		 */
		if(StringUtils.isNotBlank(table.getAppear())){
			
			if("block".equals(table.getAppear())){
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId() + "').show();");			
			}
			else{
				if(StringUtils.isNotBlank(table.getAppearDuration())){
					mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId() + "').fadeIn(" + table.getAppearDuration() + ");");
				}
				else{
					mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId() + "').fadeIn();");
				}
			}
		}
		
		webResources.setMainJsFile(mainJsFile);

		return webResources;
	}

	/**
	 * <p>
	 * If the export attribute of the table tag has been set to true, this
	 * method will convert every ExportConf bean to a HTML link, corresponding
	 * to each activated export type.
	 * 
	 * <p>
	 * All the link are wrapped into a div which is inserted in the DOM using
	 * jQuery. The wrapping div can be inserted at multiple position, depending
	 * on the tag configuration.
	 * 
	 * <p>
	 * If the user didn't add any ExportTag, Dandelion-datatables will use the default
	 * configuration.
	 * 
	 * @param table
	 *            The HTML table where to get ExportConf beans.
	 * @param mainJsFile
	 *            The web resource to update
	 */
	private void exportManagement(HtmlTable table, JsResource mainJsFile) {

		// Init the wrapping HTML div
		HtmlDiv divExport = new HtmlDiv();
		divExport.addCssClass("dandelion_dataTables_export");
		
		// ExportTag have been added to the TableTag
		if (table.getExportConfMap() != null && table.getExportConfMap().size() > 0) {
			logger.debug("Generating export links");

			HtmlHyperlink link = null;

			// A HTML link is generated for each ExportConf bean
			for (ExportConf conf : table.getExportConfMap().values()) {

				link = new HtmlHyperlink();

				if (conf.getCssClass() != null) {
					link.setCssClass(conf.getCssClass());
				}

				if (conf.getCssStyle() != null) {
					link.setCssStyle(conf.getCssStyle());
					link.addCssStyle(";margin-left:2px;");
				}
				else{
					link.addCssStyle("margin-left:2px;");
				}

				link.setHref(conf.getUrl());
				link.setLabel(conf.getLabel());

				divExport.addContent(link.toHtml());
			}
		}

		for (ExportLinkPosition position : table.getExportLinkPositions()) {

			switch (position) {
			case BOTTOM_LEFT:
				divExport.addCssStyle("float:left;margin-right:10px;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_info').before('" + divExport.toHtml() + "');$('#" + table.getId()
						+ "_info').css('clear', 'none');");
				break;

			case BOTTOM_MIDDLE:
				divExport.addCssStyle("float:left;margin-left:10px;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_paginate').before('" + divExport.toHtml() + "');");
				break;

			case BOTTOM_RIGHT:
				divExport.addCssStyle("float:right;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_paginate').before('" + divExport.toHtml() + "');");
				break;

			case TOP_LEFT:
				divExport.addCssStyle("float:left;margin-right:10px;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_length').before('" + divExport.toHtml() + "');");
				break;

			case TOP_MIDDLE:
				divExport.addCssStyle("float:left;margin-left:10px;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_filter').before('" + divExport.toHtml() + "');");
				break;

			case TOP_RIGHT:
				divExport.addCssStyle("float:right;");
				mainJsFile.appendToBeforeEndDocumentReady("$('#" + table.getId()
						+ "_filter').before('" + divExport.toHtml() + "');");
				break;

			default:
				break;
			}
		}
	}

	/**
	 * If extraFile tag have been added, its content must be extracted and merge
	 * to the main js file.
	 * 
	 * @param mainFile
	 *            The resource to update with extraFiles.
	 * @param table
	 *            The HTML tale.
	 * @throws BadConfigurationException
	 *             if
	 */
	private void extraFileManagement(JsResource mainFile, HtmlTable table) throws IOException,
			BadConfigurationException {

		logger.info("Extra files found");

		for (ExtraFile file : table.getExtraFiles()) {

			switch (file.getInsert()) {
			case BEFOREALL:
				mainFile.appendToBeforeAll(ResourceHelper.getFileContentFromWebapp(file.getSrc()));
				break;

			case AFTERSTARTDOCUMENTREADY:
				mainFile.appendToAfterStartDocumentReady(ResourceHelper
						.getFileContentFromWebapp(file.getSrc()));
				break;

			case BEFOREENDDOCUMENTREADY:
				mainFile.appendToBeforeEndDocumentReady(ResourceHelper
						.getFileContentFromWebapp(file.getSrc()));
				break;

			case AFTERALL:
				mainFile.appendToAfterAll(ResourceHelper.getFileContentFromWebapp(file.getSrc()));
				break;

			default:
				throw new BadConfigurationException("Unable to get the extraFile " + file.getSrc());
			}
		}
	}

	/**
	 * Generates a jQuery AJAX call to be able to merge the server-generated
	 * DataTables configuration with the configuration stored in extraConf
	 * files. <br />
	 * Warning : this is a temporary method. The goal is to be able to generate
	 * the entire configuration server-side.
	 * 
	 * @param mainConf
	 * @param table
	 */
	private void extraConfManagement(JsResource mainJsFile, Map<String, Object> mainConf,
			HtmlTable table) throws BadConfigurationException {

		for (ExtraConf conf : table.getExtraConfs()) {
			StringBuffer extaConf = new StringBuffer();
			extaConf.append("$.ajax({url:\"");
			extaConf.append(conf.getSrc());
			extaConf.append("\",dataType: \"text\",type: \"GET\", async: false, success: function(extraProperties, xhr, response) {");
			extaConf.append("$.extend(true, oTable_");
			extaConf.append(table.getId());
			extaConf.append("_params, eval('(' + extraProperties + ')'));");
			extaConf.append("}, error : function(jqXHR, textStatus, errorThrown){");
			extaConf.append("console.log(textStatus);");
			extaConf.append("console.log(errorThrown);");
			extaConf.append("}});");
			extaConf.append("console.log(oTable_" + table.getId() + "_params);");
			mainJsFile.appendToBeforeStartDocumentReady(extaConf.toString());
		}

		// TODO Old way here, trying to parse in JSON the content of extraConf
		// file
		// TODO using Jackson but "function" keyword is not JSON compliant

		// // Jackson object mapper
		// ObjectMapper mapper = new ObjectMapper();
		//
		// Map<String, Object> extraConf = new HashMap<String, Object>();
		// Map<String, Object> tmpMap;
		// for (ExtraConf conf : table.getExtraConfs()) {
		// String confStr =
		// ResourceHelper.getFileContentFromWebapp(conf.getSrc());
		// logger.debug("confStr = {}", confStr);
		// JsonNode newJson = JsonUtils.convertStringToJsonNode(confStr);
		// logger.debug("newJson = {}", newJson);
		// JsonNode oldJson = JsonUtils.convertObjectToJsonNode(mainConf);
		// logger.debug("oldJson = {}", oldJson);
		//
		// JsonNode result = JsonUtils.merge(oldJson, newJson);
		// logger.debug("result = {}", result);
		//
		// tmpMap = (Map<String, Object>)
		// JsonUtils.convertJsonNodeToObject(result, Map.class);
		// extraConf.putAll(tmpMap);
		// }
		//
		// logger.debug("extraConf = {}", extraConf);
		// mainConf.putAll(extraConf);
	}
}