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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.core.option.Option;
import com.github.dandelion.datatables.core.config.ConfigLoader;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class StandardConfigurationLoaderTest {

	HttpServletRequest request;
	MockPageContext mockPageContext;
	ConfigLoader loader;
	
	@Before
	public void setup() {
		MockServletContext mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		loader = new ConfigLoader();
		loader.loadDefaultConfiguration();
		
		System.clearProperty(ConfigLoader.DANDELION_DT_CONFIGURATION);
	}
	
	@Test
	public void should_return_empty_user_properties_from_classpath() {
		Properties userProperties = loader.loadUserConfiguration(request.getLocale());
		
		assertThat(userProperties).isNotNull();
		assertThat(userProperties).isEmpty();
	}
	
	@Test
	public void should_load_user_properties_from_system_property() {
		String path = new File("src/test/resources/loadingTest/test1/").getAbsolutePath();
		System.setProperty(ConfigLoader.DANDELION_DT_CONFIGURATION, path);
		
		Properties userProperties = loader.loadUserConfiguration(request.getLocale());
		
		assertThat(userProperties).isNotNull();
		assertThat(userProperties).hasSize(1);
		assertThat(userProperties.getProperty("global." + DatatableOptions.MAIN_EXTENSION_NAMES.getName())).isEqualTo("ext1,ext2");
	}
	
	@Test
	public void should_resolve_global_group_only_and_override_with_user_properties() {
		String path = new File("src/test/resources/loadingTest/test1/").getAbsolutePath();
		System.setProperty(ConfigLoader.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, Map<Option<?>, Object>> map = new HashMap<String, Map<Option<?>, Object>>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(1);
		assertThat(map.containsKey("global")).isTrue();
		
		// Default values
		assertThat(map.get("global").get(DatatableOptions.FEATURE_INFO)).isNull();
		
		// Overriden values
		assertThat(map.get("global").get(DatatableOptions.MAIN_EXTENSION_NAMES)).isEqualTo("ext1,ext2");
	}
	
	@Test
	public void should_resolve_global_group1_and_group2_from_user_properties() {
		String path = new File("src/test/resources/loadingTest/test2/").getAbsolutePath();
		System.setProperty(ConfigLoader.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, Map<Option<?>, Object>> map = new HashMap<String, Map<Option<?>, Object>>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(3);
		assertThat(map.containsKey("global")).isTrue();
		assertThat(map.containsKey("group1")).isTrue();
		assertThat(map.containsKey("group2")).isTrue();
		
		// Global group
		assertThat(map.get("global").get(DatatableOptions.MAIN_EXTENSION_NAMES)).isEqualTo("ext1,ext2"); // Overriden value from global

		// Group1 group
		assertThat(map.get("group1").get(DatatableOptions.CSS_CLASS).toString()).isEqualTo("group1-class"); // Overriden value
		assertThat(map.get("group1").get(DatatableOptions.CSS_STYLE).toString()).isEqualTo("group1-style"); // Overriden value
		assertThat(map.get("group1").get(DatatableOptions.MAIN_EXTENSION_NAMES)).isEqualTo("ext1,ext2"); // Overriden value from global		
		// Group2 group
		assertThat(map.get("group2").get(DatatableOptions.CSS_CLASS).toString()).isEqualTo("group2-class"); // Overriden value
		assertThat(map.get("group2").get(DatatableOptions.CSS_STYLE).toString()).isEqualTo("group2-style"); // Overriden value
		assertThat(map.get("group2").get(DatatableOptions.MAIN_EXTENSION_NAMES)).isEqualTo("ext1,ext2"); // Overriden value from global	}
	}
	
	@Test
	public void should_resolve_group1_only_from_user_properties() {
		String path = new File("src/test/resources/loadingTest/test3/").getAbsolutePath();
		System.setProperty(ConfigLoader.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, Map<Option<?>, Object>> map = new HashMap<String, Map<Option<?>, Object>>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(2);
		assertThat(map.containsKey("global")).isTrue();
		assertThat(map.containsKey("group1")).isTrue();
		
		// Global group
		assertThat(map.get("global").get(DatatableOptions.CSS_CLASS)).isNull(); // Default value

		// Group1 group
		assertThat(map.get("group1").get(DatatableOptions.CSS_CLASS).toString()).isEqualTo("group1-class"); // Overriden value
	}
	
	@Test
	public void should_use_en_properties_first() {
		String path = new File("src/test/resources/loadingTest/test4/").getAbsolutePath();
		System.setProperty(ConfigLoader.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, Map<Option<?>, Object>> map = new HashMap<String, Map<Option<?>, Object>>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(1);
		assertThat(map.containsKey("global")).isTrue();
		
		// Global group
		assertThat(map.get("global").get(DatatableOptions.I18N_MSG_INFO)).isEqualTo(
				"Showing _START_ to _END_ of _TOTAL_ entries"); // Overriden value
	}
}