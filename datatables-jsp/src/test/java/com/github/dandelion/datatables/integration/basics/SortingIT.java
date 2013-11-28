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

package com.github.dandelion.datatables.integration.basics;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.integration.JspBaseIT;

/**
 * Test the sorting options.
 *
 * @author Thibault Duchateau
 */
public class SortingIT extends JspBaseIT {

	@Test
	public void should_disable_sorting_only_on_the_first_column_using_dom() {
		goToPage("basics/sorting/sorting_disabled_dom", true);

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_disabled");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 2).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 3).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 4).getAttribute("class")).isEqualTo("sorting");
	}
	
	@Test
	public void should_init_sort_using_dom() {
		goToPage("basics/sorting/sorting_init_dom");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_control_direction_using_dom() {
		goToPage("basics/sorting/sorting_direction_dom");
		
		click(getTable().find("thead").find("th", 0));
		click(getTable().find("thead").find("th", 0));
		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		
		click(getTable().find("thead").find("th", 1));
		click(getTable().find("thead").find("th", 1));
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_disable_sorting_only_on_the_first_column_using_ajax() {
		goToPage("basics/sorting/sorting_disabled_ajax");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_disabled");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 2).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 3).getAttribute("class")).isEqualTo("sorting");
		assertThat(getTable().find("thead").find("th", 4).getAttribute("class")).isEqualTo("sorting");
	}
	
	@Test
	public void should_init_sort_using_ajax() {
		goToPage("basics/sorting/sorting_init_ajax");

		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
	
	@Test
	public void should_control_direction_using_ajax() {
		goToPage("basics/sorting/sorting_direction_ajax");
		
		click(getTable().find("thead").find("th", 0));
		click(getTable().find("thead").find("th", 0));
		assertThat(getTable().find("thead").find("th", 0).getAttribute("class")).isEqualTo("sorting_desc");
		
		click(getTable().find("thead").find("th", 1));
		click(getTable().find("thead").find("th", 1));
		assertThat(getTable().find("thead").find("th", 1).getAttribute("class")).isEqualTo("sorting_asc");
	}
}