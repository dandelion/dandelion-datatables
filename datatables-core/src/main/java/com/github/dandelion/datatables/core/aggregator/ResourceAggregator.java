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
package com.github.dandelion.datatables.core.aggregator;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.NameConstants;
import com.github.dandelion.datatables.core.util.ResourceHelper;

/**
 * Web resources aggregator.
 * 
 * @author Thibault Duchateau
 */
public class ResourceAggregator {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ResourceAggregator.class);

	/**
	 * Main routine of the aggregator which launches different type of
	 * aggregation depending on the Dandelion-datatables configuration.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to aggregate.
	 * @param table
	 *            The table containing the datatatables4j configuration.
	 */
	public static void processAggregation(WebResources webResources, HtmlTable table) {

		logger.debug("Processing aggregation, using configuration {}", table.getTableConfiguration()
				.getMainAggregatorMode());

		switch (table.getTableConfiguration().getMainAggregatorMode()) {
		case ALL:
			aggregateAll(webResources);
			break;

		case PLUGINS_JS:
			aggregatePluginsJs(webResources);
			break;

		case PLUGINS_CSS:
			aggregatePluginsCss(webResources);
			break;

		default:
			break;
		}
	}

	/**
	 * All web resources are aggregated. <li>All javascript resources will be
	 * merge into one file <li>All stylesheets resources will be merge into one
	 * file, if there is some.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to aggregate.
	 */
	public static void aggregateAll(WebResources webResources) {

		// Only aggregate if there's more than 1 JS file
		if (webResources.getJavascripts().size() > 0) {

			String jsResourceName = NameConstants.DT_AGG_ALL_JS + ResourceHelper.getRamdomNumber()
					+ ".js";

			JsResource aggregateJsFile = new JsResource(ResourceType.AGGREGATE, jsResourceName);
			String aggregatedJsContent = "";

			for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {
				aggregatedJsContent += entry.getValue().getContent();
			}
			aggregatedJsContent += webResources.getMainJsFile().getContent();
			aggregateJsFile.setContent(aggregatedJsContent);

			// All existing Javascript resources are removed
			webResources.getJavascripts().clear();

			webResources.setMainJsFile(aggregateJsFile);
//			 Add aggregated Javascript resource
//			webResources.getJavascripts().put(aggregateJsFile.getName(), aggregateJsFile);
		}

		// Only aggregate if there's more than 1 CSS file
		if (webResources.getStylesheets().size() > 1) {
			String cssResourceName = NameConstants.DT_AGG_ALL_CSS
					+ ResourceHelper.getRamdomNumber() + ".css";

			CssResource aggregateCssFile = new CssResource(cssResourceName);

			String aggregatedCssContent = "";

			for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {
				aggregatedCssContent += entry.getValue().getContent();
			}
			aggregateCssFile.setContent(aggregatedCssContent);

			// All existing Stylesheets resources are removed
			webResources.getStylesheets().clear();

			// Add aggregated stylesheet resource
			webResources.getStylesheets().put(aggregateCssFile.getName(), aggregateCssFile);
		}

		logger.debug("Aggregation (ALL) completed");
	}

	/**
	 * Only javascript resources are aggregated. The other ones remain
	 * unchanged.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to aggregate.
	 */
	public static void aggregatePluginsJs(WebResources webResources) {

		String jsResourceName = NameConstants.DT_AGG_PLUGINS_JS + ResourceHelper.getRamdomNumber()
				+ ".js";
		JsResource aggregatePluginsJsFile = new JsResource(ResourceType.AGGREGATE, jsResourceName);

		String aggregatedJsContent = "";

		for (Entry<String, JsResource> entry : webResources.getJavascripts().entrySet()) {

			// Filter plugin only
			if (entry.getValue().getType().equals(ResourceType.PLUGIN)) {
				aggregatedJsContent += entry.getValue().getContent();
			}

			// Remove the plugin JsResource from the map
			webResources.getJavascripts().remove(entry.getKey());
		}

		aggregatePluginsJsFile.setContent(aggregatedJsContent);

		webResources.getJavascripts().put(aggregatePluginsJsFile.getName(), aggregatePluginsJsFile);

		logger.debug("Aggregation (PLUGINS_JS) completed");
	}

	/**
	 * Only stylesheet resources are aggregated. The other ones remain
	 * unchanged.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to aggregate.
	 */
	public static void aggregatePluginsCss(WebResources webResources) {

		String cssResourceName = NameConstants.DT_AGG_PLUGINS_CSS
				+ ResourceHelper.getRamdomNumber() + ".css";
		CssResource aggregatePluginsCssFile = new CssResource(cssResourceName);

		String aggregatedCssContent = "";

		for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {

			// Filter plugin only
			if (entry.getValue().getType().equals(ResourceType.PLUGIN)) {
				aggregatedCssContent += entry.getValue().getContent();
			}

			// Remove the plugin JsResource from the map
			webResources.getStylesheets().remove(entry.getKey());
		}

		aggregatePluginsCssFile.setContent(aggregatedCssContent);

		webResources.getStylesheets().put(aggregatePluginsCssFile.getName(),
				aggregatePluginsCssFile);

		logger.debug("Aggregation (PLUGINS_CSS) completed");
	}
}
