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
package com.github.dandelion.datatables.core.processor.column;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.extension.feature.SortType;
import com.github.dandelion.datatables.core.processor.ColumnProcessorBaseTest;
import com.github.dandelion.datatables.core.processor.ConfigurationProcessor;
import com.github.dandelion.datatables.core.processor.MapEntry;

public class SortTypeProcessorTest extends ColumnProcessorBaseTest {

	@Override
	public ConfigurationProcessor getProcessor() {
		return new SortTypeProcessor();
	}

	@Test
	public void should_leave_the_value_untouched_when_empty() {
		entry = new MapEntry<ConfigToken<?>, Object>(ColumnConfig.SORTTYPE, "");
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("");
	}

	@Test
	public void should_map_to_an_enum_when_using_an_existing_value() {
		entry = new MapEntry<ConfigToken<?>, Object>(ColumnConfig.SORTTYPE, "natural");
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo(SortType.NATURAL.getName());
	}
	
	@Test
	public void should_be_able_to_use_a_custom_sort_type() {
		entry = new MapEntry<ConfigToken<?>, Object>(ColumnConfig.SORTTYPE, "my-sorttype");
		processor.process(entry, columnConfiguration, tableConfiguration);
		assertThat(entry.getValue()).isEqualTo("my-sorttype");
	}
}
