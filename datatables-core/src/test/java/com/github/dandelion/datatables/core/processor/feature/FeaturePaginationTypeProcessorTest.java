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

import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeBootstrapFeature;
import com.github.dandelion.datatables.core.extension.feature.PaginationTypeInputFeature;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;
import com.github.dandelion.datatables.core.option.processor.OptionProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeaturePaginationTypeProcessor;
import com.github.dandelion.datatables.core.processor.MapEntry;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class FeaturePaginationTypeProcessorTest extends TableProcessorBaseTest {

	@Override
	public OptionProcessor getProcessor() {
		return new FeaturePaginationTypeProcessor();
	}

	@Test
	public void should_set_paginationtype() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_PAGINATIONTYPE, "bootstrap");
		OptionProcessingContext pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo(PaginationType.BOOTSTRAP);
		assertThat(tableConfiguration.getInternalExtensions()).contains(new PaginationTypeBootstrapFeature());

		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_PAGINATIONTYPE, "BOOTSTRAP");
		pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo(PaginationType.BOOTSTRAP);
		assertThat(tableConfiguration.getInternalExtensions()).contains(new PaginationTypeBootstrapFeature());

		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_PAGINATIONTYPE, "INPUT");
		pc = new OptionProcessingContext(entry, tableConfiguration, null);
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo(PaginationType.INPUT);
		assertThat(tableConfiguration.getInternalExtensions()).contains(new PaginationTypeInputFeature());
	}

	@Test
	public void should_set_a_custom_paginationType_ans_no_extension() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.FEATURE_PAGINATIONTYPE, "custom-pagination-type");
		assertThat(entry.getValue()).isEqualTo("custom-pagination-type");
		assertThat(tableConfiguration.getInternalExtensions()).isNull();
	}
}