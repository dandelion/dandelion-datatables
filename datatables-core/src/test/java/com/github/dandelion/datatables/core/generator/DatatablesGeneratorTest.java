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
package com.github.dandelion.datatables.core.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.asset.generator.js.JsFunction;
import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.export.ReservedFormat;
import com.github.dandelion.datatables.core.extension.feature.PaginationType;
import com.github.dandelion.datatables.core.extension.feature.SortType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Callback;
import com.github.dandelion.datatables.core.option.CallbackType;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.Direction;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class DatatablesGeneratorTest {

	private static Set<String> displayTypeAllUsedForColumnDefinition = new HashSet<String>();
	private static Set<String> displayTypeHtmlUsedForColumnDefinition = new HashSet<String>();
	private static Set<String> displayTypeNotUsedForColumnDefinition = new HashSet<String>();
	private static Map<String, Object> defaultProperties = new HashMap<String, Object>();
	private HttpServletRequest request;
	private HttpServletResponse response;

	static {
		displayTypeAllUsedForColumnDefinition.add(ReservedFormat.ALL);
		displayTypeHtmlUsedForColumnDefinition.add(ReservedFormat.HTML);
		displayTypeNotUsedForColumnDefinition.add(ReservedFormat.CSV);
		displayTypeNotUsedForColumnDefinition.add(ReservedFormat.XML);
		displayTypeNotUsedForColumnDefinition.add(ReservedFormat.XLS);
		displayTypeNotUsedForColumnDefinition.add(ReservedFormat.XLSX);
		displayTypeNotUsedForColumnDefinition.add(ReservedFormat.PDF);

		defaultProperties.put(DTConstants.DT_S_DEFAULT_CONTENT, "");
	}

	private DatatableConfigGenerator generator;
	private HtmlTable table;
	private HtmlRow headerRow;
	private HtmlColumn firstColumn;

	@Before
	public void createMainGenerator() {
		request = new MockHttpServletRequest();
		request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));
		response = new MockHttpServletResponse();
		generator = new DatatableConfigGenerator();
	}

	@Before
	public void createTable() {
		table = new HtmlTable("aTable", request, response);
		table.getTableConfiguration().getConfigurations().clear();
		headerRow = table.addHeaderRow();
		firstColumn = headerRow.addHeaderColumn("firstColumn");
	}

	@Test
	public void should_have_default_values() {
		table = new HtmlTable("aTable", request, response);
		headerRow = table.addHeaderRow();

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(new ArrayList<Object>());
	}

	@Test
	public void should_not_generate_column_properties() {
		firstColumn.setEnabledDisplayTypes(displayTypeNotUsedForColumnDefinition);

		Map<String, Object> mainConf = generator.generateConfig(table);
		assertThat(mainConf).hasSize(1);
	}

	@Test
	public void should_generate_column_properties_with_ALL_display_type() {
		firstColumn.setEnabledDisplayTypes(displayTypeAllUsedForColumnDefinition);

		Map<String, Object> mainConf = generator.generateConfig(table);
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isNotEqualTo(new ArrayList<Object>());
	}

	@Test
	public void should_generate_column_properties_with_HTML_display_type() {
		firstColumn.setEnabledDisplayTypes(displayTypeHtmlUsedForColumnDefinition);

		Map<String, Object> mainConf = generator.generateConfig(table);
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isNotEqualTo(new ArrayList<Object>());
	}

	@Test
	public void should_generate_one_column_default_properties() {
		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		assertThat(columnsProperties.get(0)).isEqualTo(defaultProperties);
	}

	@Test
	public void should_generate_two_column_default_properties() {
		headerRow.addHeaderColumn("secondColumn");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(2);
		assertThat(columnsProperties.get(0)).isEqualTo(defaultProperties);
		assertThat(columnsProperties.get(1)).isEqualTo(defaultProperties);
	}

	@Test
	public void should_set_mData() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.PROPERTY, "aProperty");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_DATA, "aProperty");
		customProperties.put(DTConstants.DT_S_DEFAULT_CONTENT, "");
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_render_function() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.RENDERFUNCTION, "aRenderFunction");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_COLUMN_RENDERER, new JsSnippet("aRenderFunction"));
		customProperties.put(DTConstants.DT_S_DEFAULT_CONTENT, "");
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_default_content() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.DEFAULTVALUE, "aDefaultContent");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_S_DEFAULT_CONTENT, "aDefaultContent");
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_sort_direction() {
		List<Direction> sortDirections = new ArrayList<Direction>();
		sortDirections.add(Direction.DESC);
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTDIRECTION, sortDirections);

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		
		List<String> directions = new ArrayList<String>();
		for(Direction direction : sortDirections){
			directions.add(direction.value);
		}
		
		customProperties.put(DTConstants.DT_SORT_DIR, directions);
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_one_sort_init_direction() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITDIRECTION, "desc");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<List<Object>> columnsInitialSorting = (List<List<Object>>)mainConf.get(DTConstants.DT_SORT_INIT);
		assertThat(columnsInitialSorting).hasSize(1);
		assertThat(columnsInitialSorting.get(0)).containsExactly(0, "desc");
	}

	@Test
	public void should_set_one_sort_init_direction_sort_init_order() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITDIRECTION, "desc");
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITORDER, 1);
		HtmlColumn secondColumn = headerRow.addHeaderColumn("secondColumn");
		secondColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITDIRECTION, "asc");
		secondColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITORDER, 0);
		Set<String> enabledDisplayTypes = new HashSet<String>();
		enabledDisplayTypes.add(ReservedFormat.HTML);
		secondColumn.setEnabledDisplayTypes(enabledDisplayTypes);
		
		Map<String, Object> mainConf = generator.generateConfig(table);

		List<List<Object>> columnsInitialSorting = (List<List<Object>>)mainConf.get(DTConstants.DT_SORT_INIT);
		assertThat(columnsInitialSorting).hasSize(2);
		assertThat(columnsInitialSorting.get(0)).containsExactly(1, "asc");
		assertThat(columnsInitialSorting.get(1)).containsExactly(0, "desc");
	}
	
	@Test
	public void should_set_several_sort_init_directions() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITDIRECTION, "desc");
		headerRow.addHeaderColumn("secondColumn");
		HtmlColumn thirdColumn = headerRow.addHeaderColumn("thirdColumn");
		Set<String> enabledDisplayTypes = new HashSet<String>();
		enabledDisplayTypes.add(ReservedFormat.XLS);
		thirdColumn.setEnabledDisplayTypes(enabledDisplayTypes);
		HtmlColumn fourthColumn = headerRow.addHeaderColumn("fourthColumn");
		fourthColumn.getColumnConfiguration().addOption(DatatableOptions.SORTINITDIRECTION, "asc");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<List<Object>> columnsInitialSorting = (List<List<Object>>)mainConf.get(DTConstants.DT_SORT_INIT);
		assertThat(columnsInitialSorting).hasSize(2);
		assertThat(columnsInitialSorting.get(0)).containsExactly(0, "desc");
		assertThat(columnsInitialSorting.get(1)).containsExactly(2, "asc");
	}

	@Test
	public void should_set_sorting_type() {
		firstColumn.getColumnConfiguration().addOption(DatatableOptions.SORTTYPE, SortType.NATURAL.getName());

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_S_TYPE, "natural");
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}
	
	@Test
	public void should_set_auto_width() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_AUTOWIDTH, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_AUTO_WIDTH)).isEqualTo(true);
	}

	@Test
	public void should_set_defer_render() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_DEFERRENDER, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_DEFER_RENDER)).isEqualTo(true);
	}

	@Test
	public void should_set_filterable() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_FILTERABLE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_FILTER)).isEqualTo(true);
	}

	@Test
	public void should_set_info() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_INFO, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_INFO)).isEqualTo(true);
	}

	@Test
	public void should_set_paginate() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_PAGEABLE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_PAGINATE)).isEqualTo(true);
	}

	@Test
	public void should_set_display_length() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_DISPLAYLENGTH, 10);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_I_DISPLAY_LENGTH)).isEqualTo(10);
	}

	@Test
	public void should_set_length_change() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_LENGTHCHANGE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_LENGTH_CHANGE)).isEqualTo(true);
	}

	@Test
	public void should_set_pagination_type() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_PAGINATIONTYPE, PaginationType.INPUT);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_PAGINATION_TYPE)).isEqualTo(PaginationType.INPUT.toString());
	}

	@Test
	public void should_set_sort() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_SORTABLE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SORT)).isEqualTo(true);
	}

	@Test
	public void should_set_state_save() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_STATESAVE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_STATE_SAVE)).isEqualTo(true);
	}

	@Test
	public void should_set_jquery_ui() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_JQUERYUI, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_JQUERYUI)).isEqualTo(true);
	}

	@Test
	public void should_set_length_menu() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_LENGTHMENU, "[[100px],[200px]]");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_A_LENGTH_MENU)).isEqualTo(new JsSnippet("[[100px],[200px]]"));
	}

	@Test
	public void should_set_stripe_classes() {
		table.getTableConfiguration().addOption(DatatableOptions.CSS_STRIPECLASSES, "['oddClass','evenClass']");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_AS_STRIPE_CLASSES)).isEqualTo(new JsSnippet("['oddClass','evenClass']"));
	}

	@Test
	public void should_set_scroll_y() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_SCROLLY, "100px");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLY)).isEqualTo("100px");
	}

	@Test
	public void should_set_scroll_collapse() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_SCROLLCOLLAPSE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLCOLLAPSE)).isEqualTo(true);
	}

	@Test
	public void should_set_scroll_x() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_SCROLLX, "100%");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLX)).isEqualTo("100%");
	}

	@Test
	public void should_set_scroll_inner() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_SCROLLXINNER, "110%");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLXINNER)).isEqualTo("110%");
	}

	@Test
	public void should_set_processing() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_PROCESSING, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_B_PROCESSING)).isEqualTo(true);
	}

	@Test
	public void should_ignore_ajax_properties_if_server_side_not_enabled() {
		// TODO : should server side properties be triggered by server side
		// boolean definition ?
		// table.setServerSide(false);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SOURCE, "aUrl");
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERDATA, "someServerData");
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERPARAM, "someServerParam");
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERMETHOD, "GET");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(1);
	}

	@Test
	public void should_set_server_side() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERSIDE, true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_B_SERVER_SIDE)).isEqualTo(true);
	}

	@Test
	public void should_set_server_side_and_datasource_url() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERSIDE, true);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SOURCE, "aUrl");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_S_AJAX_SOURCE)).isEqualTo("aUrl");
	}

	@Test
	public void should_set_server_side_and_server_data_url() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERSIDE, true);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERDATA, "someServerData");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_FN_SERVERDATA)).isEqualTo(new JsSnippet("someServerData"));
	}

	@Test
	public void should_set_server_side_and_server_param_url() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERSIDE, true);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERPARAM, "someServerParam");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_FN_SERVERPARAMS)).isEqualTo(new JsSnippet("someServerParam"));
	}

	@Test
	public void should_set_server_side_and_server_method_url() {
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERSIDE, true);
		table.getTableConfiguration().addOption(DatatableOptions.AJAX_SERVERMETHOD, "GET");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_S_SERVERMETHOD)).isEqualTo("GET");
	}

	@Test
	public void should_set_a_callback() {
		Callback callback = new Callback(CallbackType.CREATEDROW, "aJsFunction");
		List<Callback> callbacks = new ArrayList<Callback>();
		callbacks.add(callback);
		table.getTableConfiguration().setCallbacks(callbacks);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(CallbackType.CREATEDROW.getName())).isEqualTo(new JsFunction(callback.getFunction().getCode(), CallbackType.CREATEDROW.getArgs()));
	}

	@Test
	public void should_set_several_callbacks() {
		List<Callback> callbacks = new ArrayList<Callback>();
		Callback callback = new Callback(CallbackType.CREATEDROW, "aJsFunction");
		callbacks.add(callback);
		Callback callback2 = new Callback(CallbackType.COOKIE, "anotherJsFunction");
		callbacks.add(callback2);
		Callback callback3 = new Callback(CallbackType.PREDRAW, "aThirdJsFunction");
		callbacks.add(callback3);
		table.getTableConfiguration().setCallbacks(callbacks);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(4);
		assertThat(mainConf.get(CallbackType.CREATEDROW.getName())).isEqualTo(new JsFunction(callback.getFunction().getCode(), CallbackType.CREATEDROW.getArgs()));
		assertThat(mainConf.get(CallbackType.COOKIE.getName())).isEqualTo(new JsFunction(callback2.getFunction().getCode(), CallbackType.COOKIE.getArgs()));
		assertThat(mainConf.get(CallbackType.PREDRAW.getName())).isEqualTo(new JsFunction(callback3.getFunction().getCode(), CallbackType.PREDRAW.getArgs()));
	}
	
	@Test
	public void should_set_dom() {
		table.getTableConfiguration().addOption(DatatableOptions.FEATURE_DOM, "aDom");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_DOM)).isEqualTo("aDom");
	}
	
	@Test
	public void should_generate_normal_messages() {
		Properties messages = new Properties();
		messages.put(DTMessages.INFO.getPropertyName(), "message value");
		table.getTableConfiguration().setMessages(messages);

		Map<String, Object> mainConf = generator.generateConfig(table);

		Map<String,String> languageValue = new HashMap<String, String>();
		languageValue.put(DTMessages.INFO.getRealName(), "message value");

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_LANGUAGE)).isEqualTo(languageValue);
	}
	
	@Test
	public void should_generate_paginate_messages() {
		Properties messages = new Properties();
		messages.put(DTMessages.PAGINATE_FIRST.getPropertyName(), "First");
		table.getTableConfiguration().setMessages(messages);

		Map<String, Object> mainConf = generator.generateConfig(table);

		Map<String, String> languageValue = new HashMap<String, String>();
		languageValue.put(DTMessages.PAGINATE_FIRST.getRealName(), "First");

		Map<String, Object> paginateMap = new HashMap<String, Object>();
		paginateMap.put(DTMessages.PAGINATE.getRealName(), languageValue);
		
		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_LANGUAGE)).isEqualTo(paginateMap);
	}
	
	@Test
	public void should_generate_aria_messages() {
		Properties messages = new Properties();
		messages.put(DTMessages.ARIA_SORT_ASC.getPropertyName(), "Value");
		table.getTableConfiguration().setMessages(messages);

		Map<String, Object> mainConf = generator.generateConfig(table);

		Map<String, String> languageValue = new HashMap<String, String>();
		languageValue.put(DTMessages.ARIA_SORT_ASC.getRealName(), "Value");

		Map<String, Object> ariaMap = new HashMap<String, Object>();
		ariaMap.put(DTMessages.ARIA.getRealName(), languageValue);
		
		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_LANGUAGE)).isEqualTo(ariaMap);
	}
}