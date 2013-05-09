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

package com.github.dandelion.datatables.jsp.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.jsp.tag.DomBaseTest;
import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.utils.TableTagBuilder;

/**
 * Default table.
 * 
 * @author Thibault Duchateau
 */
public class DefaultTableTest extends DomBaseTest {

	@Override
	public void buildTable() {
		tableBuilder = new TableTagBuilder(Mock.persons, "myTableId").context(mockPageContext)
				.defaultTable();
	}
	
	@Test
	public void should_fill_the_table() {
		assertThat(table.getHeadRows()).hasSize(1);
		assertThat(table.getBodyRows()).hasSize(Mock.persons.size());
		assertThat(table.getBodyRows().get(0).getColumns()).hasSize(tableBuilder.getColumnTags().size());
		assertThat(table.getBodyRows().get(0).getColumns().get(0).getContent().toString()).isEqualTo(Mock.persons.get(0).getId().toString());
		assertThat(table.getBodyRows().get(0).getColumns().get(1).getContent().toString()).isEqualTo(Mock.persons.get(0).getFirstName().toString());
	}

	@Test
	public void should_generate_a_default_table() {
		assertThat(table.getFeatures()).isNull();
		assertThat(table.getPlugins()).isNull();
		assertThat(table.getTheme()).isNull();
		//TODO more assertions are needed
	}
}