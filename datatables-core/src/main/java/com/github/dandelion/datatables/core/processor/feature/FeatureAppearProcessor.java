package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.feature.AppearFeature;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeatureAppearProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		String retval = null;

		if (StringUtils.isNotBlank(stringifiedValue)) {

			if (stringifiedValue.contains(",") || "fadein".equals(stringifiedValue.toLowerCase())) {
				String[] tmp = stringifiedValue.toLowerCase().split(",");

				retval = "fadein";
				if (tmp.length > 1) {
					TableConfig.FEATURE_APPEAR_DURATION.setIn(tableConfiguration, tmp[1]);
				}
			} else {
				retval = "block";
			}
		}

		updateEntry(retval);
		registerExtension(new AppearFeature());
	}
}