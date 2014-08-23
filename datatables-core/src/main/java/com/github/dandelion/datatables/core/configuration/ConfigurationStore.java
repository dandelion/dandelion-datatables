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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.exception.UnkownGroupException;
import com.github.dandelion.datatables.core.i18n.MessageResolver;

/**
 * Storage class for all configurations by Locale and group.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ConfigurationStore {

	private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationStore.class);

	/**
	 * Static map containing all configurations
	 */
	private static Map<Locale, Map<String, Map<ConfigToken<?>, Object>>> configurationStore = new HashMap<Locale, Map<String, Map<ConfigToken<?>, Object>>>();

	/**
	 * <p>
	 * Return the {@link TableConfiguration} prototype for the given locale
	 * (extracted from the request) and the given group.
	 * 
	 * @param request
	 *            The request sent by the user.
	 * @param groupName
	 *            The group name requested by the user to activate the
	 *            corresponding configuration.
	 * @return the stored proptotype of a {@link TableConfiguration}.
	 */
	public static TableConfiguration getPrototype(HttpServletRequest request, String groupName) {

		Locale locale = null;
		String group = StringUtils.isBlank(groupName) ? ConfigurationLoader.DEFAULT_GROUP_NAME : groupName;

		// Retrieve the locale either from a configured LocaleResolver or using
		// the default locale
		if (request != null) {
			locale = DatatablesConfigurator.getLocaleResolver().resolveLocale(request);
		}
		else {
			locale = Locale.getDefault();
		}

		Context context = (Context) request.getAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE);
		if (context == null) {
			LOGGER.warn("The Dandelion context doesn't seem to be available. Did you forget to declare the DandelionFilter in your web.xml file?");
		}
		else if (context.isDevModeEnabled()) {
			clear();
		}

		if (!configurationStore.containsKey(locale)) {
			resolveGroupsForLocale(locale, request);
		}

		if (!configurationStore.get(locale).containsKey(group)) {
			StringBuilder msg = new StringBuilder("The group '");
			msg.append(group);
			msg.append("' doesn't exist in your configuration files. Either create it or choose an existing one among ");
			msg.append(configurationStore.get(locale).keySet());
			throw new UnkownGroupException(msg.toString());
		}

		MessageResolver messageResolver = DatatablesConfigurator.getMessageResolver(request);

		return new TableConfiguration(configurationStore.get(locale).get(group), messageResolver, request);
	}

	/**
	 * Resolves configurations groups for the given locale and stores them in
	 * the {@link ConfigurationStore}.
	 * 
	 * @param locale
	 */
	public static void resolveGroupsForLocale(Locale locale, HttpServletRequest request) {
		Map<String, Map<ConfigToken<?>, Object>> map = new HashMap<String, Map<ConfigToken<?>, Object>>();

		ConfigurationLoader confLoader = DatatablesConfigurator.getConfigurationLoader();

		confLoader.loadDefaultConfiguration();
		confLoader.loadUserConfiguration(locale);
		confLoader.resolveGroups(locale);
		confLoader.resolveConfigurations(map, locale, request);

		configurationStore.put(locale, map);
	}

	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 * 
	 * @return
	 */
	public static Map<Locale, Map<String, Map<ConfigToken<?>, Object>>> getConfigurationStore() {
		return configurationStore;
	}

	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 */
	public static void clear() {
		configurationStore.clear();
	}
}