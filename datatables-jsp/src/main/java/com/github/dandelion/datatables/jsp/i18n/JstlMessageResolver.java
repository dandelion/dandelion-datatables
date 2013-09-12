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
package com.github.dandelion.datatables.jsp.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.i18n.AbstractMessageResolver;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.util.StringUtils;
import com.github.dandelion.datatables.jsp.tag.ColumnTag;

/**
 * <p>
 * JSTL implementation of the {@link MessageResolver}.
 * <p>
 * It will make the <code>titleKey</code> attribute of the {@link ColumnTag}
 * works the same as the <code>key</code> property of the
 * <code>&lt;fmt:message&gt;</code> tag.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class JstlMessageResolver extends AbstractMessageResolver {

	public JstlMessageResolver(HttpServletRequest request) {
		super(request);
	}

	// Logger
	private static Logger logger = LoggerFactory.getLogger(JstlMessageResolver.class);

	public String getResource(String messageKey, String defaultValue, Object... params) {

		Tag tag = null;
		PageContext pageContext = null;
		String message = null;
		ResourceBundle bundle = null;
		LocalizationContext localizationContext = null;
		
		// I'm so ashamed about that...
		tag = (Tag) params[0];
		pageContext = (PageContext) params[1];

		Tag t = TagSupport.findAncestorWithClass(tag, BundleSupport.class);
		if (t != null) {
			// use resource bundle from parent <bundle> tag
			BundleSupport parent = (BundleSupport) t;
			localizationContext = parent.getLocalizationContext();
		} else {
			localizationContext = BundleSupport.getLocalizationContext(pageContext);
		}

		if (localizationContext != null) {
			bundle = localizationContext.getResourceBundle();
		}

		if (bundle != null) {

			if (messageKey == null || StringUtils.isBlank(messageKey) && StringUtils.isNotBlank(defaultValue)) {
				message = StringUtils.capitalize(defaultValue);
			} else {
				try {
					message = bundle.getString(messageKey);
				} catch (MissingResourceException e) {
					logger.warn("No message found with the key The message key {} and locale {}.", messageKey,
							localizationContext.getLocale());
					if (StringUtils.isBlank(message)) {
						message = UNDEFINED_KEY + messageKey + UNDEFINED_KEY;
					}
				}
			}
		}
		else{
			logger.warn("The bundle hasn't been retrieved. Please check your i18n configuration.");
			message = UNDEFINED_KEY + messageKey + UNDEFINED_KEY;
		}

		return message;
	}
}