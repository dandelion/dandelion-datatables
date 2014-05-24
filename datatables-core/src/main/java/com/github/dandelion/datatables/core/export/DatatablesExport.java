package com.github.dandelion.datatables.core.export;

import java.io.OutputStream;

import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Interface for all export classes.
 * 
 * @author Thibault Duchateau
 */
public interface DatatablesExport {

	/**
	 * <p>
	 * Initialize the implementation classes with all needed informations.
	 * <p>
	 * Usually, only the HtmlTable is needed, because it already contains lots
	 * of information.
	 * 
	 * @param table
	 *            The HTML table containing all needed information for the
	 *            export.
	 */
	public void initExport(HtmlTable table);

	/**
	 * The main export method that is called by Dandelion-datatables in charge
	 * of writing in the output.
	 * 
	 * @param output
	 *            The stream to fill and which will override the default
	 *            response during export.
	 * @throws ExportException
	 *             if something goes wrong during the export.
	 */
	public void processExport(OutputStream output);
}
