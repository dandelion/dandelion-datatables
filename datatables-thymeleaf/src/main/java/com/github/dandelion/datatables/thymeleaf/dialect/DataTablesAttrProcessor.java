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

package com.github.dandelion.datatables.thymeleaf.dialect;

import com.github.dandelion.datatables.thymeleaf.processor.DatatablesAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeSizeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TablePipeliningAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableServerSideAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.ajax.TableUrlAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.basic.*;
import com.github.dandelion.datatables.thymeleaf.processor.feature.*;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadColReorderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadFixedHeaderAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.plugin.TheadScrollerAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.theme.TableThemeOptionAttrProcessor;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;

import java.lang.reflect.InvocationTargetException;

public enum DataTablesAttrProcessor {
    // Basic processors
    BASIC_AUTO_WIDTH(TableAutoWidthAttrProcessor.class, "autowidth", "table"),
    BASIC_CDN(TableCdnAttrProcessor.class, "cdn", "table"),
    BASIC_FILTER(TableFilterAttrProcessor.class, "filter", "table"),
    BASIC_INFO(TableInfoAttrProcessor.class, "info", "table"),
    BASIC_PAGINATE(TablePaginateAttrProcessor.class, "paginate", "table"),
    BASIC_SORT(TableSortAttrProcessor.class, "sort", "table"),
    BASIC_APPEAR(TableAppearAttrProcessor.class, "appear", "table"),
    BASIC_SORTABLE(ThSortableAttrProcessor.class, "sortable", "th"),
    BASIC_FILTERABLE(ThFilterableAttrProcessor.class, "filterable", "th"),
    BASIC_SEARCHABLE(ThSearchableAttrProcessor.class, "searchable", "th"),
    BASIC_FILTER_TYPE(ThFilterTypeAttrProcessor.class, "filterType", "th"),
    BASIC_LABELS(TableLabelsAttrProcessor.class, "labels", "table"),
    BASIC_PAGINATION_TYPE(TablePaginationTypeAttrProcessor.class, "paginationtype", "table"),

    // Plugin processors
    PLUGIN_SCROLLER(TheadScrollerAttrProcessor.class, "scroller", "thead"),
    PLUGIN_COLUMN_RECORDER(TheadColReorderAttrProcessor.class, "colreorder", "thead"),
    PLUGIN_FIXED_HEADER(TheadFixedHeaderAttrProcessor.class, "fixedheader", "thead"),

    // Feature processors
    FEATURE_EXPORT(TableExportAttrProcessor.class, "export", "table"),
    FEATURE_EXPORT_LINKS(TableExportLinksAttrProcessor.class, "exportLinks", "table"),
    FEATURE_EXPORT_FILENAME(ThExportFilenameAttrProcessor.class, "filename", "th"),

    EXPORT_HEADER_CSV(TbodyExportHeaderAttrProcessor.class, "csv:header", "tbody"),
    EXPORT_HEADER_PDF(TbodyExportHeaderAttrProcessor.class, "pdf:header", "tbody"),
    EXPORT_HEADER_XLS(TbodyExportHeaderAttrProcessor.class, "xls:header", "tbody"),
    EXPORT_HEADER_XLSX(TbodyExportHeaderAttrProcessor.class, "xlsx:header", "tbody"),
    EXPORT_HEADER_XML(TbodyExportHeaderAttrProcessor.class, "xml:header", "tbody"),

    EXPORT_AUTOSIZE_XLS(TbodyExportAutoSizeAttrProcessor.class, "xls:autosize", "tbody"),
    EXPORT_AUTOSIZE_XLSX(TbodyExportAutoSizeAttrProcessor.class, "xlsx:autosize", "tbody"),

    EXPORT_LINK_CLASS_CSV(TbodyExportLinkClassAttrProcessor.class, "csv:class", "tbody"),
    EXPORT_LINK_CLASS_PDF(TbodyExportLinkClassAttrProcessor.class, "pdf:class", "tbody"),
    EXPORT_LINK_CLASS_XLS(TbodyExportLinkClassAttrProcessor.class, "xls:class", "tbody"),
    EXPORT_LINK_CLASS_XLSX(TbodyExportLinkClassAttrProcessor.class, "xlsx:class", "tbody"),
    EXPORT_LINK_CLASS_XML(TbodyExportLinkClassAttrProcessor.class, "xml:class", "tbody"),

