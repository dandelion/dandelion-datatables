jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"scientific-pre" : function(a) {
		return parseFloat(a);
	},

	"scientific-asc" : function(a, b) {
		return ((a < b) ? -1 : ((a > b) ? 1 : 0));
	},

	"scientific-desc" : function(a, b) {
		return ((a < b) ? 1 : ((a > b) ? -1 : 0));
	}
});