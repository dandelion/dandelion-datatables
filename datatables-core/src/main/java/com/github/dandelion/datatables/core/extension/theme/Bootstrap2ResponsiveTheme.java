/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.datatables.core.asset.Parameter.Mode;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.config.DatatableBundles;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Extension that adds a responsive behaviour to the table.
 * <p>
 * Based on <a
 * href="https://github.com/Comanche/datatables-responsive">datatables
 * -responsive</a>, developed by <a
 * href="https://github.com/Comanche">Comanche</a>.
 * <p>
 * The extension updates the asset graph with the bootstrap2-responsive bundle,
 * add necessary variables and adds the needed DataTable's parameters.
 * <p>
 * Also note that the HTML markup needs to be updated with the
 * <code>data-class</code> and <code>data-hide</code> dynamic attributes.
 * <p>
 * For example:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" ext="bootstrap2,bootstrap2-responsive">
 *    &lt;datatables:column title="Id" property="id" data-class="expand" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="Street" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" data-hide="phone,tablet" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig#CSS_THEME
 * @see TableConfig#CSS_THEMEOPTION
 */
public class Bootstrap2ResponsiveTheme extends AbstractExtension {

	@Override
	public String getExtensionName() {
		return "bootstrap2-responsive";
	}

	@Override
	public void setup(HtmlTable table) {

		HttpServletRequest request = table.getTableConfiguration().getRequest();
		
		// Add necessary web resources
		addBundle(DatatableBundles.DDL_DT_THEME_BOOTSTRAP2_RESPONSIVE);
        
		addBundleParameter("datatables-responsive-css", "[root]", request.getContextPath());
		
		// Necessary variables and breakpoint definitions
		StringBuilder var = new StringBuilder();
		var.append("var responsiveHelper_").append(table.getId()).append(";\n");
		var.append("var breakpointDefinition = { tablet: 1024, phone : 480 };\n");
		appendToBeforeAll(var.toString());

		// Datatables' params
		addParameter(DTConstants.DT_AUTO_WIDTH, false, Mode.OVERRIDE);
		addCallback(CallbackType.PREDRAW,
				"if (!responsiveHelper_" + table.getId() + ") { responsiveHelper_" + table.getId()
						+ " = new ResponsiveDatatablesHelper(oTable_" + table.getId() + ", breakpointDefinition); }");
		addCallback(CallbackType.ROW, "responsiveHelper_" + table.getId() + ".createExpandIcon(nRow);");
		addCallback(CallbackType.DRAW, "responsiveHelper_" + table.getId() + ".respond();");
	}
}