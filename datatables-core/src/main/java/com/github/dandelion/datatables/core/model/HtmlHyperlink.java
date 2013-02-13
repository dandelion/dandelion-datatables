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
 * Plain old HTML <code>a</code> tag (link).
 * 
 * @author Thibault Duchateau
 * @since 0.7.0
 */
public class HtmlHyperlink extends HtmlTag {

	/**
	 * Plain old HTML <code>href</code> attribute.
	 */
	private String href;
	
	/**
	 * Link's label.
	 */
	private String label;
	
	public HtmlHyperlink(){
	}
	
	public HtmlHyperlink(String href, String label){
		this.href = href;
		this.label = label;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringBuffer toHtml(){
		StringBuffer html = new StringBuffer();
		html.append("<a");
		
		if(this.id != null){
			html.append(" id=\"");
			html.append(this.id);
			html.append("\"");			
		}
		
		if(this.cssClass != null){
			html.append(" class=\"");
			html.append(this.cssClass);
			html.append("\"");			
		}
		
		if(this.cssStyle != null){
			html.append(" style=\"");
			html.append(this.cssStyle);
			html.append("\"");			
		}
		
		if(this.href != null){
			html.append(" href=\"");
			html.append(this.href);
			html.append("\"");
		}
		
		html.append(">");
		html.append(this.label);
		html.append("</a>");
		
		return html;
	}
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}