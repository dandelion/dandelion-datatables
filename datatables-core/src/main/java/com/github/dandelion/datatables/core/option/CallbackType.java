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
package com.github.dandelion.datatables.core.option;

import com.github.dandelion.datatables.core.generator.DTConstants;

/**
 * Enum containing the different type of callback and their respective input
 * parameter(s).
 *
 * @author Thibault Duchateau
 * @since 0.8.9
 */
public enum CallbackType {

	CREATEDROW(DTConstants.DT_FN_CREATED_ROW, false, "nRow", "aData", "iDataIndex"),

	DRAW(DTConstants.DT_FN_DRAW_CBK, false, "oSettings"),

	FOOTER(DTConstants.DT_FN_FOOTER_CBK, false, "nFoot", "aData", "iStart", "iEnd", "aiDisplay"),

	FORMAT(DTConstants.DT_FN_FORMAT_NUMBER, true, "iIn"),

	HEADER(DTConstants.DT_FN_HEADER_CBK, false, "nHead", "aData", "iStart", "iEnd", "aiDisplay"),

	INFO(DTConstants.DT_FN_INFO_CBK, true, "oSettings", "iStart", "iEnd", "iMax", "iTotal", "sPre"),

	INIT(DTConstants.DT_FN_INIT_COMPLETE, false, "oSettings", "json"),

	PREDRAW(DTConstants.DT_FN_PRE_DRAW_CBK, true, "oSettings"),

	ROW(DTConstants.DT_FN_ROW_CBK, false, "nRow", "aData", "iDisplayIndex", "iDisplayIndexFull"),

   STATESAVE(DTConstants.DT_FN_STATESAVE_CBK, false, "oSettings", "oData"),

   STATESAVEPARAMS(DTConstants.DT_FN_STATESAVE_PARAMS_CBK, true, "oSettings"),

   STATELOAD(DTConstants.DT_FN_STATELOAD_CBK, false, "oSettings", "oData"),

   STATELOADPARAMS(DTConstants.DT_FN_STATELOAD_PARAMS_CBK, true, "oSettings"),

   STATELOADED(DTConstants.DT_FN_STATELOADED_CBK, false, "oSettings", "oData");

	private String name;
	private boolean hasReturn;
	private String[] args;

	private CallbackType(String function, boolean hasReturn, String... args){
		this.name = function;
		this.setHasReturn(hasReturn);
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public void setFunction(String function) {
		this.name = function;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public boolean hasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}
}