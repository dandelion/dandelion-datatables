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
package com.github.dandelion.datatables.integration.issue;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.integration.ThymeleafBaseIT;

/**
 * See https://github.com/dandelion/issues/issues/199.
 * 
 * @author Thibault Duchateau
 */
public class NativeTableIT extends ThymeleafBaseIT {

	@Test
	public void should_display_native_table_only() {
		goToPage("issues/native-table-only");
		
		assertThat(find("table")).hasSize(1);
		assertThat(find("div.dataTables_wrapper")).hasSize(0);
	}
	
	@Test
	public void should_display_native_tables_alongside_dataTable() {
		goToPage("issues/native-tables-with-dataTable");
		
		assertThat(find("table")).hasSize(4);
		assertThat(find("div.dataTables_wrapper")).hasSize(1);
		
		assertThat(getTable()).hasSize(1);
		assertThat(getTable().find("thead")).hasSize(1);
		assertThat(getTable().find("tbody")).hasSize(1);
		
		// By default, paging is set to 10
		assertThat(getTable().find("tbody").find("tr")).hasSize(10);
		
		String js = getConfigurationFromPage("issues/native-tables-with-dataTable");
		assertThat(js).contains("var oTable_myTableId = $('#myTableId');");
		assertThat(js).doesNotContain("var oTable_myTableId2 = $('#myTableId2');");
	}
}
