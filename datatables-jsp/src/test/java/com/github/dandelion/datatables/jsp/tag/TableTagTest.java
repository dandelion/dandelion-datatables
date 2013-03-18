package com.github.dandelion.datatables.jsp.tag;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.utils.Mock;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 */
public class TableTagTest {

	protected MockPageContext mockPageContext;
	protected TableTag tableTag;
	private HtmlTable table;
	private List<ColumnTag> columns = new ArrayList<ColumnTag>();

	@Before
	public void setup() {
		// mock ServletContext
		MockServletContext mockServletContext = new MockServletContext();

		// mock PageContext
		mockPageContext = new MockPageContext(mockServletContext);

		tableTag = new TableTag();
		tableTag.setData((Collection) Mock.persons);
		tableTag.setId("myTableId");
		tableTag.setPageContext(mockPageContext);

		ColumnTag columnTag = new ColumnTag();
		columnTag = new ColumnTag();
		columnTag.setProperty("id");
		columnTag.setParent(tableTag);
		columns.add(columnTag);

		columnTag = new ColumnTag();
		columnTag.setProperty("firstName");
		columnTag.setParent(tableTag);
		columns.add(columnTag);

		columnTag = new ColumnTag();
		columnTag.setProperty("lastName");
		columnTag.setParent(tableTag);
		columns.add(columnTag);

		columnTag = new ColumnTag();
		columnTag.setProperty("address.town.name");
		columnTag.setParent(tableTag);
		columns.add(columnTag);

		columnTag = new ColumnTag();
		columnTag.setProperty("mail");
		columnTag.setParent(tableTag);
		columns.add(columnTag);

		try {
			tableTag.doStartTag();
			for (int i = 0; i < Mock.persons.size(); i++) {
				for (ColumnTag tag : columns) {
					tag.doStartTag();
					tag.doEndTag();
				}
				tableTag.doAfterBody();
			}
			tableTag.doEndTag();
			table = tableTag.getTable();
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void should_fill_the_table() {
		assertThat(table.getHeadRows()).hasSize(1);
		assertThat(table.getBodyRows()).hasSize(Mock.persons.size());
		assertThat(table.getBodyRows().get(1).getColumns()).hasSize(columns.size());
	}
}
