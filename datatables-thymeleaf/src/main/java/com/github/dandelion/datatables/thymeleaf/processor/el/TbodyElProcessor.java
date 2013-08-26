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
 * <p>
 * Element processor applied to the HTML <tt>tbody</tt> tag.
 * <p>
 * This processor adds the <code>dt:data</code> attribute to HTML <tt>tr</tt>
 * and <tt>td</tt> tags which will be duplicated (by <tt>th:each</tt> attribute)
 * and internally processed by other attributes to fill the
 * <code>HtmlTable</code> bean.
 * 
 * @author Thibault Duchateau
 */
public class TbodyElProcessor extends AbstractDatatablesElProcessor {

	public TbodyElProcessor(IElementNameProcessorMatcher matcher) {
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

					if (trChildTag.hasAttribute("th:each")) {

						trChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");

						for (Node grandchild : trChildTag.getChildren()) {

							if (grandchild != null && grandchild instanceof Element) {

								Element tdChildTag = (Element) grandchild;
								tdChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data", "internalUse");

								// The td nodes must be processed too (for
								// HtmlColumn creation)
								tdChildTag.setProcessable(true);
							}
						}
					}
				}
			}
		}

		return ProcessorResult.ok();
	}
}