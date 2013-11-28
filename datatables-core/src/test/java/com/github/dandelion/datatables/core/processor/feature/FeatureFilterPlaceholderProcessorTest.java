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

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.processor.TableProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

public class FeatureFilterPlaceholderProcessorTest extends TableProcessorBaseTest {

	@Override
	public TableProcessor getProcessor() {
		return new FeatureFilterPlaceholderProcessor();
	}

	@Test
	public void should_set_null_when_value_is_null() {
		processor.processConfiguration(null, tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() {
		processor.processConfiguration("", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isNull();
	}
	
	@Test
	public void should_set_filterplaceholder() {
		processor.processConfiguration("none", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isEqualTo(FilterPlaceholder.NONE);
		processor.processConfiguration("NONE", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isEqualTo(FilterPlaceholder.NONE);
		processor.processConfiguration("head_after", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isEqualTo(FilterPlaceholder.HEAD_AFTER);
		processor.processConfiguration("head_before", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isEqualTo(FilterPlaceholder.HEAD_BEFORE);
		processor.processConfiguration("foot", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getFeatureFilterPlaceholder()).isEqualTo(FilterPlaceholder.FOOT);
	}
	
	@Test(expected = ConfigurationProcessingException.class)
	public void should_raise_an_exception() {
		processor.processConfiguration("noooone", tableConfiguration, confToBeApplied);
	}
}
