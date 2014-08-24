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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.core.asset.locator.impl.DelegateLocator;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.asset.ExtraJs;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.DatatableBundles;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtmlFeature;
import com.github.dandelion.datatables.core.extension.feature.ExtraJsFeature;
import com.github.dandelion.datatables.core.generator.WebResourceGenerator;
import com.github.dandelion.datatables.core.generator.javascript.JavascriptGenerator;
import com.github.dandelion.datatables.core.html.ExtraHtml;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.html.HtmlTag;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.config.ConfType;
import com.github.dandelion.datatables.thymeleaf.util.RequestUtils;

/**
 * <p>
 * Element processor applied to the HTML {@code div} tag in order to finalize
 * the configuration.
 * 
 * @author Thibault Duchateau
 * @see {@link TableInitializerElProcessor#processMarkup}
 */
public class TableFinalizerElProcessor extends AbstractElProcessor {

	private static Logger logger = LoggerFactory.getLogger(TableFinalizerElProcessor.class);

	public TableFinalizerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPrecedence() {
		return 50000;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element,
			HttpServletRequest request, HttpServletResponse response, HtmlTable htmlTable) {

		if (htmlTable != null) {

			@SuppressWarnings("unchecked")
			Map<ConfigToken<?>, Object> stagingConf = (Map<ConfigToken<?>, Object>) RequestUtils.getFromRequest(
					DataTablesDialect.INTERNAL_BEAN_TABLE_STAGING_CONF, request);
			
			applyLocalConfiguration(arguments, request, htmlTable, stagingConf);
			
			TableConfig.applyConfiguration(stagingConf, htmlTable);
			TableConfig.processConfiguration(htmlTable);

			// The table is being exported
			if (ExportUtils.isTableBeingExported(request, htmlTable)) {
				setupExport(arguments, htmlTable);
			}
			// The table must be displayed
			else {
				setupHtml(arguments, request, htmlTable);
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
	 * @param request
	 *            The current {@link HttpServletRequest}.
	 * @param htmlTable
	 *            The {@link HtmlTable} which the local configuration will apply
	 *            on.
	 * @param stagingConf
	 *            The staging configuration to applied on the current
	 *            {@code HtmlTable} instance.
	 */
	@SuppressWarnings("unchecked")
	private void applyLocalConfiguration(Arguments arguments, HttpServletRequest request, HtmlTable htmlTable, Map<ConfigToken<?>, Object> stagingConf) {
		
		Map<String, Map<ConfType, Object>> configs = (Map<String, Map<ConfType, Object>>) RequestUtils.getFromRequest(
				DataTablesDialect.INTERNAL_BEAN_CONFIGS, request);
		
		if(configs != null){
			if(configs.containsKey(htmlTable.getId())){
				
				// Export
				Map<String, ExportConf> overloadedExportConf = (Map<String, ExportConf>)configs.get(htmlTable.getId()).get(ConfType.EXPORT);
				if(overloadedExportConf != null && !overloadedExportConf.isEmpty()){
					htmlTable.getTableConfiguration().getExportConfiguration().putAll(overloadedExportConf);
				}
				
				// Callbacks
				List<Callback> callbacks = (List<Callback>) configs.get(htmlTable.getId()).get(ConfType.CALLBACK);
				if(callbacks != null && !callbacks.isEmpty()){
					htmlTable.getTableConfiguration().setCallbacks(callbacks);
				}
				
				// ExtraJs
				Set<ExtraJs> extraJs = (Set<ExtraJs>) configs.get(htmlTable.getId()).get(ConfType.EXTRAJS);
				if(extraJs != null && !extraJs.isEmpty()){
					htmlTable.getTableConfiguration().setExtraJs(extraJs);
					htmlTable.getTableConfiguration().registerExtension(new ExtraJsFeature());
				}
				
				// ExtraHtml
				List<ExtraHtml> extraHtmls = (List<ExtraHtml>) configs.get(htmlTable.getId()).get(ConfType.EXTRAHTML);
				if(extraHtmls != null && !extraHtmls.isEmpty()){
					htmlTable.getTableConfiguration().setExtraHtmlSnippets(extraHtmls);
					htmlTable.getTableConfiguration().registerExtension(new ExtraHtmlFeature());
				}
				
				// Configuration properties
				Map<ConfigToken<?>, Object> localConf = (Map<ConfigToken<?>, Object>) configs.get(htmlTable.getId()).get(ConfType.PROPERTY);
				if(localConf != null && !localConf.isEmpty()){
					stagingConf.putAll(localConf);
				}
			}
			else{
				logger.warn("No configuration was found for the table with id '{}'", htmlTable.getId());
			}
		}
		else{
			logger.debug("No configuration to apply, i.e. no '" + DataTablesDialect.DIALECT_PREFIX
					+ ":conf' has been found in the current template.");
		}
		
		// The config node (the one with the dt:conf attribute), if it exists
		Element configNode = (Element) RequestUtils.getFromRequest(DataTablesDialect.INTERNAL_NODE_CONFIG, request);
		if (configNode != null) {
			configNode.getParent().removeChild(configNode);
		}
	}

	/**
	 * <p>
	 * Applies the CSS configuration (coming from {@link TableConfig} and
	 * {@link ColumnConfig} to the current table.
	 * 
	 * @param arguments
	 *            The Thymeleaf arguments.
	 * @param request
	 *            The current {@link HttpServletRequest}.
	 * @param htmlTable
	 *            The {@link HtmlTable} which the CSS configuration will apply
	 *            on.
	 */
	private void applyCssConfiguration(Arguments arguments, HttpServletRequest request, HtmlTable htmlTable) {

		Element tableElement = (Element) RequestUtils.getFromRequest(DataTablesDialect.INTERNAL_NODE_TABLE, request);

		// CSS class
		StringBuilder configuredCssClass = TableConfig.CSS_CLASS.valueFrom(htmlTable.getTableConfiguration());
		if (configuredCssClass != null) {

			String currentCssClass = tableElement.getAttributeValue("class");
			if (StringUtils.isNotBlank(currentCssClass)) {
				currentCssClass += HtmlTag.CLASS_SEPARATOR + configuredCssClass.toString();
			}
			else {
				currentCssClass = configuredCssClass.toString();
			}
			tableElement.setAttribute("class", currentCssClass);
		}

		// CSS style
		StringBuilder configuredCssStyle = TableConfig.CSS_STYLE.valueFrom(htmlTable.getTableConfiguration());
		if (configuredCssStyle != null) {

			String currentCssStyle = tableElement.getAttributeValue("style");
			if (StringUtils.isNotBlank(currentCssStyle)) {
				currentCssStyle += HtmlTag.STYLE_SEPARATOR + configuredCssStyle.toString();
			}
			else {
				currentCssStyle = configuredCssStyle.toString();
			}
			tableElement.setAttribute("style", currentCssStyle);
		}
	}
	
	/**
	 * Sets up the export properties, before the filter intercepts the response.
	 * 
	 * @param arguments
	 *            The Thymeleaf arguments.
	 * @param htmlTable
	 *            The {@link HtmlTable} to export.
	 */
	private void setupExport(Arguments arguments, HtmlTable htmlTable) {

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		HttpServletResponse response = ((IWebContext) arguments.getContext()).getHttpServletResponse();

		String currentExportType = ExportUtils.getCurrentExportType(request);

		htmlTable.getTableConfiguration().setExporting(true);
		htmlTable.getTableConfiguration().setCurrentExportFormat(currentExportType);

		// Call the export delegate
		ExportDelegate exportDelegate = new ExportDelegate(htmlTable, request);
		exportDelegate.prepareExport();

		response.reset();
	}

	/**
	 * <p>
	 * Sets up the required configuration to display the table.
	 * 
	 * @param arguments
	 *            The Thymeleaf arguments.
	 * @param request
	 *            The current request.
	 * @param htmlTable
	 *            The {@link HtmlTable} which HTML and assets must be generated
	 *            from.
	 */
	private void setupHtml(Arguments arguments, HttpServletRequest request, HtmlTable htmlTable) {
		
		htmlTable.getTableConfiguration().setExporting(false);

		// Init the web resources generator
		WebResourceGenerator contentGenerator = new WebResourceGenerator(htmlTable);

		// Generate the web resources (JS, CSS) and wrap them into a
		// WebResources POJO
		JsResource jsResource = contentGenerator.generateWebResources();
		logger.debug("Web content generated successfully");

		applyCssConfiguration(arguments, request, htmlTable);
		
		// Asset stack update
		AssetRequestContext.get(request)
			.addBundles(DatatableBundles.DATATABLES)
			.addBundles(DatatableBundles.DDL_DT.getBundleName())
			.addParameter("dandelion-datatables", DelegateLocator.DELEGATED_CONTENT_PARAM,
						DatatablesConfigurator.getJavascriptGenerator(), false);
		
		// Buffering generated Javascript
		JavascriptGenerator javascriptGenerator = AssetRequestContext.get(request).getParameterValue("dandelion-datatables", DelegateLocator.DELEGATED_CONTENT_PARAM);
		javascriptGenerator.addResource(jsResource);
	}
}