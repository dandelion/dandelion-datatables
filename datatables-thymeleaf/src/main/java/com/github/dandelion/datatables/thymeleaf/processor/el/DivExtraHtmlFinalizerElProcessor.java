package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.util.DOMUtils;

import com.github.dandelion.datatables.core.extension.feature.ExtraHtml;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;
import com.github.dandelion.datatables.thymeleaf.processor.config.ConfType;
import com.github.dandelion.datatables.thymeleaf.util.DomUtils;
import com.github.dandelion.datatables.thymeleaf.util.RequestUtils;

/**
 * <p>
 * Element processor applied to the HTML {@code div} tag with the
 * {@code tmp:internalUseExtraHtml} pair in order to finalize the configuration
 * of an {@link ExtraHtml}.
 * 
 * @author tduchateau
 * @since 0.10.1
 */
public class DivExtraHtmlFinalizerElProcessor extends AbstractElProcessor {

   private static Logger logger = LoggerFactory.getLogger(DivExtraHtmlFinalizerElProcessor.class);

   public DivExtraHtmlFinalizerElProcessor(IElementNameProcessorMatcher matcher) {
      super(matcher);
   }

   @Override
   public int getPrecedence() {
      return 8005;
   }

   @SuppressWarnings("unchecked")
   protected ProcessorResult doProcessElement(Arguments arguments, Element element, HttpServletRequest request,
         HttpServletResponse response, HtmlTable htmlTable) {

      Map<String, Map<ConfType, Object>> configs = (Map<String, Map<ConfType, Object>>) RequestUtils.getFromRequest(
            DataTablesDialect.INTERNAL_BEAN_CONFIGS, request);

      String tableId = ((Element) element.getParent()).getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":conf");
      String uid = element.getAttributeValue(DataTablesDialect.DIALECT_PREFIX + ":uid");

      if (configs != null) {
         if (configs.containsKey(tableId)) {

            List<ExtraHtml> extraHtmls = (List<ExtraHtml>) configs.get(tableId).get(ConfType.EXTRAHTML);
            if (extraHtmls != null && !extraHtmls.isEmpty()) {

               for (ExtraHtml extraHtml : extraHtmls) {
                  if (extraHtml.getUid().equals(uid)) {

                     Element e = DomUtils.findElement((Element) element.getParent(), "div",
                           DataTablesDialect.DIALECT_PREFIX + ":uid", uid);

                     StringBuilder sb = new StringBuilder();
                     for (Node child : e.getChildren()) {
                        sb.append(DOMUtils.getHtml5For(child).replaceAll("[\n\r]", "").trim());
                     }

                     extraHtml.setContent(sb.toString());
                  }
               }
            }
         }
         else {
            logger.warn("No configuration was found for the table with id '{}'", tableId);
         }
      }

      return ProcessorResult.ok();
   }

}
