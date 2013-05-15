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
package com.github.dandelion.datatables.core.theme;

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.asset.Configuration;
import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.ResourceHelper;

/**
 * Bootstrap v2 DataTables theme.
 *
 * @author Thibault Duchateau
 * @since 0.7.1
 */
public class Bootstrap2Theme extends AbstractTheme {

	@Override
	public String getName() {
		return "bootstrap2";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setup(HtmlTable table) throws BadConfigurationException {
		// Specific theme javascript		
		appendToBeforeAll(ResourceHelper.getFileContentFromClasspath("datatables/themes/bootstrap2/bootstrap.js"));
		
		// Custom pagination type
		appendToBeforeAll(ResourceHelper.getFileContentFromClasspath("datatables/features/paginationType/bootstrap.js")); 
		
		// Specific theme css
		addCssResource(new CssResource(ResourceType.THEME, "Bootstrap2Theme", "datatables/themes/bootstrap2/bootstrap.css"));

		// Specific theme configurations
		if(StringUtils.isBlank(table.getDom())){
			addConfiguration(new Configuration(DTConstants.DT_DOM, "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>", Configuration.Mode.OVERRIDE));
		}
		addConfiguration(new Configuration(DTConstants.DT_PAGINATION_TYPE, "bootstrap", Configuration.Mode.OVERRIDE));
		addConfiguration(new Configuration("asStripeClasses", new JavascriptSnippet("[]")));
	}
}
