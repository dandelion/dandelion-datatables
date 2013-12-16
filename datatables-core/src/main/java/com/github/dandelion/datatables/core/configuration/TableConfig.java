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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportLinkPosition;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.i18n.LocaleResolver;
import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.core.processor.BooleanProcessor;
import com.github.dandelion.datatables.core.processor.IntegerProcessor;
import com.github.dandelion.datatables.core.processor.StringBuilderProcessor;
import com.github.dandelion.datatables.core.processor.StringProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxPipeliningProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxServerSideProcessor;
import com.github.dandelion.datatables.core.processor.ajax.AjaxSourceProcessor;
import com.github.dandelion.datatables.core.processor.css.CssStripeClassesProcessor;
import com.github.dandelion.datatables.core.processor.css.CssThemeOptionProcessor;
import com.github.dandelion.datatables.core.processor.css.CssThemeProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportEnabledFormatProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportFormatProcessor;
import com.github.dandelion.datatables.core.processor.export.ExportLinkPositionsProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureAppearProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureFilterPlaceholderProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeatureLengthMenuProcessor;
import com.github.dandelion.datatables.core.processor.feature.FeaturePaginationTypeProcessor;
import com.github.dandelion.datatables.core.processor.i18n.MessageProcessor;
import com.github.dandelion.datatables.core.processor.i18n.MessageResolverProcessor;
import com.github.dandelion.datatables.core.processor.main.MainExtensionNamesProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginColReorderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginFixedHeaderProcessor;
import com.github.dandelion.datatables.core.processor.plugin.PluginScrollerProcessor;

/**
 * <p>
 * All possible configurations (or {@link ConfigToken}) that can be applied on a
 * {@link TableConfiguration} object.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 * @see ConfigToken
 */
public final class TableConfig {

	private static Map<String, ConfigToken<?>> internalConf;
	
	public static final String P_MAIN_EXTENSION_PACKAGE = "main.extension.package";
	public static final String P_MAIN_EXTENSION_NAMES = "main.extension.names";
	public static final String P_MAIN_BASE_URL = "main.base.url";
	
	public static final String P_CSS_CLASS = "css.class";
	public static final String P_CSS_STYLE = "css.style";
	public static final String P_CSS_STRIPECLASSES = "css.stripeClasses";
	public static final String P_CSS_THEME = "css.theme";
	public static final String P_CSS_THEMEOPTION = "css.themeOption";
	
	public static final String P_FEATURE_INFO = "feature.info";
	public static final String P_FEATURE_AUTOWIDTH = "feature.autoWidth";
	public static final String P_FEATURE_FILTERABLE = "feature.filterable";
	public static final String P_FEATURE_FILTER_PLACEHOLDER = "feature.filterPlaceHolder";
	public static final String P_FEATURE_PAGINATE = "feature.paginate";
	public static final String P_FEATURE_PAGINATIONTYPE = "feature.paginationType";
	public static final String P_FEATURE_LENGTHCHANGE = "feature.lengthChange";
	public static final String P_FEATURE_SORT = "feature.sort";
	public static final String P_FEATURE_STATESAVE = "feature.stateSave";
	public static final String P_FEATURE_JQUERYUI = "feature.jqueryUi";
	public static final String P_FEATURE_LENGTHMENU = "feature.lengthMenu";
	public static final String P_FEATURE_DISPLAYLENGTH = "feature.displayLength";
	public static final String P_FEATURE_DOM = "feature.dom";
	public static final String P_FEATURE_SCROLLY = "feature.scrollY";
	public static final String P_FEATURE_SCROLLCOLLAPSE = "feature.scrollCollapse";
	public static final String P_FEATURE_SCROLLX = "feature.scrollX";
	public static final String P_FEATURE_SCROLLXINNER = "feature.scrollXInner";
	public static final String P_FEATURE_APPEAR = "feature.appear";
	public static final String P_FEATURE_APPEAR_DURATION = "feature.appearDuration";
	
