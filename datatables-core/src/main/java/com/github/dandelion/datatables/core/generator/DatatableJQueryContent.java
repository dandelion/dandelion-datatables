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
package com.github.dandelion.datatables.core.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.asset.generator.js.jquery.JQueryContent;
import com.github.dandelion.core.utils.StringBuilderUtils;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Extension of {@link JQueryContent} designed for buffering DataTable
 * configuration.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class DatatableJQueryContent extends JQueryContent {

	protected static Logger logger = LoggerFactory.getLogger(DatatableJQueryContent.class);

	/**
	 * Processed DOM id of the table. This id is processed because JavaScript
	 * variables are created from it and not all characters are allowed.
	 */
	private final String processedId;

	/**
	 * Original DOM id of the table.
	 */
	private final String originalId;

	// TODO remove
	private final StringBuilder dataTablesExtra;
	private final StringBuilder dataTablesExtraConf;

	/**
	 * <p>
	 * Build and fill a new instance of {@link DatatableJQueryContent} from the
	 * provided {@link HtmlTable}.
	 * </p>
	 * 
	 * @param htmlTable
	 *            The table and its configuration.
	 */
	public DatatableJQueryContent(HtmlTable htmlTable) {

		this.dataTablesExtra = new StringBuilder();
		this.dataTablesExtraConf = new StringBuilder();
		this.processedId = htmlTable.getId();
		this.originalId = htmlTable.getOriginalId();

		/**
		 * Main configuration file building
		 */
		logger.debug("Generating the main configuration for the table with id: {}", this.originalId);
		DatatableConfigGenerator configGenerator = new DatatableConfigGenerator();
		Map<String, Object> mainConf = configGenerator.generateConfig(htmlTable);

		/**
		 * Extension loading
		 */
		logger.debug("Loading extensions for the table with id: {}", this.originalId);
		ExtensionLoader extensionLoader = new ExtensionLoader(htmlTable);
		extensionLoader.loadExtensions(this, mainConf);

		/**
		 * Main configuration generation
		 */
		logger.debug("Transforming configuration to JSON...");
		Writer writer = null;
		try {
			writer = new StringWriter();
			JSONValue.writeJSONString(mainConf, writer);
		}
		catch (IOException e) {
			throw new DandelionException("Unable to generate the JSON configuration", e);
		}

		/**
		 * Finalization
		 */
		appendToBeforeAll(getJavaScriptVariables(this, writer.toString()).toString());
		appendToComponentConfiguration(getComponentConf(this).toString());
	}

	public void appendToDataTablesExtra(String dataTablesExtra) {
		this.dataTablesExtra.append(dataTablesExtra);
	}

	public void appendToDataTablesExtraConf(String dataTablesExtraConf) {
		this.dataTablesExtraConf.append(dataTablesExtraConf);
	}

	private StringBuilder getComponentConf(DatatableJQueryContent datatableContent) {

		StringBuilder datatablesConfiguration = new StringBuilder();

		datatablesConfiguration.append("oTable_").append(this.processedId).append(".DataTable(oTable_")
				.append(this.processedId).append("_params)");

		if (StringBuilderUtils.isNotBlank(this.dataTablesExtra)) {
			datatablesConfiguration.append(".");
			datatablesConfiguration.append(this.dataTablesExtra);
			datatablesConfiguration.append("(");
			if (StringBuilderUtils.isNotBlank(this.dataTablesExtra)) {
				datatablesConfiguration.append(this.dataTablesExtraConf);
			}
			datatablesConfiguration.append(")");
		}

		datatablesConfiguration.append(";");

		return datatablesConfiguration;
	}

	private StringBuilder getJavaScriptVariables(DatatableJQueryContent datatableAssetBuffer, String datatableConfig) {

		StringBuilder variables = new StringBuilder();
		variables.append("var oTable_").append(this.processedId).append("=$('#").append(this.originalId).append("');");
		variables.append("var oTable_").append(this.processedId).append("_params=").append(datatableConfig).append(";");

		return variables;
	}
}
