package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlLinkTest extends HtmlTagTest {

	private HtmlLink link;

	@Before
	@Override
	public void createHtmlTag() {
		tag = link = new HtmlLink();
	}

	@Test
	public void should_generate_link_tag_with_href() {
		tag = link = new HtmlLink("myHref");
		assertThat(link.toHtml().toString()).isEqualTo("<link href=\"myHref\"></link>");
	}

	@Test
	public void should_generate_full_ops_div_tag() {
		link.setId("fullId");
		link.addCssClass("classy");
		link.addCssStyle("styly");
		link.setHref("fullyHref");
		assertThat(link.toHtml().toString()).isEqualTo("<link id=\"fullId\" class=\"classy\" style=\"styly\" href=\"fullyHref\"></link>");
	}
}