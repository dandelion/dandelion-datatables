package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlTagTest {

	protected HtmlTag tag;

	@Before
	public void createHtmlTag() {
		tag = new HtmlTag() {
		};
	}

	@Test
	public void should_generate_empty_tag() {
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + "></" + tag.getTag() + ">");
	}

	@Test
	public void should_generate_tag_with_id() {
		tag.setId("anId");
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + " id=\"anId\"></" + tag.getTag() + ">");
	}

	@Test
	public void should_generate_tag_with_one_class() {
		tag.addCssClass("aClass");
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + " class=\"aClass\"></" + tag.getTag() + ">");
	}

	@Test
	public void should_generate_tag_with_several_classes() {
		tag.addCssClass("oneClass");
		tag.addCssClass("twoClass");
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + " class=\"oneClass twoClass\"></" + tag.getTag() + ">");
	}

	@Test
	public void should_generate_tag_with_one_style() {
		tag.addCssStyle("border:1px");
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + " style=\"border:1px\"></" + tag.getTag() + ">");
	}

	@Test
	public void should_generate_tag_with_several_styles() {
		tag.addCssStyle("border:1px");
		tag.addCssStyle("align:center");
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + " style=\"border:1px;align:center\"></" + tag.getTag() + ">");
	}
}