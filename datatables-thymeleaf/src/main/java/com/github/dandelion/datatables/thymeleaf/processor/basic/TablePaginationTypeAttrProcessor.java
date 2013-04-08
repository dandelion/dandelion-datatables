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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.feature.PaginationTypeBootstrapFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeFourButtonFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeInputFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeListboxFeature;
import com.github.dandelion.datatables.core.feature.PaginationTypeScrollingFeature;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * Attribute processor for the <code>paginationtype</code> attribute.
 * 
 * @author Thibault Duchateau
 */
public class TablePaginationTypeAttrProcessor extends AbstractDatatablesAttrProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TablePaginationTypeAttrProcessor.class);

	public TablePaginationTypeAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element,
			String attributeName, HtmlTable table) {

		// Get attribute value
		String attrValue = Utils.parseElementAttribute(arguments, element.getAttributeValue(attributeName), null, String.class);

		if (StringUtils.isNotBlank(attrValue)) {
			PaginationType paginationType = null;
			try {
				paginationType = PaginationType.valueOf(attrValue);
			} catch (IllegalArgumentException e) {
				logger.error("{} is not a valid value among {}", attrValue, PaginationType.values());
				throw new IllegalArgumentException("Wrong value for PaginationType");
			}

			switch (paginationType) {
			case bootstrap:
				table.registerFeature(new PaginationTypeBootstrapFeature());
				break;
			case input:
				table.registerFeature(new PaginationTypeInputFeature());
				break;
			case listbox:
				table.registerFeature(new PaginationTypeListboxFeature());
				break;
			case scrolling:
				table.registerFeature(new PaginationTypeScrollingFeature());
				break;
			case four_button:
				table.registerFeature(new PaginationTypeFourButtonFeature());
				break;
			default:
				break;
			}
		}

		return ProcessorResult.ok();
	}
}