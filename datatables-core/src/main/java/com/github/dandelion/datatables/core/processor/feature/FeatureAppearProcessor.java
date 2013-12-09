package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.feature.AppearFeature;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class FeatureAppearProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		String retval = null;

		if (StringUtils.isNotBlank(value)) {

			if (value.contains(",") || "fadein".equals(value.toLowerCase())) {
				String[] tmp = value.toLowerCase().split(",");

				retval = "fadein";
				if (tmp.length > 1) {
					TableConfig.FEATURE_APPEAR_DURATION.setIn(tableConfiguration, tmp[1]);
				}
			} else {
				retval = "block";
			}
		}

		tableConfiguration.set(configToken, retval);
		tableConfiguration.registerExtension(new AppearFeature());
	}
}
