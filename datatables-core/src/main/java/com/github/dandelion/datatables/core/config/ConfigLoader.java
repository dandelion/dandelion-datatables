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
package com.github.dandelion.datatables.core.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.LibraryDetector;
import com.github.dandelion.core.utils.PropertiesUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.utils.UTF8Control;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;

/**
 * <p>
 * Dandelion-Datatables configuration loader.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 * @see DatatableConfigurator
 */
public class ConfigLoader {

	private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

	public final static String DANDELION_DT_CONFIGURATION = "dandelion.datatables.configuration";
	public final static String DT_DEFAULT_PROPERTIES = "config/datatables-default.properties";
	public final static String DT_USER_PROPERTIES_LOCATION = "dandelion/datatables/";
	public final static String DT_USER_PROPERTIES = "datatables";
	public final static String DEFAULT_GROUP_NAME = "global";
	public final static String STANDALONE_BUNDLES_TO_EXCLUDE = "bootstrap-datepicker,bootstrap2,bootstrap3,jquery,jqueryui,moment";
	public final static String I18N_LOCALE_RESOLVER = "i18n.locale.resolver";
	public final static String I18N_MESSAGE_RESOLVER = "i18n.message.resolver";

	private static Properties defaultProperties;
	private Properties userProperties;
	private Set<String> groups;

