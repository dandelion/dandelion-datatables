jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"signed-num-pre" : function(a) {
		return (a == "-" || a === "") ? 0 : a.replace('+', '') * 1;
	},

	"signed-num-asc" : function(a, b) {
		return ((a < b) ? -1 : ((a > b) ? 1 : 0));
	},

	"signed-num-desc" : function(a, b) {
		return ((a < b) ? 1 : ((a > b) ? -1 : 0));
	}
});