### Feature comparison

You can find below the full list of features supported by **Dandelion-Datatables** and the correspondence between the JSP Taglib attributes and the Thymeleaf dialect ones.

<table id="features" class="table table-striped table-condensed table-hover">
  <thead>
    <tr>
      <th></th>
      <th>JSP Taglib</th>
      <th>Thymeleaf dialect</th>
      <th></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><h4>General features</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Sorting enablement</td>
      <td><a href="/ref-taglib/tagtable.html#sort">sort</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:sort">dt:sort</a></td>
      <td><a href="/basics/featurenablement.html" class="btn btn-mini btn-info">More details »</a></td>    
    </tr>
    <tr>
      <td>Filtering enablement</td>
      <td><a href="/ref-taglib/tagtable.html#filter">filter</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:filter">dt:filter</a></td>
      <td><a href="/basics/featurenablement.html" class="btn btn-mini btn-info">More details »</a></td>    
    </tr>
    <tr>
      <td>Paging enablement</td>
      <td><a href="/ref-taglib/tagtable.html#paginate">paginate</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:paginate">dt:paginate</a></td>
      <td><a href="/basics/featurenablement.html" class="btn btn-mini btn-info">More details »</a></td>    
    </tr>
    <tr>
      <td>Info enablement</td>
      <td><a href="/ref-taglib/tagtable.html#info">info</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:info">dt:info</a></td>
      <td><a href="/basics/featurenablement.html" class="btn btn-mini btn-info">More details »</a></td>    
    </tr>
    <tr>
      <td>Length change enablement</td>
      <td><a href="/ref-taglib/tagtable.html#lengthChange">lengthChange</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:lengthchange">dt:lengthchange</a></td>
      <td><a href="/basics/featurenablement.html" class="btn btn-mini btn-info">More details »</a></td>    
    </tr>
    <tr>
      <td><h4>Sorting</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Indivual sorting enablement</td>
      <td><a href="/ref-taglib/tagcolumn.html#sortable">sortable</a></td>
      <td><a href="/ref-thymeleaf/tmlth.html#dt:sortable">dt:sortable</a></td>
      <td><a href="/basics/sorting.html#Individual_column_sorting" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Sorting initialization</td>
      <td><a href="/ref-taglib/tagcolumn.html#sortInit">sortInit</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/basics/sorting.html#Sorting_initialisation" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Sorting direction control</td>
      <td><a href="/ref-taglib/tagcolumn.html#sortDirection">sortDirection</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/basics/sorting.html#Sorting_direction_control" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td><h4>Filtering</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Indivual filtering enablement (using default input)</td>
      <td><a href="/ref-taglib/tagcolumn.html#filterable">filterable</a></td>
      <td><a href="/ref-thymeleaf/tmlth.html#dt:filterable">dt:filterable</a></td>
      <td><a href="/basics/filtering.html#Individual_column_sorting" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Indivual filtering enablement (using drop down lists)</td>
      <td><a href="/ref-taglib/tagcolumn.html#filterType">filterType</a></td>
      <td><a href="/ref-thymeleaf/tmlth.html#dt:filterType">dt:filterType</a></td>
      <td><a href="/basics/filtering.html#Using_drop_down_lists" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Exclude column from filtering</td>
      <td><a href="/ref-taglib/tagcolumn.html#searchable">searchable</a></td>
      <td><a href="/ref-thymeleaf/tmlth.html#dt:searchable">dt:searchable</a></td>
      <td><a href="/basics/filtering.html#Exclude_column_from_filtering" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td><h4>Paging</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Changing pagination type</td>
      <td><a href="/ref-taglib/tagtable.html#paginationType">paginationType</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/basics/paging.html#Chaging_pagination_type" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td><h4>???</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Handling default value</td>
      <td><a href="/ref-taglib/tagcolumn.html#default">default</a></td>
      <td>Native support</td>
      <td><a href="/basics/defaultvalues.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Using CDN</td>
      <td><a href="/ref-taglib/tagtable.html#cdn">cdn</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:cdn">dt:cdn</a></td>
      <td><a href="/basics/cdn.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Changing table's appearing</td>
      <td><a href="/ref-taglib/tagtable.html#appear">appear</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:appear">dt:appear</a></td>
      <td><a href="/basics/changingappearing.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Multiple tables</td>
      <td><a href="/ref-taglib/tagtable.html#id">appear</a></td>
      <td>Just ensure to give a unique native HTML id</td>
      <td><a href="/basics/multiple.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Using implicit objects</td>
      <td><a href="/ref-taglib/tagtable.html#row">row</a></td>
      <td>Native support</td>
      <td><a href="/basics/implicit.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Specific column's content</td>
      <td><a href="/ref-taglib/tagcolumn.html#property">property</a> / <a href="/ref-taglib/tagtable.html#row">row</a></td>
      <td>Native support</td>
      <td><a href="/basics/customcontent.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Specific column's head</td>
      <td><a href="/ref-taglib/tagcolumnhead.html#uid">uid (columnHead)</a> / <a href="/ref-taglib/tagcolumn.html#uid">uid (column)</a></td>
      <td>Native support</td>
      <td><a href="/basics/customhead.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    
    <tr>
      <td><h4>i18n</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Internationalisation</td>
      <td><a href="/ref-taglib/tagtable.html#labels">labels</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:labels">dt:labels</a></td>
      <td><a href="/advanced/i18n.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    
    <tr>
      <td><h4>Export</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Export enablement</td>
      <td><a href="/ref-taglib/tagtable.html#export">export</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:export">dt:export</a></td>
      <td><a href="/export/activateexport.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Adapting links' style</td>
      <td><a href="/ref-taglib/tagexport.html#type">type</a> / <a href="/ref-taglib/tagexport.html#label">label</a> / <a href="/ref-taglib/tagexport.html#cssStyle">cssStyle</a> / <a href="/ref-taglib/tagexport.html#cssClass">cssClass</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/export/customlink.html#Link_style" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Adapting links' position</td>
      <td><a href="/ref-taglib/tagexport.html#type">type</a> / <a href="/ref-taglib/tagtable.html#exportLinks">exportLinks</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/export/customlink.html#Link_position" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Remove header (all formats)</td>
      <td><a href="/ref-taglib/tagexport.html#type">type</a> / <a href="/ref-taglib/tagexport.html#removeHeader">removeHeader</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/export/customlink.html#Remove_header_all_formats" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Column auto-sizing (XLS/XLSX)</td>
      <td><a href="/ref-taglib/tagexport.html#type">type</a> / <a href="/ref-taglib/tagexport.html#autoSize">autoSize</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/export/customlink.html#Column_auto-sizing_only_XLSXLSX" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Adapt export content</td>
      <td><a href="/ref-taglib/tagcolumn.html#display">display</a></td>
      <td><span class="icon-remove"></span></td>
      <td><a href="/export/adaptcontent.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    
    <tr>
      <td><h4>Themes</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td><a href="http://twitter.github.com/bootstrap/">Bootstrap 2</a></td>
      <td><a href="/ref-taglib/tagtable.html#theme">theme</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:theme">dt:theme</a></td>
      <td><a href="/themes/bootstrap2.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
	<tr>
      <td><a href="http://jqueryui.com/themeroller/">jQuery UI</a></td>
      <td><a href="/ref-taglib/tagtable.html#theme">theme</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:theme">dt:theme</a></td>
      <td><a href="/themes/bootstrap2.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>

    <tr>
      <td><h4>AJAX</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td>Using AJAX sources</td>
      <td><a href="/ref-taglib/tagtable.html#url">url</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:url">dt:url</a></td>
      <td><a href="/ajax/ajaxsource.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Server-side processing</td>
      <td><a href="/ref-taglib/tagtable.html#serverSide">serverSide</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:serverside">dt:serverSide</a></td>
      <td><a href="/ajax/serverside.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Processing indication</td>
      <td><a href="/ref-taglib/tagtable.html#processing">processing</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:processing">dt:processing</a></td>
      <td><a href="/ajax/serverside.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td>Pipelining data</td>
      <td><a href="/ref-taglib/tagtable.html#pipelining">pipelining</a> / <a href="/ref-taglib/tagtable.html#pipeSize">pipeSize</a></td>
      <td><a href="/ref-thymeleaf/tmltable.html#dt:pipelining">dt:pipelining</a> / <a href="/ref-thymeleaf/tmltable.html#dt:pipesize">dt:pipesize</a></td>
      <td><a href="/ajax/serverside.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    
    <tr>
      <td><h4>Plugins</h4></td>
      <td colspan="3"></td>
    </tr>
    <tr>
      <td><a href="http://datatables.net/extras/colreorder/">ColReorder</a></td>
      <td><a href="/ref-taglib/tagtable.html#colReorder">colReorder</a></td>
      <td><a href="/ref-thymeleaf/tmlthead.html#dt:colReorder">dt:colReorder</a></td>
      <td><a href="/plugins/colreorder.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td><a href="http://datatables.net/extras/scroller/">Scroller</a></td>
      <td><a href="/ref-taglib/tagtable.html#scroller">scroller</a></td>
      <td><a href="/ref-thymeleaf/tmlthead.html#dt:scroller">dt:scroller</a></td>
      <td><a href="/plugins/scroller.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
    <tr>
      <td><a href="http://datatables.net/extras/fixedheader/">FixedeHeader</a></td>
      <td><a href="/ref-taglib/tagtable.html#fixedHeader">fixedHeader</a> / <a href="/ref-taglib/tagtable.html#offsetTop">offsetTop</a></td>
      <td><a href="/ref-thymeleaf/tmlthead.html#dt:fixedheader">dt:fixedheader</a></td>
      <td><a href="/plugins/fixedheader.html" class="btn btn-mini btn-info">More details »</a></td>   
    </tr>
  </tbody>
</table>

<link rel="stylesheet" href="./css/featurecomparison.css" />