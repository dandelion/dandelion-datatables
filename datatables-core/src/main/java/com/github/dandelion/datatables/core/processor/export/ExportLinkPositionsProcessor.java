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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;


public class ExportLinkPositionsProcessor extends AbstractTableProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExportLinkPositionsProcessor.class);
		
	@Override
	public void process(String param, TableConfiguration tableConfiguration,
			Map<Configuration, Object> confToBeApplied) throws ConfigurationProcessingException {
		Set<ExportLinkPosition> retval = new HashSet<ExportLinkPosition>();

		if(StringUtils.isNotBlank(param)){
		
			String[] positions = param.trim().split(",");

			for (String position : positions) {
				try {
					retval.add(ExportLinkPosition.valueOf(position.toUpperCase().trim()));
				} catch (IllegalArgumentException e) {
					logger.error("The export cannot be activated for the table {}. ", tableConfiguration.getTableId());
					logger.error("{} is not a valid value among {}", position, ExportLinkPosition.values());
					throw new ConfigurationProcessingException(e);
				}
			}
		}
		else{
			retval.add(ExportLinkPosition.TOP_RIGHT);
		}
		
		tableConfiguration.setExportLinkPositions(retval);
	}
}