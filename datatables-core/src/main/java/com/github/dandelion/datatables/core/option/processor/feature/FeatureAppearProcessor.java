package com.github.dandelion.datatables.core.option.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.extension.feature.AppearFeature;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class FeatureAppearProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {

		String retval = null;
		String valueAsString = context.getValueAsString();
		
		if (StringUtils.isNotBlank(valueAsString)) {

			if (valueAsString.contains(",") || "fadein".equals(valueAsString.toLowerCase())) {
				String[] tmp = valueAsString.toLowerCase().split(",");

				retval = "fadein";
				if (tmp.length > 1) {
					context.addTableEntry(DatatableOptions.FEATURE_APPEAR_DURATION, tmp[1]);
				}
			} else {
				retval = "block";
			}
		}

		context.registerExtension(AppearFeature.APPEAR_FEATURE_NAME);
		
		return retval;
	}
}