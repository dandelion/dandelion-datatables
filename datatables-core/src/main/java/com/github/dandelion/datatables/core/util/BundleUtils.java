package com.github.dandelion.datatables.core.util;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class BundleUtils {

	public static Properties toProperties(ResourceBundle bundle){
		Properties properties = new Properties();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, bundle.getString(key));
        }
        return properties;
	}
}
