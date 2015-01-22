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
package com.github.dandelion.datatables.core.option;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.generator.YadcfConfigGenerator.FilterType;
import com.github.dandelion.datatables.core.option.processor.BooleanProcessor;
import com.github.dandelion.datatables.core.option.processor.EmptyStringProcessor;
import com.github.dandelion.datatables.core.option.processor.IntegerProcessor;
import com.github.dandelion.datatables.core.option.processor.StringBuilderProcessor;
import com.github.dandelion.datatables.core.option.processor.StringProcessor;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxPipeliningProcessor;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxReloadFunctionProcessor;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxReloadSelectorProcessor;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxServerSideProcessor;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxSourceProcessor;
import com.github.dandelion.datatables.core.option.processor.column.FilterTypeProcessor;
import com.github.dandelion.datatables.core.option.processor.column.FilterableProcessor;
import com.github.dandelion.datatables.core.option.processor.column.SortDirectionProcessor;
import com.github.dandelion.datatables.core.option.processor.column.SortTypeProcessor;
import com.github.dandelion.datatables.core.option.processor.css.CssStripeClassesProcessor;
import com.github.dandelion.datatables.core.option.processor.css.CssThemeOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.css.CssThemeProcessor;
import com.github.dandelion.datatables.core.option.processor.export.ExportEnabledFormatProcessor;
import com.github.dandelion.datatables.core.option.processor.export.ExportFormatProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeatureAppearProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeatureFilterPlaceholderProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeatureFilterSelectorProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeatureLengthMenuProcessor;
import com.github.dandelion.datatables.core.option.processor.feature.FeaturePaginationTypeProcessor;
import com.github.dandelion.datatables.core.option.processor.i18n.MessageProcessor;
import com.github.dandelion.datatables.core.option.processor.main.MainExtensionNamesProcessor;

/**
 * <p>
 * Registry of all possible {@link Option}s for configuring either a
 * {@link TableConfiguration} or a {@link ColumnConfiguration} instance.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.11.0
 */
public final class DatatableOptions {

	/**
	 * Map that associates each {@link Option} with its name in order to
	 * facilitate the search for an {@link Option}. See
	 * {@link #findByName(String)}.
	 */
	private static final Map<String, Option<?>> OPTIONS_BY_NAME;

	/**
	 * All TableConfiguration-related options.
	 */
	public static final Option<Set<String>> MAIN_EXTENSION_NAMES = new Option<Set<String>>("main.extension.names",
			new MainExtensionNamesProcessor(), 100);

	public static final Option<StringBuilder> CSS_CLASS = new Option<StringBuilder>("css.class",
			new StringBuilderProcessor(), 100);
	public static final Option<StringBuilder> CSS_STYLE = new Option<StringBuilder>("css.style",
			new StringBuilderProcessor(), 100);
	public static final Option<String> CSS_STRIPECLASSES = new Option<String>("css.stripeClasses",
			new CssStripeClassesProcessor(), 100);
	public static final Option<Extension> CSS_THEME = new Option<Extension>("css.theme", new CssThemeProcessor(), 100);
	public static final Option<ThemeOption> CSS_THEMEOPTION = new Option<ThemeOption>("css.themeOption",
			new CssThemeOptionProcessor(), 100);

