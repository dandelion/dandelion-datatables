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
import org.springframework.util.StringUtils;

import com.github.dandelion.datatables.integration.ThymeleafBaseIT;

/**
 * Test the possible export link positions.
 *
 * @author Thibault Duchateau
 */
public class ExportLinksPositionIT extends ThymeleafBaseIT {

	@Test
	public void should_generate_bottom_right_link() {
		goToPage("export/bottom_right_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
		
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:right;");
	}
	
	@Test
	public void should_generate_bottom_middle_link() {
		goToPage("export/bottom_middle_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_paginate");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dandelion_dataTables_export");
		
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_bottom_left_link() {
		goToPage("export/bottom_left_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-right:10px;");		
	}
	
	@Test
	public void should_generate_top_right_link() {
		goToPage("export/top_right_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:right");
	}
	
	@Test
	public void should_generate_top_middle_link() {
		goToPage("export/top_middle_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_top_middle_link_when_using_scrollY() {
		goToPage("export/top_middle_link_with_scrollY");

		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_scroll");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_scrollHead");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 5).getAttribute("class")).contains("dataTables_scrollHeadInner");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 6).getAttribute("class")).contains("dataTables_scrollBody");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 7).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 8).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-left:10px;");
	}
	
	@Test
	public void should_generate_top_left_link() {
		goToPage("export/top_left_link");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_paginate");
				
		// Div style
		assertThat(StringUtils.trimAllWhitespace(find("div.dandelion_dataTables_export").getAttribute("style"))).contains("float:left;margin-right:10px;");
	}
	
	@Test
	public void should_generate_top_and_bottom_right_links() {
		goToPage("export/top_and_bottom_right_links");
		
		// Div position inside the Datatables' wrapper
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 0).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 1).getAttribute("class")).contains("dataTables_length");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 2).getAttribute("class")).contains("dataTables_filter");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 3).getAttribute("class")).contains("dandelion_dataTables_export");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 4).getAttribute("class")).contains("dataTables_info");
		assertThat(find("#" + TABLE_ID + "_wrapper").find("div", 5).getAttribute("class")).contains("dataTables_paginate");
	}
}
