package com.github.dandelion.datatables.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.github.dandelion.datatables.testing.utils.Constants;

public class ThymeleafContextRunner extends BlockJUnit4ClassRunner {

	private static Server server;
	protected static WebAppContext context;

	public ThymeleafContextRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	public void run(RunNotifier notifier) {
		beforeClass();
		super.run(notifier);
		afterClass();
	}

	private void beforeClass() {

		// Add system property to disable asset caching
		System.setProperty("dandelion.dev.mode", "true");

		// Create a new web server
		server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost(Constants.SERVER_HOST);
		connector.setPort(Constants.SERVER_PORT);
		server.addConnector(connector);

		context = new WebAppContext("src/test/webapp", "/");
		context.addServlet(ThymeleafServlet.class, "/thymeleaf/*");

		server.setHandler(context);
		server.setStopAtShutdown(true);

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void afterClass() {
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}