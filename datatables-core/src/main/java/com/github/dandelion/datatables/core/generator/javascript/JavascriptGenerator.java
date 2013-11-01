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
package com.github.dandelion.datatables.core.generator.javascript;

import com.github.dandelion.core.asset.wrapper.impl.DelegatedContent;
import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.configuration.DatatablesConfigurator;

/**
 * <p>
 * Interface for all Javascript generators.
 * 
 * <p>
 * The default implementation is the {@link StandardJavascriptGenerator} class
 * but it can be replaced by another class that implements this interface thanks
 * to the {@link DatatablesConfigurator}.
 * 
 * <p>
 * As it extends the {@link DelegatedContent} interface, the implementation must
 * also implement the
 * {@link DelegatedContent#getContent(javax.servlet.http.HttpServletRequest)}
 * method, which should return the generated Javascript.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public interface JavascriptGenerator extends DelegatedContent {

	public static final String INDENTATION = "   ";
	public static final String NEWLINE = "\n";

	/**
	 * Transfer the data from the {@link JsResource} to the different String
	 * builders (buffer).
	 * 
	 * @param jsResource
	 *            The web resource to use to generate Javascript content.
	 */
	public void addResource(JsResource jsResource);
}
