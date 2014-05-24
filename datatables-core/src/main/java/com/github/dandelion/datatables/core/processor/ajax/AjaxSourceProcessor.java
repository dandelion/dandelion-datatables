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
package com.github.dandelion.datatables.core.processor.ajax;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.feature.AjaxFeature;
import com.github.dandelion.datatables.core.processor.AbstractConfigurationProcessor;

/**
 * <p>
 * Processor used when the table uses an AJAX source.
 * 
 * <p>
 * Note that the passed URL is not processed here but in each module
 * corresponding to the template engine currently being used.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class AjaxSourceProcessor extends AbstractConfigurationProcessor {

	@Override
	public void doProcess() {

		if (StringUtils.isNotBlank(stringifiedValue)) {
			Boolean serverSide = TableConfig.AJAX_SERVERSIDE.valueFrom(tableConfiguration);

			Boolean serverSideEnabled = serverSide != null && serverSide;
			if (!serverSideEnabled) {
				registerExtension(new AjaxFeature());
			}

			updateEntry(stringifiedValue);
		}
	}
}