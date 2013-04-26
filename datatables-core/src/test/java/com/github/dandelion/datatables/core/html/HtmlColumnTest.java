package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import com.github.dandelion.datatables.core.asset.DisplayType;

public class HtmlColumnTest extends HtmlTagWithContentTest {

	private HtmlColumn column;

	@Before
	@Override
	public void createHtmlTag(){
		tag = column = new HtmlColumn(true);
	}

	public void createHtmlCellTag(){
		tag = column = new HtmlColumn();
	}

	@Test
	public void should_contain_all_display_types() {
		assertThat(column.getEnabledDisplayTypes()).containsExactly(DisplayType.ALL);
	}

	@Test
	public void should_contain_only_specified_display_types() {
		column = new HtmlColumn(DisplayType.CSV);
		assertThat(column.getEnabledDisplayTypes()).containsExactly(DisplayType.CSV);
		assertThat(column.isHeaderColumn()).isFalse();
	}

	@Test
	public void should_create_header_column_with_content() {
		column = new HtmlColumn(true, "content");
		assertThat(column.isHeaderColumn()).isTrue();
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + ">content</" + column.getTag() + ">");
	}

	@Test
	public void should_create_column_with_content() {
		column = new HtmlColumn(false, "content");
		assertThat(column.isHeaderColumn()).isFalse();
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + ">content</" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_one_class() {
		createHtmlCellTag();
		column.addCssCellClass("aClass");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " class=\"aClass\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_several_classes() {
		createHtmlCellTag();
		column.addCssCellClass("oneClass");
		column.addCssCellClass("twoClass");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " class=\"oneClass twoClass\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_one_style() {
		createHtmlCellTag();
		column.addCssCellStyle("border:1px");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " style=\"border:1px\"></" + column.getTag() + ">");
	}

	@Test
	public void should_generate_cell_tag_with_several_styles() {
		createHtmlCellTag();
		column.addCssCellStyle("border:1px");
		column.addCssCellStyle("align:center");
		assertThat(column.toHtml().toString()).isEqualTo("<" + column.getTag() + " style=\"border:1px;align:center\"></" + column.getTag() + ">");
	}
}