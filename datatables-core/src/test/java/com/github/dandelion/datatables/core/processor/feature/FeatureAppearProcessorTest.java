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
package com.github.dandelion.datatables.core.processor.feature;

import org.junit.Test;

import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeatureAppearProcessor;
import com.github.dandelion.datatables.core.processor.MapEntry;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureAppearProcessorTest extends TableProcessorBaseTest {

	@Override
	public OptionProcessor getProcessor() {
		return new FeatureAppearProcessor();
	}
	
	@Test
	public void should_return_fadein() throws Exception{
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_APPEAR, "fadein");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("fadein");
	}
	
	@Test
	public void should_return_fadein_and_set_appear_duration() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_APPEAR, "fadein,1500");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("fadein");
		assertThat(tableConfiguration.getStagingConfiguration().get(DatatableOptions.FEATURE_APPEAR_DURATION)).isEqualTo("1500");
	}
	
	@Test
	public void should_set_default_value_when_a_wrong_format_is_used() throws Exception{
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_APPEAR, "blockkk");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("block");

		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_APPEAR, "fadein;12");
		pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("block");
	}
}