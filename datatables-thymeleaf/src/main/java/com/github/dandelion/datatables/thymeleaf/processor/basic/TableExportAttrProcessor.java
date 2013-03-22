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
package com.github.dandelion.datatables.thymeleaf.processor.basic;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.RequestHelper;
import com.github.dandelion.datatables.thymeleaf.dialect.DatatablesAttrProcessor;

/**
 * <p>
 * Attribute processor applied to the <tt>table</tt> tag for the <tt>export</tt>
 * attribute.
 * 
 * <p>
 * When the <tt>export</tt> attribute is set to <code>true</code>, HTML link
 * will be generated around the table for each enabled export.
 * 
 * @author Thibault Duchateau
 */
public class TableExportAttrProcessor extends DatatablesAttrProcessor {

	public TableExportAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableExportAttrProcessor.class);

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element,
			String attributeName, HtmlTable table) {
		logger.debug("{} attribute found", attributeName);

		// Get the HTTP request
		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();

		// Get attribute value
		String attrValue = element.getAttributeValue(attributeName);
		logger.debug("Extracted value : {}", attrValue);

		if (StringUtils.isNotBlank(attrValue) && table != null) {

			// Init the exportable flag in order to add export links
			table.setIsExportable(true);

			// Allowed export types
			String[] exportTypes = attrValue.trim().toUpperCase().split(",");

			for (String exportTypeString : exportTypes) {
				ExportType type = null;

				try {
					type = ExportType.valueOf(exportTypeString.trim().toUpperCase());
				} catch (IllegalArgumentException e) {
					logger.error("The export cannot be activated for the table {}. ", table.getId());
					logger.error("{} is not a valid value among {}", exportTypeString,
							ExportType.values());
					throw new DataTableProcessingException(e);
				}

				// ExportConf eventuellement deja charges par le tag ExportTag
				// Du coup, on va completer ici avec la liste des autres exports
				// actives par la balise export=""
				if (!table.getExportConfMap().containsKey(type)) {

					String url = RequestHelper.getCurrentUrlWithParameters(request);
					if (url.contains("?")) {
						url += "&";
					} else {
						url += "?";
					}
					url += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
							+ type.getUrlParameter() + "&"
							+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "=" + table.getId();

					ExportConf conf = new ExportConf(type, url);
					table.getExportConfMap().put(type, conf);
				}
			}
		}

		return ProcessorResult.ok();
	}
}