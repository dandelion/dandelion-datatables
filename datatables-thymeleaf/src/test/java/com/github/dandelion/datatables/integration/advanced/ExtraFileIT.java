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
package com.github.dandelion.datatables.integration.advanced;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.integration.ThymeleafBaseIT;

public class ExtraFileIT extends ThymeleafBaseIT {
	
	@Test
	public void should_insert_the_extrafile_beforeall_by_default() {
		goToPage("advanced/extrafile/default_insert_mode", true);
		String js = getConfigurationFromPage("advanced/extrafile/default_insert_mode");
		assertThat(js).contains("/* extraFile1 */\nvar oTable_myTableId = $('#myTableId');");
	}
	
	@Test
	public void should_insert_the_extrafile_afterAll() {
		goToPage("advanced/extrafile/afterall", true);
		String js = getConfigurationFromPage("advanced/extrafile/afterall");
		assertThat(js).contains("$(document).ready(function(){\n   oTable_myTableId.dataTable(oTable_myTableId_params);\n});\n/* extraFile1 */");
	}
	
	@Test
	public void should_insert_the_extrafile_afterStartDocumentReady() {
		goToPage("advanced/extrafile/afterstartdocumentready", true);
		String js = getConfigurationFromPage("advanced/extrafile/afterstartdocumentready");
		assertThat(js).contains("$(document).ready(function(){\n/* extraFile1 */\n   oTable_myTableId.dataTable(oTable_myTableId_params);\n});");
	}
	
	@Test
	public void should_insert_the_extrafile_beforeEndDocumentReady() {
		goToPage("advanced/extrafile/beforeenddocumentready", true);
		String js = getConfigurationFromPage("advanced/extrafile/beforeenddocumentready");
		assertThat(js).contains("$(document).ready(function(){\n   oTable_myTableId.dataTable(oTable_myTableId_params);\n   /* extraFile1 */\n});");
	}
	
	@Test
	public void should_insert_the_extrafile_beforeStartDocumentReady() {
		goToPage("advanced/extrafile/beforestartdocumentready", true);
		String js = getConfigurationFromPage("advanced/extrafile/beforestartdocumentready");
		assertThat(js).contains("/* extraFile1 */\n$(document).ready(function(){\n   oTable_myTableId.dataTable(oTable_myTableId_params);\n});");
	}
}