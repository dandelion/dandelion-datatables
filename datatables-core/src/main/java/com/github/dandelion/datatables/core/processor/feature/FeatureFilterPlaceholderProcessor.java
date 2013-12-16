package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeatureFilterPlaceholderProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		FilterPlaceholder placeholder = null;
		if (StringUtils.isNotBlank(stringifiedValue)) {
			try {
				placeholder = FilterPlaceholder.valueOf(stringifiedValue.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ConfigurationProcessingException(stringifiedValue + " is not a valid value among "
						+ FilterPlaceholder.values(), e);
			}
		}

		updateEntry(placeholder);
	}
}