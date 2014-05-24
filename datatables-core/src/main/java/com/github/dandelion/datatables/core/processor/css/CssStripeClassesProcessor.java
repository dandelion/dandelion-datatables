package com.github.dandelion.datatables.core.processor.css;

import java.util.Arrays;
import java.util.Iterator;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.processor.AbstractConfigurationProcessor;

public class CssStripeClassesProcessor extends AbstractConfigurationProcessor {

	@Override
	public void doProcess() {

		if (StringUtils.isNotBlank(stringifiedValue)) {

			String retval = null;

			StringBuilder stripeTmp = new StringBuilder("[");
			if (stringifiedValue.contains(",")) {
				String[] tmp = stringifiedValue.trim().split(",");
				Iterator<String> iterator = Arrays.asList(tmp).iterator();
				stripeTmp.append("'").append(iterator.next().trim()).append("'");
				while (iterator.hasNext()) {
					stripeTmp.append(",'").append(iterator.next().trim()).append("'");
				}
				retval = stripeTmp.toString();
			} else {
				stripeTmp.append("'").append(stringifiedValue).append("'");
			}
			stripeTmp.append("]");
			retval = stripeTmp.toString();

			updateEntry(retval);
		}
	}
}