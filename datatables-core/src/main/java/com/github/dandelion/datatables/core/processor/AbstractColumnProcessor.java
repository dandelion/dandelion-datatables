package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.extension.Extension;

public abstract class AbstractColumnProcessor implements ColumnProcessor {

	protected TableConfiguration tableConfiguration;
	protected ColumnConfiguration columnConfiguration;
	protected Map<ConfigToken<?>, Object> stagingConf;
	protected Map<ConfigToken<?>, Extension> stagingExtensions;
	
	@Override
	public void process(ConfigToken<?> configToken, String value, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration, Map<ConfigToken<?>, Object> stagingConf,
			Map<ConfigToken<?>, Extension> stagingExtension) {
		this.tableConfiguration = tableConfiguration;
		this.columnConfiguration = columnConfiguration;
		this.stagingConf = stagingConf;
		this.stagingExtensions = stagingExtension;
		doProcess(configToken, value);
	}

	public abstract void doProcess(ConfigToken<?> configToken, String value);
}
