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
package com.github.dandelion.datatables.core.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.model.HtmlTable;

/**
 * Aggregates the default Dandelion-datatables properties file with a potential custom one.
 * The custom properties will override default ones.
 *
 * @author Thibault Duchateau
 */
public class PropertiesLoader {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	// Properties files location
	public final static String DT_DEFAULT_PROPERTIES = "config/datatables-default.properties";
	public final static String DT_CUSTOM_PROPERTIES = "datatables.properties";
	
	/**
	 * Load the properties in the table using : <li>first, the global
	 * Dandelion-datatables properties file <li>second, the project specific properties
	 * file, if it exists
	 * 
	 * @param table
	 *            The table where to load properties.
	 */
	public static void load(HtmlTable table) throws BadConfigurationException {
		
		// Initialize properties
		Properties propertiesResource = new Properties();

		// Get current classloader
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		// Get default file as stream
		InputStream propertiesStream = classLoader.getResourceAsStream(DT_DEFAULT_PROPERTIES);

		try {
			// Load all default properties
			propertiesResource.load(propertiesStream);
		} catch (IOException e) {
			throw new BadConfigurationException("Unable to load the default configuration file", e);
		}

		// Next, try to get the custom properties file
		propertiesStream = classLoader.getResourceAsStream(DT_CUSTOM_PROPERTIES);

		if (propertiesStream != null) {

			Properties customProperties = new Properties();
			try {
				// Load project-specific properties
				customProperties.load(propertiesStream);
			} catch (IOException e) {
				throw new BadConfigurationException("Unable to load the project-specific configuration file", e);
			}

			// If custom properties have been loaded, we merge the properties
			// Custom properties will override default ones
			propertiesResource.putAll(customProperties);
		} else {
			logger.info("No custom file datatables.properties has been found. Using default one.");
		}
		
		table.getTableProperties().initProperties(propertiesResource);
	}
}