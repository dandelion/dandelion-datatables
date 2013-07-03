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
package com.github.dandelion.datatables.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;

/**
 * Helper class used to extract content from different type of input.
 * 
 * @author Thibault Duchateau
 */
public class ResourceHelper {
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	/**
	 * TODO
	 * @param pathToFile
	 * @return
	 * @throws FileNotFoundException 
	 * @throws BadConfigurationException 
	 */
	public static InputStream getFileFromWebapp(String pathToFile) throws FileNotFoundException {
		File file = new File(pathToFile);
		InputStream inputStream = new FileInputStream(file);
		return inputStream;
	}
	
	/**
	 * TODO
	 * @param pathToFile
	 * @return
	 */
	public static InputStream getFileFromClasspath(String pathToFile){
		
//		logger.debug("pathToFile : {}", pathToFile);

		return Thread.currentThread().getContextClassLoader().getResourceAsStream(pathToFile);
	}
	
	/**
	 * TODO
	 * @param pathToFile
	 * @return
	 * @throws BadConfigurationException 
	 * @throws IOException
	 */
	public static String getFileContentFromClasspath(String pathToFile) throws IOException {
		return toString(getFileFromClasspath(pathToFile));
	}
	
	
	/**
	 * TODO
	 * @param pathToFile
	 * @return
	 * @throws IOException 
	 * @throws BadConfigurationException 
	 */
	public static String getFileContentFromWebapp(String pathToFile) throws IOException {
		return toString(getFileFromWebapp(pathToFile));
	}
	
	
	/**
	 * TODO
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();

		InputStreamReader in = new InputStreamReader(input);

		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = in.read(buffer))) {
			sw.write(buffer, 0, n);
		}

		return sw.toString();
	}
	
	
	/**
	 * TODO
	 * @return
	 */
	public static String getRamdomNumber(){
		return StringUtils.randomNumeric(5);
	}
}