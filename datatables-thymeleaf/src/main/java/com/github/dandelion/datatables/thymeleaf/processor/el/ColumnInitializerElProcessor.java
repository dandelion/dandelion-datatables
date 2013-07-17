package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.Arguments;
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
		
		Map<Configuration, Object> stagingConf = new HashMap<Configuration, Object>();
					
		// AJAX sources require to set dt:property attribute
		// This attribute is processed here, before being removed
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":property")) {
			stagingConf.put(Configuration.COLUMN_PROPERTY, Utils.parseElementAttribute(arguments,
					element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":property"), null, String.class));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":property");
		}

		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":renderFunction")) {
			stagingConf.put(Configuration.COLUMN_RENDERFUNCTION,
					Utils.parseElementAttribute(arguments,
							element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":renderFunction"), null,
							String.class));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":renderFunction");
		}

		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":default")) {
			stagingConf.put(Configuration.COLUMN_DEFAULTVALUE, Utils.parseElementAttribute(arguments,
					element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":default"), null, String.class));
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":default");
		} else {
			stagingConf.put(Configuration.COLUMN_DEFAULTVALUE, "");
		}
				
		// The staging configuration is stored as a local variable. It must be
		// accessible in all column head processors.
		Map<String, Object> newVariable = new HashMap<String, Object>();
		newVariable.put(DataTablesDialect.INTERNAL_COLUMN_LOCAL_CONF, stagingConf);
		return ProcessorResult.setLocalVariables(newVariable);
	}
}