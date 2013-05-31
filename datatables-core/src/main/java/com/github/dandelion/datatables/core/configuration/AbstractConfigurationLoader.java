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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;

/**
 * Common abstract superclass for all {@link ConfigurationLoader}, used to load the
 * Dandelion-Datatables configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public abstract class AbstractConfigurationLoader implements ConfigurationLoader {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(AbstractConfigurationLoader.class);

	public final static String DT_DEFAULT_PROPERTIES = "config/datatables-default.properties";
	
	protected static Properties globalProperties;
	
	protected Map<Configuration, Object> stagingConf;
	protected String keyPrefix;
	
	/**
	 * Load the Dandelion-Datatabls configuration from the default properties file.
	 * <ul>
	 * <li>first, the global Dandelion-datatables properties file</li>
	 * <li>second, the project specific properties file, if it exists</li>
	 * </ul>
	 */
	public void loadDefaultConfiguration() throws BadConfigurationException {

		logger.debug("Loading default configuration...");

		stagingConf = new HashMap<Configuration, Object>();
		
		if (globalProperties == null) {

			// Initialize properties
			Properties propertiesResource = new Properties();

			// Get default file as stream
			InputStream propertiesStream = null;

			try {
				propertiesStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DT_DEFAULT_PROPERTIES);
				propertiesResource.load(propertiesStream);
			} catch (IOException e) {
				throw new BadConfigurationException("Unable to load the default configuration file", e);
			} finally {
				if (propertiesStream != null) {
					try {
						propertiesStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			for(Entry<Object, Object> entry : propertiesResource.entrySet()){
				if(!entry.getKey().toString().equals("groups")){
					Configuration configuration = Configuration.findByName(entry.getKey().toString().substring(TableConfiguration.DEFAULT_GROUP_NAME.length() + 1));
					if(configuration != null){
						stagingConf.put(configuration, entry.getValue().toString());
					}
				}
			}
			
			globalProperties = propertiesResource;
		}
		
		logger.debug("Default configuration loaded");
	}

	/**
	 * {@inheritDoc}
	 * @throws BadConfigurationException 
	 */
	public void loadSpecificConfiguration(String keyPrefix) throws BadConfigurationException {

		this.keyPrefix = keyPrefix;
		
		logger.debug("Loading specific configuration...");
		try {
			doLoadSpecificConfiguration();
		} catch (BadConfigurationException e) {
			throw new BadConfigurationException(
					"Unable to load the custom configuration. Only the default one will be used.", e);
		}
		
		logger.debug("Specific configuration loaded");
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract void doLoadSpecificConfiguration() throws BadConfigurationException;
	
	/**
	 * Only used internally for testing.
	 * 
	 * @return the Map containing the stating loaded configuration.
	 */
	public Map<Configuration, Object> getStagingConfiguration(){
		return stagingConf;
	}
}