package com.github.dandelion.datatables.core.processor;

import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.Configuration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;
import com.github.dandelion.datatables.core.exception.ConfigurationProcessingException;

/**
 * <p>
 * Common interface for all processors to be applied on table's columns.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public interface ColumnProcessor {

	/**
	 * Processes the passed configuration and returns the right type needed by
	 * the {@link ColumnConfiguration} to initialize the configuration.
	 * 
	 * @param configuration
	 *            The configuration to process. This is always a String because
	 *            it can come from properties file, JSP tag attributes or
	 *            Thymeleaf attributes.
	 * @param the
	 *            {@link ColumnConfiguration} instance to update with the
	 *            processing result.
	 * @param the
	 *            {@link TableConfiguration} may be used to register some
	 *            extension depending on column's attributes.
	 * @param confToBeApplied
	 *            The global configuration to be applied on the
	 *            {@link TableConfiguration} may be useful to initialize linked
	 *            configurations. For example, the
	 *            {@link com.github.dandelion.datatables.core.extension.feature.AjaxFeature}
	 *            must be registered in the TableConfiguration only if
	 *            server-side processing in not enabled.
	 * @throws ConfigurationProcessingException
	 *             if something's goes wrong during the processing.
	 */
	public void processConfiguration(String param, ColumnConfiguration columnConfiguration, TableConfiguration tableConfiguration,
			Map<Configuration, Object> confToBeApplied) throws ConfigurationProcessingException;
}
