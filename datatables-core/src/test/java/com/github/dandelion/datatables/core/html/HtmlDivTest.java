package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlDivTest extends HtmlTagWithContentTest {

	private HtmlDiv div;

	@Before
	@Override
	public void createHtmlTag() {
		tag = div = new HtmlDiv();
	}

	@Test
	public void should_generate_div_tag_with_id() {
		tag = div = new HtmlDiv("myId");
		assertThat(div.toHtml().toString()).isEqualTo("<div id=\"myId\"></div>");
	}

	@Test
	public void should_generate_div_tag_with_value() {
		div.addContent("<span>an HTML value</span>");
		assertThat(div.toHtml().toString()).isEqualTo("<div><span>an HTML value</span></div>");
	}

	@Test
	public void should_generate_full_ops_div_tag() {
		div.setId("fullId");
		div.addCssClass("classy");
		div.addCssStyle("styly");
		div.addContent("valued");
		assertThat(div.toHtml().toString()).isEqualTo("<div id=\"fullId\" class=\"classy\" style=\"styly\">valued</div>");
	}
}