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

import java.util.ArrayList;
import java.util.List;

import com.github.dandelion.core.web.handler.debug.DebugMenu;
import com.github.dandelion.core.web.handler.debug.DebugPage;

/**
 * <p>
 * Debugging menu for the Dandelion-Datatables component.
 * </p>
 * <p>
 * This menu is scanned and automatically inserted in the debugger.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public class DatatableDebugMenu implements DebugMenu {

	@Override
	public String getDisplayName() {
		return "Dandelion Datatables";
	}

	@Override
	public List<DebugPage> getPages() {
		List<DebugPage> debugPages = new ArrayList<DebugPage>();
		debugPages.add(new DatatableOptionsDebugPage());
		debugPages.add(new DatatableOptionGroupsDebugPage()); 
//		debugPages.add(new DatatableDebuggerDebugPage());
		return debugPages;
	}
}
