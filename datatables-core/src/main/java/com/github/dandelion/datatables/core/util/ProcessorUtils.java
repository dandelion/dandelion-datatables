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
package com.github.dandelion.datatables.core.util;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.asset.web.AssetRequestContext;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.processor.ConfigurationProcessor;

/**
 * <p>
 * Utilities related to the {@link ConfigurationProcessor}.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public final class ProcessorUtils {

	/**
	 * <p>
	 * Some processors accept a special syntax, allowing to load one or more
	 * Dandelion {@link Bundle}s in the current {@link HttpServletRequest}.
	 * 
	 * <p>
	 * The syntax is as follows:<br />
	 * {@code bundleNameToAdd[,anotherBundleName]#javascriptObject}.
	 * 
	 * @throws ConfigurationProcessingException
	 *             if the passed value contains an incorrect format.
	 */
	public static String getValueAfterProcessingBundles(String value, HttpServletRequest request) {

		// The value may contain a hash, indicating that one or more bundles
		// should be loaded in the current request
		if (value.contains("#")) {
			String[] splittedValue = value.split("#");
			if (value.startsWith("#") || splittedValue.length != 2) {
				StringBuilder sb = new StringBuilder();
				sb.append("Wrong format used in the attribute value. ");
				sb.append("The right format is: 'bundleToAdd#javascriptObject'");
				throw new ConfigurationProcessingException(sb.toString());
			}
			else {
				if (splittedValue[0].contains(",")) {
					String[] splittedBundles = splittedValue[0].trim().split(",");
					for (String bundle : splittedBundles) {
						AssetRequestContext.get(request).addBundles(bundle.trim());
					}
				}
				else {
					AssetRequestContext.get(request).addBundles(splittedValue[0].trim());
				}

				// Once the request updated with the bundle(s), both the
				// value and the entry are cleaned
				return value.substring(value.indexOf("#") + 1);
			}
		}
		// Nothing to process
		else {
			return value;
		}
	}

	/**
	 * Prevent instantiation.
	 */
	private ProcessorUtils() {
	}
}
