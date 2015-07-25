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
package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Feature enabled as soon as a filter selector is set, indicating that
 * filtering should be triggered externally.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see DatatableOptions#AJAX_SERVERSIDE
 */
public class ExternalFilterFeature extends AbstractExtension {

   public static final String FEATURE_NAME = "externalFilter";

   @Override
   public String getExtensionName() {
      return FEATURE_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      String filterSelector = DatatableOptions.FEATURE_FILTER_SELECTOR
            .valueFrom(table.getTableConfiguration().getOptions());
      String filterClearSelector = DatatableOptions.FEATURE_FILTER_CLEAR_SELECTOR
            .valueFrom(table.getTableConfiguration().getOptions());

      StringBuilder js = new StringBuilder();
      js.append("$('").append(filterSelector).append("').click(function() {");
      js.append("   yadcf.exFilterExternallyTriggered(oTable_").append(table.getId()).append(");");
      js.append("});");

      appendToBeforeEndDocumentReady(js.toString());

      if (StringUtils.isNotBlank(filterClearSelector)) {

         js = new StringBuilder();
         js.append("$('").append(filterClearSelector).append("').click(function() {");
         js.append("   yadcf.exResetAllFilters(oTable_").append(table.getId()).append(");");
         js.append("});");

         appendToBeforeEndDocumentReady(js.toString());
      }
   }
}