/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.ClassUtils;

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

	public void loadExtensions(JsResource mainJsFile, Map<String, Object> mainConf) {
		
		registerBuiltInExtensions(table);
		registerCustomExtensions(table);
		
		ExtensionProcessor extensionProcessor = new ExtensionProcessor(table, mainJsFile, mainConf);
		extensionProcessor.process(table.getTableConfiguration().getInternalExtensions());
		
		Extension theme = TableConfig.CSS_THEME.valueFrom(table);
		if(theme != null){
			extensionProcessor.process(new HashSet<Extension>(Arrays.asList(theme)));			
		}
	}
	
	/**
	 * Add custom extensions (for now features and plugins) to the current table
	 * if they're activated.
	 * 
	 * @param table
	 *            the HtmlTable to update with custom extensions.
	 */
	private void registerBuiltInExtensions(HtmlTable table) {

		logger.debug("Scanning built-in extensions...");

		// Scanning custom extension based on the base.package property
		List<Extension> builtinExtensions = scanForExtensions(
				"com.github.dandelion.datatables.core.extension.plugin",
				"com.github.dandelion.datatables.core.extension.theme");

		// Load custom extension if enabled
		Set<String> extensionNames = TableConfig.MAIN_EXTENSION_NAMES.valueFrom(table);
		if (builtinExtensions != null && !builtinExtensions.isEmpty() && extensionNames != null && !extensionNames.isEmpty()) {
			for (String extensionToRegister : extensionNames) {
				for (Extension extension : builtinExtensions) {
					if (extensionToRegister.equalsIgnoreCase(extension.getName())) {
						table.getTableConfiguration().registerExtension(extension);
						logger.debug("Built-in extension {} registered", extension.getName());
						continue;
					}
				}
			}
		} 
	}
	
	/**
	 * Add custom extensions (for now features and plugins) to the current table
	 * if they're activated.
	 * 
	 * @param table
	 *            the HtmlTable to update with custom extensions.
	 */
	private void registerCustomExtensions(HtmlTable table) {

		String packageToScan = TableConfig.MAIN_EXTENSION_PACKAGE.valueFrom(table);
		if (StringUtils.isNotBlank(packageToScan)) {

			logger.debug("Scanning custom extensions...");

			// Scanning custom extension based on the base.package property
			List<Extension> customExtensions = scanForExtensions(packageToScan);

			// Load custom extension if enabled
			Set<String> extensionNames = TableConfig.MAIN_EXTENSION_NAMES.valueFrom(table);
			if (customExtensions != null && !customExtensions.isEmpty() && extensionNames != null && !extensionNames.isEmpty()) {
				for (String extensionToRegister : extensionNames) {
					for (Extension customExtension : customExtensions) {
						if (extensionToRegister.equals(customExtension.getName().toLowerCase())) {
							table.getTableConfiguration().registerExtension(customExtension);
							logger.debug("Custom extension {} registered", customExtension.getName());
							continue;
						}
					}
				}
			} else {
				logger.warn("A base backage to scan has been detected but no custom extension has been found");
			}
		}
	}
	
	private List<Extension> scanForExtensions(String... packageNames) {
		List<Extension> extensions = new ArrayList<Extension>();
		for(String packageName : packageNames){
			extensions.addAll(scanForExtensions(packageName));
		}
		return extensions;
	}
	
	private List<Extension> scanForExtensions(String packageName) {

		// Init return value
		List<Extension> retval = new ArrayList<Extension>();

		List<Class<?>> customExtensionClassList = null;
		
		try {
			customExtensionClassList = ClassUtils.getSubClassesInPackage(packageName, AbstractExtension.class);
		} catch (ClassNotFoundException e) {
			throw new ExtensionLoadingException("Unable to load extensions", e);
		} catch (IOException e) {
			throw new ExtensionLoadingException("Unable to access the package '" + packageName + "'", e);
		}
		
		// Instanciate all found classes
		for (Class<?> clazz : customExtensionClassList) {

			try {
				retval.add((AbstractExtension) ClassUtils.getClass(clazz.getName()).newInstance());
			} catch (InstantiationException e) {
				throw new ExtensionLoadingException("Unable to instanciate the class " + clazz.getName(), e);
			} catch (IllegalAccessException e) {
				throw new ExtensionLoadingException("Unable to access the class " + clazz.getName(), e);
			} catch (ClassNotFoundException e) {
				throw new ExtensionLoadingException("Unable to load the class " + clazz.getName(), e);
			}
		}

		return retval;
	}
}