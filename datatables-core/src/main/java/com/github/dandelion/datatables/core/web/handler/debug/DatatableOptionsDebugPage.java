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
package com.github.dandelion.datatables.core.web.handler.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.core.util.ResourceUtils;
import com.github.dandelion.core.web.handler.HandlerContext;
import com.github.dandelion.core.web.handler.debug.AbstractDebugPage;
import com.github.dandelion.datatables.core.config.DatatableConfigurator;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfigurationFactory;
import com.github.dandelion.datatables.core.util.ConfigUtils;

/**
 * <p>
 * Debug page that displays the current configuration options applied to the
 * current tables.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.0.0
 */
public class DatatableOptionsDebugPage extends AbstractDebugPage {

	private static final String PAGE_ID = "datatable-options";
	private static final String PAGE_NAME = "Current options";
	private static final String PAGE_LOCATION = "META-INF/resources/ddl-dt-debugger/html/datatable-options.html";

	@Override
	public String getId() {
		return PAGE_ID;
	}

	@Override
	public String getName() {
		return PAGE_NAME;
	}

	@Override
	public String getTemplate(HandlerContext context) throws IOException {
		return ResourceUtils.getContentFromInputStream(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PAGE_LOCATION));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getPageContext() {
		List<HtmlTable> htmlTables = (List<HtmlTable>) context.getRequest().getAttribute(
				ConfigUtils.DDL_DT_REQUESTATTR_TABLES);

		List<Map<String, Object>> tablesJson = new ArrayList<Map<String, Object>>();
		if (!htmlTables.isEmpty()) {

			int index = 0;
			for (HtmlTable htmlTable : htmlTables) {
				Map<String, Object> tableJson = new HashMap<String, Object>();
				tableJson.put("tableId", "#" + htmlTable.getOriginalId());
				tableJson.put("groupName", htmlTable.getTableConfiguration().getOptionGroupName());
				tableJson.put("options", getTableOptions(htmlTable, context.getRequest()));
				
				List<Map<String, Object>> extensions = new ArrayList<Map<String, Object>>();

				if(htmlTable.getTableConfiguration().getInternalExtensions() != null){
				   for (Extension ext : htmlTable.getTableConfiguration().getInternalExtensions()) {
				      extensions.add(new MapBuilder<String, Object>()
				            .entry("name", ext.getExtensionName().toLowerCase())
				            .entry("class", ext.getClass().getName())
				            .create());
				   }
				   tableJson.put("extensions", extensions);
				}
		      
				tableJson.put("active", index == 0 ? "active" : "");
				tablesJson.add(tableJson);
				index++;
			}
		}

		Map<String, Object> pageContext = new HashMap<String, Object>();
		pageContext.put("tables", tablesJson);
		return pageContext;
	}
	
	private List<Map<String, Object>> getTableOptions(HtmlTable htmlTable, HttpServletRequest request) {
		Map<Locale, Map<String, Map<Option<?>, Object>>> store = TableConfigurationFactory.getConfigurationStore();
		Locale locale = null;
		if (request != null) {
			locale = DatatableConfigurator.getLocaleResolver().resolveLocale(request);
		}
		else {
			locale = Locale.getDefault();
		}

		List<Map<String, Object>> tableOptions = new ArrayList<Map<String, Object>>();

		for (Entry<Option<?>, Object> entry : store.get(locale)
				.get(htmlTable.getTableConfiguration().getOptionGroupName()).entrySet()) {
			
			tableOptions.add(new MapBuilder<String, Object>()
               .entry("name", entry.getKey().getName())
               .entry("value", entry.getValue())
               .entry("precedence", entry.getKey().getPrecedence())
               .create());
		}
		return tableOptions;
	}
}
