<h3>Table attributes</h3>
<hr />

<h4>Description</h4>
<br />

The <code>&lt;table&gt;</code> attributes allow to set the main options for the table, ie sorting, filtering, theme activation, \...

Don\'t forget to add the prefix <code>dt:</code> before each attribute.

<br />
<h4>Usage</h4>

    <table dt:table="true" ...>
       ...
    </table>

<br />
<h4>Reference</h4>

<table id="tagReference" class="table table-striped table-bordered">
  <thead>
    <tr>
      <th>Name</th>
      <th>Description</th>
      <th>Value(s)</th>
      <th>Default</th>
    </tr>
  </thead>
  <tbody>
  <tr>
    <td>dt:table</td>
    <td><strong>(required)</strong> Enables DataTables4j in a Thymeleaf template. The value must be <code>true</code></td>
    <td>true|false</td>
    <td></td>
  </tr>
  <tr>
    <td>dt:appear</td>
    <td>Display mode used when the table has finished loading and must appear in the page. A duration can be set (in milliseconds) if the display mode is set to "fadein"</td>
    <td>block|fadein[,duration]</td>
    <td></td>
  </tr>
  <tr>
    <td>dt:cdn</td>
    <td>Enable the DataTables source files to be loaded from the Microsoft CDN (Content Delivery Framework) preventing you from hosting the files yourself.</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:info</td>
    <td>Enable or disable the table information display. This shows information about the data that is currently visible on the page, including information about filtered data if that action is being performed</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:paginate</td>
    <td>Enable or disable pagination</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:filter</td>
    <td>Enable or disable filtering of data</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:sort</td>
    <td>Enable or disable sorting of columns. Sorting of individual columns can be disabled by the "sortable" attribute of column tag</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:autowidth</td>
    <td>Enable or disable automatic column width calculation</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:export</td>
    <td>Enable or disable the export functionality</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:theme</td>
    <td>Name of a theme to activate for the current table</td>
    <td>bootstrap2|jqueryui</td>
    <td></td>
  </tr>
  <tr>
    <td>dt:themeOption</td>
    <td>Name of an option to activate in relation to the current activated theme.</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>dt:labels</td>
    <td>Relative URL of an AJAX loaded file which contains all the labels used in tables.</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>dt:serverside</td>
    <td>Tell DataTables to use server-side processing.</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:pipelining</td>
    <td>Enable pipelining data for paging when server-side processing is enabled.</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:pipesize</td>
    <td>Pipe size used when pipelining is enabled, i.e. times that the user can page before a request must be made of the server.</td>
    <td></td>
    <td>5</td>
  </tr>
  <tr>
    <td>dt:processing</td>
    <td>Enable or disable the display of a 'processing' indicator when the table is being processed (e.g. a sort). This is particularly useful for tables with large amounts of data where it can take a noticeable amount of time to sort the entries</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:jsonp</td>
    <td>Allows to retrieve JSON data from any domain name, regardless of XSS protection.</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  </tbody>
</table>

<link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css" />
<link rel="stylesheet" href="./css/tabletag.css" />
<script src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
<script src="./js/datatables.fixedheader.min.js"></script>
<script src="./js/tagreference.js"></script>