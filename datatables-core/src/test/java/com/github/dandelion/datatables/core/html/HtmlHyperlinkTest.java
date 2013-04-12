package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlHyperlinkTest extends HtmlTagWithContentTest {

	private HtmlHyperlink hyperlink;

	@Before
	@Override
	public void createHtmlTag() {
		tag = hyperlink = new HtmlHyperlink();
	}

	@Test
	public void should_generate_div_tag_with_id() {
		tag = hyperlink = new HtmlHyperlink("myHref", "myLabel");
		assertThat(hyperlink.toHtml().toString()).isEqualTo("<href a=\"myHref\">myLabel</href>");
	}

	@Test
	public void should_generate_full_ops_href_tag() {
		hyperlink.setId("fullId");
		hyperlink.addCssClass("classy");
		hyperlink.addCssStyle("styly");
		hyperlink.setHref("fullHref");
		hyperlink.addContent("valued");
		assertThat(hyperlink.toHtml().toString()).isEqualTo("<href id=\"fullId\" class=\"classy\" style=\"styly\" a=\"fullHref\">valued</href>");
	}
}