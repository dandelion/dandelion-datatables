package com.github.dandelion.datatables.core.extension;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.JsonIndentingWriter;

public class ExtensionProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExtensionProcessor.class);
		
	private HtmlTable table;
	private JsResource mainJsFile;
	private Map<String, Object> mainConfig;
	
	public ExtensionProcessor(HtmlTable table, JsResource mainJsFile, Map<String, Object> mainConfig){
		this.table = table;
		this.mainJsFile = mainJsFile;
		this.mainConfig = mainConfig;
	}
	
	public void process(Collection<Extension> extensions) throws ExtensionLoadingException{
		if(extensions != null && !extensions.isEmpty()){
			for(Extension extension : extensions){
				process(extension);
			}
		}
	}
	public void process(Extension extension) throws ExtensionLoadingException{
	
		if (extension != null) {

			// Extension initialization
			extension.setupWrapper(table);
			injectIntoMainJsFile(extension);
			injectIntoMainConfiguration(extension);
		}
	}
	
	/**
	 * 
	 * @param extension
	 * @throws ExtensionLoadingException
	 */
	private void injectIntoMainJsFile(Extension extension) throws ExtensionLoadingException {

		// Extension configuration loading
		if (extension.getBeforeAll() != null) {
			mainJsFile.appendToBeforeAll(extension.getBeforeAll().toString());
		}
		if (extension.getAfterStartDocumentReady() != null) {
			mainJsFile.appendToAfterStartDocumentReady(extension.getAfterStartDocumentReady().toString());
		}
		if (extension.getBeforeEndDocumentReady() != null) {
			mainJsFile.appendToBeforeEndDocumentReady(extension.getBeforeEndDocumentReady().toString());
		}

		if (extension.getAfterAll() != null) {
			mainJsFile.appendToAfterAll(extension.getAfterAll().toString());
		}

		if (extension.getFunction() != null) {
			mainJsFile.appendToDataTablesExtra(extension.getFunction());
		}

		// Extension custom configuration generator
		if (extension.getConfigGenerator() != null) {
			logger.debug("A custom configuration generator has been set");

			Writer writer = new JsonIndentingWriter();

			Map<String, Object> conf = extension.getConfigGenerator().generateConfig(table);

			// Allways pretty prints the JSON
			try {
				JSONValue.writeJSONString(conf, writer);
			} catch (IOException e) {
				throw new ExtensionLoadingException("Unable to convert the configuration into JSON", e);
			}

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

					if (mainConfig.get(conf.getName()) instanceof JavascriptFunction) {
						processJavascriptFunction(conf);
					} else if (mainConfig.get(conf.getName()) instanceof JavascriptSnippet) {
						processJavascriptSnippet(conf);
					} else {
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

	private void processJavascriptFunction(Parameter conf) {

		JavascriptFunction jsFunction = (JavascriptFunction) mainConfig.get(conf.getName());
		String newValue = null;

		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = ((JavascriptFunction) conf.getValue()).getCode() + jsFunction.getCode();
			jsFunction.setCode(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND:
			newValue = jsFunction.getCode() + ((JavascriptFunction) conf.getValue()).getCode();
			jsFunction.setCode(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case APPEND_WITH_SPACE:
			newValue = ((JavascriptFunction) conf.getValue()).getCode() + " " + jsFunction.getCode();
			jsFunction.setCode(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		case PREPEND_WITH_SPACE:
			newValue = jsFunction.getCode() + " " + ((JavascriptFunction) conf.getValue()).getCode();
			jsFunction.setCode(newValue);
			mainConfig.put(conf.getName(), jsFunction);
			break;

		default:
			break;
		}
	}

	private void processJavascriptSnippet(Parameter conf) {

		JavascriptSnippet jsSnippet = (JavascriptSnippet) mainConfig.get(conf.getName());
		String newValue = null;

		switch (conf.getMode()) {
		case OVERRIDE:
			mainConfig.put(conf.getName(), conf.getValue());
			break;

		case APPEND:
			newValue = ((JavascriptSnippet) conf.getValue()).getJavascript() + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND:
			newValue = jsSnippet.getJavascript() + ((JavascriptSnippet) conf.getValue()).getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case APPEND_WITH_SPACE:
			newValue = ((JavascriptSnippet) conf.getValue()).getJavascript() + " " + jsSnippet.getJavascript();
			jsSnippet.setJavascript(newValue);
			mainConfig.put(conf.getName(), jsSnippet);
			break;

		case PREPEND_WITH_SPACE:
			newValue = jsSnippet.getJavascript() + " " + ((JavascriptSnippet) conf.getValue()).getJavascript();
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
