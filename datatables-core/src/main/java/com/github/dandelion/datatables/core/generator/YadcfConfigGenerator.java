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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.util.CollectionUtils;

/**
 * <p>
 * Class in charge of Column Filtering widget configuration generation.
 * 
 * @author Thibault Duchateau
 */
public class YadcfConfigGenerator {

	private static Logger logger = LoggerFactory.getLogger(YadcfConfigGenerator.class);

	public List<Map<String, Object>> generateConfig(HtmlTable table) {

		logger.debug("Generating YADCF Filtering configuration...");

		List<Map<String, Object>> retval = new ArrayList<Map<String, Object>>();
		
//		// Placeholder for filtering elements
//		FilterPlaceholder filterPlaceholder = DatatableOptions.FEATURE_FILTER_PLACEHOLDER.valueFrom(table.getTableConfiguration());
//		if (filterPlaceholder != null) {
//			filteringConf.put(DTConstants.DT_S_PLACEHOLDER, filterPlaceholder.getExtensionName());
//		}
//
//		// Filtering trigger
//		String filterTrigger = DatatableOptions.FEATURE_FILTER_TRIGGER.valueFrom(table.getTableConfiguration());
//		if (StringUtils.isNotBlank(filterTrigger)) {
//			filteringConf.put(DTConstants.DT_S_FILTERING_TRIGGER, filterTrigger);
//		}
//
//		// Filtering delay
//		Integer filterColumnDelay = DatatableOptions.FEATURE_FILTER_DELAY.valueFrom(table.getTableConfiguration());
//		if (filterColumnDelay != null) {
//			filteringConf.put(DTConstants.DT_I_FILTERING_DELAY, filterColumnDelay);
//		}

		// Columns configuration
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		int columnIndex = 0;
		for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {


			Set<String> enabledDisplayTypes = column.getEnabledDisplayTypes();
			if (CollectionUtils.containsAny(enabledDisplayTypes, ReservedFormat.ALL, ReservedFormat.HTML)) {

				// Filtering configuration object
				Map<String, Object> columnConf = new HashMap<String, Object>();

				ColumnConfiguration columnConfiguration = column.getColumnConfiguration();

				Boolean filterable = DatatableOptions.FILTERABLE.valueFrom(columnConfiguration);
				if (filterable != null && filterable) {

					columnConf.put("column_number", columnIndex);
					FilterType filterType = DatatableOptions.FILTERTYPE.valueFrom(columnConfiguration);
					if(filterType != null){
						
					
					switch (filterType) {
					case INPUT:
						columnConf.put("filter_type", "text");
						break;
					case AUTO_COMPLETE:
						columnConf.put("filter_type", "auto_complete");
						break;
					case NUMBER:
						columnConf.put("filter_type", "number");
						break;
					case SELECT:
						columnConf.put(DTConstants.DT_FILTER_TYPE, "select");
						break;
					case NUMBER_RANGE:
						columnConf.put(DTConstants.DT_FILTER_TYPE, "number-range");
						break;
					case DATE_RANGE:
						columnConf.put(DTConstants.DT_FILTER_TYPE, "range_date");
						String dateFormat = DatatableOptions.FILTERDATEFORMAT.valueFrom(columnConfiguration);
						if (StringUtils.isNotBlank(dateFormat)) {
							columnConf.put(DTConstants.DT_S_DATEFORMAT, dateFormat);
						}
						break;
					}
					}
					else {
						columnConf.put("filter_type", "text");
					}
					retval.add(columnConf);
				} 

//				String name = DatatableOptions.NAME.valueFrom(columnConfiguration);
//				if (StringUtils.isNotBlank(name)) {
//					tmp.put(DTConstants.DT_NAME, name);
//				}

//				String selector = DatatableOptions.SELECTOR.valueFrom(columnConfiguration);
//				if (StringUtils.isNotBlank(selector)) {
//					tmp.put(DTConstants.DT_S_SELECTOR, selector);
//				}
//
//				String filterValues = DatatableOptions.FILTERVALUES.valueFrom(columnConfiguration);
//				if (StringUtils.isNotBlank(filterValues)) {
//					tmp.put(DTConstants.DT_FILTER_VALUES, new JsSnippet(filterValues));
//				}
//
//				Integer filterMinLength = DatatableOptions.FILTERMINLENGTH.valueFrom(columnConfiguration);
//				if (filterMinLength != null) {
//					tmp.put(DTConstants.DT_FILTER_LENGTH, filterMinLength);
//				}
//				aoColumnsContent.add(tmp);
//
//				String filterCssClass = DatatableOptions.FILTERCSSCLASS.valueFrom(columnConfiguration);
//				if (StringUtils.isNotBlank(filterCssClass)) {
//					tmp.put(DTConstants.DT_FILTER_CLASS, filterCssClass);
//				}
				
			}
			
			columnIndex++;
		}
//		filteringConf.put(DTConstants.DT_AOCOLUMNS, aoColumnsContent);

		logger.debug("Column filtering configuration generated");

		return retval;
	}
}