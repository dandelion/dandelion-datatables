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

package com.github.dandelion.datatables.integration.export;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dandelion.datatables.integration.ThymeleafContextRunner;
import com.github.dandelion.datatables.testing.export.ExportLinksBaseIT;
import com.github.dandelion.datatables.testing.utils.ThymeleafTest;

/**
 * Test the export link generation.
 *
 * @author Thibault Duchateau
 */
@RunWith(ThymeleafContextRunner.class)
@ThymeleafTest
public class ExportLinksIT extends ExportLinksBaseIT {

	@Test
	public void should_generate_default_csv_link_with_one_existing_url_parameter() throws Exception {
		goToPage("export/default_csv_link?param1=val1");

		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href"))
			.isEqualTo(getDefaultBaseUrl() + "/thymeleaf/export/default_csv_link?param1=val1&dtt=1&dti=myTableId");
	}
	
	@Test
	public void should_generate_default_csv_link_with_multiple_existing_url_parameters() throws Exception {
		goToPage("export/default_csv_link?param1=val1&param2=val2");

		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getText()).isEqualTo("CSV");
		assertThat(find("div.dandelion_dataTables_export").findFirst("a").getAttribute("href"))
			.isEqualTo(getDefaultBaseUrl() + "/thymeleaf/export/default_csv_link?param1=val1&param2=val2&dtt=1&dti=myTableId");
	}
}
