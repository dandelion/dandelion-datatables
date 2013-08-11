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

import com.github.dandelion.datatables.core.configuration.Configuration;

/**
 * Tag used to locally override the Dandelion global configuration.
 *
 * @author Thibault Duchateau
 */
public class PropTag extends TagSupport {
	private static final long serialVersionUID = -3453884184847355817L;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(PropTag.class);
		
	// Tag attributes
	private String name;
	private String value;
	
	/**
	 * A PropTag has no body but we test here that the PropTag is in the right place.
	 */
	public int doStartTag() throws JspException {

		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);	    

		// There isn't an ancestor of given class
		if (parent == null) {
			throw new JspException("PropTag must be inside AbstractTableTag");
		}

		return SKIP_BODY;
	}
	
	/**
	 * Process the tag updating table properties.
	 */
	public int doEndTag() throws JspException {
		
		// Get parent tag
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		// Evaluate the tag only once using the isFirstIteration method
		if(parent.isFirstIteration()){
			
			Configuration configuration = Configuration.findByName(name);
			
			if(configuration == null){
				logger.warn("{} is not a valid property.", name);
				throw new JspException(name + " is not a valid property");
			}
			else{
				parent.stagingConf.put(configuration, value);
			}
		}
		
		return EVAL_PAGE;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
}