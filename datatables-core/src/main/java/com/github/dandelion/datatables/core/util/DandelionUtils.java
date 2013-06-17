package com.github.dandelion.datatables.core.util;

import com.github.dandelion.datatables.core.constants.SystemConstants;

/**
 * Main utility methods.
 * 
 * @author Thibault Duchateau
 * @since 0.8.6
 */
public class DandelionUtils {

	public static boolean isDevModeEnabled() {
		return StringUtils.isBlank(System.getProperty(SystemConstants.DANDELION_DEV_MODE))
				|| "true".equals(System.getProperty(SystemConstants.DANDELION_DEV_MODE));
	}
}
