### Advanced :: Using extra conf (only JSP)

As for the `datatables:extraFile` tag, extra configuration allows you to add DataTables configuration which are not supported by the tag attributes, e.g. [custom column rendering](http://datatables.net/release-datatables/examples/advanced_init/column_render.html).

This is almost the same principle as for the `datatables:extraFile` but here, the `datatables:extraConf` tag allows you to specify the location of a file containing DataTables extra configuration to merge with the generated one. So this file must contain a javascript JSON-formatted object.

Internally, **Dandelion-Datatables** will generate a jQuery AJAX synchronous call to merge the DataTables configuration before initializing the table.

<p class="alert alert-info"><strong>Info!</strong><br /> The file can contain non-valid JSON content (e.g. function )</p>

#### Example
Considering the following DataTables configuration, stored in `/assets/js/datatables.extraConf.js` :

	{
	    "fnRowCallback" : function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
	        $('td:eq(0)', nRow).css('color', 'red');
	        $('td:eq(1)', nRow).css('color', 'green');
	        $('td:eq(2)', nRow).css('color', 'gold');
	        $('td:eq(3)', nRow).css('color', 'blue');
	        $('td:eq(4)', nRow).css('color', 'darkorange');
	        return nRow; 
	    }
	}

You just need to declare a `datatables:extraConf` tag as follows :

	<datatables:table id="myTableId" data="${persons}">
	   <datatables:column title="Id" property="id" />
	   <datatables:column title="Firstname" property="firstName" />
	   <datatables:column title="LastName" property="lastName" />
	   <datatables:column title="City" property="address.town.name" sortable="false" />
	   <datatables:column title="Mail" property="mail" />
	   <datatables:extraConf src="/assets/js/datatables.extraConf.js" />
	</datatables:table>
