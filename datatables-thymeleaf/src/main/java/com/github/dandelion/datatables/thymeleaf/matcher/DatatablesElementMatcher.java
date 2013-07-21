package com.github.dandelion.datatables.thymeleaf.matcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.util.Validate;

import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;

public class DatatablesElementMatcher implements IElementNameProcessorMatcher {

	private final String elementName;
	private final Map<String, String> attributeValuesByNameFilter;
	private final boolean applyDialectPrefix;

	public DatatablesElementMatcher(final String elementName) {
		this(elementName, true);
	}

	public DatatablesElementMatcher(final String elementName, final boolean applyDialectPrefix) {
		this(elementName, null, applyDialectPrefix);
	}

	public DatatablesElementMatcher(final String elementName, final String filterAttributeName,
			final String filterAttributeValue) {
		this(elementName, filterAttributeName, filterAttributeValue, true);
	}

	public DatatablesElementMatcher(final String elementName, final String filterAttributeName,
			final String filterAttributeValue, final boolean applyDialectPrefix) {
		this(elementName, Collections.singletonMap(filterAttributeName, filterAttributeValue), applyDialectPrefix);
	}

	public DatatablesElementMatcher(final String elementName, final Map<String, String> attributeValuesByNameFilter) {
		this(elementName, attributeValuesByNameFilter, true);
	}

	public DatatablesElementMatcher(final String elementName, final Map<String, String> attributeValuesByNameFilter,
			final boolean applyDialectPrefix) {
		super();
		Validate.notEmpty(elementName, "Element name cannot be null or empty");
		this.elementName = elementName;
		if (attributeValuesByNameFilter == null || attributeValuesByNameFilter.size() == 0) {
			this.attributeValuesByNameFilter = null;
		} else {
			final Map<String, String> newAttributeValuesByNameFilter = new HashMap<String, String>(
					attributeValuesByNameFilter.size() + 1, 1.0f);
			newAttributeValuesByNameFilter.putAll(attributeValuesByNameFilter);
			this.attributeValuesByNameFilter = Collections.unmodifiableMap(newAttributeValuesByNameFilter);
		}
		this.applyDialectPrefix = applyDialectPrefix;
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
		Element parent = (Element) element.getParent();
		if (!parent.getNormalizedName().equals("table")
				|| !parent.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":table")
				|| !parent.getAttributeValueFromNormalizedName(DataTablesDialect.DIALECT_PREFIX + ":table").equals(
						"true")) {
			return false;
		}

		if (this.attributeValuesByNameFilter != null) {

			for (final Map.Entry<String, String> filterAttributeEntry : this.attributeValuesByNameFilter.entrySet()) {

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
		if (this.applyDialectPrefix) {
			return Node.applyDialectPrefix(this.elementName, context.getDialectPrefix());
		}

		return this.elementName;
	}
}