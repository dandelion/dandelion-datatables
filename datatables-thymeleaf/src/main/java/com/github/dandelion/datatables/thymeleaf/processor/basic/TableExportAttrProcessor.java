/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

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
public class TableExportAttrProcessor extends AbstractDatatableAttrProcessor {

	public TableExportAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
		// TODO Auto-generated constructor stub
	}

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TableExportAttrProcessor.class);

	// public TableExportAttrProcessor(IAttributeNameProcessorMatcher matcher) {
	// super(matcher);
	// }

	@Override
	public int getPrecedence() {
		return 8000;
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element,
			String attributeName) {
		logger.debug("{} attribute found", attributeName);

		// Get HtmlTable POJO from the HttpServletRequest
		HtmlTable htmlTable = Utils.getTable(arguments);

		// Get attribute value
		String attrValue = element.getAttributeValue(attributeName);
		logger.debug("Extracted value : {}", attrValue);
				
		if (StringUtils.isNotBlank(attrValue) && htmlTable != null) {

			// Init the exportable flag in order to add export links
			htmlTable.setIsExportable(true);

			// Allowed export types
			String[] exportTypes = attrValue.trim().toUpperCase().split(",");
			
			for (String exportTypeString : exportTypes) {
				ExportType type = null;

				try {
					type = ExportType.valueOf(exportTypeString.trim().toUpperCase());
				} catch (IllegalArgumentException e) {
					logger.error("The export cannot be activated for the table {}. ", htmlTable.getId());
					logger.error("{} is not a valid value among {}", exportTypeString,
							ExportType.values());
					throw new DataTableProcessingException(e);
				}

				// ExportConf eventuellement deja charges par le tag ExportTag
				// Du coup, on va completer ici avec la liste des autres exports
				// actives par la balise export=""
				if (!htmlTable.getExportConfMap().containsKey(type)) {

					String url = htmlTable.getCurrentUrl() + "?"
							+ ExportConstants.DT4J_REQUESTPARAM_EXPORT_TYPE + "="
							+ type.getUrlParameter() + "&"
							+ ExportConstants.DT4J_REQUESTPARAM_EXPORT_ID + "="
							+ htmlTable.getId();

					ExportConf conf = new ExportConf(type, url);
					htmlTable.getExportConfMap().put(type, conf);
				}

				// TODO ne pas prendre ne compte le tag ExportTag s'il permet de
				// customizer un export qui n'est pas specifie dans
				// export="XXXX"
			}

//			// Export links position
//			List<ExportLinkPosition> positionList = new ArrayList<ExportLinkPosition>();
//			if (StringUtils.isNotBlank(this.exportLinks)) {
//				String[] positions = this.exportLinks.trim().toUpperCase().split(",");
//
//				for (String position : positions) {
//					try {
//						positionList.add(ExportLinkPosition.valueOf(position));
//					} catch (IllegalArgumentException e) {
//						logger.error("The export cannot be activated for the table {}. ",
//								table.getId());
//						logger.error("{} is not a valid value among {}", position,
//								ExportLinkPosition.values());
//						throw new BadConfigurationException(e);
//					}
//				}
//			} else {
//				positionList.add(ExportLinkPosition.TOP_RIGHT);
//			}
//			this.table.setExportLinkPositions(positionList);
		}

		return nonLenientOK(element, attributeName);
	}
}