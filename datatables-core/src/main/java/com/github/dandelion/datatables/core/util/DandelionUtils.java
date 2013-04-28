package com.github.dandelion.datatables.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * Main utility methods.
 *
 * @author Thibault Duchateau
 * @since 0.8.6
 */
public class DandelionUtils {

	public static boolean isDevModeEnabled(){
		return StringUtils.isBlank(System.getProperty("dandelion.dev.mode")) || "true".equals(System.getProperty("dandelion.dev.mode")); 
	}
}
