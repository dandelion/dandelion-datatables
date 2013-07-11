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

import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.util.StringUtils;

/**
 * <p>
 * Tag used to generate a HTML table's column.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class ColumnTag extends AbstractColumnTag {

	private static final long serialVersionUID = -8928415196287387948L;

	/**
	 * Nothing happens in doStartTag.
	 */
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * <p>
	 * Configure the current {@link HtmlColumn}.
	 * <p>
	 * Note that when using an AJAX source, since there is only one iteration,
	 * it just adds a header column to the last header {@link HtmlRow} added.
	 * When using a DOM source, first a header {@link HtmlColumn} is added at
	 * first iteration and a {@link HtmlColumn} is added for each iteration.
	 */
	public int doEndTag() throws JspException {
		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

		// A header column must be added at first iteration
		if(parent.isFirstIteration()){

			// The 'title' attribute has precedence over 'titleKey'
			String columnTitle = title;
			
			// If the 'titleKey' attribute is used, the column's title must be
			// retrieved from the current ResourceBundle
			if(columnTitle == null && StringUtils.isNotBlank(titleKey)){
				columnTitle = parent.getTable().getTableConfiguration().getInternalMessageResolver()
						.getResource(titleKey, title, this, pageContext);
			}
			
			if ("DOM".equals(parent.getLoadingType())) {
				addDomColumn(true, columnTitle);
			}
			else if ("AJAX".equals(parent.getLoadingType())) {
				addAjaxColumn(true, columnTitle);
				return EVAL_PAGE;
			}
		}

		// At this point, only DOM sources are concerned 
		if(parent.getCurrentObject() != null){
			
			// The 'property' attribute has precedence over the body of the
			// column tag
			if (getBodyContent() == null) {
				addDomColumn(false, getColumnContent());	
			}
			// No 'property' attribute is used but a body is set instead
			else{
				String bodyString = getBodyContent().getString().trim().replaceAll("[\n\r]", "");
				addDomColumn(false, bodyString);
			}
		}

		return EVAL_PAGE;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String titleKey) {
		this.title = titleKey;
	}
	
	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}
}