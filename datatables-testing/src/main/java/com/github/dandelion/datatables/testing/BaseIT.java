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

import com.github.dandelion.datatables.testing.utils.Constants;
import com.github.dandelion.datatables.testing.utils.JspTest;
import com.github.dandelion.datatables.testing.utils.ThymeleafTest;

public abstract class BaseIT extends FluentAdapter {

	protected static WebDriver driver;
	protected static WebAppContext context;
	protected String page;
	
	
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
	
	public void goToPage(String page){
		if(this.getClass().isAnnotationPresent(JspTest.class)){
			goTo("/" + page + ".jsp");
		}
		else if(this.getClass().isAnnotationPresent(ThymeleafTest.class)) {
			goTo("/thymeleaf/" + page);
		}
	}
	
	public void goToPage(String page, Boolean display){
		if(this.getClass().isAnnotationPresent(JspTest.class)){
			goTo("/" + page + ".jsp");
		}
		else if(this.getClass().isAnnotationPresent(ThymeleafTest.class)) {
			goTo("/thymeleaf/" + page);
		}
		if(display){
			System.out.println(driver.getPageSource());
		}
	}
	
//	public JsResource getConfigurationFromPage(String page){
//		String url = "/" + page + "|myTableId";
//		JsResource jsResource = ((WebResources)AssetCache.cache.get(url)).getMainJsFile();
//		return jsResource;
//	}
	
//	public void goToPageAndPrint(String page){
//		goToPage(page);
//
//		System.out.println(driver.getPageSource());
//		System.out.println("*****************************************");
//		System.out.println(getConfigurationFromPage(page.replaceFirst("^/", "")).getContent());
//	}
}