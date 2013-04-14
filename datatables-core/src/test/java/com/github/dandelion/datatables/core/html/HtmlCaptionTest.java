package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlCaptionTest extends HtmlTagWithContentTest {

	private HtmlCaption caption;

	@Before
	@Override
	public void createHtmlTag(){
		tag = caption = new HtmlCaption();
	}

	@Test
	public void should_generate_caption_tag_with_title(){
		caption.setTitle("dummy title");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption title=\"dummy title\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_value(){
		caption.addContent("<span>an HTML value</span>");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption><span>an HTML value</span></caption>");
	}

	@Test
	public void should_generate_full_ops_caption_tag(){
		caption.setId("fullId");
		caption.addCssClass("classy");
		caption.addCssStyle("styly");
		caption.setTitle("titly");
		caption.addContent("valued");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption id=\"fullId\" class=\"classy\" style=\"styly\" title=\"titly\">valued</caption>");
	}
}