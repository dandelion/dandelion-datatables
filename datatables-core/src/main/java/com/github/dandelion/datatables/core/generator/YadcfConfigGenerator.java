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
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.ColumnConfiguration;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.util.CollectionUtils;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 * @since 1.0.0
 */
public class YadcfConfigGenerator {

   private static Logger logger = LoggerFactory.getLogger(YadcfConfigGenerator.class);

   public List<Map<String, Object>> generateConfig(HtmlTable table) {

      logger.debug("Generating YADCF Filtering configuration...");

      List<Map<String, Object>> retval = new ArrayList<Map<String, Object>>();

      // Columns configuration
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

               if (filterType != null) {

                  switch (filterType) {
                  case INPUT:
                     columnConf.put("filter_type", "text");
                     break;
                  case SELECT:
                     columnConf.put("filter_type", "select");
                     break;
                  }
               }
               else {
                  columnConf.put("filter_type", "text");
               }
               retval.add(columnConf);
            }

            String selector = DatatableOptions.SELECTOR.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(selector)) {
               columnConf.put("filter_container_id", selector);
            }

            String filterValues = DatatableOptions.FILTERVALUES.valueFrom(columnConfiguration);
            if (StringUtils.isNotBlank(filterValues)) {
               columnConf.put("data", new JsSnippet(filterValues));
            }

            String filterSelector = DatatableOptions.FEATURE_FILTER_SELECTOR.valueFrom(table);
            if (StringUtils.isNotBlank(filterSelector)) {
               columnConf.put("externally_triggered", true);
            }

            // Filtering delay
            Integer filterColumnDelay = DatatableOptions.FEATURE_FILTER_DELAY.valueFrom(table.getTableConfiguration());
            if (filterColumnDelay != null) {
               columnConf.put("filter_delay", filterColumnDelay);
            }
         }

         columnIndex++;
      }

      logger.debug("Column filtering configuration generated");

      return retval;
   }

   /**
    * <p>
    * Type of filter that can be used when individual column filtering is
    * enabled.
    * </p>
    * 
    * @author Thibault Duchateau
    */
   public enum FilterType {
      SELECT, INPUT;
   }
}