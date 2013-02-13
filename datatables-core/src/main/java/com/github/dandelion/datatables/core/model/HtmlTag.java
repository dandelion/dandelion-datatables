/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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
package com.github.dandelion.datatables.core.model;

/**
 * Abstract superclass for all HTML tags.
 * 
 * @author Thibault Duchateau
 */
public abstract class HtmlTag {

	/**
	 * Plain old HTML <code>id</code> attribute.
	 */
	protected String id;
	
	/**
	 * Plain old HTML <code>class</code> attribute.
	 */
	protected StringBuffer cssClass;

	/**
	 * Plain old HTML <code>style</code> attribute.
	 */
	protected StringBuffer cssStyle;

	/**
	 * Render the tag in HTML code.
	 * 
	 * @return the HTML code corresponding to the tag.
	 */
	public abstract StringBuffer toHtml();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public StringBuffer getCssClass() {
		return cssClass;
	}

	public void setCssClass(StringBuffer cssClass) {
		this.cssClass = cssClass;
	}

	public StringBuffer getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(StringBuffer cssStyle) {
		this.cssStyle = cssStyle;
	}

	public void addCssClass(String cssClass) {
		if (this.cssClass == null) {
			this.cssClass = new StringBuffer();
		}
		this.cssClass.append(cssClass);
	}

	public void addCssStyle(String cssStyle) {
		if (this.cssStyle == null) {
			this.cssStyle = new StringBuffer();
		}
		this.cssStyle.append(cssStyle);
	}
}
