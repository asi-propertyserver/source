<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:set var="version" value="0.0.1" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet"
	href="/resources/css/jquery/smoothness/jquery-ui-1.10.4.custom.min.css?ver=${version}">
<link type="text/css" rel="stylesheet"
	href="/resources/css/layout-default-latest.css?ver=${version}">
<link type="text/css" rel="stylesheet"
	href="/resources/css/jsTable.css?ver=${version}">
<link type="text/css" rel="stylesheet"
	href="/resources/css/freebim.css?ver=${version}">
<script type="text/javascript"
	src="/resources/js/jquery-1.11.0.min.js?ver=${version}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/jquery-ui-1.10.4.custom.min.js?ver=${version}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/jquery.layout-latest.min.js?ver=${version}"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/objectKeys.js?ver=${version}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/main.js?ver=${version}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/onLayout.js?ver=${version}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/table.js?ver=${version}" charset="UTF-8"></script>
<script type="text/javascript"
	src="/resources/js/form.js?ver=${version}" charset="UTF-8"></script>