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
package com.github.dandelion.datatables.core.configuration;

import com.github.dandelion.datatables.core.aggregator.AggregatorMode;
import com.github.dandelion.datatables.core.compressor.CompressorMode;
import com.github.dandelion.datatables.core.processor.AbstractProcessor;
import com.github.dandelion.datatables.core.processor.BooleanProcessor;
import com.github.dandelion.datatables.core.processor.IntegerProcessor;
import com.github.dandelion.datatables.core.processor.StringProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxSourceProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxServerSideProcessor;
import com.github.dandelion.datatables.core.processor.css.CssStripeClassesProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportLinkPositionsProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportTypesProcessor;
import com.github.dandelion.datatables.core.processor.extra.ExtraCustomExtensionProcessor;
import com.github.dandelion.datatables.core.processor.extra.ExtraThemeOptionProcessor;
import com.github.dandelion.datatables.core.processor.extra.ExtraThemeProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureAppearProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureLengthMenuProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeaturePaginationTypeProcessor;
import com.github.dandelion.datatables.core.processor.main.MainAggregatorModeProcessor;
import com.github.dandelion.datatables.core.processor.main.MainCompressorModeProcessor;
import com.github.dandelion.datatables.core.theme.Theme;
import com.github.dandelion.datatables.core.theme.ThemeOption;


public enum Configuration {

