/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;

public class ProcessorUtils {

	public static String processAttributeAndScope(String value, Entry<ConfigToken<?>, Object> configEntry,
			HttpServletRequest request) {
		if (value.contains("#")) {
			String[] splittedValue = value.split("#");
			if (value.startsWith("#") || splittedValue.length != 2) {
				StringBuilder sb = new StringBuilder();
				sb.append("Wrong format used in the attribute value. ");
				sb.append("The right format is: 'scopeToAdd#JavascriptObject'");
				throw new ConfigurationProcessingException(sb.toString());
			} else {
				if (splittedValue[0].contains(",")) {
					String[] splittedScopes = splittedValue[0].trim().split(",");
					for (String scope : splittedScopes) {
						AssetsRequestContext.get(request).addScopes(scope.trim());
					}
				} else {
					AssetsRequestContext.get(request).addScopes(splittedValue[0].trim());
				}
				configEntry.setValue(StringUtils.isNotBlank(splittedValue[1]) ? splittedValue[1] : null);
			}
			
			return splittedValue[1];
		}
		else {
			if (value.contains(",")) {
				throw new ConfigurationProcessingException("The character ',' is not allowed by the property '"
						+ configEntry.getKey().getPropertyName() + "'.");
			}
			
			configEntry.setValue(value);
			return value;
		}
	}
}