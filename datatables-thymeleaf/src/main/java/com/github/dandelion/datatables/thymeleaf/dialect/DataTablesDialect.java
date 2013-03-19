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
package com.github.dandelion.datatables.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.IProcessor;

import com.github.dandelion.datatables.thymeleaf.matcher.ElementNameWithoutPrefixProcessorMatcher;
import com.github.dandelion.datatables.thymeleaf.processor.ColumnInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TableFinalizerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TableInitializerElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TbodyElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TdElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.TrElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeliningAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableServerSideAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableUrlAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableAppearAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableAutoWidthAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableCdnAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableExportAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableExportLinksAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableFilterAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableInfoAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableLabelsAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TablePaginateAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TablePaginationTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.TableSortAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThFilterTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThFilterableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThSearchableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.ThSortableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportAutoSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportFilenameAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportHeaderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkClassAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkLabelAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.TbodyExportLinkStyleAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.feature.ThExportFilenameAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadColReorderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadFixedHeaderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadScrollerAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeOptionAttrProcessor;

/**
 * The Dandelion-datatables dialect.
 * 
 * @author Thibault Duchateau
 */
public class DataTablesDialect extends AbstractDialect {

	public static final String DIALECT_PREFIX = "dt";
	public static final String LAYOUT_NAMESPACE = "http://www.thymeleaf.org/dandelion/datatables";
	public static final int DT_HIGHEST_PRECEDENCE = 3500;
	
	public String getPrefix() {
		return DIALECT_PREFIX;
	}

	public boolean isLenient() {
		return false;
	}

	/*
	 * The processors.
	 */
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		
		// Core processors
		processors.add(new TableInitializerElProcessor(new ElementNameWithoutPrefixProcessorMatcher("table", DIALECT_PREFIX + ":table", "true")));
		processors.add(new TableFinalizerElProcessor(new ElementNameWithoutPrefixProcessorMatcher("div", DIALECT_PREFIX + ":tmp", "internalUse")));
		processors.add(new ColumnInitializerElProcessor(new ElementNameWithoutPrefixProcessorMatcher("th")));
		processors.add(new TbodyElProcessor(new ElementNameWithoutPrefixProcessorMatcher("tbody")));
		processors.add(new TrElProcessor(new ElementNameWithoutPrefixProcessorMatcher("tr", DIALECT_PREFIX + ":data", "internalUse")));
		processors.add(new TdElProcessor(new ElementNameWithoutPrefixProcessorMatcher("td", DIALECT_PREFIX + ":data", "internalUse")));

		// Basic processors
		processors.add(new TableAutoWidthAttrProcessor(new AttributeNameProcessorMatcher("autowidth", "table")));
		processors.add(new TableCdnAttrProcessor(new AttributeNameProcessorMatcher("cdn", "table")));
		processors.add(new TableFilterAttrProcessor(new AttributeNameProcessorMatcher("filter", "table")));
		processors.add(new TableInfoAttrProcessor(new AttributeNameProcessorMatcher("info", "table")));
		processors.add(new TablePaginateAttrProcessor(new AttributeNameProcessorMatcher("paginate", "table")));
		processors.add(new TableSortAttrProcessor(new AttributeNameProcessorMatcher("sort", "table")));
		processors.add(new TableAppearAttrProcessor(new AttributeNameProcessorMatcher("appear", "table")));
		processors.add(new ThSortableAttrProcessor(new AttributeNameProcessorMatcher("sortable", "th")));
		processors.add(new ThFilterableAttrProcessor(new AttributeNameProcessorMatcher("filterable", "th")));
		processors.add(new ThSearchableAttrProcessor(new AttributeNameProcessorMatcher("searchable", "th")));
		processors.add(new ThFilterTypeAttrProcessor(new AttributeNameProcessorMatcher("filterType", "th")));
		processors.add(new TableLabelsAttrProcessor(new AttributeNameProcessorMatcher("labels", "table")));
		processors.add(new TablePaginationTypeAttrProcessor(new AttributeNameProcessorMatcher("paginationtype", "table")));
		
		// Plugin processors
		processors.add(new TheadScrollerAttrProcessor(new AttributeNameProcessorMatcher("scroller", "thead")));
		processors.add(new TheadColReorderAttrProcessor(new AttributeNameProcessorMatcher("colreorder", "thead")));
		processors.add(new TheadFixedHeaderAttrProcessor(new AttributeNameProcessorMatcher("fixedheader", "thead")));
		
