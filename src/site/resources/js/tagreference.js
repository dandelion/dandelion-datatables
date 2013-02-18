$(document).ready(function () {
	var oTable = $('#tagReference').dataTable({
		"bInfo" : false,
		"bPaginate" : false,
		"bLengthChange" : false,
		"aaSorting": []
	});
	new FixedHeader( oTable );
});