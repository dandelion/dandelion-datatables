package com.github.dandelion.datatables.thymeleaf.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import com.github.dandelion.datatables.core.model.HtmlColumn;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 */
public class ColumnInitializerElProcessor extends AbstractElementProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ColumnInitializerElProcessor.class);
		
	public ColumnInitializerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult processElement(Arguments arguments, Element element) {
		logger.debug("{} element found", element.getNormalizedName());

		// Get HtmlTable POJO from the HttpServletRequest
		HtmlTable htmlTable = Utils.getTable(arguments);		
				
		// Get the TH content
		String content = null;
		if(element.getFirstChild() instanceof Text){
			content = ((Text) element.getFirstChild()).getContent().trim();
		}
		else{
			content = element.getChildren().toString();
		}

		// Init a new column
		HtmlColumn htmlColumn = new HtmlColumn(true, content);
		
		// AJAX sources require to set dt:property attribute
		// This attribute is processed here, before being removed
		if(element.hasAttribute("dt:property")){
			htmlColumn.setProperty(element.getAttributeValue("dt:property").trim());
			element.removeAttribute("dt:property");
		}

		if(element.hasAttribute("dt:default")){
			htmlColumn.setDefaultValue(element.getAttributeValue("dt:default").trim());
			element.removeAttribute("dt:default");
		}
		else{
			htmlColumn.setDefaultValue("");
		}
		
		// Add it to the table
		if(htmlTable != null){
			htmlTable.getLastHeaderRow().addHeaderColumn(htmlColumn);			
		}

		// HtmlColumn POJO is made available during the TH element processing
		Map<String, Object> newVariable = new HashMap<String, Object>();
		newVariable.put("htmlColumn", htmlColumn);
		return ProcessorResult.setLocalVariables(newVariable);
	}
}