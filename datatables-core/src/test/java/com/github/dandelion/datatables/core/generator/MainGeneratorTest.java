package com.github.dandelion.datatables.core.generator;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.asset.JavascriptFunction;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.callback.Callback;
import com.github.dandelion.datatables.core.callback.CallbackType;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.feature.PaginationType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;

@SuppressWarnings("unchecked")
public class MainGeneratorTest {

	private static List<DisplayType> displayTypeAllUsedForColumnDefinition = new ArrayList<DisplayType>();
	private static List<DisplayType> displayTypeHtmlUsedForColumnDefinition = new ArrayList<DisplayType>();
	private static List<DisplayType> displayTypeNotUsedForColumnDefinition = new ArrayList<DisplayType>();
	private static Map<String, Object> defaultProperties = new HashMap<String, Object>();
	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;

	static {
		displayTypeAllUsedForColumnDefinition.add(DisplayType.ALL);
		displayTypeHtmlUsedForColumnDefinition.add(DisplayType.HTML);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.CSV);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.XML);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.XLS);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.XLSX);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.PDF);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.RTF);
		displayTypeNotUsedForColumnDefinition.add(DisplayType.JSON);

		defaultProperties.put(DTConstants.DT_VISIBLE, true);
		defaultProperties.put(DTConstants.DT_SEARCHABLE, true);
		defaultProperties.put(DTConstants.DT_SORTABLE, true);
	}

	private MainGenerator generator;
	private HtmlTable table;
	private HtmlRow headerRow;
	private HtmlColumn firstColumn;

	@Before
	public void createMainGenerator() {
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		generator = new MainGenerator();
	}

	@Before
	public void createTable() {
		table = new HtmlTable("aTable", (HttpServletRequest) mockPageContext.getRequest());
		headerRow = table.addHeaderRow();
		firstColumn = headerRow.addColumn("firstColumn");
	}

	@Test
	public void should_have_default_values() {
		table = new HtmlTable("aTable", (HttpServletRequest) mockPageContext.getRequest());
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
		headerRow.addColumn("secondColumn");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(2);
		assertThat(columnsProperties.get(0)).isEqualTo(defaultProperties);
		assertThat(columnsProperties.get(1)).isEqualTo(defaultProperties);
	}

	@Test
	public void should_override_searchable_if_column_is_not_visible() {
		firstColumn.setSearchable(true);
		firstColumn.setVisible(false);

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_VISIBLE, false);
		customProperties.put(DTConstants.DT_SEARCHABLE, false);
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_mData() {
		firstColumn.setProperty("aProperty");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_DATA, "aProperty");
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_render_function() {
		firstColumn.setRenderFunction("aRenderFunction");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_COLUMN_RENDERER, new JavascriptSnippet("aRenderFunction"));
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_default_content() {
		firstColumn.setDefaultValue("aDefaultContent");

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
		ArrayList<String> sortDirections = new ArrayList<String>();
		sortDirections.add("asc");
		firstColumn.setSortDirections(sortDirections);

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> columnsProperties = (List<Map<String, Object>>)mainConf.get(DTConstants.DT_AOCOLUMNS);
		assertThat(columnsProperties).hasSize(1);
		Map<String, Object> firstColumnProperties = columnsProperties.get(0);
		Map<String, Object> customProperties = new HashMap<String, Object>(defaultProperties);
		customProperties.put(DTConstants.DT_SORT_DIR, sortDirections);
		assertThat(firstColumnProperties).isEqualTo(customProperties);
	}

	@Test
	public void should_set_one_sort_direction_init() {
		firstColumn.setSortInit("desc");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<List<Object>> columnsInitialSorting = (List<List<Object>>)mainConf.get(DTConstants.DT_SORT_INIT);
		assertThat(columnsInitialSorting).hasSize(1);
		assertThat(columnsInitialSorting.get(0)).containsExactly(0, "desc");
	}

	@Test
	public void should_set_several_sort_direction_inits() {
		firstColumn.setSortInit("desc");
		headerRow.addColumn("secondColumn");
		HtmlColumn thirdColumn = headerRow.addColumn("thirdColumn");
		thirdColumn.setSortInit("asc");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<List<Object>> columnsInitialSorting = (List<List<Object>>)mainConf.get(DTConstants.DT_SORT_INIT);
		assertThat(columnsInitialSorting).hasSize(2);
		assertThat(columnsInitialSorting.get(0)).containsExactly(0, "desc");
		assertThat(columnsInitialSorting.get(1)).containsExactly(2, "asc");
	}

	@Test
	public void should_set_label() {
		table.getTableConfiguration().setExtraLabels("FR_fr");

		Map<String, Object> mainConf = generator.generateConfig(table);

		Map<String, Object> languageLabels = (Map<String, Object>)mainConf.get(DTConstants.DT_LANGUAGE);
		assertThat(languageLabels).hasSize(1);
		assertThat(languageLabels.get(DTConstants.DT_URL)).isEqualTo("FR_fr");
	}

	@Test
	public void should_set_auto_width() {
		table.getTableConfiguration().setFeatureAutoWidth(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_AUTO_WIDTH)).isEqualTo(true);
	}

	@Test
	public void should_set_defer_render() {
		table.getTableConfiguration().setAjaxDeferRender(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_DEFER_RENDER)).isEqualTo(true);
	}

	@Test
	public void should_set_filterable() {
		table.getTableConfiguration().setFeatureFilterable(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_FILTER)).isEqualTo(true);
	}

	@Test
	public void should_set_info() {
		table.getTableConfiguration().setFeatureInfo(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_INFO)).isEqualTo(true);
	}

	@Test
	public void should_set_paginate() {
		table.getTableConfiguration().setFeaturePaginate(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_PAGINATE)).isEqualTo(true);
	}

	@Test
	public void should_set_display_length() {
		table.getTableConfiguration().setFeatureDisplayLength(10);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_I_DISPLAY_LENGTH)).isEqualTo(10);
	}

	@Test
	public void should_set_length_change() {
		table.getTableConfiguration().setFeatureLengthChange(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_LENGTH_CHANGE)).isEqualTo(true);
	}

	@Test
	public void should_set_pagination_type() {
		table.getTableConfiguration().setFeaturePaginationType(PaginationType.INPUT);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_PAGINATION_TYPE)).isEqualTo(PaginationType.INPUT.toString());
	}

	@Test
	public void should_set_sort() {
		table.getTableConfiguration().setFeatureSort(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SORT)).isEqualTo(true);
	}

	@Test
	public void should_set_state_save() {
		table.getTableConfiguration().setFeatureStateSave(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_STATE_SAVE)).isEqualTo(true);
	}

	@Test
	public void should_set_jquery_ui() {
		table.getTableConfiguration().setFeatureJqueryUi(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_JQUERYUI)).isEqualTo(true);
	}

	@Test
	public void should_set_length_menu() {
		table.getTableConfiguration().setFeatureLengthMenu("[[100px],[200px]]");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_A_LENGTH_MENU)).isEqualTo(new JavascriptSnippet("[[100px],[200px]]"));
	}

	@Test
	public void should_set_stripe_classes() {
		table.getTableConfiguration().setCssStripeClasses("['oddClass','evenClass']");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_AS_STRIPE_CLASSES)).isEqualTo(new JavascriptSnippet("['oddClass','evenClass']"));
	}

	@Test
	public void should_set_scroll_y() {
		table.getTableConfiguration().setFeatureScrolly("100px");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLY)).isEqualTo("100px");
	}

	@Test
	public void should_set_scroll_collapse() {
		table.getTableConfiguration().setFeatureScrollCollapse(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_SCROLLCOLLAPSE)).isEqualTo(true);
	}

	@Test
	public void should_set_processing() {
		table.getTableConfiguration().setAjaxProcessing(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_B_PROCESSING)).isEqualTo(true);
	}

	@Test
	public void should_ignore_ajax_properties_if_server_side_not_enabled() {
		// TODO : should server side properties be triggered by server side
		// boolean definition ?
		// table.setServerSide(false);
		table.getTableConfiguration().setAjaxSource("aUrl");
		table.getTableConfiguration().setAjaxServerData("someServerData");
		table.getTableConfiguration().setAjaxServerParam("someServerParam");
		table.getTableConfiguration().setAjaxServerMethod("GET");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(1);
	}

	@Test
	public void should_set_server_side() {
		table.getTableConfiguration().setAjaxServerSide(true);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_B_SERVER_SIDE)).isEqualTo(true);
	}

	@Test
	public void should_set_server_side_and_datasource_url() {
		table.getTableConfiguration().setAjaxServerSide(true);
		table.getTableConfiguration().setAjaxSource("aUrl");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_S_AJAX_SOURCE)).isEqualTo("aUrl");
	}

	@Test
	public void should_set_server_side_and_server_data_url() {
		table.getTableConfiguration().setAjaxServerSide(true);
		table.getTableConfiguration().setAjaxServerData("someServerData");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_FN_SERVERDATA)).isEqualTo(new JavascriptSnippet("someServerData"));
	}

	@Test
	public void should_set_server_side_and_server_param_url() {
		table.getTableConfiguration().setAjaxServerSide(true);
		table.getTableConfiguration().setAjaxServerParam("someServerParam");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_FN_SERVERPARAMS)).isEqualTo(new JavascriptSnippet("someServerParam"));
	}

	@Test
	public void should_set_server_side_and_server_method_url() {
		table.getTableConfiguration().setAjaxServerSide(true);
		table.getTableConfiguration().setAjaxServerMethod("GET");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(3);
		assertThat(mainConf.get(DTConstants.DT_S_SERVERMETHOD)).isEqualTo("GET");
	}

	@Test
	public void should_set_a_callback() {
		Callback callback = new Callback(CallbackType.CREATEDROW, "aJavascriptFunction");
		List<Callback> callbacks = new ArrayList<Callback>();
		callbacks.add(callback);
		table.getTableConfiguration().setCallbacks(callbacks);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(CallbackType.CREATEDROW.getName())).isEqualTo(new JavascriptFunction(callback.getFunction(), CallbackType.CREATEDROW.getArgs()));
	}

	@Test
	public void should_set_several_callbacks() {
		List<Callback> callbacks = new ArrayList<Callback>();
		Callback callback = new Callback(CallbackType.CREATEDROW, "aJavascriptFunction");
		callbacks.add(callback);
		Callback callback2 = new Callback(CallbackType.COOKIE, "anotherJavascriptFunction");
		callbacks.add(callback2);
		Callback callback3 = new Callback(CallbackType.PREDRAW, "aThirdJavascriptFunction");
		callbacks.add(callback3);
		table.getTableConfiguration().setCallbacks(callbacks);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(4);
		assertThat(mainConf.get(CallbackType.CREATEDROW.getName())).isEqualTo(new JavascriptFunction(callback.getFunction(), CallbackType.CREATEDROW.getArgs()));
		assertThat(mainConf.get(CallbackType.COOKIE.getName())).isEqualTo(new JavascriptFunction(callback2.getFunction(), CallbackType.COOKIE.getArgs()));
		assertThat(mainConf.get(CallbackType.PREDRAW.getName())).isEqualTo(new JavascriptFunction(callback3.getFunction(), CallbackType.PREDRAW.getArgs()));
	}
	
	@Test
	public void should_set_dom() {
		table.getTableConfiguration().setFeatureDom("aDom");

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_DOM)).isEqualTo("aDom");
	}
}