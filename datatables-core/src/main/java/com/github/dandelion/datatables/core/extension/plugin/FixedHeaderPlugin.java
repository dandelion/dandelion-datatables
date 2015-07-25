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
package com.github.dandelion.datatables.core.extension.plugin;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONValue;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Java implementation of the DataTables FixedHeader plugin.
 * </p>
 * 
 * @author Thibault Duchateau
 * @see DatatableOptions#PLUGIN_FIXEDOFFSETTOP
 * @see DatatableOptions#PLUGIN_FIXEDPOSITION
 */
public class FixedHeaderPlugin extends AbstractExtension {

   public static final String FIXEDHEADER_PLUGIN_NAME = "fixedHeader";

   @Override
   public String getExtensionName() {
      return FIXEDHEADER_PLUGIN_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      addBundle(DatatableBundles.DATATABLES_FIXEDHEADER);

      Map<String, Object> specificConfObj = getSpecificCongiguration(table);
      String specificConfStr = JSONValue.toJSONString(specificConfObj);
      appendToBeforeEndDocumentReady("new FixedHeader(oTable_" + table.getId() + "," + specificConfStr + ");");
   }

   /**
    * Depending on the attributes, the FixedHeader object may need a JSON object
    * as configuration.
    * 
    * @param table
    *           The HTML table.
    * @return Map<String, Object> Map of property used by the FixedHeader
    *         plugin.
    */
   private Map<String, Object> getSpecificCongiguration(HtmlTable table) {
      Map<String, Object> conf = new HashMap<String, Object>();

      String fixedPosition = DatatableOptions.PLUGIN_FIXEDPOSITION
            .valueFrom(table.getTableConfiguration().getOptions());
      Integer fixedOffset = DatatableOptions.PLUGIN_FIXEDOFFSETTOP
            .valueFrom(table.getTableConfiguration().getOptions());

      // fixedPosition attribute (default "top")
      if (StringUtils.isNotBlank(fixedPosition)) {
         if (fixedPosition.toLowerCase().equals("bottom")) {
            conf.put("bottom", true);
         }
         else if (fixedPosition.toLowerCase().equals("right")) {
            conf.put("right", true);
         }
         else if (fixedPosition.toLowerCase().equals("left")) {
            conf.put("left", true);
         }
         else {
            conf.put("top", true);
         }
      }
      else {
         conf.put("top", true);
      }

      // offsetTop attribute
      if (fixedOffset != null) {
         conf.put(DTConstants.DT_OFFSETTOP, fixedOffset);
      }

      return conf;
   }
}
