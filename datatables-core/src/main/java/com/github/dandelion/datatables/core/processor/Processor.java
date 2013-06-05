package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.AttributeProcessingException;

/**
 * <p>
 * Common interface for all processors to be applied on Dandelion-Datatables
 * attributes.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public interface Processor {

	/**
	 * Update the passed {@link TableConfiguration} instance depending on the
	 * value of the passed param. Some feature may be registered as well.
	 * 
	 * @param param
	 *            The parameter to process. This is always a String because it
	 *            can come from properties file, JSP tag attributes or Thymeleaf
	 *            attributes.
	 * @param the
	 *            {@link TableConfiguration} instance to update depending on the
	 *            param's value.
	 * @param confToBeApplied
	 *            The global configuration to be applied on the
	 *            {@link TableConfiguration} may be useful to initialize linked
	 *            configurations. For example, the
	 *            {@link com.github.dandelion.datatables.core.feature.AjaxFeature}
	 *            must be registered in the TableConfiguration only if
	 *            server-side processing in not enabled.
	 */
	public void process(String param, TableConfiguration tableConfiguration, Map<Configuration, Object> confToBeApplied)
			throws AttributeProcessingException;
}
