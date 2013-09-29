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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.feature.SortType;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.processor.AbstractGenericProcessor;
import com.github.dandelion.datatables.core.processor.AbstractMessageProcessor;
import com.github.dandelion.datatables.core.processor.ColumnProcessor;
import com.github.dandelion.datatables.core.processor.GenericProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxPipeliningProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxServerSideProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxSourceProcessor;
import com.github.dandelion.datatables.core.processor.column.DefaultValueProcessor;
import com.github.dandelion.datatables.core.processor.column.FilterTypeProcessor;
import com.github.dandelion.datatables.core.processor.column.SortDirectionProcessor;
import com.github.dandelion.datatables.core.processor.column.SortTypeProcessor;
import com.github.dandelion.datatables.core.processor.css.CssStripeClassesProcessor;
import com.github.dandelion.datatables.core.processor.css.CssThemeOptionProcessor;
import com.github.dandelion.datatables.core.processor.css.CssThemeProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportConfsProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportLinkPositionsProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureAppearProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureFilterPlaceholderProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureLengthMenuProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeaturePaginationTypeProcessor;
import com.github.dandelion.datatables.core.processor.generic.BooleanProcessor;
import com.github.dandelion.datatables.core.processor.generic.IntegerProcessor;
import com.github.dandelion.datatables.core.processor.generic.StringBuilderProcessor;
import com.github.dandelion.datatables.core.processor.generic.StringProcessor;
import com.github.dandelion.datatables.core.processor.i18n.MessageProcessor;
import com.github.dandelion.datatables.core.processor.internal.MessageResolverProcessor;
import com.github.dandelion.datatables.core.processor.main.MainExtensionNamesProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginColReorderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginFixedHeaderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginScrollerProcessor;

