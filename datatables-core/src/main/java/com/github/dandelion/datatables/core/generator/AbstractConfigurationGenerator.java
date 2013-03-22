package com.github.dandelion.datatables.core.generator;

import java.util.Map;

import com.github.dandelion.datatables.core.html.HtmlTable;

public abstract class AbstractConfigurationGenerator {

	public abstract Map<String, Object> generate(HtmlTable table);
}
