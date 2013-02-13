package com.github.dandelion.datatables.thymeleaf.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import com.github.dandelion.datatables.core.model.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * Element processor applied to the <tt>td</tt> HTML tag. Whenever Thymeleaf
 * meets a <tt>td</tt> tag, a HtmlColumn is added to the last added HtmlRow.
 * 
 * @author Thibault Duchateau
 */
public class TdElProcessor extends AbstractElementProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TdElProcessor.class);
	public TdElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 4002;
	}

	@Override
	protected ProcessorResult processElement(Arguments arguments, Element element) {

		// Get HtmlTable POJO from local variables
		HtmlTable htmlTable = Utils.getTable(arguments);

		if (htmlTable != null) {
			// If the first child of the td node is a Text, we get this Text to
			// fill in the td
			if (element.getFirstChild() instanceof Text) {
				htmlTable.getLastBodyRow().addColumn(
						((Text) element.getFirstChild()).getContent().trim());
			} 
			// Else we look for the first Text node
			else {
				// TODO				
				// Node tdText = DomUtils.getNodeByType(element, Text.class);

				logger.warn("Only cells containing plain text are supported, those containing HTML code are still not !");
				htmlTable.getLastBodyRow().addColumn("");
			}
		}

		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
		}

		return ProcessorResult.OK;
	}
}