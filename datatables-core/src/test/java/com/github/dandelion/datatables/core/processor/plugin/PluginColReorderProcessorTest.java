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
package com.github.dandelion.datatables.core.processor.plugin;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.plugin.ColReorderPlugin;
import com.github.dandelion.datatables.core.processor.TableProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

public class PluginColReorderProcessorTest extends TableProcessorBaseTest {

	@Override
	public TableProcessor getProcessor() {
		return new PluginColReorderProcessor();
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.process(TableConfig.PLUGIN_COLREORDER, null, tableConfiguration);
		assertThat(TableConfig.PLUGIN_COLREORDER.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.process(TableConfig.PLUGIN_COLREORDER, "", tableConfiguration);
		assertThat(TableConfig.PLUGIN_COLREORDER.valueFrom(tableConfiguration)).isNull();
	}
	
	@Test
	public void should_enable_plugin_when_value_is_true() throws Exception {
		processor.process(TableConfig.PLUGIN_COLREORDER, "true", tableConfiguration);
		assertThat(TableConfig.PLUGIN_COLREORDER.valueFrom(tableConfiguration)).isTrue();
		assertThat(tableConfiguration.getInternalExtensions()).contains(new ColReorderPlugin());
	}
	
	@Test
	public void should_not_enable_plugin_when_value_is_false() throws Exception {
		processor.process(TableConfig.PLUGIN_COLREORDER, "false", tableConfiguration);
		assertThat(TableConfig.PLUGIN_COLREORDER.valueFrom(tableConfiguration)).isFalse();
		assertThat(tableConfiguration.getInternalExtensions()).isNull();
		
		processor.process(TableConfig.PLUGIN_COLREORDER, "weird value", tableConfiguration);
		assertThat(TableConfig.PLUGIN_COLREORDER.valueFrom(tableConfiguration)).isFalse();
		assertThat(tableConfiguration.getInternalExtensions()).isNull();
	}
}