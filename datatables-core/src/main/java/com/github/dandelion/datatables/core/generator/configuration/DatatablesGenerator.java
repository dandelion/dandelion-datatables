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
package com.github.dandelion.datatables.core.generator.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.constants.DTMessages;
import com.github.dandelion.datatables.core.constants.Direction;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.CollectionUtils;

/**
 * <p>
 * Generator for the Datatables configuration.
 * 
 * @author Thibault Duchateau
 */
public class DatatablesGenerator extends AbstractConfigurationGenerator {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(DatatablesGenerator.class);

	/**
	 * If no custom config is specified with table attributes, DataTables will
	 * internally use default one.
	 * 
	 * @param table
	 *            The POJO containing the HTML table.
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

	private void generateScrollingConfiguration(Map<String, Object> mainConf,
			TableConfiguration tableConfiguration) {
		String featureScrollY = TableConfig.FEATURE_SCROLLY.valueFrom(tableConfiguration);
		Boolean featureScrollCollapse = TableConfig.FEATURE_SCROLLCOLLAPSE.valueFrom(tableConfiguration);
		String featureScrollX = TableConfig.FEATURE_SCROLLX.valueFrom(tableConfiguration);
		String featureScrollXInner = TableConfig.FEATURE_SCROLLXINNER.valueFrom(tableConfiguration);
		
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
		
		Boolean featureFilterable = TableConfig.FEATURE_FILTERABLE.valueFrom(tableConfiguration);
		Boolean featureInfo = TableConfig.FEATURE_INFO.valueFrom(tableConfiguration);
		Boolean featurePaginate = TableConfig.FEATURE_PAGEABLE.valueFrom(tableConfiguration);
		Boolean featureSort = TableConfig.FEATURE_SORTABLE.valueFrom(tableConfiguration);
		
		if (featureFilterable != null) {
			mainConf.put(DTConstants.DT_FILTER, featureFilterable);
		}
		if (featureInfo != null) {
			mainConf.put(DTConstants.DT_INFO, featureInfo);
		}
		if (featurePaginate != null) {
			mainConf.put(DTConstants.DT_PAGINATE, featurePaginate);
		}
		if (featureSort != null) {
			mainConf.put(DTConstants.DT_SORT, featureSort);
		}
	}

	private void generateMiscConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
		
		String featureDom = TableConfig.FEATURE_DOM.valueFrom(tableConfiguration);
		Boolean featureAutoWidth = TableConfig.FEATURE_AUTOWIDTH.valueFrom(tableConfiguration);
		String featureLengthMenu = TableConfig.FEATURE_LENGTHMENU.valueFrom(tableConfiguration);
		String cssStripeClasses = TableConfig.CSS_STRIPECLASSES.valueFrom(tableConfiguration);
		Integer featureDisplayLength = TableConfig.FEATURE_DISPLAYLENGTH.valueFrom(tableConfiguration);
		Boolean featureLengthChange = TableConfig.FEATURE_LENGTHCHANGE.valueFrom(tableConfiguration);
		PaginationType featurePaginationType = TableConfig.FEATURE_PAGINATIONTYPE.valueFrom(tableConfiguration);
		Boolean featureStateSave = TableConfig.FEATURE_STATESAVE.valueFrom(tableConfiguration);
		Boolean featureJqueryUi = TableConfig.FEATURE_JQUERYUI.valueFrom(tableConfiguration);
		
		if (StringUtils.isNotBlank(featureDom)) {
			mainConf.put(DTConstants.DT_DOM, featureDom);
		}
		if(featureAutoWidth != null){
			mainConf.put(DTConstants.DT_AUTO_WIDTH, featureAutoWidth);
		}
		if (featureDisplayLength != null) {
			mainConf.put(DTConstants.DT_I_DISPLAY_LENGTH, featureDisplayLength);
		}
		if (featureLengthChange != null) {
			mainConf.put(DTConstants.DT_LENGTH_CHANGE, featureLengthChange);
		}
		if (featurePaginationType != null) {
			mainConf.put(DTConstants.DT_PAGINATION_TYPE, featurePaginationType.toString());
		}
		if (featureStateSave != null) {
			mainConf.put(DTConstants.DT_STATE_SAVE, featureStateSave);
		}
		if (featureJqueryUi != null) {
			mainConf.put(DTConstants.DT_JQUERYUI, featureJqueryUi);
		}
		if (StringUtils.isNotBlank(featureLengthMenu)) {
			mainConf.put(DTConstants.DT_A_LENGTH_MENU, new JavascriptSnippet(featureLengthMenu));
		}
		if (StringUtils.isNotBlank(cssStripeClasses)) {
			mainConf.put(DTConstants.DT_AS_STRIPE_CLASSES,
					new JavascriptSnippet(cssStripeClasses));
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
				String name = ColumnConfig.NAME.valueFrom(columnConfiguration);
				if(StringUtils.isNotBlank(name)) {
					tmp.put(DTConstants.DT_S_NAME, name);
				}
				
				// Sortable
				Boolean sortable = ColumnConfig.SORTABLE.valueFrom(columnConfiguration);
				if(sortable != null) {
					tmp.put(DTConstants.DT_SORTABLE, sortable);
				}

				// Searchable
				Boolean searchable = ColumnConfig.SEARCHABLE.valueFrom(columnConfiguration);
				if(searchable != null){
					tmp.put(DTConstants.DT_SEARCHABLE, searchable);
				}

				// Visible
				Boolean visible = ColumnConfig.VISIBLE.valueFrom(columnConfiguration);
				if(visible != null){
					tmp.put(DTConstants.DT_VISIBLE, visible);
				}

				// Column's content
				String property = ColumnConfig.PROPERTY.valueFrom(columnConfiguration);
				if (StringUtils.isNotBlank(property)) {
					tmp.put(DTConstants.DT_DATA, property);
				}

				String renderFunction = ColumnConfig.RENDERFUNCTION.valueFrom(columnConfiguration);
				if (StringUtils.isNotBlank(renderFunction)) {
					tmp.put(DTConstants.DT_COLUMN_RENDERER, new JavascriptSnippet(renderFunction));
				}

				String defaultValue = ColumnConfig.DEFAULTVALUE.valueFrom(columnConfiguration);
				if (defaultValue != null) {
					tmp.put(DTConstants.DT_S_DEFAULT_CONTENT, defaultValue);
				}
				else {
					tmp.put(DTConstants.DT_S_DEFAULT_CONTENT, "");
				}

				// Column's style (AJAX only)
				StringBuilder cssCellClass = ColumnConfig.CSSCELLCLASS.valueFrom(columnConfiguration);
				if (StringUtils.isNotBlank(TableConfig.AJAX_SOURCE.valueFrom(table))) {
					if (cssCellClass != null && StringUtils.isNotBlank(cssCellClass.toString())) {
						tmp.put(DTConstants.DT_S_CLASS, cssCellClass.toString());
					}
				}

				// Sorting direction
				List<Direction> sortDirections = ColumnConfig.SORTDIRECTION.valueFrom(columnConfiguration);
				if (sortDirections != null) {
					List<String> directions = new ArrayList<String>();
					for (Direction direction : sortDirections) {
						directions.add(direction.value);
					}
					tmp.put(DTConstants.DT_SORT_DIR, directions);
				}

				// Sorting type
				String sortType = ColumnConfig.SORTTYPE.valueFrom(columnConfiguration);
				if (StringUtils.isNotBlank(sortType)) {
					tmp.put(DTConstants.DT_S_TYPE, sortType);
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
			
			String sortInitDirection = ColumnConfig.SORTINITDIRECTION.valueFrom(column.getColumnConfiguration());
			Integer sortInitOrder = ColumnConfig.SORTINITORDER.valueFrom(column.getColumnConfiguration());

			if (sortInitOrder != null && StringUtils.isNotBlank(sortInitDirection)) {
				Map<Integer, String> value = new HashMap<Integer, String>();
				value.put(columnIndex, sortInitDirection);
				sortedColumnMap.put(sortInitOrder, value);
			}
			else if(StringUtils.isNotBlank(sortInitDirection)){
				Map<Integer, String> value = new HashMap<Integer, String>();
				value.put(columnIndex, sortInitDirection);
				sortedColumnMap.put(sortedColumnMap.keySet().size(), value);
			}
			columnIndex++;
		}
		
		for(Entry<Integer, Map<Integer, String>> entry : sortedColumnMap.entrySet()) {
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
	 *            The mainConfiguration to update.
	 * @param tableConfiguration
	 *            The {@link TableConfiguration} to read configuration from.
	 */
	private void generateI18nConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
		Map<String, Object> languageMap = new HashMap<String, Object>();
		Map<String, Object> languagePaginateMap = new HashMap<String, Object>();
		Map<String, Object> languageAriaMap = new HashMap<String, Object>();

