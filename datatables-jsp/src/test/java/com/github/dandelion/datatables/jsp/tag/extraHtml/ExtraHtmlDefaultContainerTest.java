package com.github.dandelion.datatables.jsp.tag.extraHtml;

import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;

import org.junit.Test;
import org.springframework.mock.web.MockBodyContent;

import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtraHtmlDefaultContainerTest extends ExtraHtmlBaseTest {

	@Override
	public void initTag() {
		StringBuilder body = new StringBuilder();
		body.append("<a href=\"http://myDomain.com\">Link</a>");
		body.append("\n");
		body.append("<div>Content</div>");
		
		extraHtmlTag.setBodyContent(new MockBodyContent(body.toString(), mockPageContext.getOut()));
		extraHtmlTag.setUid("M");
		extraHtmlTag.setCssClass("class1");
		extraHtmlTag.setCssStyle("float:left");
	}
	
	@Test
	public void should_add_an_extraHtml_with_default_container() throws JspException, UnsupportedEncodingException {
		
		ExtraHtml extraHtml = new ExtraHtml();
		extraHtml.setContainer("div");
		extraHtml.setContent("<a href=\"http://myDomain.com\">Link</a><div>Content</div>");
		extraHtml.setUid("M");
		extraHtml.setCssClass("class1");
		extraHtml.setCssStyle("float:left");
		
		assertThat(table.getTableConfiguration().getExtraHtmlSnippets()).hasSize(1);
		assertThat(table.getTableConfiguration().getExtraHtmlSnippets()).contains(extraHtml);
	}
}