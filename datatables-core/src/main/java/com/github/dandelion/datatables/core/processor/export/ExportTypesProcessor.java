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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.constants.ExportConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportType;
import com.github.dandelion.datatables.core.processor.AbstractProcessor;
import com.github.dandelion.datatables.core.util.RequestHelper;


public class ExportTypesProcessor extends AbstractProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportTypesProcessor.class);
		
	@Override
	public Object process(String param, TableConfiguration tableConfiguration, Map<Configuration, Object> confToBeApplied) {
		ExportType type = null;
		if (StringUtils.isNotBlank(param)) {

			// Init the exportable flag in order to add export links
			tableConfiguration.setIsExportable(true);

			// Allowed export types
			String[] types = param.trim().toUpperCase().split(",");

			for (String exportTypeString : types) {

				try {
					type = ExportType.valueOf(exportTypeString);
				} catch (IllegalArgumentException e) {
//					logger.error("The export cannot be activated for the table {}. ", table.getId());
					logger.error("{} is not a valid value among {}", exportTypeString,
							ExportType.values());
//					throw new BadConfigurationException(e);
				}

				// ExportConf eventuellement deja charges par le tag ExportTag
				// Du coup, on va completer ici avec la liste des autres exports
				// actives par la balise export=""
				if (!tableConfiguration.getExportConfMap().containsKey(type)) {

					String url = RequestHelper.getCurrentURIWithParameters(tableConfiguration.getRequest());
					if(url.contains("?")){
						url += "&";
					}
					else{
						url += "?";
					}
					url += ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_TYPE + "="
							+ type.getUrlParameter() + "&"
							+ ExportConstants.DDL_DT_REQUESTPARAM_EXPORT_ID + "="
							+ tableConfiguration.getTableId();

					ExportConf exportConf = new ExportConf(type, url);
					tableConfiguration.getExportConfMap().put(type, exportConf);
				}

				// TODO ne pas prendre ne compte le tag ExportTag s'il permet de
				// customizer un export qui n'est pas specifie dans
				// export="XXXX"
			}
		}
		
		return null;
	}
}
