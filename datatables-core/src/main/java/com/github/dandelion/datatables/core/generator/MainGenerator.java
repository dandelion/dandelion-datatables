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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Class in charge of DataTables configuration generation.
 *
 * @author Thibault Duchateau
 */
public class MainGenerator extends AbstractConfigurationGenerator {

    // Logger
    private static Logger logger = LoggerFactory.getLogger(MainGenerator.class);

    /**
     * If no custom config is specified with table attributes, DataTables
     * will internally use default one.
     *
     * @param table The POJO containing the HTML table.
     * @return MainConf The main configuration file associated with the HTML
     *         table.
     */
    public Map<String, Object> generateConfig(HtmlTable table) {

    	TableConfiguration tableConfiguration = table.getTableConfiguration();
        logger.debug("Generating DataTables configuration ..");

        // Main configuration object
        Map<String, Object> mainConf = new HashMap<String, Object>();

        // Columns configuration using aoColumn
        Map<String, Object> tmp = null;
        List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
        for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
        	
			if (column.getEnabledDisplayTypes().contains(DisplayType.ALL)
					|| column.getEnabledDisplayTypes().contains(DisplayType.HTML)) {
        		tmp = new HashMap<String, Object>();
        		
        		// Sortable
        		tmp.put(DTConstants.DT_SORTABLE, column.isSortable());
        		
        		// Searchable
        		tmp.put(DTConstants.DT_SEARCHABLE, column.getSearchable());
        		
        		// Visible
        		tmp.put(DTConstants.DT_VISIBLE, column.getVisible());
        		if(column.getVisible() != null && !column.getVisible()){
        			tmp.put(DTConstants.DT_SEARCHABLE, false);	
        		}
        		
        		// Column's content
        		if (StringUtils.isNotBlank(column.getProperty())) {
        			tmp.put(DTConstants.DT_DATA, column.getProperty());
        		}
        		
        		if (StringUtils.isNotBlank(column.getRenderFunction())) {
        			tmp.put(DTConstants.DT_COLUMN_RENDERER, new JavascriptSnippet(column.getRenderFunction()));
        		}
        		
        		if(column.getDefaultValue() != null){
        			tmp.put(DTConstants.DT_S_DEFAULT_CONTENT, column.getDefaultValue());
        		}
        		
        		// Sorting direction
        		if (column.getSortDirections() != null) {
        			tmp.put(DTConstants.DT_SORT_DIR, column.getSortDirections());
        		}
        		
        		aoColumnsContent.add(tmp);
        	}
        }
        mainConf.put(DTConstants.DT_AOCOLUMNS, aoColumnsContent);

        // Column sorting configuration
        List<Object> aaSortingtmp = null;
        List<Object> aaSortingContent = new ArrayList<Object>();
        Integer columnIndex = 0;
        for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {

            // Sorting direction
            if (StringUtils.isNotBlank(column.getSortInit())) {
                aaSortingtmp = new ArrayList<Object>();
                aaSortingtmp.add(columnIndex);
                aaSortingtmp.add(column.getSortInit());
                aaSortingContent.add(aaSortingtmp);
            }

            columnIndex++;
        }
        if (!aaSortingContent.isEmpty()) {
            mainConf.put(DTConstants.DT_SORT_INIT, aaSortingContent);
        }

