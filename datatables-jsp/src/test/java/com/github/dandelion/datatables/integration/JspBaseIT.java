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
package com.github.dandelion.datatables.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.fluentlenium.core.Fluent;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.asset.Asset;
import com.github.dandelion.core.asset.AssetType;
import com.github.dandelion.core.asset.cache.AssetCacheManager;
import com.github.dandelion.core.asset.web.AssetServlet;
import com.github.dandelion.core.utils.ResourceUtils;
import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.mock.Person;

/**
 * <p>
 * Common abstract superclass for all JSP integration tests.
 * 
 * <p>
 * This class initializes the Selenium WebDriver using a {@link TestWatcher}.
 * Also it contains some utility methods made available in all tests.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public abstract class JspBaseIT extends Fluent {

	public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1024, 768);
	public static final String TABLE_ID = "myTableId";
	public static final String TABLE_ID2 = "mySecondTableId";
	public static final String SERVER_HOST = "127.0.0.1";
	public static final int SERVER_PORT = 9190;
	
	protected static Server server;
	protected static WebDriver driver;
	protected static WebAppContext context;
	
	@BeforeClass
	public static void beforeClass(){
		// Add system property to disable asset caching
		System.setProperty("dandelion.dev.mode", "true");

		// Create a new web server
		server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost(SERVER_HOST);
		connector.setPort(SERVER_PORT);
		server.addConnector(connector);

		context = new WebAppContext("src/test/webapp", "/");

		// Add support for JSP
		ServletHolder jsp = context.addServlet(JspServlet.class, "*.jsp");
		jsp.setInitParameter("classpath", context.getClassPath());

		// Add new servlet context attributes
		context.setAttribute("persons", Mock.persons);
		context.setAttribute("emptyList", new ArrayList<Person>());
		context.setAttribute("nullList", null);

		server.setHandler(context);
		server.setStopAtShutdown(true);

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@AfterClass
	public static void afterClass(){
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
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

				DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
				desiredCapabilities.setCapability("phantomjs.page.settings.handlesAlerts", false);
				desiredCapabilities.setCapability("phantomjs.page.settings.localToRemoteUrlAccessEnabled", true);
				desiredCapabilities.setCapability("phantomjs.page.settings.webSecurityEnabled", false);
				desiredCapabilities.setCapability("takesScreenshot", false);
				desiredCapabilities.setCapability("browserConnectionEnabled", true);
				desiredCapabilities.setCapability("locationContextEnabled", true);
				desiredCapabilities.setCapability("applicationCacheEnabled", true);
				DriverService service = PhantomJSDriverService.createDefaultService(desiredCapabilities);
				driver = new PhantomJSDriver(service, desiredCapabilities);
			}

			driver.manage().deleteAllCookies();
			driver.manage().window().setSize(DEFAULT_WINDOW_SIZE);
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
		return "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public String getDefaultBaseUrl() {
		return "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	@SuppressWarnings("unchecked")
	public FluentList<FluentWebElement> getTable() {
		return find("#" + TABLE_ID + "_wrapper").find("table");
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
		goTo("/" + page + ".jsp");
	}
	
	public void goToPage(String page, Boolean display){
		goToPage(page);
		if(display){
			System.out.println(driver.getPageSource());
			System.out.println(getConfigurationFromPage(page));
		}
	}
	
	public String getConfigurationFromPage(String page) {
		AssetCacheManager cacheManager = new AssetCacheManager(null);
		Asset asset = new Asset("dandelion-datatables", "0.10.0", AssetType.js);
		String cacheKey = cacheManager.generateCacheKey("http://" + SERVER_HOST + ":" + SERVER_PORT + "/" + page + ".jsp", asset);
		String url = "http://" + SERVER_HOST + ":" + SERVER_PORT + AssetServlet.DANDELION_ASSETS_URL + cacheKey;
		try {
			URL urlLocation = new URL(url);
			return ResourceUtils.getContentFromInputStream(urlLocation.openStream());
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder("The content pointed by the url ");
			sb.append(url);
			sb.append(" can't be read.");
			throw new DandelionException(sb.toString(), e);
		}
	}
}