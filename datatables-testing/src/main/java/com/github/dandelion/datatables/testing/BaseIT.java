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
package com.github.dandelion.datatables.testing;

import java.io.File;

import org.eclipse.jetty.webapp.WebAppContext;
import org.fluentlenium.core.FluentAdapter;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import com.github.dandelion.datatables.core.asset.JsResource;
import com.github.dandelion.datatables.core.asset.WebResources;
import com.github.dandelion.datatables.core.cache.AssetCache;
import com.github.dandelion.datatables.testing.utils.Constants;
import com.github.dandelion.datatables.testing.utils.JspTest;
import com.github.dandelion.datatables.testing.utils.ThymeleafTest;

/**
 * <p>
 * Common abstract superclass for all tests, JSP or Thymeleaf.
 * 
 * <p>
 * This class initializes the Selenium WebDriver using a {@link TestWatcher}.
 * Also it contains some utility methods available in all tests.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public abstract class BaseIT extends FluentAdapter {

	protected static WebDriver driver;
	protected static WebAppContext context;
	
	@Rule
	public TestWatcher watchman = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			if (null == driver) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						if (driver != null) {
							driver.quit();
						}
					}
				});

				Capabilities capabilities = new DesiredCapabilities();
				DriverService service = PhantomJSDriverService.createDefaultService(capabilities);
				driver = new PhantomJSDriver(service, capabilities);
			}

			driver.manage().deleteAllCookies();
			driver.manage().window().setSize(Constants.DEFAULT_WINDOW_SIZE);
			initFluent(driver).withDefaultUrl(defaultUrl());
		}

		@Override
		protected void succeeded(Description description) {
			snapshotFile(description).delete();
		}

		@Override
		protected void failed(Throwable e, Description description) {
			takeScreenShot(snapshotFile(description).getAbsolutePath());
		}

		private File snapshotFile(Description description) {
			return new File("snapshots", description.getMethodName() + ".png");
		}
	};

	public String defaultUrl() {
		return "http://" + Constants.SERVER_HOST + ":" + Constants.SERVER_PORT;
	}
	
	@Override
	public String getDefaultBaseUrl() {
		return "http://" + Constants.SERVER_HOST + ":" + Constants.SERVER_PORT;
	}
	
	@SuppressWarnings("unchecked")
	public FluentList<FluentWebElement> getTable() {
		return find("#" + Constants.TABLE_ID + "_wrapper").find("table");
	}

	public FluentWebElement getHtmlBody() {
		return findFirst("body");
	}
	
	/**
	 * <p>
	 * Build the path to the view to load for an integration test depending on
	 * the kind of test to run.
	 * 
	 * <p>
	 * All test classes must be annotated either with {@link JspTest} or with
	 * {@link ThymeleafTest}.
	 * 
	 * @param page
	 *            The page to load, without any prefix or suffix.
	 */
	public void goToPage(String page){
		if(this.getClass().isAnnotationPresent(JspTest.class)){
			goTo("/" + page + ".jsp");
		}
		else if(this.getClass().isAnnotationPresent(ThymeleafTest.class)) {
			goTo("/thymeleaf/" + page);
		}
	}
	
	public void goToPage(String page, Boolean display){
		goToPage(page);
		if(display){
			System.out.println(driver.getPageSource());
			System.out.println(getConfigurationFromPage(page));
		}
	}
	
	public JsResource getConfigurationFromPage(String page) {
		String url = null;
		if(this.getClass().isAnnotationPresent(JspTest.class)){
			url = "/" + page + ".jsp|myTableId";
		}
		else if(this.getClass().isAnnotationPresent(ThymeleafTest.class)) {
			url = "/thymeleaf/" + page + "|myTableId";
		}
		WebResources webResources = ((WebResources) AssetCache.cache.get(url));
		return webResources.getMainJsFile() != null ? webResources.getMainJsFile() : null;
	}
}