		// Feature processors
		processors.add(new TableExportAttrProcessor(new AttributeNameProcessorMatcher("export", "table")));
		processors.add(new TableExportLinksAttrProcessor(new AttributeNameProcessorMatcher("exportLinks", "table")));
		processors.add(new ThExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("filename", "th")));
		
		processors.add(new TbodyExportHeaderAttrProcessor(new AttributeNameProcessorMatcher("csv:header", "tbody")));
		processors.add(new TbodyExportHeaderAttrProcessor(new AttributeNameProcessorMatcher("pdf:header", "tbody")));
		processors.add(new TbodyExportHeaderAttrProcessor(new AttributeNameProcessorMatcher("xls:header", "tbody")));
		processors.add(new TbodyExportHeaderAttrProcessor(new AttributeNameProcessorMatcher("xlsx:header", "tbody")));
		processors.add(new TbodyExportHeaderAttrProcessor(new AttributeNameProcessorMatcher("xml:header", "tbody")));
		
		processors.add(new TbodyExportAutoSizeAttrProcessor(new AttributeNameProcessorMatcher("xls:autosize", "tbody")));
		processors.add(new TbodyExportAutoSizeAttrProcessor(new AttributeNameProcessorMatcher("xlsx:autosize", "tbody")));
		
		processors.add(new TbodyExportLinkClassAttrProcessor(new AttributeNameProcessorMatcher("csv:class", "tbody")));
		processors.add(new TbodyExportLinkClassAttrProcessor(new AttributeNameProcessorMatcher("pdf:class", "tbody")));
		processors.add(new TbodyExportLinkClassAttrProcessor(new AttributeNameProcessorMatcher("xls:class", "tbody")));
		processors.add(new TbodyExportLinkClassAttrProcessor(new AttributeNameProcessorMatcher("xlsx:class", "tbody")));
		processors.add(new TbodyExportLinkClassAttrProcessor(new AttributeNameProcessorMatcher("xml:class", "tbody")));
		
		processors.add(new TbodyExportLinkStyleAttrProcessor(new AttributeNameProcessorMatcher("csv:style", "tbody")));
		processors.add(new TbodyExportLinkStyleAttrProcessor(new AttributeNameProcessorMatcher("pdf:style", "tbody")));
		processors.add(new TbodyExportLinkStyleAttrProcessor(new AttributeNameProcessorMatcher("xls:style", "tbody")));
		processors.add(new TbodyExportLinkStyleAttrProcessor(new AttributeNameProcessorMatcher("xlsx:style", "tbody")));
		processors.add(new TbodyExportLinkStyleAttrProcessor(new AttributeNameProcessorMatcher("xml:style", "tbody")));
		
		processors.add(new TbodyExportLinkLabelAttrProcessor(new AttributeNameProcessorMatcher("csv:label", "tbody")));
		processors.add(new TbodyExportLinkLabelAttrProcessor(new AttributeNameProcessorMatcher("pdf:label", "tbody")));
		processors.add(new TbodyExportLinkLabelAttrProcessor(new AttributeNameProcessorMatcher("xls:label", "tbody")));
		processors.add(new TbodyExportLinkLabelAttrProcessor(new AttributeNameProcessorMatcher("xlsx:label", "tbody")));
		processors.add(new TbodyExportLinkLabelAttrProcessor(new AttributeNameProcessorMatcher("xml:label", "tbody")));
		
		processors.add(new TbodyExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("csv:filename", "tbody")));
		processors.add(new TbodyExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("pdf:filename", "tbody")));
		processors.add(new TbodyExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("xls:filename", "tbody")));
		processors.add(new TbodyExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("xlsx:filename", "tbody")));
		processors.add(new TbodyExportFilenameAttrProcessor(new AttributeNameProcessorMatcher("xml:filename", "tbody")));
		
		// AJAX processors
		processors.add(new TableUrlAttrProcessor(new AttributeNameProcessorMatcher("url", "table")));
		processors.add(new TableServerSideAttrProcessor(new AttributeNameProcessorMatcher("serverside", "table")));
		processors.add(new TablePipeliningAttrProcessor(new AttributeNameProcessorMatcher("pipelining", "table")));
		processors.add(new TablePipeSizeAttrProcessor(new AttributeNameProcessorMatcher("pipesize", "table")));
		
		// Theme processors
		processors.add(new TableThemeAttrProcessor(new AttributeNameProcessorMatcher("theme", "table")));
		processors.add(new TableThemeOptionAttrProcessor(new AttributeNameProcessorMatcher("themeOption", "table")));

		return processors;
	}
}
