package com.github.dandelion.datatables.core.processor.css;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssThemeOptionProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		if (StringUtils.isNotBlank(value)) {

			ThemeOption themeOption = null;

			try {
				themeOption = ThemeOption.valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(value);
				sb.append("' is not a valid theme option. Possible values are: ");
				sb.append(ThemeOption.possibleValues());
				throw new ConfigurationProcessingException(sb.toString(), e);
			}

			tableConfiguration.set(configToken, themeOption);
		}
	}
}