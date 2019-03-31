/**
 * 
 */
var myLayout;

function doLayout() {
	//$browser.msie = false;
	myLayout = jq('body').layout({
		north__slidable:			false
		,north__resizable:			false
		,north__spacing_open:		0
		,north__spacing_closed:		0
		,south__resizable:			false
		//,north__size:				30
		//,south__size:				30
		,south__spacing_open:		0
		,south__spacing_closed:		0
	});
	myLayout.allowOverflow("north");
}
function refreshLayout() {
}

jq(document).ready(function () {
	doLayout();
	jq("#logout-btn").off("click").click(function() {
		document.location.href="/j_myApplication_logout";
	});
});

