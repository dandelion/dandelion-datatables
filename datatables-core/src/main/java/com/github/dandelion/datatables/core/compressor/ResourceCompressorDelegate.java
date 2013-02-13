/*
 * [The "BSD licence"]
 * Copyright (c) 2012 DataTables4j
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
 * 3. Neither the name of DataTables4j nor the names of its contributors 
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

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.exception.CompressionException;
import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.core.util.ReflectHelper;

/**
 * Helper class in charge of the instanciation of the compressor implementation
 * defined in the datatables4j configuration file.
 * 
 * @author Thibault Duchateau
 */
public class ResourceCompressorDelegate {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ResourceCompressorDelegate.class);

	private HtmlTable table;
	private String compressorClassName;

	/**
	 * Private constructor which retrieve the compressor class from properties.
	 */
	public ResourceCompressorDelegate(HtmlTable table) {

		this.table = table;
		this.compressorClassName = table.getTableProperties().getCompressorClassName();

		logger.debug("ResourceCompressor loaded. About to use {} implementation",
				this.compressorClassName);
	}

	/**
	 * Compress the javascript input using the compressorClassName and return
	 * it.
	 * 
	 * @param input
	 *            The stringified javascript to compress.
	 * @return The compressed stringified javascript.
	 * @throws BadConfigurationException
	 *             if the compressorClassName is not present in the classPath.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	public String getCompressedJavascript(String input) throws BadConfigurationException,
			CompressionException {

		Class<?> compressorClass = ReflectHelper.getClass(this.compressorClassName);

		logger.debug("Instancing the compressor class {}", compressorClass);
		Object obj = ReflectHelper.getNewInstance(compressorClass);

		logger.debug("Invoking method getCompressedJavascript");
		try {
			return (String) ReflectHelper.invokeMethod(obj, "getCompressedJavascript",
					new Object[] { table, input });
		} catch (BadConfigurationException e) {
			if (e.getCause() instanceof InvocationTargetException) {
				throw (CompressionException) ((InvocationTargetException) e.getCause())
						.getTargetException();
			} else {
				throw e;
			}
		}
	}

	/**
	 * Compress the CSS input using the compressorClassName and return it.
	 * 
	 * @param input
	 *            The stringified CSS to compress.
	 * @return The compressed stringified CSS.
	 * @throws BadConfigurationException
	 *             if the compressorClassName is not present in the classPath.
	 * @throws CompressionException
	 *             if a error append during the compression.
	 */
	public String getCompressedCss(String input) throws BadConfigurationException,
			CompressionException {

		Class<?> compressorClass = ReflectHelper.getClass(this.compressorClassName);

		Object obj = ReflectHelper.getNewInstance(compressorClass);

		try {
			return (String) ReflectHelper.invokeMethod(obj, "getCompressedCss",
					new Object[] { input });
		} catch (BadConfigurationException e) {
			if (e.getCause() instanceof InvocationTargetException) {
				throw (CompressionException) ((InvocationTargetException) e.getCause())
						.getTargetException();
			} else {
				throw e;
			}
		}
	}
}