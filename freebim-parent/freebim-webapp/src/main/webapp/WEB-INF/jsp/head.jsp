<%@page import="at.freebim.db.service.DateService"%>
<%@page import="at.freebim.db.domain.base.State"%>
<%@page import="at.freebim.db.domain.base.LifetimeBaseNode"%>
<%@page import="at.freebim.db.service.LifetimeBaseNodeService"%>
<%@page
	import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page import="at.freebim.db.domain.base.rel.RelationTypeEnum"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<!-- version: ${appVersion}<c:if test="${release}"> release</c:if>, build date: ${appBuildTimestamp} -->

<script>
		window.validity = Number("${tokenValidity}");
		window.refresh_token_validity = Number("${refresh_token_validity}")
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="/resources/favicon.ico"
	type="image/x-icon" />
<link rel="icon" href="/resources/favicon.ico" type="image/x-icon" />
<link type="text/css" rel="stylesheet"
	href="/resources/css/jquery/smoothness/jquery-ui-1.10.4.custom.min.css?ver=${appVersion}">
<link type="text/css" rel="stylesheet"
	href="/resources/css/layout-default-latest.css?ver=${appVersion}">
<sec:authorize
	access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR') || hasRole('ROLE_USERMANAGER')">
	<link type="text/css" rel="stylesheet"
		href="/resources/css/bsdd.css?ver=${appVersion}">
	<link type="text/css" rel="stylesheet"
		href="/resources/css/jsTable.css?ver=${appVersion}">
</sec:authorize>
<sec:authorize
	access="hasRole('ROLE_CONTRIBUTOR') || hasRole('ROLE_USERMANAGER')">
	<link type="text/css" rel="stylesheet"
		href="/resources/css/jsForm.css?ver=${appVersion}">
</sec:authorize>
<link type="text/css" rel="stylesheet"
	href="/resources/css/freebim.css?ver=${appVersion}">
<%--
<script type="text/javascript" src="/resources/lib/jquery.qrcode.min.js?ver=${appVersion}" charset="UTF-8"></script>
--%>
<script type="text/javascript"
	src="/resources/lib/jquery-1.11.0.min.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/lib/jquery-ui-1.10.4.custom.min.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/lib/jquery.layout-latest.min.js?ver=${appVersion}"
	charset="UTF-8"></script>
<sec:authorize access="hasRole('ROLE_USERMANAGER')">
	<script type="text/javascript"
		src="/resources/lib/dropzone.js?ver=${appVersion}" charset="UTF-8"></script>
</sec:authorize>
<%-- JavaScript logging library from http://log4javascript.org --%>
<c:if test="${release}">
	<script type="text/javascript">var log4javascript_disabled = true;</script>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<script type="text/javascript">log4javascript_disabled = false;</script>
	</sec:authorize>
</c:if>
<script type="text/javascript"
	src="/resources/lib/log4javascript/log4javascript.js?ver=${appVersion}"></script>
<%-- <link type="text/css" rel="stylesheet" href="/resources/lib/log4javascript/main.css?ver=${appVersion}">  --%>

<script type="text/javascript"
	src="/resources${minified}/lib/objectKeys.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/lib/json2.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/lib/jsCookie.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/lib/jsDownload.js?ver=${appVersion}"
	charset="UTF-8"></script>
<c:choose>
	<c:when test="${release}">
		<script type="text/javascript">
var freebim_debug = false;
var freebim_version = "${appVersion}";
</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
var freebim_debug = true;
var freebim_version = "${appVersion}";
</script>
	</c:otherwise>
</c:choose>
<script type="text/javascript"
	src="/resources${minified}/lib/i18n.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/main.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/request.js?.ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/tabs.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/domain.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/reltype.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/nodeFields.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/lib/jsForm.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/lib/jsTable.js?ver=${appVersion}"
	charset="UTF-8"></script>
<sec:authorize
	access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR') || hasRole('ROLE_USERMANAGER')">
	<script type="text/javascript"
		src="/resources${minified}/js/ctxmenu.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/paramlist.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/delegate.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/bsdd.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/lib/ifd.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/lib/IfcGuid.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/search.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources/lib/jquery.ui-contextmenu.min.js?ver=${appVersion}"
		charset="UTF-8"></script>
</sec:authorize>
<script type="text/javascript"
	src="/resources${minified}/js/tree.js?ver=${appVersion}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/rel.js?ver=${appVersion}" charset="UTF-8"></script>
