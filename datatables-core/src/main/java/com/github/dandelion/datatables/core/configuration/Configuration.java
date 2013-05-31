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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.github.dandelion.datatables.core.aggregator.AggregatorMode;
import com.github.dandelion.datatables.core.compressor.CompressorMode;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.processor.AbstractProcessor;
import com.github.dandelion.datatables.core.processor.BooleanProcessor;
import com.github.dandelion.datatables.core.processor.IntegerProcessor;
import com.github.dandelion.datatables.core.processor.StringProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxPipeliningProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxServerSideProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxSourceProcessor;
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
import com.github.dandelion.datatables.core.processor.plugin.PluginColReorderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginFixedHeaderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginScrollerProcessor;
import com.github.dandelion.datatables.core.theme.AbstractTheme;
import com.github.dandelion.datatables.core.theme.ThemeOption;

/**
 * Enumeration containing all possible configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public enum Configuration {

	MAIN_BASE_PACKAGE("main.base.package", "mainBasePackage", String.class, StringProcessor.class),
	MAIN_BASE_URL("main.base.url", "mainUrlBase", String.class, StringProcessor.class),
	MAIN_COMPRESSORENABLE("main.compressor.enable", "mainCompressorEnable", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORCLASS("main.compressor.class", "mainCompressorClass", String.class, StringProcessor.class),
	MAIN_COMPRESSORMODE("main.compressor.mode", "mainCompressorMode", CompressorMode.class, MainCompressorModeProcessor.class),
	MAIN_COMPRESSORMUNGE("main.compressor.munge", "mainCompressorMunge", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORPRESERVESEMI("main.compressor.preservesemi", "mainCompressorPreserveSemiColons", Boolean.class, BooleanProcessor.class),
	MAIN_COMPRESSORDISABLEOPTI("main.compressor.disableopti", "mainCompressorDisableOpti", Boolean.class, BooleanProcessor.class),
	MAIN_AGGREGATORENABLE("main.aggregator.enable", "mainAggregatorEnable", Boolean.class, BooleanProcessor.class),
	MAIN_AGGREGATORMODE("main.aggregator.mode", "mainAggregatorMode", AggregatorMode.class, MainAggregatorModeProcessor.class),
	
	CSS_CLASS("css.class", "cssClass", String.class, StringProcessor.class),
	CSS_STYLE("css.style", "cssStyle", String.class, StringProcessor.class),
	CSS_STRIPECLASSES("css.stripeClasses", "cssStripeClasses", String.class, CssStripeClassesProcessor.class),
	
	// DataTables features
	FEATURE_INFO("feature.info", "featureInfo", Boolean.class, BooleanProcessor.class),
	FEATURE_AUTOWIDTH("feature.autoWidth", "featureAutoWidth", Boolean.class, BooleanProcessor.class),
	FEATURE_FILTERABLE("feature.filterable", "featureFilterable", Boolean.class, BooleanProcessor.class),
	FEATURE_PAGINATE("feature.paginate", "featurePaginate", Boolean.class, BooleanProcessor.class),
	FEATURE_PAGINATIONTYPE("feature.paginationType", "featurePaginationType", PaginationType.class, FeaturePaginationTypeProcessor.class),
	FEATURE_LENGTHCHANGE("feature.lengthChange", "featureLengthChange", Boolean.class, BooleanProcessor.class),
	FEATURE_SORT("feature.sort", "featureSort", Boolean.class, BooleanProcessor.class),
	FEATURE_STATESAVE("feature.stateSave", "featureStateSave", Boolean.class, BooleanProcessor.class),
	FEATURE_JQUERYUI("feature.jqueryUi", "featureJqueryUi", Boolean.class, BooleanProcessor.class),
	FEATURE_LENGTHMENU("feature.lengthMenu", "featureLengthMenu", String.class, FeatureLengthMenuProcessor.class),
	FEATURE_DISPLAYLENGTH("feature.displayLength", "featureDisplayLength", Integer.class, IntegerProcessor.class),
	FEATURE_DOM("feature.dom", "featureDom", String.class, StringProcessor.class),
	FEATURE_SCROLLY("feature.scrollY", "featureScrolly", String.class, StringProcessor.class),
	FEATURE_SCROLLCOLLAPSE("feature.scrollCollapse", "featureScrollCollapse", Boolean.class, BooleanProcessor.class),
	
	EXTRA_THEME("extra.theme", "extraTheme", AbstractTheme.class, ExtraThemeProcessor.class),
	EXTRA_THEMEOPTION("extra.themeOption", "extraThemeOption", ThemeOption.class, ExtraThemeOptionProcessor.class),
	EXTRA_CUSTOMFEATURES("extra.customFeatures", "extraCustomFeatures", List.class, ExtraCustomExtensionProcessor.class),
	EXTRA_CUSTOMPLUGINS("extra.customPlugins", "extraCustomPlugins", List.class, ExtraCustomExtensionProcessor.class),
//	EXTRA_FILES("extra.files", "extraFiles", String.class, StringProcessor.class),
//	EXTRA_CONFS("extra.confs", "extraConfs", String.class, StringProcessor.class),
//	EXTRA_CALLBACKS("extra.callbacks", String.class, StringProcessor.class),
	EXTRA_CDN("extra.cdn", "extraCdn", Boolean.class, BooleanProcessor.class),
	EXTRA_APPEAR("extra.appear", "extraAppear", String.class, FeatureAppearProcessor.class),
	EXTRA_APPEARDURATION("extra.appearDuration", "extraAppearDuration", String.class, StringProcessor.class),
	
	// DataTables AJAX features
	AJAX_PROCESSING("ajax.processing", "ajaxProcessing", Boolean.class, BooleanProcessor.class),
	AJAX_DEFERRENDER("ajax.deferRender", "ajaxDeferRender", Boolean.class, BooleanProcessor.class),
	AJAX_SERVERSIDE("ajax.serverSide", "ajaxServerSide", Boolean.class, AjaxServerSideProcessor.class),
	AJAX_SOURCE("ajax.source", "ajaxSource", String.class, AjaxSourceProcessor.class),
	AJAX_PIPELINING("ajax.pipelining", "ajaxPipelining", Boolean.class, AjaxPipeliningProcessor.class),
	AJAX_PIPESIZE("ajax.pipeSize", "ajaxPipeSize", Integer.class, IntegerProcessor.class),
	AJAX_SERVERDATA("ajax.serverData", "ajaxServerData", String.class, StringProcessor.class),
	AJAX_SERVERPARAM("ajax.serverParam", "ajaxServerParam", String.class, StringProcessor.class),
	AJAX_SERVERMETHOD("ajax.serverMethod", "ajaxServerMethod", String.class, StringProcessor.class),
	
	PLUGIN_FIXEDPOSITION("plugin.fixedPosition", "pluginFixedPosition", String.class, StringProcessor.class),
	PLUGIN_FIXEDOFFSETTOP("plugin.fixedOffsetTop", "pluginFixedOffsetTop", Integer.class, IntegerProcessor.class),
	PLUGIN_FIXEDHEADER("plugin.fixedHeader", "pluginFixedHeader", Boolean.class, PluginFixedHeaderProcessor.class),
	PLUGIN_SCROLLER("plugin.scroller", "pluginScroller", Boolean.class, PluginScrollerProcessor.class),
	PLUGIN_COLREORDER("plugin.colReorder", "pluginColReorder", Boolean.class, PluginColReorderProcessor.class),
	
	EXPORT_TYPES("export.types", "exportTypes", String.class, ExportTypesProcessor.class),
	EXPORT_LINKS("export.links", "exportLinkPositions", List.class, ExportLinkPositionsProcessor.class),
	EXPORT_CSV_DEFAULT_CLASS("export.csv.default.class", "exportDefaultCsvClass", String.class, StringProcessor.class),
	EXPORT_XML_DEFAULT_CLASS("export.xml.default.class", "exportDefaultXmlClass", String.class, StringProcessor.class),
	EXPORT_XLS_DEFAULT_CLASS("export.xls.default.class", "exportDefaultXlsClass", String.class, StringProcessor.class),
	EXPORT_XLSX_DEFAULT_CLASS("export.xlsx.default.class", "exportDefaultXlsxClass", String.class, StringProcessor.class),
	EXPORT_PDF_DEFAULT_CLASS("export.pdf.default.class", "exportDefaultPdfClass", String.class, StringProcessor.class),
	EXPORT_CSV_CLASS("export.csv.class", "exportCsvClass", String.class, StringProcessor.class),
	EXPORT_XML_CLASS("export.xml.class", "exportXmlClass", String.class, StringProcessor.class),
	EXPORT_XLS_CLASS("export.xls.class", "exportXlsClass", String.class, StringProcessor.class),
	EXPORT_XLSX_CLASS("export.xlsx.class", "exportXlsxClass", String.class, StringProcessor.class),
	EXPORT_PDF_CLASS("export.pdf.class", "exportPdfClass", String.class, StringProcessor.class),
	
	// For internal use only
	INTERNAL_OBJECTTYPE("internal.objectType", "internalObjectType", String.class, StringProcessor.class);
	
	/**
	 * Name of the configuration in the properties file.
	 */
	private String name;
	
	private String property;
	
	/**
	 * 
	 */
	private Class<?> returnType;
	
	/**
	 * Processor that has to be applied to update the {@link TableConfiguration}.
	 */
	private Class<? extends AbstractProcessor> processor;
	
	private Configuration(String name, String property, Class<?> returnType, Class<? extends AbstractProcessor> processor){
		this.name = name;
		this.property = property;
		this.returnType = returnType;
		this.processor = processor;
	}

	public String getName() {
		return name;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public String getProperty() {
		return property;
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
	
	public static Configuration findByName(String name) {
		for (Configuration config : values()) {
			if (config.getName().equals(name)) {
				return config;
			}
		}
		return null;
	}
	
	public static void applyConfiguration(TableConfiguration tableConfiguration, Map<Configuration, Object> localConf) {

		try {
			// Appliquer la configuration locale
			// Pour chaque cle (= nom de la variable d'instance) dans la map, on
			// va recuperer le setter correspondant et l'invoquer
			for (Map.Entry<Configuration, Object> entry : localConf.entrySet()) {
				
				System.out.println("entry.getKey() ="  + entry.getKey());
				System.out.println("entry.getKey().getProcessor() = " + entry.getKey().getProcessor());
				System.out.println("entry.getValue() = " + entry.getValue());
				// Processing de la valeur
				Object object = entry.getKey().getProcessor().process(entry.getValue().toString(), tableConfiguration, localConf);
				
//				String tmp = WordUtils.capitalizeFully(entry.getKey().getName(), new char[]{'.'});
//				String propertyName = entry.getKey().getName().split("\\.")[0].trim() + StringUtils.capitalize(entry.getKey().getName().split("\\.")[1].trim());
//				propertyName = tmp.replace(".", "");
//				System.out.println("propertyName = " + propertyName);
				String propertyName = StringUtils.capitalize(entry.getKey().getProperty());
				System.out.println("propertyName = " + propertyName);
				Method method = TableConfiguration.class.getMethod("set" + propertyName, new Class[]{ entry.getKey().getReturnType() });
//				Method method = MethodUtils.invokeExactMethod(object, methodName, arg)getAccessibleMethod(TableConfiguration.class, "set" + propertyName);
//				System.out.println("mthod = " + method);
				if(method != null){
					method.invoke(tableConfiguration, object);
				}
			}
		} 
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}