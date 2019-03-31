<%@page import="at.freebim.db.service.DateService"%>
<%@page import="at.freebim.db.domain.base.State"%>
<%@page import="at.freebim.db.domain.base.LifetimeBaseNode"%>
<%@page import="at.freebim.db.service.LifetimeBaseNodeService"%>
<%@page import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page import="at.freebim.db.domain.base.rel.RelationTypeEnum"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<title>freeBIM - Parameterliste</title>
<%@ include file="head.jsp" %>
</head>
<body>

	<div id="parameterlist-div">
	</div>

	<div id="freebim-dlg2"></div>
	<div id="freebim-progress">
		<img alt="progress" src="/resources/ajax-progress.gif">
		<p id="freebim-progress-msg">in Bearbeitung ...</p>
	</div>
	
</body>
</html>