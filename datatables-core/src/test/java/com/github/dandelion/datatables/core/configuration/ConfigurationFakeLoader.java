package com.github.dandelion.datatables.core.configuration;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;

public class ConfigurationFakeLoader implements ConfigurationLoader {

	@Override
	public Properties loadDefaultConfiguration() throws ConfigurationLoadingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties loadUserConfiguration(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> resolveGroups(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resolveConfigurations(Map<String, TableConfiguration> map, Locale locale, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
}
