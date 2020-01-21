<%@ include file="doctype.jsp"%>
<script type="text/javascript"
	src="/resources/js/tree.js?ver=${version}" charset="UTF-8"></script>
<title>freeBIM - Hierarchie</title>
<%@ include file="layout-begin.jsp"%>

<div class="ui-layout-center" id="root1"></div>
<div class="ui-layout-east" id="root2"></div>

<script type="text/javascript">
	jq(document).ready(function() {
		jq("#headline").html("freeBIM - Struktur");

		jq('.ui-layout-center').layout({
			west__size:				0.5
		});
	
		var tree1 = Object.create(at.freebim.db.tree);
		tree1.rootId = "root1";
		tree1.getChildren();
		
		tree2 = Object.create(at.freebim.db.tree);
		tree2.rootId = "root2";
		tree2.getChildren();
	});
</script>
<%@ include file="layout-end.jsp"%>
