/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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

package com.github.dandelion.datatables.jsp.basics;

import org.junit.Test;

import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.jsp.tag.DomBaseTest;
import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.utils.TableTagBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Styling table.
 * 
 * @author Thibault Duchateau
 */
public class StylingTableTest extends DomBaseTest {

	@Override
	public void buildTable() {
		tableTagBuilder = new TableTagBuilder(Mock.persons, "myTableId").context(mockPageContext)
				.defaultTable()
				.cssClass("myClass")
				.cssStripes("myStrip1,myStrip2")
				.cssStyle("myStyle");
	}
	
	@Test
	public void should_set_css_style_using_class() {
		assertThat(DatatableOptions.CSS_CLASS.valueFrom(table.getTableConfiguration()).toString()).isEqualTo("myClass");
	}
	
	@Test
	public void should_set_strip_classes() {
		assertThat(DatatableOptions.CSS_STRIPECLASSES.valueFrom(table.getTableConfiguration())).isEqualTo("['myStrip1','myStrip2']");
	}
	
	@Test
	public void should_set_css_style() {
		assertThat(DatatableOptions.CSS_STYLE.valueFrom(table.getTableConfiguration()).toString()).isEqualTo("myStyle");
	}
}