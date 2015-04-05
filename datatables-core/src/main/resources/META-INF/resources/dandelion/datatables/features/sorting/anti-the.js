jQuery.extend( jQuery.fn.dataTableExt.oSort, {
    "anti-the-pre": function ( a ) {
        return a.replace(/^the /i, "");
    },
 
    "anti-the-asc": function ( a, b ) {
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
 
    "anti-the-desc": function ( a, b ) {
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
} );