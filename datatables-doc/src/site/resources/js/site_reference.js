$(document).ready(function () {
	
	var search = "";
	if ( window.location.hash !== "" ) {
		search = window.location.hash.substring( 1 );
	}
	
	var oTable = $('#reference').dataTable( {
		"bInfo" : false,
		"bPaginate": false,
		"bLengthChange" : false,
		"bSortClasses": false,
		"aaSorting": [],
		"oSearch": { "sSearch": search },
		"fnInitComplete": function () {
			this.fnAdjustColumnSizing();
			$('div.dataTables_filter input').focus();
		}
	} );
});