        if(StringUtils.isNotBlank(tableConfiguration.getFeatureDom())){
        	mainConf.put(DTConstants.DT_DOM, tableConfiguration.getFeatureDom());
        }
        if (tableConfiguration.getExtraLabels() != null) {
            tmp = new HashMap<String, Object>();
            tmp.put(DTConstants.DT_URL, tableConfiguration.getExtraLabels());
            mainConf.put(DTConstants.DT_LANGUAGE, tmp);
        }
        if (tableConfiguration.getFeatureAutoWidth() != null) {
            mainConf.put(DTConstants.DT_AUTO_WIDTH, tableConfiguration.getFeatureAutoWidth());
        }
        if (tableConfiguration.getAjaxDeferRender() != null) {
            mainConf.put(DTConstants.DT_DEFER_RENDER, tableConfiguration.getAjaxDeferRender());
        }
        if (tableConfiguration.getFeatureFilterable() != null) {
            mainConf.put(DTConstants.DT_FILTER, tableConfiguration.getFeatureFilterable());
        }
        if (tableConfiguration.getFeatureInfo() != null) {
            mainConf.put(DTConstants.DT_INFO, tableConfiguration.getFeatureInfo());
        }
        if (tableConfiguration.getFeaturePaginate() != null) {
            mainConf.put(DTConstants.DT_PAGINATE, tableConfiguration.getFeaturePaginate());
        }
        if (tableConfiguration.getFeatureDisplayLength() != null) {
            mainConf.put(DTConstants.DT_I_DISPLAY_LENGTH, tableConfiguration.getFeatureDisplayLength());
        }
        if (tableConfiguration.getFeatureLengthChange() != null) {
            mainConf.put(DTConstants.DT_LENGTH_CHANGE, tableConfiguration.getFeatureLengthChange());
        }
        if (tableConfiguration.getFeaturePaginationType() != null) {
            mainConf.put(DTConstants.DT_PAGINATION_TYPE, tableConfiguration.getFeaturePaginationType().toString());
        }
        if (tableConfiguration.getFeatureSort() != null) {
            mainConf.put(DTConstants.DT_SORT, tableConfiguration.getFeatureSort());
        }
        if (tableConfiguration.getFeatureStateSave() != null) {
            mainConf.put(DTConstants.DT_STATE_SAVE, tableConfiguration.getFeatureStateSave());
        }
        if (tableConfiguration.getFeatureJqueryUI() != null) {
            mainConf.put(DTConstants.DT_JQUERYUI, tableConfiguration.getFeatureJqueryUI());
        }
        if(StringUtils.isNotBlank(tableConfiguration.getFeatureLengthMenu())){
        	mainConf.put(DTConstants.DT_A_LENGTH_MENU, new JavascriptSnippet(tableConfiguration.getFeatureLengthMenu()));
        }
        if(StringUtils.isNotBlank(tableConfiguration.getCssStripeClasses())){
        	mainConf.put(DTConstants.DT_AS_STRIPE_CLASSES, new JavascriptSnippet(tableConfiguration.getCssStripeClasses()));
        }
        if(StringUtils.isNotBlank(tableConfiguration.getFeatureScrollY())){
        	mainConf.put(DTConstants.DT_SCROLLY, tableConfiguration.getFeatureScrollY());
        }
        if(tableConfiguration.getFeatureScrollCollapse() != null){
        	mainConf.put(DTConstants.DT_SCROLLCOLLAPSE, tableConfiguration.getFeatureScrollCollapse());
        }
        
        // AJAX
        if (tableConfiguration.getAjaxProcessing() != null) {
            mainConf.put(DTConstants.DT_B_PROCESSING, table.getTableConfiguration().getAjaxProcessing());
        }
        if(tableConfiguration.getAjaxServerSide() != null){
        	mainConf.put(DTConstants.DT_B_SERVER_SIDE, table.getTableConfiguration().getAjaxServerSide());

        	if(StringUtils.isNotBlank(tableConfiguration.getAjaxSource())){
        		mainConf.put(DTConstants.DT_S_AJAX_SOURCE, tableConfiguration.getAjaxSource());
        	}
        	if(StringUtils.isNotBlank(tableConfiguration.getAjaxServerData())){
        		mainConf.put(DTConstants.DT_FN_SERVERDATA, new JavascriptSnippet(tableConfiguration.getAjaxServerData()));
        	}
        	if(StringUtils.isNotBlank(tableConfiguration.getAjaxServerParam())){
        		mainConf.put(DTConstants.DT_FN_SERVERPARAMS, new JavascriptSnippet(tableConfiguration.getAjaxServerParam()));
        	}
        	if(StringUtils.isNotBlank(tableConfiguration.getAjaxServerMethod())){
        		mainConf.put(DTConstants.DT_S_SERVERMETHOD, tableConfiguration.getAjaxServerMethod());
        	}
        }
        
        // Callbacks
        if(tableConfiguration.getCallbacks() != null){
        	for(Callback callback : tableConfiguration.getCallbacks()){
//        		mainConf.put(callback.getType().getName(), new JavascriptFunction(callback.getFunction(), callback.getType().getArgs()));
        		mainConf.put(callback.getType().getName(), callback.getFunction());
        	}
        }
        
        logger.debug("DataTables configuration generated");

        return mainConf;
    }
}
