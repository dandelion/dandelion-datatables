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
package com.github.dandelion.datatables.web.handler.debug;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.github.dandelion.core.utils.ResourceUtils;
import com.github.dandelion.core.web.handler.RequestHandlerContext;
import com.github.dandelion.core.web.handler.debug.AbstractDebugPage;

/**
 * <p>
 * TODO
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class DatatableDebuggerDebugPage extends AbstractDebugPage {

	private static final String PAGE_ID = "datatable-debugger";
	private static final String PAGE_NAME = "Debugger";
	private static final String PAGE_LOCATION = "META-INF/resources/ddl-dt-debugger/html/datatable-debugger.html";

	@Override
	public String getId() {
		return PAGE_ID;
	}

	@Override
	public String getName() {
		return PAGE_NAME;
	}

	@Override
	public String getTemplate(RequestHandlerContext context) throws IOException {
		return ResourceUtils.getContentFromInputStream(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PAGE_LOCATION));
	}

	@Override
	protected Map<String, String> getCustomParameters(RequestHandlerContext context) {
		// TODO Auto-generated method stub
		return Collections.emptyMap();
	}
}