	public static final String P_AJAX_PROCESSING = "ajax.processing";
	public static final String P_AJAX_DEFERRENDER = "ajax.deferRender";
	public static final String P_AJAX_SERVERSIDE = "ajax.serverSide";
	public static final String P_AJAX_SOURCE = "ajax.source";
	public static final String P_AJAX_PIPELINING = "ajax.pipelining";
	public static final String P_AJAX_PIPESIZE = "ajax.pipeSize";
	public static final String P_AJAX_SERVERDATA = "ajax.serverData";
	public static final String P_AJAX_SERVERPARAM = "ajax.serverParam";
	public static final String P_AJAX_SERVERMETHOD = "ajax.serverMethod";
	
	public static final String P_PLUGIN_FIXEDPOSITION = "plugin.fixedPosition";
	public static final String P_PLUGIN_FIXEDOFFSETTOP = "plugin.fixedOffsetTop";
	public static final String P_PLUGIN_FIXEDHEADER = "plugin.fixedHeader";
	public static final String P_PLUGIN_SCROLLER = "plugin.scroller";
	public static final String P_PLUGIN_COLREORDER = "plugin.colReorder";
	
	public static final String P_EXPORT_ENABLED_FORMATS = "export.enabled.formats";
	public static final String P_EXPORT_LINK_POSITIONS = "export.link.positions";
	
	public static final String P_I18N_MESSAGE_RESOLVER = "i18n.message.resolver";
	public static final String P_I18N_LOCALE_RESOLVER = "i18n.locale.resolver";
	public static final String P_I18N_MSG_PROCESSING = "i18n.msg.processing";
	public static final String P_I18N_MSG_SEARCH = "i18n.msg.search";
	public static final String P_I18N_MSG_LENGTHMENU = "i18n.msg.lengthmenu";
	public static final String P_I18N_MSG_INFO = "i18n.msg.info";
	public static final String P_I18N_MSG_INFOEMPTY = "i18n.msg.info.empty";
	public static final String P_I18N_MSG_INFOFILTERED = "i18n.msg.info.filtered";
	public static final String P_I18N_MSG_INFOPOSTFIX = "i18n.msg.info.postfix";
	public static final String P_I18N_MSG_LOADINGRECORDS = "i18n.msg.loadingrecords";
	public static final String P_I18N_MSG_ZERORECORDS = "i18n.msg.zerorecords";
	public static final String P_I18N_MSG_EMPTYTABLE = "i18n.msg.emptytable";
	public static final String P_I18N_MSG_PAGINATE_FIRST = "i18n.msg.paginate.first";
	public static final String P_I18N_MSG_PAGINATE_PREVIOUS = "i18n.msg.paginate.previous";
	public static final String P_I18N_MSG_PAGINATE_NEXT = "i18n.msg.paginate.next";
	public static final String P_I18N_MSG_PAGINATE_LAST = "i18n.msg.paginate.last";
	public static final String P_I18N_MSG_ARIA_SORTASC = "i18n.msg.aria.sortasc";
	public static final String P_I18N_MSG_ARIA_SORTDESC = "i18n.msg.aria.sortdesc";

			
	public static ConfigToken<String> MAIN_EXTENSION_PACKAGE = new ConfigToken<String>(P_MAIN_EXTENSION_PACKAGE, new StringProcessor());
	public static ConfigToken<Set<String>> MAIN_EXTENSION_NAMES = new ConfigToken<Set<String>>(P_MAIN_EXTENSION_NAMES, new MainExtensionNamesProcessor());
	public static ConfigToken<String> MAIN_BASE_URL = new ConfigToken<String>(P_MAIN_BASE_URL, new StringProcessor());

	public static ConfigToken<StringBuilder> CSS_CLASS = new ConfigToken<StringBuilder>(P_CSS_CLASS, new StringBuilderProcessor());
	public static ConfigToken<StringBuilder> CSS_STYLE = new ConfigToken<StringBuilder>(P_CSS_STYLE, new StringBuilderProcessor());
	public static ConfigToken<String> CSS_STRIPECLASSES = new ConfigToken<String>(P_CSS_STRIPECLASSES, new CssStripeClassesProcessor());
	public static ConfigToken<Extension> CSS_THEME = new ConfigToken<Extension>(P_CSS_THEME, new CssThemeProcessor());
	public static ConfigToken<ThemeOption> CSS_THEMEOPTION = new ConfigToken<ThemeOption>(P_CSS_THEMEOPTION, new CssThemeOptionProcessor());
	
