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
package com.github.dandelion.datatables.core.option.processor.export;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.utils.UrlUtils;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.extension.feature.ExportFeature;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

/**
 * This processor is particular in the sense that it's returning null. Indeed,
 * it actually updates the exportConfMap attribute instead of the exportTypes
 * that doesn't exist.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see ExportEnabledFormatProcessor
 */
public class ExportEnabledFormatProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {

		TableConfiguration tableConfiguration = context.getTableConfiguration();
		String valueAsString = context.getValueAsString();
		if (StringUtils.isNotBlank(valueAsString)) {

			// Init the exportable flag in order to add export links
			tableConfiguration.setIsExportable(true);

			// Allowed export types
			String[] enabledFormats = valueAsString.split(",");
			
			// An ExportConf will be initialized for each enable export format
			for (String enabledFormat : enabledFormats) {
				enabledFormat = enabledFormat.toLowerCase().trim();

				ExportConf exportConf = null;

				// The exportConf may already exist due to the ExportTag (JSP)
				// or the DivConfTypeAttrProcessor (Thymeleaf)
				if (!tableConfiguration.getExportConfiguration().containsKey(enabledFormat)) {
					
					// Default mode (export using filter)
					StringBuilder exportUrl = UrlUtils.getCurrentUri(tableConfiguration.getRequest());
					UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_TYPE, "f");
					UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_FORMAT, enabledFormat);
					UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_ID, tableConfiguration.getTableId());
					UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_IN_PROGRESS, "y");
					UrlUtils.addParameter(exportUrl, WebConstants.DANDELION_ASSET_FILTER_STATE, false);
					
					exportConf = new ExportConf(enabledFormat, UrlUtils.getProcessedUrl(exportUrl, tableConfiguration.getRequest(), tableConfiguration.getResponse()));
					
					tableConfiguration.getExportConfiguration().put(enabledFormat, exportConf);
				} 
			}

			context.registerExtension(ExportFeature.EXPORT_FEATURE_NAME);
		}
		
		// TODO bizarre
		return null;
	}
}