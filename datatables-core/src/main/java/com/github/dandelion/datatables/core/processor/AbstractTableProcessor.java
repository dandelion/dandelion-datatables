package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public abstract class AbstractTableProcessor implements TableProcessor {

	protected TableConfiguration tableConfiguration;
	protected Map<ConfigToken<?>, Object> stagingConf;
	
	@Override
	public void process(ConfigToken<?> configToken, String value, TableConfiguration tableConfiguration,
			Map<ConfigToken<?>, Object> stagingConf) {
		this.tableConfiguration = tableConfiguration;
		this.stagingConf = stagingConf;
		doProcess(configToken, value);
	}

	public abstract void doProcess(ConfigToken<?> configToken, String value);
}