	public static ConfigToken<Boolean> FEATURE_INFO = new ConfigToken<Boolean>(P_FEATURE_INFO, new BooleanProcessor());
	public static ConfigToken<Boolean> FEATURE_AUTOWIDTH = new ConfigToken<Boolean>(P_FEATURE_AUTOWIDTH, new BooleanProcessor());
	public static ConfigToken<Boolean> FEATURE_FILTERABLE = new ConfigToken<Boolean>(P_FEATURE_FILTERABLE, new BooleanProcessor());
	public static ConfigToken<FilterPlaceholder> FEATURE_FILTER_PLACEHOLDER = new ConfigToken<FilterPlaceholder>(P_FEATURE_FILTER_PLACEHOLDER, new FeatureFilterPlaceholderProcessor());
	public static ConfigToken<Boolean> FEATURE_PAGINATE = new ConfigToken<Boolean>(P_FEATURE_PAGINATE, new BooleanProcessor());
	public static ConfigToken<PaginationType> FEATURE_PAGINATIONTYPE = new ConfigToken<PaginationType>(P_FEATURE_PAGINATIONTYPE, new FeaturePaginationTypeProcessor());
	public static ConfigToken<Boolean> FEATURE_LENGTHCHANGE = new ConfigToken<Boolean>(P_FEATURE_LENGTHCHANGE, new BooleanProcessor());
	public static ConfigToken<Boolean> FEATURE_SORT = new ConfigToken<Boolean>(P_FEATURE_SORT, new BooleanProcessor());
	public static ConfigToken<Boolean> FEATURE_STATESAVE = new ConfigToken<Boolean>(P_FEATURE_STATESAVE, new BooleanProcessor());
	public static ConfigToken<Boolean> FEATURE_JQUERYUI = new ConfigToken<Boolean>(P_FEATURE_JQUERYUI, new BooleanProcessor());
	public static ConfigToken<String> FEATURE_LENGTHMENU = new ConfigToken<String>(P_FEATURE_LENGTHMENU, new FeatureLengthMenuProcessor());
	public static ConfigToken<Integer> FEATURE_DISPLAYLENGTH = new ConfigToken<Integer>(P_FEATURE_DISPLAYLENGTH, new IntegerProcessor());
	public static ConfigToken<String> FEATURE_DOM = new ConfigToken<String>(P_FEATURE_DOM, new StringProcessor());
	public static ConfigToken<String> FEATURE_SCROLLY = new ConfigToken<String>(P_FEATURE_SCROLLY, new StringProcessor());
	public static ConfigToken<Boolean> FEATURE_SCROLLCOLLAPSE = new ConfigToken<Boolean>(P_FEATURE_SCROLLCOLLAPSE, new BooleanProcessor());
	public static ConfigToken<String> FEATURE_SCROLLX = new ConfigToken<String>(P_FEATURE_SCROLLX, new StringProcessor());
	public static ConfigToken<String> FEATURE_SCROLLXINNER = new ConfigToken<String>(P_FEATURE_SCROLLXINNER, new StringProcessor());
	public static ConfigToken<String> FEATURE_APPEAR = new ConfigToken<String>(P_FEATURE_APPEAR, new FeatureAppearProcessor());
	public static ConfigToken<String> FEATURE_APPEAR_DURATION = new ConfigToken<String>(P_FEATURE_APPEAR_DURATION, null);
	
