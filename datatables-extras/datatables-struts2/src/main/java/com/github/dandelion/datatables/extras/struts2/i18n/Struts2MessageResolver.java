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
package com.github.dandelion.datatables.extras.struts2.i18n;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.struts2.views.jsp.TagUtils;

import com.github.dandelion.core.i18n.AbstractMessageResolver;
import com.github.dandelion.core.i18n.MessageResolver;
import com.github.dandelion.core.util.StringUtils;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ognl.OgnlValueStack;

/**
 * <p>
 * Struts2 implementation of the {@link MessageResolver}.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.1
 */
public class Struts2MessageResolver extends AbstractMessageResolver {

   public Struts2MessageResolver(HttpServletRequest request) {
      super(request);
   }

   @Override
   public String getResource(String messageKey, String defaultValue, Object... params) {

      String message = null;
      PageContext pageContext = null;

      // I'm so ashamed about that...
      pageContext = (PageContext) params[0];

      // Both title and titleKey attributes are not used
      if (messageKey == null || StringUtils.isBlank(messageKey) && StringUtils.isNotBlank(defaultValue)) {
         message = StringUtils.capitalize(defaultValue);
      }
      // the titleKey attribute is used
      else {
         OgnlValueStack stack = (OgnlValueStack) TagUtils.getStack(pageContext);

         for (Object o : stack.getRoot()) {
            if (o instanceof TextProvider) {
               TextProvider tp = (TextProvider) o;
               message = tp.getText(messageKey, UNDEFINED_KEY + messageKey + UNDEFINED_KEY);
               break;
            }
         }
      }

      return message;
   }
}
