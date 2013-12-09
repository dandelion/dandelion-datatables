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

import com.github.dandelion.datatables.core.asset.Parameter;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.Scope;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * JQueryUI DataTables theme.
 * 
 * @since 0.7.1
 * @see TableConfig#CSS_THEME
 * @see TableConfig#CSS_THEMEOPTION
 */
public class JQueryUITheme extends AbstractExtension {

	@Override
	public String getName() {
		return "jQueryUI";
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	public void setup(HtmlTable table) {

		addScope(Scope.DDL_DT_THEME_JQUERYUI);
		addParameter(new Parameter(DTConstants.DT_JQUERYUI, true));

		ThemeOption themeOption = TableConfig.CSS_THEMEOPTION.valueFrom(table);
		
		if (themeOption != null) {
			switch(themeOption){
			case BASE:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_BASE);
				break;
			case BLACKTIE:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_BLACKTIE);
				break;
			case BLITZER:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_BLITZER);
				break;
			case CUPERTINO:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_CUPERTINO);
				break;
			case DARKHIVE:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_DARKHIVE);
				break;
			case DOTLUV:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_DOTLUV);
				break;
			case EGGPLANT:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_EGGPLANT);
				break;
			case EXCITEBIKE:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_EXCITEBIKE);
				break;
			case FLICK:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_FLICK);
				break;
			case HOTSNEAKS:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_HOTSNEAKS);
				break;
			case HUMANITY:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_HUMANITY);
				break;
			case LEFROG:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_LEFROG);
				break;
			case MINTCHOC:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_MINTCHOC);
				break;
			case OVERCAST:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_OVERCAST);
				break;
			case PEPPERGRINDER:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_PEPPERGRINDER);
				break;
			case REDMOND:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_REDMOND);
				break;
			case SMOOTHNESS:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_SMOOTHNESS);
				break;
			case SOUTHSTREET:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_SOUTHSTREET);
				break;
			case START:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_START);
				break;
			case SUNNY:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_SUNNY);
				break;
			case SWANKYPURSE:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_SWANKYPURSE);
				break;
			case TRONTASTIC:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_TRONTASTIC);
				break;
			case UIDARKNESS:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_UIDARKNESS);
				break;
			case UILIGHTNESS:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_UILIGHTNESS);
				break;
			case VADER:
				addScope(Scope.DDL_DT_THEME_JQUERYUI_VADER);
				break;
			}
		}

		table.addCssClass("display");
	}
}