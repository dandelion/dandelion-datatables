package com.github.dandelion.datatables.jsp.tag;

import java.util.Map;

import org.springframework.mock.web.MockPageContext;

import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.utils.TableTagBuilder;

public class TagBaseTest {

	protected MockPageContext mockPageContext;
	protected TableTag tableTag;
	protected TableTagBuilder tableTagBuilder;
	protected HtmlTable table;
	protected Map<String, Object> mainConf;
	
//	@Before
//	public void setup() throws JspException {
//		MockServletContext mockServletContext = new MockServletContext();
//
//		mockPageContext = new MockPageContext(mockServletContext);
//		
//		tableTagBuilder = new TableTagBuilder(Mock.persons, "myTableId").context(mockPageContext).defaultTable();
//		tableTagBuilder.getTableTag().doStartTag();
//		for (int i = 0; i < Mock.persons.size(); i++) {
//			for (ColumnTag columnTag : tableTagBuilder.getColumnTags()) {
//				columnTag.doStartTag();
//				columnTag.doEndTag();
//			}
//			tableTagBuilder.getTableTag().doAfterBody();
//		}
//		tableTagBuilder.getTableTag().doEndTag();
//		tableTag = tableTagBuilder.getTableTag();
//		table = tableTagBuilder.getTableTag().getTable();
//	}
}
