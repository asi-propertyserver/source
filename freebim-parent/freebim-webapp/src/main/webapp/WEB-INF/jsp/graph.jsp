<%@page import="at.freebim.db.service.DateService"%>
<%@page import="at.freebim.db.domain.base.State"%>
<%@page import="at.freebim.db.domain.base.LifetimeBaseNode"%>
<%@page import="at.freebim.db.service.LifetimeBaseNodeService"%>
<%@page
	import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page import="at.freebim.db.domain.base.rel.RelationTypeEnum"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>freeBIM - Graph</title>
<%@ include file="head.jsp"%>
<script type="text/javascript"
	src="/resources/lib/d3.min.js?ver=${appVersion}" charset="UTF-8"></script>
</head>
<body>
	<div class="ui-layout-center" id="center">
		<div id="graph"></div>
	</div>
	<!-- center -->
	<div class="ui-layout-south">
		<div id="graph_options"></div>
	</div>
</body>
</html>
