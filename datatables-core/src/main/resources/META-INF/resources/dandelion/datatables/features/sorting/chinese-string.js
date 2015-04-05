jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"chinese-string-asc" : function(s1, s2) {
		return s1.localeCompare(s2);
	},

	"chinese-string-desc" : function(s1, s2) {
		return s2.localeCompare(s1);
	}
});