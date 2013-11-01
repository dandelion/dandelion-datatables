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

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.asset.JavascriptSnippet;
import com.github.dandelion.datatables.core.constants.DTConstants;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.generator.configuration.ColumnFilteringGenerator;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Unit test for the Column Filtering generator.
 *
 * @author Thibault Duchateau
 */
public class ColumnFilteringGeneratorTest {

	private MockServletContext mockServletContext;
	private MockPageContext mockPageContext;
	
	private ColumnFilteringGenerator generator;
	private HtmlTable table;
	private HtmlRow headerRow;
	private HtmlColumn firstColumn;

	@Before
	public void createMainGenerator() {
		mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		generator = new ColumnFilteringGenerator();
	}

	@Before
	public void createTable() {
		table = new HtmlTable("aTable", (HttpServletRequest) mockPageContext.getRequest());
		headerRow = table.addHeaderRow();
		firstColumn = headerRow.addHeaderColumn("firstColumn");
	}
	
	@Test
	public void should_not_generate_filter_conf_when_using_pdf_displaytype() {
		Set<DisplayType> displayTypes = new HashSet<DisplayType>();
		displayTypes.add(DisplayType.PDF);
		firstColumn.setEnabledDisplayTypes(displayTypes);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(new ArrayList<Object>());
	}
	
	@Test
	public void should_generate_filter_conf_when_using_html_displaytype() {
		Set<DisplayType> displayTypes = new HashSet<DisplayType>();
		displayTypes.add(DisplayType.HTML);
		firstColumn.setEnabledDisplayTypes(displayTypes);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "null");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filter_placeholder() {
		table.getTableConfiguration().setFeatureFilterPlaceholder(FilterPlaceholder.FOOT);

		Map<String, Object> mainConf = generator.generateConfig(table);

		assertThat(mainConf).hasSize(2);
		assertThat(mainConf.get(DTConstants.DT_S_PLACEHOLDER)).isEqualTo("foot");
	}
	
	@Test
	public void should_generate_filtertype_text_by_default() {
		firstColumn.getColumnConfiguration().setFilterable(true);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "text");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filtertype_number() {
		firstColumn.getColumnConfiguration().setFilterable(true);
		firstColumn.getColumnConfiguration().setFilterType(FilterType.NUMBER);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "number");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filtertype_select() {
		firstColumn.getColumnConfiguration().setFilterable(true);
		firstColumn.getColumnConfiguration().setFilterType(FilterType.SELECT);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "select");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filtertype_number_range() {
		firstColumn.getColumnConfiguration().setFilterable(true);
		firstColumn.getColumnConfiguration().setFilterType(FilterType.NUMBER_RANGE);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "number-range");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filtertype_text() {
		firstColumn.getColumnConfiguration().setFilterable(true);
		firstColumn.getColumnConfiguration().setFilterType(FilterType.INPUT);

		Map<String, Object> mainConf = generator.generateConfig(table);
		
		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_TYPE, "text");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filter_selector() {
		firstColumn.getColumnConfiguration().setSelector("mySelector");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_S_SELECTOR, "mySelector");
		conf.put(DTConstants.DT_FILTER_TYPE, "null");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
	
	@Test
	public void should_generate_filter_values() {
		firstColumn.getColumnConfiguration().setFilterValues("myValues");

		Map<String, Object> mainConf = generator.generateConfig(table);

		List<Map<String, Object>> aoColumnsContent = new ArrayList<Map<String, Object>>();
		Map<String, Object> conf = new HashMap<String, Object>();
		
		conf.put(DTConstants.DT_FILTER_VALUES, new JavascriptSnippet("myValues"));
		conf.put(DTConstants.DT_FILTER_TYPE, "null");
		aoColumnsContent.add(conf);
		
		assertThat(mainConf).hasSize(1);
		assertThat(mainConf.get(DTConstants.DT_AOCOLUMNS)).isEqualTo(aoColumnsContent);
	}
}