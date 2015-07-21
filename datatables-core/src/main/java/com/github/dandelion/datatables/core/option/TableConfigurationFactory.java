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
package com.github.dandelion.datatables.core.option;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.config.ConfigLoader;
import com.github.dandelion.datatables.core.config.DatatableConfigurator;

/**
 * <p>
 * Factory that creates {@link TableConfiguration} instances.
 * </p>
 * <p>
 * Once created, the instance is cached by {@link Locale} and then by group
 * name.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.0.0
 */
public class TableConfigurationFactory {

   private static Logger LOGGER = LoggerFactory.getLogger(TableConfigurationFactory.class);

   /**
    * Static map containing all templates of configuration.
    */
   private static final Map<Locale, Map<String, Map<Option<?>, Object>>> OPTIONS_BY_GROUP_BY_LOCALE = new ConcurrentHashMap<Locale, Map<String, Map<Option<?>, Object>>>();

   /**
    * <p>
    * Returns a new instance of {@link TableConfiguration} corresponding to
    * {@link ConfigLoader#DEFAULT_GROUP_NAME}, i.e. containing all global
    * configuration.
    * </p>
    * <p>
    * The instance is duplicated from the template instance stored in the
    * {@link #OPTIONS_BY_GROUP_BY_LOCALE} map.
    * </p>
    * 
    * @param tableId
    *           The DOM id of the HTML table.
    * @param request
    *           The current request.
    * @return a new and initialized instance of {@link TableConfiguration}.
    */
   public static TableConfiguration newInstance(String tableId, HttpServletRequest request) {
      return newInstance(tableId, request, ConfigLoader.DEFAULT_GROUP_NAME);
   }

   /**
    * <p>
    * Returns a new instance of {@link TableConfiguration} corresponding to the
    * passed {@code groupName} and locale resolved from the request.
    * </p>
    * <p>
    * If the passed group name doesn't exist, the DEFAULT_GROUP_NAME (global)
    * will be used.
    * </p>
    * <p>
    * The instance is duplicated from the template instance stored in the
    * {@link #OPTIONS_BY_GROUP_BY_LOCALE} map.
    * </p>
    * 
    * @param tableId
    *           The DOM id of the HTML table.
    * @param request
    *           The current request.
    * @param groupName
    *           Name of the configuration group to load.
    * @return a new and initialized instance of {@link TableConfiguration}.
    */
   public synchronized static TableConfiguration newInstance(String tableId, HttpServletRequest request,
         String groupName) {

      // Process group
      String group = StringUtils.isBlank(groupName) ? ConfigLoader.DEFAULT_GROUP_NAME : groupName;

      // Retrieve the locale either from a configured LocaleResolver or using
      // the default locale
      Locale locale = null;
      if (request != null) {
         locale = DatatableConfigurator.getLocaleResolver().resolveLocale(request);
      }
      else {
         locale = Locale.getDefault();
      }

      // Clear the map only in dev profile
      // WARNING: not thread safe
      Context context = (Context) request.getAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE);
      if (context == null) {
         LOGGER.warn(
               "The Dandelion context doesn't seem to be available. Did you forget to declare the DandelionFilter in your web.xml file?");
      }

      // Feed the map for the corresponding locale if it doesn't exist
      if (!OPTIONS_BY_GROUP_BY_LOCALE.containsKey(locale)) {
         resolveGroupsForLocale(locale, request);
      }

      if (!OPTIONS_BY_GROUP_BY_LOCALE.get(locale).containsKey(group)) {
         StringBuilder msg = new StringBuilder("The group '");
         msg.append(group);
         msg.append("' doesn't exist in your configuration files. Either create it or choose an existing one among ");
         msg.append(OPTIONS_BY_GROUP_BY_LOCALE.get(locale).keySet());
         throw new DandelionException(msg.toString());
      }

      MessageResolver messageResolver = DatatableConfigurator.getMessageResolver(request);

      // Return a fresh instance from the template instance
      return new TableConfiguration(tableId,
            new HashMap<Option<?>, Object>(OPTIONS_BY_GROUP_BY_LOCALE.get(locale).get(group)), messageResolver, request,
            group);
   }

   /**
    * <p>
    * Resolves configurations groups for the given locale and stores them in the
    * {@link #OPTIONS_BY_GROUP_BY_LOCALE} map.
    * </p>
    * 
    * @param locale
    *           The locale resolved from the user properties or from the
    *           request.
    * @param request
    *           The current request.
    */
   public static void resolveGroupsForLocale(Locale locale, HttpServletRequest request) {
      Map<String, Map<Option<?>, Object>> map = new ConcurrentHashMap<String, Map<Option<?>, Object>>();

      ConfigLoader confLoader = DatatableConfigurator.getConfigLoader();

      confLoader.loadDefaultConfiguration();
      confLoader.loadUserConfiguration(locale);
      confLoader.resolveGroups(locale);
      confLoader.resolveConfigurations(map, locale, request);

      OPTIONS_BY_GROUP_BY_LOCALE.put(locale, map);
   }

   /**
    * <b>FOR INTERNAL USE ONLY</b>
    */
   public static Map<Locale, Map<String, Map<Option<?>, Object>>> getConfigurationStore() {
      return OPTIONS_BY_GROUP_BY_LOCALE;
   }

   /**
    * <b>FOR INTERNAL USE ONLY</b>
    */
   public static void clear() {
      OPTIONS_BY_GROUP_BY_LOCALE.clear();
   }

   /**
    * <p>
    * Suppress default constructor for noninstantiability.
    * </p>
    */
   private TableConfigurationFactory() {
      throw new AssertionError();
   }
}