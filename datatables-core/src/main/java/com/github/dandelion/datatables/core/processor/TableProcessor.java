package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

/**
 * <p>
 * Common interface for all processors to be applied on table.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public interface TableProcessor {

	/**
	 * Processes the passed configuration and returns the right type needed by
	 * the {@link TableConfiguration} to initialize the configuration.
	 * 
	 * @param param
	 *            The parameter to process. This is always a String because it
	 *            can come from properties file, JSP tag attributes or Thymeleaf
	 *            attributes.
	 * @param the
	 *            {@link TableConfiguration} instance to update with the
	 *            processing result.
	 * @param confToBeApplied
	 *            The global configuration to be applied on the
	 *            {@link TableConfiguration} may be useful to initialize linked
	 *            configurations. For example, the
	 *            {@link com.github.dandelion.datatables.core.extension.feature.AjaxFeature}
	 *            must be registered in the TableConfiguration only if
	 *            server-side processing in not enabled.
	 */
	public void processConfiguration(String configuration, TableConfiguration tableConfiguration,
			Map<Configuration, Object> confToBeApplied);
}
