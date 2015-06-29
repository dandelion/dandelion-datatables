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
package com.github.dandelion.datatables.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessor;

import com.github.dandelion.datatables.thymeleaf.processor.config.DivConfAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.config.DivConfTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.ColumnFinalizerProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.ColumnInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.DivExtraHtmlFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TableFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TableInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TbodyElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TdElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TheadElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TrElProcessor;

/**
 * The Dandelion-datatables dialect.
 * 
 * @author Thibault Duchateau
 */
public class DataTablesDialect extends AbstractDialect {

   public static final String DIALECT_PREFIX = "dt";
   public static final String LAYOUT_NAMESPACE = "http://www.thymeleaf.org/dandelion/datatables";
   public static final int DT_HIGHEST_PRECEDENCE = 3500;
   public static final int DT_DEFAULT_PRECEDENCE = 8000;

   public static final String INTERNAL_BEAN_TABLE = "htmlTable";
   public static final String INTERNAL_BEAN_TABLE_CONFIGURATION = "tableConfiguration";
   public static final String INTERNAL_BEAN_CONFIGS = "configs";
   public static final String INTERNAL_NODE_CONFIG = "nodeConfig";
   public static final String INTERNAL_NODE_TABLE = "tableNode";
   public static final String INTERNAL_CONF_GROUP = "confGroup";
   public static final String INTERNAL_BEAN_TABLE_STAGING_CONF = "tableStagingConf";
   public static final String INTERNAL_BEAN_COLUMN_LOCAL_CONF = "columnLocalConf";
   public static final String INTERNAL_BEAN_COLUMN_LOCAL_EXT = "columnLocalExtension";

   public String getPrefix() {
      return DIALECT_PREFIX;
   }

   public boolean isLenient() {
      return false;
   }

   /**
    * @return all processors contained inside the Dandelion-Datatables dialect.
    */
   @Override
   public Set<IProcessor> getProcessors() {
      final Set<IProcessor> processors = new HashSet<IProcessor>();

      // Element processors
      processors.add(new TableInitializerElProcessor(new ElementNameProcessorMatcher("table",
            DIALECT_PREFIX + ":table", "true", false)));
      processors.add(new TableFinalizerElProcessor(new ElementNameProcessorMatcher("div", DIALECT_PREFIX + ":tmp",
            "internalUse", false)));
      processors.add(new TheadElProcessor(new ElementNameProcessorMatcher("thead", DIALECT_PREFIX + ":data",
            "internalUse", false)));
      processors.add(new TbodyElProcessor(new ElementNameProcessorMatcher("tbody", DIALECT_PREFIX + ":data",
            "internalUse", false)));
      processors.add(new ColumnInitializerElProcessor(new ElementNameProcessorMatcher("th", DIALECT_PREFIX + ":data",
            "internalUse", false)));
      processors.add(new ColumnFinalizerProcessor(new ElementNameProcessorMatcher("th", DIALECT_PREFIX + ":data",
            "internalUse", false)));
      processors.add(new TrElProcessor(new ElementNameProcessorMatcher("tr", DIALECT_PREFIX + ":data", "internalUse",
            false)));
      processors.add(new TdElProcessor(new ElementNameProcessorMatcher("td", DIALECT_PREFIX + ":data", "internalUse",
            false)));

      // Config processors
      processors.add(new DivConfAttrProcessor(new AttributeNameProcessorMatcher("conf", "div")));
      processors.add(new DivConfTypeAttrProcessor(new AttributeNameProcessorMatcher("confType", "div")));
      processors.add(new DivExtraHtmlFinalizerElProcessor(new ElementNameProcessorMatcher("div", DIALECT_PREFIX
            + ":tmp", "internalUseExtraHtml", false)));

      // Table attribute processors
      for (TableAttrProcessors processor : TableAttrProcessors.values()) {
         processors.add(processor.getProcessor());
      }

      // Column attribute processors
      for (ColumnAttrProcessors processor : ColumnAttrProcessors.values()) {
         processors.add(processor.getProcessor());
      }

      return processors;
   }
}