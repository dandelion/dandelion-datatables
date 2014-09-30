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
package com.github.dandelion.datatables.thymeleaf.processor.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.el.TableFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.util.AttributeUtils;
import com.github.dandelion.datatables.thymeleaf.util.RequestUtils;

/**
 * <p>
 * Attribute processor applied to the HTML {@code div} tag with the
 * {@code dt:conf} attribute.
 * 
 * <p>
 * This processor initializes a configuration map for a given table id. Once
 * filled (thanks to the {@link DivConfTypeAttrProcessor}, the configuration map
 * will be processed later in the {@link TableFinalizerElProcessor}.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class DivConfAttrProcessor extends AbstractAttrProcessor {

	public DivConfAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPrecedence() {
		return DataTablesDialect.DT_HIGHEST_PRECEDENCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
		
		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();

		// A Map<ConfType, Object> is associated with each table id
		Map<String, Map<ConfType, Object>> configs = (Map<String, Map<ConfType, Object>>) RequestUtils.getFromRequest(
				DataTablesDialect.INTERNAL_BEAN_CONFIGS, request);

		String tableId = AttributeUtils.parseStringAttribute(arguments, element, attributeName);

		if (configs != null && configs.containsKey(tableId)) {
			throw new DandelionException("A div with id '" + tableId
					+ "' is already present in the current template.");
		}
		else {
			configs = new HashMap<String, Map<ConfType, Object>>();
		}

		configs.put(tableId, new HashMap<ConfType, Object>());
		RequestUtils.storeInRequest(DataTablesDialect.INTERNAL_BEAN_CONFIGS, configs, request);

		// The node is stored to be easily accessed later during the processing
		RequestUtils.storeInRequest(DataTablesDialect.INTERNAL_NODE_CONFIG, element, request);

		return ProcessorResult.ok();
	}
}