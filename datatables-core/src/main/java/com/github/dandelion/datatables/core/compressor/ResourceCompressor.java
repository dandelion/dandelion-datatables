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
package com.github.dandelion.datatables.core.compressor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.ResourceType;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.model.CssResource;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.core.model.JsResource;
import com.github.dandelion.datatables.core.model.WebResources;
import com.github.dandelion.datatables.core.util.ReflectHelper;

/**
 * Web resources compressor.
 * 
 * @author Thibault Duchateau
 */
public class ResourceCompressor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ResourceCompressor.class);

	/**
	 * Main routine of the compressor which launches different type of
	 * compression depending on the Dandelion-datatables configuration.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to compress.
	 * @param table
	 *            The table containing the datatatables4j configuration.
	 * @throws BadConfigurationException
	 *             if a property have a bad configuration.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	public static void processCompression(WebResources webResources, HtmlTable table)
			throws BadConfigurationException, CompressionException {

		logger.debug("Processing compression, using class {} and mode {}", table
				.getTableProperties().getCompressorClassName(), table.getTableProperties()
				.getCompressorMode());

		// Get the compressor helper instance
		ResourceCompressorDelegate compressorHelper = new ResourceCompressorDelegate(table);

		// If Dandelion-datatables has been manually installed, some jar might be
		// missing
		// So, first check if the CompressorClass exist in the classpath
		if (ReflectHelper.canBeUsed(table.getTableProperties().getCompressorClassName())) {

			switch (table.getTableProperties().getCompressorMode()) {
			case ALL:
				compressJavascript(webResources, compressorHelper);
				compressMainJavascript(webResources, compressorHelper);
				compressStylesheet(webResources, compressorHelper);
				break;
			case CSS:
				compressStylesheet(webResources, compressorHelper);
				break;
			case JS:
				compressJavascript(webResources, compressorHelper);
				compressMainJavascript(webResources, compressorHelper);
				break;
			default:
				break;

			}

			logger.debug("Compression completed");

		} else {
			logger.warn(
					"The compressor class {} hasn't been found in the classpath. Compression is disabled.",
					table.getTableProperties().getCompressorClassName());
		}

	}

	/**
	 * Compress only javascript resources using the implementation defined in
	 * the Dandelion-datatables properties file.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to compress.
	 * @param helper
	 *            The resource compressor helper, proxy of the real compressor
	 *            implementation.
	 * @throws BadConfigurationException
	 *             if no compressor implementation can be found.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	private static void compressJavascript(WebResources webResources,
			ResourceCompressorDelegate helper) throws BadConfigurationException,
			CompressionException {

		Map<String, JsResource> newJavascripts = new TreeMap<String, JsResource>();

		// Compress all Javascript resources
		for (Entry<String, JsResource> oldEntry : webResources.getJavascripts().entrySet()) {

			// Copy the entry
			JsResource minimifiedResource = oldEntry.getValue();

			// Update content using the compressor implementation
			minimifiedResource.setContent(helper.getCompressedJavascript(oldEntry.getValue()
					.getContent()));

			// Update name
			minimifiedResource.setName(oldEntry.getValue().getName().replace(".js", ".min.js"));

			// Update type
			minimifiedResource.setType(ResourceType.MINIMIFIED);

			// Add the new minified resource
			newJavascripts.put(minimifiedResource.getName(), minimifiedResource);
		}

		// Use the new map of compressed javascript file instead of the old
		// one
		webResources.setJavascripts(newJavascripts);
	}

	/**
	 * Compress the main Javascript file using the implementation defined in the
	 * Dandelion-datatables properties file.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing the resource to compress.
	 * @param helper
	 *            The resource compressor helper, proxy of the real compressor
	 *            implementation.
	 * @throws BadConfigurationException
	 *             if no compressor implementation can be found.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	private static void compressMainJavascript(WebResources webResources,
			ResourceCompressorDelegate helper) throws BadConfigurationException,
			CompressionException {

		// Get a copy of the main Javascript file
		JsResource minMainJsFile = webResources.getMainJsFile();

		// Update content using the compressor implementation
		minMainJsFile.setContent(helper.getCompressedJavascript(minMainJsFile.getContent()));

		// Update name
		minMainJsFile.setName(minMainJsFile.getName().replace(".js", ".min.js"));

		// Update type so that the servlet will get the content bean's property
		// instead of generating Datatables initialization code again
		minMainJsFile.setType(ResourceType.MINIMIFIED);

		// Override the previous JsResource with the minimified one
		webResources.setMainJsFile(minMainJsFile);
	}

	/**
	 * Compress only stylesheet resources using the implementation defined in
	 * the Dandelion-datatables properties file.
	 * 
	 * @param webResources
	 *            The wrapper POJO containing all web resources to compress.
	 * @param helper
	 *            The resource compressor helper, proxy of the real compressor
	 *            implementation.
	 * @throws BadConfigurationException
	 *             if no compressor implementation can be found.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	private static void compressStylesheet(WebResources webResources,
			ResourceCompressorDelegate helper) throws BadConfigurationException,
			CompressionException {

		Map<String, CssResource> newStylesheets = new TreeMap<String, CssResource>();

		// Compress all Stylesheet resources
		for (Entry<String, CssResource> entry : webResources.getStylesheets().entrySet()) {

			// Copy the entry
			CssResource minimifiedResource = entry.getValue();

			// Update content using the compressor implementation
			minimifiedResource.setContent(helper.getCompressedCss(entry.getValue().getContent()));

			// Update name
			minimifiedResource.setName(entry.getValue().getName().replace(".css", ".min.css"));

			newStylesheets.put(minimifiedResource.getName(), minimifiedResource);
		}

		// Use the new map of compressed stylesheets file instead of the old
		// one
		webResources.setStylesheets(newStylesheets);
	}
}