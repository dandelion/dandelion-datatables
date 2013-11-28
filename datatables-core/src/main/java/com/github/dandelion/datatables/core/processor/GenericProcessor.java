package com.github.dandelion.datatables.core.processor;

import com.github.dandelion.datatables.core.configuration.ColumnConfiguration;
import com.github.dandelion.datatables.core.configuration.TableConfiguration;

/**
 * <p>
 * Common interface for all generic processors.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public interface GenericProcessor {

	/**
	 * <p>
	 * Processes the passed configuration and update the passed object with the
	 * processing result.
	 * 
	 * @param configuration
	 *            The configuration to process. This is always a String because
	 *            it can come from properties file, JSP tag attributes or
	 *            Thymeleaf attributes.
	 * @param objectToUpdate
	 *            The {@link ColumnConfiguration} or {@link TableConfiguration}
	 *            instance to update with the processing result.
	 */
	public void processConfiguration(String param, Object objectToUpdate);
}
