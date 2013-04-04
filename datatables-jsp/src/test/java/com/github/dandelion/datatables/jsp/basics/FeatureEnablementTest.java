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

import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.jsp.tag.DomBaseTest;
import com.github.dandelion.datatables.utils.Mock;
import com.github.dandelion.datatables.utils.TableBuilder;

public class FeatureEnablementTest extends DomBaseTest {

	@Override
	public void buildTable() {
		tableBuilder = new TableBuilder(Mock.persons, "myTableId").context(mockPageContext)
				.defaultTable()
				.info(false)
				.paginate(true)
				.lengthChange(false)
				.sort(false)
				.filter(true);
	}

	@Test
	public void should_disable_info(){
		assertThat(table.getInfo()).isEqualTo(false);
		assertThat(mainConf.containsKey(DTConstants.DT_INFO)).isTrue();
		assertThat(mainConf.get(DTConstants.DT_INFO)).isEqualTo(false);
	}
	
	@Test
	public void should_disable_lengthChange(){
		assertThat(table.getLengthChange()).isEqualTo(false);
		assertThat(mainConf.containsKey(DTConstants.DT_LENGTH_CHANGE)).isTrue();
		assertThat(mainConf.get(DTConstants.DT_LENGTH_CHANGE)).isEqualTo(false);
	}
	
	@Test
	public void should_disable_sort(){
		assertThat(table.getSort()).isEqualTo(false);
		assertThat(mainConf.containsKey(DTConstants.DT_SORT)).isTrue();
		assertThat(mainConf.get(DTConstants.DT_SORT)).isEqualTo(false);
	}
	
	@Test
	public void should_enable_filter(){
		assertThat(table.getFilterable()).isEqualTo(true);
		assertThat(mainConf.containsKey(DTConstants.DT_FILTER)).isTrue();
		assertThat(mainConf.get(DTConstants.DT_FILTER)).isEqualTo(true);
	}
	
	@Test
	public void should_enable_paginate(){
		assertThat(table.getPaginate()).isEqualTo(true);
		assertThat(mainConf.containsKey(DTConstants.DT_PAGINATE)).isTrue();
		assertThat(mainConf.get(DTConstants.DT_PAGINATE)).isEqualTo(true);
	}
}
