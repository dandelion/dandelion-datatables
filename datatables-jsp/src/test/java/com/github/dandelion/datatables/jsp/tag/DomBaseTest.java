/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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

import java.util.Map;

import javax.servlet.jsp.JspException;

import org.junit.Before;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.generator.MainGenerator;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.utils.TableTagBuilder;

/**
 * Base class used for unit-testing the library using DOM-based source.
 *
 * @author Thibault Duchateau
 */
public abstract class DomBaseTest {

	protected MockPageContext mockPageContext;
	protected TableTag tableTag;
	protected TableTagBuilder tableTagBuilder;
	protected HtmlTable table;
	protected Map<String, Object> mainConf;
	
	@Before
	public void setup() {
		// mock ServletContext
		MockServletContext mockServletContext = new MockServletContext();

		// mock PageContext
		mockPageContext = new MockPageContext(mockServletContext);

		buildTable();
		
		try {
			tableTagBuilder.getTableTag().doStartTag();
			for (int i = 0; i < Mock.persons.size(); i++) {
				for (ColumnTag columnTag : tableTagBuilder.getColumnTags()) {
					columnTag.doStartTag();
					columnTag.doEndTag();
				}
				tableTagBuilder.getTableTag().doAfterBody();
			}
			tableTagBuilder.getTableTag().doEndTag();
			table = tableTagBuilder.getTableTag().getTable();
			
			MainGenerator configGenerator = new MainGenerator();
			mainConf = configGenerator.generateConfig(table);
			
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO
	 */
	public abstract void buildTable();
}
