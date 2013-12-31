package com.github.dandelion.datatables.core.processor;

import java.util.Map.Entry;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.util.ProcessorUtils;

public class StringProcessor implements TableProcessor, ColumnProcessor {

	private boolean scopeUpdatable;

	public StringProcessor() {
		scopeUpdatable = false;
	}

	public StringProcessor(boolean scopeUpdatable) {
		this.scopeUpdatable = scopeUpdatable;
	}

	@Override
	public void process(Entry<ConfigToken<?>, Object> configEntry, TableConfiguration tableConfiguration) {
		doProcess(configEntry, tableConfiguration);
	}

	@Override
	public void process(Entry<ConfigToken<?>, Object> configEntry, ColumnConfiguration columnConfiguration,
			TableConfiguration tableConfiguration) {
		doProcess(configEntry, tableConfiguration);
	}

	private void doProcess(Entry<ConfigToken<?>, Object> configEntry, TableConfiguration tableConfiguration) {

		String value = String.valueOf(configEntry.getValue());

		if (scopeUpdatable) {
			ProcessorUtils.processAttributeAndScope(value, configEntry, tableConfiguration.getRequest());
		} else {
			configEntry.setValue(StringUtils.isNotBlank(value) ? value : null);
		}
	}
}