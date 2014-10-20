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
package com.github.dandelion.datatables.core.extension;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.asset.generator.js.JsFunction;
import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.datatables.core.generator.DatatableJQueryContent;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Processor used to execute a processor against an {@link Extension}.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ExtensionProcessor {

	private static Logger logger = LoggerFactory.getLogger(ExtensionProcessor.class);

	/**
	 * The table instance where extensions must be loaded.
	 */
	private final HtmlTable table;

	/**
	 * The buffer used for the DataTables initialization code.
	 */
	private final DatatableJQueryContent datatableContent;

	/**
	 * The map containing the DataTables parameters.
	 */
	private final Map<String, Object> mainConfig;

	public ExtensionProcessor(HtmlTable table, DatatableJQueryContent datatableContent, Map<String, Object> mainConfig) {
		this.table = table;
		this.datatableContent = datatableContent;
		this.mainConfig = mainConfig;
	}

	public void process(Collection<Extension> extensions) {
		if (extensions != null && !extensions.isEmpty()) {
			for (Extension extension : extensions) {
				process(extension);
			}
		}
	}

	public void process(Extension extension) {

		if (extension != null) {

			extension.setupWrapper(table);
			injectIntoMainJsFile(extension);
			injectIntoMainConfiguration(extension);
		}
	}

	/**
	 * 
	 * @param extension
	 */
	private void injectIntoMainJsFile(Extension extension) {

		// Extension configuration loading
		if (extension.getBeforeAll() != null) {
			datatableContent.appendToBeforeAll(extension.getBeforeAll().toString());
		}
		if (extension.getBeforeStartDocumentReady() != null) {
			datatableContent.appendToBeforeStartDocumentReady(extension.getBeforeStartDocumentReady().toString());
		}
		if (extension.getAfterStartDocumentReady() != null) {
			datatableContent.appendToAfterStartDocumentReady(extension.getAfterStartDocumentReady().toString());
		}
		if (extension.getBeforeEndDocumentReady() != null) {
			datatableContent.appendToBeforeEndDocumentReady(extension.getBeforeEndDocumentReady().toString());
		}
		if (extension.getAfterAll() != null) {
			datatableContent.appendToAfterAll(extension.getAfterAll().toString());
		}
		if (extension.getFunction() != null) {
			datatableContent.appendToDataTablesExtra(extension.getFunction());
		}

		// Extension custom configuration generator
		if (extension.getConfigGenerator() != null) {
			logger.debug("Custom configuration generator used: {}", extension.getConfigGenerator().getClass()
					.getSimpleName());

			Writer writer = new StringWriter();

			Map<String, Object> conf = extension.getConfigGenerator().generateConfig(table);

			try {
				JSONValue.writeJSONString(conf, writer);
			}
			catch (IOException e) {
				throw new DandelionException("Unable to convert the configuration into JSON", e);
			}

			datatableContent.appendToDataTablesExtraConf(writer.toString());
		}
	}

	/**
	 * 
	 * @param extension
	 */
	private void injectIntoMainConfiguration(Extension extension) {

		// Extra configuration setting
		if (extension.getParameters() != null) {

			for (Parameter param : extension.getParameters()) {

				// The module configuration already exists in the main
				// configuration
				if (mainConfig.containsKey(param.getName())) {

					if (mainConfig.get(param.getName()) instanceof JsFunction) {
						processJsFunction(param);
					}
					else if (mainConfig.get(param.getName()) instanceof JsSnippet) {
						processJavascriptSnippet(param);
					}
					else {
						processString(param);
					}
				}
				// No existing configuration in the main configuration, so we
				// just add it
				else {
					mainConfig.put(param.getName(), param.getValue());
				}
			}
		}
	}

	private void processJsFunction(Parameter conf) {

		JsFunction jsFunction = (JsFunction) mainConfig.get(conf.getName());
		StringBuilder newValue = null;

		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = new StringBuilder(((JsFunction) conf.getValue()).getCode());
			newValue.append(jsFunction.getCode());
			jsFunction.setCode(newValue.toString());
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND:
			newValue = new StringBuilder(jsFunction.getCode());
			newValue.append(((JsFunction) conf.getValue()).getCode());
			jsFunction.setCode(newValue.toString());
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case APPEND_WITH_SPACE:
			newValue = new StringBuilder(((JsFunction) conf.getValue()).getCode());
			newValue.append(" ");
			newValue.append(jsFunction.getCode());
			jsFunction.setCode(newValue.toString());
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND_WITH_SPACE:
			newValue = new StringBuilder(jsFunction.getCode());
			newValue.append(" ");
			newValue.append(((JsFunction) conf.getValue()).getCode());
			jsFunction.setCode(newValue.toString());
			mainConfig.put(conf.getName(), jsFunction);
			break;

		default:
			break;
		}
	}

	private void processJavascriptSnippet(Parameter conf) {

		JsSnippet jsSnippet = (JsSnippet) mainConfig.get(conf.getName());
		String newValue = null;

		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = ((JsSnippet) conf.getValue()).getJavascript() + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND:
			newValue = jsSnippet.getJavascript() + ((JsSnippet) conf.getValue()).getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case APPEND_WITH_SPACE:
			newValue = ((JsSnippet) conf.getValue()).getJavascript() + " " + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND_WITH_SPACE:
			newValue = jsSnippet.getJavascript() + " " + ((JsSnippet) conf.getValue()).getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		default:
			break;
		}

	}

	private void processString(Parameter conf) {
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