	MAIN_BASE_PACKAGE("main.base.package", String.class, StringProcessor.class),
	MAIN_BASE_URL("main.base.url", String.class, StringProcessor.class),
	MAIN_COMPRESSORENABLE("main.compressor.enable", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORCLASS("main.compressor.class", String.class, StringProcessor.class),
	MAIN_COMPRESSORMODE("main.compressor.mode", CompressorMode.class, MainCompressorModeProcessor.class),
	MAIN_COMPRESSORMUNGE("main.compressor.munge", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORPRESERVESEMI("main.compressor.preservesemi", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORDISABLEOPTI("main.compressor.disableopti", Boolean.class, BooleanProcessor.class),
	MAIN_AGGREGATORENABLE("main.aggregator.enable", Boolean.class, BooleanProcessor.class),
	MAIN_AGGREGATORMODE("main.aggregator.mode", AggregatorMode.class, MainAggregatorModeProcessor.class),
	
	CSS_CLASS("css.class", String.class, StringProcessor.class),
	CSS_STYLE("css.style", String.class, StringProcessor.class),
	CSS_STRIPECLASSES("css.stripeClasses", String.class, CssStripeClassesProcessor.class),
	
	// DataTables features
	FEATURE_INFO("feature.info", Boolean.class, BooleanProcessor.class),
	FEATURE_AUTOWIDTH("feature.autoWidth", Boolean.class, BooleanProcessor.class),
	FEATURE_FILTERABLE("feature.filterable", Boolean.class, BooleanProcessor.class),
	FEATURE_PAGINATE("feature.paginate", Boolean.class, BooleanProcessor.class),
	FEATURE_PAGINATIONTYPE("feature.paginationType", String.class, FeaturePaginationTypeProcessor.class),
	FEATURE_LENGTHCHANGE("feature.lengthChange", Boolean.class, BooleanProcessor.class),
	FEATURE_SORT("feature.sort", Boolean.class, BooleanProcessor.class),
	FEATURE_STATESAVE("feature.stateSave", Boolean.class, BooleanProcessor.class),
	FEATURE_JQUERYUI("feature.jqueryUi", Boolean.class, BooleanProcessor.class),
	FEATURE_LENGTHMENU("feature.lengthMenu", String.class, FeatureLengthMenuProcessor.class),
	FEATURE_DISPLAYLENGTH("feature.displayLength", Boolean.class, BooleanProcessor.class),
	FEATURE_DOM("feature.dom", Boolean.class, BooleanProcessor.class),
	FEATURE_SCROLLY("feature.scrollY", Boolean.class, BooleanProcessor.class),
	FEATURE_SCROLLCOLLAPSE("feature.scrollCollapse", Boolean.class, BooleanProcessor.class),
	
	EXTRA_THEME("extra.theme", Theme.class, ExtraThemeProcessor.class),
	EXTRA_THEMEOPTION("extra.themeOption", ThemeOption.class, ExtraThemeOptionProcessor.class),
	EXTRA_CUSTOMFEATURES("extra.customFeatures", String.class, ExtraCustomExtensionProcessor.class),
	EXTRA_CUSTOMPLUGINS("extra.customPlugins", String.class, ExtraCustomExtensionProcessor.class),
	EXTRA_FILES("extra.files", String.class, StringProcessor.class),
	EXTRA_CONFS("extra.confs", String.class, StringProcessor.class),
	EXTRA_CALLBACKS("extra.callbacks", String.class,StringProcessor.class),
	EXTRA_CDN("extra.cdn", Boolean.class, BooleanProcessor.class),
	EXTRA_APPEAR("extra.appear", String.class, FeatureAppearProcessor.class),
	EXTRA_APPEARDURATION("extra.appearDuration", String.class, StringProcessor.class),
	
	// DataTables AJAX features
	AJAX_PROCESSING("ajax.processing", Boolean.class, BooleanProcessor.class),
	AJAX_DEFERRENDER("ajax.deferRender", Boolean.class, BooleanProcessor.class),
	AJAX_SERVERSIDE("ajax.serverSide", Boolean.class, AjaxServerSideProcessor.class),
	AJAX_SOURCE("ajax.source", String.class, AjaxSourceProcessor.class),
	AJAX_PIPELINING("ajax.pipelining", Boolean.class, BooleanProcessor.class),
	AJAX_PIPESIZE("ajax.pipeSize", Integer.class, IntegerProcessor.class),
	AJAX_SERVERDATA("ajax.serverData", String.class, StringProcessor.class),
	AJAX_SERVERPARAM("ajax.serverParam", String.class, StringProcessor.class),
	AJAX_SERVERMETHOD("ajax.serverMethod", String.class, StringProcessor.class),
	
	PLUGIN_FIXEDPOSITION("plugin.fixedPosition", String.class, StringProcessor.class),
	PLUGIN_FIXEDOFFSETTOP("plugin.fixedOffsetTop", String.class, StringProcessor.class),
	PLUGIN_FIXEDHEADER("plugin.fixedHeader", String.class, StringProcessor.class),
	PLUGIN_SCROLLER("plugin.scroller", String.class, StringProcessor.class),
	PLUGIN_COLREORDER("plugin.colReorder", String.class, StringProcessor.class),
	
	EXPORT_TYPES("export.types", String.class, ExportTypesProcessor.class),
	EXPORT_LINKS("export.links", String.class, ExportLinkPositionsProcessor.class),
	EXPORT_CSV_DEFAULT_CLASS("export.csv.default.class", String.class, StringProcessor.class),
	EXPORT_XML_DEFAULT_CLASS("export.xml.default.class", String.class, StringProcessor.class),
	EXPORT_XLS_DEFAULT_CLASS("export.xls.default.class", String.class, StringProcessor.class),
	EXPORT_XLSX_DEFAULT_CLASS("export.xlsx.default.class", String.class, StringProcessor.class),
	EXPORT_PDF_DEFAULT_CLASS("export.pdf.default.class", String.class, StringProcessor.class),
	EXPORT_CSV_CLASS("export.csv.class", String.class, StringProcessor.class),
	EXPORT_XML_CLASS("export.xml.class", String.class, StringProcessor.class),
	EXPORT_XLS_CLASS("export.xls.class", String.class, StringProcessor.class),
	EXPORT_XLSX_CLASS("export.xlsx.class", String.class, StringProcessor.class),
	EXPORT_PDF_CLASS("export.pdf.class", String.class, StringProcessor.class),
	
	// For internal use only
	INTERNAL_OBJECTTYPE("internal.objectType", String.class, StringProcessor.class);
	
	private String name;
	private Class<?> type;
	private Class<? extends AbstractProcessor> processor;
	
	private Configuration(String name, Class<?> type, Class<? extends AbstractProcessor> processor){
		this.name = name;
		this.type = type;
		this.processor = processor;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public AbstractProcessor getProcessor() {
		try {
			return processor.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}