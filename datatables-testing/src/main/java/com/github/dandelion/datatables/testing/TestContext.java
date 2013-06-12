package com.github.dandelion.datatables.testing;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.openqa.selenium.WebDriver;

public class TestContext {

	protected static WebDriver driver;
	protected static Server server;
	protected static WebAppContext context;
}
