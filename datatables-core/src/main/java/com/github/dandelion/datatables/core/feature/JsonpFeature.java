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
package com.github.dandelion.datatables.core.feature;

import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.model.AbstractFeature;
import com.github.dandelion.datatables.core.model.Configuration;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.core.model.JavascriptSnippet;

/**
 * <p>
 * Feature that is always enabled when server-side processing has been
 * activated.
 * <p>
 * Removing the fnAddjustColumnSizing will cause strange column's width at each
 * interaction with the table (paging, sorting, filtering ...)
 * 
 * @author Thibault Duchateau
 * @since 0.8.3
 */
public class JsonpFeature extends AbstractFeature {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public void setup(HtmlTable table) throws BadConfigurationException {
		addConfiguration(new Configuration(
				DTConstants.DT_FN_SERVERDATA,
				new JavascriptSnippet(
						"function( sUrl, aoData, fnCallback, oSettings ) { oSettings.jqXHR = $.ajax( {\"url\": sUrl,\"data\": aoData,\"success\": fnCallback,\"dataType\": \"jsonp\",\"cache\": false});}")));
	}
}