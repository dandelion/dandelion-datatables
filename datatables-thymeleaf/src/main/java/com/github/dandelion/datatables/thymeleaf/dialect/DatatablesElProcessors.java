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

import static com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect.DIALECT_PREFIX;

import java.lang.reflect.InvocationTargetException;

import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.thymeleaf.matcher.ElementNameWithoutPrefixProcessorMatcher;
import com.github.dandelion.datatables.thymeleaf.processor.el.ColumnFinalizerProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.ColumnInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TableFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TableInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TbodyElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TdElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TheadElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.el.TrElProcessor;

/**
 * All element processors used by Dandelion-DataTables.
 *
 * @since 0.8.9
 */
public enum DatatablesElProcessors {
    TABLE_INITIALIZER(TableInitializerElProcessor.class, "table", DIALECT_PREFIX + ":table", "true"),
    TABLE_FINALIZER(TableFinalizerElProcessor.class, "div", DIALECT_PREFIX + ":tmp", "internalUse"),
    COLUMN_INITIALIZER(ColumnInitializerElProcessor.class, "th"),
    COLUMN_FINALIZER(ColumnFinalizerProcessor.class, "th", DIALECT_PREFIX + ":data", "internalUse"),
    THEAD(TheadElProcessor.class, "thead"),
    TBODY(TbodyElProcessor.class, "tbody"),
    TR(TrElProcessor.class, "tr", DIALECT_PREFIX + ":data", "internalUse"),
    TD(TdElProcessor.class, "td", DIALECT_PREFIX + ":data", "internalUse");

    private Class<? extends AbstractElementProcessor> processorClass;
    private String elementName;
    private String filterAttributeName;
    private String filterAttributeValue;
    private boolean withFilter;

    DatatablesElProcessors(Class<? extends AbstractElementProcessor> processorClass,
                          String elementName) {
        this.processorClass = processorClass;
        this.elementName = elementName;
        this.withFilter = false;
    }

    DatatablesElProcessors(Class<? extends AbstractElementProcessor> processorClass,
                          String elementName, String filterAttributeName, String filterAttributeValue) {
        this.processorClass = processorClass;
        this.elementName = elementName;
        this.filterAttributeName = filterAttributeName;
        this.filterAttributeValue = filterAttributeValue;
        this.withFilter = true;
    }

    public AbstractElementProcessor getProcessor() {
        ElementNameWithoutPrefixProcessorMatcher matcher;
        if(withFilter) {
            matcher = new ElementNameWithoutPrefixProcessorMatcher(elementName, filterAttributeName, filterAttributeValue);
        } else {
            matcher = new ElementNameWithoutPrefixProcessorMatcher(elementName);
        }

        try {
            return processorClass.getDeclaredConstructor(IElementNameProcessorMatcher.class).newInstance(matcher);
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