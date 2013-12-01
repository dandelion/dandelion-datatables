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
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.asset.ExtraFile;
import com.github.dandelion.datatables.core.asset.InsertMode;
import com.github.dandelion.datatables.core.extension.feature.ExtraFileFeature;

/**
 * Tag used to add an extra Javascript configuration file.
 *
 * @author Thibault Duchateau
 */
public class ExtraFileTag extends TagSupport {
	private static final long serialVersionUID = -287813095911386884L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ExtraFileTag.class);
		
	// Tag attributes
	private String src;
	private String insert;
	
	/**
	 * TODO
	 */
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}
	
	/**
	 * TODO
	 */
	public int doEndTag() throws JspException {
		
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);
		
		if(parent.isFirstIteration()){
			InsertMode mode = null;
			if(StringUtils.isNotBlank(this.insert)){
				try {
					mode = InsertMode.valueOf(this.insert.trim().toUpperCase());
				} catch (IllegalArgumentException e) {
					logger.error("{} is not a valid value among {}. Please choose a valid one.",
							insert, InsertMode.values());
					throw new JspException(e);
				}
			}
			else{
				mode = InsertMode.BEFOREALL;
			}
			parent.getTable().getTableConfiguration().addExtraFile(new ExtraFile(getRealSource(this.src), mode));
			parent.getTable().getTableConfiguration().registerExtension(new ExtraFileFeature());
		}
		return EVAL_PAGE;
	}
	
	/**
	 * TODO
	 * @param tmpSource
	 * @return
	 */
	private String getRealSource(String tmpSource){
		return pageContext.getServletContext().getRealPath(tmpSource);
	}
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getInsert() {
		return insert;
	}

	public void setInsert(String insert) {
		this.insert = insert;
	}
}
