package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.exception.ConfigurationLoadingException;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;
import com.github.dandelion.datatables.core.exception.DataTableProcessingException;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;

public class ColumnFinalizerProcessor extends AbstractDatatablesElProcessor {

	public ColumnFinalizerProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 8005;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {
		HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();

		// Get the TH content
		String content = null;
		if (element.getFirstChild() instanceof Text) {
			content = ((Text) element.getFirstChild()).getContent().trim();
		} else {
			content = element.getChildren().toString();
		}

		// Init a new column
		HtmlColumn htmlColumn = new HtmlColumn(true, content);

		Map<Configuration, Object> stagingConf = (Map<Configuration, Object>) request
				.getAttribute(DataTablesDialect.INTERNAL_COLUMN_LOCAL_CONF);

		try {
			Configuration.applyColumnConfiguration(htmlColumn.getColumnConfiguration(), table.getTableConfiguration(),
					stagingConf);
		} catch (ConfigurationProcessingException e) {
			throw new DataTableProcessingException(e);
		} catch (ConfigurationLoadingException e) {
			throw new DataTableProcessingException(e);
		}

		// Add it to the table
		if (table != null) {
			table.getLastHeaderRow().addHeaderColumn(htmlColumn);
		}

		// Let's clean the TR attributes
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
		}

		return ProcessorResult.ok();
	}
}