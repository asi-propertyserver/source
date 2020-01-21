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
<style>
html, body, #statistic {
	width: 100%;
	height: 100%;
	overflow: hidden;
}

body {
	font: 10px sans-serif;
}

.axis path, .axis line {
	fill: none;
	stroke: #000;
	shape-rendering: crispEdges;
}

.x.axis path {
	display: none;
}

.line {
	fill: none;
	stroke: steelblue;
	stroke-width: 1.5px;
}
</style>
<script type="text/javascript"
	src="/resources/lib/d3.min.js?ver=${appVersion}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources${minified}/js/statistic.js?ver=${appVersion}"
	charset="UTF-8"></script>
<!-- <script src="http://d3js.org/d3.v3.js"></script>
 -->
</head>
<body>
	<div class="ui-layout-center" id="center">
		<div id="statistic"></div>
	</div>
	<!-- center -->
</body>
</html>




