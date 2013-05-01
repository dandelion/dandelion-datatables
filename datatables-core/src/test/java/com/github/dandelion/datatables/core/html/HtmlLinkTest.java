package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlLinkTest {

	protected HtmlTag tag;
	private HtmlLink link;

	@Before
	public void createHtmlTag() {
		tag = link = new HtmlLink();
	}

	@Test
	public void should_generate_link_tag_with_href() {
		tag = link = new HtmlLink("myHref");
		assertThat(link.toHtml().toString()).isEqualTo("<link rel=\"stylesheet\" href=\"myHref\"></link>");
	}

	@Test
	public void should_generate_full_ops_div_tag() {
		link.setId("fullId");
		link.addCssClass("classy");
		link.addCssStyle("styly");
		link.setHref("fullyHref");
		assertThat(link.toHtml().toString()).isEqualTo("<link id=\"fullId\" class=\"classy\" style=\"styly\" rel=\"stylesheet\" href=\"fullyHref\"></link>");
	}
}