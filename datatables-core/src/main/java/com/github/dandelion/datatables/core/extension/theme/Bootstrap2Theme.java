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

import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.exception.ExtensionLoadingException;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Bootstrap v2 DataTables theme.
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 * @see TableConfig#CSS_THEME
 * @see TableConfig#CSS_THEMEOPTION
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
	public void setup(HtmlTable table) {

		addScope(Scope.DDL_DT_THEME_BOOTSTRAP2);
		
		if(isEnabled(TableConfig.FEATURE_PAGEABLE)){
			addScope(Scope.DDL_DT_PAGING_BOOTSTRAP);
			if(TableConfig.FEATURE_PAGINATIONTYPE.valueFrom(table.getTableConfiguration()) == null){
				addParameter(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString());
			}
		}
		
		ThemeOption themeOption = TableConfig.CSS_THEMEOPTION.valueFrom(table);
		
		if (themeOption != null) {
			if(themeOption.equals(ThemeOption.TABLECLOTH)){
				addScope(Scope.DDL_DT_THEME_BOOTSTRAP2_TABLECLOTH);
			}
			else{
				throw new ExtensionLoadingException("Only the 'tablecloth' theme option is compatible with the 'bootstrap2' theme");
			}
		}
		
		addParameter(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]"));
	}
}