	public static final Option<Boolean> FEATURE_INFO = new Option<Boolean>("feature.info", new BooleanProcessor(), 100);
	public static final Option<Boolean> FEATURE_AUTOWIDTH = new Option<Boolean>("feature.autoWidth",
			new BooleanProcessor(), 100);
	public static final Option<Boolean> FEATURE_FILTERABLE = new Option<Boolean>("feature.filterable",
			new BooleanProcessor(), 100);
	public static final Option<FilterPlaceholder> FEATURE_FILTER_PLACEHOLDER = new Option<FilterPlaceholder>(
			"feature.filterPlaceHolder", new FeatureFilterPlaceholderProcessor(), 100);
	public static final Option<Integer> FEATURE_FILTER_DELAY = new Option<Integer>("feature.filterDelay",
			new IntegerProcessor(), 100);
	public static final Option<String> FEATURE_FILTER_SELECTOR = new Option<String>("feature.filterSelector",
			new FeatureFilterSelectorProcessor(), 100);
	public static final Option<String> FEATURE_FILTER_CLEAR_SELECTOR = new Option<String>(
			"feature.filterClearSelector", new StringProcessor(), 100);
	public static final Option<String> FEATURE_FILTER_TRIGGER = new Option<String>("feature.filterTrigger",
			new StringProcessor(), 100);
	public static final Option<Boolean> FEATURE_PAGEABLE = new Option<Boolean>("feature.pageable",
			new BooleanProcessor(), 100);
	public static final Option<PaginationType> FEATURE_PAGINATIONTYPE = new Option<PaginationType>(
			"feature.paginationType", new FeaturePaginationTypeProcessor(), 100);
	public static final Option<Boolean> FEATURE_LENGTHCHANGE = new Option<Boolean>("feature.lengthChange",
			new BooleanProcessor(), 100);
	public static final Option<Boolean> FEATURE_SORTABLE = new Option<Boolean>("feature.sortable",
			new BooleanProcessor(), 100);
	public static final Option<Boolean> FEATURE_STATESAVE = new Option<Boolean>("feature.stateSave",
			new BooleanProcessor(), 100);
	public static final Option<Boolean> FEATURE_JQUERYUI = new Option<Boolean>("feature.jqueryUi",
			new BooleanProcessor(), 100);
	public static final Option<String> FEATURE_LENGTHMENU = new Option<String>("feature.lengthMenu",
			new FeatureLengthMenuProcessor(), 100);
	public static final Option<Integer> FEATURE_DISPLAYLENGTH = new Option<Integer>("feature.displayLength",
			new IntegerProcessor(), 100);
	public static final Option<String> FEATURE_DOM = new Option<String>("feature.dom", new StringProcessor(), 100);
	public static final Option<String> FEATURE_SCROLLY = new Option<String>("feature.scrollY", new StringProcessor(),
			100);
	public static final Option<Boolean> FEATURE_SCROLLCOLLAPSE = new Option<Boolean>("feature.scrollCollapse",
			new BooleanProcessor(), 100);
	public static final Option<String> FEATURE_SCROLLX = new Option<String>("feature.scrollX", new StringProcessor(),
			100);
	public static final Option<String> FEATURE_SCROLLXINNER = new Option<String>("feature.scrollXInner",
			new StringProcessor(), 100);
	public static final Option<String> FEATURE_APPEAR = new Option<String>("feature.appear",
			new FeatureAppearProcessor(), 100);
	public static final Option<String> FEATURE_APPEAR_DURATION = new Option<String>("feature.appearDuration", null, 100);
	public static final Option<Boolean> FEATURE_PROCESSING = new Option<Boolean>("feature.processing",
			new BooleanProcessor(), 100);

	public static final Option<Boolean> AJAX_DEFERRENDER = new Option<Boolean>("ajax.deferRender",
			new BooleanProcessor(), 100);
	public static final Option<String> AJAX_SOURCE = new Option<String>("ajax.source", new AjaxSourceProcessor(), 100);
	public static final Option<String> AJAX_PARAMS = new Option<String>("ajax.params",
			new StringProcessor(true), 98);
	public static final Option<Boolean> AJAX_SERVERSIDE = new Option<Boolean>("ajax.serverSide",
			new AjaxServerSideProcessor(), 99);
	public static final Option<Boolean> AJAX_PIPELINING = new Option<Boolean>("ajax.pipelining",
			new AjaxPipeliningProcessor(), 101);
	public static final Option<Integer> AJAX_PIPESIZE = new Option<Integer>("ajax.pipeSize", new IntegerProcessor(),
			100);
	public static final Option<String> AJAX_RELOAD_SELECTOR = new Option<String>("ajax.reloadSelector",
			new AjaxReloadSelectorProcessor(), 100);
	public static final Option<String> AJAX_RELOAD_FUNCTION = new Option<String>("ajax.reloadFunction",
			new AjaxReloadFunctionProcessor(true), 100);
	
	public static final Option<String> PLUGIN_FIXEDPOSITION = new Option<String>("plugin.fixedPosition",
			new StringProcessor(), 100);
	public static final Option<Integer> PLUGIN_FIXEDOFFSETTOP = new Option<Integer>("plugin.fixedOffsetTop",
			new IntegerProcessor(), 100);

