package com.github.dandelion.datatables.core.processor.css;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.theme.Theme;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssThemeProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		Extension theme = null;
		if (StringUtils.isNotBlank(value)) {
			try {
				theme = Theme.valueOf(value.toUpperCase()).getInstance();
			} catch (IllegalArgumentException e) {
				throw new ConfigurationProcessingException("'" + value + "' is not a valid value among "
						+ Theme.possibleValues(), e);
			}
		}
		
		tableConfiguration.set(configToken, theme);
	}
}