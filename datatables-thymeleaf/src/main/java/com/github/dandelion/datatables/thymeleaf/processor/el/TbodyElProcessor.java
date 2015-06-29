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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;

/**
 * <p>
 * Element processor applied to the HTML {@code tbody} tag.
 * <p>
 * This processor adds the {@code dt:data} attribute to all {@code tr} and
 * {@code td} tags which will be duplicated (by {@code th:each} attribute) and
 * internally processed by other attribute processors to fill the
 * {@link HtmlTable} bean.
 * 
 * @author Thibault Duchateau
 */
public class TbodyElProcessor extends AbstractElProcessor {

   public TbodyElProcessor(IElementNameProcessorMatcher matcher) {
      super(matcher);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getPrecedence() {
      return 4000;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected ProcessorResult doProcessElement(Arguments arguments, Element element, HttpServletRequest request,
         HttpServletResponse response, HtmlTable htmlTable) {

      // All the tbody tag are iterated over
      for (Node trChild : element.getChildren()) {

         if (trChild != null && trChild instanceof Element) {

            Element trChildTag = (Element) trChild;
            String trChildTagName = trChildTag.getNormalizedName();

            // The tr nodes must be processed (for HtmlRow creation)
            trChildTag.setProcessable(true);

            if (trChildTagName != null && trChildTagName.equals("tr")) {

               if (trChildTag.hasAttribute("th:each")) {

                  trChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");

                  for (Node grandchild : trChildTag.getChildren()) {

                     if (grandchild != null && grandchild instanceof Element) {

                        Element tdChildTag = (Element) grandchild;
                        tdChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");

                        // The td nodes must be processed too (for
                        // HtmlColumn creation)
                        tdChildTag.setProcessable(true);
                     }
                  }
               }
            }
         }
      }

      // Housekeeping
      if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
         element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
      }

      return ProcessorResult.ok();
   }
}