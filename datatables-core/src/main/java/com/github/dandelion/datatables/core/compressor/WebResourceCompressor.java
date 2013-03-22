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
package com.github.dandelion.datatables.core.compressor;

import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Compressor for web resources (Javascript, Stylesheets).
 * 
 * @author Thibault Duchateau
 */
public interface WebResourceCompressor {

	/**
	 * Return as String a compressed version of the given Javascript code.
	 * 
	 * @param table
	 *            The table containing, among others, the compression options.
	 * @param input
	 *            The Javascript code to compress.
	 * @return The Javascript code compressed.
	 * @throws CompressionException
	 *             if the String containing Javascript code is malformed or
	 *             cannot be evaluated.
	 */
	public String getCompressedJavascript(HtmlTable table, String input)
			throws CompressionException;

	/**
	 * Return as String a compressed version of the given CSS code.
	 * 
	 * @param input
	 *            The CSS code to compress.
	 * @return The CSS code compressed.
	 * @throws CompressionException
	 *             if the String containing CSS is malformed.
	 */
	public String getCompressedCss(String input) throws CompressionException;
}