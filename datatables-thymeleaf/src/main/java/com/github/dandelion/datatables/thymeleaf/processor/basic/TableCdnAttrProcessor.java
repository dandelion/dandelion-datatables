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
package com.github.dandelion.datatables.thymeleaf.processor.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * <p>
 * Attribute processor applied to the <tt>table</tt> tag for the
 * <tt>cdn</tt> attribute.
 * 
 * <p>
 * When the <tt>cdn</tt> is set to <code>true</code>, <tt>link</tt> and
 * <tt>script</tt> HTML tag are generated and point to the Microsoft's CDN
 * to include DataTables resources in the page.
 * 
 * @author Thibault Duchateau
 */
public class TableCdnAttrProcessor extends AbstractDatatableAttrProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableCdnAttrProcessor.class);

	public TableCdnAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 11000;
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element,
			String attributeName) {
		logger.debug("{} attribute found", attributeName);

		// Get HtmlTable POJO from the HttpServletRequest
		HtmlTable htmlTable = Utils.getTable(arguments);

		// Get attribute value
		Boolean attrValue = Boolean.parseBoolean(element.getAttributeValue(attributeName));
		logger.debug("Extracted value : {}", attrValue);

		// HtmlTable update
		if (htmlTable != null) {
			htmlTable.setCdn(attrValue);
		}

		return nonLenientOK(element, attributeName);
	}
}