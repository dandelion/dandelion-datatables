package com.github.dandelion.datatables.core.option.processor.css;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.EnumUtils;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.extension.theme.Theme;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class CssThemeProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {
		
		Extension retval = null;
		String valueAsString = context.getValueAsString();
		if (StringUtils.isNotBlank(valueAsString)) {

			try {
				retval = Theme.valueOf(valueAsString.toUpperCase()).getInstance();
			} catch (IllegalArgumentException e) {
				StringBuilder sb = new StringBuilder();
				sb.append("'");
				sb.append(valueAsString);
				sb.append("' is not a valid theme name. Possible values are: ");
				sb.append(EnumUtils.printPossibleValuesOf(Theme.class));
				throw new DandelionException(sb.toString());
			}

		}
		
		return retval;
	}
}