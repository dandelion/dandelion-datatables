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
package com.github.dandelion.datatables.core.extension.feature;

import java.util.HashSet;
import java.util.Set;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.config.DatatableBundles;
import com.github.dandelion.datatables.core.config.DatatableOptions;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.util.JavascriptUtils;


/**
 * <p>
 * Feature that is always enabled when server-side processing has been
 * activated.
 * <p>
 * Removing the fnAddjustColumnSizing will cause strange column's width at each
 * interaction with the table (paging, sorting, filtering ...)
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see TableConfig#AJAX_SERVERSIDE
 */
public class MultiFilterFeature extends AbstractExtension {

	public static final String MULTI_FILTER_FEATURE_NAME = "multiFilter";
	
	@Override
	public String getExtensionName() {
		return MULTI_FILTER_FEATURE_NAME;
	}

	@Override
	public void setup(HtmlTable table) {
		
		checkConfiguration(table);
		
		addBundle(DatatableBundles.DDL_DT_MULTIFILTER);
		
		String filterSelector = DatatableOptions.FEATURE_FILTER_SELECTOR.valueFrom(table.getTableConfiguration());
		String filterClearSelector = DatatableOptions.FEATURE_FILTER_CLEAR_SELECTOR.valueFrom(table.getTableConfiguration());
		Set<String> selectors = null;
		StringBuilder js = null;
		FilterPlaceholder filterPlaceholder = DatatableOptions.FEATURE_FILTER_PLACEHOLDER.valueFrom(table.getTableConfiguration());
		
		if(filterPlaceholder != null && filterPlaceholder.equals(FilterPlaceholder.NONE)) {
			
			selectors = new HashSet<String>();
			for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
				String selector = DatatableOptions.SELECTOR.valueFrom(column.getColumnConfiguration());
				if(StringUtils.isNotBlank(selector)) {
					selectors.add(selector);
				}
			}
			
			js = new StringBuilder();
			js.append("$('").append(filterSelector).append("').click(function() {");
			js.append("   var filterParams = {};");
			js.append("   $.each(").append(JavascriptUtils.toArray(selectors)).append(", function(i, selector){");
			js.append("      $(selector + ' .dandelion_column_filter').each(function (index) {");
			js.append("         if(!$(this).hasClass('search_init') && $(this).val() != ''){");
			js.append("            filterParams[$(this).attr('data-column-name')] = $(this).val();");
			js.append("         }");
			js.append("      });");
			js.append("   });");
			js.append("   oTable_").append(table.getId()).append(".fnMultiFilter( filterParams );");
			js.append("});");
		}
		else {
			js = new StringBuilder();
			js.append("$('" + filterSelector + "').click(function() {");
			js.append("   var filterParams = {};");
			js.append("   $('#").append(table.getId()).append(" .dandelion_column_filter').each(function (index) {;");
			js.append("      if(!$(this).hasClass('search_init') && $(this).val() != ''){");
			js.append("         filterParams[$(this).attr('data-column-name')] = $(this).val();");
			js.append("      }");
			js.append("   });");
			js.append("   oTable_").append(table.getId()).append(".fnMultiFilter( filterParams );");
			js.append("});");
		}
		appendToBeforeEndDocumentReady(js.toString());
				
		if(StringUtils.isNotBlank(filterClearSelector)) {
			if(FilterPlaceholder.NONE.equals(filterPlaceholder)) {
				js = new StringBuilder();
				js.append("$('").append(filterClearSelector).append("').click(function() {");
				js.append("   oTable_").append(table.getId()).append(".fnFilterClear();");
				js.append("   $.each(" + JavascriptUtils.toArray(selectors) + ", function(i, selector){");
				js.append("      $(selector + ' .dandelion_column_filter').each(function (index) {");
				js.append("         $(this).val('');");
				js.append("         $(this).trigger('blur');");
				js.append("      });");
				js.append("   });");
				js.append("});");
			}
			else{
				js = new StringBuilder();
				js.append("$('").append(filterClearSelector).append("').click(function() {");
				js.append("   $('#").append(table.getId()).append(" .dandelion_column_filter').each(function (index) {;");
				js.append("      $(this).val('');");
				js.append("      $(this).trigger('blur');");
				js.append("   });");
				js.append("   oTable_").append(table.getId()).append(".fnFilterClear();");
				js.append("});");
			}
			appendToBeforeEndDocumentReady(js.toString());
		}
	}

	private void checkConfiguration(HtmlTable table) {
		
		String buttonId = DatatableOptions.FEATURE_FILTER_SELECTOR.valueFrom(table.getTableConfiguration());
		if (StringUtils.isBlank(buttonId)) {
			StringBuilder msg = new StringBuilder();
			msg.append("A filter button must be set in order to make the multi-filter work.");
			msg.append(" Please use the filterButton/dt:filterButton (JSP/Thymeleaf) table attribute.");
			throw new DandelionException(msg.toString());
		}

		Set<String> columnWithoutName = new HashSet<String>();
		for (HtmlColumn headerColumn : table.getLastHeaderRow().getColumns()) {
			String name = DatatableOptions.NAME.valueFrom(headerColumn.getColumnConfiguration());
			if (StringUtils.isBlank(name)) {
				columnWithoutName.add(name);
			}
		}

		if (!columnWithoutName.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			msg.append("All columns must have a name in order to make the multi-filter work.");
			msg.append(" Please use the name/dt:name (JSP/Thymeleaf) column attribute to assign a name to each column.");
			throw new DandelionException(msg.toString());
		}
	}
}