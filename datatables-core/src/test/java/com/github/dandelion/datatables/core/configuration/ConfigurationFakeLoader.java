package com.github.dandelion.datatables.core.configuration;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;

public class ConfigurationFakeLoader implements ConfigurationLoader {

	@Override
	public Properties loadDefaultConfiguration() throws ConfigurationLoadingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceBundle loadUserConfiguration(Locale locale) throws ConfigurationLoadingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resolveGroups(Map<String, TableConfiguration> map) {
		// TODO Auto-generated method stub
		
	}
}
