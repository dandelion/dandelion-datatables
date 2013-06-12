package com.github.dandelion.datatables.testing.basics;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.testing.BaseIT;

public class CdnBaseIT extends BaseIT {

	public static final String CDN_DATATABLES_JS = "//ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.js";
	public static final String CDN_DATATABLES_JS_MIN = "//ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js";
	public static final String CDN_DATATABLES_CSS = "//ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css";
	
	@Test
	public void should_disable_paging_using_dom_source() throws IOException, Exception {
		goToPage("basics/cdn_dom");

		assertThat(getHtmlBody().findFirst("link").getAttribute("href")).isEqualTo("http:" + CDN_DATATABLES_CSS);
		assertThat(getHtmlBody().findFirst("script").getAttribute("src")).isEqualTo("http:" + CDN_DATATABLES_JS_MIN);
	}
	
	@Test
	public void should_disable_paging_using_ajax_source() throws IOException, Exception {
		goToPage("basics/cdn_ajax");

		assertThat(getHtmlBody().findFirst("link").getAttribute("href")).isEqualTo("http:" + CDN_DATATABLES_CSS);
		assertThat(getHtmlBody().findFirst("script").getAttribute("src")).isEqualTo("http:" + CDN_DATATABLES_JS_MIN);
	}
}