/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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
package com.github.dandelion.datatables.thymeleaf.processor.feature;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.thymeleaf.dialect.AbstractDatatablesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * Attribute processor applied to the <code>tbody</code> tag for the following
 * attributes :
 * <ul>
 * <li>dt:csv:class</li>
 * <li>dt:xml:class</li>
 * <li>dt:xls:class</li>
 * <li>dt:xlsx:class</li>
 * <li>dt:pdf:class</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.8.8
 */
public class TbodyExportLinkClassAttrProcessor extends AbstractDatatablesAttrProcessor {

	public TbodyExportLinkClassAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element,
			String attributeName, HtmlTable table) {

		// Get the HTTP request
		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
				
		String tmpValue = Utils.parseElementAttribute(arguments, element.getAttributeValue(attributeName), null, String.class);
		StringBuffer attrValue = new StringBuffer(tmpValue);
		ExportType exportType = ExportType.valueOf(attributeName.split(":")[1].toUpperCase().trim());
		
		// The ExportConf already exists
		if(table.getExportConfMap().containsKey(exportType)){
			table.getExportConfMap().get(exportType).setCssClass(attrValue);
		}
		// The ExportConf still doesn't exist
		else{
			// Export URL build
			String url = RequestHelper.getCurrentUrlWithParameters(request);
			if(url.contains("?")){
				url += "&";
			}
			else{
				url += "?";
			}
			url += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
					+ exportType.getUrlParameter() + "&"
					+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
					+ table.getId();
						
			ExportConf conf = new ExportConf(exportType, url);
			conf.setCssClass(attrValue);
			table.getExportConfMap().put(exportType, conf);
		}
		
		return ProcessorResult.ok();
	}
}