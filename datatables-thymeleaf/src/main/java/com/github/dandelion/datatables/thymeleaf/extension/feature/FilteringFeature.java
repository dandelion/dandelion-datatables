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
package com.github.dandelion.datatables.thymeleaf.extension.feature;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

import com.github.dandelion.datatables.core.extension.feature.AbstractFilteringFeature;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.util.DomUtils;

/**
 * Thymeleaf implementation of the filtering feature.
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class FilteringFeature extends AbstractFilteringFeature {

   private Arguments arguments;

   public FilteringFeature(Arguments argument) {
      this.arguments = argument;
   }

   @Override
   protected void adaptHeader(HtmlTable table) {

      Node tableNode = (Node) ((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute(
            DataTablesDialect.INTERNAL_NODE_TABLE);
      Element thead = DomUtils.findElement((Element) tableNode, "thead");

      Element tr = new Element("tr");
      for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
         Element th = new Element("th");
         th.addChild(new Text(column.getContent().toString()));
         tr.addChild(th);
      }

      if (thead != null) {
         thead.addChild(tr);
      }
      else {
         thead = new Element("thead");
         thead.addChild(tr);
         ((Element) tableNode).addChild(thead);
      }

   }

   @Override
   protected void adaptFooter(HtmlTable table) {

      Element tfoot = new Element("tfoot");

      Node tableNode = (Node) ((IWebContext) arguments.getContext()).getHttpServletRequest().getAttribute(
            DataTablesDialect.INTERNAL_NODE_TABLE);

      for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
         Element th = new Element("th");
         th.addChild(new Text(column.getContent().toString()));
         tfoot.addChild(th);
      }

      ((Element) tableNode).addChild(tfoot);
   }
}
