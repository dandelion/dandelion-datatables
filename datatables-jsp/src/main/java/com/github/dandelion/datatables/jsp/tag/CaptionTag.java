/*
 * [The "BSD licence"] Copyright (c) 2012 Dandelion All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of Dandelion
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission. THIS
 * SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.github.dandelion.datatables.core.html.HtmlCaption;

/**
 * <p>
 * Tag used to generate a HTML table's caption.
 * 
 * @author Gautier Dhordain
 */
public class CaptionTag extends BodyTagSupport{

	private static final long serialVersionUID = 273025310498552490L;
	private String id;
	private String cssClass;
	private String cssStyle;
	private String title;

	@Override
	public int doStartTag() throws JspException{
		return EVAL_BODY_BUFFERED;
	}
	
	@Override
	public int doEndTag() throws JspException {
		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		HtmlCaption caption = new HtmlCaption();
		caption.setId(id);
		caption.addCssClass(cssClass);
		caption.addCssStyle(cssStyle);
		caption.setTitle(title);
		caption.addContent(getBodyContent().getString());
		
		parent.getTable().setCaption(caption);
		// TODO : gerer le body du tag
		// TODO : inclure les attributs statndard HTML  id, class, style
		return EVAL_PAGE;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getCssClass(){
		return cssClass;
	}

	public void setCssClass(String cssClass){
		this.cssClass = cssClass;
	}

	public String getCssStyle(){
		return cssStyle;
	}

	public void setCssStyle(String cssStyle){
		this.cssStyle = cssStyle;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}
}