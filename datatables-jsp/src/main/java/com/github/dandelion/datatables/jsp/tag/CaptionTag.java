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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.html.HtmlCaption;

/**
 * <p>
 * JSP tag used to generate a HTML {@code caption} tag.
 * 
 * <p>
 * Note that this tag supports dynamic attributes with only string values. See
 * {@link #setDynamicAttribute(String, String, Object)} below.
 * 
 * @author Gautier Dhordain
 * @author Thibault Duchateau
 */
public class CaptionTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 273025310498552490L;
	
	/**
	 * Tag attributes
	 */
	private String id;
	private String cssClass;
	private String cssStyle;
	private String title;
	private boolean escapeXml = true; // Whether XML characters should be escaped
		
	/**
	 * Internal attributes
	 */
	protected Map<String, String> dynamicAttributes;
	
	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException{

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
		if(parent != null){
			return EVAL_BODY_BUFFERED;
		}
		
		throw new JspException("The tag 'caption' must be inside the 'table' tag.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int doEndTag() throws JspException {
		
		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		// The tag is evaluated only once, at the first iteration
		if (parent.isFirstIteration()) {
			
			HtmlCaption caption = new HtmlCaption();
			caption.setId(id);
			caption.addCssClass(cssClass);
			caption.addCssStyle(cssStyle);
			caption.setTitle(StringUtils.escape(this.escapeXml, this.title));
			caption.addContent(getBodyContent().getString());
			
			parent.getTable().setCaption(caption);
		}

		return EVAL_PAGE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {

		validateDynamicAttribute(localName, value);

		if (this.dynamicAttributes == null) {
			this.dynamicAttributes = new HashMap<String, String>();
		}

		dynamicAttributes.put(localName, (String) value);
	}
	
	/**
	 * <p>
	 * Validates the passed dynamic attribute.
	 * 
	 * <p>
	 * The dynamic attribute must not conflict with other attributes and must
	 * have a valid type.
	 * 
	 * @param localName
	 *            Name of the dynamic attribute.
	 * @param value
	 *            Value of the dynamic attribute.
	 */
	private void validateDynamicAttribute(String localName, Object value) {
		if (localName.equals("class")) {
			throw new IllegalArgumentException(
					"The 'class' attribute is not allowed. Please use the 'cssClass' attribute instead.");
		}
		if (localName.equals("style")) {
			throw new IllegalArgumentException(
					"The 'style' attribute is not allowed. Please use the 'cssStyle' attribute instead.");
		}
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("The attribute " + localName
					+ " won't be added to the table. Only string values are accepted.");
		}
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEscapeXml(boolean escapeXml) {
		this.escapeXml = escapeXml;
	}
}