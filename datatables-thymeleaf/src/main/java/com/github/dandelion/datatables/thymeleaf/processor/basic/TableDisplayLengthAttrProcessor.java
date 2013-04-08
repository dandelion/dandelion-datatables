package com.github.dandelion.datatables.thymeleaf.processor.basic;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesAttrProcessor;

public class TableDisplayLengthAttrProcessor extends AbstractDatatablesAttrProcessor{

	public TableDisplayLengthAttrProcessor(IAttributeNameProcessorMatcher matcher){
		super(matcher);
	}

	@Override
	public int getPrecedence(){
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element, String attributeName, HtmlTable table){
		Integer attrValue = Integer.parseInt(element.getAttributeValue(attributeName));
		table.setDisplayLength(attrValue);
		return ProcessorResult.ok();
	}
}