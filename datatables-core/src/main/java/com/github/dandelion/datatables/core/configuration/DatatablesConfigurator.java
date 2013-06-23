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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.i18n.LocaleResolver;
import com.github.dandelion.datatables.core.util.ClassUtils;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * <p>
 * Configurator used to set the {@link ConfigurationLoaderOld} that will be used
 * to load specific configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class DatatablesConfigurator {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(DatatablesConfigurator.class);

	public final static String DT_USER_PROPERTIES = "datatables";

	private static ConfigurationLoader confLoader;
	private static LocaleResolver localeResolver;

	/**
	 * <p>Return an instance of {@link ConfigurationLoader} using the following
	 * strategy:
	 * <ul>
	 * <li>Check first if the <code>dandelion.datatables.confloader.class</code>
	 * system property is set and tries to instantiate it</li>
	 * <li>Instantiate the {@link StandardConfigurationLoader}</li>
	 * </ul>
	 * 
	 * @return an instance of {@link ConfigurationLoader}.
	 */
	public static ConfigurationLoader getConfigurationLoader() {

		if(confLoader == null){
		
			logger.debug("Initializing the configuration loader...");
	
			if (StringUtils.isNotBlank(System.getProperty(SystemConstants.DANDELION_DT_CONFLOADER_CLASS))) {
				Class<?> clazz;
				try {
					clazz = ClassUtils.getClass(System.getProperty(SystemConstants.DANDELION_DT_CONFLOADER_CLASS));
					confLoader = (ConfigurationLoader) ClassUtils.getNewInstance(clazz);
				} catch (BadConfigurationException e) {
					logger.warn("The custom configuration loader {} has not been found in the classpath. Falling back to the default one");
				}
			}
			
			if(confLoader == null){
				confLoader = new StandardConfigurationLoader();
			}
		}
		
		return confLoader;
	}


	/**
	 * Return an instance of {@link LocaleResolver}.
	 * 
	 * @return an instance of {@link LocaleResolver}.
	 * @throws ConfigurationLoadingException
	 *             if the class that implements {@link LocaleResolver} cannot be
	 *             instantiated.
	 */
	@SuppressWarnings("unchecked")
	public static LocaleResolver getLocaleResolver() {
		ResourceBundle userProperties = null;
		String className = null;
		ConfigurationLoader configurationLoader = new StandardConfigurationLoader();
		
		if (localeResolver == null) {

			try {
				userProperties = configurationLoader.loadUserConfiguration(Locale.getDefault());

				try{
					className = userProperties.getString("i18n.locale.resolver");
				}
				catch (MissingResourceException e){
					
					logger.debug("No custom LocaleResolver has been configured. Using default one.");
					Properties defaultProperties = configurationLoader.loadDefaultConfiguration(); 
					className = defaultProperties.getProperty("i18n.locale.resolver");
				}
				
				if(className != null){
					try {
						Class<LocaleResolver> classProperty = (Class<LocaleResolver>) ClassUtils
								.classForName(className);
						localeResolver = classProperty.newInstance();
					} catch (Throwable e) {
						//		                    log.warn(Messages.getString("TableProperties.errorloading", //$NON-NLS-1$
						// new Object[]{
						// ClassUtils.getShortClassName(LocaleResolver.class),
						// e.getClass().getName(),
						// e.getMessage()}));
					}
				}
			} catch (ConfigurationLoadingException e) {
				logger.error("Unable to retrieve the LocaleResolver using the class {}", className);
			}
		}
		return localeResolver;
	}

	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 */
	public static void clear(){
		confLoader = null;
		localeResolver = null;
	}
}