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
package com.github.dandelion.datatables.core.asset;

import com.github.dandelion.datatables.core.util.StringUtils;


/**
 * <p>
 * Bean used to hold a Javascript function that must not be converted into
 * String by the JSON parser.
 * <p>
 * Overriding the toString() method allow the parser to return the Javascript
 * content as an Object, resulting in a non-quoted String in JSON format.
 * 
 * @author Thibault Duchateau
 * @since 0.8.11
 */
public class JavascriptFunction {

	private String code;
	private boolean hasReturn;
	private String[] args;
	
	public JavascriptFunction(String code){
		this.code = code;
		this.hasReturn = false;
		this.args = null;
	}
	
	public JavascriptFunction(String code, boolean hasReturn){
		this.code = code;
		this.hasReturn = hasReturn;
		this.args = null;
	}
	
	public JavascriptFunction(String code, String... args) {
		this.code = code;
		this.args = args;
	}
	
	public JavascriptFunction(String code, boolean hasReturn, String... args) {
		this.code = code;
		this.hasReturn = hasReturn;
		this.args = args;
	}

	@Override
	public String toString() {
		return "function(" + (args != null ? StringUtils.join(args, ",") : "") + "){" + (hasReturn ? "return " : "") + code + "}";
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setCode(String code){
		this.code = code;
	}

	public void appendCode(String code){
		this.code += code;
	}
	
	@Override
	public boolean equals(Object javascriptFunction) {
		if(javascriptFunction != null && javascriptFunction instanceof JavascriptFunction) {
			JavascriptFunction castedJavascriptFunction = (JavascriptFunction)javascriptFunction;
			return toString().equals(castedJavascriptFunction.toString());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}