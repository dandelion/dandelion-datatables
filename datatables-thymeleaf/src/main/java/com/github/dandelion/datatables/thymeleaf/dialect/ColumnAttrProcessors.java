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

import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractColumnAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThCssCellClassAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThDefaultAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThFilterTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThFilterValuesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThFilterableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThNameAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThPropertyAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThRenderFunctionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSearchableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSelectorAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSortDirectionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSortInitDirectionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSortInitOrderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSortTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThSortableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.ThVisibleAttrProcessor;

/**
 * All attribute processors used by Dandelion-DataTables.
 * 
 * @since 0.9.0
 */
public enum ColumnAttrProcessors {

   PROPERTY(ThPropertyAttrProcessor.class, "property", "th"),
   DEFAULT(ThDefaultAttrProcessor.class, "default", "th"),
   NAME(ThNameAttrProcessor.class, "name", "th"),
   RENDER_FUNCTION(ThRenderFunctionAttrProcessor.class, "renderFunction", "th"),
   SORTABLE(ThSortableAttrProcessor.class, "sortable", "th"),
   FILTERABLE(ThFilterableAttrProcessor.class, "filterable", "th"),
   SEARCHABLE(ThSearchableAttrProcessor.class, "searchable", "th"),
   VISIBLE(ThVisibleAttrProcessor.class, "visible", "th"),
   FILTER_TYPE(ThFilterTypeAttrProcessor.class, "filterType", "th"),
   FILTER_VALUES(ThFilterValuesAttrProcessor.class, "filterValues", "th"),
   SORT_INITDIRECTION(ThSortInitDirectionAttrProcessor.class, "sortInitDirection", "th"),
   SORT_INITORDER(ThSortInitOrderAttrProcessor.class, "sortInitOrder", "th"),
   SORT_DIR(ThSortDirectionAttrProcessor.class, "sortDir", "th"),
   SORT_TYPE(ThSortTypeAttrProcessor.class, "sortType", "th"), 
   SELECTOR(ThSelectorAttrProcessor.class, "selector", "th"),
   CSS_CELLCLASS(ThCssCellClassAttrProcessor.class, "cssCellClass", "th");

   private Class<? extends AbstractColumnAttrProcessor> processorClass;
   private String attributeName;
   private String elementNameFilter;

   private ColumnAttrProcessors(Class<? extends AbstractColumnAttrProcessor> processorClass, String attributeName,
         String elementNameFilter) {
      this.processorClass = processorClass;
      this.attributeName = attributeName;
      this.elementNameFilter = elementNameFilter;
   }

   public AbstractColumnAttrProcessor getProcessor() {
      AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(attributeName, elementNameFilter,
            DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");
      try {
         return processorClass.getDeclaredConstructor(IAttributeNameProcessorMatcher.class).newInstance(matcher);
      }
      catch (Exception e) {
         throw new DandelionException(e);
      }
   }
}
