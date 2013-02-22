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

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.model.DisplayType;
import com.github.dandelion.datatables.core.model.HtmlColumn;

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
		return EVAL_PAGE;
	}

	/**
	 * TODO
	 */
	public int doEndTag() throws JspException {
		TableTag parent = (TableTag) getParent();

		// DOM source
		if ("DOM".equals(parent.getLoadingType())) {

			// The column has a body
			if (getBodyContent() != null) {
				String bodyString = getBodyContent().getString().trim().replaceAll("[\n\r]", "");
				this.addColumn(false, bodyString);
			}
			// The column doens't have a body but a property is set
			else{
				this.addColumn(false, getColumnContent());	
			}
			
			// At the first iteration, the header row must filled in too
			if (parent.isFirstIteration()) {
				this.addColumn(true, this.title);
			}

			return EVAL_PAGE;
		} 
		// AJAX source
		else if ("AJAX".equals(parent.getLoadingType())) {

			HtmlColumn column = new HtmlColumn(true, this.title);

			// All display types are added
			for (DisplayType type : DisplayType.values()) {
				column.getEnabledDisplayTypes().add(type);
			}
			column.setProperty(this.property);

			column.setDefaultValue(StringUtils.isNotBlank(this.defaultValue) ? this.defaultValue : "");
			
			if (this.sortable != null) {
				column.setSortable(this.sortable);
			}

			parent.getTable().getLastHeaderRow().addColumn(column);
			parent.getTable().getLastFooterRow().addColumn(new HtmlColumn());
			return EVAL_PAGE;
		}

		return SKIP_PAGE;
	}
}