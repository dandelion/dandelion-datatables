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
package com.github.dandelion.datatables.core.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.extension.feature.PagingType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Callback;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Direction;
import com.github.dandelion.datatables.core.option.TableConfiguration;
import com.github.dandelion.datatables.core.util.CollectionUtils;

/**
 * <p>
 * Generator for the Datatables configuration.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class DatatableConfigGenerator extends AbstractConfigGenerator {

   private static Logger logger = LoggerFactory.getLogger(DatatableConfigGenerator.class);

   /**
    * <p>
    * Generates the configuration If no custom config is specified with table
    * attributes, DataTables will internally use default one.
    * 
    * @param table
    *           The POJO containing the HTML table.
    * @return MainConf The main configuration file associated with the HTML
    *         table.
    */
   public Map<String, Object> generateConfig(HtmlTable table) {

      TableConfiguration tableConfiguration = table.getTableConfiguration();
      logger.debug("Generating DataTables configuration ..");

      // Main configuration object
      Map<String, Object> mainConf = new HashMap<String, Object>();

      generateColumnConfiguration(mainConf, table, tableConfiguration);
      generateI18nConfiguration(mainConf, tableConfiguration);
      generateFeatureEnablementConfiguration(mainConf, tableConfiguration);
      generateScrollingConfiguration(mainConf, tableConfiguration);
      generateMiscConfiguration(mainConf, tableConfiguration);
      generateAjaxConfiguration(mainConf, tableConfiguration);
      generateCallbackConfiguration(mainConf, tableConfiguration);

      logger.debug("DataTables configuration generated");

      return mainConf;
   }

   private void generateScrollingConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
      String featureScrollY = DatatableOptions.FEATURE_SCROLLY.valueFrom(tableConfiguration);
      Boolean featureScrollCollapse = DatatableOptions.FEATURE_SCROLLCOLLAPSE.valueFrom(tableConfiguration);
      String featureScrollX = DatatableOptions.FEATURE_SCROLLX.valueFrom(tableConfiguration);
      String featureScrollXInner = DatatableOptions.FEATURE_SCROLLXINNER.valueFrom(tableConfiguration);

      if (StringUtils.isNotBlank(featureScrollY)) {
         mainConf.put(DTConstants.DT_SCROLLY, featureScrollY);
      }
      if (featureScrollCollapse != null) {
         mainConf.put(DTConstants.DT_SCROLLCOLLAPSE, featureScrollCollapse);
      }
      if (StringUtils.isNotBlank(featureScrollX)) {
         mainConf.put(DTConstants.DT_SCROLLX, featureScrollX);
      }
      if (StringUtils.isNotBlank(featureScrollXInner)) {
         mainConf.put(DTConstants.DT_SCROLLXINNER, featureScrollXInner);
      }
   }

   private void generateFeatureEnablementConfiguration(Map<String, Object> mainConf,
         TableConfiguration tableConfiguration) {

      Boolean featureFilterable = DatatableOptions.FEATURE_FILTERABLE.valueFrom(tableConfiguration);
      Boolean featureInfo = DatatableOptions.FEATURE_INFO.valueFrom(tableConfiguration);
      Boolean featurePaginate = DatatableOptions.FEATURE_PAGEABLE.valueFrom(tableConfiguration);
      Boolean featureSort = DatatableOptions.FEATURE_SORTABLE.valueFrom(tableConfiguration);
      Boolean featureProcessing = DatatableOptions.FEATURE_PROCESSING.valueFrom(tableConfiguration);

      if (featureFilterable != null) {
         mainConf.put(DTConstants.DT_FILTER, featureFilterable);
      }
      if (featureInfo != null) {
         mainConf.put(DTConstants.DT_INFO, featureInfo);
      }
      if (featurePaginate != null) {
         mainConf.put(DTConstants.DT_PAGING, featurePaginate);
      }
      if (featureSort != null) {
         mainConf.put(DTConstants.DT_SORT, featureSort);
      }
      if (featureProcessing != null) {
         mainConf.put(DTConstants.DT_B_PROCESSING, featureProcessing);
      }
   }

   private void generateMiscConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {

      String featureDom = DatatableOptions.FEATURE_DOM.valueFrom(tableConfiguration);
      Boolean featureAutoWidth = DatatableOptions.FEATURE_AUTOWIDTH.valueFrom(tableConfiguration);
      String featureLengthMenu = DatatableOptions.FEATURE_LENGTHMENU.valueFrom(tableConfiguration);
      String cssStripeClasses = DatatableOptions.CSS_STRIPECLASSES.valueFrom(tableConfiguration);
      Integer featureDisplayLength = DatatableOptions.FEATURE_DISPLAYLENGTH.valueFrom(tableConfiguration);
      Boolean featureLengthChange = DatatableOptions.FEATURE_LENGTHCHANGE.valueFrom(tableConfiguration);
      PagingType featurePaginationType = DatatableOptions.FEATURE_PAGINGTYPE.valueFrom(tableConfiguration);
      Boolean featureStateSave = DatatableOptions.FEATURE_STATESAVE.valueFrom(tableConfiguration);
      Boolean featureJqueryUi = DatatableOptions.FEATURE_JQUERYUI.valueFrom(tableConfiguration);

      if (StringUtils.isNotBlank(featureDom)) {
         mainConf.put(DTConstants.DT_DOM, featureDom);
      }
      if (featureAutoWidth != null) {
         mainConf.put(DTConstants.DT_AUTO_WIDTH, featureAutoWidth);
      }
      if (featureDisplayLength != null) {
         mainConf.put(DTConstants.DT_I_LENGTH, featureDisplayLength);
      }
      if (featureLengthChange != null) {
         mainConf.put(DTConstants.DT_LENGTH_CHANGE, featureLengthChange);
      }
      if (featurePaginationType != null) {
         mainConf.put(DTConstants.DT_PAGINGTYPE, featurePaginationType.toString());
      }
      if (featureStateSave != null) {
         mainConf.put(DTConstants.DT_STATE_SAVE, featureStateSave);
      }
      if (featureJqueryUi != null) {
         mainConf.put(DTConstants.DT_JQUERYUI, featureJqueryUi);
      }
      if (StringUtils.isNotBlank(featureLengthMenu)) {
         mainConf.put(DTConstants.DT_A_LENGTH_MENU, new JsSnippet(featureLengthMenu));
      }
      if (StringUtils.isNotBlank(cssStripeClasses)) {
         mainConf.put(DTConstants.DT_AS_STRIPE_CLASSES, new JsSnippet(cssStripeClasses));
      }

   }

   private void generateColumnConfiguration(Map<String, Object> mainConf, HtmlTable table,
         TableConfiguration tableConfiguration) {

      // Columns configuration using aoColumn
      Map<String, Object> tmp = null;
      List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
      for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
         ColumnConfiguration columnConfiguration = column.getColumnConfiguration();

         Set<String> enabledDisplayTypes = column.getEnabledDisplayTypes();
         if (CollectionUtils.containsAny(enabledDisplayTypes, ReservedFormat.ALL, ReservedFormat.HTML)) {
            tmp = new HashMap<String, Object>();

            // Name
            String name = DatatableOptions.NAME.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(name)) {
               tmp.put(DTConstants.DT_NAME, name);
            }

            // Sortable
            Boolean sortable = DatatableOptions.SORTABLE.valueFrom(columnConfiguration);
            if (sortable != null) {
               tmp.put(DTConstants.DT_SORTABLE, sortable);
            }

            // Searchable
            Boolean searchable = DatatableOptions.SEARCHABLE.valueFrom(columnConfiguration);
            if (searchable != null) {
               tmp.put(DTConstants.DT_SEARCHABLE, searchable);
            }

            // Visible
            Boolean visible = DatatableOptions.VISIBLE.valueFrom(columnConfiguration);
            if (visible != null) {
               tmp.put(DTConstants.DT_VISIBLE, visible);
            }

            // Column's content
            String property = DatatableOptions.PROPERTY.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(property)) {
               tmp.put(DTConstants.DT_DATA, property);
            }

            String renderFunction = DatatableOptions.RENDERFUNCTION.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(renderFunction)) {
               tmp.put(DTConstants.DT_COLUMN_RENDERER, new JsSnippet(renderFunction));
            }

            String defaultValue = DatatableOptions.DEFAULTVALUE.valueFrom(columnConfiguration);
            if (defaultValue != null) {
               tmp.put(DTConstants.DT_DEFAULT_CONTENT, defaultValue);
            }
            else {
               tmp.put(DTConstants.DT_DEFAULT_CONTENT, "");
            }

            // Column's style (AJAX only)
            StringBuilder cssCellClass = DatatableOptions.CSSCELLCLASS.valueFrom(columnConfiguration);
            String ajaxSource = DatatableOptions.AJAX_SOURCE.valueFrom(table.getTableConfiguration());
            if (StringUtils.isNotBlank(ajaxSource)) {
               if (cssCellClass != null && StringUtils.isNotBlank(cssCellClass.toString())) {
                  tmp.put(DTConstants.DT_S_CLASS, cssCellClass.toString());
               }
            }

            // Sorting direction
            List<Direction> sortDirections = DatatableOptions.SORTDIRECTION.valueFrom(columnConfiguration);
            if (sortDirections != null) {
               List<String> directions = new ArrayList<String>();
               for (Direction direction : sortDirections) {
                  directions.add(direction.value);
               }
               tmp.put(DTConstants.DT_SORT_DIR, directions);
            }

            // Sorting type
            String sortType = DatatableOptions.SORTTYPE.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(sortType)) {
               tmp.put(DTConstants.DT_TYPE, sortType);
            }

            aoColumnsContent.add(tmp);
         }
      }
      mainConf.put(DTConstants.DT_AOCOLUMNS, aoColumnsContent);

      // Column sorting configuration
      List<Object> aaSortingtmp = null;
      List<Object> aaSortingContent = new ArrayList<Object>();
      Map<Integer, Map<Integer, String>> sortedColumnMap = new TreeMap<Integer, Map<Integer, String>>();
      int columnIndex = 0;

      for (HtmlColumn column : table.getLastHeaderRow().getColumns(ReservedFormat.ALL, ReservedFormat.HTML)) {

         String sortInitDirection = DatatableOptions.SORTINITDIRECTION.valueFrom(column.getColumnConfiguration());
         Integer sortInitOrder = DatatableOptions.SORTINITORDER.valueFrom(column.getColumnConfiguration());

         if (sortInitOrder != null && StringUtils.isNotBlank(sortInitDirection)) {
            Map<Integer, String> value = new HashMap<Integer, String>();
            value.put(columnIndex, sortInitDirection);
            sortedColumnMap.put(sortInitOrder, value);
         }
         else if (StringUtils.isNotBlank(sortInitDirection)) {
            Map<Integer, String> value = new HashMap<Integer, String>();
            value.put(columnIndex, sortInitDirection);
            sortedColumnMap.put(sortedColumnMap.keySet().size(), value);
         }
         columnIndex++;
      }

      for (Entry<Integer, Map<Integer, String>> entry : sortedColumnMap.entrySet()) {
         aaSortingtmp = new ArrayList<Object>();
         Integer key = entry.getValue().entrySet().iterator().next().getKey();
         aaSortingtmp.add(key);
         aaSortingtmp.add(entry.getValue().get(key));
         aaSortingContent.add(aaSortingtmp);
      }

      if (!aaSortingContent.isEmpty()) {
         mainConf.put(DTConstants.DT_SORT_INIT, aaSortingContent);
      }
   }

   /**
    * Build the map that will generate the oLanguage object and update the main
    * configuration.
    * 
    * @param mainConf
    *           The mainConfiguration to update.
    * @param tableConfiguration
    *           The {@link TableConfiguration} to read configuration from.
    */
   private void generateI18nConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
      Map<String, Object> languageMap = new HashMap<String, Object>();
      Map<String, Object> languagePaginateMap = new HashMap<String, Object>();
      Map<String, Object> languageAriaMap = new HashMap<String, Object>();

      if (tableConfiguration.getMessages() != null && tableConfiguration.getMessages().size() > 0) {
         for (Entry<Object, Object> entry : tableConfiguration.getMessages().entrySet()) {
            for (DTMessages conf : DTMessages.values()) {
               if (entry.getKey().equals(conf.getPropertyName())) {
                  if (entry.getKey().toString().contains("paginate")) {
                     languagePaginateMap.put(conf.getRealName(), entry.getValue());
                  }
                  else if (entry.getKey().toString().contains("aria")) {
                     languageAriaMap.put(conf.getRealName(), entry.getValue());
                  }
                  else {
                     languageMap.put(conf.getRealName(), entry.getValue());
                  }
                  break;
               }
            }
         }
      }

      if (languagePaginateMap.size() > 0) {
         languageMap.put(DTMessages.PAGINATE.getRealName(), languagePaginateMap);
      }

      if (languageAriaMap.size() > 0) {
         languageMap.put(DTMessages.ARIA.getRealName(), languageAriaMap);
      }

      if (languageMap.size() > 0) {
         mainConf.put(DTConstants.DT_LANGUAGE, languageMap);
      }
   }

   /**
    * <p>
    * Fill the configuration map with all AJAX-related parameters.
    * 
    * @param mainConf
    *           The map that is to be used to generate the JSON configuration.
    * @param tableConfiguration
    *           The table configuration to read from.
    */
   private void generateAjaxConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
      Boolean ajaxServerSide = DatatableOptions.AJAX_SERVERSIDE.valueFrom(tableConfiguration);
      Boolean ajaxDeferRender = DatatableOptions.AJAX_DEFERRENDER.valueFrom(tableConfiguration);

      if (ajaxServerSide != null) {
         // String ajaxSource =
         // DatatableOptions.AJAX_SOURCE.valueFrom(tableConfiguration);
         // String ajaxServerData =
         // DatatableOptions.AJAX_SERVERDATA.valueFrom(tableConfiguration);
         // String ajaxServerParam =
         // DatatableOptions.AJAX_SERVERPARAM.valueFrom(tableConfiguration);
         // String ajaxServerMethod =
         // DatatableOptions.AJAX_SERVERMETHOD.valueFrom(tableConfiguration);

         mainConf.put(DTConstants.DT_B_SERVER_SIDE, ajaxServerSide);

         // if (StringUtils.isNotBlank(ajaxSource)) {
         // mainConf.put(DTConstants.DT_S_AJAX_SOURCE, ajaxSource);
         // }
         // if (StringUtils.isNotBlank(ajaxServerData)) {
         // mainConf.put(DTConstants.DT_FN_SERVERDATA, new
         // JsSnippet(ajaxServerData));
         // }
         // if (StringUtils.isNotBlank(ajaxServerParam)) {
         // mainConf.put(DTConstants.DT_FN_SERVERPARAMS, new
         // JsSnippet(ajaxServerParam));
         // }
         // if (StringUtils.isNotBlank(ajaxServerMethod)) {
         // mainConf.put(DTConstants.DT_S_SERVERMETHOD, ajaxServerMethod);
         // }
      }
      if (ajaxDeferRender != null) {
         mainConf.put(DTConstants.DT_DEFER_RENDER, ajaxDeferRender);
      }
   }

   private void generateCallbackConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
      if (tableConfiguration.getCallbacks() != null) {
         for (Callback callback : tableConfiguration.getCallbacks()) {
            mainConf.put(callback.getType().getName(), callback.getFunction());
         }
      }
   }
}