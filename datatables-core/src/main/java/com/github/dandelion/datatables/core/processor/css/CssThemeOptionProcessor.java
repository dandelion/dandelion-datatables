package com.github.dandelion.datatables.core.processor.css;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssThemeOptionProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		ThemeOption themeOption = null;
		if (StringUtils.isNotBlank(value)) {
			try {
				themeOption = ThemeOption.valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ConfigurationProcessingException(value + " is not a valid value among " + ThemeOption.values());
			}
		}

		tableConfiguration.set(configToken, themeOption);
	}
}