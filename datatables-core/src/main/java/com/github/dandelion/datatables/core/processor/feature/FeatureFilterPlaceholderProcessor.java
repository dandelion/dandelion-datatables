package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.feature.FilterPlaceholder;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeatureFilterPlaceholderProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		FilterPlaceholder placeholder = null;
		if (StringUtils.isNotBlank(value)) {
			try {
				placeholder = FilterPlaceholder.valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ConfigurationProcessingException(value + " is not a valid value among "
						+ FilterPlaceholder.values(), e);
			}
		}

		tableConfiguration.set(configToken, placeholder);
	}
}