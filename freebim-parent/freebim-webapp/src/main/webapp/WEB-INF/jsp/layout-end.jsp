</div> <!-- .ui-layout-center -->
<div class="ui-layout-south">
<sec:authorize access="hasRole('ROLE_USERMANAGER')"> 
	<input id="btn_users" type="button" value="Benutzer" >
	<input id="btn_libraries" type="button" value="Bibliotheken" >
</sec:authorize>
	<input class="btn_contributors" type="button" value="Bearbeiter" >
	<input id="btn_units" type="button" value="Einheiten" >
	<input id="btn_phases" type="button" value="Phasen" >
	<input id="btn_tree" type="button" value="Struktur" >
	<input id="btn_search" type="button" value="Suche" >
	<script type="text/javascript">
		jq(document).ready(function(){
<sec:authorize access="hasRole('ROLE_USERMANAGER')"> 
			jq("#btn_users").click(function(){ document.location.href="/user"; });
			jq("#btn_libraries").click(function(){ document.location.href="/libraries"; });
</sec:authorize>
			jq(".btn_contributors").click(function(){ document.location.href="/contributor"; });
			jq("#btn_units").click(function(){ document.location.href="/unit"; });
			jq("#btn_phases").click(function(){ document.location.href="/phase"; });
			jq("#btn_tree").click(function(){ document.location.href="/tree"; });
			jq("#btn_search").click(function(){ document.location.href="/search"; });
			
			jq("#freebim-progress").dialog({
				title: "freeBIM - Prozess",
			    modal: true,
			    autoOpen: false,
			    width: 300,
			    height: 200,
			    show: "slideDown", 
			    hide: 'slideUp'
			});	
		});
	</script>
</div> <!-- .ui-layout-south -->
<div id="freebim-progress">
	<img alt="progress" src="/resources/ajax-progress.gif">
	<p>in Bearbeitung ...</p>
</div>
</body>
</html>