package com.github.dandelion.datatables.core.processor;

import java.util.Map.Entry;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

public class EmptyStringProcessor implements TableProcessor, ColumnProcessor {

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
		configEntry.setValue(String.valueOf(configEntry.getValue()));
	}
}