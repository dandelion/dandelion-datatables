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
package com.github.dandelion.datatables.core.extension.feature;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONValue;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Pipelining feature that may be used if server-side processing has been
 * enabled.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 * @see ServerSideFeature
 * @see DatatableOptions#AJAX_PIPESIZE
 */
public class PipeliningFeature extends AbstractExtension {

   public static final String PIPELINING_FEATURE_NAME = "pipelining";

   @Override
   public String getExtensionName() {
      return PIPELINING_FEATURE_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      addBundle(DatatableBundles.DDL_DT_AJAX_PIPELINING);
      Integer pipeSize = DatatableOptions.AJAX_PIPESIZE.valueFrom(table.getTableConfiguration());

      Map<String, Object> ajaxParams = new HashMap<String, Object>();
      ajaxParams.put("url", DatatableOptions.AJAX_SOURCE.valueFrom(table.getTableConfiguration()));
      if (pipeSize != null) {
         ajaxParams.put("pages", pipeSize);
      }
      else {
         ajaxParams.put("pages", 5);
      }

      Writer writer = new StringWriter();
      try {
         JSONValue.writeJSONString(ajaxParams, writer);
      }
      catch (IOException e) {
         throw new DandelionException("Unable to convert the configuration to JSON", e);
      }
      addParameter(DTConstants.DT_S_AJAX_SOURCE, new JsSnippet("$.fn.dataTable.pipeline( " + writer.toString() + ")"));
   }
}