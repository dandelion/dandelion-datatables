/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings ) {
    return {
        "iStart":         oSettings._iDisplayStart,
        "iEnd":           oSettings.fnDisplayEnd(),
        "iLength":        oSettings._iDisplayLength,
        "iTotal":         oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
        "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
    };
}

/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
    "bootstrap_four_button": {
        "fnInit": function( oSettings, nPaging, fnDraw ) {
            var oLang = oSettings.oLanguage.oPaginate;
            var fnClickHandler = function ( e ) {
                e.preventDefault();
                if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
                    fnDraw( oSettings );
                }
            };
            
            $(nPaging).addClass('pagination').append(
                '<ul>'+
                    '<li class="prev disabled"><a href="#">&laquo; '+oLang.sFirst+'</a></li>'+
                    '<li class="disabled"><a href="#">&lsaquo; '+oLang.sPrevious+'</a></li>'+
                    '<li class="disabled"><a href="#">'+oLang.sNext+' &rsaquo;</a></li>'+
                    '<li class="next disabled"><a href="#">'+oLang.sLast+' &raquo;</a></li>'+
                '</ul>'
            );
            var els = $('a', nPaging);
            $(els[0]).bind( 'click.DT', { action: "first" }, fnClickHandler );
            $(els[1]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
            $(els[2]).bind( 'click.DT', { action: "next" }, fnClickHandler );
            $(els[3]).bind( 'click.DT', { action: "last" }, fnClickHandler );
        },
        
        "fnUpdate": function ( oSettings, fnDraw ) {
            var oPaging = oSettings.oInstance.fnPagingInfo();
            var an = oSettings.aanFeatures.p;
            var i;
            
            for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
                // Add / remove disabled classes from the static elements
                if ( oPaging.iPage === 0 ) {
                    $('li:first', an[i]).addClass('disabled');
                    $('li:nth-child(2)', an[i]).addClass('disabled');
                } else {
                    $('li:first', an[i]).removeClass('disabled');
                    $('li:nth-child(2)', an[i]).removeClass('disabled');
                }
                
                if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
                    $('li:last', an[i]).addClass('disabled');
                    $('li:nth-last-child(2)', an[i]).addClass('disabled');
                } else {
                    $('li:last', an[i]).removeClass('disabled');
                    $('li:nth-last-child(2)', an[i]).removeClass('disabled');
                }
            }
        }
    }
} );
