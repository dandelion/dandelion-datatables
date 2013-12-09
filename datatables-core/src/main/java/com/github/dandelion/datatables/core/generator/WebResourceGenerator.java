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
import java.util.Map;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.exception.WebResourceGenerationException;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.generator.configuration.DatatablesGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.JsonIndentingWriter;

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

	private HtmlTable table;
	
	/**
	 * The DataTables configuration generator.
	 */
	private static DatatablesGenerator configGenerator;
	
	public WebResourceGenerator(HtmlTable table){
		this.table = table;
	}
	
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
	 */
	public JsResource generateWebResources() {

		/**
		 * Main configuration file building
		 */
		JsResource mainJsFile = new JsResource(table.getId(), table.getOriginalId());
		
		// Init the "configuration" map with the table informations
		// The configuration may be updated depending on the user's choices
		configGenerator = new DatatablesGenerator();
		Map<String, Object> mainConf = configGenerator.generateConfig(table);

		/**
		 * Extension loading
		 */
		logger.debug("Loading extensions...");
		ExtensionLoader extensionLoader = new ExtensionLoader(table);
		extensionLoader.loadExtensions(mainJsFile, mainConf);
		
		/**
		 * Main configuration generation
		 */
		logger.debug("Transforming configuration to JSON...");
		// Allways pretty prints the JSON
		try {
			Writer writer = new JsonIndentingWriter();
			JSONValue.writeJSONString(mainConf, writer);
			mainJsFile.appendToDataTablesConf(writer.toString());
		} catch (IOException e) {
			throw new WebResourceGenerationException("Unable to generate the JSON configuration", e);
		}

		return mainJsFile;
	}
}