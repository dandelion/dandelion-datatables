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
package com.github.dandelion.datatables.core.generator;

import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * 
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public enum DTMessages {

	PROCESSING("sProcessing", DatatableOptions.I18N_MSG_PROCESSING.getName()),
	SEARC("sSearch", DatatableOptions.I18N_MSG_SEARCH.getName()),
	LENGTHMENU("sLengthMenu", DatatableOptions.I18N_MSG_LENGTHMENU.getName()),
	INFO("sInfo", DatatableOptions.I18N_MSG_INFO.getName()),
	INFOEMPTY("sInfoEmpty", DatatableOptions.I18N_MSG_INFOEMPTY.getName()),
	INFOFILTERED("sInfoFiltered", DatatableOptions.I18N_MSG_INFOFILTERED.getName()),
	INFOPOSTFIX("sInfoPostFix", DatatableOptions.I18N_MSG_INFOPOSTFIX.getName()),
	LOADINGRECORDS("sLoadingRecords", DatatableOptions.I18N_MSG_LOADINGRECORDS.getName()),
	ZERORECORDS("sZeroRecords", DatatableOptions.I18N_MSG_ZERORECORDS.getName()),
	EMPTYTABLE("sEmptyTable", DatatableOptions.I18N_MSG_EMPTYTABLE.getName()),
	PAGINATE("oPaginate", ""),
	PAGINATE_FIRST("sFirst", DatatableOptions.I18N_MSG_PAGINATE_FIRST.getName()),
	PAGINATE_PREVIOUS("sPrevious", DatatableOptions.I18N_MSG_PAGINATE_PREVIOUS.getName()),
	PAGINATE_NEXT("sNext", DatatableOptions.I18N_MSG_PAGINATE_NEXT.getName()),
	PAGINATE_LAST("sLast", DatatableOptions.I18N_MSG_PAGINATE_LAST.getName()),
	ARIA("oAria", ""),
	ARIA_SORT_ASC("sSortAscending", DatatableOptions.I18N_MSG_ARIA_SORTASC.getName()),
	ARIA_SORT_DESC("sSortDescending", DatatableOptions.I18N_MSG_ARIA_SORTDESC.getName());
	
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