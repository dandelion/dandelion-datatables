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
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.generator.YadcfConfigGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Abstract base class of the filtering feature.
 * </p>
 * 
 * <p>
 * Depending on the value of the
 * {@link DatatableOptions#FEATURE_FILTER_PLACEHOLDER} option, the table may
 * need slight adaptation, which depends on the template engine.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 * @see DatatableOptions#FILTERABLE
 * @see DatatableOptions#FEATURE_FILTER_PLACEHOLDER
 */
public abstract class AbstractFilteringFeature extends AbstractExtension {

   public static final String FILTERING_FEATURE_NAME = "filtering";

   @Override
   public String getExtensionName() {
      return FILTERING_FEATURE_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      addBundle(DatatableBundles.YADCF);

      FilterPlaceholder filterPlaceHolder = DatatableOptions.FEATURE_FILTER_PLACEHOLDER.valueFrom(table
            .getTableConfiguration());
      if (filterPlaceHolder != null) {
         switch (filterPlaceHolder) {
         case FOOTER:
            adaptFooter(table);
            break;
         case HEADER:
            // Nothing to do
            break;
         case NONE:
            break;
         }
      }
      // Default: footer
      else {
         filterPlaceHolder = FilterPlaceholder.FOOTER;
         adaptFooter(table);
      }

      YadcfConfigGenerator configGenerator = new YadcfConfigGenerator();
      List<Map<String, Object>> config = configGenerator.generateConfig(table);

      Writer writer = new StringWriter();
      try {
         JSONValue.writeJSONString(config, writer);
      }
      catch (IOException e) {
         throw new DandelionException("Unable to convert the configuration to JSON", e);
      }

      StringBuilder yadcf = new StringBuilder("yadcf.init(oTable_");
      yadcf.append(table.getId());
      yadcf.append(",");
      yadcf.append(writer.toString());

      if (filterPlaceHolder != null) {
         yadcf.append(", '");
         yadcf.append(filterPlaceHolder.getName());
         yadcf.append("'");
      }

      yadcf.append(");");
      appendToBeforeEndDocumentReady(yadcf.toString());
   }

   protected abstract void adaptHeader(HtmlTable table);

   protected abstract void adaptFooter(HtmlTable table);
}