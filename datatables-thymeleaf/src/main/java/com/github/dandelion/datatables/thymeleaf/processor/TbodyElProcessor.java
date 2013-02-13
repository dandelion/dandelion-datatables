package com.github.dandelion.datatables.thymeleaf.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;

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
public class TbodyElProcessor extends AbstractElementProcessor {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(TbodyElProcessor.class);

	public TbodyElProcessor(IElementNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 4000;
	}

	/**
	 * 
	 * Parler de ça : Ability to precompute the processors to be applied to
	 * nodes, so that the sequence of operations to be performed at each node
	 * during template processing can be cached along with the DOM tree itself
	 * in many scenarios, significantly reducing processing time.
	 */
	@Override
	protected ProcessorResult processElement(Arguments arguments, Element element) {
		logger.debug("{} element found", element.getNormalizedName());

		// All the tbody tag are iterated over
		for (Node child : element.getChildren()) {

			if (child != null && child instanceof Element) {

				Element trChildTag = (Element) child;
				String trChildTagName = trChildTag.getNormalizedName();

				// The tr nodes must be processed (for HtmlRow creation)
				trChildTag.setProcessable(true);

				if (trChildTagName != null && trChildTagName.equals("tr")) {

					if (trChildTag.hasAttribute("th:each")) {

						trChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data",
								"internalUse");
						// trChildTag.setRecomputeProcessorsImmediately(true);

						for (Node grandchild : trChildTag.getChildren()) {

							if (grandchild != null && grandchild instanceof Element) {

								Element tdChildTag = (Element) grandchild;
								tdChildTag.setAttribute(DataTablesDialect.DIALECT_PREFIX + ":data",
										"internalUse");

								// The td nodes must be processed too (for
								// HtmlColumn creation)
								tdChildTag.setProcessable(true);
								// tdChildTag.setRecomputeProcessorsImmediately(true);
							}
						}
					}
				}
			}
		}

		return ProcessorResult.OK;
	}
}