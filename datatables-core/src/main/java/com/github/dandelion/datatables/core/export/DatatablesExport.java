package com.github.dandelion.datatables.core.export;

import java.io.OutputStream;

import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Interface for all export classes.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public interface DatatablesExport {

   /**
    * <p>
    * Initialize the implementation classes with all needed informations.
    * </p>
    * <p>
    * Usually, only the HtmlTable is needed, because it already contains lots of
    * information.
    * </p>
    * 
    * @param table
    *           The HTML table containing all needed information for the export.
    */
   public void initExport(HtmlTable table);

   /**
    * <p>
    * The main export method that is called by Dandelion-datatables in charge of
    * writing in the output.
    * </p>
    * 
    * @param output
    *           The stream to fill and which will override the default response
    *           during export.
    * @throws DandelionException
    *            if something goes wrong during the export.
    */
   public void processExport(OutputStream output);
}
