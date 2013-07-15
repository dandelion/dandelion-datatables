package com.github.dandelion.datatables.thymeleaf.processor.el;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

import com.github.dandelion.datatables.core.asset.DisplayType;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;

/**
 * <p>
 * Element processor applied to the <tt>td</tt> HTML tag. Whenever Thymeleaf
 * meets a <tt>td</tt> tag, a HtmlColumn is added to the last added HtmlRow.
 * <p>
 * Important note : the unique goal of this processor is to fill the HtmlTable
 * bean (with HtmlRows and HtmlColumns) for the export feature
 * 
 * @author Thibault Duchateau
 */
public class TdElProcessor extends AbstractDatatablesElProcessor {

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
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {

		if (table != null) {
			
			HtmlColumn column = null;
			String content = null;
			String attrValue = null;
			
			if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf") 
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls")
					|| element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx")){
				
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":csv");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":csv");
					column = new HtmlColumn(DisplayType.CSV);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xml");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xml");
					column = new HtmlColumn(DisplayType.XML);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":pdf");
					column = new HtmlColumn(DisplayType.PDF);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xls");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xls");
					column = new HtmlColumn(DisplayType.XLS);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
				if(element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx")) {
					attrValue = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					content =  StandardExpressionProcessor.processExpression(arguments, attrValue).toString();
					element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":xlsx");
					column = new HtmlColumn(DisplayType.XLSX);
					column.setContent(new StringBuilder(content));
					table.getLastBodyRow().addColumn(column);
				}
			}
			// If the element contains a Text node, the content of the text node
			// will be displayed in all formats
			else if (element.getFirstChild() instanceof Text) {
				table.getLastBodyRow().addColumn(((Text) element.getFirstChild()).getContent().trim());
			}
			// Otherwise, an empty cell will be displayed
			else{
				logger.warn("Only cells containing plain text are supported, those containing HTML code are still not !");
				table.getLastBodyRow().addColumn("");
			}
		}

		// Remove internal attribute
		if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
			element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
		}

		return ProcessorResult.OK;
	}
}