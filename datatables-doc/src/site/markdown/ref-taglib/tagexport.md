### Export Tag
<br />

#### Description
<br />

Configure the HTML link and properties for a given export type.

<br />
#### Usage

    <datatables:table>
       ...
       <datatables:export type="..." />
       ...
    </datatables:table>

<br />
#### Reference

<table id="reference" class="table table-striped table-bordered">
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
    <td>type</td>
    <td>(<strong>required</strong>) Comma-separated list of activated exports.</td>
    <td>csv[,xml|pdf|xls|xlsx]</td>
    <td></td>
  </tr>
  <tr>
    <td>label</td>
    <td>Label of the HTML link</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>cssStyle</td>
    <td>CSS style applied to the HTML link</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>cssClass</td>
    <td>CSS class applied to the HTML link</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>includeHeader</td>
    <td>Indicated whether headers should be added in export or not (only for compatible export : CSV, XLS)</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <!--
  <tr>
    <td>area</td>
    <td>Indicates how much data should be exported. Choose <tt>list</tt> for the full list or <tt>current</tt> to only export the data that is currently being shown</td>
    <td>list|current</td>
    <td>list</td>
  </tr>
  -->
  <tr>
    <td>fileName</td>
    <td>Name of the file containing exported data</td>
    <td></td>
    <td>The export type uppercased</td>
  </tr>
  <tr>
    <td>autoSize</td>
    <td>Indicates whether columns should be auto-sized or not (only for compatible export : XLS) (See the <a href="http://poi.apache.org/spreadsheet/quick-guide.html#Autofit">reference</a>)</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  </tbody>
</table>

<link rel="stylesheet" href="//ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css" />
<script src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js"></script>
<script src="./../js/site_reference.js"></script>