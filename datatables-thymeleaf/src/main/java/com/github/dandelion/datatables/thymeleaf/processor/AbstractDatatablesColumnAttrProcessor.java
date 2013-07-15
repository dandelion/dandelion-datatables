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
package com.github.dandelion.datatables.thymeleaf.processor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;

/**
 * Base for all Datatables Thymeleaf column attribute processors.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0 
 */
public abstract class AbstractDatatablesColumnAttrProcessor extends AbstractAttrProcessor {

	public AbstractDatatablesColumnAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

    @Override
    @SuppressWarnings("unchecked")
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
    	
    	HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		
		Map<Configuration, Object> stagingConf = (Map<Configuration, Object>) request.getAttribute(DataTablesDialect.INTERNAL_COLUMN_LOCAL_CONF); 

		HtmlTable table = (HtmlTable) request.getAttribute(DataTablesDialect.INTERNAL_TABLE_BEAN);
		
        ProcessorResult processorResult = processColumnAttribute(arguments, element, attributeName, table, stagingConf);
        element.removeAttribute(attributeName);
        return processorResult;
    }

    @Override
    public abstract int getPrecedence();

    /**
     * Process the Attribute
     *
     * @param arguments Thymeleaf arguments
     * @param element Element of the attribute
     * @param attributeName attribute name
     * @return result of process
     */
    protected abstract ProcessorResult processColumnAttribute(Arguments arguments, Element element, String attributeName, HtmlTable table, Map<Configuration, Object> statingConf);
}
