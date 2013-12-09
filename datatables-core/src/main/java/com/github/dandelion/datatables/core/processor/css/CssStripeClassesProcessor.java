package com.github.dandelion.datatables.core.processor.css;

import java.util.Arrays;
import java.util.Iterator;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.configuration.ConfigToken;
import com.github.dandelion.datatables.core.processor.AbstractTableProcessor;

public class CssStripeClassesProcessor extends AbstractTableProcessor {

	@Override
	public void doProcess(ConfigToken<?> configToken, String value) {
		String retval = null;

		if (StringUtils.isNotBlank(value)) {
			StringBuilder stripeTmp = new StringBuilder("[");
			if (value.contains(",")) {
				String[] tmp = value.trim().split(",");
				Iterator<String> iterator = Arrays.asList(tmp).iterator();
				stripeTmp.append("'").append(iterator.next().trim()).append("'");
				while (iterator.hasNext()) {
					stripeTmp.append(",'").append(iterator.next().trim()).append("'");
				}
				retval = stripeTmp.toString();
			} else {
				stripeTmp.append("'").append(value).append("'");
			}
			stripeTmp.append("]");
			retval = stripeTmp.toString();
		}

		tableConfiguration.set(configToken, retval);
	}
}