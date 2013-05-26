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

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.CollectionUtils;
import com.github.dandelion.datatables.core.util.JsonIndentingWriter;
import com.github.dandelion.datatables.core.util.NameConstants;
import com.github.dandelion.datatables.core.util.Predicate;
import com.github.dandelion.datatables.core.util.ResourceHelper;

/**
 * <p>
 * Loader for all extensions : features, plugins, themes.
 * <p>
 * 
 * @author Thibault Duchateau
 */
public class ExtensionLoader {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

	private HtmlTable table;
	private JsResource mainJsFile;
	private Map<String, Object> mainConfig;
	private WebResources webResources;

	/**
	 * Constructor of the ExtensionLoader.
	 * 
	 * @param table
	 *            The table containing module informations.
	 * @param mainJsFile
	 *            The main Javascript file which will be generated and may be
	 *            updated accordingly to modules.
	 * @param mainConfig
	 *            Main DataTables configuration which may be updated accordingly
	 *            to modules.
	 * @param webResources
	 *            The wrapper POJO containing all web resources to generate.
	 * @throws BadConfigurationException
	 * @throws IOException
	 */
	public ExtensionLoader(HtmlTable table, JsResource mainJsFile, Map<String, Object> mainConfig,
			WebResources webResources) {
		this.table = table;
		this.mainJsFile = mainJsFile;
		this.mainConfig = mainConfig;
		this.webResources = webResources;
	}

	/**
	 * Load all type of extension (plugin, feature, theme).
	 * 
	 * @param extensions
	 *            The collection of extensions to load.
	 * @throws BadConfigurationException
	 * @throws IOException
	 */
	public void load(Set<? extends Extension> extensions)
			throws BadConfigurationException, IOException {

		if (extensions != null && !extensions.isEmpty()) {

			// TODO tester s'il faut appender un random number
			for (Extension extension : extensions) {

				// Extension initialization
				extension.setup(table);

				// TODO
				loadJsResources(extension);

				// TODO
				loadCssResources(table, extension);
				
				// TODO
				loadExternalCssResources(extension);
				
				// TODO
				injectIntoMainJsFile(extension);

				// TODO
				injectIntoMainConfiguration(extension);
			}
		}
	}

	/**
	 * Load potential JS resource of the current extension.
	 * 
	 * @param extension
	 *            The extension to load.
	 * @throws BadConfigurationException
	 */
	private void loadJsResources(Extension extension) throws BadConfigurationException {

		JsResource extensionJsFile = null;
		String resourceName = null;

		// Extension javascript
		if (extension.getJsResources() != null && !extension.getJsResources().isEmpty()) {

			resourceName = extension.getAppendRandomNumber() ? extension.getName().toLowerCase()
					+ "-" + table.getRandomId() + ".js" : extension.getName().toLowerCase() + ".js";

			//
			extensionJsFile = new JsResource(ResourceType.EXTENSION, resourceName);

			StringBuilder jsContent = new StringBuilder();

			// All JS resources are merged
			for (JsResource jsResource : extension.getJsResources()) {
				jsContent.append(ResourceHelper.getFileContentFromClasspath(jsResource
						.getLocation()));
			}

			extensionJsFile.setContent(jsContent.toString());
			webResources.getJavascripts().put(extensionJsFile.getName(), extensionJsFile);
		}
	}

	
	/**
	 * TODO
	 * @param extension
	 */
	private void loadExternalCssResources(Extension extension){
		
		Predicate<CssResource> isExternalCss = new Predicate<CssResource>() {
		    public boolean apply(CssResource cssResource) {
		        return cssResource.getType().equals(ResourceType.EXTERNAL);
		    }
		};

		if(extension.getCssResources() != null){
			
			List<CssResource> externalCssResources = (List<CssResource>) CollectionUtils.filter(extension.getCssResources(), isExternalCss);
			
			if(externalCssResources != null && !externalCssResources.isEmpty()){
				for(CssResource cssResource : externalCssResources){
					webResources.getStylesheets().put(cssResource.getName(), cssResource);
				}
			}
		}
	}
	
	/**
	 * Load potential CSS resource of the current extension.
	 * 
	 * @param extension
	 *            The extension to load.
	 * @throws BadConfigurationException
	 */
	private void loadCssResources(HtmlTable table, Extension extension) throws BadConfigurationException {

		CssResource pluginsSourceCssFile = null;
		String resourceName = null;

		// Extension javascript
		if (extension.getCssResources() != null && !extension.getCssResources().isEmpty()) {

			resourceName = extension.getAppendRandomNumber() ? NameConstants.DT_PLUGIN_JS
					+ extension.getName().toLowerCase() + "-" + table.getRandomId() + ".css"
					: NameConstants.DT_PLUGIN_JS + extension.getName().toLowerCase() + ".css";

			pluginsSourceCssFile = new CssResource(ResourceType.EXTENSION, resourceName);

			StringBuilder cssContent = new StringBuilder();

			// Module source loading (stylesheets)
			for (CssResource cssResource : extension.getCssResources()) {
				// Most of CSS resource have a type different from EXTERNAL, which is theme-specific
				if(!cssResource.getType().equals(ResourceType.EXTERNAL)){
					cssContent.append(ResourceHelper.getFileContentFromClasspath(cssResource
							.getLocation()));					
				}
			}

			pluginsSourceCssFile.setContent(cssContent.toString());
			webResources.getStylesheets().put(pluginsSourceCssFile.getName(), pluginsSourceCssFile);
		}
	}

