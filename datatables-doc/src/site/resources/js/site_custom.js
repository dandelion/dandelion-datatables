// Javascript executed in all pages
$(document).ready(function () {
	$('footer div.row').html($('footer div.row').html() + "<a href='./acknowledgments.html'>Acknowledgment</a>");
	$('#twitter').css('margin-top', '10px');
	$('#poweredBy').append('<a href="http://www.cloudbees.com/" class="poweredBy"><img src="./images/logo_cloudbees_built-on.png" width="100px" height="40px" /></a><div class="clear"></div>');
	$('#poweredBy').css('text-align', 'center');
});