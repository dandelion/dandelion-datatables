package com.github.dandelion.datatables.thymeleaf.processor.el;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractDatatablesElProcessor;

/**
 * TODO
 * 
 * @author Thibault Duchateau
 */
public class TheadElProcessor extends AbstractDatatablesElProcessor {

	public TheadElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 4000;
	}

	@Override
	protected ProcessorResult doProcessElement(Arguments arguments, Element element, HtmlTable table) {

		// All the tbody tag are iterated over
		for (Node child : element.getChildren()) {

			if (child != null && child instanceof Element) {

				Element trChildTag = (Element) child;
				String trChildTagName = trChildTag.getNormalizedName();

				// The tr nodes must be processed (for HtmlRow creation)
				trChildTag.setProcessable(true);

				if (trChildTagName != null && trChildTagName.equals("tr")) {

//					if (trChildTag.hasAttribute("th:each")) {

//						trChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data",
//								"internalUse");

						for (Node grandchild : trChildTag.getChildren()) {

							if (grandchild != null && grandchild instanceof Element) {

								Element thChildTag = (Element) grandchild;
								thChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data",
										"internalUse");

								// The td nodes must be processed too (for
								// HtmlColumn creation)
								thChildTag.setProcessable(true);
							}
						}
//					}
				}
			}
		}

		return ProcessorResult.ok();
	}
}