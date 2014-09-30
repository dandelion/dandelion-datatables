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
package com.github.dandelion.datatables.core.processor.column;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.EnumUtils;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.column.FilterTypeProcessor;
import com.github.dandelion.datatables.core.processor.ColumnProcessorBaseTest;
import com.github.dandelion.datatables.core.processor.MapEntry;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterTypeProcessorTest extends ColumnProcessorBaseTest {

	@Override
	public OptionProcessor getProcessor() {
		return new FilterTypeProcessor();
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void should_return_null_when_value_is_empty() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FILTERTYPE, "");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration);
		processor.process(pc);

		assertThat(entry.getValue()).isNull();
	}

	@Test
	public void should_map_to_an_enum_when_using_an_existing_value() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FILTERTYPE, "select ");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration);
		processor.process(pc);

		assertThat(entry.getValue()).isEqualTo(FilterType.SELECT);
	}

	@Test
	public void should_throw_an_exception_wiwth_message() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FILTERTYPE, "seleeeeect");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, columnConfiguration);

		exception.expect(DandelionException.class);
		exception.expectMessage("\"seleeeeect\" is not a valid filter type. Possible values are: "
				+ EnumUtils.printPossibleValuesOf(FilterType.class));

		processor.process(pc);
	}
}