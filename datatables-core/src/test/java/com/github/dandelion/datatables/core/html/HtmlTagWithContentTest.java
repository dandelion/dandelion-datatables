package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlTagWithContentTest extends HtmlTagTest {

	@Before
	@Override
	public void createHtmlTag() {
		tag = new HtmlTagWithContent() {
		};
	}

	@Test
	public void should_generate_tag_with_content() {
		((HtmlTagWithContent)tag).setContent(new StringBuilder("dummy value"));
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + ">dummy value</" + tag.getTag() + ">");
	}
}