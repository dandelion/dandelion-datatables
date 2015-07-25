package com.github.dandelion.datatables.core.extension;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.github.dandelion.core.Context;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.generator.DatatableJQueryContent;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class AbstractExtensionTest {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	protected ExtensionProcessor extensionProcessor;
	protected HtmlTable table;
	protected DatatableJQueryContent datatableContent;
	protected Map<String, Object> mainConfig;
	
	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		request.setAttribute(WebConstants.DANDELION_CONTEXT_ATTRIBUTE, new Context(new MockFilterConfig()));
		table = new HtmlTable("fakeId", request, response);
		table.getTableConfiguration().getOptions().clear();
		table.addHeaderRow();
		table.getLastHeaderRow().addHeaderColumn("column1");
		table.getLastHeaderRow().addHeaderColumn("column2");
		datatableContent = new DatatableJQueryContent(table);
		mainConfig = new HashMap<String, Object>();
		extensionProcessor = new ExtensionProcessor(table, datatableContent, mainConfig);
	}
}
