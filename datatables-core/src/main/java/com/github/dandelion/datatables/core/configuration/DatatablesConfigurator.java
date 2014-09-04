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
package com.github.dandelion.datatables.core.configuration;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.ClassUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.constants.SystemConstants;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.i18n.LocaleResolver;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.i18n.StandardLocaleResolver;

/**
 * <p>
 * The {@link DatatablesConfigurator} is used to pick up different classes in
 * charge of the configuration loading, instantiate them and cache them.
 * 
 * <ul>
 * <li>The locale resolver, in charge of retrieving the current locale from
 * which the configuration will be loaded</li>
 * <li>The configuration loader, in charge of loading default and user
 * properties and resolve configurations groups</li>
 * </ul>
 * <p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 * @see LocaleResolver
 * @see StandardLocaleResolver
 * @see ConfigurationLoader
 * @see StandardConfigurationLoader
 */
public class DatatablesConfigurator {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(DatatablesConfigurator.class);

	private static ConfigurationLoader configurationLoader;
	private static LocaleResolver localeResolver;
	private static MessageResolver messageResolver;

	/**
	 * Return a uniq implementation of {@link LocaleResolver} using the
	 * following strategy:
	 * 
	 * <ol>
	 * <li>First, check if the <code>i18n.locale.resolver</code> has been
	 * defined in any of the user properties files. If so, tries to instantiate
	 * the class.</li>
	 * <li>If no specific LocaleResolver is defined in user properties, the
	 * default will be used</li>
	 * </ol>
	 * 
	 * @return an implementation {@link LocaleResolver}.
	 */
	@SuppressWarnings("unchecked")
	public static LocaleResolver getLocaleResolver() {
		Properties userProperties = null;
		String className = null;
		ConfigurationLoader configurationLoader = getConfigurationLoader();

		if (localeResolver == null) {

			try {
				userProperties = configurationLoader.loadUserConfiguration(Locale.getDefault());

				if(userProperties != null){
					try {
							className = userProperties.getProperty("i18n.locale.resolver");
					} catch (MissingResourceException e) {
	
						logger.debug("No custom LocaleResolver has been configured. Using default one.");
					}
				}

				if(className == null){
					Properties defaultProperties = configurationLoader.loadDefaultConfiguration();
					className = defaultProperties.getProperty("i18n.locale.resolver");
				}

				if (className != null) {
						Class<LocaleResolver> classProperty;
						try {
							classProperty = (Class<LocaleResolver>) ClassUtils.getClass(className);
							localeResolver = (LocaleResolver) ClassUtils.getNewInstance(classProperty);
						} catch (Exception e) {
							throw new ConfigurationLoadingException(e);
						}
				}
			} catch (ConfigurationLoadingException e) {
				throw new ConfigurationLoadingException("Unable to retrieve the LocaleResolver using the class '"
						+ className + "'", e);
			}
		}
		return localeResolver;
	}

	@SuppressWarnings("unchecked")
	public static MessageResolver getMessageResolver(HttpServletRequest request) {
		Properties userProperties = null;
		String className = null;
		ConfigurationLoader configurationLoader = getConfigurationLoader();

		if (messageResolver == null) {

			try {
				userProperties = configurationLoader.loadUserConfiguration(Locale.getDefault());

				if(userProperties != null){
					try {
							className = userProperties.getProperty("i18n.message.resolver");
					} catch (MissingResourceException e) {
	
						logger.debug("No custom MessageResolver has been configured. Using default one.");
					}
				}

				if(className == null){
					Properties defaultProperties = configurationLoader.loadDefaultConfiguration();
					className = defaultProperties.getProperty("i18n.message.resolver");
				}

				if (className != null) {
						Class<MessageResolver> classProperty;
						try {
							classProperty = (Class<MessageResolver>) ClassUtils.getClass(className);
							messageResolver = classProperty.getDeclaredConstructor(new Class[] { HttpServletRequest.class })
									.newInstance(request);
						} catch (Exception e) {
							throw new ConfigurationLoadingException(e);
						}
				}
			} catch (ConfigurationLoadingException e) {
				throw new ConfigurationLoadingException("Unable to retrieve the MessageResolver using the class '"
						+ className + "'", e);
			}
		}
		return messageResolver;
	}
	
	/**
	 * <p>
	 * Returns an implementation of {@link ConfigurationLoader} using the
	 * following strategy:
	 * <ol>
	 * <li>Check first if the <code>dandelion.datatables.confloader.class</code>
	 * system property is set and tries to instantiate it</li>
	 * <li>Otherwise, instantiate the {@link StandardConfigurationLoader} based
	 * on property files</li>
	 * </ol>
	 * 
	 * @return an implementation of {@link ConfigurationLoader}.
	 */
	public static ConfigurationLoader getConfigurationLoader() {

		if (configurationLoader == null) {

			logger.debug("Initializing the configuration loader...");

			if (StringUtils.isNotBlank(System.getProperty(SystemConstants.DANDELION_DT_CONFLOADER_CLASS))) {
				Class<?> clazz;
				try {
					clazz = ClassUtils.getClass(System.getProperty(SystemConstants.DANDELION_DT_CONFLOADER_CLASS));
					configurationLoader = (ConfigurationLoader) ClassUtils.getNewInstance(clazz);
				} catch (Exception e) {
					logger.warn(
							"Unable to instantiate the configured {} due to a {} exception. Falling back to the default one.",
							SystemConstants.DANDELION_DT_CONFLOADER_CLASS, e.getClass().getName(), e);
				} 
			}

			if (configurationLoader == null) {
				configurationLoader = new StandardConfigurationLoader();
			}
		}

		return configurationLoader;
	}

	
	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 */
	public static void clear() {
		configurationLoader = null;
		localeResolver = null;
	}
}