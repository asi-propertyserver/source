</head>
<body>
<div class="ui-layout-north">
	<div class="headline">
		<div id="headline" title="">lade Daten ...</div>
	</div>
<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')"> 
	<div class="userinfo">
		<input id="logout-btn" type="button" value="'${contributorName}' abmelden" >
	</div>
</sec:authorize>
<sec:authorize access="!hasRole('ROLE_CONTRIBUTOR')"> 
	<c:if test="${username != null && username.length() > 0}">
		<div class="userinfo">
			<input id="logout-btn" type="button" value="'${username}' abmelden" >
		</div>
	</c:if>
</sec:authorize>
</div> <!-- .ui-layout-north -->
<div class="ui-layout-center">
