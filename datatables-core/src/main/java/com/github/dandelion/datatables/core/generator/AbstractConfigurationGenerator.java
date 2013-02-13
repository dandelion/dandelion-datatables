package com.github.dandelion.datatables.core.generator;

import java.util.Map;

import com.github.dandelion.datatables.core.model.HtmlTable;

public abstract class AbstractConfigurationGenerator {

	public abstract Map<String, Object> generate(HtmlTable table);
}
