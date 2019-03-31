/**
 * 
 */
at.freebim.db.admin = at.freebim.db.admin || {
	
	updateTimer : null, 
	
	updateInterval : 10000,
	
	updateUsers : function () {
		var i18n = net.spectroom.js.i18n;
		jq.post("/users", {}, function(response) {
			try {
				if (response && response.result) {
					var i, n = response.result.length, 
						table = document.createElement("table"), 
						thead = document.createElement("thead"),
						tbody = document.createElement("tbody"), tr, th, td,
						sfn = function (tr, td, u) {
							jq.post("/sessions", {username : u}, function (response) {
								if (response && response.result) {
									var ul = document.createElement("ul"), i, n = response.result.length;
									jq(td).append(ul);
									for (i=0; i<n; i++) {
										var s = response.result[i], li = document.createElement("li");
										jq(ul).append(li);
										jq(li).html(s);
										tsfn(tr, li, s);
									}
								}
								
							}).fail(function(error) {
//												alert(error);
							});
						},
						tsfn = function (tr, li, s) {
							jq.post("/lastRequest", {sessionId : s}, function (response) {
								if (response && response.result) {
									jq(li).html(s + " [" + at.freebim.db.time.formatISO(response.result) + "]");
								}								
							}).fail(function(error) {
//												alert(error);
							});
						};
					
					tr = document.createElement("tr");
					jq(thead).append(tr);
					th = document.createElement("th");
					jq(th).html(i18n.get("TAB_USER"));
					jq(tr).append(th);
					th = document.createElement("th");
					jq(th).html("Sessions");
					jq(tr).append(th);

					for (i=0; i<n; i++) {
						var u = response.result[i];
						tr = document.createElement("tr");
						jq(tbody).append(tr);
						td = document.createElement("td");
						jq(tr).append(td);
						jq(td).html(u);
						td = document.createElement("td");
						jq(tr).append(td);
						sfn(tr, td, u);
						
					}

					
					jq(table).append(thead);
					jq(table).append(tbody);
					jq("#loggedin-users").empty().append(table);
				}
			} catch (e) {
				at.freebim.db.logger.error("Error in response /users: " + e.message);
//				alert(error);
			}
		}).fail(function(error) {
//			alert(error);
		});
	}
};


jq(document).ready(function () {
	jq("#update-users").off("click").click(at.freebim.db.admin.updateUsers);
	at.freebim.db.admin.updateTimer = setInterval(at.freebim.db.admin.updateUsers, at.freebim.db.admin.updateInterval);
	at.freebim.db.admin.updateUsers();
});

/**
 * Edit 'References' relation (when logged in as 'admin' AND 'contributor')
 */
jq(document).delegate(".freebim-relation.type-" + at.freebim.db.domain.RelationTypeEnum.REFERENCES, "mouseover", function (event, data) {
	jq(this).css("background-color", "lightgray").css("border", "solid 1px black").css("cursor", "pointer");
});
jq(document).delegate(".freebim-relation.type-" + at.freebim.db.domain.RelationTypeEnum.REFERENCES, "mouseout", function (event, data) {
	jq(this).css("background-color", "").css("border", "solid 1px lightgray").css("cursor", "normal");
});
jq(document).delegate(".freebim-relation.type-" + at.freebim.db.domain.RelationTypeEnum.REFERENCES, "click", function (event, data) {
	var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
	relId = jq(this).attr("relid"), rel = d.rel.rels[relId], 
		a = rel[rf.FROM_NODE], b = rel[rf.TO_NODE], 
		node = d.get(a),
		l, lib = d.get(b),
		sel = document.createElement("select"), o, i, n = d.Library.used.length, 
		dlg = net.spectroom.js.newDiv(),
		save = function () {
			var i, n = node[nf.REFERENCES].length;
			for (i=0; i<n; i++) {
				if (node[nf.REFERENCES][i][rf.ID] == rel[rf.ID]) {
					node[nf.REFERENCES][i][rf.TO_NODE] = jq(sel).val() * 1;
					break;
				}
			}
			jq(document).trigger("_save", [{entity: node}]);
		};

	for (i=0; i<n; i++) {
		o = document.createElement("option");
		jq(sel).append(o);
		l = d.get(d.Library.used[i]);
		jq(o).attr("value", l[nf.NODEID]).html(l[nf.NAME]);
	}
	jq(sel).val(lib[nf.NODEID]);
	jq(dlg).append(sel).dialog({
		title : i18n.g("DLG_TITLE_EDIT_REFERENCES"),
		width: 400,
		height: 300,
		open : function () {},
		close : function () {},
		buttons : [{
			text : i18n.getButton("DLG_BTN_CANCEL"),
			click : function() {
				jq(dlg).dialog("close");
			}
		}, {
			text : i18n.getButton("DLG_BTN_SAVE"),
			click : function() {
				jq(dlg).dialog("close");
				save();
			}
		}]
	}).prev().attr("i18n_dlg", "DLG_TITLE_EDIT_REFERENCES");
});

jq(document).delegate("#create_backup", "click", function () {
	var self = this, db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
	jq(self).val("creating backup ...").prop("disabled", true);
	db.post("/admin/createBackup",
		null,
		"creating backup",
		function (response) {
			if (response.result) {
				alert("backup will be written to configured backup directory (server-side). It has not finished yet. See server logs for details.");
			} else {
				alert("create backup failed.");
			}
			jq(self).val("create backup ...").prop("disabled", false);
		},
		function (error) {
			alert("create backup failed.");
			jq(self).val("create backup ...").prop("disabled", false);
		}, (15 * 60 * 1000)
	);
});
jq(document).delegate("#restore_backup", "click", function () {
	var self = this, 
		db = at.freebim.db, d = db.domain, nf = d.NodeFields,
		i18n = net.spectroom.js.i18n, s = "restore backup",
		dlg = net.spectroom.js.newDiv(),
		sel = document.createElement("select"),
		restore = function (b) {
			db.post("/admin/restoreBackup", { backup : b }, s, 
				function (response) {
					alert("Please wait some minutes. Browser reload required afterwards. See server logs for details.");
				}, function (error) {
					alert("restore backup failed.");
				}, (15 * 60 * 1000)
			);
		},
		confirm = function () {
			var b = jq(sel).val();
			if (b) {
				jq(document).trigger("confirm", [{
					msg: "Do you really want to restore Backup '" + b + "'? All existing data will be overridden!",
					yes: function() {
						restore(b);
					}
				}]);
			}
		},
		showDlg = function () {
			jq(dlg).append(sel).dialog({
				title : "Select Backup",
				width: 400,
				height: 300,
				open : function () {},
				close : function () {},
				buttons : [{
					text : i18n.getButton("DLG_BTN_CANCEL"),
					click : function() {
						jq(dlg).dialog("close");
					}
				}, {
					text : i18n.getButton("DLG_BTN_OK"),
					click : function() {
						jq(dlg).dialog("close");
						confirm();
					}
				}]
			});
		};
	jq(sel).append("<option disabled value=''>select Backup to restore ...</option>");
	jq(self).val(s + " ...").prop("disabled", true);
	db.get("/admin/listBackups",
		null,
		s,
		function (response) {
			if (response.result) {
				jq(response.result).each(function (i, r) {
					jq(sel).append("<option value='" + r + "'>" + r + "</option>");
				});
				showDlg();
			} else {
				alert(s + " failed.");
			}
			jq(self).val(s + " ...").prop("disabled", false);
		},
		function (error) {
			alert(s + " failed.");
			jq(self).val(s + " ...").prop("disabled", false);
		}
	);
});
