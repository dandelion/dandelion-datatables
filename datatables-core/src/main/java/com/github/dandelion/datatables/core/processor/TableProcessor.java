package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public interface TableProcessor extends ConfigurationProcessor {

	public void process(ConfigToken<?> configToken, String value, TableConfiguration tableConfiguration,
			Map<ConfigToken<?>, Object> stagingConf);
}