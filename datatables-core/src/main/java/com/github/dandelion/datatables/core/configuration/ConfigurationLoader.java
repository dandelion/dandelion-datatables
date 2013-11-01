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
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;

/**
 * <p>
 * Interface for all configuration loaders.
 * 
 * <p>
 * The default implementation is the {@link StandardConfigurationLoader} class
 * but it can be replaced by another class that implements this interface thanks
 * to the {@link DatatablesConfigurator}.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public interface ConfigurationLoader {

	public final static String DT_DEFAULT_PROPERTIES = "config/datatables-default.properties";
	public final static String DT_USER_PROPERTIES = "datatables";
	public final static String DEFAULT_GROUP_NAME = "global";
	
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
	 * @throws ConfigurationLoadingException
	 *             if the default properties cannot be loader.
	 */
	public Properties loadDefaultConfiguration() throws ConfigurationLoadingException;

	/**
	 * <p>
	 * Load the user configuration which can be localized thanks to the given locale.
	 * <p>
	 * Once the bundle loaded, it is converted into Properties.
	 * 
	 * @param locale
	 *            The current locale used to load the right properties file.
	 * @return the ResourceBundle containing the user configuration.
	 */
	public Properties loadUserConfiguration(Locale locale);

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
	public Set<String> resolveGroups(Locale locale);
	
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
	 * group1.msg.search=My label<br/>
	 * group1.msg.processing=My other label<br/>
	 * </code> <br/>
	 * the {@link ConfigurationLoader} must create a group called 'group1'
	 * containing all properties present in the 'global' group but where
	 * <code>msg.search</code> and <code>msg.processing</code> are overriden
	 * with the user's ones.
	 * 
	 * <p>
	 * Note that:
	 * <ul>
	 * <li>A configuration group can be enabled locally in a table thanks to an
	 * tag attribute.</li>
	 * <li>A configuration group always extends the 'global' group.</li>
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
	 * @throws ConfigurationLoadingException 
	 */
	public void resolveConfigurations(Map<String, TableConfiguration> map, Locale locale, HttpServletRequest request)
			throws ConfigurationLoadingException;
}