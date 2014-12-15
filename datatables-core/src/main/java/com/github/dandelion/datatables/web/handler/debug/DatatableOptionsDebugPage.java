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
package com.github.dandelion.datatables.web.handler.debug;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.github.dandelion.core.utils.ResourceUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.core.web.handler.RequestHandlerContext;
import com.github.dandelion.core.web.handler.debug.AbstractDebugPage;
import com.github.dandelion.datatables.core.config.ConfigLoader;
import com.github.dandelion.datatables.core.config.DatatableConfigurator;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.TableConfigurationFactory;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class DatatableOptionsDebugPage extends AbstractDebugPage {

	private static final String PAGE_ID = "datatable-options";
	private static final String PAGE_NAME = "Options";
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
	public String getTemplate(RequestHandlerContext context) throws IOException {
		return ResourceUtils.getContentFromInputStream(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PAGE_LOCATION));
	}

	@Override
	protected Map<String, String> getCustomParameters(RequestHandlerContext context) {

		Map<String, String> params = new HashMap<String, String>();

		Map<Locale, Map<String, Map<Option<?>, Object>>> store = TableConfigurationFactory.getConfigurationStore();
		Locale currentLocale = DatatableConfigurator.getLocaleResolver().resolveLocale(context.getRequest());

		StringBuilder groupLinks = new StringBuilder();
		for (Entry<String, Map<Option<?>, Object>> entry : store.get(currentLocale).entrySet()) {
			if (!entry.getKey().equals(ConfigLoader.DEFAULT_GROUP_NAME)) {
				groupLinks.append("<li><a href='#");
				groupLinks.append(entry.getKey());
				groupLinks.append("-group' id='");
				groupLinks.append(entry.getKey());
				groupLinks.append("-tab' data-toggle='tab'>");
				groupLinks.append(StringUtils.capitalize(entry.getKey()));
				groupLinks.append("</a></li>");
			}
		}

		StringBuilder groupContents = new StringBuilder();
		for (Entry<String, Map<Option<?>, Object>> entry : store.get(currentLocale).entrySet()) {
			if (!entry.getKey().equals(ConfigLoader.DEFAULT_GROUP_NAME)) {
				groupContents.append(getTabContent(entry.getKey(), entry.getValue()));
			}
		}

		params.put("%OPTION_GROUP_LINKS%", groupLinks.toString());
		params.put("%OPTION_GROUP_CONTENTS%", groupContents.toString());
		params.put("%OPTION_GROUP_GLOBAL%",
				getTableForGroup(store.get(Locale.getDefault()).get(ConfigLoader.DEFAULT_GROUP_NAME), false).toString());
		return params;
	}

	private StringBuilder tr(Option<?> option, Map<Option<?>, Object> optionStore) {
		StringBuilder line = new StringBuilder();
		line.append("<tr>");
		line.append("<td>").append(option.getName()).append("</td>");
		line.append("<td>").append(option.getPrecedence()).append("</td>");
		line.append("<td>").append(optionStore.containsKey(option) ? optionStore.get(option) : "").append("</td>");
		line.append("</tr>");
		return line;
	}

	private StringBuilder getTabContent(String groupName, Map<Option<?>, Object> optionsGroup) {
		StringBuilder tabContent = new StringBuilder();
		tabContent.append("<div class='tab-pane fade' style='margin-top:15px;' id='");
		tabContent.append(groupName);
		tabContent.append("-group'>");
		tabContent.append(getTableForGroup(optionsGroup, true));
		tabContent.append("</div>");
		return tabContent;
	}

	private StringBuilder getTableForGroup(Map<Option<?>, Object> optionsGroup, boolean customGroup) {
		StringBuilder table = new StringBuilder("<table class='table table-striped table-hover table-bordered'><thead>");
		table.append("<tr><th style='width:20%;'>Option</th><th style='width:10%;'>Precedence</th><th>Active value</th></tr></thead><tbody>");

		// CSS-related options
		table.append("<tr class='header-tr'><td colspan='3'>CSS-related options</td></tr>");
		table.append(tr(DatatableOptions.CSS_STYLE, optionsGroup));
		table.append(tr(DatatableOptions.CSS_CLASS, optionsGroup));
		table.append(tr(DatatableOptions.CSS_STRIPECLASSES, optionsGroup));
		table.append(tr(DatatableOptions.CSS_THEME, optionsGroup));
		table.append(tr(DatatableOptions.CSS_THEMEOPTION, optionsGroup));

		// Feature-related options
		table.append("<tr class='header-tr'><td colspan='3'>Feature-related options</td></tr>");
		table.append(tr(DatatableOptions.FEATURE_APPEAR, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_APPEAR_DURATION, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_AUTOWIDTH, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_DISPLAYLENGTH, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_DOM, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTERABLE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTER_CLEAR_SELECTOR, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTER_DELAY, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTER_PLACEHOLDER, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTER_SELECTOR, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_FILTER_TRIGGER, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_INFO, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_JQUERYUI, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_LENGTHCHANGE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_LENGTHMENU, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_PAGEABLE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_PAGINATIONTYPE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_PROCESSING, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_SCROLLCOLLAPSE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_SCROLLX, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_SCROLLXINNER, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_SCROLLY, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_SORTABLE, optionsGroup));
		table.append(tr(DatatableOptions.FEATURE_STATESAVE, optionsGroup));

		// AJAX-related options
		table.append("<tr class='header-tr'><td colspan='3'>AJAX-related options</td></tr>");
		table.append(tr(DatatableOptions.AJAX_SOURCE, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_SERVERSIDE, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_DEFERRENDER, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_PIPELINING, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_PIPESIZE, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_RELOAD_FUNCTION, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_RELOAD_SELECTOR, optionsGroup));
		table.append(tr(DatatableOptions.AJAX_PARAMS, optionsGroup));

		// Plugin-related options
		table.append("<tr class='header-tr'><td colspan='3'>Plugin-related options</td></tr>");
		table.append(tr(DatatableOptions.PLUGIN_FIXEDPOSITION, optionsGroup));
		table.append(tr(DatatableOptions.PLUGIN_FIXEDOFFSETTOP, optionsGroup));

		// Export-related options
		table.append("<tr class='header-tr'><td colspan='3'>Export-related options</td></tr>");
		table.append(tr(DatatableOptions.EXPORT_ENABLED_FORMATS, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_CLASS, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_CONTAINER_CLASS, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_CONTAINER_STYLE, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_FILENAME, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_LABEL, optionsGroup));
		table.append(tr(DatatableOptions.EXPORT_MIMETYPE, optionsGroup));

		// I18n-related options
		table.append("<tr class='header-tr'><td colspan='3'>I18n-related options</td></tr>");
		table.append(tr(DatatableOptions.I18N_MSG_ARIA_SORTASC, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_ARIA_SORTDESC, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_EMPTYTABLE, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_INFO, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_INFOEMPTY, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_INFOFILTERED, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_INFOPOSTFIX, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_LENGTHMENU, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_LOADINGRECORDS, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_PAGINATE_FIRST, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_PAGINATE_LAST, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_PAGINATE_NEXT, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_PAGINATE_PREVIOUS, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_PROCESSING, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_SEARCH, optionsGroup));
		table.append(tr(DatatableOptions.I18N_MSG_ZERORECORDS, optionsGroup));

		// Misc options
		table.append("<tr class='header-tr'><td colspan='3'>Misc options</td></tr>");
		table.append(tr(DatatableOptions.INTERNAL_OBJECTTYPE, optionsGroup));

		table.append("</tbody></table>");
		return table;
	}
}
