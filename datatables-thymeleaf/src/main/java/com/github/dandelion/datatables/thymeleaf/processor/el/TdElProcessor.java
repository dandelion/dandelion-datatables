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
import org.thymeleaf.Configuration;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressionParser;

import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;

/**
 * <p>
 * Element processor applied to the {@code td} tag. Whenever Thymeleaf meets a
 * {@code td} tag, a {@link HtmlColumn} is added to the last added
 * {@link HtmlRow}.
 * <p>
 * Important note : the unique goal of this processor is to fill the
 * {@link HtmlTable} bean in order to make it exportable.
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

		IStandardExpressionParser parser = new StandardExpressionParser();
		Configuration configuration = arguments.getConfiguration();
		IProcessingContext processingContext = arguments.getTemplateProcessingParameters().getProcessingContext();
		
		if (table != null) {

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
					content = parser.parseExpression(configuration, processingContext, attrValue)
							.execute(configuration, processingContext).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv");
					column = new HtmlColumn(ReservedFormat.CSV);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xml");
					content = parser.parseExpression(configuration, processingContext, attrValue)
							.execute(configuration, processingContext).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml");
					column = new HtmlColumn(ReservedFormat.XML);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					content = parser.parseExpression(configuration, processingContext, attrValue)
							.execute(configuration, processingContext).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					column = new HtmlColumn(ReservedFormat.PDF);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xls");
					content = parser.parseExpression(configuration, processingContext, attrValue)
							.execute(configuration, processingContext).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls");
					column = new HtmlColumn(ReservedFormat.XLS);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					content = parser.parseExpression(configuration, processingContext, attrValue)
							.execute(configuration, processingContext).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					column = new HtmlColumn(ReservedFormat.XLSX);
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