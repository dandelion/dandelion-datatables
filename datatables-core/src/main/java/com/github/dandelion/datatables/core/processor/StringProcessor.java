package com.github.dandelion.datatables.core.processor;

import java.util.Map.Entry;

import com.github.dandelion.core.asset.web.AssetsRequestContext;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;

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
			if (value.contains("#")) {
				String[] splittedValue = value.split("#");
				if (value.startsWith("#") || splittedValue.length != 2) {
					StringBuilder sb = new StringBuilder();
					sb.append("Wrong format used in the attribute value. ");
					sb.append("The right format is: 'scopeToAdd#JavascriptObject'");
					throw new ConfigurationProcessingException(sb.toString());
				} else {
					AssetsRequestContext.get(tableConfiguration.getRequest()).addScopes(splittedValue[0]);
					configEntry.setValue(StringUtils.isNotBlank(splittedValue[1]) ? splittedValue[1] : null);
				}
			}
		} else {
			configEntry.setValue(StringUtils.isNotBlank(value) ? value : null);
		}
	}
}