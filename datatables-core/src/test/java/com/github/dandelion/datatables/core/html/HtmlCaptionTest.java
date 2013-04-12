package com.github.dandelion.datatables.core.html;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

public class HtmlCaptionTest{

	private HtmlCaption caption;

	@Before
	public void createHtmlCaption(){
		caption = new HtmlCaption();
	}

	@Test
	public void should_generate_empty_caption_tag(){
		assertThat(caption.toHtml().toString()).isEqualTo("<caption></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_title(){
		caption.setTitle("dummy title");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption title=\"dummy title\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_value(){
		caption.setValue("<span>an HTML value</span>");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption><span>an HTML value</span></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_id(){
		caption.setId("anId");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption id=\"anId\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_one_class(){
		caption.addCssClass("aClass");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption class=\"aClass\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_several_classes(){
		caption.addCssClass("oneClass");
		caption.addCssClass("twoClass");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption class=\"oneClass twoClass\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_one_style(){
		caption.addCssStyle("border:1px");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption style=\"border:1px\"></caption>");
	}

	@Test
	public void should_generate_caption_tag_with_several_styles(){
		caption.addCssStyle("border:1px");
		caption.addCssStyle("align:center");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption style=\"border:1px;align:center\"></caption>");
	}

	@Test
	public void should_generate_full_ops_caption_tag(){
		caption.setId("fullId");
		caption.addCssClass("classy");
		caption.addCssStyle("styly");
		caption.setTitle("titly");
		caption.setValue("valued");
		assertThat(caption.toHtml().toString()).isEqualTo("<caption id=\"fullId\" class=\"classy\" style=\"styly\" title=\"titly\">valued</caption>");
	}
}