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

import com.github.dandelion.core.asset.generator.js.jquery.JQueryContent;
import com.github.dandelion.core.asset.generator.js.jquery.JQueryContentGenerator;
import com.github.dandelion.core.asset.locator.impl.ApiLocator;
import com.github.dandelion.core.html.AbstractHtmlTag;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportDelegate;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtmlFeature;
import com.github.dandelion.datatables.core.extension.feature.ExtraJs;
import com.github.dandelion.datatables.core.extension.feature.ExtraJsFeature;
import com.github.dandelion.datatables.core.generator.DatatableJQueryContent;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Callback;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.util.ConfigUtils;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.config.ConfType;
import com.github.dandelion.datatables.thymeleaf.util.RequestUtils;

/**
 * <p>
 * Element processor applied to the HTML {@code div} tag in order to finalize
 * the configuration.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class TableFinalizerElProcessor extends AbstractElProcessor {

	private static Logger logger = LoggerFactory.getLogger(TableFinalizerElProcessor.class);

	public TableFinalizerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 50000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element,
			HttpServletRequest request, HttpServletResponse response, HtmlTable htmlTable) {

		if (htmlTable != null) {

			@SuppressWarnings("unchecked")
			Map<Option<?>, Object> stagingConf = (Map<Option<?>, Object>) RequestUtils.getFromRequest(
					DataTablesDialect.INTERNAL_BEAN_TABLE_STAGING_CONF, request);
			
			applyLocalConfiguration(arguments, request, htmlTable, stagingConf);
			
			ConfigUtils.applyStagingOptions(stagingConf, htmlTable);
			ConfigUtils.processOptions(htmlTable);

			// The table is being exported
			if (ExportUtils.isTableBeingExported(request, htmlTable)) {
				setupExport(arguments, htmlTable);
			}
			// The table must be displayed
			else {
				ConfigUtils.storeTableInRequest(request, htmlTable);
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
	private void applyLocalConfiguration(Arguments arguments, HttpServletRequest request, HtmlTable htmlTable, Map<Option<?>, Object> stagingConf) {
		
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
				Map<Option<?>, Object> localConf = (Map<Option<?>, Object>) configs.get(htmlTable.getId()).get(ConfType.PROPERTY);
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
		StringBuilder configuredCssClass = DatatableOptions.CSS_CLASS.valueFrom(htmlTable.getTableConfiguration());
		if (configuredCssClass != null) {

			String currentCssClass = tableElement.getAttributeValue("class");
			if (StringUtils.isNotBlank(currentCssClass)) {
				currentCssClass += AbstractHtmlTag.CLASS_SEPARATOR + configuredCssClass.toString();
			}
			else {
				currentCssClass = configuredCssClass.toString();
			}
			tableElement.setAttribute("class", currentCssClass);
		}

		// CSS style
		StringBuilder configuredCssStyle = DatatableOptions.CSS_STYLE.valueFrom(htmlTable.getTableConfiguration());
		if (configuredCssStyle != null) {

			String currentCssStyle = tableElement.getAttributeValue("style");
			if (StringUtils.isNotBlank(currentCssStyle)) {
				currentCssStyle += AbstractHtmlTag.STYLE_SEPARATOR + configuredCssStyle.toString();
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
		
		applyCssConfiguration(arguments, request, htmlTable);
		
		htmlTable.getTableConfiguration().setExporting(false);

		// Generate the JavaScript code according to the table and its configuration
		JQueryContent datatableContent = new DatatableJQueryContent(htmlTable);

		// Get the existing JavaScript generator or create it if it doesn't exist
		JQueryContentGenerator javascriptGenerator = AssetRequestContext.get(request).getParameterValue(
				"dandelion-datatables", ApiLocator.API_CONTENT_PARAM);

		if (javascriptGenerator == null) {
			javascriptGenerator = new JQueryContentGenerator(datatableContent);
		}
		else {
			javascriptGenerator.appendContent(datatableContent);
		}
		
		// Update the asset request context with the enabled bundles and
		// Javascript generator
		AssetRequestContext
			.get(request)
			.addBundles(DatatableBundles.DDL_DT)
			.addBundles(DatatableBundles.DATATABLES)
			.addParameter("dandelion-datatables", ApiLocator.API_CONTENT_PARAM, javascriptGenerator,
					false);
	}
}