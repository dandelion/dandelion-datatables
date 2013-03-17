package com.github.dandelion.datatables.selenium;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.github.dandelion.datatables.core.constants.CdnConstants;

/**
 * Test the CDN activation.
 *
 * @author Thibault Duchateau
 */
public class CdnIT extends DomBaseIT {

	@Test
	public void should_disable_paging() throws IOException, Exception {
		goTo("/basicFeatures/table_cdn.jsp");

		System.out.println(driver.getPageSource());
		assertThat(getHtmlBody().findFirst("link").getAttribute("href")).isEqualTo("http:" + CdnConstants.CDN_DATATABLES_CSS);
		assertThat(getHtmlBody().findFirst("script").getAttribute("src")).isEqualTo("http:" + CdnConstants.CDN_DATATABLES_JS_MIN);
	}
}
