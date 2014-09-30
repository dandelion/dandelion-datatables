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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.ServiceLoaderUtils;
import com.github.dandelion.core.utils.Validate;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.generator.DatatableAssetBuffer;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Loader for all extensions : features, plugins, themes.
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 */
public class ExtensionLoader {

	private static Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

	private final HtmlTable table;

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
	 */
	public ExtensionLoader(HtmlTable table) {
		this.table = table;
	}

	public void loadExtensions(DatatableAssetBuffer mainJsFile, Map<String, Object> mainConf) {

		registerExtensions(table);

		ExtensionProcessor extensionProcessor = new ExtensionProcessor(table, mainJsFile, mainConf);
		extensionProcessor.process(table.getTableConfiguration().getInternalExtensions());

		Extension theme = DatatableOptions.CSS_THEME.valueFrom(table.getTableConfiguration());
		if (theme != null) {
			extensionProcessor.process(new HashSet<Extension>(Arrays.asList(theme)));
		}
	}

	/**
	 * <p>
	 * Returns the {@link Extension} associated with the passed name or
	 * {@code null} if the {@link Extension} doesn't exist among all scanned
	 * extensions.
	 * 
	 * @param extensionName
	 *            The name of the {@link Extension} to retrieve.
	 * @return the corresponding {@link Extension}.
	 */
	public static Extension get(String extensionName) {

		Validate.notBlank(extensionName, "The extension name can't be blank");

		ServiceLoader<Extension> loadedExtensions = ServiceLoader.load(Extension.class);

		for (Extension ex : loadedExtensions) {
			if (ex.getExtensionName().equalsIgnoreCase(extensionName)) {
				return ex;
			}
		}

		throw new DandelionException("The requested extension \"" + extensionName
				+ "\" is not present in the classpath.");
	}

	/**
	 * Add custom extensions (for now features and plugins) to the current table
	 * if they're activated.
	 * 
	 * @param table
	 *            the HtmlTable to update with custom extensions.
	 */
	private void registerExtensions(HtmlTable table) {

		logger.debug("Scanning for extensions...");

		// Get all available extensions from the classpath
		List<Extension> builtInExtensions = ServiceLoaderUtils.getProvidersAsList(Extension.class);

		// Load built-in extension if some are enabled
		Set<String> extensionNames = DatatableOptions.MAIN_EXTENSION_NAMES.valueFrom(table.getTableConfiguration());
		if (builtInExtensions != null && !builtInExtensions.isEmpty() && extensionNames != null
				&& !extensionNames.isEmpty()) {
			for (String extensionToRegister : extensionNames) {
				for (Extension extension : builtInExtensions) {
					if (extensionToRegister.equalsIgnoreCase(extension.getExtensionName())) {
						table.getTableConfiguration().registerExtension(extension);
						logger.debug("Extension '{}' registered in table '{}'", extension.getExtensionName(),
								table.getId());
						continue;
					}
				}
			}
		}
	}
}