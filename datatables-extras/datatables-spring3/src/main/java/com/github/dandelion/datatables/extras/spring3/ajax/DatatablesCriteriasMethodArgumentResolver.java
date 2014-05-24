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
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
 * This HandlerMethodArgumentResolver can be enabled in Spring 3.1 and greater
 * using either Java or XML configuration as shown below:
 * 
 * <pre>
 * &lt;mvc:annotation-driven&gt;
 *    &lt;mvc:argument-resolvers&gt;
 *       &lt;bean class="com.github.dandelion.datatables.extras.spring3.ajax.DatatablesCriteriasMethodArgumentResolver" /&gt;
 *    &lt;/mvc:argument-resolvers&gt;
 * &lt;/mvc:annotation-driven&gt;
 * </pre>
 * 
 * <pre>
 * &#064;Configuration
 * &#064;EnableWebMvc
 * public class MyWebConfig extends WebMvcConfigurerAdapter {
 * 	&#064;Override
 * 	public void addArgumentResolvers(List&lt;HandlerMethodArgumentResolver&gt; argumentResolvers) {
 * 		argumentResolvers.add(new DatatablesCriteriasMethodArgumentResolver());
 * 	}
 * }
 * </pre>
 * 
 * @see DatatablesParams
 * @see DatatablesCriterias
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class DatatablesCriteriasMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		DatatablesParams parameterAnnotation = parameter.getParameterAnnotation(DatatablesParams.class);
		if (parameterAnnotation != null) {
			if (DatatablesCriterias.class.isAssignableFrom(parameter.getParameterType())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		return DatatablesCriterias.getFromRequest(request);
	}
}