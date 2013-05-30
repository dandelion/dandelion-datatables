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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;

/**
 * <p>
 * Specific implementation of {@link ConfigurationLoader} based on properties
 * files.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ConfigurationPropertiesLoader extends AbstractConfigurationLoader {

	public final static String DT_CUSTOM_PROPERTIES = "datatables.properties";
	public final static String KEY_SEP = ".";
	
	private Properties customProperties;
	
	@Override
	public void doLoadSpecificConfiguration() throws BadConfigurationException {

		// Initialize properties
		customProperties = new Properties();
					
		InputStream propertiesStream = null;

		// First try to load the properties using the system property
		if(StringUtils.isNotBlank(System.getProperty(SystemConstants.DANDELION_DT_CONF))){
			String propertiesLocation  = System.getProperty(SystemConstants.DANDELION_DT_CONF);
			
			try {
				propertiesStream = new FileInputStream(propertiesLocation);
				customProperties.load(propertiesStream);
				loadStagingConfiguration();
				return;
			} catch (FileNotFoundException e) {
				throw new BadConfigurationException("Unable to locate the custom configuration file", e);
			}
			catch (IOException e) {
				throw new BadConfigurationException("Unable to read the custom configuration file", e);
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
		}

		// Then, try to load from classpath
		try {
			propertiesStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DT_CUSTOM_PROPERTIES);
			customProperties.load(propertiesStream);
			loadStagingConfiguration();
		} catch (IOException e) {
			throw new BadConfigurationException("Unable to load the custom configuration file", e);
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
	}
	
	private void loadStagingConfiguration() throws BadConfigurationException{
		for(Entry<Object, Object> entry : customProperties.entrySet()){
			Configuration configuration = Configuration.findByName(entry.getKey().toString().substring(keyPrefix.length() + 1));
			if(configuration == null){
				throw new BadConfigurationException("The property '" + entry.getKey() + "' is invalid. Please see the documentation.");
			}
			else{
				stagingConf.put(configuration, entry.getValue().toString());
			}
		}
	}
}