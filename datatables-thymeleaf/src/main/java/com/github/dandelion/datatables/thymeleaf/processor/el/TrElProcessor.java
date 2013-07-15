package com.github.dandelion.datatables.thymeleaf.processor.el;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.html.HtmlRow;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;

/**
 * <p>
 * Element processor applied to the <tt>tr</tt> HTML tag. Whenever Thymeleaf
 * meets a <tt>tr</tt> tag, a HtmlRow is added to the current table. So each
 * <tt>td</tt> tag will be able to update it.
 * <p>
 * This processor is only used for the export feature which is based on the
 * HtmlTable bean.
 * 
 * @author Thibault Duchateau
 */
public class TrElProcessor extends AbstractDatatablesElProcessor {

	public TrElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 4001;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {

		// The HtmlTable is updated with a new row
		if (table != null) {
			table.getBodyRows().add(new HtmlRow());
		}

		// Remove internal attribute
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
		}

		return ProcessorResult.OK;
	}
}