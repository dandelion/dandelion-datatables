/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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
package com.github.dandelion.datatables.thymeleaf.matcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.util.Validate;

/**
 * <p>
 * Custom element matcher that allows to match an HTML tag without the dialect's
 * prefix.
 * 
 * <p>
 * Only the <code>getElementName</code> method is overriden.
 * 
 * @author Thibault Duchateau
 */
public class ElementNameWithoutPrefixProcessorMatcher implements IElementNameProcessorMatcher {

	private final String elementName;
	private final Map<String, String> attributeValuesByNameFilter;

	public ElementNameWithoutPrefixProcessorMatcher(final String elementName) {
		this(elementName, null);
	}

	public ElementNameWithoutPrefixProcessorMatcher(final String elementName,
			final String filterAttributeName, final String filterAttributeValue) {
		this(elementName, Collections.singletonMap(filterAttributeName, filterAttributeValue));
	}

	public ElementNameWithoutPrefixProcessorMatcher(final String elementName,
			final Map<String, String> attributeValuesByNameFilter) {
		super();
		Validate.notEmpty(elementName, "Element name cannot be null or empty");
		this.elementName = elementName;
		if (attributeValuesByNameFilter == null || attributeValuesByNameFilter.size() == 0) {
			this.attributeValuesByNameFilter = null;
		} else {
			final Map<String, String> newAttributeValuesByNameFilter = new HashMap<String, String>(
					attributeValuesByNameFilter.size() + 1, 1.0f);
			newAttributeValuesByNameFilter.putAll(attributeValuesByNameFilter);
			this.attributeValuesByNameFilter = Collections
					.unmodifiableMap(newAttributeValuesByNameFilter);
		}
	}

	@Override
	public boolean matches(Node node, ProcessorMatchingContext context) {
		if (!(node instanceof Element)) {
			return false;
		}

		final Element element = (Element) node;
		final String completeNormalizedElementName = Node.normalizeName(getElementName(context));

		if (!element.getNormalizedName().equals(completeNormalizedElementName)) {
			return false;
		}

		if (this.attributeValuesByNameFilter != null) {

			for (final Map.Entry<String, String> filterAttributeEntry : this.attributeValuesByNameFilter
					.entrySet()) {

				final String filterAttributeName = filterAttributeEntry.getKey();
				final String filterAttributeValue = filterAttributeEntry.getValue();

				if (!element.hasAttribute(filterAttributeName)) {
					if (filterAttributeValue != null) {
						return false;
					}
					continue;
				}
				final String elementAttributeValue = element.getAttributeValue(filterAttributeName);
				if (elementAttributeValue == null) {
					if (filterAttributeValue != null) {
						return false;
					}
				} else {
					if (!elementAttributeValue.equals(filterAttributeValue)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public Class<? extends Element> appliesTo() {
		return Element.class;
	}

	@Override
	public String getElementName(ProcessorMatchingContext context) {
		return this.elementName;
	}
}