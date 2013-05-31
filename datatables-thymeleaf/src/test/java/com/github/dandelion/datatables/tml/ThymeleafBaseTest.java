package com.github.dandelion.datatables.tml;

import java.util.Arrays;

import org.junit.Before;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.testing.templateengine.context.IProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.context.web.SpringWebProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;

import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;

public class ThymeleafBaseTest {

	protected TestExecutor executor;

	@Before
	public void setup() {
//		IProcessingContextBuilder springPCBuilder = new SpringWebProcessingContextBuilder();
//		springPCBuilder.setApplicationContextConfigLocation("classpath:springConfig/spring.xml");
		executor = new TestExecutor();
//		executor.setProcessingContextBuilder(springPCBuilder);
		executor.setDialects(Arrays.asList(new IDialect[] { new StandardDialect(), new DataTablesDialect() }));
	}
}