/**
 * Enumeration containing all possible configuration.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public enum Configuration {

	MAIN_EXTENSION_PACKAGE("main.extension.package", "mainExtensionPackage", String.class, StringProcessor.class),
	MAIN_EXTENSION_NAMES("main.extension.names", "mainExtensionNames", Set.class, MainExtensionNamesProcessor.class),
	MAIN_BASE_URL("main.base.url", "mainUrlBase", String.class, StringProcessor.class),
	MAIN_CDN("main.cdn", "mainCdn", Boolean.class, BooleanProcessor.class),
	MAIN_CDN_JS("main.cdn.js", "mainCdnJs", String.class, StringProcessor.class),
	MAIN_CDN_CSS("main.cdn.css", "mainCdnCss", String.class, StringProcessor.class),
	
	CSS_CLASS("css.class", "cssClass", StringBuilder.class, StringBuilderProcessor.class),
	CSS_STYLE("css.style", "cssStyle", StringBuilder.class, StringBuilderProcessor.class),
	CSS_STRIPECLASSES("css.stripeClasses", "cssStripeClasses", String.class, CssStripeClassesProcessor.class),
	CSS_THEME("css.theme", "cssTheme", Extension.class, CssThemeProcessor.class),
	CSS_THEMEOPTION("css.themeOption", "cssThemeOption", ThemeOption.class, CssThemeOptionProcessor.class),
	
	FEATURE_INFO("feature.info", "featureInfo", Boolean.class, BooleanProcessor.class),
	FEATURE_AUTOWIDTH("feature.autoWidth", "featureAutoWidth", Boolean.class, BooleanProcessor.class),
	FEATURE_FILTERABLE("feature.filterable", "featureFilterable", Boolean.class, BooleanProcessor.class),
	FEATURE_FILTER_PLACEHOLDER("feature.filterplaceholder", "featureFilterPlaceholder", FilterPlaceholder.class, FeatureFilterPlaceholderProcessor.class),
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
	FEATURE_SCROLLX("feature.scrollX", "featureScrollx", String.class, StringProcessor.class),
	FEATURE_SCROLLXINNER("feature.scrollXInner", "featureScrollXInner", String.class, StringProcessor.class),
	FEATURE_APPEAR("feature.appear", "featureAppear", String.class, FeatureAppearProcessor.class),
	
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
	
	EXPORT_TYPES("export.types", "exportConfs", Set.class, ExportConfsProcessor.class),
	EXPORT_LINKS("export.links", "exportLinkPositions", Set.class, ExportLinkPositionsProcessor.class),
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

	MSG_PROCESSING("msg.processing", "msgProcessing", null, MessageProcessor.class),
	MSG_SEARCH("msg.search", "msgSearch", null, MessageProcessor.class),
	MSG_LENGTHMENU("msg.lengthmenu", "msgLengthmenu", null, MessageProcessor.class),
	MSG_INFO("msg.info", "msgInfo", null, MessageProcessor.class),
	MSG_INFOEMPTY("msg.info.empty", "msgInfoEmpty", null, MessageProcessor.class),
	MSG_INFOFILTERED("msg.info.filtered", "msgInfoFiltered", null, MessageProcessor.class),
	MSG_INFOPOSTFIX("msg.info.postfix", "msgInfoPostfix", null, MessageProcessor.class),
	MSG_LOADINGRECORDS("msg.loadingrecords", "msgLoadingRecords", null, MessageProcessor.class),
	MSG_ZERORECORDS("msg.zerorecords", "msgZeroRecords", null, MessageProcessor.class),
	MSG_EMPTYTABLE("msg.emptytable", "msgEmptyTable", null, MessageProcessor.class),
	MSG_PAGINATE_FIRST("msg.paginate.first", "msgPaginateFirst", null, MessageProcessor.class),
	MSG_PAGINATE_PREVIOUS("msg.paginate.previous", "msgPaginatePrevious", null, MessageProcessor.class),
	MSG_PAGINATE_NEXT("msg.paginate.next", "msgPaginateNext", null, MessageProcessor.class),
	MSG_PAGINATE_LAST("msg.paginate.last", "msgPaginateLast", null, MessageProcessor.class),
	MSG_ARIA_SORTASC("msg.aria.sortasc", "msgAriaSortAsc", null, MessageProcessor.class),
	MSG_ARIA_SORTDESC("msg.aria.sortdesc", "msgAriaSortDesc", null, MessageProcessor.class),
			
	// Column configuration
	COLUMN_UID("column.uid", "uid", String.class, StringProcessor.class),
	COLUMN_TITLE("column.title", "title", String.class, StringProcessor.class),
	COLUMN_TITLEKEY("column.titleKey", "titleKey", String.class, StringProcessor.class),
	COLUMN_PROPERTY("", "property", String.class, StringProcessor.class),
	COLUMN_DEFAULTVALUE("", "defaultValue", String.class, DefaultValueProcessor.class),
	COLUMN_CSSSTYLE("", "cssStyle", String.class, StringProcessor.class),
	COLUMN_CSSCELLSTYLE("", "cssCellStyle", StringBuilder.class, StringBuilderProcessor.class),
	COLUMN_CSSCLASS("", "cssClass", String.class, StringProcessor.class),
	COLUMN_CSSCELLCLASS("", "cssCellClass", String.class, StringProcessor.class),
	COLUMN_SORTABLE("", "sortable", Boolean.class, BooleanProcessor.class),
	COLUMN_SORTDIRECTION("", "sortDirections", List.class, SortDirectionProcessor.class),
	COLUMN_SORTINIT("", "sortInit", String.class, StringProcessor.class),
	COLUMN_SORTTYPE("", "sortType", SortType.class, SortTypeProcessor.class),
	COLUMN_FILTERABLE("", "filterable", Boolean.class, BooleanProcessor.class),
	COLUMN_SEARCHABLE("", "searchable", Boolean.class, BooleanProcessor.class),
	COLUMN_VISIBLE("", "visible", Boolean.class, BooleanProcessor.class),
	COLUMN_FILTERTYPE("", "filterType", FilterType.class, FilterTypeProcessor.class),
	COLUMN_FILTERVALUES("", "filterValues", String.class, StringProcessor.class),
	COLUMN_FILTERCSSCLASS("", "filterCssClass", String.class, StringProcessor.class),
	COLUMN_FILTERPLACEHOLDER("", "filterPlaceholder", FilterPlaceholder.class, StringProcessor.class),
//	COLUMN_DISPLAY("", "enabledDisplayTypes", List.class, DisplayTypesProcessor.class),
	COLUMN_RENDERFUNCTION("", "renderFunction", String.class, StringProcessor.class),
	COLUMN_FORMAT("", "format", String.class, StringProcessor.class),
	COLUMN_SELECTOR("", "selector", String.class, StringProcessor.class),

	// For internal use only
	INTERNAL_OBJECTTYPE("internal.objectType", "internalObjectType", String.class, StringProcessor.class),
	INTERNAL_MESSAGE_RESOLVER("i18n.message.resolver", "internalMessageResolver", MessageResolver.class, MessageResolverProcessor.class);
		
	// Logger
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	/**
	 * Name of the configuration in the properties file.
	 */
	private String name;
	
	/**
	 * Name of the property in the {@link TableConfiguration} or in the
	 * {@link ColumnConfiguration} instance.
	 */
	private String property;
	
	/**
	 * Return type of the setter method that is called when using subclass of
	 * {@link AbstractGenericProcessor}.
	 */
	private Class<?> returnType;
	
	/**
	 * Processor associated with the configuration. This processor will be
	 * applied on values passed from JSP or Thymeleaf attributes.
	 */
	private Class<?> processor;
	
	private Configuration(String name, String property, Class<?> returnType, Class<?> processor){
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

	public Class<?> getProcessor() {
		return processor;
	}
	
	public static Configuration findByName(String name) {
		for (Configuration config : values()) {
			if (config.getName().equals(name)) {
				return config;
			}
		}
		return null;
	}
	
	/**
	 * TODO
	 * @param tableConfiguration
	 * @param localConf
	 * @throws ConfigurationLoadingException
	 */
	public static void applyConfiguration(TableConfiguration tableConfiguration, Map<Configuration, Object> localConf)
			throws ConfigurationLoadingException {

		logger.trace("Applying staging configuration to the TableConfiguration instance...");
		
		for (Map.Entry<Configuration, Object> entry : localConf.entrySet()) {
			
			logger.trace("Processing configuration {}...", entry.getKey());
			
			if(StringUtils.isNotBlank(entry.getValue().toString())){
				String propertyName = null;
				try {
					// Generic processors, for primitive types
					if (AbstractGenericProcessor.class.isAssignableFrom(entry.getKey().getProcessor())) {
						propertyName = StringUtils.capitalize(entry.getKey().getProperty());
						Method setter = TableConfiguration.class.getMethod("set" + propertyName, new Class[]{ entry.getKey().getReturnType() });
						
						logger.trace(" --> the {} will be used to process the value {}", setter, entry.getValue().toString());
						
						GenericProcessor genericProcessor = (GenericProcessor) entry.getKey().getProcessor().getDeclaredConstructor(new Class[]{ Method.class })
								.newInstance(setter);
						genericProcessor.processConfiguration(entry.getValue().toString(), tableConfiguration);
					}
					else{
						TableProcessor processor = null;
					
						// I18n message processors
						if(AbstractMessageProcessor.class.isAssignableFrom(entry.getKey().getProcessor())){
						processor = (TableProcessor) entry.getKey().getProcessor().getDeclaredConstructor(new Class[]{ String.class })
								.newInstance(entry.getKey().getName());
						}
						// All other processors
						else {
							processor = (TableProcessor) entry.getKey().getProcessor().newInstance();
						}

						processor.processConfiguration(entry.getValue().toString(), tableConfiguration, localConf);
					}
					
					logger.trace(" --> Processing completed successfully");
				} 
				catch (InstantiationException e) {
					throw new ConfigurationLoadingException("Unable to instantiate the "
							+ entry.getKey().getProcessor() + " processor", e);
				} 
				catch (IllegalAccessException e) {
					throw new ConfigurationLoadingException("Unable to access the "
							+ entry.getKey().getProcessor() + " processor", e);
				} 
				catch (InvocationTargetException e) {
					throw new ConfigurationLoadingException("Unable to invoke the constructor of the "
							+ entry.getKey().getProcessor() + " processor", e);
				} 
				catch (NoSuchMethodException e) {
					throw new ConfigurationLoadingException("The method 'set" + propertyName
							+ "' doesn't exist in the TableConfiguration object", e);
				}
			}
		}
		
		logger.trace("All configurations have been applied.");
	}
	
	public static void applyColumnConfiguration(ColumnConfiguration columnConfiguration, TableConfiguration tableConfiguration, Map<Configuration, Object> stagingConf)
			throws ConfigurationLoadingException {

		logger.trace("Applying staging configuration to the ColumnConfiguration instance...");
		
		for (Map.Entry<Configuration, Object> entry : stagingConf.entrySet()) {
			
			logger.trace("Processing configuration {}...", entry.getKey());
			
			String propertyName = null;
			try {
				
				// Generic processors, for primitive types
				if (AbstractGenericProcessor.class.isAssignableFrom(entry.getKey().getProcessor())) {
					propertyName = StringUtils.capitalize(entry.getKey().getProperty());
					Method setter = ColumnConfiguration.class.getMethod("set" + propertyName, new Class[] { entry.getKey().getReturnType() });

					logger.trace(" --> the {} will be used to process the value {}", setter, entry.getValue().toString());

					GenericProcessor genericProcessor = (GenericProcessor) entry.getKey().getProcessor().getDeclaredConstructor(new Class[] { Method.class }).newInstance(setter);
					genericProcessor.processConfiguration(entry.getValue().toString(), columnConfiguration);
				} 
				// Column processors
				else {
					ColumnProcessor columnProcessor = (ColumnProcessor) entry.getKey().getProcessor().newInstance();
					columnProcessor.processConfiguration(entry.getValue().toString(), columnConfiguration, tableConfiguration, stagingConf);
				}
				
				
				logger.trace(" --> Processing completed successfully");
			} 
			catch (InstantiationException e) {
				throw new ConfigurationLoadingException("Unable to instantiate the "
						+ entry.getKey().getProcessor() + " processor", e);
			} 
			catch (IllegalAccessException e) {
				throw new ConfigurationLoadingException("Unable to access the "
						+ entry.getKey().getProcessor() + " processor", e);
			} 
			catch (InvocationTargetException e) {
				throw new ConfigurationLoadingException("Unable to invoke the constructor of the "
						+ entry.getKey().getProcessor() + " processor", e);
			} 
			catch (NoSuchMethodException e) {
				throw new ConfigurationLoadingException("The method 'set" + propertyName
						+ "' doesn't exist in the ColumnConfiguration object", e);
			}
		}
		
		logger.trace("All configurations have been applied.");
	}
}