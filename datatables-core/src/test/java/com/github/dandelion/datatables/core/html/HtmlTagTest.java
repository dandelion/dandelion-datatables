/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.core.html;

import org.junit.Before;
import org.junit.Test;

import com.github.dandelion.core.html.AbstractHtmlTag;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlTagTest {

	protected AbstractHtmlTag tag;

	@Before
	public void createHtmlTag() {
		tag = new AbstractHtmlTag() {
		};
	}

	@Test
	public void should_generate_empty_tag() {
		assertThat(tag.toHtml().toString()).isEqualTo("<" + tag.getTag() + "></" + tag.getTag() + ">");
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