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

package com.github.dandelion.datatables.integration.advanced;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.integration.ThymeleafBaseIT;

/**
 * Test the callbacks feature.
 *
 * @author Thibault Duchateau
 */
public class CallbacksIT extends ThymeleafBaseIT {

	@Test
	public void should_generate_initcomplete_callback() {
		goToPage("advanced/callbacks/callback_init");
		String js = getConfigurationFromPage("advanced/callbacks/callback_init");
		assertThat(js).contains(DTConstants.DT_FN_INIT_COMPLETE);
	}
	
	@Test
	public void should_avoid_conflict_between_feature_and_callback() {
		goToPage("advanced/callbacks/callback_init_conflict_with_feature");
		String js = getConfigurationFromPage("advanced/callbacks/callback_init_conflict_with_feature");
		assertThat(js).contains("function(oSettings,json) {\n      oTable_myTableId.fnAdjustColumnSizing(true);\n      callback(oSettings,json);\n   }");
	}
	
	@Test
	public void should_avoid_conflict_between_multiple_callback_of_the_same_type() {
		goToPage("advanced/callbacks/callback_init_conflict_with_other_callback");
		String js = getConfigurationFromPage("advanced/callbacks/callback_init_conflict_with_other_callback");
		assertThat(js).contains("function(oSettings,json) {\n      oTable_myTableId.fnAdjustColumnSizing(true);\n      callback1(oSettings,json);\n      callback2(oSettings,json);\n   }");
	}
	
	@Test
	public void should_generate_cookie_callback() {
		goToPage("advanced/callbacks/callback_cookie");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_cookie")).contains(DTConstants.DT_FN_COOKIE_CBK);
	}
	
	@Test
	public void should_generate_createdrow_callback() {
		goToPage("advanced/callbacks/callback_createdrow");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_createdrow")).contains(DTConstants.DT_FN_CREATED_ROW);
	}
	
	@Test
	public void should_generate_draw_callback() {
		goToPage("advanced/callbacks/callback_draw");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_draw")).contains(DTConstants.DT_FN_DRAW_CBK);
	}
	
	@Test
	public void should_generate_footer_callback() {
		goToPage("advanced/callbacks/callback_footer");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_footer")).contains(DTConstants.DT_FN_FOOTER_CBK);
	}
	
	@Test
	public void should_generate_format_callback() {
		goToPage("advanced/callbacks/callback_format");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_format")).contains(DTConstants.DT_FN_FORMAT_NUMBER);
	}
	
	@Test
	public void should_generate_header_callback() {
		goToPage("advanced/callbacks/callback_header");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_header")).contains(DTConstants.DT_FN_HEADER_CBK);
	}
	
	@Test
	public void should_generate_info_callback() {
		goToPage("advanced/callbacks/callback_info");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_info")).contains(DTConstants.DT_FN_INFO_CBK);
	}
	
	@Test
	public void should_generate_predraw_callback() {
		goToPage("advanced/callbacks/callback_predraw");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_predraw")).contains(DTConstants.DT_FN_PRE_DRAW_CBK);
	}
	
	@Test
	public void should_generate_row_callback() {
		goToPage("advanced/callbacks/callback_row");
		assertThat(getConfigurationFromPage("advanced/callbacks/callback_row")).contains(DTConstants.DT_FN_ROW_CBK);
	}
}
