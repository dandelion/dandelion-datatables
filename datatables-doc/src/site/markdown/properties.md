<h3>Configuration properties</h3>
<hr />

<h4>Description</h4>
<br />

<strong>Dandelion-DataTables</strong> internally uses a configuration file which contains properties that affect all the table of your application.

But you can of course override the default configuration using :

 * either your own configuration file, which will affect all tables too. Just override the property you need and for others, Dandelion-DataTables will use the default ones
 * or the [Prop tag](./ref-taglib/tagprop.html), which allow you to locally override the table configuration

If you decide to create your own configuration file, just ensure that :
 
 * The file has the name <strong>datatables.properties</strong>
 * The file is located at the root of the application classpath (e.g. under `src/main/resources` if you use Maven)

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
    <td>compressor.enable</td>
    <td>Enable or disable the web resources compression</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>compressor.mode</td>
    <td>Mode used for compression. ALL : JS and CSS, JS : only JS are compressed, CSS : only CSS are compressed</td>
    <td>ALL|JS|CSS</td>
    <td>ALL</td>
  </tr>
  <tr>
    <td>compressor.class</td>
    <td>Java class name for the compressor implementation. By default, Dandelion-DataTables uses the YuiCompressor library but if the class is not found in the application classpath, compression is disabled.</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.extras.compression.YuiResourceCompressor</td>
  </tr>
  <tr>
    <td>compressor.munge</td>
    <td>Whether the compressor must obfuscate the Javascript and CSS code or not.</td>
    <td>true|false</td>
    <td>true</td>
  </tr>
  <tr>
    <td>compressor.preserveSemiColons</td>
    <td>Whether the compressor must preserve semi-colons, even if it's not necessary (if the next character is a right-curly), or not</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>compressor.disableOptimizations</td>
    <td>Whether the compressor must disable the micro optimizations or not. Micro optimizations concern : <ul>
	 <li>object member access : Transforms obj["foo"] into obj.foo whenever
	 possible, saving 3 bytes</li>
	 <li>object litteral member declaration : Transforms 'foo': ... into foo:
	 ... whenever possible, saving 2 bytes</li>
	 </ul></td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>aggregator.enable</td>
    <td>Enable or disable the web resources aggregation</td>
    <td>true|false</td>
    <td>false</td>
  </tr>
  <tr>
    <td>aggregator.mode</td>
    <td>Specify the aggregation mode.</td>
    <td>ALL|PLUGINS_JS|PLUGINS_CSS</td>
    <td>ALL</td>
  </tr>
  <tr>
    <td>export.types</td>
    <td>Comma-separated list of allowed export types</td>
    <td>csv|xml|pdf|xls|xlsx</td>
    <td>xml,csv,pdf,xls,xlsx</td>
  </tr>
  <tr>
    <td>export.csv.class</td>
    <td>Java class name to use for the CSV export</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.core.export.CsvExport</td>
  </tr>
  <tr>
    <td>export.xml.class</td>
    <td>Java class name to use for the XML export</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.core.export.XmlExport</td>
  </tr>
  <tr>
    <td>export.xls.class</td>
    <td>Java class name to use for the XLS export.  <span class="label label-warning">Warning :</span> don't forget the additional dependency !</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.extras.export.poi.XlsExport</td>
  </tr>
  <tr>
    <td>export.xlsx.class</td>
    <td>Java class name to use for the XLSX export.  <span class="label label-warning">Warning :</span> don't forget the additional dependency !</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.extras.export.poi.XlsxExport</td>
  </tr>
  <tr>
    <td>export.pdf.class</td>
    <td>Java class name to use for the PDF export. <span class="label label-warning">Warning :</span> don't forget the additional dependency !</td>
    <td></td>
    <td style="font-size: 12px;">com.github.dandelion.datatables.extras.export.itext.PdfExport</td>
  </tr>
  </tbody>
</table>

<link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css" />
<link rel="stylesheet" href="./css/tabletag.css" />
<script src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js" ></script>
<script src="./js/datatables.fixedheader.min.js" ></script>
<script src="./js/tagreference.js" ></script>