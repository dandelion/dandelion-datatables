/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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
package com.github.dandelion.datatables.core.properties;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.Test;

import com.github.dandelion.datatables.core.constants.ConfConstants;
import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Test everything about Datatables properties.
 *
 * @author Thibault Duchateau
 */
public class PropertiesLoaderTest {

	private HtmlTable table = new HtmlTable("dummyDomId", "dummyId");
	private TableProperties tableProperties = new TableProperties();
	
	@Test
	public void should_load_properties() throws BadConfigurationException {
		PropertiesLoader.load(table);
		assertThat(table.getTableProperties().getAggregatorMode()).isNotNull();
	}
	
	@Test
	public void should_not_validate_any_wrong_property() throws BadConfigurationException {
		assertThat(tableProperties.isValidProperty("wrongProperty")).isFalse();
	}
	
	@Test
	public void should_validate_right_properties() throws IllegalArgumentException, IllegalAccessException, BadConfigurationException {

		Field[] confConstants = ConfConstants.class.getDeclaredFields();

		for (Field constant : confConstants) {
			constant.setAccessible(true);
			assertThat(tableProperties.isValidProperty(String.valueOf(constant.get(ConfConstants.class)))).isTrue();
		}
	}
}
