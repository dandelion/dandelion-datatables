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
package com.github.dandelion.datatables.thymeleaf.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

/**
 * Custom abstract attribute processor that just provides the nonLenientOK
 * utility method.
 * 
 * @author Thibault Duchateau
 */
public class AbstractDatatableAttrProcessor extends AbstractAttrProcessor {

	public AbstractDatatableAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	public AbstractDatatableAttrProcessor(String attributeName) {
		super(attributeName);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element,
			String attributeName) {
		return null;
	}

	@Override
	public int getPrecedence() {
		return 0;
	}

	/**
	 * Remove the processed attribute, cause the dialect is non lenient.
	 * 
	 * @param element
	 *            The element from which the attribute has to be removed.
	 * @param attributeName
	 *            The attribute to remove.
	 * @return ProcessorResult.OK
	 */
	protected ProcessorResult nonLenientOK(Element element, String attributeName) {
		element.removeAttribute(attributeName);
		return ProcessorResult.OK;
	}
}
