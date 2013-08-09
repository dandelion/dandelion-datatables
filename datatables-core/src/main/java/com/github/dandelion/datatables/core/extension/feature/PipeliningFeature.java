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
package com.github.dandelion.datatables.core.extension.feature;

import java.io.IOException;

import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.FileUtils;

/**
 * <p>Pipelining feature that may be used if server-side processing has been
 * enabled.
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 * @see ServerSideFeature
 */
public class PipeliningFeature extends AbstractExtension {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setup(HtmlTable table) throws ExtensionLoadingException {
		String content;
		try {
			content = FileUtils
					.getFileContentFromClasspath("datatables/ajax/pipelining.js");
		} catch (IOException e) {
			throw new ExtensionLoadingException("Unable to read the content of the file 'pipelining.js'", e);
		}

		// Add the table id to avoid conflict if several tables use pipelining
		// in the same page
		String adaptedContent = content.replace("oCache", "oCache_" + table.getId());
		
		// Adapt the pipe size if it has been overriden
		if (table.getTableConfiguration().getAjaxPipeSize() != 5) {
			appendToBeforeAll(adaptedContent
					.replace("var iPipe = 5", "var iPipe = " + table.getTableConfiguration().getAjaxPipeSize()));
		} else {
			appendToBeforeAll(adaptedContent);
		}

		addParameter(new Parameter(DTConstants.DT_FN_SERVERDATA, new JavascriptSnippet("fnDataTablesPipeline")));
	}
}