	public static final Option<Set<ExportConf>> EXPORT_ENABLED_FORMATS = new Option<Set<ExportConf>>(
			"export.enabled.formats", new ExportEnabledFormatProcessor(), 100);
	public static final Option<String> EXPORT_CONTAINER_STYLE = new Option<String>("export.container.style",
			new StringProcessor(), 100);
	public static final Option<String> EXPORT_CONTAINER_CLASS = new Option<String>("export.container.class",
			new StringProcessor(), 100);
	public static final Option<String> EXPORT_CLASS = new Option<String>(ExportFormatProcessor.REGEX_EXPORT_CLASS,
			new ExportFormatProcessor(), 100);
	public static final Option<String> EXPORT_LABEL = new Option<String>(ExportFormatProcessor.REGEX_EXPORT_LABEL,
			new ExportFormatProcessor(), 100);
	public static final Option<String> EXPORT_FILENAME = new Option<String>(
			ExportFormatProcessor.REGEX_EXPORT_FILENAME, new ExportFormatProcessor(), 100);
	public static final Option<String> EXPORT_MIMETYPE = new Option<String>(
			ExportFormatProcessor.REGEX_EXPORT_MIMETYPE, new ExportFormatProcessor(), 100);

	public static final Option<String> I18N_MSG_PROCESSING = new Option<String>("i18n.msg.processing",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_SEARCH = new Option<String>("i18n.msg.search", new MessageProcessor(),
			100);
	public static final Option<String> I18N_MSG_LENGTHMENU = new Option<String>("i18n.msg.lengthmenu",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_INFO = new Option<String>("i18n.msg.info", new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_INFOEMPTY = new Option<String>("i18n.msg.info.empty",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_INFOFILTERED = new Option<String>("i18n.msg.info.filtered",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_INFOPOSTFIX = new Option<String>("i18n.msg.info.postfix",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_LOADINGRECORDS = new Option<String>("i18n.msg.loadingrecords",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_ZERORECORDS = new Option<String>("i18n.msg.zerorecords",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_EMPTYTABLE = new Option<String>("i18n.msg.emptytable",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_PAGINATE_FIRST = new Option<String>("i18n.msg.paginate.first",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_PAGINATE_PREVIOUS = new Option<String>("i18n.msg.paginate.previous",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_PAGINATE_NEXT = new Option<String>("i18n.msg.paginate.next",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_PAGINATE_LAST = new Option<String>("i18n.msg.paginate.last",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_ARIA_SORTASC = new Option<String>("i18n.msg.aria.sortasc",
			new MessageProcessor(), 100);
	public static final Option<String> I18N_MSG_ARIA_SORTDESC = new Option<String>("i18n.msg.aria.sortdesc",
			new MessageProcessor(), 100);

	public static final Option<String> INTERNAL_OBJECTTYPE = new Option<String>("internal.objectType",
			new StringProcessor(), 100);

	/**
	 * All ColumnConfiguration-related options.
	 */
	public static final Option<String> ID = new Option<String>("id", new StringProcessor(), 100);
	public static final Option<String> TITLE = new Option<String>("title", new StringProcessor(), 100);
	public static final Option<String> TITLEKEY = new Option<String>("titleKey", new StringProcessor(), 100);
	public static final Option<String> NAME = new Option<String>("name", new StringProcessor(), 100);
	public static final Option<String> PROPERTY = new Option<String>("property", new StringProcessor(), 100);
	public static final Option<String> DEFAULTVALUE = new Option<String>("defaultValue", new EmptyStringProcessor(),
			100);
	public static final Option<StringBuilder> CSSSTYLE = new Option<StringBuilder>("cssStyle",
			new StringBuilderProcessor(), 100);
	public static final Option<StringBuilder> CSSCELLSTYLE = new Option<StringBuilder>("cssCellStyle",
			new StringBuilderProcessor(), 100);
	public static final Option<StringBuilder> CSSCLASS = new Option<StringBuilder>("cssClass",
			new StringBuilderProcessor(), 100);
	public static final Option<StringBuilder> CSSCELLCLASS = new Option<StringBuilder>("cssCellClass",
			new StringBuilderProcessor(), 100);
	public static final Option<Boolean> SORTABLE = new Option<Boolean>("sortable", new BooleanProcessor(), 100);
	public static final Option<List<Direction>> SORTDIRECTION = new Option<List<Direction>>("sortDirection",
			new SortDirectionProcessor(), 100);
	public static final Option<String> SORTINITDIRECTION = new Option<String>("sortInitDirection",
			new StringProcessor(), 100);
	public static final Option<Integer> SORTINITORDER = new Option<Integer>("sortInitOrder", new IntegerProcessor(),
			100);
	public static final Option<String> SORTTYPE = new Option<String>("sortType", new SortTypeProcessor(true), 100);
	public static final Option<Boolean> FILTERABLE = new Option<Boolean>("filterable", new FilterableProcessor(), 100);
	public static final Option<Boolean> SEARCHABLE = new Option<Boolean>("searchable", new BooleanProcessor(), 100);
	public static final Option<Boolean> VISIBLE = new Option<Boolean>("visible", new BooleanProcessor(), 100);
	public static final Option<FilterType> FILTERTYPE = new Option<FilterType>("filterType", new FilterTypeProcessor(),
			100);
	public static final Option<String> FILTERVALUES = new Option<String>("filterValues", new StringProcessor(true), 100);
	public static final Option<String> FILTERPLACEHOLDER = new Option<String>("filterPlaceholder",
			new StringProcessor(), 100);
	public static final Option<String> RENDERFUNCTION = new Option<String>("renderFunction", new StringProcessor(true),
			100);
	public static final Option<String> SELECTOR = new Option<String>("selector", new StringProcessor(), 100);

	/**
	 * <p>
	 * Searches for the {@link Option} corresponding to the provided
	 * {@code optionName}.
	 * </p>
	 * 
	 * @param optionName
	 *            The normalized name of the {@link Option} to search for.
	 * @return the corresponding {@link Option}.
	 */
	public static Option<?> findByName(String optionName) {

		String normalizedKey = optionName.trim().toLowerCase();
		if (OPTIONS_BY_NAME.containsKey(normalizedKey)) {
			return OPTIONS_BY_NAME.get(normalizedKey);
		}
		else if (optionName.contains("export")) {
			Option<?> option = null;

			Pattern patternExportClass = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_CLASS,
					Pattern.CASE_INSENSITIVE);
			if (patternExportClass.matcher(optionName).find()) {
				option = EXPORT_CLASS;
				option.setUserName(optionName);
				return option;
			}

			Pattern patternExportFilename = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_FILENAME,
					Pattern.CASE_INSENSITIVE);
			if (patternExportFilename.matcher(optionName).find()) {
				option = EXPORT_FILENAME;
				option.setUserName(optionName);
				return option;
			}

			Pattern patternExportLabel = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_LABEL,
					Pattern.CASE_INSENSITIVE);
			if (patternExportLabel.matcher(optionName).find()) {
				option = EXPORT_LABEL;
				option.setUserName(optionName);
				return option;
			}

			Pattern patternExportMimetype = Pattern.compile(ExportFormatProcessor.REGEX_EXPORT_MIMETYPE,
					Pattern.CASE_INSENSITIVE);
			if (patternExportMimetype.matcher(optionName).find()) {
				option = EXPORT_MIMETYPE;
				option.setUserName(optionName);
				return option;
			}
		}

		return null;
	}

	/**
	 * <p>
	 * Initializes the {@code internalConf} map to simplify the search of
	 * options. All option names are normalized before being stored.
	 * </p>
	 */
	static {

		OPTIONS_BY_NAME = new HashMap<String, Option<?>>();

		Field[] fields = DatatableOptions.class.getDeclaredFields();

		// Filter only Options
		for (Field field : fields) {
			if (field.getType() == Option.class) {
				try {
					Option<?> ct = (Option<?>) field.get(null);
					OPTIONS_BY_NAME.put(ct.getName().trim().toLowerCase(), ct);
				}
				catch (Exception e) {
					// Should never happen
				}
			}
		}
	}
}