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

import java.util.HashMap;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;

/**
 * <p>
 * Tag used to generate a HTML table's column.
 * 
 * @author Thibault Duchateau
 * @since 0.1.0
 */
public class ColumnTag extends AbstractColumnTag {

	private static final long serialVersionUID = -8928415196287387948L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ColumnTag.class);
		
	public ColumnTag(){
		stagingConf = new HashMap<Configuration, Object>();
	}
	
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

		// A header column must be added at first iteration, regardless the data
		// source type (DOM or AJAX)
		if(parent.isFirstIteration()){

			// The 'title' attribute has precedence over 'titleKey'
			String columnTitle = title;
			
			// If the 'titleKey' attribute is used, the column's title must be
			// retrieved from the current ResourceBundle
			if(columnTitle == null && titleKey != null){
				if(parent.getTable().getTableConfiguration().getInternalMessageResolver() != null){
					columnTitle = parent.getTable().getTableConfiguration().getInternalMessageResolver()
							.getResource(titleKey, property, this, pageContext);
				}
				else{
					logger.warn(
							"You cannot use the 'titleKey' attribute if no message resolver is configured. Please take a look at the {} property in the configuration reference.",
							Configuration.INTERNAL_MESSAGE_RESOLVER.getName());
				}
			}
			
			if ("DOM".equals(parent.getLoadingType())) {
				addDomHeadColumn(columnTitle);
			}
			else if ("AJAX".equals(parent.getLoadingType())) {
				addAjaxColumn(true, columnTitle);
				return EVAL_PAGE;
			}
		}

		// At this point, only DOM sources are concerned 
		if(parent.getCurrentObject() != null){

			String columnContent = null;
			// The 'property' attribute has precedence over the body of the
			// column tag
			if (getBodyContent() == null) {
				columnContent = getColumnContent();
			}
			// No 'property' attribute is used but a body is set instead
			else{
				columnContent = getBodyContent().getString().trim().replaceAll("[\n\r]", "");
			}
			addDomBodyColumn(columnContent);
		}

		return EVAL_PAGE;
	}
}