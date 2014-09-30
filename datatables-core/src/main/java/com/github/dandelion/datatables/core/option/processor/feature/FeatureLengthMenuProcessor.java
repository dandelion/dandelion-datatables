package com.github.dandelion.datatables.core.option.processor.feature;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.option.processor.AbstractOptionProcessor;
import com.github.dandelion.datatables.core.option.processor.OptionProcessingContext;

public class FeatureLengthMenuProcessor extends AbstractOptionProcessor {

	@Override
	protected Object getProcessedValue(OptionProcessingContext context) {

		String valueAsString = context.getValueAsString();
		String retval = null;
		if (StringUtils.isNotBlank(valueAsString)) {
			String[] tmp = valueAsString.split(";");
			if (tmp.length > 1) {
				String[] tmp2 = tmp[0].split(",");
				String[] tmp3 = tmp[1].split(",");
				if (tmp2.length == tmp3.length) {
					retval = "[[" + tmp[0] + "],[" + tmp[1] + "]]";
				} else {
					throw new DandelionException(
							"You must provide the exact same number of elements separated by a \";\"");
				}
			} else {
				retval = "[" + valueAsString + "]";
			}
		}

		return retval;
	}
}