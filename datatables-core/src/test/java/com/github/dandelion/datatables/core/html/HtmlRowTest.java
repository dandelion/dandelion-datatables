package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import com.github.dandelion.datatables.core.asset.DisplayType;

public class HtmlRowTest extends HtmlTagTest {

	private HtmlRow row;
	private HtmlColumn headerColumn;
	private HtmlColumn column1;
	private HtmlColumn column2;

	@Before
	@Override
	public void createHtmlTag() {
		tag = row = new HtmlRow();
	}

	@Test
	public void should_generate_row_with_id() {
		row = new HtmlRow("myId");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + " id=\"myId\"></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_one_column() {
		row.addColumn("ColumnContent");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><td>ColumnContent</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_several_columns() {
		row.addColumns("ColumnContent1", "ColumnContent2");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><td>ColumnContent1</td><td>ColumnContent2</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_one_header_column() {
		row.addHeaderColumn("ColumnContent");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnContent</th></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_several_header_columns() {
		row.addHeaderColumns("ColumnContent1", "ColumnContent2");
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnContent1</th><th>ColumnContent2</th></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_with_composed_columns() {
		populateColumns();
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "><th>ColumnHeaderContent</th><td>ColumnContent1</td><td>ColumnContent2</td></" + row.getTag() + ">");
	}

	@Test
	public void should_generate_row_without_column_without_HTML_DisplayType() {
		row.addColumn(new HtmlColumn(DisplayType.CSV));
		assertThat(row.toHtml().toString()).isEqualTo("<" + row.getTag() + "></" + row.getTag() + ">");
	}
	
	@Test
	public void should_get_header_columns() {
		populateColumns();
		assertThat(row.getHeaderColumns()).containsExactly(headerColumn);
	}
	
	@Test
	public void should_get_last_column() {
		populateColumns();
		assertThat(row.getLastColumn()).isEqualTo(column2);
	}

	private void populateColumns() {
		headerColumn = new HtmlColumn(true, "ColumnHeaderContent");
		column1 = new HtmlColumn(false, "ColumnContent1");
		column2 = new HtmlColumn(false, "ColumnContent2");
		row.addHeaderColumn(headerColumn);
		row.addColumn(column1);
		row.addColumn(column2);
	}
}