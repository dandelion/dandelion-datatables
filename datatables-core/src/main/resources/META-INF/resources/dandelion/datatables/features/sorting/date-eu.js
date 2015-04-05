jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"date-eu-pre" : function(date) {
		date = date.replace(" ", "");
		var eu_date, year;

		if (date == '') {
			return 0;
		}

		if (date.indexOf('.') > 0) {
			/*date a, format dd.mn.(yyyy) ; (year is optional)*/
			eu_date = date.split('.');
		} else {
			/*date a, format dd/mn/(yyyy) ; (year is optional)*/
			eu_date = date.split('/');
		}

		/*year (optional)*/
		if (eu_date[2]) {
			year = eu_date[2];
		} else {
			year = 0;
		}

		/*month*/
		var month = eu_date[1];
		if (month.length == 1) {
			month = 0 + month;
		}

		/*day*/
		var day = eu_date[0];
		if (day.length == 1) {
			day = 0 + day;
		}

		return (year + month + day) * 1;
	},

	"date-eu-asc" : function(a, b) {
		return ((a < b) ? -1 : ((a > b) ? 1 : 0));
	},

	"date-eu-desc" : function(a, b) {
		return ((a < b) ? 1 : ((a > b) ? -1 : 0));
	}
});