<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="imprint-content">
	<h2>ASI-Propertyserver</h2>
	<div id="freebim_imprint_graph"></div>
	<div id="freebim_imprint_who">
		<h3 i18n="IMPRINT_WHO"></h3>
		<h4>ASI</h4>
		<a href="https://www.austrian-standards.at" target="_blank"><img
			src="/resources/img/logo_austrian_standards.png" alt="ASI"
			width="140px"></a>
		<h4>
			<span i18n="FREEBIM-TYROL">freeBIM-Tirol</span>
		</h4>
		<a href="http://www.freebim.at" target="_blank"><img
			src="/resources/freebim_logo.gif" alt="freeBIM" width="140px"></a>
	</div>
	<div id="freebim_imprint_contact">
		<h3 i18n="IMPRINT_CONTACT"></h3>
		<a href="http://www.freebim.at/?go_i3b%20-%20Uni%20Innsbruck"
			target="_blank"><img src="/resources/img/i3b-logo.jpg" alt="i3b"
			width="140px"></a>
	</div>
	<div id="freebim_imprint_dev">
		<h3 i18n="IMPRINT_DEV"></h3>
		<a href="http://www.freebim.at/?go_DBIS%20Uni%20Innsbruck"
			target="_blank"><img src="/resources/img/dbis-logo.gif"
			alt="dbis" width="140px"></a> Dipl.-Ing. Rainer Breuss <a
			href="mailto:rainer.breuss@uibk.ac.at?subject=ASI-Propertyserver">rainer.breuss@uibk.ac.at</a>
	</div>
	<div id="freebim_imprint_hosting">
		<h3 i18n="IMPRINT_HOSTING"></h3>
		<a href="http://www.freebim.at/?go_DBIS%20Uni%20Innsbruck"
			target="_blank"><img src="/resources/img/dbis-logo.gif"
			alt="dbis" width="140px"></a> Dipl.-Ing. Rainer Breuss <a
			href="mailto:rainer.breuss@uibk.ac.at?subject=ASI-Propertyserver">rainer.breuss@uibk.ac.at</a>
	</div>
	<div id="freebim_imprint_ws">
		<h3>WebService</h3>
		<table>
			<thead>
				<tr>
					<th colspan="2">WebService - Access</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><label>Username</label></td>
					<td>wstest</td>
				</tr>
				<tr>
					<td><label>Password</label></td>
					<td>wstest</td>
				</tr>
			</tbody>
		</table>
		<h5>Documentation</h5>
		<p>
			<a href="/apidocs/" target="_blank">/apidocs/</a>
		</p>
		<!-- 
		<p>
			<a href="/api-docs" target="_blank">/api-docs REST-API Documentation</a>
		</p> -->
		<p>
			<a id="webServiceWsdlLink"  target="_blank"></a>
		</p>
		<p>
			<a id="webServiceXsdLink" target="_blank"></a>
		</p>
		<script>
			let wsdlLink = document.getElementById("webServiceWsdlLink");
			let xsdLink = document.getElementById("webServiceXsdLink");
			
			let link = location.protocol + "//" + location.host + "/FreebimWebservice?";
			wsdlLink.setAttribute("href", link + "wsdl");
			wsdlLink.innerHTML = link + "wsdl";
			
			xsdLink.setAttribute("href", link + "xsd");
			xsdLink.innerHTML = link + "xsd";
		</script>
	</div>
	<div id="license">
		<h3>License</h3>
		<p>
			<a href="http://www.gnu.org/licenses/agpl-3.0.en.html"
				target="_blank"><img
				src="/resources/img/440px-AGPLv3_Logo.svg.png" alt="AGPLv3"
				width="110px" /><br>GNU Affero General Public License Version 3</a>
		</p>
	</div>
	<div id="sourcecode">
		<h3>Source-Code</h3>
		<p>
			<a href="https://github.com/asi-propertyserver" target="_blank"><img
				src="/resources/img/GitHub_Logo.png" alt="GitHub-logo" width="125px" /><img
				src="/resources/img/GitHub-Mark-120px-plus.png" alt="GitHub"
				style="width: 32px; margin: 10px" /><br>https://github.com/asi-propertyserver</a>
		</p>
	</div>
	<div id="freebim_imprint_libs">
		<h3 i18n="IMPRINT_LIBS"></h3>
		<h5>Client</h5>
		<ul>
			<li><a href="https://jquery.com/" target="_blank">jQuery</a></li>
			<li><a href="https://jqueryui.com/" target="_blank">jQuery
					UI</a></li>
			<li><a href="https://github.com/mar10/jquery-ui-contextmenu"
				target="_blank">jQuery UI contextmenu</a></li>
			<li><a href="http://plugins.jquery.com/layout/" target="_blank">jQuery
					UI layout</a></li>
			<li><a href="http://log4javascript.org/" target="_blank">log4javascript</a></li>
			<li><a href="http://www.muigg.com" target="_blank">IfcGuid</a></li>
			<li><a href="http://d3js.org/" target="_blank">d3</a></li>
			<li><a href="http://www.json.org/js.html" target="_blank">json2</a></li>
			<li>jsForm</li>
			<li>jsTable</li>
			<li>i18n</li>
		</ul>
		<h5>Server</h5>
		<ul>
			<li><a href="https://www.java.com" target="_blank">java</a></li>
			<li><a href="http://tomcat.apache.org/" target="_blank">Apache
					Tomcat</a></li>
			<li><a href="http://httpd.apache.org/" target="_blank">Apache
					httpd server</a></li>
			<li><a href="http://neo4j.com/" target="_blank">Neo4j</a></li>
			<li><a href="http://projects.spring.io/spring-data-neo4j/"
				target="_blank">Spring Data Neo4J</a></li>
			<li><a href="http://www.slf4j.org/" target="_blank">slf4j</a></li>
		</ul>
		and many others
	</div>
</div>
