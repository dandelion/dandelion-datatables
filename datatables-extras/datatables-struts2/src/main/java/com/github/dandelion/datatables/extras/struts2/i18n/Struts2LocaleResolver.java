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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.i18n.LocaleResolver;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.ognl.OgnlValueStack;

/**
 * <p>
 * Struts2 implementation of the {@link LocaleResolver}.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.1
 */
public class Struts2LocaleResolver implements LocaleResolver {

   @Override
   public Locale resolveLocale(HttpServletRequest request) {

      Locale result = null;
      OgnlValueStack stack = (OgnlValueStack) ActionContext.getContext().getValueStack();

      for (Object o : stack.getRoot()) {
         if (o instanceof LocaleProvider) {
            LocaleProvider lp = (LocaleProvider) o;
            result = lp.getLocale();
            break;
         }
      }

      // Falling back to the request locale
      if (result == null) {
         result = request.getLocale();
      }

      return result;
   }
}