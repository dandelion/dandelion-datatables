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
package com.github.dandelion.datatables.extras.compression;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.compressor.WebResourceCompressor;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * Default compression class which uses YUICompressor.
 * 
 * @author Thibault Duchateau
 */
public class YuiResourceCompressor implements WebResourceCompressor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(YuiResourceCompressor.class);

	/**
	 * {@inheritDoc}
	 */
	public String getCompressedJavascript(HtmlTable table, String input)
			throws CompressionException {

		Writer output = new StringWriter();
		JavaScriptCompressor compressor = null;
		TableConfiguration props = table.getTableConfiguration();

		try {
			// Instanciate the compressor
			compressor = new JavaScriptCompressor(new StringReader(input),
					new YuiCompressorErrorReporter());

			// Compress code
			compressor.compress(output, -1, props.getMainCompressorMunge(), false,
					props.getMainCompressorPreserveSemiColons(), props.getCompressorDisableOpti());
		} catch (EvaluatorException e) {
			logger.error("Unable to compress Javascript resource");
			throw new CompressionException(e);
		} catch (IOException e) {
			logger.error("Unable to compress Javascript resource");
			throw new CompressionException(e);
		}

		return output.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCompressedCss(String input) throws CompressionException {

		Writer output = new StringWriter();
		CssCompressor compressor;

		try {
			// Instanciate the compressor
			compressor = new CssCompressor(new StringReader(input));

			// Compress code
			compressor.compress(output, -1);
		} catch (IOException e) {
			logger.error("Unable to compress Css resource");
			throw new CompressionException(e);
		}

		return output.toString();
	}
}