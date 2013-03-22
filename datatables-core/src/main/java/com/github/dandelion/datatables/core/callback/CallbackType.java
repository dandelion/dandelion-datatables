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
package com.github.dandelion.datatables.core.callback;

import com.github.dandelion.datatables.core.constants.DTConstants;

/**
 * Enum containing the different type of callback. 
 *
 * @author Thibault Duchateau
 */
public enum CallbackType {

	COOKIE(DTConstants.DT_FN_COOKIE_CBK),
	
	CREATEDROW(DTConstants.DT_FN_CREATED_ROW),
	
	DRAW(DTConstants.DT_FN_DRAW_CBK),
	
	FOOTER(DTConstants.DT_FN_FOOTER_CBK),
	
	FORMAT(DTConstants.DT_FN_FORMAT_NUMBER),
	
	HEADER(DTConstants.DT_FN_HEADER_CBK),
	
	INFO(DTConstants.DT_FN_INFO_CBK),
	
	INIT(DTConstants.DT_FN_INIT_COMPLETE),
	
	PREDRAW(DTConstants.DT_FN_PRE_DRAW_CBK),
	
	ROW(DTConstants.DT_FN_ROW_CBK);
	
	private String function;
	
	private CallbackType(String function){
		this.function = function;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
}
