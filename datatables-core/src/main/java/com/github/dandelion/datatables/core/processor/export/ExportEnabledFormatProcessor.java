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
import java.util.Set;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.extension.feature.ExportFeature;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;
import com.github.dandelion.datatables.core.util.UrlUtils;

/**
 * This processor is particular in the sense that it's returning null. Indeed,
 * it actually updates the exportConfMap attribute instead of the exportTypes
 * that doesn't exist.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see ExportEnabledFormatProcessor
 */
public class ExportEnabledFormatProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		if (StringUtils.isNotBlank(value)) {

			Set<ExportConf> retval = null;

			retval = new HashSet<ExportConf>();

			// Init the exportable flag in order to add export links
			tableConfiguration.setIsExportable(true);

			// Allowed export types
			String[] enabledFormats = value.toUpperCase().split(",");
			for (String enabledFormat : enabledFormats) {
				enabledFormat = enabledFormat.toLowerCase().trim();

				ExportConf exportConf = null;

				// The exportConf may already exist due to the ExportTag
				if (!tableConfiguration.getExportConfiguration().containsKey(enabledFormat)) {
					String url = UrlUtils.getExportUrl(tableConfiguration.getRequest(),
							tableConfiguration.getResponse(), enabledFormat, tableConfiguration.getTableId());
					exportConf = new ExportConf(enabledFormat, url);
					retval.add(exportConf);
					tableConfiguration.getExportConfiguration().put(enabledFormat, exportConf);
				} else {
					exportConf = tableConfiguration.getExportConfiguration().get(enabledFormat);
					exportConf.setUrl(UrlUtils.getExportUrl(tableConfiguration.getRequest(),
							tableConfiguration.getResponse(), enabledFormat, tableConfiguration.getTableId()));
				}
			}

			// Apply default export links if nothing is configured
			if (!stagingConf.containsKey(TableConfig.EXPORT_LINK_POSITIONS)) {
				tableConfiguration.set(TableConfig.EXPORT_LINK_POSITIONS,
						new HashSet<ExportLinkPosition>(Arrays.asList(ExportLinkPosition.TOP_RIGHT)));
			}

			tableConfiguration.registerExtension(new ExportFeature());
		}
	}
}