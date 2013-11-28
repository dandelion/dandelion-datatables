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
package com.github.dandelion.datatables.core.processor.export;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;
import com.github.dandelion.datatables.core.util.RequestHelper;

/**
 * This processor is particular in the sense that it's returning null. Indeed,
 * it actually updates the exportConfMap attribute instead of the exportTypes that doesn't
 * exist.
 * 
 * @author Thibault Duchateau
 */
public class ExportConfsProcessor extends AbstractTableProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportConfsProcessor.class);

	@Override
	public void process(String param, TableConfiguration tableConfiguration,
			Map<Configuration, Object> confToBeApplied) {
		
		Set<ExportConf> retval = null;
		
		if (StringUtils.isNotBlank(param)) {

			retval = new HashSet<ExportConf>();
			
			ExportType type = null;

			// Init the exportable flag in order to add export links
			tableConfiguration.setIsExportable(true);

			// Allowed export types
			String[] types = param.trim().toUpperCase().split(",");
			for (String exportTypeString : types) {

				try {
					type = ExportType.valueOf(exportTypeString);
				} catch (IllegalArgumentException e) {
					logger.error("{} is not a valid value among {}", exportTypeString,
							ExportType.values());
					throw new ConfigurationProcessingException("Invalid value", e);
				}

				// The exportConf may already exist due to the ExportTag
				if (tableConfiguration.getExportConf(type) == null) {

					StringBuilder url = new StringBuilder(RequestHelper.getCurrentURIWithParameters(tableConfiguration
							.getRequest()));
					if(url.toString().contains("?")){
						url.append("&");
					}
					else{
						url.append("?");
					}
					url.append(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE).append("=")
							.append(type.getUrlParameter());
					url.append("&");
					url.append(ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID).append("=")
							.append(tableConfiguration.getTableId());

					ExportConf exportConf = new ExportConf(type, url.toString());
					retval.add(exportConf);
				}
			}
			
			if(tableConfiguration.getExportLinkPositions() == null){
				tableConfiguration.setExportLinkPositions(
						new HashSet<ExportLinkPosition>(Arrays.asList(ExportLinkPosition.TOP_RIGHT)));
			}
		}
		
		tableConfiguration.setExportConfs(retval);
	}
}