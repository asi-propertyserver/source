
// create namespace
var at = at || {};
at.freebim = at.freebim || {};


at.freebim.db = at.freebim.db || {
	nodes: {},
	marked: [],
	selected: [],
	updateTimer: null,
	logger: log4javascript.getDefaultLogger(),

	ms: function (x, arr, cl) {
		setTimeout(function () {
			if (x) {
				var nodeId = x.attr("nodeid"), idx;
				if (nodeId != undefined) {
					nodeId *= 1;
					idx = arr.indexOf(nodeId);
					if (idx >= 0) {
						arr.splice(idx, 1);
						jq(".freebim-item[nodeid='" + nodeId + "']").removeClass(cl);
					} else {
						arr.push(nodeId);
						jq(".freebim-item[nodeid='" + nodeId + "']").addClass(cl);
					}
					jq("#status").trigger("_update");
					jq(document).trigger("_at.freebim.db.marked.changed", [{ nodeId: nodeId }]);
				}
			}
		}, 200);
	},
	isIn: function (x, arr) {
		if (x) {
			if (isNaN(x)) {
				x = x.attr("nodeid");
			}
			var idx;
			if (x != undefined) {
				x *= 1;
				idx = arr.indexOf(x);
				return (idx >= 0);
			}
		}
		return false;
	},
	ums: function (cl) {
		setTimeout(function () {
			jq("." + cl).removeClass(cl);
			jq("#status").trigger("_update");
			jq(document).trigger("_at.freebim.db.marked.changed");
		}, 200);
	},
	mark: function (x) {
		at.freebim.db.ms(x, at.freebim.db.marked, "freebim-marked");
	},
	isMarked: function (x) {
		return at.freebim.db.isIn(x, at.freebim.db.marked);
	},
	unmarkAll: function () {
		at.freebim.db.marked = [];
		at.freebim.db.ums("freebim-marked");
	},
	select: function (x) {
		at.freebim.db.ms(x, at.freebim.db.selected, "freebim-selected");
	},
	isSelected: function (x) {
		return at.freebim.db.isIn(x, at.freebim.db.selected);
	},
	unselectAll: function () {
		at.freebim.db.selected = [];
		at.freebim.db.ums("freebim-selected");
	},
	fetchAux: function (clazz) {
		clazz.init();
		jq(document).trigger("_load", [{ cn: clazz.className }]);
	}

};

at.freebim.db.post = function (url, data, msg, success, fail, timeout, method) {
	at.freebim.db.request.request(url, data, msg, timeout, method).then((result) => {
		if(success) {
			success(result);
		}
	}).catch((error) => {
		if(fail) {
			fail(error);
		}
	})
};
at.freebim.db.get = function (url, data, msg, success, fail) {
	var key = "_" + Math.random();
	key = key.replace(".", "");
	jq(document).trigger("show_progress", [{ key: key, msg: msg }]);

	at.freebim.db.request.get(url, data).then((response) => {
		jq(document).trigger("hide_progress", [{ key: key }]);
		if (response.error != undefined) {
			alert(response.error);
			return;
		} else if (response.accessDenied) {
			at.freebim.db.request.logout();
			return;
		} else if (!response.result) {
			//			document.location.href = "/logout";
			return;
		}
		if (success) { success(response); }
	}).catch((error) => {
		jq(document).trigger("hide_progress", [{ key: key }]);
		if (fail) { fail(error); }
	});
};

at.freebim.db.user = at.freebim.db.user || {};
at.freebim.db.user.doShowAbstract = function () {
	if (at.freebim.db.user.isShowAbstract) {
		jq("tr.abstract").removeClass("hide-abstract");
	} else {
		jq("tr.abstract").addClass("hide-abstract");
	}
	jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
};

