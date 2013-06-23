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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Class that stores all configurations by Locale and group.
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class ConfigurationStore {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ConfigurationStore.class);
	
	/**
	 * Static map containing all configurations
	 */
	private static Map<Locale, Map<String, TableConfiguration>> configurationStore = new HashMap<Locale, Map<String, TableConfiguration>>();
	
	/**
	 * <p>
	 * Return the {@link TableConfiguration} prototype for the given locale
	 * (extracted thanks to the request) and the given group.
	 * 
	 * @param request
	 *            The request sent by the user.
	 * @param groupName
	 *            The group name request by the user to active the corresponding
	 *            configuration.
	 * @return the stored proptotype of a {@link TableConfiguration}.
	 */
	public static TableConfiguration getPrototype(HttpServletRequest request, String groupName) {
		
		Locale locale = null;
		String group = StringUtils.isBlank(groupName) ? ConfigurationLoader.DEFAULT_GROUP_NAME : groupName;
		
		// Retrieve the locale either from a configured LocaleResolver or using the default locale
		if (request != null) {
			locale = DatatablesConfigurator.getLocaleResolver().resolveLocale(request);
		} else {
			locale = Locale.getDefault();
		}
        
		if(!configurationStore.containsKey(locale)){
			// resolution complete des configuration pour la locale (=> regarder tous les groupes)
			resolveGroupsForLocale(locale);
		}
			
		return configurationStore.get(locale).get(group);
	}

	/**
	 * 
	 * @param locale
	 */
	public static void resolveGroupsForLocale(Locale locale) {
		Map<String, TableConfiguration> map = new HashMap<String, TableConfiguration>();
		
		ConfigurationLoader confLoader = DatatablesConfigurator.getConfigurationLoader();
		
		try {
			// 1: chargement de la configuration par defaut
			confLoader.loadDefaultConfiguration();
				
			// 2: chargement du bundle correspondant à la locale
			confLoader.loadUserConfiguration(locale);
			
			// 3: extraction des groupes s'il y en a
			confLoader.resolveGroups(map, locale);		
					
		} catch (ConfigurationLoadingException e) {
			logger.error("Unable to load Datatables configuration", e);
		}
		
		configurationStore.put(locale, map);
	}
	
	/**
	 * FOR INTERNAL USE ONLY
	 * 
	 * @return
	 */
	public static Map<Locale, Map<String, TableConfiguration>> getConfigurationStore(){
		return configurationStore;
	}
	
	/**
	 * <b>FOR INTERNAL USE ONLY</b>
	 */
	public static void clear(){
		configurationStore.clear();
	}
}