    EXPORT_LINK_STYLE_CSV(TbodyExportLinkStyleAttrProcessor.class, "csv:style", "tbody"),
    EXPORT_LINK_STYLE_PDF(TbodyExportLinkStyleAttrProcessor.class, "pdf:style", "tbody"),
    EXPORT_LINK_STYLE_XLS(TbodyExportLinkStyleAttrProcessor.class, "xls:style", "tbody"),
    EXPORT_LINK_STYLE_XLSX(TbodyExportLinkStyleAttrProcessor.class, "xlsx:style", "tbody"),
    EXPORT_LINK_STYLE_XML(TbodyExportLinkStyleAttrProcessor.class, "xml:style", "tbody"),

    EXPORT_LINK_LABEL_CSV(TbodyExportLinkLabelAttrProcessor.class, "csv:label", "tbody"),
    EXPORT_LINK_LABEL_PDF(TbodyExportLinkLabelAttrProcessor.class, "pdf:label", "tbody"),
    EXPORT_LINK_LABEL_XLS(TbodyExportLinkLabelAttrProcessor.class, "xls:label", "tbody"),
    EXPORT_LINK_LABEL_XLSX(TbodyExportLinkLabelAttrProcessor.class, "xlsx:label", "tbody"),
    EXPORT_LINK_LABEL_XML(TbodyExportLinkLabelAttrProcessor.class, "xml:label", "tbody"),

    EXPORT_FILENAME_CSV(TbodyExportFilenameAttrProcessor.class, "csv:filename", "tbody"),
    EXPORT_FILENAME_PDF(TbodyExportFilenameAttrProcessor.class, "pdf:filename", "tbody"),
    EXPORT_FILENAME_XLS(TbodyExportFilenameAttrProcessor.class, "xls:filename", "tbody"),
    EXPORT_FILENAME_XLSX(TbodyExportFilenameAttrProcessor.class, "xlsx:filename", "tbody"),
    EXPORT_FILENAME_XML(TbodyExportFilenameAttrProcessor.class, "xml:filename", "tbody"),

    // AJAX processors
    AJAX_URL(TableUrlAttrProcessor.class, "url", "table"),
    AJAX_SERVER_SIDE(TableServerSideAttrProcessor.class, "serverside", "table"),
    AJAX_PIPELINING(TablePipeliningAttrProcessor.class, "pipelining", "table"),
    AJAX_PIPE_SI(TablePipeSizeAttrProcessor.class, "pipesize", "table"),

    // Theme processors
    THEME(TableThemeAttrProcessor.class, "theme", "table"),
    THEME_OPTION(TableThemeOptionAttrProcessor.class, "themeOption", "table"),

    // Callbacks
    CALLBACK_COOKIE(TbodyCallbackCookieProcessor.class, "cbk:cookie", "tbody"),
    CALLBACK_CREATE_ROW(TbodyCallbackCreatedRowProcessor.class, "cbk:createdrow", "tbody"),
    CALLBACK_DRAW(TbodyCallbackDrawProcessor.class, "cbk:draw", "tbody"),
    CALLBACK_Footer(TbodyCallbackFooterProcessor.class, "cbk:footer", "tbody"),
    CALLBACK_FORMAT_NUMBER(TbodyCallbackFormatNumberProcessor.class, "cbk:format", "tbody"),
    CALLBACK_HEADER(TbodyCallbackHeaderProcessor.class, "cbk:header", "tbody"),
    CALLBACK_INFO(TbodyCallbackInfoProcessor.class, "cbk:info", "tbody"),
    CALLBACK_INIT(TbodyCallbackInitProcessor.class, "cbk:init", "tbody"),
    CALLBACK_PRE_DRAW(TbodyCallbackPreDrawProcessor.class, "cbk:predraw", "tbody"),
    CALLBACK_ROW(TbodyCallbackRowProcessor.class, "cbk:row", "tbody");


    private Class<? extends DatatablesAttrProcessor> processorClass;
    private String attributeName;
    private String elementNameFilter;

    private DataTablesAttrProcessor(Class<? extends DatatablesAttrProcessor> processorClass, String attributeName, String elementNameFilter) {
        this.processorClass = processorClass;
        this.attributeName = attributeName;
        this.elementNameFilter = elementNameFilter;
    }

    public DatatablesAttrProcessor getProcessor() {
        AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(attributeName, elementNameFilter);
        try {
            return processorClass.getDeclaredConstructor(AttributeNameProcessorMatcher.class).newInstance(matcher);
        } catch (InstantiationException e) {
            // TODO add a logger
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO add a logger
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO add a logger
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO add a logger
            e.printStackTrace();
        }
        return null;
    }
}
