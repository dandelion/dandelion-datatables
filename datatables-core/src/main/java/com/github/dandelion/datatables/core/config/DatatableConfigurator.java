/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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
package com.github.dandelion.datatables.core.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.i18n.LocaleResolver;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.i18n.StandardLocaleResolver;
import com.github.dandelion.core.util.ClassUtils;

/**
 * <p>
 * The {@link DatatableConfigurator} is used to pick up different classes in
 * charge of the configuration loading, instantiate them and cache them.
 * </p>
 * <ul>
 * <li>The locale resolver, in charge of retrieving the current locale from
 * which the configuration will be loaded</li>
 * <li>The configuration loader, in charge of loading default and user
 * properties and resolve configurations groups</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 * @see LocaleResolver
 * @see StandardLocaleResolver
 * @see ConfigLoader
 * @see ConfigLoader
 */
public class DatatableConfigurator {

   private static Logger logger = LoggerFactory.getLogger(DatatableConfigurator.class);

   private static LocaleResolver localeResolver;
   private static MessageResolver messageResolver;

   /**
    * Return a uniq implementation of {@link LocaleResolver} using the following
    * strategy:
    * 
    * <ol>
    * <li>First, check if the <code>i18n.locale.resolver</code> has been defined
    * in any of the user properties files. If so, tries to instantiate the
    * class.</li>
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
      ConfigLoader configurationLoader = getConfigLoader();

      if (localeResolver == null) {

         try {
            userProperties = configurationLoader.loadUserConfiguration(Locale.getDefault());

            if (userProperties != null) {
               try {
                  className = userProperties.getProperty(ConfigLoader.I18N_LOCALE_RESOLVER);
               }
               catch (MissingResourceException e) {

                  logger.debug("No custom LocaleResolver has been configured. Using default one.");
               }
            }

            if (className == null) {
               Properties defaultProperties = configurationLoader.loadDefaultConfiguration();
               className = defaultProperties.getProperty(ConfigLoader.I18N_LOCALE_RESOLVER);
            }

            if (className != null) {
               Class<LocaleResolver> classProperty;
               try {
                  classProperty = (Class<LocaleResolver>) ClassUtils.getClass(className);
                  localeResolver = (LocaleResolver) ClassUtils.getNewInstance(classProperty);
               }
               catch (Exception e) {
                  throw new DandelionException(e);
               }
            }
         }
         catch (DandelionException e) {
            throw new DandelionException("Unable to retrieve the LocaleResolver using the class '" + className + "'",
                  e);
         }
      }
      return localeResolver;
   }

   @SuppressWarnings("unchecked")
   public static MessageResolver getMessageResolver(HttpServletRequest request) {
      Properties userProperties = null;
      String className = null;
      ConfigLoader configurationLoader = getConfigLoader();

      if (messageResolver == null) {

         try {
            userProperties = configurationLoader.loadUserConfiguration(Locale.getDefault());

            if (userProperties != null) {
               try {
                  className = userProperties.getProperty(ConfigLoader.I18N_MESSAGE_RESOLVER);
               }
               catch (MissingResourceException e) {

                  logger.debug("No custom MessageResolver has been configured. Using default one.");
               }
            }

            if (className == null) {
               Properties defaultProperties = configurationLoader.loadDefaultConfiguration();
               className = defaultProperties.getProperty(ConfigLoader.I18N_MESSAGE_RESOLVER);
            }

            if (className != null) {
               Class<MessageResolver> classProperty;
               try {
                  classProperty = (Class<MessageResolver>) ClassUtils.getClass(className);
                  messageResolver = classProperty.getDeclaredConstructor(new Class[] { HttpServletRequest.class })
                        .newInstance(request);
               }
               catch (Exception e) {
                  throw new DandelionException(e);
               }
            }
         }
         catch (DandelionException e) {
            throw new DandelionException("Unable to retrieve the MessageResolver using the class '" + className + "'",
                  e);
         }
      }
      return messageResolver;
   }

   /**
    * @return the uniq instance of {@link ConfigLoader}.
    */
   public static ConfigLoader getConfigLoader() {
      return ConfigLoaderHolder.INSTANCE;
   }

   /**
    * TODO
    * 
    * @author Thibault Duchateau
    *
    */
   private static class ConfigLoaderHolder {

      private final static ConfigLoader INSTANCE = new ConfigLoader();
   }

   /**
    * <b>FOR INTERNAL USE ONLY</b>
    */
   public static void clear() {
      localeResolver = null;
   }

   /**
    * <p>
    * Suppress default constructor for noninstantiability.
    * </p>
    */
   private DatatableConfigurator() {
      throw new AssertionError();
   }
}