package com.github.dandelion.datatables.thymeleaf.processor.attr.basic;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractTableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

public class TableLengthChangeAttrProcessor extends AbstractTableAttrProcessor {

	public TableLengthChangeAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element, String attributeName) {
		
		Boolean attrValue = Utils.parseElementAttribute(arguments, element.getAttributeValue(attributeName),
				new Boolean(false), Boolean.class);
		
		localConf.put(TableConfig.FEATURE_LENGTHCHANGE, attrValue);

		return ProcessorResult.ok();
	}
}