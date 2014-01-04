package com.github.dandelion.datatables.core.processor.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.TableConfig;
import com.github.dandelion.datatables.core.extension.feature.AppearFeature;
import com.github.dandelion.datatables.core.processor.AbstractConfigurationProcessor;

public class FeatureAppearProcessor extends AbstractConfigurationProcessor {

	@Override
	public void doProcess() {

		String retval = null;

		if (StringUtils.isNotBlank(stringifiedValue)) {

			if (stringifiedValue.contains(",") || "fadein".equals(stringifiedValue.toLowerCase())) {
				String[] tmp = stringifiedValue.toLowerCase().split(",");

				retval = "fadein";
				if (tmp.length > 1) {
					addTableEntry(TableConfig.FEATURE_APPEAR_DURATION, tmp[1]);
				}
			} else {
				retval = "block";
			}
		}

		registerExtension(new AppearFeature());
		updateEntry(retval);
	}
}