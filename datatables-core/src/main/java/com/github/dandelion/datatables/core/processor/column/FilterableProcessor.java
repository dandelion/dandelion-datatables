package com.github.dandelion.datatables.core.processor.column;

import com.github.dandelion.datatables.core.configuration.ColumnConfig;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.extension.feature.FilterType;
import com.github.dandelion.datatables.core.processor.AbstractColumnProcessor;

public class FilterableProcessor extends AbstractColumnProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		tableConfiguration.registerExtension(stagingExtensions.get(ColumnConfig.FILTERABLE));

		if (!stagingConf.containsKey(ColumnConfig.FILTERTYPE)) {
			ColumnConfig.FILTERTYPE.setIn(columnConfiguration, FilterType.INPUT);
		}

		ColumnConfig.FILTERABLE.setIn(columnConfiguration, true);
	}
}