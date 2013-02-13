package com.github.dandelion.datatables.core.model;

import java.util.List;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.generator.AbstractConfigurationGenerator;

/**
 * <p>
 * Abstract superclass for all extensions. An extension can be a plugin (e.g.
 * Scroller, ColReorder), a feature (e.g. Bootstrap pagination type, filtering
 * add-on) or a theme (e.g. Bootstrap 2 theme).
 * <p>
 * An extension can be composed of :
 * <ul>
 * <li>one or more JsResource, i.e. Javascript code externalized in a file</li>
 * <li>one or more CssResource, i.e. CSS code externalized in a file</li>
 * <li>one or more Configuration, i.e. one or more specific DataTables
 * parameters that will be used during the DataTables initialization</li>
 * <li>an AbstractConfigurationGenerator if the extension needs its proper
 * configuration generator. The one used for the main DataTables configuration
 * is the {@link MainGenerator}. You can also take a look at the
 * {@link ColumnFilteringGenerator} to see the configuration generated for the
 * Column Filtering add-on.</li>
 * <li>a potential Javascript function name that will be called after DataTables
 * initialization. <br>
 * Example : columnFilter <blockquote>
 *
 * <pre>
 * oTable_myTableId = $('#myTableId').dataTable(oTable_myTableId_params).columnFilter({...});
 * </pre>
 *
 * </blockquote></li>
 * <li>specific Javascript snippets to add in the main JS resource, i.e. the
 * resource that contains the DataTables initilization Javascript code. You can
 * add snippet at multiple locations in this file thanks to the following
 * attributes :
 * <ul>
 * <li>beforeAll</li>
 * <li>beforeStartDocumentReady</li>
 * <li>afterStartDocumentReady</li>
 * <li>beforeEndDocumentReady</li>
 * <li>afterAll</li>
 * </ul>
 * These attributes can be visualized in the following Javascript snippet :
 * <blockquote>
 *
 * <pre>
 * => <b>BEFOREALL</b>
 * var oTable_tableId;
 * var oTable_tableId_params = {...};
 * => <b>BEFORESTARTDOCUMENTREADY</b>
 * $(document).ready(function(){
 *    => <b>AFTERSTARTDOCUMENTREADY</b>
 *    oTable_myTableId = $('#myTableId').dataTable(oTable_myTableId_params);
 *    => <b>BEFOREENDDOCUMENTREADY</b>
 * });
 * => <b>AFTERALL</b>
 * </pre>
 *
 * </blockquote></li>
 * </ul>
 *
 * @author Thibault Duchateau
 * @since 0.7.1
 */
public interface Extension {

    /**
     * Returns the extension's name.
     */
    String getName();

    /**
     * Returns the extension's version.
     */
    String getVersion();

    /**
     * Set the extension up.
     * <p>
     * The HtmlTable object is available if a particular configuration is
     * needed.
     * </p>
     *
     * @param table
     *            The HTML table.
     */
    void setup(HtmlTable table) throws BadConfigurationException;


    public StringBuffer getBeforeAll();

    public StringBuffer getAfterAll();

    public StringBuffer getAfterStartDocumentReady();

    public StringBuffer getBeforeEndDocumentReady();

    public List<JsResource> getJsResources();

    public void setJsResources(List<JsResource> jsResources);

    public List<CssResource> getCssResources();

    public void setCssResources(List<CssResource> cssResources);

    public List<Configuration> getConfs();

    public void setConfs(List<Configuration> confs);

    public void addJsResource(JsResource resource);

    public void addCssResource(CssResource resource);

    public void addConfiguration(Configuration conf);

    public AbstractConfigurationGenerator getConfigGenerator();

    public void setConfigGenerator(AbstractConfigurationGenerator configGenerator);

    public Boolean getAppendRandomNumber();

    public void setAppendRandomNumber(Boolean appendRandomNumber);

    public void appendToBeforeAll(String beforeAll);

    public void appendToBeforeStartDocumentReady(String beforeStartDocumentReady);

    public void appendToAfterStartDocumentReady(String afterStartDocumentReady);

    public void appendToBeforeEndDocumentReady(String beforeEndDocumentReady);

    public void appendToAfterAll(String afterAll);

    public String getFunction();

    public void setFunction(String function);
}
