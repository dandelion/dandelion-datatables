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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;

/**
 * <p>
 * Element processor applied to the HTML <tt>table</tt> tag.
 * 
 * @author Thibault Duchateau
 */
public class TableInitializerElProcessor extends AbstractElProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableInitializerElProcessor.class);

	public TableInitializerElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return DataTablesDialect.DT_HIGHEST_PRECEDENCE;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element) {
		
		String tableId = element.getAttributeValue("id");

		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		HttpServletResponse response = ((IWebContext) arguments.getContext()).getHttpServletResponse();
		
		if (tableId == null) {
			logger.error("The 'id' attribute is required.");
			throw new IllegalArgumentException();
		} else {
			
			String confGroup = (String) request.getAttribute(DataTablesDialect.INTERNAL_CONF_GROUP);
			
			HtmlTable htmlTable = new HtmlTable(tableId, request, response, confGroup);

			// Add default footer and header row
			htmlTable.addHeaderRow();

			// Add a "finalizing div" after the HTML table tag in order to
			// finalize the Dandelion-datatables configuration generation
			// The div will be removed in its corresponding processor
			Element div = new Element("div");
			div.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":tmp", "internalUse");
			div.setRecomputeProcessorsImmediately(true);
			element.getParent().insertAfter(element, div);

			// Store the htmlTable POJO as a request attribute, so that all the
			// others following HTML tags can access it and particularly the
			// "finalizing div"
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_BEAN, htmlTable);

			// The table node is also saved in the request, to be easily accessed later
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_NODE, element);
			
			// Map used to store the table local configuration
			request.setAttribute(DataTablesDialect.INTERNAL_TABLE_LOCAL_CONF, new HashMap<ConfigToken<?>, Object>());
			
			// TODO this has to be moved
			// Export has been enabled
//			if(element.hasAttribute("dt:export")){
//
//				Map<ExportType, ExportConf> exportConfMap = new HashMap<ExportType, ExportConf>();
//				ExportType type = null;
//				String val = Utils.parseElementAttribute(arguments, element.getAttributeValue("dt:export"), null, String.class);
//				
//				String[] types = val.trim().toUpperCase().split(",");
//				for (String exportTypeString : types) {
//
//					try {
//						type = ExportType.valueOf(exportTypeString);
//					} catch (IllegalArgumentException e) {
//						throw new ConfigurationProcessingException("Invalid value", e);
//					}
//
//					ExportConf exportConf = new ExportConf(type);
//					String exportUrl = UrlUtils.getExportUrl(request, response, type, tableId);
//					
//							RequestHelper.getCurrentURIWithParameters(request);
//					if(exportUrl.contains("?")){
//						exportUrl += "&";
//					}
//					else{
//						exportUrl += "?";
//					}
//					exportUrl += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
//						+ type.getUrlParameter() + "&"
//						+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
//						+ tableId;
//						
//					exportConf.setUrl(exportUrl);
//					exportConf.setCustom(false);
//					exportConfMap.put(type, exportConf);
//				}
//				
//				request.setAttribute(DataTablesDialect.INTERNAL_EXPORT_CONF_MAP, exportConfMap);
//			}

			// Cleaning attribute
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":table");
						
			return ProcessorResult.OK;
		}
	}
}