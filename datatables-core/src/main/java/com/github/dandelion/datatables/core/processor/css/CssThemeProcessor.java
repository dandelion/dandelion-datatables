package com.github.dandelion.datatables.core.processor.css;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.theme.Theme;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssThemeProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		if (StringUtils.isNotBlank(stringifiedValue)) {
			Extension theme = null;
			
			try {
				theme = Theme.valueOf(stringifiedValue.toUpperCase()).getInstance();
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(stringifiedValue);
				sb.append("' is not a valid theme name. Possible values are: ");
				sb.append(Theme.possibleValues());
				throw new ConfigurationProcessingException(sb.toString());
			}
		
			updateEntry(theme);
		}
	}
}