	public static ConfigToken<Boolean> AJAX_PROCESSING = new ConfigToken<Boolean>(P_AJAX_PROCESSING, new BooleanProcessor());
	public static ConfigToken<Boolean> AJAX_DEFERRENDER = new ConfigToken<Boolean>(P_AJAX_DEFERRENDER, new BooleanProcessor());
	public static ConfigToken<Boolean> AJAX_SERVERSIDE = new ConfigToken<Boolean>(P_AJAX_SERVERSIDE, new AjaxServerSideProcessor());
	public static ConfigToken<String> AJAX_SOURCE = new ConfigToken<String>(P_AJAX_SOURCE, new AjaxSourceProcessor());
	public static ConfigToken<Boolean> AJAX_PIPELINING = new ConfigToken<Boolean>(P_AJAX_PIPELINING, new AjaxPipeliningProcessor());
	public static ConfigToken<Integer> AJAX_PIPESIZE = new ConfigToken<Integer>(P_AJAX_PIPESIZE, new IntegerProcessor());
	public static ConfigToken<String> AJAX_SERVERDATA = new ConfigToken<String>(P_AJAX_SERVERDATA, new StringProcessor());
	public static ConfigToken<String> AJAX_SERVERPARAM = new ConfigToken<String>(P_AJAX_SERVERPARAM, new StringProcessor());
	public static ConfigToken<String> AJAX_SERVERMETHOD = new ConfigToken<String>(P_AJAX_SERVERMETHOD, new StringProcessor());
	
	public static ConfigToken<String> PLUGIN_FIXEDPOSITION = new ConfigToken<String>(P_PLUGIN_FIXEDPOSITION, new StringProcessor());
	public static ConfigToken<Integer> PLUGIN_FIXEDOFFSETTOP = new ConfigToken<Integer>(P_PLUGIN_FIXEDOFFSETTOP, new IntegerProcessor());
	public static ConfigToken<Boolean> PLUGIN_FIXEDHEADER = new ConfigToken<Boolean>(P_PLUGIN_FIXEDHEADER, new PluginFixedHeaderProcessor());
	public static ConfigToken<Boolean> PLUGIN_SCROLLER = new ConfigToken<Boolean>(P_PLUGIN_SCROLLER, new PluginScrollerProcessor());
	public static ConfigToken<Boolean> PLUGIN_COLREORDER = new ConfigToken<Boolean>(P_PLUGIN_COLREORDER, new PluginColReorderProcessor());
	
	public static ConfigToken<Set<ExportConf>> EXPORT_ENABLED_FORMATS = new ConfigToken<Set<ExportConf>>(P_EXPORT_ENABLED_FORMATS, new ExportEnabledFormatProcessor());
	public static ConfigToken<Set<ExportLinkPosition>> EXPORT_LINK_POSITIONS = new ConfigToken<Set<ExportLinkPosition>>(P_EXPORT_LINK_POSITIONS, new ExportLinkPositionsProcessor());
	public static ConfigToken<String> EXPORT_CLASS = new ConfigToken<String>(ExportFormatProcessor.REGEX_EXPORT_CLASS, new ExportFormatProcessor());
	public static ConfigToken<String> EXPORT_LABEL = new ConfigToken<String>(ExportFormatProcessor.REGEX_EXPORT_LABEL, new ExportFormatProcessor());
	public static ConfigToken<String> EXPORT_FILENAME = new ConfigToken<String>(ExportFormatProcessor.REGEX_EXPORT_FILENAME, new ExportFormatProcessor());
	public static ConfigToken<String> EXPORT_MIMETYPE = new ConfigToken<String>(ExportFormatProcessor.REGEX_EXPORT_MIMETYPE, new ExportFormatProcessor());
	
