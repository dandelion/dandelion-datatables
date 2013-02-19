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
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.model.HtmlColumn;

/**
 * <p>
 * Tag used to custom the header of a table's column.
 * 
 * @author Thibault Duchateau
 * @since 0.8.1
 */
public class ColumnHeadTag extends BodyTagSupport {

	private static final long serialVersionUID = -8928415196287387948L;

	private String uid;

	public int doStartTag() throws JspException {

		// Never reached
		return EVAL_BODY_BUFFERED;
	}

	public int doAfterBody() throws JspException {
		return EVAL_PAGE;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int doEndTag() throws JspException {

		TableTag parent = (TableTag) getParent();

		if (StringUtils.isNotBlank(this.uid)) {
			HtmlColumn column = parent.getTable().getColumnHeadByUid(this.uid);

			if (column != null) {
				// Recuperer la colonne et mettre a jour le contenu avec le
				// corps
				column.setContent(getBodyContent().getString());
			} else {
				// Ajouter la colonne
			}
		} else {
			throw new JspException(
					"The attribute 'uid' is required. Please read the documentation.");
		}

		return EVAL_PAGE;
	}
}