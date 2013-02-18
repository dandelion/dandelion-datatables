// Javascript executed in all pages
$(document).ready(function() {
	$('.collapse').collapse({
		toggle : false
	});
	$('#myCollapsibleExample').on('click', function() {
		// ...
		$('#demo').collapse('toggle'); // Here is the magic trick
		// ...
	});
});