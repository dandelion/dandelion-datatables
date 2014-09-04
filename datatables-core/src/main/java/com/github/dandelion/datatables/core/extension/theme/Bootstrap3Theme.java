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
import com.github.dandelion.datatables.core.configuration.DatatableBundles;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Bootstrap v3 DataTables theme.
 * <p>
 * Example usage with JSP:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" cssClass="table" theme="bootstrap3">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="Street" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 * &lt;/datatables:table>
 * </pre>
 * <p>
 * Example usage with Thymeleaf:
 * 
 * <pre>
 * &lt;table id="myTableId" dt:table="true" dt:theme="bootstrap3"&gt;
 *    ...
 * &lt;table/>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig#CSS_THEME
 */
public class Bootstrap3Theme extends AbstractExtension {

	@Override
	public String getExtensionName() {
		return "bootstrap3";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setup(HtmlTable table) {

		addBundle(DatatableBundles.DDL_DT_THEME_BOOTSTRAP3);
		
		Boolean pageable = TableConfig.FEATURE_PAGEABLE.valueFrom(table.getTableConfiguration());
		if(pageable != null && pageable){
			addParameter(DTConstants.DT_PAGINATION_TYPE, PaginationType.BOOTSTRAP.toString());
		}
		
		addParameter(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet("[]"));
	}
}