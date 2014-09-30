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
import javax.servlet.jsp.tagext.TagSupport;

import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;

/**
 * <p>
 * JSP tag used to overload locally a configuration property.
 *
 * <p>
 * Note that this tag will be processed only once, at the first iteration.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 *    &lt;datatables:prop name="main.extension.package" value="com.foo.bar" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 */
public class PropTag extends TagSupport {
	
	private static final long serialVersionUID = -3453884184847355817L;

	/**
	 * Tag attributes
	 */
	// Name of the configuration property
	private String name;
	
	// Value of the configuration property
	private String value;
	
	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {

		TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
		if(parent != null){
			return SKIP_BODY;
		}

		throw new JspException("The tag 'prop' must be inside the 'table' tag.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int doEndTag() throws JspException {
		
		AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

		// The tag is evaluated only once, at the first iteration
		if(parent.isFirstIteration()){
			
			Option<?> option = DatatableOptions.findByName(name);
			
			if(option == null){
				throw new JspException("'" + name + "' is not a valid property. Please read the documentation.");
			}
			else{
				parent.stagingConf.put(option, value);
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