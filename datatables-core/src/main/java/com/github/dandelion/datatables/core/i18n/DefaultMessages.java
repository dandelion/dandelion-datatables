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
package com.github.dandelion.datatables.core.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Helper class for message bundle access.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public final class DefaultMessages {

	/**
	 * Base name for the bundle.
	 */
	private static final String BUNDLE_NAME = "datatables";

	/**
	 * Loaded ResourceBundle.
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Don't instantiate.
	 */
	private DefaultMessages() {
		// unused
	}

	/**
	 * Returns a message from the resource bundle.
	 * 
	 * @param key
	 *            Message key.
	 * @return message String.
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Reads a message from the resource bundle and format it using java
	 * MessageFormat.
	 * 
	 * @param key
	 *            Message key.
	 * @param parameters
	 *            Parameters to pass to MessageFormat.format()
	 * @return message String.
	 */
	public static String getString(String key, Object[] parameters) {
		String baseMsg;
		try {
			baseMsg = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}

		return MessageFormat.format(baseMsg, parameters);
	}

	/**
	 * Reads a message from the resource bundle and format it using java
	 * MessageFormat.
	 * 
	 * @param key
	 *            Message key.
	 * @param parameter
	 *            single parameter to pass to MessageFormat.format()
	 * @return message String.
	 */
	public static String getString(String key, Object parameter) {
		return getString(key, new Object[] { parameter });
	}
}