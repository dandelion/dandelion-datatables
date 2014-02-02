/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
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

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.html.HtmlColumn;

/**
 * <p>
 * JSP tag used to add a specific content inside a column header.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" row="person">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 *    &lt;%-- Displays a master checkbox in the column header --%>
 *    &lt;datatables:columnHead uid="actionColumn">
 *       &lt;input type="checkbox" onclick="$('#myTableId').find(':checkbox').attr('checked', this.checked);" />
 *    &lt;/datatables:columnHead>
 *    &lt;datatables:column uid="actionColumn" sortable="false" cssCellStyle="text-align:center;">
 *       &lt;input type="checkbox" value="${person.id}" />
 *    &lt;/datatables:column>
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.8.1
 */
public class ColumnHeadTag extends BodyTagSupport {

	private static final long serialVersionUID = -8928415196287387948L;

	/**
	 * Tag attributes
	 */
	private String uid;

	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
		if(parent != null){
			return EVAL_BODY_BUFFERED;
		}
		
		throw new JspException("The tag 'columnHead' must be inside the 'table' tag.");
	}

	/**
	 * {@inheritDoc}
	 */
	public int doAfterBody() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * {@inheritDoc}
	 */
	public int doEndTag() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		if (StringUtils.isNotBlank(this.uid)) {
			HtmlColumn column = parent.getTable().getColumnHeadByUid(this.uid);
			if (column != null) {
				column.setContent(new StringBuilder(getBodyContent().getString()));
			}
		} else {
			throw new JspException("The attribute 'uid' is required. Please read the documentation.");
		}

		return EVAL_PAGE;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
}