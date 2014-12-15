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
package com.github.dandelion.datatables.thymeleaf.dialect;

import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractTableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxParamsAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAppearAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAutoWidthAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableConfGroupAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableCustomExtensionsProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxDeferRenderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableDisplayLengthAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableDomAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableExportAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableExportContainerClassAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableExportContainerStyleAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterClearSelectorAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterDelayAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterPlaceholderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterSelectorAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterTriggerAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableFilterableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableInfoAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableLengthChangeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableLengthMenuAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TablePageableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TablePaginationTypeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxPipeSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxPipeliningAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableProcessingAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableReloadFunctionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableReloadSelectorAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableScrollCollapseAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableScrollXAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableScrollXInnerAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableScrollYAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxServerSideAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableSortableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableStateSaveAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableStripeClassesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableThemeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableThemeOptionAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TableAjaxUrlAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TheadFixedOffsetTopAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.attr.TheadFixedPositionAttrProcessor;

/**
 * All attribute processors used by Dandelion-DataTables.
 *
 * @since 0.8.9
 */
public enum TableAttrProcessors {
	
	// Configuration
	CONF_GROUP(TableConfGroupAttrProcessor.class, "confGroup", "table"),
	
    // Basic processors
    BASIC_AUTO_WIDTH(TableAutoWidthAttrProcessor.class, "autoWidth", "table"),
    BASIC_FILTERABLE(TableFilterableAttrProcessor.class, "filterable", "table"),
    BASIC_INFO(TableInfoAttrProcessor.class, "info", "table"),
    BASIC_PAGINATE(TablePageableAttrProcessor.class, "pageable", "table"),
    BASIC_SORTABLE(TableSortableAttrProcessor.class, "sortable", "table"),
    BASIC_APPEAR(TableAppearAttrProcessor.class, "appear", "table"),
    BASIC_FILTER_PLACEHOLDER(TableFilterPlaceholderAttrProcessor.class, "filterPlaceholder", "table"),
    BASIC_FILTER_DELAY(TableFilterDelayAttrProcessor.class, "filterDelay", "table"),
    BASIC_FILTER_SELECTOR(TableFilterSelectorAttrProcessor.class, "filterSelector", "table"),
    BASIC_FILTER_CLEAR_SELECTOR(TableFilterClearSelectorAttrProcessor.class, "filterClearSelector", "table"),
    BASIC_FILTER_TRIGGER(TableFilterTriggerAttrProcessor.class, "filterTrigger", "table"),
    BASIC_PAGINATION_TYPE(TablePaginationTypeAttrProcessor.class, "paginationType", "table"),
    BASIC_LENGTH_MENU(TableLengthMenuAttrProcessor.class, "lengthMenu", "table"),
    BASIC_LENGTH_CHANGE(TableLengthChangeAttrProcessor.class, "lengthChange", "table"),
    BASIC_STRIPE_CLASSES(TableStripeClassesAttrProcessor.class, "stripeClasses", "table"),
    BASIC_DISPLAY_LENGTH(TableDisplayLengthAttrProcessor.class, "displayLength", "table"),
    BASIC_SCROLL_Y(TableScrollYAttrProcessor.class, "scrollY", "table"),
    BASIC_SCROLL_COLLAPSE(TableScrollCollapseAttrProcessor.class, "scrollCollapse", "table"),
    BASIC_SCROLL_X(TableScrollXAttrProcessor.class, "scrollX", "table"),
    BASIC_SCROLL_XINNER(TableScrollXInnerAttrProcessor.class, "scrollXInner", "table"),
    BASIC_DOM(TableDomAttrProcessor.class, "dom", "table"),
    BASIC_STATESAVE(TableStateSaveAttrProcessor.class, "stateSave", "table"),
    
    // Plugin processors
    PLUGIN_OFFSETTOP(TheadFixedOffsetTopAttrProcessor.class, "fixedOffsetTop", "thead"),
    PLUGIN_FIXEDPOSITION(TheadFixedPositionAttrProcessor.class, "fixedPosition", "thead"),

    // Feature processors
    FEATURE_CUSTOM_EXTENSIONS(TableCustomExtensionsProcessor.class, "ext", "table"),
    FEATURE_EXPORT(TableExportAttrProcessor.class, "export", "table"),
    FEATURE_EXPORT_CONTAINER_STYLE(TableExportContainerStyleAttrProcessor.class, "exportStyle", "table"),
    FEATURE_EXPORT_CONTAINER_CLASS(TableExportContainerClassAttrProcessor.class, "exportClass", "table"),

    // AJAX processors
    AJAX_URL(TableAjaxUrlAttrProcessor.class, "url", "table"),
    AJAX_SERVER_SIDE(TableAjaxServerSideAttrProcessor.class, "serverSide", "table"),
    AJAX_DEFER_RENDER(TableAjaxDeferRenderAttrProcessor.class, "deferRender", "table"),
    AJAX_PIPELINING(TableAjaxPipeliningAttrProcessor.class, "pipelining", "table"),
    AJAX_PIPESIZE(TableAjaxPipeSizeAttrProcessor.class, "pipeSize", "table"),
    AJAX_PROCESSING(TableProcessingAttrProcessor.class, "processing", "table"),
    AJAX_RELOAD_SELECTOR(TableReloadSelectorAttrProcessor.class, "reloadSelector", "table"),
    AJAX_RELOAD_FUNCTION(TableReloadFunctionAttrProcessor.class, "reloadFunction", "table"),
    AJAX_PARAMS(TableAjaxParamsAttrProcessor.class, "ajaxParams", "table"),

	// Theme processors
	THEME(TableThemeAttrProcessor.class, "theme", "table"), 
	THEME_OPTION(TableThemeOptionAttrProcessor.class, "themeOption", "table");

	private Class<? extends AbstractTableAttrProcessor> processorClass;
	private String attributeName;
	private String elementNameFilter;

	private TableAttrProcessors(Class<? extends AbstractTableAttrProcessor> processorClass, String attributeName,
			String elementNameFilter) {
		this.processorClass = processorClass;
		this.attributeName = attributeName;
		this.elementNameFilter = elementNameFilter;
	}

	public AbstractTableAttrProcessor getProcessor() {
		AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(attributeName, elementNameFilter,
				DataTablesDialect.DIALECT_PREFIX + ":table", "true");
		try {
			return processorClass.getDeclaredConstructor(IAttributeNameProcessorMatcher.class).newInstance(matcher);
		}
		catch (Exception e) {
			throw new DandelionException(e);
		}
	}
}