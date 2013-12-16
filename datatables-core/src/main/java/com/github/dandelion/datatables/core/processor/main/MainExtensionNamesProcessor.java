package com.github.dandelion.datatables.core.processor.main;

import java.util.HashSet;
import java.util.Set;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class MainExtensionNamesProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess() {
		Set<String> retval = null;
		if (StringUtils.isNotBlank(stringifiedValue)) {
			retval = new HashSet<String>();
			
			String[] customFeatures = stringifiedValue.split(",");

			for (String feature : customFeatures) {
				retval.add(feature.trim().toLowerCase());
			}
		}

		updateEntry(retval);
	}
}