<sec:authorize access="hasRole('ROLE_ADMIN')">
	<script type="text/javascript"
		src="/resources${minified}/js/admin.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources/lib/trumbowyg/trumbowyg.min.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<link rel="stylesheet"
		href="/resources/lib/trumbowyg/ui/trumbowyg.min.css?ver=${appVersion}">
</sec:authorize>
<script type="text/javascript"
	src="/resources/lib/d3.min.js?ver=${appVersion}" charset="UTF-8"></script>
<%-- TIP for d3 <script type="text/javascript" src="/resources/lib/index.js?ver=${appVersion}" charset="UTF-8"></script> --%>
<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
	<script type="text/javascript"
		src="/resources${minified}/js/merge.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<link type="text/css" rel="stylesheet"
		href="/resources/css/graph.css?ver=${appVersion}">
	<script type="text/javascript"
		src="/resources${minified}/lib/jsCss.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/graph.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<link type="text/css" rel="stylesheet"
		href="/resources/css/statistic.css?ver=${appVersion}">
	<script type="text/javascript"
		src="/resources${minified}/js/statistic.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/problems.js?ver=${appVersion}"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="/resources${minified}/js/pset.js?ver=${appVersion}"
		charset="UTF-8"></script>
</sec:authorize>

<%-- jQuery contextmenu 
<link type="text/css" rel="stylesheet" href="/resources/lib/jquery-contextmenu/jquery.contextMenu.css?ver=${appVersion}">
<script type="text/javascript" src="/resources/lib/jquery-contextmenu/jquery.contextMenu.js?ver=${appVersion}" charset="UTF-8"></script>
<script type="text/javascript" src="/resources/lib/jquery-contextmenu/jquery.ui.position.js?ver=${appVersion}" charset="UTF-8"></script>
 end of jQuery contextmenu --%>

<sec:authorize
	access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR') || hasRole('ROLE_USERMANAGER')">
	<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
		<script type="text/javascript">
at.freebim.db.relations.edit = true;
</script>
	</sec:authorize>
	<script type="text/javascript">
at = at || {};
at.freebim = at.freebim || {};
at.freebim.db = at.freebim.db || {};
at.freebim.db.domain = at.freebim.db.domain || {};
at.freebim.db.domain.pingInterval = 15000;
at.freebim.db.websocketsettings = {
	endpoint   		: '/' + '${wsendpoint}',
	clientprefix   	: '/' + '${wsclientprefix}' + '/',
	serverprefix   	: '/' + '${wsserverprefix}' + '/'
};

at.freebim.db.delta = <%out.print(DateService.FREEBIM_DELTA);%>;
at.freebim.db.user = at.freebim.db.user || {};
<c:if test="${username != null && username.length() > 0}">at.freebim.db.user.username = "${username}";</c:if>
<c:if test="${contributorName != null && contributorName.length() > 0}">at.freebim.db.user.isShowAbstract = true;</c:if>
<c:if test="${contributorName == null || contributorName.length() == 0}">at.freebim.db.user.isShowAbstract = false;</c:if>
<sec:authorize access="hasRole('ROLE_USERMANAGER')">at.freebim.db.user.usermanager = true;</sec:authorize>
<c:if test="${userGuest}">at.freebim.db.user.isGuest = true;</c:if>
<c:if test="${userAdmin}">at.freebim.db.user.isAdmin = true;</c:if>
<c:if test="${userEdit}">at.freebim.db.user.isEdit = true;</c:if>
<c:if test="${contributorName != null && contributorName.length() > 0}">
at.freebim.db.contributor = {
	id : ${contributorId},
	name : "${contributorName}",
	mayDelete : ${contributorMayDelete},
	mayViewExtensions : ${contributorMayViewExtensions},
	maySetStatus : ${contributorMaySetStatus},
	maySetReleaseStatus : ${contributorMaySetReleaseStatus},
	mayManageLibraries : ${contributorMayManageLibraries}
};
</c:if>

</script>
</sec:authorize>
<script type="text/javascript"
	src="/resources${minified}/js/state.js?ver=${appVersion}"
	charset="UTF-8"></script>
<link type="text/css" rel="stylesheet"
	href="/resources/css/imprint.css?ver=${appVersion}">
<script type="text/javascript"
	src="/resources${minified}/js/imprint.js?ver=${appVersion}"
	charset="UTF-8"></script>
