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

import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * JSP tag used to add a specific content inside a column header.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" row="person"&gt;
 *    &lt;datatables:column title="Id" property="id" /&gt;
 *    &lt;datatables:column title="FirstName" property="firstName" /&gt;
 *    &lt;datatables:column title="LastName" property="lastName" /&gt;
 *    &lt;datatables:column title="City" property="address.town.name" /&gt;
 *    &lt;datatables:column title="Mail" property="mail" /&gt;
 *    &lt;datatables:column sortable="false" cssCellStyle="text-align:center;"&gt;
 *       &lt;datatables:columnHead&gt;
 *          &lt;input type="checkbox" onclick="$('#myTableId').find(':checkbox').attr('checked', this.checked);" /&gt;
 *       &lt;/datatables:columnHead&gt;
 *       &lt;input type="checkbox" value="${person.id}" /&gt;
 *    &lt;/datatables:column&gt;
 * &lt;/datatables:table&gt;
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.8.1
 */
public class ColumnHeadTag extends BodyTagSupport {

	private static final long serialVersionUID = -8928415196287387948L;

	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {

		ColumnTag parent = (ColumnTag) findAncestorWithClass(this, ColumnTag.class);
		if (parent != null) {
			return EVAL_BODY_BUFFERED;
		}

		throw new JspException("The 'columnHead' tag must be inside the 'column' tag.");
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

		TableTag tableTag = (TableTag) findAncestorWithClass(this, TableTag.class);

		// The tag is evaluated only once, at the first iteration
		if (tableTag.isFirstIteration()) {

			ColumnTag parentColumnTag = (ColumnTag) findAncestorWithClass(this, ColumnTag.class);

			// The content of the header column is overriden with the content of
			// the columnHeader tag
			parentColumnTag.getHeaderColumn().setContent(new StringBuilder(getBodyContent().getString()));
		}

		return EVAL_PAGE;
	}

	public void setCssStyle(String cssStyle) {
		ColumnTag parent = (ColumnTag) findAncestorWithClass(this, ColumnTag.class);
		parent.getStagingConf().put(DatatableOptions.CSSSTYLE, cssStyle);
	}

	public void setCssClass(String cssClass) {
		ColumnTag parent = (ColumnTag) findAncestorWithClass(this, ColumnTag.class);
		parent.getStagingConf().put(DatatableOptions.CSSCLASS, cssClass);
	}
}