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

import com.github.dandelion.datatables.core.plugin.ScrollerPlugin;
import com.github.dandelion.datatables.core.processor.Processor;
import com.github.dandelion.datatables.core.processor.ProcessorBaseTest;

public class PluginScrollerProcessorTest extends ProcessorBaseTest {

	@Override
	public Processor getProcessor() {
		return new PluginScrollerProcessor();
	}

	@Test
	public void should_set_null_when_value_is_null() throws Exception {
		processor.process(null, tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getPluginScroller()).isNull();
	}
	
	@Test
	public void should_set_null_when_value_is_empty() throws Exception {
		processor.process("", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getPluginScroller()).isNull();
	}
	
	@Test
	public void should_enable_plugin_when_value_is_true() throws Exception {
		processor.process("true", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getPluginScroller()).isTrue();
		assertThat(tableConfiguration.getExtraPlugins()).contains(new ScrollerPlugin());
	}
	
	@Test
	public void should_not_enable_plugin_when_value_is_false() throws Exception {
		processor.process("false", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getPluginScroller()).isFalse();
		assertThat(tableConfiguration.getExtraPlugins()).isNull();
		
		processor.process("weird value", tableConfiguration, confToBeApplied);
		assertThat(tableConfiguration.getPluginScroller()).isFalse();
		assertThat(tableConfiguration.getExtraPlugins()).isNull();
	}
}