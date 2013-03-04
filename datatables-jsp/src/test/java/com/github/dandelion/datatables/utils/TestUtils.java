package com.github.dandelion.datatables.utils;

import org.eclipse.jetty.webapp.WebAppContext;

import com.github.dandelion.datatables.core.model.JsResource;

public class TestUtils {

	public static JsResource getJsResourceFromName( WebAppContext context, String name){
		return (JsResource) context.getServletContext().getAttribute(name);
	}
}
