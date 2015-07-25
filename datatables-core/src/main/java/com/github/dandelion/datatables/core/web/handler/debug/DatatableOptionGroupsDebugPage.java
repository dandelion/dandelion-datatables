/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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

import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.util.ResourceUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.web.handler.HandlerContext;
import com.github.dandelion.core.web.handler.debug.AbstractDebugPage;
import com.github.dandelion.datatables.core.config.DatatableConfigurator;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.TableConfigurationFactory;

/**
 * <p>
 * Debug page that displays all available option groups.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 1.0.0
 */
public class DatatableOptionGroupsDebugPage extends AbstractDebugPage {

   private static final String PAGE_ID = "datatable-option-groups";
   private static final String PAGE_NAME = "Option groups";
   private static final String PAGE_LOCATION = "META-INF/resources/ddl-dt-debugger/html/datatable-option-groups.html";

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
   protected Map<String, Object> getPageContext() {
      Map<Locale, Map<String, Map<Option<?>, Object>>> store = TableConfigurationFactory.getConfigurationStore();
      Locale currentLocale = DatatableConfigurator.getLocaleResolver().resolveLocale(context.getRequest());

      List<Map<String, Object>> groupsJson = new ArrayList<Map<String, Object>>();

      int index = 0;
      for (Entry<String, Map<Option<?>, Object>> entry : store.get(currentLocale).entrySet()) {
         Map<String, Object> groupJson = new HashMap<String, Object>();
         groupJson.put("groupName", StringUtils.capitalize(entry.getKey()));
         groupJson.put("options", getGroupOptions(entry.getValue()));
         groupJson.put("active", index == 0 ? "active" : "");
         groupsJson.add(groupJson);
         index++;
      }

      Map<String, Object> pageContext = new HashMap<String, Object>();

      pageContext.put("groups", groupsJson);
      pageContext.put("page-header", PAGE_NAME);
      return pageContext;
   }

   private List<Map<String, Object>> getGroupOptions(Map<Option<?>, Object> optionsMap) {

      List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();

      // Style-related options
      options.add(option(DatatableOptions.CSS_STYLE, optionsMap));
      options.add(option(DatatableOptions.CSS_CLASS, optionsMap));
      options.add(option(DatatableOptions.CSS_STRIPECLASSES, optionsMap));
      options.add(option(DatatableOptions.CSS_THEME, optionsMap));
      options.add(option(DatatableOptions.CSS_THEMEOPTION, optionsMap));

      // Feature-related options
      options.add(option(DatatableOptions.FEATURE_AUTOWIDTH, optionsMap));
      options.add(option(DatatableOptions.FEATURE_DISPLAYLENGTH, optionsMap));
      options.add(option(DatatableOptions.FEATURE_DOM, optionsMap));
      options.add(option(DatatableOptions.FEATURE_FILTERABLE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_FILTER_CLEAR_SELECTOR, optionsMap));
      options.add(option(DatatableOptions.FEATURE_FILTER_DELAY, optionsMap));
      options.add(option(DatatableOptions.FEATURE_FILTER_PLACEHOLDER, optionsMap));
      options.add(option(DatatableOptions.FEATURE_FILTER_SELECTOR, optionsMap));
      options.add(option(DatatableOptions.FEATURE_INFO, optionsMap));
      options.add(option(DatatableOptions.FEATURE_JQUERYUI, optionsMap));
      options.add(option(DatatableOptions.FEATURE_LENGTHCHANGE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_LENGTHMENU, optionsMap));
      options.add(option(DatatableOptions.FEATURE_PAGEABLE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_PAGINGTYPE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_PROCESSING, optionsMap));
      options.add(option(DatatableOptions.FEATURE_SCROLLCOLLAPSE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_SCROLLX, optionsMap));
      options.add(option(DatatableOptions.FEATURE_SCROLLXINNER, optionsMap));
      options.add(option(DatatableOptions.FEATURE_SCROLLY, optionsMap));
      options.add(option(DatatableOptions.FEATURE_SORTABLE, optionsMap));
      options.add(option(DatatableOptions.FEATURE_STATESAVE, optionsMap));

      // AJAX-related options
      options.add(option(DatatableOptions.AJAX_SOURCE, optionsMap));
      options.add(option(DatatableOptions.AJAX_SERVERSIDE, optionsMap));
      options.add(option(DatatableOptions.AJAX_DEFERRENDER, optionsMap));
      options.add(option(DatatableOptions.AJAX_PIPELINING, optionsMap));
      options.add(option(DatatableOptions.AJAX_PIPESIZE, optionsMap));
      options.add(option(DatatableOptions.AJAX_RELOAD_FUNCTION, optionsMap));
      options.add(option(DatatableOptions.AJAX_RELOAD_SELECTOR, optionsMap));
      options.add(option(DatatableOptions.AJAX_PARAMS, optionsMap));

      // Plugin-related options
      options.add(option(DatatableOptions.PLUGIN_FIXEDPOSITION, optionsMap));
      options.add(option(DatatableOptions.PLUGIN_FIXEDOFFSETTOP, optionsMap));

      // Export-related options
      options.add(option(DatatableOptions.EXPORT_ENABLED_FORMATS, optionsMap));
      options.add(option(DatatableOptions.EXPORT_CLASS, optionsMap));
      options.add(option(DatatableOptions.EXPORT_CONTAINER_CLASS, optionsMap));
      options.add(option(DatatableOptions.EXPORT_CONTAINER_STYLE, optionsMap));
      options.add(option(DatatableOptions.EXPORT_FILENAME, optionsMap));
      options.add(option(DatatableOptions.EXPORT_LABEL, optionsMap));
      options.add(option(DatatableOptions.EXPORT_MIMETYPE, optionsMap));

      // I18n-related options
      options.add(option(DatatableOptions.I18N_MSG_ARIA_SORTASC, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_ARIA_SORTDESC, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_EMPTYTABLE, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_INFO, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_INFOEMPTY, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_INFOFILTERED, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_INFOPOSTFIX, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_LENGTHMENU, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_LOADINGRECORDS, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_PAGINATE_FIRST, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_PAGINATE_LAST, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_PAGINATE_NEXT, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_PAGINATE_PREVIOUS, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_PROCESSING, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_SEARCH, optionsMap));
      options.add(option(DatatableOptions.I18N_MSG_ZERORECORDS, optionsMap));
      return options;
   }

   private Map<String, Object> option(Option<?> option, Map<Option<?>, Object> optionsMap) {
      Map<String, Object> optionMap = new HashMap<String, Object>();
      optionMap.put("name", option.getName());
      if (optionsMap.containsKey(option)) {
         if (optionsMap.get(option) instanceof Extension) {
            optionMap.put("value", optionsMap.get(option).getClass().getCanonicalName());
         }
         else {
            optionMap.put("value", optionsMap.get(option));
         }
      }
      else {
         optionMap.put("value", "");
      }
      return optionMap;
   }
}
