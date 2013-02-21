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
package com.github.dandelion.datatables.core.constants;

/**
 * DataTables internal constants.
 * 
 * @author Thibault Duchateau
 */
public class DTConstants {

	public static final String DT_DOM = "sDom";
	public static final String DT_AUTO_WIDTH = "bAutoWidth"; 
	public static final String DT_DEFER_RENDER = "bDeferRender";
	public static final String DT_FILTER = "bFilter";
	public static final String DT_INFO = "bInfo";
	public static final String DT_SORT = "bSort";
	public static final String DT_PAGINATE = "bPaginate";
	public static final String DT_LENGTH_CHANGE = "bLengthChange";
	public static final String DT_STATE_SAVE = "bStateSave";
	public static final String DT_S_DEFAULT_CONTENT = "sDefaultContent";

	// AJAX related constants
	public static final String DT_B_DEFER_RENDER = "bDeferRender";
	public static final String DT_B_PROCESSING = "bProcessing";
	public static final String DT_B_SERVER_SIDE = "bServerSide";
	public static final String DT_S_AJAX_SOURCE = "sAjaxSource";
	public static final String DT_FN_SERVERDATA = "fnServerData";
	public static final String DT_S_AJAXDATAPROP = "sAjaxDataProp";
	public static final String DT_FN_INIT_COMPLETE = "fnInitComplete";
		 
	// AJAX URL parameters
	public static final String DT_S_ECHO = "sEcho";
	public static final String DT_I_COLUMNS = "iColumns";
	public static final String DT_S_COLUMNS = "sColumns";
	public static final String DT_I_DISPLAY_START = "iDisplayStart";
	public static final String DT_I_DISPLAY_LENGTH = "iDisplayLength";
	public static final String DT_M_DATA_PROP= "mDataProp_";
	public static final String DT_S_SEARCH= "sSearch";
	public static final String DT_B_REGEX= "bRegex";
	public static final String DT_B_SEARCHABLE= "bSearchable_";
	public static final String DT_B_SORTABLE= "bSortable_";
	public static final String DT_I_SORT_COL= "iSortCol_";
	public static final String DT_S_SORT_DIR= "sSortDir_";
	public static final String DT_I_SORTING_COLS = "iSortingCols";
	public static final String DT_B_REGEX_COL = "bRegex_";
	
	// Advanced configuration
	public static final String DT_PAGINATION_TYPE = "sPaginationType";
	public static final String DT_DS_DATA = "aaData";
	public static final String DT_AOCOLUMNS = "aoColumns";
	public static final String DT_LANGUAGE = "oLanguage";
	public static final String DT_URL = "sUrl";
	public static final String DT_JQUERYUI = "bJQueryUI";
	
	// Extra features
	public static final String DT_SCROLLY = "sScrollY";
	public static final String DT_OFFSETTOP = "offsetTop";
	public static final String DT_FILTER_TYPE = "type";
	
	// Column configuration
	public static final String DT_SORTABLE = "bSortable";
	public static final String DT_SORT_INIT = "aaSorting";
	public static final String DT_SORT_DIR = "asSorting";
	public static final String DT_DATA = "mData";
}