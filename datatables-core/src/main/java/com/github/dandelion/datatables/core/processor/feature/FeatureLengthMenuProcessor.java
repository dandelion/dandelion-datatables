package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeatureLengthMenuProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		String retval = null;
		if (StringUtils.isNotBlank(stringifiedValue)) {
			String[] tmp = stringifiedValue.split(";");
			if (tmp.length > 1) {
				String[] tmp2 = tmp[0].split(",");
				String[] tmp3 = tmp[1].split(",");
				if (tmp2.length == tmp3.length) {
					retval = "[[" + tmp[0] + "],[" + tmp[1] + "]]";
				} else {
					throw new ConfigurationProcessingException(
							"You must provide the exact same number of elements separated by a \";\"");
				}
			} else {
				retval = "[" + stringifiedValue + "]";
			}
		}

		updateEntry(retval);
	}
}