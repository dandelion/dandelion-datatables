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
package com.github.dandelion.datatables.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.ConfigurationStore;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public abstract class ColumnProcessorBaseTest {

	protected ColumnProcessor processor;
	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected HttpServletRequest request;
	protected Map<ConfigToken<?>, Object> confToBeApplied;
	protected Entry<ConfigToken<?>, Object> entry;
	
	@Before
	public void setup() {
		processor = getProcessor();
		MockServletContext mockServletContext = new MockServletContext();
		MockPageContext mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		confToBeApplied = new HashMap<ConfigToken<?>, Object>();
		tableConfiguration = new TableConfiguration(confToBeApplied, request);
		tableConfiguration.setTableId("fakeId");
		columnConfiguration = new ColumnConfiguration();
	}
	
	@After
	public void after(){
		ConfigurationStore.clear();
	}
	
	public abstract ColumnProcessor getProcessor();
}