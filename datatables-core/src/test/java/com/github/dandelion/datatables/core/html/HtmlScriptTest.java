package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlScriptTest extends HtmlTagTest {

	private HtmlScript script;

	@Before
	@Override
	public void createHtmlTag() {
		tag = script = new HtmlScript();
	}

	@Test
	public void should_generate_link_tag_with_href() {
		tag = script = new HtmlScript("mySrc");
		assertThat(script.toHtml().toString()).isEqualTo("<script src=\"mySrc\"></script>");
	}

	@Test
	public void should_generate_full_ops_div_tag() {
		script.setId("fullId");
		script.addCssClass("classy");
		script.addCssStyle("styly");
		script.setSrc("fullySrc");
		assertThat(script.toHtml().toString()).isEqualTo("<script id=\"fullId\" class=\"classy\" style=\"styly\" src=\"fullySrc\"></script>");
	}
}