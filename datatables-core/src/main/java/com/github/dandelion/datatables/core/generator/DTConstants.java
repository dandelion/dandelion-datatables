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

/**
 * DataTables parameters.
 * 
 * @author Thibault Duchateau
 */
public final class DTConstants {

	public static final String DT_DOM = "dom";
	public static final String DT_AUTO_WIDTH = "autoWidth";
	public static final String DT_FILTER = "searching";
	public static final String DT_INFO = "info";
	public static final String DT_SORT = "ordering";
	public static final String DT_PAGING = "paging";
	public static final String DT_PAGINGTYPE = "pagingType";
	public static final String DT_LENGTH_CHANGE = "lengthChange";
	public static final String DT_STATE_SAVE = "bStateSave";
	public static final String DT_A_LENGTH_MENU = "lengthMenu";
	public static final String DT_AS_STRIPE_CLASSES = "stripeClasses";
	public static final String DT_S_CLASS = "className";

	// AJAX related constants
	public static final String DT_DEFER_RENDER = "deferRender";
	public static final String DT_B_PROCESSING = "processing";
	public static final String DT_B_SERVER_SIDE = "serverSide";
	public static final String DT_S_AJAX_SOURCE = "ajax";
	public static final String DT_S_AJAXDATAPROP = "dataSrc";

	// AJAX URL parameters
	public static final String DT_I_DRAW = "draw";
	public static final String DT_I_COLUMNS = "iColumns";
	public static final String DT_S_COLUMNS = "sColumns";
	public static final String DT_I_START = "start";
	public static final String DT_I_LENGTH = "length";
	public static final String DT_M_DATA_PROP = "mDataProp_";
	public static final String DT_S_SEARCH = "search[value]";
	public static final String DT_S_COLUMN_SEARCH = "sSearch_";
	public static final String DT_B_REGEX = "bRegex";
	public static final String DT_B_SEARCHABLE = "bSearchable_";
	public static final String DT_B_SORTABLE = "bSortable_";
	public static final String DT_I_SORT_COL = "iSortCol_";
	public static final String DT_S_SORT_DIR = "sSortDir_";
	public static final String DT_I_SORTING_COLS = "iSortingCols";
	public static final String DT_B_REGEX_COL = "bRegex_";

	// Advanced configuration
	public static final String DT_DS_DATA = "data";
	public static final String DT_AOCOLUMNS = "columns";
	public static final String DT_LANGUAGE = "language";
	public static final String DT_JQUERYUI = "jQueryUI";

	// Extra features
	public static final String DT_SCROLLY = "scrollY";
	public static final String DT_SCROLLX = "scrollX";
	public static final String DT_SCROLLXINNER = "scrollXInner";
	public static final String DT_SCROLLCOLLAPSE = "scrollCollapse";
	public static final String DT_OFFSETTOP = "offsetTop";
	public static final String DT_FILTER_TYPE = "type";
	public static final String DT_FILTER_VALUES = "values";
	public static final String DT_S_SELECTOR = "sSelector";
	public static final String DT_SORT_INIT = "order";

	// Column configuration
	public static final String DT_NAME = "name";
	public static final String DT_SORTABLE = "orderable";
	public static final String DT_SEARCHABLE = "searchable";
	public static final String DT_VISIBLE = "visible";
	public static final String DT_SORT_DIR = "asSorting";
	public static final String DT_DATA = "data";
	public static final String DT_COLUMN_RENDERER = "render";
	public static final String DT_DEFAULT_CONTENT = "defaultContent";
	public static final String DT_TYPE = "type";

	// Callbacks
	public static final String DT_FN_CREATED_ROW = "createdRow";
	public static final String DT_FN_DRAW_CBK = "drawCallback";
	public static final String DT_FN_FOOTER_CBK = "footerCallback";
	public static final String DT_FN_FORMAT_NUMBER = "formatNumber";
	public static final String DT_FN_HEADER_CBK = "headerCallback";
	public static final String DT_FN_INFO_CBK = "infoCallback";
	public static final String DT_FN_INIT_COMPLETE = "initComplete";
	public static final String DT_FN_PRE_DRAW_CBK = "preDrawCallback";
	public static final String DT_FN_ROW_CBK = "rowCallback";

	/**
    * <p>
    * Suppress default constructor for noninstantiability.
    * </p>
    */
   private DTConstants() {
      throw new AssertionError();
   }
}
