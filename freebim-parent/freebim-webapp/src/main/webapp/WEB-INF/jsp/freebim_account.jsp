<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="freebim-login">
	<c:if test="${loginError}">
		<p style="color: red;">
			<b><span i18n="LOGIN_FAILED">Login fehlgeschlagen.</span></b>
		</p>
	</c:if>
	<c:if test="${authError}">
		<p style="color: red;">
			<b><span i18n="LOGIN_NO_AUTH">Fehlende Berechtigung.</span></b>
		</p>
	</c:if>
	<div id="login_error_box">
		<p style="color: red;">
			<b><span i18n="LOGIN_FAILED">Login fehlgeschlagen.</span></b>
		</p>
	</div>
	<c:if
		test="${username == null || username.length() == 0 || username.equals('anonymousUser')}">
		<img alt="freeBIM-Logo" src="/resources/freebim_logo.gif" />
		<div id="login-error">${error}</div>
		<form onSubmit="return at.freebim.db.request.login()" method="post"  id="form_login">
			<table>
				<tbody>
					<tr>
						<td><label for="j_username"><span i18n="DLG_USERNAME">Benutzername</span></label></td>
						<td><input id="j_username" name="j_username" type="text" /></td>
					</tr>
					<tr>
						<td><label for="j_password"><span i18n="DLG_PASSWORD">Passwort</span></label></td>
						<td><input id="j_password" name="j_password" type="password" /></td>
					</tr>
					<tr>
						<td colspan="2"><button type="submit" style="width: 100%;">
								<span i18n="DLG_LOGIN">Login</span>
							</button></td>
					</tr>
				</tbody>
			</table>
		</form>
		<p class="small">
			<span i18n="DLG_GUEST_ACCESS">Gastzugang</span>
		<table>
			<tbody>
				<tr>
					<td class="small"><label for="j_username"><span
							i18n="DLG_USERNAME">Benutzername</span></label></td>
					<td class="small">${guest_name}</td>
				</tr>
				<tr>
					<td class="small"><label for="j_password"><span
							i18n="DLG_PASSWORD">Passwort</span></label></td>
					<td class="small">${guest_password}</td>
				</tr>
			</tbody>
		</table>
		</p>
	</c:if>
	<div id="lang_select"></div>
	<c:if
		test="${username != null && username.length() > 0 && !username.equals('anonymousUser')}">
		<h1>
			<span i18n="DLG_LOGOUT">Logout</span>
		</h1>
		<form onSubmit="return at.freebim.db.request.logout()" method="post" id="form_logout">
			<p>
				<c:if
					test="${contributorName != null && contributorName.length() > 0}">
					<span i18n="LOGIN_CONTRIBUTOR">Bearbeiter</span> '${contributorName}', <span
						i18n="LOGGED_IN_AS">angemeldet als</span> '${username}' 
							</c:if>
				<c:if
					test="${contributorName == null || contributorName.length() == 0}">
								'${username}' 
							</c:if>
			</p>
			<input type="submit" name="logoutaction" i18n="BUTTON_LOGOUT"
				value="abmelden" />
		</form>
		<c:if
			test="${contributorName != null && contributorName.length() > 0}">
			<div id="contributor-options">
				<p>
					<input id="show-deleted" type="checkbox" name="show-deleted" /> <span
						i18n="SETTINGS_SHOW_DELETED">zeige gelöschte Objekte</span>
				</p>
				<p>
					<input id="show-unused" type="checkbox" name="show-unused" /> <span
						i18n="SETTINGS_SHOW_UNUSED">zeige nicht verknüpfte Objekte</span>
					<!-- <input type="button" value="aktualisieren" id="refresh-relevant"> -->
				</p>
				<p>
					<input id="show-abstract" type="checkbox" name="show-abstract"
						checked /> <span i18n="SETTINGS_SHOW_ABSTRACT">zeige
						abstrakte Objekte</span>
				</p>
				<p>
					<input id="show-library" type="checkbox" name="show-library" /> <span
						i18n="SETTINGS_SHOW_LIBRARY">zeige Bibliothek</span>
				</p>
				<p>
					<input id="show-nodeId" type="checkbox" name="show-id" /> <span
						i18n="SETTINGS_SHOW_ID">zeige ID</span>
				</p>
				<p>
					<input id="show-freebimId" type="checkbox" name="show-freebimId" />
					<span i18n="SETTINGS_SHOW_FREEBIMID">zeige freeBIM-ID</span>
				</p>
				<p>
					<input id="show-bsdd" type="checkbox" name="show-bsdd" /> <span
						i18n="SETTINGS_SHOW_BSDD_GUID">zeige bsDD-Guid</span>
				</p>
				<p>
					<input id="show-pset" type="checkbox" name="show-pset" /> <span
						i18n="SETTINGS_SHOW_PSET">zeige Parameter-Set</span>
				</p>
				<p>
					<input id="confirm-deletion" type="checkbox"
						name="confirm-deletion" checked /> <span
						i18n="SETTINGS_CONFIRM_DELETE">löschen erfordert
						Bestätigung</span>
				</p>
				<p>
					<input id="omit-server-prompt" type="checkbox"
						name="omit-server-prompt" /> <span
						i18n="SETTINGS_OMIT_SERVERPROMPT">Servermeldungen
						unterdrücken</span>
				</p>
				<p>
					<input type="radio" name="at.freebim.db.lang" value="de" /> <span
						i18n="SETTINGS_OWN_LANGUAGE">eigene Sprache</span> <input
						type="radio" name="at.freebim.db.lang" value="en" /> <span
						i18n="SETTINGS_INTL_ENGL">english</span>
				</p>
			</div>
			<div style="margin: 15px;">
				<p>
					<span i18n="LOGIN_CURRENT_LIBRARY">bearbeitete Bibliothek:</span>
				</p>
				<div id="contributor-responsibility">
					<span i18n="LOGIN_NO_CURRENT_LIBRARY">keine Bibliothek
						zugewiesen</span>
				</div>
			</div>
		</c:if>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<div>
				<input type="button" id="create_backup" value="create Backup">
				<input type="button" id="restore_backup" value="restore Backup">
			</div>
		</sec:authorize>
		<c:if
			test="${contributorName != null && contributorName.length() > 0 && contributorMaySetReleaseStatus}">
			<div id="bsdd">
				<h3>buildingSMART Data Dictionary</h3>
			</div>
		</c:if>
	</c:if>
	<p class="app-info">
		<span i18n="APP_VERSION">Version:</span> ${appVersion}, <span
			i18n2="APP_BUILD_TIMESTAMP" i18n_P1="${appBuildDate}"
			, i18n_P2="${appBuildTime}">erstellt am ${appBuildDate} um
			${appBuildTime}</span><br /> <a href="http://www.freebim.at/?info"
			i18n_href="APP_FREEBIM_LINK" target="_blank" title="freeBIM-Tirol">www.freebim.at</a>
	</p>
<c:if test="${username != null && username.length() > 0 && !username.equals('anonymousUser')}">
	<p class="info"><span i18n="INFO_BIB"></span></p>
	<p class="info"><span i18n="INFO_STATE"></span></p>
</c:if>
</div>
