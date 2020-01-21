<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<title>freeBIM</title>
	<%@ include file="head.jsp"%>
</head>

<body>
	<div class="ui-layout-center" id="center">
		<div id="tabs">
			<ul>
				<li><a href="#freebim_account"><span i18n="TAB_ACCOUNT">Benutzerkonto</span></a></li>
				<sec:authorize access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR')">
					<li><a href="#freebim_tree"><span i18n="TAB_STRUCTURE">Struktur</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li><a href="#MessageNode"><span i18n="CLAZZ_MESSAGENODE">Messages</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USERMANAGER')">
					<li><a href="#FreebimUser"><span i18n="TAB_USER">Benutzer</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USERMANAGER') || hasRole('ROLE_CONTRIBUTOR')">
					<li><a href="#Contributor"><span i18n="TAB_CONTRIBUTOR">Bearbeiter</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USERMANAGER')">
					<li><a href="#Company"><span i18n="TAB_COMPANY">Unternehmen</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
					<li><a href="#freebim_data"><span i18n="TAB_DATA">Daten</span></a></li>
					<li><a href="#freebim_aux"><span i18n="TAB_AUX">Hilfstabellen</span></a></li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR')">
					<li><a href="#freebim_rel"><span i18n="TAB_RELATIONS">Beziehungen</span></a></li>
					<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
						<li><a href="#freebim_graph"><span i18n="TAB_GRAPH">Graph</span></a></li>
						<li><a href="#freebim_statistic"><span i18n="TAB_STAT">Statistik</span></a></li>
						<li><a href="#freebim_problems"><span i18n="TAB_PROBLEMS">Probleme</span></a></li>
					</sec:authorize>
					<li><a href="#freebim_search"><span i18n="TAB_SEARCH">Suche</span></a></li>
				</sec:authorize>
				<li><a href="#freebim_imprint"><span i18n="TAB_IMPRINT">Impressum</span></a></li>
			</ul>

			<div id="freebim_account">
				<%@ include file="freebim_account.jsp"%>
			</div>

			<sec:authorize access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR')">
				<div id="freebim_tree">
					<div class="ui-layout-center" id="root1"></div>
					<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
						<div class="ui-layout-east" id="root2"></div>
						<div class="ui-layout-west" id="root3"></div>
					</sec:authorize>
				</div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<div id="MessageNode"></div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_USERMANAGER')">
				<div id="FreebimUser"></div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_USERMANAGER') || hasRole('ROLE_CONTRIBUTOR')">
				<div id="Contributor"></div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_USERMANAGER')">
				<div id="Company"></div>
			</sec:authorize>

			<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
				<div id="freebim_data">
					<ul>
						<li><a href="#ParameterSet"><span i18n="TAB_PARAMETERSETS">Parameter-Sets</span></a>
						<li><a href="#Component"><span i18n="TAB_COMPONENTS">Komponenten</span></a>
						<li><a href="#Parameter"><span i18n="TAB_PROPERTIES">Parameter</span></a>
						<li><a href="#Measure"><span i18n="TAB_MEASURES">Bemessungen</span></a>
						<li><a href="#ValueList"><span i18n="TAB_VALUELISTS">Wertelisten</span></a>
						<li><a href="#ValueListEntry"><span i18n="TAB_VALUELISTENTRIES">Werte</span></a>
					</ul>
					<div id="ParameterSet"></div>
					<div id="Component"></div>
					<div id="Parameter"></div>
					<div id="Measure"></div>
					<div id="ValueList"></div>
					<div id="ValueListEntry"></div>
				</div>
				<div id="freebim_aux">
					<ul>
						<c:if test="${contributorMayManageLibraries}">
							<li><a href="#Library"><span i18n="TAB_LIBRARIES">Bibliotheken</span></a></li>
						</c:if>
						<li><a href="#Unit"><span i18n="TAB_UNITS">Einheiten</span></a></li>
						<li><a href="#Phase"><span i18n="TAB_PHASES">Projektphasen</span></a></li>
						<li><a href="#DataType"><span i18n="TAB_DATATYPES">Datentypen</span></a></li>
						<li><a href="#Discipline"><span i18n="TAB_DISCIPLINES">Disziplinen</span></a></li>
						<li><a href="#Document"><span i18n="TAB_DOCUMENTS">Dokumente</span></a></li>
					</ul>
					<c:if test="${contributorMayManageLibraries}">
						<div id="Library"></div>
					</c:if>
					<div id="Unit"></div>
					<div id="Phase"></div>
					<div id="DataType"></div>
					<div id="Discipline"></div>
					<div id="Document"></div>
				</div>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR')">
				<div id="freebim_rel"></div>
				<sec:authorize access="hasRole('ROLE_CONTRIBUTOR')">
					<div id="freebim_graph">
						<div id="graph_load_options">
							<div class="graph-load-option">
								<input type="button" value="zurücksetzen" i18n="BUTTON_RESET" class="graph-reset-btn">
							</div>
							<div class="graph-load-option">
								<input type="checkbox" name="RECURSIVE"><span i18n="RECURSIVE">rekursiv</span>
							</div>
							<div class="graph-load-option">
								<input type="checkbox" name="CLAZZ_PARAMETER"><span
									i18n="CLAZZ_PARAMETER">Parameter</span>
							</div>
							<div class="graph-load-option">
								<input type="checkbox" name="EQUALITY"><span i18n="EQUALITY">Gleichheit</span>
							</div>
							<div class="graph-view-option">
								<small>CMD-click, SHIFT-hover, ALT-hover,<br>CTRL-click,
									ALT-click
								</small>
							</div>
						</div>
						<div id="graph_view_options">
							<div class="graph-view-option">
								<input type="button" value="einfrieren" i18n="TOGGLE_GRAPH_FORCE"
									class="std-button graph-freeze-btn">
							</div>
							<div class="graph-view-option">
								<span i18n="TOGGLE_PARAMETER_VIEW">Parameter ein/aus</span><input type="checkbox"
									name="TOGGLE_PARAMETER_VIEW" checked>
							</div>
							<div class="graph-view-option">
								<span i18n="TOGGLE_EQUAL_VIEW">Gleichheit ein/aus</span><input type="checkbox"
									name="TOGGLE_EQUAL_VIEW" checked>
							</div>
							<div class="graph-view-option">
								<span i18n="TOGGLE_TEXT_VIEW">Text ein/aus</span><input type="checkbox"
									name="TOGGLE_TEXT_VIEW" checked>
							</div>
							<div class="graph-view-option">
								<span i18n="TOGGLE_LIBRARY_VIEW">Bibliotheken einfärben</span><input type="checkbox"
									name="TOGGLE_LIBRARY_VIEW" checked>
							</div>
						</div>
						<div id="graph"></div>
					</div>
					<div id="freebim_statistic">
						<div id="statistic-select"></div>
						<div id="statistic"></div>
					</div>
					<div id="freebim_problems">
						<div id="freebim_problems_acc">
							<h3 problem="1">
								<span i18n="H_COMP_NO_PARAMS">Komponenten ohne Parameter</span>
							</h3>
							<div id="freebim_problems_no_params"></div>
							<h3 problem="2">
								<span i18n="H_PARAMS_WITH_NO_MEASURE">Parameter ohne
									Bemessung</span>
							</h3>
							<div id="freebim_problems_no_measure"></div>
							<h3 problem="3">
								<span i18n="H_MEASURE_WITHOUT_VALUELIST">Bemessung ohne
									Werteliste und Datentyp</span>
							</h3>
							<div id="freebim_problems_empty_measure"></div>
							<h3 problem="4">
								<span i18n="H_PARAMS_WITH_DELETED_PHASE">Parameter mit
									gelöschter Projektphase</span>
							</h3>
							<div id="freebim_problems_deleted_phase"></div>
							<h3 problem="5">
								<span i18n="H_PARAMS_TO_MOVE_UP">Parameter zu Obergruppe
									verschieben</span>
							</h3>
							<div id="freebim_problems_params_move_up"></div>
							<h3 problem="6">
								<span i18n="H_MULTIPLE_PARAMS">Parameter
									Mehrfachzuordnung</span>
							</h3>
							<div id="freebim_problems_multiple_parameter_assignment"></div>
						</div>
					</div>
				</sec:authorize>
				<div id="freebim_search"></div>
			</sec:authorize>

			<div id="freebim_imprint">
				<%@ include file="imprint.jsp"%>
			</div>

		</div>
		<!-- tabs -->
		<sec:authorize
			access="hasRole('ROLE_GUEST') || hasRole('ROLE_CONTRIBUTOR') || hasRole('ROLE_USERMANAGER') || hasRole('ROLE_ADMIN')">
			<div id="freebim-dlg2"></div>
			<div id="freebim-progress">
				<img alt="progress" src="/resources/ajax-progress.gif">
				<p id="freebim-progress-msg">
					<span i18n="DLG_IN_PROGRESS">in Bearbeitung ...</span>
				</p>
			</div>
		</sec:authorize>
	</div>
	<!-- center -->
	<div class="ui-layout-south" id="status">
		<div id="status_info"></div>
	</div>
</body>

</html>