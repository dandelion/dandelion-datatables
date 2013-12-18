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
package com.github.dandelion.datatables.core.configuration;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.dandelion.datatables.core.constants.Direction;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.extension.feature.SortType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.processor.BooleanProcessor;
import com.github.dandelion.datatables.core.processor.ColumnProcessor;
import com.github.dandelion.datatables.core.processor.IntegerProcessor;
import com.github.dandelion.datatables.core.processor.StringBuilderProcessor;
import com.github.dandelion.datatables.core.processor.EmptyStringProcessor;
import com.github.dandelion.datatables.core.processor.StringProcessor;
import com.github.dandelion.datatables.core.processor.column.FilterTypeProcessor;
import com.github.dandelion.datatables.core.processor.column.FilterableProcessor;
import com.github.dandelion.datatables.core.processor.column.SortDirectionProcessor;
import com.github.dandelion.datatables.core.processor.column.SortTypeProcessor;

/**
 * <p>
 * All possible configurations (or {@link ConfigToken}) that can be applied on a
 * {@link ColumnConfiguration} object.
 * <p>
 * Only header columns have a {@link ColumnConfiguration} instance attached.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public final class ColumnConfig {

	public static ConfigToken<String> UID = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> ID = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> TITLE = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> TITLEKEY = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> PROPERTY = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> DEFAULTVALUE = new ConfigToken<String>("", new EmptyStringProcessor());
	public static ConfigToken<StringBuilder> CSSSTYLE = new ConfigToken<StringBuilder>("", new StringBuilderProcessor());
	public static ConfigToken<StringBuilder> CSSCELLSTYLE = new ConfigToken<StringBuilder>("", new StringBuilderProcessor());
	public static ConfigToken<StringBuilder> CSSCLASS = new ConfigToken<StringBuilder>("", new StringBuilderProcessor());
	public static ConfigToken<StringBuilder> CSSCELLCLASS = new ConfigToken<StringBuilder>("", new StringBuilderProcessor());
	public static ConfigToken<Boolean> SORTABLE = new ConfigToken<Boolean>("", new BooleanProcessor());
	public static ConfigToken<List<Direction>> SORTDIRECTION = new ConfigToken<List<Direction>>("", new SortDirectionProcessor());
	public static ConfigToken<String> SORTINIT = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<SortType> SORTTYPE = new ConfigToken<SortType>("", new SortTypeProcessor());
	public static ConfigToken<Boolean> FILTERABLE = new ConfigToken<Boolean>("", new FilterableProcessor());
	public static ConfigToken<Boolean> SEARCHABLE = new ConfigToken<Boolean>("", new BooleanProcessor());
	public static ConfigToken<Boolean> VISIBLE = new ConfigToken<Boolean>("", new BooleanProcessor());
	public static ConfigToken<FilterType> FILTERTYPE = new ConfigToken<FilterType>("", new FilterTypeProcessor());
	public static ConfigToken<String> FILTERVALUES = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> FILTERCSSCLASS = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> FILTERPLACEHOLDER = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<Integer> FILTERMINLENGTH = new ConfigToken<Integer>("", new IntegerProcessor());
	public static ConfigToken<String> RENDERFUNCTION = new ConfigToken<String>("", new StringProcessor());
	public static ConfigToken<String> SELECTOR = new ConfigToken<String>("", new StringProcessor());
	
	/**
	 * <p>
	 * Overloads the configurations stored in the {@link ColumnConfiguration}
	 * instance with the one passed as parameter.
	 * <p>
	 * Only configuration token with not blank values will be merged into the
	 * {@link ColumnConfiguration} instance.
	 * 
	 * @param stagingConf
	 *            The staging configurations filled either with the JSP taglib
	 *            or with the Thymeleaf dialect.
	 * @param stagingExtensions
	 *            The staging extensions filled either with the JSP taglib or
	 *            with the Thymeleaf dialect.
	 * @param column
	 *            The column which holds the {@link ColumnConfiguration} to
	 *            overload.
	 */
	public static void applyConfiguration(Map<ConfigToken<?>, Object> stagingConf, Map<ConfigToken<?>, Extension> stagingExtensions,
			HtmlColumn column) {

		for(Entry<ConfigToken<?>, Object> stagingEntry : stagingConf.entrySet()){
			column.getColumnConfiguration().getConfigurations().put(stagingEntry.getKey(), stagingEntry.getValue());
		}
		
		column.getColumnConfiguration().getStagingExtension().putAll(stagingExtensions);
	}
	
	/**
	 * <p>
	 * At this point, the configuration stored inside the
	 * {@link ColumnConfiguration} contains only Strings. All these strings will
	 * be processed in this method, depending on the {@link ConfigToken} they
	 * are bound to.
	 * 
	 * <p>
	 * Once processed, all strings will be replaced by the typed value.
	 * 
	 * @param column
	 *            The column which contains the configurations to process.
	 * @param table
	 *            The table may be used by processor to register extensions.
	 */
	public static void processConfiguration(HtmlColumn column, HtmlTable table) {
		
		if(column.getColumnConfiguration().getConfigurations() != null){
			for(Entry<ConfigToken<?>, Object> entry : column.getColumnConfiguration().getConfigurations().entrySet()) {
				ColumnProcessor columnProcessor = (ColumnProcessor) entry.getKey().getProcessor();
				columnProcessor.process(entry, column.getColumnConfiguration(), table.getTableConfiguration());
			}
		
			// Merging staging configuration into to the final configuration map
			column.getColumnConfiguration().getConfigurations()
					.putAll(column.getColumnConfiguration().getStagingConfigurations());
		}
	}
	
	/**
	 * Hidden constructor.
	 */
	private ColumnConfig() {
	}
}