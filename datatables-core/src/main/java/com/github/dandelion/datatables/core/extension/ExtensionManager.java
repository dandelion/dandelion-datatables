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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.feature.AbstractFeature;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.plugin.AbstractPlugin;
import com.github.dandelion.datatables.core.util.ReflectHelper;

/**
 * Extension manager.
 *
 * @author Thibault Duchateau
 */
public class ExtensionManager {

	// Logger
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		

	/**
	 * Add custom extensions (for now features and plugins) to the current table
	 * if they're activated.
	 * 
	 * @param table
	 *            the HtmlTable to update with custom extensions.
	 * @throws BadConfigurationException
	 */
	public void registerCustomExtensions(HtmlTable table) throws BadConfigurationException{

		if (StringUtils.isNotBlank(table.getTableConfiguration().getBasePackage())) {

			logger.debug("Scanning custom extensions...");

			// Scanning custom extension based on the base.package property
			List<AbstractFeature> customFeatures = ReflectHelper.scanForFeatures(table.getTableConfiguration()
					.getBasePackage());

			// Load custom extension if enabled
			if (customFeatures != null && !customFeatures.isEmpty() && table.getTableConfiguration().getExtraCustomFeatures() != null) {
				for (String extensionToRegister : table.getTableConfiguration().getExtraCustomFeatures()) {
					for (AbstractFeature customFeature : customFeatures) {
						if (extensionToRegister.equals(customFeature.getName().toLowerCase())) {
							table.getTableConfiguration().registerFeature(customFeature);
							logger.debug("Feature {} (version: {}) registered", customFeature.getName(),
									customFeature.getVersion());
							continue;
						}
					}
				}
			} else {
				logger.debug("No custom feature found");
			}

			// Scanning custom extension based on the base.package property
			List<AbstractPlugin> customPlugins = ReflectHelper.scanForPlugins(table.getTableConfiguration()
					.getBasePackage());

			// Load custom extension if enabled
			if (customPlugins != null && !customPlugins.isEmpty() && table.getTableConfiguration().getExtraCustomPlugins() != null) {
				for (String extensionToRegister : table.getTableConfiguration().getExtraCustomPlugins()) {
					for (AbstractPlugin customPlugin : customPlugins) {
						if (extensionToRegister.equals(customPlugin.getName().toLowerCase())) {
							table.getTableConfiguration().registerPlugin(customPlugin);
							logger.debug("Plugin {} (version: {}) registered", customPlugin.getName(),
									customPlugin.getVersion());
							continue;
						}
					}
				}
			} else {
				logger.debug("No custom plugin found");
			}
		}
		else{
			logger.debug("The 'base.package' property is blank. Unable to scan any class.");
		}
	}
}