at.freebim.db.time = {
	formatISO: function (ts) {
		var date = new Date(ts * 1 + at.freebim.db.delta), month = date
			.getMonth() + 1, day = date.getDate(), h = date
				.getHours(), m = date.getMinutes(), s = date
					.getSeconds(), y = date.getFullYear();
		return y + "-"
			+ ((month < 10) ? "0" : "") + month + "-"
			+ ((day < 10) ? "0" : "") + day + " "
			+ ((h < 10) ? "0" : "") + h + ":"
			+ ((m < 10) ? "0" : "") + m + ":"
			+ ((s < 10) ? "0" : "") + s;
	},
	now: function () {
		var now = new Date(), ts = now.getTime()/* + now.getTimezoneOffset() * 60000*/;
		return ts;
	},
	validTime: function (tsF, tsT) {
		if (tsF != undefined) {
			var now = at.freebim.db.time.now() - at.freebim.db.delta;
			if (tsF < now) {
				if (tsT != undefined && tsT < now) {
					return false;
				}
				return true;
			}
			return false;
		}
		return true;
	},
	validNode: function (node) {
		var nf = at.freebim.db.domain.NodeFields,
			tsF = node[nf.VALID_FROM], tsT = node[nf.VALID_TO];
		if (!at.freebim.db.contributor) {
			if (node[nf.STATE] && node[nf.STATE] == at.freebim.db.domain.State.INV) { // State.INV = State.REJECTED
				return false;
			}
		}
		return at.freebim.db.time.validTime(tsF, tsT);
	}
};

at.freebim.db.progress = {
	msg: {},
	timer: null
};

jq(document).ready(function () {

	var db = at.freebim.db, d = db.domain, nf = d.NodeFields,
		i18n = net.spectroom.js.i18n;

	db.lang = "de";

	if (db.contributor) {

		db.layout = jq("body").layout({
			south__initClosed: true
			, south__size: 30
			, south__maxSize: 30
			, south__togglerTip_closed: i18n.getTitle("LAYOUT_SHOW_STATUSBAR") // "Statuszeile anzeigen"
			, south__togglerTip_open: i18n.getTitle("LAYOUT_HIDE_STATUSBAR") // "Statuszeile verbergen"
			, south__slidable: false
			, south__resizable: false
			, south__onopen: function () {
				jq("#status").trigger("_update");
			}
		});


	}

	jq("#freebim-MessageDlg").dialog({
		title: i18n.g("DLG_TITLE_FREEBIM_MESSAGE"),
		modal: true,
		autoOpen: false,
		show: "slideDown",
		hide: 'slideUp'
	}).prev().attr("i18n_dlg", "DLG_TITLE_FREEBIM_MESSAGE");

	if (net.spectroom.js.cookie.get("token") != "") {
		db.post("/messages/getCurrent", {}, null, function (response) {
			if (response && response.result) {
				jq.each(response.result, function (i, msg) {
					var dlg = net.spectroom.js.newDiv(),
						buttons = [];
					if (!db.user.isGuest) {
						buttons.push({
							text: i18n.getButton("DLG_BTN_READ"),
							click: function () {
								jq(dlg).dialog("close");
								setTimeout(function () {
									db.post("/messages/setClosed", { nodeId: msg[nf.NODEID] }, null, function (response) {
									}, function (error) {
										db.logger.error("Error in getting current messages: " + error);
									}, null, "POST");
								}, 100);
							}
						});
					}
					buttons.push({
						text: i18n.getButton("DLG_BTN_CLOSE"),
						click: function () {
							jq(dlg).dialog("close");
						}
					});
					jq(dlg).append("<div class=\"msg-content\"><div class=\"icon\"></div><p class=\"message\"></p></div>").addClass("freebim-MessageDlg");
					jq(dlg).dialog({
						title: i18n.g("DLG_TITLE_FREEBIM_MESSAGE"),
						modal: true,
						autoOpen: false,
						show: "slideDown",
						hide: 'slideUp',
						open: function () {
							setTimeout(function () {
								db.post("/messages/setSeen", { nodeId: msg[nf.NODEID] }, null, function (response) {
								}, function (error) {
									db.logger.error("Error in getting current messages: " + error);
								}, null, "POST");
							}, 100);
							jq(dlg).css("overflow", "hidden");
						},
						buttons: buttons
					}).prev().attr("i18n_dlg", "DLG_TITLE_FREEBIM_MESSAGE");
					var cl = msg[nf.TYPE].toLowerCase(), txt;
					txt = ((i18n.lang == "de-DE") ? msg[nf.NAME] : msg[nf.NAME_EN]);
					jq(dlg).find("div.icon").addClass(cl);
					jq(dlg).find("p.message").html(txt).addClass(cl);
					jq(dlg).dialog("open");
				});
			}
		}, function (error) {
			db.logger.error("Error in getting current messages: " + error);
		}, null, "GET");
	}
});

