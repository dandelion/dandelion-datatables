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
package com.github.dandelion.datatables.extras.spring3.ajax;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

/**
 * <p>
 * Resolves a method argument of type {@code DatatablesCriterias} annotated with
 * {@code DatatablesParams}.
 * 
 * <p>
 * Datatable attributes are obtained from the {@code HttpServletRequest}, and
 * mapped to the annotated {@code DatatablesCriterias} object.
 * 
 * <p>
 * This WebArgumentResolver can be enabled in Spring 3.0 and earlier using XML
 * configuration as shown below. Note for versions of Spring 3.1 and greater the
 * {@link DatatablesCriteriasMethodArgumentResolver} should be used instead.
 * 
 * <pre>
 * &lt;mvc:annotation-driven&gt;
 *    &lt;mvc:argument-resolvers&gt;
 *       &lt;bean class="com.github.dandelion.datatables.extras.spring3.ajax.DatatablesCriteriasResolver" /&gt;
 *    &lt;/mvc:argument-resolvers&gt;
 * &lt;/mvc:annotation-driven&gt;
 * </pre>
 * 
 * @see DatatablesParams
 * @see DatatablesCriterias
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 * @deprecated in favor of the {@link DatatablesCriteriasMethodArgumentResolver}
 *             for versions of Spring 3.1 and greater.
 */
public class DatatablesCriteriasResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter methodParam, NativeWebRequest nativeWebRequest) throws Exception {
		DatatablesParams parameterAnnotation = methodParam.getParameterAnnotation(DatatablesParams.class);

		if (parameterAnnotation != null) {
			HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
			return DatatablesCriterias.getFromRequest(request);
		}

		return WebArgumentResolver.UNRESOLVED;
	}
}