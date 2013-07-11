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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.constants.DTMessages;
import com.github.dandelion.datatables.core.constants.SystemConstants;

public class StandardConfigurationLoaderTest {

	HttpServletRequest request;
	MockPageContext mockPageContext;
	StandardConfigurationLoader loader;
	
	@Before
	public void setup() throws Exception {
		MockServletContext mockServletContext = new MockServletContext();
		mockPageContext = new MockPageContext(mockServletContext);
		request = (HttpServletRequest) mockPageContext.getRequest();
		loader = new StandardConfigurationLoader();
		loader.loadDefaultConfiguration();
		
		System.clearProperty(SystemConstants.DANDELION_DT_CONFIGURATION);
	}
	
	@Test
	public void should_return_empty_user_properties_from_classpath() throws Exception {
		Properties userProperties = loader.loadUserConfiguration(request.getLocale());
		
		assertThat(userProperties).isNotNull();
		assertThat(userProperties).isEmpty();
	}
	
	@Test
	public void should_load_user_properties_from_system_property() throws Exception {
		String path = new File("src/test/resources/loadingTest/test1/").getAbsolutePath();
		System.setProperty(SystemConstants.DANDELION_DT_CONFIGURATION, path);
		
		Properties userProperties = loader.loadUserConfiguration(request.getLocale());
		
		assertThat(userProperties).isNotNull();
		assertThat(userProperties).hasSize(1);
		assertThat(userProperties.getProperty("global.main.base.package")).isEqualTo("my.custom.package");
	}
	
	@Test
	public void should_resolve_global_group_only_and_override_with_user_properties() throws Exception {
		String path = new File("src/test/resources/loadingTest/test1/").getAbsolutePath();
		System.setProperty(SystemConstants.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, TableConfiguration> map = new HashMap<String, TableConfiguration>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(1);
		assertThat(map.containsKey("global")).isTrue();
		
		// Default values
		assertThat(map.get("global").getMainCompressorEnable()).isFalse();
		assertThat(map.get("global").getFeatureInfo()).isNull();
		
		// Overriden values
		assertThat(map.get("global").getMainBasePackage()).isEqualTo("my.custom.package");
	}
	
	@Test
	public void should_resolve_global_group1_and_group2_from_user_properties() throws Exception {
		String path = new File("src/test/resources/loadingTest/test2/").getAbsolutePath();
		System.setProperty(SystemConstants.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, TableConfiguration> map = new HashMap<String, TableConfiguration>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(3);
		assertThat(map.containsKey("global")).isTrue();
		assertThat(map.containsKey("group1")).isTrue();
		assertThat(map.containsKey("group2")).isTrue();
		
		// Global group
		assertThat(map.get("global").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("global").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("global").getCssClass()).isNull(); // Default value
		assertThat(map.get("global").getCssStyle()).isNull(); // Default value
		assertThat(map.get("global").getMainBasePackage()).isEqualTo("my.custom.package"); // Overriden value from global
		assertThat(map.get("global").getMessage(DTMessages.INFO.getPropertyName())).isNull(); // Default value
		
		// Group1 group
		assertThat(map.get("group1").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("group1").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("group1").getCssClass().toString()).isEqualTo("group1-class"); // Overriden value
		assertThat(map.get("group1").getCssStyle().toString()).isEqualTo("group1-style"); // Overriden value
		assertThat(map.get("group1").getMainBasePackage()).isEqualTo("my.custom.package"); // Overriden value from global
		assertThat(map.get("group1").getMessage(DTMessages.INFO.getPropertyName())).isNull(); // Default value
		
		// Group2 group
		assertThat(map.get("group2").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("group2").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("group2").getCssClass().toString()).isEqualTo("group2-class"); // Overriden value
		assertThat(map.get("group2").getCssStyle().toString()).isEqualTo("group2-style"); // Overriden value
		assertThat(map.get("group2").getMainBasePackage()).isEqualTo("my.custom.package"); // Overriden value from global
		assertThat(map.get("group2").getMessage(DTMessages.INFO.getPropertyName())).isNull(); // Default value
	}
	
	@Test
	public void should_resolve_group1_only_from_user_properties() throws Exception {
		String path = new File("src/test/resources/loadingTest/test3/").getAbsolutePath();
		System.setProperty(SystemConstants.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, TableConfiguration> map = new HashMap<String, TableConfiguration>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(2);
		assertThat(map.containsKey("global")).isTrue();
		assertThat(map.containsKey("group1")).isTrue();
		
		// Global group
		assertThat(map.get("global").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("global").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("global").getCssClass()).isNull(); // Default value
		assertThat(map.get("global").getCssStyle()).isNull(); // Default value
		assertThat(map.get("global").getMainBasePackage()).isNull(); // Default value
		assertThat(map.get("global").getMessage(DTMessages.INFO.getPropertyName())).isNull(); // Default value

		// Group1 group
		assertThat(map.get("group1").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("group1").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("group1").getCssClass().toString()).isEqualTo("group1-class"); // Overriden value
		assertThat(map.get("group1").getCssStyle()).isNull(); // Overriden value
		assertThat(map.get("group1").getMainBasePackage()).isNull(); // Default value
		assertThat(map.get("group1").getMessage(DTMessages.INFO.getPropertyName())).isNull(); // Default value
	}
	
	@Test
	public void should_use_en_properties_first() throws Exception {
		String path = new File("src/test/resources/loadingTest/test4/").getAbsolutePath();
		System.setProperty(SystemConstants.DANDELION_DT_CONFIGURATION, path);
		
		Map<String, TableConfiguration> map = new HashMap<String, TableConfiguration>();

		loader.loadUserConfiguration(request.getLocale());
		loader.resolveGroups(request.getLocale());
		loader.resolveConfigurations(map, request.getLocale(), request);
		
		assertThat(map).hasSize(1);
		assertThat(map.containsKey("global")).isTrue();
		
		// Global group
		assertThat(map.get("global").getMainCompressorEnable()).isFalse(); // Default value
		assertThat(map.get("global").getFeatureInfo()).isNull(); // Default value
		assertThat(map.get("global").getCssClass()).isNull(); // Default value
		assertThat(map.get("global").getCssStyle()).isNull(); // Default value
		assertThat(map.get("global").getMainBasePackage()).isNull(); // Default value
		assertThat(map.get("global").getMessage(DTMessages.INFO.getPropertyName())).isEqualTo(
				"Showing _START_ to _END_ of _TOTAL_ entries"); // Overriden value
	}
}