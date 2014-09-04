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

import com.github.dandelion.core.asset.generator.jquery.JQueryAssetBuffer;
import com.github.dandelion.datatables.core.exception.WebResourceGenerationException;
import com.github.dandelion.datatables.core.extension.ExtensionLoader;
import com.github.dandelion.datatables.core.generator.configuration.DatatablesGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class DatatableAssetBuffer extends JQueryAssetBuffer {

	private DatatableAssetBuffer() {
	}

	protected static Logger logger = LoggerFactory.getLogger(DatatableAssetBuffer.class);

	private String processedId;
	private String originalId;

	private StringBuilder dataTablesConf;
	private StringBuilder dataTablesExtra;
	private StringBuilder dataTablesExtraConf;

	public String getProcessedId() {
		return processedId;
	}

	public void setProcessedId(String processedId) {
		this.processedId = processedId;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public StringBuilder getDataTablesConf() {
		return dataTablesConf;
	}

	public void appendToDataTablesConf(String dataTablesConf) {
		if (this.dataTablesConf == null) {
			this.dataTablesConf = new StringBuilder();
		}
		this.dataTablesConf.append(dataTablesConf);
	}

	public StringBuilder getDataTablesExtra() {
		return dataTablesExtra;
	}

	public void appendToDataTablesExtra(String dataTablesExtra) {
		if (this.dataTablesExtra == null) {
			this.dataTablesExtra = new StringBuilder();
		}
		this.dataTablesExtra.append(dataTablesExtra);
	}

	public StringBuilder getDataTablesExtraConf() {
		return dataTablesExtraConf;
	}

	public void appendToDataTablesExtraConf(String dataTablesExtraConf) {
		if (this.dataTablesExtraConf == null) {
			this.dataTablesExtraConf = new StringBuilder();
		}
		this.dataTablesExtraConf.append(dataTablesExtraConf);
	}

	public static DatatableAssetBuffer create(HtmlTable table) {

		/**
		 * Main configuration file building
		 */
		DatatableAssetBuffer dab = new DatatableAssetBuffer();
		dab.setOriginalId(table.getOriginalId());
		dab.setProcessedId(table.getId());

		// Init the "configuration" map with the table informations
		// The configuration may be updated depending on the user's choices
		DatatablesGenerator configGenerator = new DatatablesGenerator();
		Map<String, Object> mainConf = configGenerator.generateConfig(table);

		/**
		 * Extension loading
		 */
		logger.debug("Loading extensions...");
		ExtensionLoader extensionLoader = new ExtensionLoader(table);
		extensionLoader.loadExtensions(dab, mainConf);

		/**
		 * Main configuration generation
		 */
		logger.debug("Transforming configuration to JSON...");

		try {
			Writer writer = new StringWriter();
			JSONValue.writeJSONString(mainConf, writer);
			dab.appendToDataTablesConf(writer.toString());
		}
		catch (IOException e) {
			throw new WebResourceGenerationException("Unable to generate the JSON configuration", e);
		}

		return dab;
	}
}