		if (tableConfiguration.getMessages() != null && tableConfiguration.getMessages().size() > 0) {
			for (Entry<Object, Object> entry : tableConfiguration.getMessages().entrySet()) {
				for (DTMessages conf : DTMessages.values()) {
					if (entry.getKey().equals(conf.getPropertyName())
							&& StringUtils.isNotBlank(entry.getValue().toString())) {
						if (entry.getKey().toString().contains("paginate")) {
							languagePaginateMap.put(conf.getRealName(), entry.getValue());
						} else if (entry.getKey().toString().contains("aria")) {
							languageAriaMap.put(conf.getRealName(), entry.getValue());
						} else {
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
	 *            The map that is to be used to generate the JSON configuration.
	 * @param tableConfiguration
	 *            The table configuration to read from.
	 */
	private void generateAjaxConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
		Boolean ajaxProcessing = TableConfig.FEATURE_PROCESSING.valueFrom(tableConfiguration);
		Boolean ajaxDeferRender = TableConfig.AJAX_DEFERRENDER.valueFrom(tableConfiguration);
		Boolean ajaxServerSide = TableConfig.AJAX_SERVERSIDE.valueFrom(tableConfiguration);
		
		if (ajaxProcessing != null) {
			mainConf.put(DTConstants.DT_B_PROCESSING, ajaxProcessing);
		}
		if (ajaxDeferRender != null) {
			mainConf.put(DTConstants.DT_DEFER_RENDER, ajaxDeferRender);
		}
		
		if (ajaxServerSide != null) {
			String ajaxSource = TableConfig.AJAX_SOURCE.valueFrom(tableConfiguration);
			String ajaxServerData = TableConfig.AJAX_SERVERDATA.valueFrom(tableConfiguration);
			String ajaxServerParam = TableConfig.AJAX_SERVERPARAM.valueFrom(tableConfiguration);
			String ajaxServerMethod = TableConfig.AJAX_SERVERMETHOD.valueFrom(tableConfiguration);
			
			mainConf.put(DTConstants.DT_B_SERVER_SIDE, ajaxServerSide);

			if (StringUtils.isNotBlank(ajaxSource)) {
				mainConf.put(DTConstants.DT_S_AJAX_SOURCE, ajaxSource);
			}
			if (StringUtils.isNotBlank(ajaxServerData)) {
				mainConf.put(DTConstants.DT_FN_SERVERDATA,
						new JavascriptSnippet(ajaxServerData));
			}
			if (StringUtils.isNotBlank(ajaxServerParam)) {
				mainConf.put(DTConstants.DT_FN_SERVERPARAMS,
						new JavascriptSnippet(ajaxServerParam));
			}
			if (StringUtils.isNotBlank(ajaxServerMethod)) {
				mainConf.put(DTConstants.DT_S_SERVERMETHOD, ajaxServerMethod);
			}
		}
	}

	private void generateCallbackConfiguration(Map<String, Object> mainConf, TableConfiguration tableConfiguration) {
		// Callbacks
		if (tableConfiguration.getCallbacks() != null) {
			for (Callback callback : tableConfiguration.getCallbacks()) {
				mainConf.put(callback.getType().getName(), callback.getFunction());
			}
		}
	}
}