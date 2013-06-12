package com.github.dandelion.datatables.integration;

import java.util.ArrayList;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.mock.Person;
import com.github.dandelion.datatables.testing.utils.Constants;

public class JspContextRunner extends BlockJUnit4ClassRunner {

	private static Server server;
	protected static WebAppContext context;

	public JspContextRunner(Class<?> klass) throws InitializationError {
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

	private void afterClass() {
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}