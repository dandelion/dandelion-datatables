/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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
package com.github.dandelion.datatables.core.html;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract superclass for all HTML tags.
 * 
 * @author Thibault Duchateau
 */
public abstract class HtmlTag {

	public static final char CLASS_SEPARATOR = ' ';
	public static final char CSS_SEPARATOR = ';';
	
	/**
	 * Tag label.
	 */
	protected String tag;

	/**
	 * Plain old HTML <code>id</code> attribute.
	 */
	protected String id;

	/**
	 * Plain old HTML <code>class</code> attribute.
	 */
	protected StringBuilder cssClass;

	/**
	 * Plain old HTML <code>style</code> attribute.
	 */
	protected StringBuilder cssStyle;

	/**
	 * Dynamic native HTML attributes.
	 */
	protected Map<String, String> dynamicAttributes;

        /**
	 * Render the tag in HTML code.
	 * 
	 * @return the HTML code corresponding to the tag.
	 */
	public StringBuilder toHtml() {
		StringBuilder html = new StringBuilder();
		html.append(getHtmlOpeningTag());
		html.append(getHtmlClosingTag());
		return html;
	}

	protected StringBuilder getHtmlOpeningTag() {
		StringBuilder html = new StringBuilder();
		html.append('<');
		html.append(this.tag);
		html.append(getHtmlAttributes());
		html.append(getDynamicHtmlAttributes());
		html.append('>');
		return html;
	}

	protected StringBuilder getHtmlAttributes() {
		StringBuilder html = new StringBuilder();
		html.append(writeAttribute("id", this.id));
		html.append(writeAttribute("class", this.cssClass));
		html.append(writeAttribute("style", this.cssStyle));
		return html;
	}

	protected StringBuilder getDynamicHtmlAttributes() {

		// If no dynamicAttributes set, return empty StringBuilder
		if(dynamicAttributes == null) {
			return new StringBuilder();
		}
		StringBuilder html = new StringBuilder();
		for(Map.Entry<String, String> attribute : dynamicAttributes.entrySet()) {
			html.append(writeAttribute(attribute.getKey(), attribute.getValue()));
		}
		return html;
	}
	
	protected static StringBuilder writeAttribute(String name, Object data) {
		StringBuilder html = new StringBuilder();
		if(data != null) {
			html.append(' ');
			html.append(name);
			html.append("=\"");
			html.append(data.toString());
			html.append('"');
		}
		return html;
	}

	protected StringBuilder getHtmlClosingTag() {
		StringBuilder html = new StringBuilder();
		html.append("</");
		html.append(this.tag);
		html.append('>');
		return html;
	}

	public String getTag() {
		return tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StringBuilder getCssClass() {
		return cssClass;
	}

	public void setCssClass(StringBuilder cssClass) {
		this.cssClass = cssClass;
	}

	public StringBuilder getCssStyle() {
		return cssStyle;
	}

	public void setCssStyle(StringBuilder cssStyle) {
		this.cssStyle = cssStyle;
	}

	public Map<String, String> getDynamicAttributes() {
		return dynamicAttributes;
	}

	public void setDynamicAttributes(Map<String, String> dynamicAttributes) {
		this.dynamicAttributes = dynamicAttributes;
	}

	public void addDynamicAttribute(String name, String value){
		if(dynamicAttributes == null){
			dynamicAttributes = new HashMap<String, String>();
		}
		dynamicAttributes.put(name, value);
	}
	
	public void addCssClass(String cssClass) {
		if(this.cssClass == null) {
			this.cssClass = new StringBuilder();
		} else {
			this.cssClass.append(CLASS_SEPARATOR);
		}
		this.cssClass.append(cssClass);
	}

	public void addCssStyle(String cssStyle) {
		if(this.cssStyle == null) {
			this.cssStyle = new StringBuilder();
		} else {
			this.cssStyle.append(CSS_SEPARATOR);
		}
		this.cssStyle.append(cssStyle);
	}
}