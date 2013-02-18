$(document).ready(function () {
	$('#exampleTable').removeClass('table table-striped');
	var oTable = $('#exampleTable').dataTable({
		"aaSorting": [],
		"bJQueryUI": true
	});	
});