package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

public interface ColumnProcessor extends ConfigurationProcessor {

	public void process(ConfigToken<?> configToken, String value, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration, Map<ConfigToken<?>, Object> stagingConf, Map<ConfigToken<?>, Extension> stagingExtension);
}
