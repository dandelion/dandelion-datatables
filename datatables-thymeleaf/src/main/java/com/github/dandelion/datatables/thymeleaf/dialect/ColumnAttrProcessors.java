/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.lang.reflect.InvocationTargetException;

import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;

import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesColumnAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThFilterTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThFilterValuesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThFilterableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSearchableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSelectorAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSortDirectionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSortInitAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSortTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThSortableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.basic.ThVisibleAttrProcessor;

/**
 * All attribute processors used by Dandelion-DataTables.
 *
 * @since 0.9.0
 */
public enum ColumnAttrProcessors {
	
    BASIC_SORTABLE(ThSortableAttrProcessor.class, "sortable", "th"),
    BASIC_FILTERABLE(ThFilterableAttrProcessor.class, "filterable", "th"),
    BASIC_SEARCHABLE(ThSearchableAttrProcessor.class, "searchable", "th"),
    BASIC_VISIBLE(ThVisibleAttrProcessor.class, "visible", "th"),
    BASIC_FILTER_TYPE(ThFilterTypeAttrProcessor.class, "filterType", "th"),
    BASIC_FILTER_VALUES(ThFilterValuesAttrProcessor.class, "filterValues", "th"),
    BASIC_SORT_INIT(ThSortInitAttrProcessor.class, "sortinit", "th"),
    BASIC_SORT_DIR(ThSortDirectionAttrProcessor.class, "sortdir", "th"),
    BASIC_SORT_TYPE(ThSortTypeAttrProcessor.class, "sorttype", "th"),
    BASIC_SELECTOR(ThSelectorAttrProcessor.class, "selector", "th");
    
    private Class<? extends AbstractDatatablesColumnAttrProcessor> processorClass;
    private String attributeName;
    private String elementNameFilter;

    private ColumnAttrProcessors(Class<? extends AbstractDatatablesColumnAttrProcessor> processorClass, String attributeName, String elementNameFilter) {
        this.processorClass = processorClass;
        this.attributeName = attributeName;
        this.elementNameFilter = elementNameFilter;
    }

    public AbstractDatatablesColumnAttrProcessor getProcessor() {
        AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(attributeName, elementNameFilter);
        try {
            return processorClass.getDeclaredConstructor(IAttributeNameProcessorMatcher.class).newInstance(matcher);
        } catch (InstantiationException e) {
        	throw new DataTableProcessingException(e);
        } catch (IllegalAccessException e) {
        	throw new DataTableProcessingException(e);
        } catch (InvocationTargetException e) {
        	throw new DataTableProcessingException(e);
        } catch (NoSuchMethodException e) {
        	throw new DataTableProcessingException(e);
        }
    }
}
