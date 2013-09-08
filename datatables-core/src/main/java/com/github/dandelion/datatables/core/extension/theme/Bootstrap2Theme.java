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
package com.github.dandelion.datatables.core.extension.theme;

import java.io.IOException;

import com.github.dandelion.datatables.core.asset.CssResource;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.asset.ResourceType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.FileUtils;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * Bootstrap v2 DataTables theme.
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 */
public class Bootstrap2Theme extends AbstractExtension {

	@Override
	public String getName() {
		return "bootstrap2";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setup(HtmlTable table) throws ExtensionLoadingException {

		// Specific Javascript code is required
		try {
			appendToBeforeAll(FileUtils.getFileContentFromClasspath("datatables/themes/bootstrap2/bootstrap.js"));
		} catch (IOException e) {
			throw new ExtensionLoadingException(
					"Unable to read the content of the file 'datatables/themes/bootstrap2/bootstrap.js'", e);
		}

		// Specific Javascript is also required to handle the pagination type
		try {
			appendToBeforeAll(FileUtils.getFileContentFromClasspath("datatables/features/paginationType/bootstrap.js"));
		} catch (IOException e) {
			throw new ExtensionLoadingException(
					"Unable to read the content of the file 'datatables/features/paginationType/bootstrap.js'", e);
		}

		// Specific CSS
		addCssResource(new CssResource(ResourceType.THEME, "Bootstrap2Theme",
				"datatables/themes/bootstrap2/bootstrap.css"));

		if (table.getTableConfiguration().getCssThemeOption() != null) {
			if(table.getTableConfiguration().getCssThemeOption().equals(ThemeOption.TABLECLOTH)){
				addCssResource(new CssResource(ResourceType.THEME, "Tablecloth", "datatables/themes/bootstrap2/tablecloth.css"));
			}
			else{
				throw new ExtensionLoadingException("Only the 'tablecloth' theme option is compatible with the 'bootstrap2' theme");
			}
		}
		
		// DataTables parameters
		if (StringUtils.isBlank(table.getTableConfiguration().getFeatureDom())) {
			addParameter(new Parameter(DTConstants.DT_DOM,
					"<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>", Parameter.Mode.OVERRIDE));
		}
		addParameter(new Parameter(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString(), Parameter.Mode.OVERRIDE));
		addParameter(new Parameter(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]")));
	}
}