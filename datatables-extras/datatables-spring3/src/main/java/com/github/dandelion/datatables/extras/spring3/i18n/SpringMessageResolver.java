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
package com.github.dandelion.datatables.extras.spring3.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.dandelion.datatables.core.i18n.AbstractMessageResolver;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Spring implementation of the {@link MessageResolver}.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class SpringMessageResolver extends AbstractMessageResolver {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(SpringMessageResolver.class);

	private MessageSource messageSource;

	public SpringMessageResolver() {
	}

	public SpringMessageResolver(HttpServletRequest request) {
		super(request);

		// Retrieve the Spring messageSource bean
		messageSource = RequestContextUtils.getWebApplicationContext(request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResource(String messageKey, String defaultValue, Object... objects) {

		Locale locale = RequestContextUtils.getLocale(request);
		String message = null;

		if (StringUtils.isBlank(messageKey) && StringUtils.isNotBlank(defaultValue)) {
			message = StringUtils.capitalize(defaultValue);
		} else {
			try {
				message = messageSource.getMessage(messageKey, null, locale);
			} catch (NoSuchMessageException e) {
				logger.warn("No message found with the key The message key {} and locale {}.", messageKey, locale);
				if (StringUtils.isBlank(message)) {
					message = UNDEFINED_KEY + messageKey + UNDEFINED_KEY;
				}
			}
		}

		return message;
	}
}