	public static ConfigToken<MessageResolver> I18N_MESSAGE_RESOLVER = new ConfigToken<MessageResolver>(P_I18N_MESSAGE_RESOLVER, new MessageResolverProcessor());
	public static ConfigToken<LocaleResolver> I18N_LOCALE_RESOLVER = new ConfigToken<LocaleResolver>(P_I18N_LOCALE_RESOLVER, new MessageResolverProcessor());
	public static ConfigToken<String> I18N_MSG_PROCESSING = new ConfigToken<String>(P_I18N_MSG_PROCESSING, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_SEARCH = new ConfigToken<String>(P_I18N_MSG_SEARCH, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_LENGTHMENU = new ConfigToken<String>(P_I18N_MSG_LENGTHMENU, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_INFO = new ConfigToken<String>(P_I18N_MSG_INFO, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_INFOEMPTY = new ConfigToken<String>(P_I18N_MSG_INFOEMPTY, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_INFOFILTERED = new ConfigToken<String>(P_I18N_MSG_INFOFILTERED, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_INFOPOSTFIX = new ConfigToken<String>(P_I18N_MSG_INFOPOSTFIX, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_LOADINGRECORDS = new ConfigToken<String>(P_I18N_MSG_LOADINGRECORDS, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_ZERORECORDS = new ConfigToken<String>(P_I18N_MSG_ZERORECORDS, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_EMPTYTABLE = new ConfigToken<String>(P_I18N_MSG_EMPTYTABLE, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_PAGINATE_FIRST = new ConfigToken<String>(P_I18N_MSG_PAGINATE_FIRST, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_PAGINATE_PREVIOUS = new ConfigToken<String>(P_I18N_MSG_PAGINATE_PREVIOUS, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_PAGINATE_NEXT = new ConfigToken<String>(P_I18N_MSG_PAGINATE_NEXT, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_PAGINATE_LAST = new ConfigToken<String>(P_I18N_MSG_PAGINATE_LAST, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_ARIA_SORTASC = new ConfigToken<String>(P_I18N_MSG_ARIA_SORTASC, new MessageProcessor());
	public static ConfigToken<String> I18N_MSG_ARIA_SORTDESC = new ConfigToken<String>(P_I18N_MSG_ARIA_SORTDESC, new MessageProcessor());
	
	public static ConfigToken<String> INTERNAL_OBJECTTYPE = new ConfigToken<String>("internal.objectType", new StringProcessor());
	
	static {
		internalConf = new HashMap<String, ConfigToken<?>>();
		internalConf.put(P_MAIN_EXTENSION_PACKAGE, MAIN_EXTENSION_PACKAGE);
		internalConf.put(P_MAIN_EXTENSION_NAMES, MAIN_EXTENSION_NAMES);
		internalConf.put(P_MAIN_BASE_URL, MAIN_BASE_URL);
		
		internalConf.put(formalize(P_CSS_CLASS), CSS_CLASS);
		internalConf.put(formalize(P_CSS_STYLE), CSS_STYLE);
		internalConf.put(formalize(P_CSS_STRIPECLASSES), CSS_STRIPECLASSES);
		internalConf.put(formalize(P_CSS_THEME), CSS_THEME);
		internalConf.put(formalize(P_CSS_THEMEOPTION), CSS_THEMEOPTION);
		
		internalConf.put(formalize(P_FEATURE_INFO), FEATURE_INFO);
		internalConf.put(formalize(P_FEATURE_AUTOWIDTH), FEATURE_AUTOWIDTH);
		internalConf.put(formalize(P_FEATURE_FILTERABLE), FEATURE_FILTERABLE);
		internalConf.put(formalize(P_FEATURE_FILTER_PLACEHOLDER), FEATURE_FILTER_PLACEHOLDER);
		internalConf.put(formalize(P_FEATURE_PAGINATE), FEATURE_PAGINATE);
		internalConf.put(formalize(P_FEATURE_PAGINATIONTYPE), FEATURE_PAGINATIONTYPE);
		internalConf.put(formalize(P_FEATURE_LENGTHCHANGE), FEATURE_LENGTHCHANGE);
		internalConf.put(formalize(P_FEATURE_SORT), FEATURE_SORT);
		internalConf.put(formalize(P_FEATURE_STATESAVE), FEATURE_STATESAVE);
		internalConf.put(formalize(P_FEATURE_JQUERYUI), FEATURE_JQUERYUI);
		internalConf.put(formalize(P_FEATURE_LENGTHMENU), FEATURE_LENGTHMENU);
		internalConf.put(formalize(P_FEATURE_DISPLAYLENGTH), FEATURE_DISPLAYLENGTH);
		internalConf.put(formalize(P_FEATURE_DOM), FEATURE_DOM);
		internalConf.put(formalize(P_FEATURE_SCROLLY), FEATURE_SCROLLY);
		internalConf.put(formalize(P_FEATURE_SCROLLCOLLAPSE), FEATURE_SCROLLCOLLAPSE);
		internalConf.put(formalize(P_FEATURE_SCROLLX), FEATURE_SCROLLX);
		internalConf.put(formalize(P_FEATURE_SCROLLXINNER), FEATURE_SCROLLXINNER);
		internalConf.put(formalize(P_FEATURE_APPEAR), FEATURE_APPEAR);
		internalConf.put(formalize(P_FEATURE_APPEAR_DURATION), FEATURE_APPEAR_DURATION);
		
		internalConf.put(formalize(P_AJAX_PROCESSING), AJAX_PROCESSING);
		internalConf.put(formalize(P_AJAX_DEFERRENDER), AJAX_DEFERRENDER);
		internalConf.put(formalize(P_AJAX_SERVERSIDE), AJAX_SERVERSIDE);
		internalConf.put(formalize(P_AJAX_SOURCE), AJAX_SOURCE);
		internalConf.put(formalize(P_AJAX_PIPELINING), AJAX_PIPELINING);
		internalConf.put(formalize(P_AJAX_PIPESIZE), AJAX_PIPESIZE);
		internalConf.put(formalize(P_AJAX_SERVERDATA), AJAX_SERVERDATA);
		internalConf.put(formalize(P_AJAX_SERVERPARAM), AJAX_SERVERPARAM);
		internalConf.put(formalize(P_AJAX_SERVERMETHOD), AJAX_SERVERMETHOD);
		
		internalConf.put(formalize(P_PLUGIN_FIXEDPOSITION), PLUGIN_FIXEDPOSITION);
		internalConf.put(formalize(P_PLUGIN_FIXEDOFFSETTOP), PLUGIN_FIXEDOFFSETTOP);
		internalConf.put(formalize(P_PLUGIN_FIXEDHEADER), PLUGIN_FIXEDHEADER);
		internalConf.put(formalize(P_PLUGIN_SCROLLER), PLUGIN_SCROLLER);
		internalConf.put(formalize(P_PLUGIN_COLREORDER), PLUGIN_COLREORDER);
		
		internalConf.put(formalize(P_EXPORT_ENABLED_FORMATS), EXPORT_ENABLED_FORMATS);
		internalConf.put(formalize(P_EXPORT_LINK_POSITIONS), EXPORT_LINK_POSITIONS);
		internalConf.put(formalize(ExportFormatProcessor.REGEX_EXPORT_CLASS), EXPORT_CLASS);
		internalConf.put(formalize(ExportFormatProcessor.REGEX_EXPORT_LABEL), EXPORT_LABEL);
		internalConf.put(formalize(ExportFormatProcessor.REGEX_EXPORT_FILENAME), EXPORT_FILENAME);
		internalConf.put(formalize(ExportFormatProcessor.REGEX_EXPORT_MIMETYPE), EXPORT_MIMETYPE);
		
		internalConf.put(formalize(P_I18N_MESSAGE_RESOLVER), I18N_MESSAGE_RESOLVER);
		internalConf.put(formalize(P_I18N_LOCALE_RESOLVER), I18N_LOCALE_RESOLVER);
		internalConf.put(formalize(P_I18N_MSG_PROCESSING), I18N_MSG_PROCESSING);
		internalConf.put(formalize(P_I18N_MSG_SEARCH), I18N_MSG_SEARCH);
		internalConf.put(formalize(P_I18N_MSG_LENGTHMENU), I18N_MSG_LENGTHMENU);
		internalConf.put(formalize(P_I18N_MSG_INFO), I18N_MSG_INFO);
		internalConf.put(formalize(P_I18N_MSG_INFOEMPTY), I18N_MSG_INFOEMPTY);
		internalConf.put(formalize(P_I18N_MSG_INFOFILTERED), I18N_MSG_INFOFILTERED);
		internalConf.put(formalize(P_I18N_MSG_INFOPOSTFIX), I18N_MSG_INFOPOSTFIX);
		internalConf.put(formalize(P_I18N_MSG_LOADINGRECORDS), I18N_MSG_LOADINGRECORDS);
		internalConf.put(formalize(P_I18N_MSG_ZERORECORDS), I18N_MSG_ZERORECORDS);
		internalConf.put(formalize(P_I18N_MSG_EMPTYTABLE), I18N_MSG_EMPTYTABLE);
		internalConf.put(formalize(P_I18N_MSG_PAGINATE_FIRST), I18N_MSG_PAGINATE_FIRST);
		internalConf.put(formalize(P_I18N_MSG_PAGINATE_PREVIOUS), I18N_MSG_PAGINATE_PREVIOUS);
		internalConf.put(formalize(P_I18N_MSG_PAGINATE_NEXT), I18N_MSG_PAGINATE_NEXT);
		internalConf.put(formalize(P_I18N_MSG_PAGINATE_LAST), I18N_MSG_PAGINATE_LAST);
		internalConf.put(formalize(P_I18N_MSG_ARIA_SORTASC), I18N_MSG_ARIA_SORTASC);
		internalConf.put(formalize(P_I18N_MSG_ARIA_SORTDESC), I18N_MSG_ARIA_SORTDESC);
	}
	
	private static String formalize(String propertyName){
		return propertyName.trim().toLowerCase();
	}
	
	public static ConfigToken<?> findByPropertyName(String propertyName) {
		
		if(internalConf.containsKey(propertyName)){
			return internalConf.get(propertyName);
		}
		else if(propertyName.contains("export")){
			Pattern patternExportClass = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_CLASS, Pattern.CASE_INSENSITIVE);
			if(patternExportClass.matcher(propertyName).find()){
				ConfigToken<?> export = EXPORT_CLASS;
				export.setPropertyName(propertyName);
				return export;
			}
			
			Pattern patternExportFilename = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_FILENAME, Pattern.CASE_INSENSITIVE);
			if(patternExportFilename.matcher(propertyName).find()){
				ConfigToken<?> export = EXPORT_FILENAME;
				export.setPropertyName(propertyName);
				return export;
			}
			
			Pattern patternExportLabel = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_LABEL, Pattern.CASE_INSENSITIVE);
			if(patternExportLabel.matcher(propertyName).find()){
				ConfigToken<?> export = EXPORT_LABEL;
				export.setPropertyName(propertyName);
				return export;
			}
			
			Pattern patternExportMimetype = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_MIMETYPE, Pattern.CASE_INSENSITIVE);
			if(patternExportMimetype.matcher(propertyName).find()){
				ConfigToken<?> export = EXPORT_MIMETYPE;
				export.setPropertyName(propertyName);
				return export;
			}
		}

		return null;
	}

	/**
	 * <p>
	 * Overloads the configurations stored in the {@link TableConfiguration}
	 * instance with the one passed as parameter.
	 * 
	 * @param stagingConf
	 *            The staging configurations filled either with the JSP taglib
	 *            or with the Thymeleaf dialect.
	 * @param table
	 *            The table which holds the {@link TableConfiguration} to
	 *            overload.
	 */
	public static Map<ConfigToken<?>, Object> applyConfiguration(Map<ConfigToken<?>, Object> stagingConf,
			HtmlTable table) {
		
		for(Entry<ConfigToken<?>, Object> stagingEntry : stagingConf.entrySet()){
			if(StringUtils.isNotBlank(String.valueOf(stagingEntry.getValue()))){
				table.getTableConfiguration().getConfigurations().put(stagingEntry.getKey(), stagingEntry.getValue());
			}
		}
		
		return stagingConf;
	}
	
	/**
	 * <p>
	 * At this point, the configuration stored inside the
	 * {@link TableConfiguration} contains only Strings. All these strings will
	 * be processed in this method, depending on the {@link ConfigToken} they
	 * are bound to.
	 * 
	 * <p>
	 * Once processed, all strings will be replaced by the typed value.
	 * 
	 * <p>
	 * Only configuration token with not blank values will be merged into the
	 * {@link TableConfiguration} instance.
	 * 
	 * @param table
	 *            The table which holds the configuration to process.
	 */
	public static Map<ConfigToken<?>, Object> processConfiguration(HtmlTable table) {
		
		if(table.getTableConfiguration().getConfigurations() != null){
			for(Entry<ConfigToken<?>, Object> entry : table.getTableConfiguration().getConfigurations().entrySet()) {
				TableProcessor tableProcessor = (TableProcessor) entry.getKey().getProcessor();
				tableProcessor.process(entry, table.getTableConfiguration());
			}
			
			// Merging staging configuration into to the final configuration map
			table.getTableConfiguration().getConfigurations()
					.putAll(table.getTableConfiguration().getStagingConfiguration());
		}
		
		return table.getTableConfiguration().getConfigurations();
	}
	
	
	/**
	 * Hidden constructor.
	 */
	private TableConfig() {
	}
}