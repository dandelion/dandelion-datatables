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
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;
import com.github.dandelion.datatables.core.extension.feature.ExtraHtmlFeature;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * JSP tag used to create a HTML snippet and insert it anywhere around the table
 * thanks to the {@link DatatableOptions#FEATURE_DOM} configuration.
 * </p>
 * <p>
 * Note that this tag will be processed only once, at the first iteration.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" dom="l0frtip"> 
 *   &lt;datatables:column title="Id" property="id" />
 *   &lt;datatables:column title="LastName" property="lastName" />
 *   &lt;datatables:column title="FirstName" property="firstName" />
 *   &lt;datatables:column title="City" property="address.town.name" />
 *   &lt;datatables:column title="Mail" property="mail" />
 *   &lt;datatables:extraHtml uid="0" cssStyle="float:right; margin-left: 5px;"> 
 *     &lt;a class="btn" onclick="alert('Click!');">My custom link&lt;/a> 
 *   &lt;/datatables:extraHtml>
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class ExtraHtmlTag extends BodyTagSupport {

   private static final long serialVersionUID = -3060955123376442925L;

   /**
    * Tag attributes
    */
   private String uid;
   private String container;
   private String cssStyle;
   private String cssClass;
   private boolean escapeXml = true; // Whether XML characters should be

   // escaped

   @Override
   public int doStartTag() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
      if (parent != null) {
         return EVAL_BODY_BUFFERED;
      }

      throw new JspException("The tag 'extraHtml' must be inside the 'table' tag.");
   }

   @Override
   public int doEndTag() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      // The tag is evaluated only once, at the first iteration
      if (parent.isFirstIteration()) {

         ExtraHtml extraHtml = new ExtraHtml();
         extraHtml.setUid(this.uid);
         extraHtml.setContainer(
               StringUtils.isNotBlank(this.container) ? StringUtils.escape(this.escapeXml, this.container) : "div");
         extraHtml.setCssStyle(this.cssStyle);
         extraHtml.setCssClass(this.cssClass);
         if (getBodyContent() != null) {
            extraHtml.setContent(getBodyContent().getString().replaceAll("[\n\r]", "").trim());
         }

         parent.getTable().getTableConfiguration().addExtraHtmlSnippet(extraHtml);
         parent.getTable().getTableConfiguration().registerExtension(new ExtraHtmlFeature());
      }

      return EVAL_PAGE;
   }

   public void setUid(String uid) {
      this.uid = uid;
   }

   public void setContainer(String container) {
      this.container = container;
   }

   public void setCssStyle(String cssStyle) {
      this.cssStyle = cssStyle;
   }

   public void setCssClass(String cssClass) {
      this.cssClass = cssClass;
   }

   public void setEscapeXml(boolean escapeXml) {
      this.escapeXml = escapeXml;
   }
}