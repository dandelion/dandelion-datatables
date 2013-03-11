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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.model.DisplayType;
import com.github.dandelion.datatables.core.model.HtmlColumn;
import com.github.dandelion.datatables.core.model.HtmlTable;

/**
 * Class in charge of DataTables configuration generation.
 *
 * @author Thibault Duchateau
 */
public class MainGenerator {

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

        logger.debug("Generating DataTables configuration ..");

        // Main configuration object
        Map<String, Object> mainConf = new HashMap<String, Object>();

        // Columns configuration
        Map<String, Object> tmp = null;
        List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
        for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
        	
        	if(column.getEnabledDisplayTypes().contains(DisplayType.HTML)){
        		tmp = new HashMap<String, Object>();
        		
        		// Sortable
        		tmp.put(DTConstants.DT_SORTABLE, column.isSortable());
        		
        		// Searchable
        		tmp.put(DTConstants.DT_SEARCHABLE, column.getSearchable());
        		
        		// Column's content
        		if (StringUtils.isNotBlank(column.getProperty())) {
        			tmp.put(DTConstants.DT_DATA, column.getProperty());
        		}
        		
        		if (StringUtils.isNotBlank(column.getRenderFunction())) {
        			tmp.put(DTConstants.DT_COLUMN_RENDERER, new JSFunction(column.getRenderFunction()));
        		}
        		
        		if(column.getDefaultValue() != null){
        			tmp.put(DTConstants.DT_S_DEFAULT_CONTENT, column.getDefaultValue());
        		}
        		
//        		tmp.put(DTConstants.DT_S_TYPE, "html");
        		
        		// Sorting direction
        		if (StringUtils.isNotBlank(column.getSortDirection())) {
        			List<Object> sortDirection = new ArrayList<Object>();
        			Collections.addAll(sortDirection, column.getSortDirection().trim().toLowerCase().split(","));
        			tmp.put(DTConstants.DT_SORT_DIR, sortDirection);
        		}
        		
        		aoColumnsContent.add(tmp);
        	}
        }
        mainConf.put(DTConstants.DT_AOCOLUMNS, aoColumnsContent);

        // Column sorting initialisation
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

        if (table.getLabels() != null) {
            tmp = new HashMap<String, Object>();
            tmp.put(DTConstants.DT_URL, table.getLabels());
            mainConf.put(DTConstants.DT_LANGUAGE, tmp);
        }
        if (table.getAutoWidth() != null) {
            mainConf.put(DTConstants.DT_AUTO_WIDTH, table.getAutoWidth());
        }
        if (table.getDeferRender() != null) {
            mainConf.put(DTConstants.DT_DEFER_RENDER, table.getDeferRender());
        }
        if (table.getFilterable() != null) {
            mainConf.put(DTConstants.DT_FILTER, table.getFilterable());
        }
        if (table.getInfo() != null) {
            mainConf.put(DTConstants.DT_INFO, table.getInfo());
        }
        if (table.getPaginate() != null) {
            mainConf.put(DTConstants.DT_PAGINATE, table.getPaginate());
        }
        if (table.getLengthChange() != null) {
            mainConf.put(DTConstants.DT_LENGTH_CHANGE, table.getLengthChange());
        }
        if (table.getPaginationType() != null) {
            mainConf.put(DTConstants.DT_PAGINATION_TYPE, table.getPaginationType().toString());
        }
        if (table.getSort() != null) {
            mainConf.put(DTConstants.DT_SORT, table.getSort());
        }
        if (table.getStateSave() != null) {
            mainConf.put(DTConstants.DT_STATE_SAVE, table.getStateSave());
        }
        if (table.getJqueryUI() != null) {
            mainConf.put(DTConstants.DT_JQUERYUI, table.getJqueryUI());
        }

        // AJAX
        if (table.getProcessing() != null) {
            mainConf.put(DTConstants.DT_B_PROCESSING, table.getProcessing());
        }
        if(table.getServerSide() != null){
        	mainConf.put(DTConstants.DT_B_SERVER_SIDE, table.getServerSide());

        	if(StringUtils.isNotBlank(table.getDatasourceUrl())){
        		mainConf.put(DTConstants.DT_S_AJAX_SOURCE, table.getDatasourceUrl());
        	}
        }
        
        mainConf.put(DTConstants.DT_DOM, "lfrtip");

        logger.debug("DataTables configuration generated");

        return mainConf;
    }
    
    /**
     * Private class which cause escape avoiding in JSON serialization,
     * though datatables call it as function no as string (when escaped) 
     * @author Pavel Janecka
     */
    private class JSFunction {
    	private String functionName;
    	public JSFunction(String functionName) {
    		this.functionName = functionName;
    	}
		@Override
		public String toString() {
			return functionName;
		}
    }
}
