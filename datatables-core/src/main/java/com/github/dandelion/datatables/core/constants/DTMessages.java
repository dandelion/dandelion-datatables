/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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
package com.github.dandelion.datatables.core.constants;

import com.github.dandelion.datatables.core.configuration.TableConfig;

/**
 * 
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public enum DTMessages {

	PROCESSING("sProcessing", TableConfig.P_I18N_MSG_PROCESSING),
	SEARC("sSearch", TableConfig.P_I18N_MSG_SEARCH),
	LENGTHMENU("sLengthMenu", TableConfig.P_I18N_MSG_LENGTHMENU),
	INFO("sInfo", TableConfig.P_I18N_MSG_INFO),
	INFOEMPTY("sInfoEmpty", TableConfig.P_I18N_MSG_INFOEMPTY),
	INFOFILTERED("sInfoFiltered", TableConfig.P_I18N_MSG_INFOFILTERED),
	INFOPOSTFIX("sInfoPostFix", TableConfig.P_I18N_MSG_INFOPOSTFIX),
	LOADINGRECORDS("sLoadingRecords", TableConfig.P_I18N_MSG_LOADINGRECORDS),
	ZERORECORDS("sZeroRecords", TableConfig.P_I18N_MSG_ZERORECORDS),
	EMPTYTABLE("sEmptyTable", TableConfig.P_I18N_MSG_EMPTYTABLE),
	PAGINATE("oPaginate", ""),
	PAGINATE_FIRST("sFirst", TableConfig.P_I18N_MSG_PAGINATE_FIRST),
	PAGINATE_PREVIOUS("sPrevious", TableConfig.P_I18N_MSG_PAGINATE_PREVIOUS),
	PAGINATE_NEXT("sNext", TableConfig.P_I18N_MSG_PAGINATE_NEXT),
	PAGINATE_LAST("sLast", TableConfig.P_I18N_MSG_PAGINATE_LAST),
	ARIA("oAria", ""),
	ARIA_SORT_ASC("sSortAscending", TableConfig.P_I18N_MSG_ARIA_SORTASC),
	ARIA_SORT_DESC("sSortDescending", TableConfig.P_I18N_MSG_ARIA_SORTDESC);
	
	private String realName;
	
	private String propertyName;
	
	private DTMessages(String realName, String propertyName){
		this.realName = realName;
		this.propertyName = propertyName;
	}

	public String getRealName() {
		return realName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}