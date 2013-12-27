/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.core.asset.wrapper.impl.DelegatedLocationWrapper;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.extension.feature.ExtraFileFeature;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.generator.javascript.JavascriptGenerator;
import com.github.dandelion.datatables.core.util.UrlUtils;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.config.ConfType;

/**
 * <p>
 * Element processor applied to the HTML {@code div} tag in order to finalize
 * the configuration.
 * 
 * @author Thibault Duchateau
 * @see {@link TableInitializerElProcessor#processMarkup}
 */
public class TableFinalizerElProcessor extends AbstractElProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableFinalizerElProcessor.class);

	public TableFinalizerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 50000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element) {

		if (this.table != null) {

			@SuppressWarnings("unchecked")
			Map<ConfigToken<?>, Object> stagingConf = (Map<ConfigToken<?>, Object>) getFromRequest(DataTablesDialect.INTERNAL_BEAN_TABLE_STAGING_CONF);
			
			applyLocalConfiguration(arguments, stagingConf);
			
			TableConfig.applyConfiguration(stagingConf, table);
			TableConfig.processConfiguration(table);

			applyCssConfiguration(arguments);
			
			// The table is being exported
			if (UrlUtils.isTableBeingExported(request, table)) {
				setupExport(arguments);
			}
			// The table must be generated and displayed
			else {
				setupHtmlGeneration(arguments, element, request);
			}
		}

		// The "finalizing div" can now be removed
		element.getParent().removeChild(element);

		return ProcessorResult.OK;
	}

	/**
	 * <p>
	 * Applies the local configuration (coming from the {@code div} marked with
	 * {@code dt:conf}) to the current table.
	 * 
	 * @param arguments
	 *            The Thymeleaf arguments.
	 * @param stagingConf
	 *            The staging configuration to applied on the current
	 *            {@code HtmlTable} instance.
	 */
	@SuppressWarnings("unchecked")
	private void applyLocalConfiguration(Arguments arguments, Map<ConfigToken<?>, Object> stagingConf) {
		
		Map<String, Map<ConfType, Object>> configs = (Map<String, Map<ConfType, Object>>) getFromRequest(DataTablesDialect.INTERNAL_BEAN_CONFIGS);
		
		if(configs != null){
			if(configs.containsKey(table.getId())){
				
				// Export
				Map<String, ExportConf> overloadedExportConf = (Map<String, ExportConf>)configs.get(table.getId()).get(ConfType.EXPORT);
				if(overloadedExportConf != null && !overloadedExportConf.isEmpty()){
					table.getTableConfiguration().getExportConfiguration().putAll(overloadedExportConf);
				}
				
				// Callbacks
				List<Callback> callbacks = (List<Callback>) configs.get(table.getId()).get(ConfType.CALLBACK);
				if(callbacks != null && !callbacks.isEmpty()){
					table.getTableConfiguration().setCallbacks(callbacks);
				}
				
				// Extrafile
				List<ExtraFile> extraFiles = (List<ExtraFile>) configs.get(table.getId()).get(ConfType.EXTRAFILE);
				if(extraFiles != null && !extraFiles.isEmpty()){
					table.getTableConfiguration().setExtraFiles(extraFiles);
					table.getTableConfiguration().registerExtension(new ExtraFileFeature());
				}
				
				// Configuration properties
				Map<ConfigToken<?>, Object> localConf = (Map<ConfigToken<?>, Object>) configs.get(table.getId()).get(ConfType.PROPERTY);
				if(localConf != null && !localConf.isEmpty()){
					stagingConf.putAll(localConf);
				}
			}
			else{
				logger.warn("No configuration was found for the table with id '{}'", table.getId());
			}
		}
		else{
			logger.debug("No configuration to apply, i.e. no '" + DataTablesDialect.DIALECT_PREFIX
					+ ":conf' has been found in the current template.");
		}
		
		Element configNode = (Element) getFromRequest(DataTablesDialect.INTERNAL_NODE_CONFIG);
		if(configNode != null){
			configNode.getParent().removeChild(configNode);
		}
	}

	private void applyCssConfiguration(Arguments arguments){
		
		Element tableElement = (Element) getFromRequest(DataTablesDialect.INTERNAL_NODE_TABLE);
		
		// CSS class
		StringBuilder configuredCssClass = TableConfig.CSS_CLASS.valueFrom(table.getTableConfiguration());
		if(configuredCssClass != null){
			
			String currentCssClass = tableElement.getAttributeValue("class");
			if(StringUtils.isNotBlank(currentCssClass)){
				currentCssClass += " " + configuredCssClass.toString();
			}
			else{
				currentCssClass = configuredCssClass.toString();
			}
			tableElement.setAttribute("class", currentCssClass);
		}
		
		// CSS style
		StringBuilder configuredCssStyle = TableConfig.CSS_STYLE.valueFrom(table.getTableConfiguration());
		if(configuredCssStyle != null){
			
			String currentCssStyle = tableElement.getAttributeValue("style");
			if(StringUtils.isNotBlank(currentCssStyle)){
				currentCssStyle += ";" + configuredCssStyle.toString();
			}
			else{
				currentCssStyle = configuredCssStyle.toString();
			}
			tableElement.setAttribute("style", currentCssStyle.toString());
		}
	}
	

	/**
	 * Set up the export properties, before the filter intercepts the response.
	 */
	private void setupExport(Arguments arguments) {
		logger.debug("Setting export up ...");

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		HttpServletResponse response = ((IWebContext) arguments.getContext()).getHttpServletResponse();

		String currentExportType = ExportUtils.getCurrentExportType(request);

		this.table.getTableConfiguration().setExporting(true);
		this.table.getTableConfiguration().setCurrentExportFormat(currentExportType);

		// Call the export delegate
		ExportDelegate exportDelegate = new ExportDelegate(this.table, request);
		exportDelegate.launchExport();

		response.reset();
	}

	/**
	 * Set up the HTML table generation.
	 */
	private void setupHtmlGeneration(Arguments arguments, Element element, HttpServletRequest request) {
		
		table.getTableConfiguration().setExporting(false);

		// Init the web resources generator
		WebResourceGenerator contentGenerator = new WebResourceGenerator(table);

		// Generate the web resources (JS, CSS) and wrap them into a
		// WebResources POJO
		JsResource jsResource = contentGenerator.generateWebResources();
		logger.debug("Web content generated successfully");

		// Scope update
		AssetsRequestContext.get(request)
			.addScopes(Scope.DATATABLES)
			.addScopes(Scope.DDL_DT.getScopeName())
			.addParameter("dandelion-datatables", DelegatedLocationWrapper.DELEGATED_CONTENT_PARAM,
						DatatablesConfigurator.getJavascriptGenerator(), false);
		
		// Buffering generated Javascript
		JavascriptGenerator javascriptGenerator = AssetsRequestContext.get(request).getParameterValue("dandelion-datatables", DelegatedLocationWrapper.DELEGATED_CONTENT_PARAM);
		javascriptGenerator.addResource(jsResource);
	}
}