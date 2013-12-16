/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.standard.expression.StandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.github.dandelion.datatables.core.export.Format;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;

/**
 * <p>
 * Element processor applied to the <tt>td</tt> HTML tag. Whenever Thymeleaf
 * meets a <tt>td</tt> tag, a HtmlColumn is added to the last added HtmlRow.
 * <p>
 * Important note : the unique goal of this processor is to fill the HtmlTable
 * bean (with HtmlRows and HtmlColumns) for the export feature
 * 
 * @author Thibault Duchateau
 */
public class TdElProcessor extends AbstractElProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TdElProcessor.class);
	public TdElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 4002;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element) {

		if (table != null) {

			StandardExpressionParser parser = new StandardExpressionParser();
			
			HtmlColumn column = null;
			String content = null;
			String attrValue = null;
			
			if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls")
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx")){
				
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":csv");
					content = parser.parseExpression(arguments.getConfiguration(), arguments
							.getTemplateProcessingParameters().getProcessingContext(), attrValue).getStringRepresentation();
//					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv");
					column = new HtmlColumn(Format.CSV);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xml");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml");
					column = new HtmlColumn(Format.XML);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					column = new HtmlColumn(Format.PDF);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xls");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls");
					column = new HtmlColumn(Format.XLS);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					column = new HtmlColumn(Format.XLSX);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
			}
			// If the element contains a Text node, the content of the text node
			// will be displayed in all formats
			else if (element.getFirstChild() instanceof Text) {
				table.getLastBodyRow().addColumn(((Text) element.getFirstChild()).getContent().trim());
			}
			// Otherwise, an empty cell will be displayed
			else{
				logger.warn("Only cells containing plain text are supported, those containing HTML code are still not!");
				table.getLastBodyRow().addColumn("");
			}
		}

		// Remove internal attribute
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
		}

		return ProcessorResult.OK;
	}
}