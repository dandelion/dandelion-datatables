<h3>Th attributes</h3>
<hr />

<h4>Description</h4>
<br />

The <code>&lt;th&gt;</code> attributes allow to configure the columns.

<br />
<h4>Usage</h4>

    <table dt:table="true" ...>
        <thead ...>
            <tr>
                <th>...</th>
                <th>...</th>
                <th dt:filterable="true">...</th>
                <th>...</th>
                <th>...</th>
            </tr>
        </thead>
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
    <td>dt:sortable</td>
    <td>Enable or disable sorting on column</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>dt:filterable</td>
    <td>Enable or disable filtering on column</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>dt:filterType</td>
    <td>If <code>dt:filterable</code> is enabled, you can choose the filter type between 'select' and 'input', adding either select box or input field in the table's footer</td>
    <td>select|input</td>
    <td>input</td>
  </tr>
  <tr>
    <td>dt:property</td>
    <td>Property (or nested property) that must is read from the AJAX source to fill in the corresponding column</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>dt:default</td>
    <td>Default value if the property's value is null</td>
    <td>java.lang.String</td>
    <td>Empty string</td>
  </tr>
  </tbody>
</table>

<link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css" />
<link rel="stylesheet" href="./css/tabletag.css" />
<script src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
<script src="./js/datatables.fixedheader.min.js"></script>
<script src="./js/tagreference.js"></script>