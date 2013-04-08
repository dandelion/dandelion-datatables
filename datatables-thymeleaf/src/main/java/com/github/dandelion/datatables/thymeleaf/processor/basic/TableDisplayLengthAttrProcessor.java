package com.github.dandelion.datatables.thymeleaf.processor.basic;

import java.math.BigDecimal;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

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
		BigDecimal attrValue = Utils.parseElementAttribute(arguments, element.getAttributeValue(attributeName), new BigDecimal(10), BigDecimal.class);
		table.setDisplayLength(attrValue.intValueExact());
		return ProcessorResult.ok();
	}
}