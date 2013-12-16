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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.export.Format;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.CollectionUtils;

/**
 * <p>
 * Class in charge of Column Filtering widget configuration generation.
 * 
 * @author Thibault Duchateau
 */
public class ColumnFilteringGenerator extends AbstractConfigurationGenerator {

    // Logger
    private static Logger logger = LoggerFactory.getLogger(ColumnFilteringGenerator.class);

    public Map<String, Object> generateConfig(HtmlTable table) {

        logger.debug("Generating Column Filtering configuration...");

        // Filtering configuration object
        Map<String, Object> filteringConf = new HashMap<String, Object>();

        FilterPlaceholder filterPlaceholder = TableConfig.FEATURE_FILTER_PLACEHOLDER.valueFrom(table);
        if(filterPlaceholder != null){
        	filteringConf.put(DTConstants.DT_S_PLACEHOLDER, filterPlaceholder.getName());
        }
        
        // Columns configuration
        Map<String, Object> tmp = null;
        List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
        for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
        	
        	Set<String> enabledDisplayTypes = column.getEnabledDisplayTypes();
			if (CollectionUtils.containsAny(enabledDisplayTypes, Format.ALL,  Format.HTML)) {
        		tmp = new HashMap<String, Object>();
        		
        		ColumnConfiguration columnConfiguration = column.getColumnConfiguration();
        		
        		Boolean filterable = ColumnConfig.FILTERABLE.valueFrom(columnConfiguration);
        		FilterType filterType = ColumnConfig.FILTERTYPE.valueFrom(columnConfiguration);
				if (filterable != null && filterable && filterType != null) {
        		
        			switch(filterType){
					case INPUT:
						tmp.put(DTConstants.DT_FILTER_TYPE, "text");
						break;
					case NUMBER:
						tmp.put(DTConstants.DT_FILTER_TYPE, "number");
						break;
					case SELECT:
						tmp.put(DTConstants.DT_FILTER_TYPE, "select");
						break;
					case NUMBER_RANGE:
						tmp.put(DTConstants.DT_FILTER_TYPE, "number-range");        				
						break;
        			}
        		}
        		else{
        			tmp.put(DTConstants.DT_FILTER_TYPE, "null");
        		}
        		
				String selector = ColumnConfig.SELECTOR.valueFrom(columnConfiguration);
        		if(StringUtils.isNotBlank(selector)){
        			tmp.put(DTConstants.DT_S_SELECTOR, selector);
        		}

        		String filterValues = ColumnConfig.FILTERVALUES.valueFrom(columnConfiguration);
        		if(StringUtils.isNotBlank(filterValues)){
					tmp.put(DTConstants.DT_FILTER_VALUES, new JavascriptSnippet(filterValues));
				}
        		
        		Integer filterLength = ColumnConfig.FILTERLENGTH.valueFrom(columnConfiguration);
        		if(filterLength != null){
        			tmp.put(DTConstants.DT_FILTER_LENGTH, filterLength);
        		}
        		aoColumnsContent.add(tmp);
        	}
        }
        filteringConf.put(DTConstants.DT_AOCOLUMNS, aoColumnsContent);

        logger.debug("Column filtering configuration generated");

        return filteringConf;
    }
}