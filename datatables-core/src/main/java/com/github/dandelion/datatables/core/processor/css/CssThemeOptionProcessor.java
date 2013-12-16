package com.github.dandelion.datatables.core.processor.css;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.theme.ThemeOption;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssThemeOptionProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		if (StringUtils.isNotBlank(stringifiedValue)) {

			ThemeOption themeOption = null;

			try {
				themeOption = ThemeOption.valueOf(stringifiedValue.toUpperCase());
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(stringifiedValue);
				sb.append("' is not a valid theme option. Possible values are: ");
				sb.append(ThemeOption.possibleValues());
				throw new ConfigurationProcessingException(sb.toString(), e);
			}

			updateEntry(themeOption);
		}
	}
}