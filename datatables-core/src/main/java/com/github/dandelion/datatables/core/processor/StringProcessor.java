package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

public class StringProcessor implements TableProcessor, ColumnProcessor {

	@Override
	public void process(ConfigToken<?> configToken, String value, TableConfiguration tableConfiguration,
			Map<ConfigToken<?>, Object> stagingConf) {
		
		tableConfiguration.set(configToken, StringUtils.isNotBlank(value) ? value : null);
	}

	@Override
	public void process(ConfigToken<?> configToken, String value, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration, Map<ConfigToken<?>, Object> stagingConf, Map<ConfigToken<?>, Extension> stagingExtension) {
		
		columnConfiguration.set(configToken, StringUtils.isNotBlank(value) ? value : null);
	}
}