package com.github.dandelion.datatables.jsp.tag.extraHtml;

import java.util.Map;

import javax.servlet.jsp.JspException;

import org.junit.Before;
import org.springframework.mock.web.MockBodyContent;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.mock.Mock;
import com.github.dandelion.datatables.jsp.tag.ColumnTag;
import com.github.dandelion.datatables.jsp.tag.ExtraHtmlTag;
import com.github.dandelion.datatables.jsp.tag.TableTag;
import com.github.dandelion.datatables.utils.TableTagBuilder;

public abstract class ExtraHtmlBaseTest {

	protected ExtraHtmlTag extraHtmlTag;
	protected MockPageContext mockPageContext;
	protected TableTag tableTag;
	protected TableTagBuilder tableTagBuilder;
	protected HtmlTable table;
	protected Map<String, Object> mainConf;
	
	@Before
	public void setupTest() throws JspException {
		
		MockServletContext mockServletContext = new MockServletContext();

		mockPageContext = new MockPageContext(mockServletContext);
		
		tableTagBuilder = new TableTagBuilder(Mock.persons, "myTableId").context(mockPageContext).defaultTable();
		tableTag = tableTagBuilder.getTableTag();

		extraHtmlTag = new ExtraHtmlTag();
		extraHtmlTag.setPageContext(mockPageContext);
		extraHtmlTag.setParent(tableTag);
		initTag();
		
		tableTag.doStartTag();
		
		extraHtmlTag.doStartTag();
		extraHtmlTag.doAfterBody();
		extraHtmlTag.doEndTag();
		
		for (int i = 0; i < Mock.persons.size(); i++) {
			for (ColumnTag columnTag : tableTagBuilder.getColumnTags()) {
				columnTag.doStartTag();
				columnTag.doEndTag();
			}
			tableTag.doAfterBody();
		}
		tableTag.doEndTag();
		
		table = tableTag.getTable();
		
	}

	public abstract void initTag();
}
