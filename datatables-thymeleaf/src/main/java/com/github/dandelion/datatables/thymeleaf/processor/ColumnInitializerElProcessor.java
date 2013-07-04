package com.github.dandelion.datatables.thymeleaf.processor;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesElProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 */
public class ColumnInitializerElProcessor extends AbstractDatatablesElProcessor {

	public ColumnInitializerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {

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
			htmlColumn.setProperty(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:property"), null, String.class));
			element.removeAttribute("dt:property");
		}
		
		if(element.hasAttribute("dt:renderFunction")) {
			htmlColumn.setRenderFunction(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:renderFunction"), null, String.class));
			element.removeAttribute("dt:renderFunction");
		}

		if(element.hasAttribute("dt:default")){
			htmlColumn.setDefaultValue(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:default"), null, String.class));
			element.removeAttribute("dt:default");
		}
		else{
			htmlColumn.setDefaultValue("");
		}
		
		// Add it to the table
		if(table != null){
			table.getLastHeaderRow().addHeaderColumn(htmlColumn);			
		}

		// HtmlColumn POJO is made available during the TH element processing
		Map<String, Object> newVariable = new HashMap<String, Object>();
		newVariable.put("htmlColumn", htmlColumn);
		return ProcessorResult.setLocalVariables(newVariable);
	}
}