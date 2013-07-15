package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;
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

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		
//		// Get the TH content
//		String content = null;
//		if(element.getFirstChild() instanceof Text){
//			content = ((Text) element.getFirstChild()).getContent().trim();
//		}
//		else{
//			content = element.getChildren().toString();
//		}
//
//		// Init a new column
//		HtmlColumn htmlColumn = new HtmlColumn(true, content);
		
		Map<Configuration, Object> stagingConf = new HashMap<Configuration, Object>();
		
					
		// AJAX sources require to set dt:property attribute
		// This attribute is processed here, before being removed
		if(element.hasAttribute("dt:property")){
			stagingConf.put(Configuration.COLUMN_PROPERTY,
					Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:property"), null, String.class));
//			htmlColumn.getColumnConfiguration().setProperty(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:property"), null, String.class));
			element.removeAttribute("dt:property");
		}
		
		if(element.hasAttribute("dt:renderFunction")) {
			stagingConf.put(Configuration.COLUMN_PROPERTY,
					Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:renderFunction"), null, String.class));
//			htmlColumn.getColumnConfiguration().setRenderFunction(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:renderFunction"), null, String.class));
			element.removeAttribute("dt:renderFunction");
		}

		if(element.hasAttribute("dt:default")){
			stagingConf.put(Configuration.COLUMN_PROPERTY,
					Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:default"), null, String.class));
//			htmlColumn.getColumnConfiguration().setDefaultValue(Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:default"), null, String.class));
			element.removeAttribute("dt:default");
		}
		else{
			stagingConf.put(Configuration.COLUMN_PROPERTY, "");
//			htmlColumn.getColumnConfiguration().setDefaultValue("");
		}
		
		// Map used to store the table local configuration
		request.setAttribute(DataTablesDialect.INTERNAL_COLUMN_LOCAL_CONF, stagingConf);
				
//		// HtmlColumn POJO is made available during the TH element processing
//		Map<String, Object> newVariable = new HashMap<String, Object>();
//		newVariable.put("htmlColumn", htmlColumn);
//		return ProcessorResult.setLocalVariables(newVariable);
		return ProcessorResult.ok();
	}
}