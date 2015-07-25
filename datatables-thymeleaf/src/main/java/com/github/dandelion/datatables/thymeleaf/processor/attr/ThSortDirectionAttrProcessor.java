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
package com.github.dandelion.datatables.thymeleaf.processor.attr;

import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;

import com.github.dandelion.core.option.Option;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractColumnAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.AttributeUtils;

/**
 * <p>
 * Attribute processor applied to the {@code th} and associated with the
 * {@link DatatableOptions#SORTDIRECTION} option.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.8.9
 */
public class ThSortDirectionAttrProcessor extends AbstractColumnAttrProcessor {

   public ThSortDirectionAttrProcessor(IAttributeNameProcessorMatcher matcher) {
      super(matcher);
   }

   @Override
   public int getPrecedence() {
      return DataTablesDialect.DT_DEFAULT_PRECEDENCE;
   }

   @Override
   protected void doProcessAttribute(Arguments arguments, Element element, String attributeName,
         Map<Option<?>, Object> stagingConf, Map<Option<?>, Extension> stagingExt) {

      String attrValue = AttributeUtils.parseStringAttribute(arguments, element, attributeName);

      stagingConf.put(DatatableOptions.SORTDIRECTION, attrValue);
   }
}