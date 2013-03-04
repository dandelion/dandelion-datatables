package com.github.dandelion.datatables.core.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Cache that stores generated assets in a static Map.
 *
 * @author Thibault Duchateau
 * @since 0.8.6
 */
public class AssetCache {

	public static Map<String, Object> cache;
	
	static{
		cache = new HashMap<String, Object>();
	}
}