	/**
	 * <p>
	 * Load the default configuration from the internal properties file and
	 * both:
	 * <ul>
	 * <li>stores the properties inside a class field</li>
	 * <li>returns the properties if they need to be used outside of the class</li>
	 * </ul>
	 * 
	 * @return the default properties
	 */
	public Properties loadDefaultConfiguration() {

		if (defaultProperties == null) {

			logger.debug("Loading default configuration...");

			// Initialize properties
			Properties propertiesResource = new Properties();

			// Get default file as stream
			InputStream propertiesStream = null;

			try {
				propertiesStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(DT_DEFAULT_PROPERTIES);
				Reader reader = new InputStreamReader(propertiesStream, "UTF-8");
				propertiesResource.load(reader);
			}
			catch (IOException e) {
				throw new DandelionException("Unable to load the default configuration file", e);
			}
			finally {
				if (propertiesStream != null) {
					try {
						propertiesStream.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			defaultProperties = propertiesResource;

			logger.debug("Default configuration loaded");
		}

		return defaultProperties;
	}

	/**
	 * <p>
	 * Load the user configuration which can be localized thanks to the given
	 * locale.
	 * <p>
	 * Once the bundle loaded, it is converted into Properties.
	 * 
	 * @param locale
	 *            The current locale used to load the right properties file.
	 * @return the ResourceBundle containing the user configuration.
	 */
	public Properties loadUserConfiguration(Locale locale) {

		// Always clear the cache
		ResourceBundle.clearCache();
		ResourceBundle userBundle = null;

		// First check if the resource bundle is externalized
		if (StringUtils.isNotBlank(System.getProperty(DANDELION_DT_CONFIGURATION))) {

			String path = System.getProperty(DANDELION_DT_CONFIGURATION);

			try {
				URL resourceURL = new File(path).toURI().toURL();
				URLClassLoader urlLoader = new URLClassLoader(new URL[] { resourceURL });
				userBundle = ResourceBundle.getBundle(DT_USER_PROPERTIES, locale, urlLoader, new UTF8Control());
			}
			catch (MalformedURLException e) {
				logger.warn("Wrong path to the externalized bundle", e);
			}
			catch (MissingResourceException e) {
				logger.info("No *.properties file in {}. Trying to lookup in classpath...", path);
			}
		}

		// No system property is set, retrieves the bundle from the classpath
		if (userBundle == null) {
			try {
				userBundle = ResourceBundle.getBundle(DT_USER_PROPERTIES_LOCATION + DT_USER_PROPERTIES, locale,
						new UTF8Control());
			}
			catch (MissingResourceException e) {
				// if no resource bundle is found, try using the context
				// classloader
				try {
					userBundle = ResourceBundle.getBundle(DT_USER_PROPERTIES_LOCATION + DT_USER_PROPERTIES, locale,
							Thread.currentThread().getContextClassLoader(), new UTF8Control());
				}
				catch (MissingResourceException mre) {
					logger.debug("No custom configuration. Using default one.");
				}
			}
		}

		userProperties = PropertiesUtils.bundleToProperties(userBundle);
		return userProperties;
	}

	/**
	 * <p>
	 * Return a set containing all configuration groups.
	 * <p>
	 * Note that the <code>i18n.locale.resolver</code> is a special property
	 * that must not be taken into account because it is group-independent.
	 * 
	 * @param locale
	 *            The current locale.
	 * @return the resolved groups.
	 */
	public Set<String> resolveGroups(Locale locale) {

		logger.debug("Resolving groups for the locale {}...", locale);

		Set<String> groups = new HashSet<String>();

		if (userProperties != null && !userProperties.isEmpty()) {
			for (Entry<Object, Object> entry : userProperties.entrySet()) {
				String key = entry.getKey().toString();
				if (!key.contains(I18N_LOCALE_RESOLVER) && !key.contains(I18N_MESSAGE_RESOLVER)
						&& !key.contains("main.standalone")) {
					groups.add(key.substring(0, key.indexOf(".")));
				}
			}
		}

		// The 'global' group is always added
		groups.add(DEFAULT_GROUP_NAME);

		logger.debug("{} groups declared {}.", groups.size(), groups.toString());

		this.groups = groups;

		return this.groups;
	}

	/**
	 * <p>
	 * Resolve configuration groups for the given locale.
	 * <p>
	 * The default properties file (datatables-default.properties) already
	 * contains the 'global' group: this is the group of configuration that is
	 * used for all tables in the application.
	 * <p>
	 * Inside the user properties files (datatables_XX.properties), users can:
	 * <ul>
	 * <li>Override some properties of the 'global' group, by prefixing the
	 * property name with <code>global.</code></li>
	 * <li>Create configuration groups, by prefixing the property name with the
	 * name of the group to create</li>
	 * </ul>
	 * </p>
	 * <p>
	 * 
	 * <p>
	 * For example, if the user properties file contains:<br />
	 * <code>
	 * group1.i18n.msg.search=My label<br/>
	 * group1.i18n.msg.processing=My other label<br/>
	 * </code> <br/>
	 * the {@link ConfigLoader} must create a group called 'group1' containing
	 * all properties present in the 'global' group but where
	 * <code>i18n.msg.search</code> and <code>i18n.msg.processing</code> are
	 * overriden with the user's ones.
	 * 
	 * <p>
	 * Note that:
	 * <ul>
	 * <li>A configuration group can be enabled locally in a table thanks to an
	 * tag attribute.</li>
	 * <li>A configuration group always extends the 'global' group.</li>
	 * <li>All properties must be prefixed by the group name (even for the
	 * 'global' group).</li>
	 * </ul>
	 * 
	 * @param map
	 *            The map to update after the resolution of the configuration
	 *            groups.
	 * @param locale
	 *            The current locale used to get the right properties file from
	 *            the resource bundle.
	 * @param request
	 *            The request sent by the browser.
	 */
	public void resolveConfigurations(Map<String, Map<Option<?>, Object>> map, Locale locale, HttpServletRequest request) {

		logger.debug("Resolving configurations for the locale {}...", locale);

		loadAutoConfiguration(userProperties);

		// Retrieve the configuration for the 'global' group
		// The 'global' group contains all defaut properties, some of which may
		// have been overriden by user
		// The group information is removed from the key before storing the
		// property in the properties file
		Properties globalProperties = new Properties();
		for (Entry<Object, Object> entry : defaultProperties.entrySet()) {
			String key = entry.getKey().toString();
			if (!key.equalsIgnoreCase(I18N_LOCALE_RESOLVER) && !key.equalsIgnoreCase(I18N_MESSAGE_RESOLVER)
					&& !key.equalsIgnoreCase("main.standalone")) {
				globalProperties.put(key.substring(key.indexOf(".") + 1), entry.getValue());
			}
		}
		for (Entry<Object, Object> entry : userProperties.entrySet()) {
			String key = entry.getKey().toString();
			if (key.startsWith(DEFAULT_GROUP_NAME)) {
				globalProperties.put(key.substring(key.indexOf(".") + 1), entry.getValue());
			}
		}

		// Updates the ARC if Dandelion-Datatables is used in a standalone mode
		if (userProperties.containsKey("main.standalone")
				&& userProperties.getProperty("main.standalone").equals("true")) {
			AssetRequestContext.get(request).excludeBundles(STANDALONE_BUNDLES_TO_EXCLUDE);
		}

		// Compute configuration to apply on each group
		Map<Option<?>, Object> userConf = null;
		Map<String, List<String>> wrongKeys = new ConcurrentHashMap<String, List<String>>();

		for (String groupName : groups) {

			// groupedProperties = globalProperties + current group
			Properties groupedProperties = new Properties();
			groupedProperties.putAll(globalProperties);
			for (Entry<Object, Object> entry : userProperties.entrySet()) {
				String key = entry.getKey().toString();
				if (key.startsWith(groupName)) {
					groupedProperties.put(key.substring(key.indexOf(".") + 1), entry.getValue());
				}
			}

			logger.debug("Group '{}' initialized with {} properties", groupName, groupedProperties.size());

			userConf = new HashMap<Option<?>, Object>();

			for (Entry<Object, Object> entry : groupedProperties.entrySet()) {
				String key = entry.getKey().toString().trim().toLowerCase();

				Option<?> config = DatatableOptions.findByName(key);
				if (config != null) {
					userConf.put(config, entry.getValue().toString());
				}
				else if (!key.equals(I18N_LOCALE_RESOLVER) && !key.equals(I18N_MESSAGE_RESOLVER)) {
					if (wrongKeys.containsKey(groupName)) {
						wrongKeys.get(groupName).add(key);
					}
					else {
						List<String> values = new ArrayList<String>();
						values.add(key);
						wrongKeys.put(groupName, values);
					}
				}
			}

			map.put(groupName, userConf);
		}

		if (!wrongKeys.isEmpty()) {
			StringBuilder msg = new StringBuilder("Some properties of your configuration file are not recognized.\n");
			for (Entry<String, List<String>> entry : wrongKeys.entrySet()) {
				msg.append("The group '");
				msg.append(entry.getKey());
				msg.append("' contains ");
				msg.append(entry.getValue().size());
				msg.append(" unknown propert");
				msg.append(entry.getValue().size() > 1 ? "ies:\n" : "y:\n");
				for (int i = 0; i < entry.getValue().size(); i++) {
					msg.append(entry.getValue().get(i));
					if (i < entry.getValue().size() - 1) {
						msg.append("\n");
					}
				}
				msg.append("\n");
			}
			logger.error(msg.toString());
			throw new DandelionException(msg.toString());
		}

		logger.debug("{} group(s) resolved {} for the locale {}", groups.size(), groups.toString(), locale);
	}

	/**
	 * TODO
	 * @return
	 */
	public Properties getUserProperties(){
		return this.userProperties;
	}
	
	/**
	 * TODO
	 * 
	 * @param userProps
	 */
	private void loadAutoConfiguration(Properties userProps) {

		// The JSTL is required by Tiles, which can be used with Thymeleaf but
		// in any case, if Thymeleaf is available, the JstlMessageResolver
		// should not be enabled
		if (LibraryDetector.isJstlAvailable() && !LibraryDetector.isThymeleafAvailable() && userProps != null) {
			if (!userProps.isEmpty()) {
				for (Entry<Object, Object> entry : userProps.entrySet()) {
					String key = entry.getKey().toString();
					if (key.contains(I18N_MESSAGE_RESOLVER) && StringUtils.isBlank(entry.getValue().toString())) {
						userProps.put(entry.getKey(), "com.github.dandelion.datatables.jsp.i18n.JstlMessageResolver");
					}
				}
			}
			else {
				userProps.put("global." + I18N_MESSAGE_RESOLVER,
						"com.github.dandelion.datatables.jsp.i18n.JstlMessageResolver");
			}
		}
	}
}