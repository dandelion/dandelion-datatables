package com.github.dandelion.datatables.core.configuration;

import javax.servlet.http.HttpServletRequest;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.generator.javascript.JavascriptGenerator;

public class FakeJavascriptGenerator implements JavascriptGenerator {

	@Override
	public String getContent(HttpServletRequest request) {
		return null;
	}

	public void addResource(JsResource jsResource) {
	}
}
