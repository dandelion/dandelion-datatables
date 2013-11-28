package com.github.dandelion.datatables.core.extension;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class AbstractExtensionTest {

	private MockHttpServletRequest request;
	protected ExtensionProcessor extensionProcessor;
	protected HtmlTable table;
	protected JsResource mainJsFile;
	protected Map<String, Object> mainConfig;
	
	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		table = new HtmlTable("fakeId", request);
		table.addHeaderRow();
		table.getLastHeaderRow().addHeaderColumn("column1");
		table.getLastHeaderRow().addHeaderColumn("column2");
		mainJsFile = new JsResource();
		mainConfig = new HashMap<String, Object>();
		extensionProcessor = new ExtensionProcessor(table, mainJsFile, mainConfig);
	}
}