	/**
	 * 
	 * @param extension
	 * @throws IOException
	 */
	private void injectIntoMainJsFile(Extension extension) throws IOException {

		// Extension configuration loading
		if (extension.getBeforeAll() != null) {
			mainJsFile.appendToBeforeAll(extension.getBeforeAll().toString());
		}
		if (extension.getAfterStartDocumentReady() != null) {
			mainJsFile.appendToAfterStartDocumentReady(extension.getAfterStartDocumentReady()
					.toString());
		}
		if (extension.getBeforeEndDocumentReady() != null) {
			mainJsFile.appendToBeforeEndDocumentReady(extension.getBeforeEndDocumentReady()
					.toString());
		}
		if (extension.getAfterAll() != null) {
			mainJsFile.appendToAfterAll(extension.getAfterAll().toString());
		}

		// TODO
		if (extension.getFunction() != null) {
			mainJsFile.appendToDataTablesExtra(extension.getFunction());
		}

		// Extension custom configuration generator
		if (extension.getConfigGenerator() != null) {
			logger.debug("A custom configuration generator has been set");

			Writer writer = new JsonIndentingWriter();

			Map<String, Object> conf = extension.getConfigGenerator().generateConfig(table);

			// Allways pretty prints the JSON
			JSONValue.writeJSONString(conf, writer);

			mainJsFile.appendToDataTablesExtraConf(writer.toString());
		}
	}

	/**
	 * 
	 * @param extension
	 */
	private void injectIntoMainConfiguration(Extension extension) {

		// Extra configuration setting
		if (extension.getConfs() != null) {

			for (Parameter conf : extension.getConfs()) {

				// The module configuration already exists in the main
				// configuration
				if (mainConfig.containsKey(conf.getName())) {
					
					if(mainConfig.get(conf.getName()) instanceof JavascriptFunction){
						processJavascriptFunction(conf);
					}
					else if(mainConfig.get(conf.getName()) instanceof JavascriptSnippet){
						processJavascriptSnippet(conf);
					}
					else{
						processString(conf);
					}
				}
				// No existing configuration in the main configuration, so we
				// just add it
				else {
					mainConfig.put(conf.getName(), conf.getValue());
				}
			}
		}
	}
	
	private void processJavascriptFunction(Parameter conf){
		
		JavascriptFunction jsFunction = (JavascriptFunction) mainConfig.get(conf.getName());
		String newValue = null;
		
		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = ((JavascriptFunction)conf.getValue()).getJavascript() + jsFunction.getJavascript();
			jsFunction.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND:
			newValue = jsFunction.getJavascript() + ((JavascriptFunction)conf.getValue()).getJavascript();
			jsFunction.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case APPEND_WITH_SPACE:
			newValue = ((JavascriptFunction)conf.getValue()).getJavascript() + " " + jsFunction.getJavascript();
			jsFunction.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND_WITH_SPACE:
			newValue = jsFunction.getJavascript() + " " + ((JavascriptFunction)conf.getValue()).getJavascript();
			jsFunction.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		default:
			break;
		}
	}
	
	private void processJavascriptSnippet(Parameter conf){
	
		JavascriptSnippet jsSnippet = (JavascriptSnippet) mainConfig.get(conf.getName());
		String newValue = null;
		
		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = ((JavascriptSnippet)conf.getValue()).getJavascript() + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND:
			newValue = jsSnippet.getJavascript() + ((JavascriptSnippet)conf.getValue()).getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case APPEND_WITH_SPACE:
			newValue = ((JavascriptSnippet)conf.getValue()).getJavascript() + " " + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND_WITH_SPACE:
			newValue = jsSnippet.getJavascript() + " " + ((JavascriptSnippet)conf.getValue()).getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		default:
			break;
		}
		
	}
	
	private void processString(Parameter conf){
		String value = null;
		
		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			value = (String) mainConfig.get(conf.getName());
			value = value + conf.getValue();
			mainConfig.put(conf.getName(), value);
			break;

		case PREPEND:
			value = (String) mainConfig.get(conf.getName());
			value = conf.getValue() + value;
			mainConfig.put(conf.getName(), value);
			break;

		case APPEND_WITH_SPACE:
			value = (String) mainConfig.get(conf.getName());
			value = value + " " + conf.getValue();
			mainConfig.put(conf.getName(), value);
			break;

		case PREPEND_WITH_SPACE:
			value = (String) mainConfig.get(conf.getName());
			value = conf.getValue() + " " + value;
			mainConfig.put(conf.getName(), value);
			break;

		default:
			break;
		}
	}
}