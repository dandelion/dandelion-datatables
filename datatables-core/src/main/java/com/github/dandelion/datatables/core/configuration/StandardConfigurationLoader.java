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
package com.github.dandelion.datatables.core.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Common abstract superclass for all {@link ConfigurationLoaderOld}, used to
 * load the Dandelion-Datatables configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class StandardConfigurationLoader implements ConfigurationLoader {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(StandardConfigurationLoader.class);

	public final static String DT_DEFAULT_PROPERTIES = "config/datatables-default.properties";
	public final static String DT_USER_PROPERTIES = "datatables";

	protected static Properties defaultProperties;
	private ResourceBundle userProperties;

	/**
	 * {@inheritDoc}
	 */
	public Properties loadDefaultConfiguration() throws ConfigurationLoadingException {

		logger.debug("Loading default configuration...");

		if (defaultProperties == null) {

			// Initialize properties
			Properties propertiesResource = new Properties();

			// Get default file as stream
			InputStream propertiesStream = null;

			try {
				propertiesStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DT_DEFAULT_PROPERTIES);
				propertiesResource.load(propertiesStream);
			} catch (IOException e) {
				throw new ConfigurationLoadingException("Unable to load the default configuration file", e);
			}
			finally {
				if (propertiesStream != null) {
					try {
						propertiesStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			defaultProperties = propertiesResource;
		}

		logger.debug("Default configuration loaded");
		
		return defaultProperties;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceBundle loadUserConfiguration(Locale locale) throws ConfigurationLoadingException {

		// First check if the resource bundle is externalized
		if(StringUtils.isNotBlank(System.getProperty(SystemConstants.DANDELION_DT_CONFIGURATION))){
			
			String path = System.getProperty(SystemConstants.DANDELION_DT_CONFIGURATION);
			
			try {
				URL resourceURL = new File(path).toURI().toURL();
				URLClassLoader urlLoader = new URLClassLoader(new URL[] { resourceURL });
				userProperties = ResourceBundle.getBundle(DT_USER_PROPERTIES, locale, urlLoader);
			} 
			catch (MalformedURLException e) {
				logger.warn("Wrong path to the externalized bundle", e);
			}
			catch (MissingResourceException e) {
				logger.info("No *.properties file in {}. Trying to lookup in classpath...");
			} 
			
		}

		// No system property is set, retrieves the bundle from the classpath
		if(userProperties == null){
			try {
				userProperties = ResourceBundle.getBundle(DT_USER_PROPERTIES, locale);
			} catch (MissingResourceException e) {
				// if no resource bundle is found, try using the context classloader
				try {
					userProperties = ResourceBundle.getBundle(DT_USER_PROPERTIES, locale, Thread.currentThread()
							.getContextClassLoader());
				} catch (MissingResourceException mre) {
					logger.debug("No custom configuration. Using default one.");
				}
			}
		}
		
		return userProperties;
	}

	/**
	 * {@inheritDoc}
	 */
	public void resolveGroups(Map<String, TableConfiguration> map) {

		Enumeration<String> keys = userProperties.getKeys();

		// Get all group names
		Set<String> groups = new HashSet<String>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			groups.add(key.substring(0, key.indexOf(".")));
		}

		// Merge default and user properties
		Properties defaultProps = new Properties();
		defaultProps.putAll(defaultProperties);
		keys = userProperties.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			defaultProps.setProperty(key, userProperties.getString(key));
		}

		// Compute configuration to apply on each group
		Map<Configuration, Object> stagingConf = null;
		for (String groupName : groups) {
			stagingConf = new HashMap<Configuration, Object>();
			for (Entry<Object, Object> entry : defaultProps.entrySet()) {
				String key = entry.getKey().toString();
				if (key.startsWith(groupName)) {
					Configuration configuration = Configuration.findByName(key.substring(groupName.length() + 1));
					if (configuration != null) {
						stagingConf.put(configuration, entry.getValue().toString());
					} else {
						logger.warn("The property {} (inside the {} group) doesn't exist",
								key.substring(groupName.length() + 1), groupName);
					}
				}
			}
			map.put(groupName, new TableConfiguration(stagingConf));
		}
	}
}