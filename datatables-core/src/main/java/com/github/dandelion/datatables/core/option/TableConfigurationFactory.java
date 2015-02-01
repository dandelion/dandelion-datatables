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
package com.github.dandelion.datatables.core.option;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.utils.StringUtils;
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
 * @since 0.11.0
 */
public class TableConfigurationFactory {

	private static Logger LOGGER = LoggerFactory.getLogger(TableConfigurationFactory.class);

	/**
	 * Static map containing all configurations
	 */
	private static final Map<Locale, Map<String, Map<Option<?>, Object>>> OPTIONS_BY_GROUP_BY_LOCALE = new ConcurrentHashMap<Locale, Map<String, Map<Option<?>, Object>>>();

	/**
	 * Return an instance of {@link TableConfiguration} for the
	 * <code>DEFAULT_GROUP_NAME</code> (global), i.e. containing all global
	 * configurations.
	 * 
	 * @param request
	 *            The request is not used yet but will to work with Locale.
	 * @return an instance of {@link TableConfiguration} that contains all the
	 *         table configuration.
	 */
	public static TableConfiguration getInstance(String tableId, HttpServletRequest request) {
		return getInstance(tableId, request, ConfigLoader.DEFAULT_GROUP_NAME);
	}

	/**
	 * <p>
	 * Returns an instance of {@link TableConfiguration} for the given
	 * groupName. The instance is retrieved from the
	 * {@link TableConfigurationFactory}.
	 * <p>
	 * If the passed group name doesn't exist, the DEFAULT_GROUP_NAME (global)
	 * will be used.
	 * 
	 * @param request
	 * 
	 * @param groupName
	 *            Name of the configuration group to load.
	 * @return an instance of {@link TableConfiguration} that contains all the
	 *         table configuration.
	 */
	public static TableConfiguration getInstance(String tableId, HttpServletRequest request, String groupName) {

		// Retrieve the TableConfiguration prototype from the store
		// TableConfiguration prototype =
		// TableConfigurationFactory.getPrototype(request, groupName);

		Locale locale = null;
		String group = StringUtils.isBlank(groupName) ? ConfigLoader.DEFAULT_GROUP_NAME : groupName;

		// Retrieve the locale either from a configured LocaleResolver or using
		// the default locale
		if (request != null) {
			locale = DatatableConfigurator.getLocaleResolver().resolveLocale(request);
		}
		else {
			locale = Locale.getDefault();
		}

		Context context = (Context) request.getAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE);
		if (context == null) {
			LOGGER.warn("The Dandelion context doesn't seem to be available. Did you forget to declare the DandelionFilter in your web.xml file?");
		}
		else if (context.isDevProfileEnabled()) {
			clear();
		}

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

		TableConfiguration tc = new TableConfiguration(tableId, OPTIONS_BY_GROUP_BY_LOCALE.get(locale).get(group),
				messageResolver, request, group);

		TableConfiguration clone = TableConfiguration.clone(tc);
		return clone;
	}

	/**
	 * Resolves configurations groups for the given locale and stores them in
	 * the {@link TableConfigurationFactory}.
	 * 
	 * @param locale
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