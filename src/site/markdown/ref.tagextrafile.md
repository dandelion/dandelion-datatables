<h3>ExtraFile Tag</h3>
<hr />

<h4>Description</h4>
<br />

Specify the location of an extra file containing Javascript code which will be merged with the main Javascript generated file.

<br />
<h4>Usage</h4>

    <datatables:table>
       ...
       <datatables:extraFile src="..." />
       ...
    </datatables:table>

<br />
<h4>Reference</h4>

<table id="tagReference" class="table table-striped table-bordered">
  <thead>
    <tr>
      <th>Name</th>
      <th>Description</th>
      <th>Type</th>
      <th>Value(s)</th>
      <th>Default</th>
    </tr>
  </thead>
  <tbody>
  <tr>
    <td>src</td>
    <td>(<strong>required</strong>) Location of the file containing Javascript code</td>
    <td>java.lang.String</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>insert</td>
    <td>Specify where the extra file content must be inserted in the main Javascript generated file</td>
    <td>java.lang.String</td>
    <td>BEFOREALL : at the beginning of the main Javascript generated file<br/>AFTERSTARTDOCUMENTREADY : just after the jQuery <a href="http://api.jquery.com/ready/">.ready() function</a><br />BEFORENDDOCUMENTREADY : just before the end of the jQuery <a href="http://api.jquery.com/ready/">.ready() function</a><br />AFTERALL : at the end of the main Javascript file</td>
    <td>BEFOREALL</td>
  </tr>
  </tbody>
</table>

<link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css" />
<link rel="stylesheet" href="./css/tabletag.css" />
<script src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js" ></script>
<script src="./js/datatables.fixedheader.min.js" ></script>
<script src="./js/tagreference.js" ></script>