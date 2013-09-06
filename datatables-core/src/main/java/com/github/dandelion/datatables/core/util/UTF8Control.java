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
package com.github.dandelion.datatables.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Custom implementation of @link {@link Control} that reads properties file in
 * UTF-8.
 * 
 * @author Thibault Duchateau
 */
public class UTF8Control extends Control {

	/**
	 * <p>
	 * Instantiates a resource bundle for the given bundle name of the given
	 * format and locale, using the given class loader if necessary. This method
	 * returns <code>null</code> if there is no resource bundle available for
	 * the given parameters. If a resource bundle can't be instantiated due to
	 * an unexpected error, the error must be reported by throwing an
	 * <code>Error</code> or <code>Exception</code> rather than simply returning
	 * <code>null</code>.
	 * 
	 * <p>
	 * If the <code>reload</code> flag is <code>true</code>, it indicates that
	 * this method is being called because the previously loaded resource bundle
	 * has expired.
	 * 
	 * <p>
	 * The default implementation instantiates a <code>ResourceBundle</code> as
	 * follows.
	 * 
	 * <ul>
	 * 
	 * <li>The bundle name is obtained by calling
	 * {@link #toBundleName(String, Locale) toBundleName(baseName, locale)}.</li>
	 * 
	 * <li>If <code>format</code> is <code>"java.class"</code>, the
	 * {@link Class} specified by the bundle name is loaded by calling
	 * {@link ClassLoader#loadClass(String)}. Then, a
	 * <code>ResourceBundle</code> is instantiated by calling
	 * {@link Class#newInstance()}. Note that the <code>reload</code> flag is
	 * ignored for loading class-based resource bundles in this default
	 * implementation.</li>
	 * 
	 * <li>If <code>format</code> is <code>"java.properties"</code>,
	 * {@link #toResourceName(String, String) toResourceName(bundlename,
	 * "properties")} is called to get the resource name. If <code>reload</code>
	 * is <code>true</code>, {@link ClassLoader#getResource(String)
	 * load.getResource} is called to get a {@link URL} for creating a
	 * {@link URLConnection}. This <code>URLConnection</code> is used to
	 * {@linkplain URLConnection#setUseCaches(boolean) disable the caches} of
	 * the underlying resource loading layers, and to
	 * {@linkplain URLConnection#getInputStream() get an <code>InputStream
	 * </code>}. Otherwise, {@link ClassLoader#getResourceAsStream(String)
	 * loader.getResourceAsStream} is called to get an {@link InputStream}.
	 * Then, a {@link PropertyResourceBundle} is constructed with the
	 * <code>InputStream</code>.</li>
	 * 
	 * <li>If <code>format</code> is neither <code>"java.class"</code> nor
	 * <code>"java.properties"</code>, an <code>IllegalArgumentException</code>
	 * is thrown.</li>
	 * 
	 * </ul>
	 * 
	 * @param baseName
	 *            the base bundle name of the resource bundle, a fully qualified
	 *            class name
	 * @param locale
	 *            the locale for which the resource bundle should be
	 *            instantiated
	 * @param format
	 *            the resource bundle format to be loaded
	 * @param loader
	 *            the <code>ClassLoader</code> to use to load the bundle
	 * @param reload
	 *            the flag to indicate bundle reloading; <code>true</code> if
	 *            reloading an expired resource bundle, <code>false</code>
	 *            otherwise
	 * @return the resource bundle instance, or <code>null</code> if none could
	 *         be found.
	 * @exception NullPointerException
	 *                if <code>bundleName</code>, <code>locale</code>,
	 *                <code>format</code>, or <code>loader</code> is
	 *                <code>null</code>, or if <code>null</code> is returned by
	 *                {@link #toBundleName(String, Locale) toBundleName}
	 * @exception IllegalArgumentException
	 *                if <code>format</code> is unknown, or if the resource
	 *                found for the given parameters contains malformed data.
	 * @exception ClassCastException
	 *                if the loaded class cannot be cast to
	 *                <code>ResourceBundle</code>
	 * @exception IllegalAccessException
	 *                if the class or its nullary constructor is not accessible.
	 * @exception InstantiationException
	 *                if the instantiation of a class fails for some other
	 *                reason.
	 * @exception ExceptionInInitializerError
	 *                if the initialization provoked by this method fails.
	 * @exception SecurityException
	 *                If a security manager is present and creation of new
	 *                instances is denied. See {@link Class#newInstance()} for
	 *                details.
	 * @exception IOException
	 *                if an error occurred when reading resources using any I/O
	 *                operations
	 * 
	 */
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");
		ResourceBundle bundle = null;
		InputStream stream = null;
		if (reload) {
			URL url = loader.getResource(resourceName);
			if (url != null) {
				URLConnection connection = url.openConnection();
				if (connection != null) {
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) {
			try {
				bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
			} finally {
				stream.close();
			}
		}
		return bundle;
	}
}