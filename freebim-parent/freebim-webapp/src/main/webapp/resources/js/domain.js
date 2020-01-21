/**
 * 
 */

at.freebim.db.domain = {

	csv: function (s) {
		if (s === null || s === undefined) {
			return net.spectroom.js.table.csvDelim;
		}
		s = s.replace(/"/g, "");
		s = s.replace(/'/g, "");
		return "\"" + s + "\"" + net.spectroom.js.table.csvDelim;
	},

	init: function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		if (d.initialized) {
			return;
		}
		d.initialized = true;
		d.showDeleted = "hide-deleted";
		d.showUnused = "hide-unused";
		d.confirmDelete = true;

		/**
		 * define some standard table columns
		 */
		d.columns = {
			validFrom: {
				label: "SINCE",
				field: nf.VALID_FROM,
				type: "callback",
				data: function (v, td) {
					var date = db.time.formatISO(v);
					return date;
				},
				sort: true,
				csv: false
			},
			code: {
				label: "CODE", // "Code",
				field: nf.CODE,
				type: "text",
				sort: true
			},
			state: {
				label: "STATUS",
				field: nf.STATE,
				type: "callback",
				data: d.State.data,
				sort: true,
				csv: d.csv(jq(d.State.data).text())
			}
		};

		/**
		 * define some standard fields to be used in edit form:
		 */
		d.fields = {
			nodeId: {
				label: "",
				type: "hidden",
				field: nf.NODEID
			},
			validFrom: {
				label: "",
				type: "hidden",
				field: nf.VALID_FROM
			},
			validTo: {
				label: "",
				type: "hidden",
				field: nf.VALID_TO
			},
			name: {
				label: "NAME",
				type: "text",
				field: nf.NAME,
				className: "NAME"
			},
			nameEn: {
				label: "NAME_INTL",
				type: "text",
				field: nf.NAME_EN,
				className: "NAME-EN"
			},
			desc: {
				label: "DESCRIPTION",
				type: "textarea",
				field: nf.DESC,
				className: "DESC"
			},
			descEn: {
				label: "DESCRIPTION_INTL",
				type: "textarea",
				field: nf.DESC_EN,
				className: "DESC-EN"
			},
			code: {
				label: "CODE",
				type: "text",
				field: nf.CODE,
				className: "CODE"
			},
			state: {
				label: "STATUS",
				type: "select",
				field: nf.STATE,
				values: d.State.values,
				data: d.State.data,
				dataValue: d.State.dataValue,
				readOnly: ((db.contributor && (db.contributor.maySetStatus || db.contributor.maySetReleaseStatus)) ? false : true)
			},
			statusComment: {
				label: "STATUS_COMMENT",
				type: "textarea",
				field: nf.STATUS_COMMENT,
				readOnly: ((db.contributor && (db.contributor.maySetStatus || db.contributor.maySetReleaseStatus)) ? false : true)
			},
			bsDD: {
				label: "BSDD_GUID",
				type: "custom",
				field: nf.BSDD_GUID,
				createField: function (form, i, ip) {
					// ip depends on entity ...
				},
				setEntityValue: function (form, i, entity, ip) {
					var title = ((entity && entity[nf.BSDD_GUID] && db.bsdd.isValidGuid(entity[nf.BSDD_GUID])) ? "BSDD_SHOW_OBJECT"
						: ((entity && entity[nf.LOADED_BSDD]) ? "BSDD_SHOW_GUIDS" : "BSDD_CREATE_OBJECT"));
					jq(ip).empty();
					jq(ip).parent().append("<input type='text' class='jsForm-field' id='field_" + nf.BSDD_GUID + "' style='width: 300px;'"
						+ ((!(db.contributor && db.contributor.mayViewExtensions)) ? " readonly" : "")
						+ " value='" + ((entity && entity[nf.BSDD_GUID]) ? entity[nf.BSDD_GUID] : ((entity && entity[nf.LOADED_BSDD]) ? entity[nf.LOADED_BSDD].length + " " + i18n.g_n("LOADED_BSDD_GUIDS", entity[nf.LOADED_BSDD].length) : "")) + "'"
						+ ">"
						+ "<div class='bsdd-button'></div>");
					if (entity && entity[nf.BSDD_GUID] && db.bsdd.isValidGuid(entity[nf.BSDD_GUID])) {
						jq(ip).parent().find(".jsForm-field").attr("title", i18n.g("BSDD_CONVERT_GUID")).attr("i18n_title", "BSDD_CONVERT_GUID");
						jq(ip).parent().find(".bsdd-button");
					}
					jq(ip).parent().addClass("GUID").find(".bsdd-button").attr("nodeid", entity[nf.NODEID]).attr("cn", entity[nf.CLASS_NAME]).attr("title", i18n.g(title)).attr("i18n_title", title);
					jq(ip).remove();
				},
				getValue: function (x) {
					return x.val();
				},
				validate: function (entity, x) {
					if (db.bsdd.isValidGuid(x.val())) {
						x.removeClass("invalid");
					} else {
						x.addClass("invalid");
					}
				}
			},
			freebimID: {
				label: "FREEBIM_ID",
				type: "text",
				field: nf.FREEBIM_ID,
				readOnly: !(at.freebim.db.user.isAdmin),
				className: "freebim-freebimId"
			},
			id: {
				label: "ID",
				type: "text",
				field: nf.NODEID,
				readOnly: true,
				className: "freebim-nodeId"
			},
			docs: {
				label: "DOC",
				type: "custom",
				field: nf.DOCUMENTED,
				createField: function (form, i, ip) {
					d.relationFormField(form, i, ip, false, d.Document.title, d.RelationTypeEnum.DOCUMENTED_IN, nf.DOCUMENTED, "Document");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.DOCUMENTED, "Document", "OUT");
				}
			}
		};
		if (db.contributor && !db.contributor.maySetReleaseStatus) {
			var idx = jq.inArray(d.State.R, d.State.values); // R=RELEASED
			if (idx >= 0) {
				d.State.values.splice(idx, 1);
			}
		}

		if (!d.bbnId) {
			d.loadBbn(function (bbn) {
				d.bbn = bbn;
			});
		}
		if (db.contributor) {
			var val, x;
			// load user settings from cookies:
			val = net.spectroom.js.cookie.get("show-deleted");
			if (val != "") {
				d.showDeleted = ((val == "true") ? "show-deleted" : "hide-deleted");
				x = jq("#show-deleted")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
			}

			val = net.spectroom.js.cookie.get("show-unused");
			if (val != "") {
				d.showUnused = ((val == "true") ? "show-unused" : "hide-unused");
				x = jq("#show-unused")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
			} else {
				// no cookie yet, turn it on
				d.showUnused = "show-unused";
				x = jq("#show-unused")[0];
				if (x) { x.checked = true; }
				net.spectroom.js.cookie.set("show-unused", true, 14);
			}

			val = net.spectroom.js.cookie.get("show-abstract");
			if (val != "") {
				db.user.isShowAbstract = ((val == "true") ? true : false);
				x = jq("#show-abstract")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
			}
			val = net.spectroom.js.cookie.get("show-library");
			if (val != "") {
				db.user.isShowLibrary = ((val == "true") ? true : false);
				x = jq("#show-library")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
				d.showLibraryChanged();
			}
			val = net.spectroom.js.cookie.get("show-nodeId");
			if (val != "") {
				db.user.isShowNodeId = ((val == "true") ? true : false);
				x = jq("#show-nodeId")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
				d.showNodeIdChanged();
			}
			val = net.spectroom.js.cookie.get("show-freebimId");
			if (val != "") {
				db.user.isShowFreebimId = ((val == "true") ? true : false);
				x = jq("#show-freebimId")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
				d.showFreebimIdChanged();
			}
			val = net.spectroom.js.cookie.get("show-bsdd");
			if (val != "") {
				db.user.isShowBsdd = ((val == "true") ? true : false);
				x = jq("#show-bsdd")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
				d.showBsddChanged();
			}
			val = net.spectroom.js.cookie.get("show-pset");
			if (val != "") {
				db.user.isShowPset = ((val == "true") ? true : false);
				x = jq("#show-pset")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
				d.showPsetChanged();
			}
			val = net.spectroom.js.cookie.get("confirm-deletion");
			if (val != "") {
				db.user.confirmDelete = ((val == "true") ? true : false);
				x = jq("#confirm-deletion")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
			}
			val = net.spectroom.js.cookie.get("omit-server-prompt");
			if (val != "") {
				db.user.omitServerPrompt = ((val == "true") ? true : false);
				x = jq("#omit-server-prompt")[0];
				if (x) { x.checked = ((val == "true") ? true : false); }
			}
			val = net.spectroom.js.cookie.get("at.freebim.db.lang");
			if (val != "") {
				db.lang = ((val == "en") ? val : "de");
			}
			jq("input[type='radio'][name='at.freebim.db.lang'][value='" + db.lang + "']").attr("checked", true);
		}
		d.updateRelevance();
	},

	showLibraryInfo: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, cr = jq("#contributor-responsibility"), i18n = net.spectroom.js.i18n;
		cr.empty();
		if (db.contributor) {
			db.contributor[nf.RESPONSIBLE] = [];
			d.getOrLoad(db.contributor.id, d.Contributor.className, function (c) {
				d.Library.init();
				var i, n = ((c[nf.RESPONSIBLE]) ? c[nf.RESPONSIBLE].length : 0);
				if (n == 0) {
					cr.html(i18n.get("LOGIN_NO_CURRENT_LIBRARY"));
					return;
				}
				c[nf.RESPONSIBLE].sort(function (r1, r2) {
					var o1 = r1[rf.ORDERING],
						o2 = r2[rf.ORDERING];
					if (o1 != undefined && o2 != undefined) {
						return (o1 - o2);
					}
					return 0;
				});
				var ul = document.createElement("ul"), timer = null;
				cr.append(ul);
				ul = jq(ul);
				var respLib = net.spectroom.js.cookie.get("at.freebim.db.contributor.responsible"), found = false;
				db.contributor.currentLib = respLib;
				for (i = 0; i < n; i++) {
					var libId = c[nf.RESPONSIBLE][i][rf.TO_NODE], li = document.createElement("li"), addLib = function (libId, li) {
						ul.append(li);
						d.getOrLoad(libId, d.Library.className, function (lib) {
							if (!found && (respLib == lib[nf.NODEID])) {
								found = true;
							}
							jq(li).html("<input type='radio' name='resp-lib' value='" + lib[nf.NODEID] + "' " + (((respLib == lib[nf.NODEID])) ? "checked" : "") + "> <span class='freebim-item info-only Library'>" + lib[nf.NAME] + "</span>").change(function () {
								var respLib = jq("input[type='radio'][name='resp-lib']:checked").val();
								db.contributor.currentLib = respLib;
								net.spectroom.js.cookie.set("at.freebim.db.contributor.responsible", respLib, 365);
							});
						});
						if (timer) {
							clearTimeout(timer);
						}
						timer = setTimeout(function () {
							if (n == 1) {
								// check the one and only available Library
								jq("input[type='radio'][name='resp-lib'][value='" + libId + "']").get(0).checked = true;
								db.contributor.currentLib = libId;
								net.spectroom.js.cookie.set("at.freebim.db.contributor.responsible", libId, 365);
								found = true;
							}
							if (!found) {
								var respLib = jq("input[type='radio'][name='resp-lib']:checked").val();
								if (!respLib) {
									jq(document).trigger("alert", [{
										title: "DLG_TITLE_FREEBIM_INFO",
										content: i18n.get("LOGIN_PLEASE_CHOOSE_LIB"),
										okFn: function (val) { }
									}]);
								}
							}
						}, 1000);
					};
					db.contributor[nf.RESPONSIBLE].push(libId);
					addLib(libId, li);
				}

			});
		}
	},

	bbnId: null,

	modifiedNodes: [],

	addModifiedNode: function (nodeId) {
		nodeId *= 1;
		if (at.freebim.db.domain.modifiedNodes.indexOf(nodeId) < 0) {
			at.freebim.db.domain.modifiedNodes.push(nodeId);
		}
	},

	relevantId: function (cn, id) {
		if (cn) {
			var clazz = at.freebim.db.domain[cn];
			if (clazz && clazz.used) {
				return (clazz.used.indexOf(id) >= 0);
			}
		} else {
			return (id != undefined);
		}
	},

	showLibrary: function (el) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields;
		if (db.user.isShowLibrary) {
			jq(el).find("sub.freebim-library").remove();
			if (jq(el).closest("sub").length > 0) {
				return;
			}
			var nodeId = jq(el).attr("nodeid"), node = d.get(nodeId);
			if (node && node[nf.REFERENCES]) {
				var i, n = node[nf.REFERENCES].length, libs = {}, keys, id;
				for (i = 0; i < n; i++) {
					id = node[nf.REFERENCES][i][rf.TO_NODE];
					if (!libs[id]) {
						libs[id] = id;
					}
				}
				keys = Object.keys(libs);
				for (i = 0; i < keys.length; i++) {
					d.getOrLoad(keys[i], d.Library.className, function (lib) {
						var sub = document.createElement("sub");
						d.renderNode(lib, sub);
						jq(sub).addClass("freebim-library info-only")
							.removeClass("freebim-contextmenu")
							.appendTo(el);
					});
				}
			}
		}
	},

	showId: function (el) {
		if (at.freebim.db.user.isShowNodeId) {
			jq(el).find("sub.freebim-nodeId").remove();
			if (jq(el).closest("sub").length > 0) {
				return;
			}
			jq(el).html(jq(el).html() + "<sub class='freebim-nodeId'> " + net.spectroom.js.i18n.get("ID") + "=" + jq(el).attr("nodeid") + "</sub>");
		}
	},

	showFreebimId: function (el) {
		var db = at.freebim.db, nf = db.domain.NodeFields;
		if (db.user.isShowFreebimId) {
			var nodeId = jq(el).attr("nodeid"), node = db.domain.get(nodeId);
			if (node && node[nf.FREEBIM_ID]) {
				jq(el).find("sub.freebim-freebimId").remove();
				if (jq(el).closest("sub").length > 0) {
					return;
				}
				jq(el).html(jq(el).html() + "<sub class='freebim-freebimId'> " + net.spectroom.js.i18n.get("FREEBIM_ID") + "=" + node[nf.FREEBIM_ID] + "</sub>");
			}
		}
	},

	showBsdd: function (el) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
		if (db.user.isShowBsdd) {
			var nodeId = jq(el).attr("nodeid"), node = d.get(nodeId);
			if (node && node[nf.BSDD_GUID]) {
				jq(el).find("sub.freebim-bsdd").remove();
				if (jq(el).closest("sub").length > 0) {
					return;
				}
				jq(el).html(jq(el).html() + "<sub class='freebim-bsdd'> " + net.spectroom.js.i18n.get("BSDD_GUID") + ": " + node[nf.BSDD_GUID] + "</sub>");
			}
		}
	},

	showPset: function (el) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields;
		if (db.user.isShowPset) {
			var nodeId = jq(el).attr("nodeid"), node = d.get(nodeId), i, j, n, m;
			if (node && node[nf.PSETS] && node[nf.PSETS].length > 0) {
				// node is a Parameter
				jq(el).find("sub.freebim-pset").remove();
				if (jq(el).closest("sub").length > 0) {
					return;
				}
				var sub = document.createElement("sub");
				jq(sub).addClass("freebim-pset freebim-item info-only")
					.html(node[nf.PSETS].length + " " + ((node[nf.PSETS].length == 1) ? "PSet" : "PSets"))
					.appendTo(el);
				var compId = jq(el).closest(".Component.tree-node, .pl-div").attr("nodeid");
				if (!compId) {
					compId = jq("#field_i:visible").val();
				}
				if (compId) {
					var comp = d.get(compId);
					if (comp && comp[nf.PSETS] && comp[nf.PSETS].length > 0) {
						// comp is a Component
						// check if that Component has a PSet that contains that Parameter
						n = node[nf.PSETS].length;
						m = comp[nf.PSETS].length;
						for (i = 0; i < n; i++) {
							for (j = 0; j < m; j++) {
								if (node[nf.PSETS][i][rf.FROM_NODE] == comp[nf.PSETS][j][rf.TO_NODE]) {
									var psetId = node[nf.PSETS][i][rf.FROM_NODE];
									d.getOrLoad(psetId, "ParameterSet", function (e) {
										// show name of PSet
										jq(sub).html(jq(sub).html() + ", " + e[nf.NAME]);
									});
								}
							}
						}
					}
				}
				jq(sub).tooltip({
					open: function (event, ui) {
						// remove all other tooltips!
						jq(ui.tooltip).siblings(".ui-tooltip").remove();
					},
					content: function (callback) {
						var c = null, dir = null;
						if (node[nf.CLASS_NAME] == d.Component.className) {
							c = d.Component;
							dir = "OUT";
						} else if (node[nf.CLASS_NAME] == d.Parameter.className) {
							c = d.Parameter;
							dir = "IN";
						}
						if (c && dir) {
							c.fetchEntries("li", node, nf.PSETS, "ParameterSet", dir, function (entries) {
								var j, title = document.createElement("ul"), n = entries.length, x = jq(title);
								for (j = 0; j < n; j++) {
									x.append(entries[j]);
								}
								// call the passed tooltip function to set the content
								callback(x.html());
							});
						}
					}
				});
			}
		}
	},

	handleSavedNodes: function (savedNodes) {
		if (savedNodes) {
			var i, n = savedNodes.length, nodeInfo;
			for (i = 0; i < n; i++) {
				nodeInfo = savedNodes[i];
				at.freebim.db.domain.handleNodeInfo(nodeInfo);
			}
		}
	},

	handleNodeInfo: function (nodeInfo) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
		if (nodeInfo) {
			var cl, id = "" + nodeInfo.nodeId, node = d.get(id);
			switch (nodeInfo.action) {
				case "RELATION_MODIFIED":
				case "DELETED":
				case "SAVED":
					if (node) {
						d[node[nf.CLASS_NAME]].init();
						jq(document).trigger("getNode", [{ nodeId: id, cn: node[nf.CLASS_NAME] }]);
					}
					break;
				case "INSERTED":
					cl = nodeInfo[nf.CLASS_NAME];
					if (cl && cl.initialized && cl.arr && cl.table) {
						jq("." + nodeInfo.c + "[nodeid='" + id + "']").one("newNodeModified", function (event, data) {
							var entity = data.entity;
							jq("table.clazz_" + entity[nf.CLASS_NAME]).trigger("newInserted", [{ entity: entity, ts: db.time.now() }]);
						});
						jq(document).trigger("getNode", [{ nodeId: id, cn: nodeInfo[nf.CLASS_NAME] }]);
					}
					break;
			}
		}
	},

	deleteRelation: function (i, form, field, ul, ip, relId, nodeId) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields;
		d.addModifiedNode(nodeId);
		// remove dropped entry from the array of entries
		var j, entity = form.entity, arr = [];
		for (j = 0; j < entity[field].length; j++) {
			var he = entity[field][j];
			if (he[rf.ID] == relId) {
				// the trashed entry
				continue;
			}
			arr.push(he);
		}
		entity[field] = arr;
		jq(ul).empty();
		// update only that field, not the hole entity
		form.setEntityValue(i, entity, ip);
	},

	loadBbn: function (cb) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
		d.bbnId = 0; //why true?

		let refreshToken = localStorage.getItem("refresh-token");

		if (refreshToken) {
			at.freebim.db.request.get("/bbn", {}).then(function (response) {
				if (response.error != undefined) {
					alert(response.error);
				} else {
					if (response.result) {
						var bbn = response.result; // BigBangNode
						d.bbnId = bbn[nf.NODEID];
						d.bbn = bbn;
						d.BigBangNode.used = [bbn[nf.NODEID]];
						d.listen(bbn);
						if (cb) {
							cb(bbn);
						}
					}
				}
			}).catch(function (error) {
				if (error != null && error.status != 401) {
					var msg = ((error) ? ((error.status) ? error.status + " " : "") + ((error.statusText) ? error.statusText + " " : "") : net.spectroom.js.i18n.get("ERROR"));
					alert(msg);
				}
			});
		}

	},

	get: function (nodeId) {
		return at.freebim.db.nodes["" + nodeId];
	},

	getOrLoad: function (id, cn, f) {
		var node = at.freebim.db.domain.get(id);
		if (node) {
			if (f) {
				f(node);
			}
		} else {
			setTimeout(function () {
				jq(document).trigger("getNodeWithCallback", [{
					nodeId: id, cn: cn, callback: function (e) {
						if (f) {
							f(e);
						}
					}
				}]);
			}, 1);
		}
	},

	listen: function (e) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
		if (e && e[nf.NODEID] != undefined) {
			e.updated = db.time.now();
			if (!db.time.validNode(e)) {
				e.deleted = 1;
			}
			if (e[nf.CLASS_NAME] != d.BigBangNode.className
				&& e[nf.CLASS_NAME] != d.FreebimUser.className
				&& e[nf.CLASS_NAME] != d.Contributor.className
				&& !d.relevantId(e[nf.CLASS_NAME], e[nf.NODEID])) {
				e.unused = 1;
			}
			db.nodes["" + e[nf.NODEID]] = e;
			jq("#status").trigger("_update");
			d.cleanupNode(e);
		}
	},

	/**
	 * do some house keeping
	 */
	cleanupNode: function (e) {
/*		if (e) {
			var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, rels;
			if (e[nf.CLASS_NAME] === d.Component.className) {
				// check for duplicate HasParameter relations ...
				rels = e[nf.HAS_PARAMETER];
				if (rels) {
					var r, j, i, m, n = rels.length, params = [];
					for (i=0; i<n; i++) {
						r = rels[i];
						var found = false;
						m = params.length;
						for (j=0; j<m; j++) {
							if (params[j][rf.TO_NODE] === r[rf.TO_NODE]) {
								found = true;
								break;
							}
						}
						if (!found) {
							params.push(r);
						}
					}
					if (params.length != n) {
						// duplicate HasParameter relations !
						e[nf.HAS_PARAMETER] = params;
						jq(document).trigger("_save", [{ entity: e }]);
					}
				}
			}
		}
*/	},

	renderNodeId: function (nodeId, cn, el, parent, cb) {
		var d = at.freebim.db.domain;
		d.getOrLoad(nodeId, cn, function (e) {
			d.renderNode(e, el, parent);
			if (cb) {
				cb(e);
			}
		});
	},

	renderNode: function (node, el, parent) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
		if (node && el) {
			var name = "", max = jq(el).attr("maxLength"), abbr = jq(el).attr("abbr");
			jq(el).empty();
			if (node[nf.CLASS_NAME] == "BigBangNode") {
				name = "☀︎";
				//jq(el).addClass("no-edit");
			} else {
				d[node[nf.CLASS_NAME]].init();
				if ((el.nodeName == "DIV" || el.nodeName == "TD" || el.nodeName == "LI" || el.nodeName == "SUB") && node[nf.CLASS_NAME]) {
					if (abbr) {
						if (d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].getAbbrOf) {
							name = d[node[nf.CLASS_NAME]].getAbbrOf(node, el);
						}
					} else {
						if (d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].getNameOf) {
							name = d[node[nf.CLASS_NAME]].getNameOf(node, el);
						}
					}
				}
				if (name == undefined) {
					name = ((node[nf.NAME]) ? node[nf.NAME] : ((node[nf.CODE]) ? node[nf.CODE] : ""));
				}
			}
			if (max) {
				name = net.spectroom.js.shrinkText(name, max, el);
			}
			jq(el).append("<span>" + name + "</span>");
			d.setFreebimItemClasses(node, el, parent);
		}
	},
	setFreebimItemClasses: function (node, el, parent) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, c = db.contributor;
		if (node && el) {
			jq(el).addClass("freebim-item freebim-contextmenu " + node[nf.CLASS_NAME]).attr("nodeid", "" + node[nf.NODEID]).attr("c", "" + node[nf.CLASS_NAME]);
			if (node.deleted && node.deleted == 1) {
				jq(el).addClass(d.showDeleted);
				if (parent) {
					jq(parent).addClass(d.showDeleted);
				}
			}
			if (node.unused && node.unused == 1) {
				jq(el).addClass(d.showUnused);
				if (parent) {
					jq(parent).addClass(d.showUnused);
				}
			}
			if (node[nf.CLASS_NAME] != "BigBangNode" && node[nf.STATE]) {
				d.State.css(node.s, el);
			}
			if (node[nf.CLASS_NAME] == "Parameter" && node[nf.PSETS] && node[nf.PSETS].length > 0) {
				jq(el).addClass("pset-param");
			} else {
				jq(el).removeClass("pset-param");
			}
			if (db.isMarked(node[nf.NODEID])) {
				jq(el).addClass("freebim-marked");
			} else {
				jq(el).removeClass("freebim-marked");
			}
			if (db.isSelected(node[nf.NODEID])) {
				jq(el).addClass("freebim-selected");
			} else {
				jq(el).removeClass("freebim-selected");
			}
			if (node[nf.TYPE] && node[nf.TYPE] == 2) {
				jq(el).addClass("abstract");
			} else {
				jq(el).removeClass("abstract");
			}
			if (node[nf.CLASS_NAME] == "Component" && node[nf.IS_MATERIAL]) {
				jq(el).addClass("Material");
			} else {
				jq(el).removeClass("Material");
			}
			if (node[nf.BSDD_GUID] && node[nf.BSDD_GUID].length >= 0) {
				jq(el).addClass("bsdd");
			} else {
				jq(el).removeClass("bsdd");
				// show fetched BsddNode relations only if there is no bsdd-guid set
				if (c && node[nf.LOADED_BSDD] && node[nf.LOADED_BSDD].length > 0) {
					jq(el).addClass("bsdd-fetched");
				} else {
					jq(el).removeClass("bsdd-fetched");
				}
			}
		}
		d.showId(el);
		d.showFreebimId(el);
		d.showBsdd(el);
		d.showPset(el);
		d.showLibrary(el);

		var bsddCount = ((node[nf.BSDD_GUID] && node[nf.BSDD_GUID].length >= 0) ? undefined : ((c && node[nf.LOADED_BSDD] && node[nf.LOADED_BSDD].length) ? node[nf.LOADED_BSDD].length : undefined));
		if (bsddCount != undefined) {
			jq(el).html(jq(el).html() + "<span class='bsdd-fetched-count'><span> (⌘</span><sub>" + bsddCount + "</sub><span>) </span></span>");
		}
	},

	mayEdit: function (entity) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, c = db.contributor;
		if (entity) {
			if (entity[nf.CLASS_NAME] == "FreebimUser" || entity[nf.CLASS_NAME] == "Contributor") {
				return db.user.usermanager;
			} else {
				if (entity[nf.CLASS_NAME] == "Library"
					|| entity[nf.CLASS_NAME] == "BigBangNode") {
					return (c && c.mayManageLibraries);
				} else {
					if (d[entity[nf.CLASS_NAME]].aux) {
						// auxilary classes may be edited without Library restriction
						return true;
					}
					if (entity[nf.REFERENCES] && entity[nf.REFERENCES].length > 0) {
						if (c && c[nf.RESPONSIBLE]) {
							var i, j, n = entity[nf.REFERENCES].length, m = c[nf.RESPONSIBLE].length;
							for (i = 0; i < n; i++) {
								var libId = entity[nf.REFERENCES][i][rf.TO_NODE];
								for (j = 0; j < m; j++) {
									if (libId == c[nf.RESPONSIBLE][j]) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	},

	filters: {
		bsdd: function (clazz, filterdiv, filterFn, srcFn) {
			var d = at.freebim.db.domain, nf = d.NodeFields,
				div = document.createElement("div"), cbBsdd = document.createElement("input"),
				perform = function (row) {
					var checked = jq(cbBsdd).is(':checked'), node = d.get(clazz.arr[row]);
					if (!node || !checked) {
						return undefined;
					}
					return ((node === undefined) ? true : ((node[nf.BSDD_GUID]) ? false : true));
				};
			jq(cbBsdd).attr("type", "checkbox").change(function () {
				filterFn();
			});
			jq(div).append(cbBsdd).append("⌘ ").addClass("filter-bsdd").attr("title", net.spectroom.js.i18n.g("FILTER_BSDD_GUID")).attr("i18n_title", "FILTER_BSDD_GUID");
			jq(filterdiv).append(div);
			return perform;
		}, // bsdd

		bsddLoaded: function (clazz, filterdiv, filterFn, srcFn) {
			var d = at.freebim.db.domain, nf = d.NodeFields,
				div = document.createElement("div"), cbBsdd = document.createElement("input"),
				perform = function (row) {
					var checked = jq(cbBsdd).is(':checked'), node = d.get(clazz.arr[row]);
					if (!node || !checked) {
						return undefined;
					}
					return ((node === undefined) ? true : ((node[nf.LOADED_BSDD] && node[nf.LOADED_BSDD].length > 0) ? false : true));
				};
			jq(cbBsdd).attr("type", "checkbox").change(function () {
				filterFn();
			});
			jq(div).append(cbBsdd).append("(⌘<sub>n</sub>) ").addClass("filter-bsdd").attr("title", net.spectroom.js.i18n.g("FILTER_LOADED_BSDD_GUIDS")).attr("i18n_title", "FILTER_LOADED_BSDD_GUIDS");
			jq(filterdiv).append(div);
			return perform;
		}, // bsdd

		text: function (clazz, filterdiv, filterFn, srcFn) {
			var d = at.freebim.db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
				div = document.createElement("div"), ip = document.createElement("input"), cbex = document.createElement("input"),
				perform = function (row) {
					var node = d.get(clazz.arr[row]),
						filter = jq(ip).val();
					if (filter) {
						return srcFn(node, filter, jq(cbex).is(':checked'));
					} else {
						return undefined;
					}
				};
			ip.type = "text";
			jq(cbex).attr("type", "checkbox").addClass("filter-exact").change(function () {
				filterFn();
			});
			jq(ip).keyup(function (event) {
				filterFn();
			});
			jq(div).append(ip).append(cbex).append(i18n.get("FILTER_EXACTLY") + " ").addClass("filter-text").attr("title", i18n.g("FILTER_TEXT_TITLE")).attr("i18n_title", "FILTER_TEXT_TITLE");
			jq(filterdiv).append(div);
			return perform;
		}, // text

		mark: function (clazz, filterdiv, filterFn, srcFn) {
			var d = at.freebim.db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
				div = document.createElement("div"), cbmark = document.createElement("input"),
				perform = function (row) {
					var checked = jq(cbmark).is(':checked'), node = d.get(clazz.arr[row]);
					if (!node || !checked || at.freebim.db.marked.length == 0) {
						return undefined;
					}
					return srcFn(node, null, checked);
				};
			jq(cbmark).attr("type", "checkbox").change(function () {
				filterFn();
			});
			jq(div).append(cbmark).append(i18n.get("FILTER_MARKED") + " ").addClass("filter-marked").attr("title", i18n.g("FILTER_MARKED_TITLE")).attr("i18n_title", "FILTER_MARKED_TITLE");
			jq(filterdiv).append(div);
			//			jq(document).trigger("_at.freebim.db.marked.changed");
			return perform;
		}, // mark

		lib: function (clazz, filterdiv, filterFn, srcFn) {
			var d = at.freebim.db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
				div = document.createElement("div"), lib, i, n = d.Library.arr.length,
				ip = document.createElement("select"),
				o = document.createElement("option"),
				perform = function (row) {
					var node = d.get(clazz.arr[row]),
						libId = jq(ip).val();
					if (libId == "-") {
						return undefined;
					}
					return srcFn(node, libId, false);
				};
			jq(o).html(i18n.get("FILTER_LIBRARY_ALL")).attr("value", "-").selected = true;
			jq(ip).append(o).addClass("lib-filter");
			jq(div).append(" " + i18n.get("FILTER_LIBRARY") + ":").append(ip).addClass("filter-library").attr("title", i18n.g("FILTER_LIBRARY_TITLE")).attr("i18n_title", "FILTER_LIBRARY_TITLE");
			for (i = 0; i < n; i++) {
				lib = d.get(d.Library.arr[i]);
				if (lib && !lib.deleted) {
					o = document.createElement("option");
					jq(o).html(lib[nf.NAME]).attr("value", lib[nf.NODEID]);
					jq(ip).append(o);
				}
			}
			jq(ip).change(function () {
				filterFn();
			});
			jq(filterdiv).append(div);
			return perform;
		}, // lib

		filterMarkedRelated: function (arr, dir) {
			if (arr) {
				var r, i, n = arr.length, m = at.freebim.db.isMarked, rf = at.freebim.db.domain.RelationFields;
				for (i = 0; i < n; i++) {
					r = arr[i];
					switch (dir) {
						case "IN": if (m(r[rf.FROM_NODE])) { return false; } break;
						case "OUT": if (m(r[rf.TO_NODE])) { return false; } break;
						case "BOTH": if (m(r[rf.FROM_NODE]) || m(r[rf.TO_NODE])) { return false; } break;
					}
				}
			}
			return true;
		},
		filterByLib: function (node, libId, opt) {
			var rf = at.freebim.db.domain.RelationFields, nf = at.freebim.db.domain.NodeFields, inLib = false;
			if (node && libId && node[nf.REFERENCES]) {
				libId *= 1;
				var i, n = node[nf.REFERENCES].length;
				for (i = 0; i < n; i++) {
					if (node[nf.REFERENCES][i][rf.TO_NODE] * 1 == libId) {
						inLib = true;
						break;
					}
				}
			}
			return !inLib;
		},
		filterByName: function (node, filter, opt) {
			var nf = at.freebim.db.domain.NodeFields;
			if (opt) {
				return (!node || node[nf.NAME] != filter);
			}
			filter = ((filter.toLowerCase) ? filter.toLowerCase() : filter);
			var filtered = false, src = ((node) ? ((node[nf.NAME]) ? node[nf.NAME] : "") : "");
			if (src && src.toLowerCase) {
				src = src.toLowerCase();
				filtered = (src.indexOf(filter) < 0);
			}
			return filtered;
		},
		filterByCodeAndName: function (node, filter, opt) {
			var nf = at.freebim.db.domain.NodeFields;
			if (opt) {
				return (!node || (node[nf.NAME] != filter && node[nf.CODE] != filter));
			}
			filter = ((filter.toLowerCase) ? filter.toLowerCase() : filter);
			var filtered = false, src = ((node) ? ((node[nf.CODE]) ? node[nf.CODE] : "") + ((node[nf.NAME]) ? node[nf.NAME] : "") : "");
			if (src && src.toLowerCase) {
				src = src.toLowerCase();
				filtered = (src.indexOf(filter) < 0);
			}
			return filtered;
		}

	},

	getNodeTimer: {},

	loadNode: function (nodeId, cn, callback) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		db.logger.debug("at.freebim.db.domain.loadNode(" + nodeId + ")");
		d.getNodeTimer[nodeId] = null;
		var title = "RELATIONS",
			path = "relations";
		if (cn) {
			var clazz = d[cn];
			clazz.init();
			path = clazz.path;
			title = clazz.i18n;
		}
		if (path === undefined) {
			return;
		}

		db.post("/" + path + "/get",
			{ nodeId: nodeId, },
			i18n.gA("LOADING", title),
			function (response) {
				if (response) {
					if (response.result) {
						var entity = response.result;
						d.listen(entity);
						if (callback) {
							callback(entity);
						}
						jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{ entity: entity }]);
					} else {
						return;
					}
				}
				jq(document).trigger("filter", [{}]);
			},
			null, // fail
			null,
			"GET"
		);
	},

	delayedLoadNode: function (nodeId, cn) {
		// load a node by it's nodeId
		var d = at.freebim.db.domain, timer = d.getNodeTimer[nodeId];
		if (timer) {
			clearTimeout(timer);
		}
		d.getNodeTimer[nodeId] = setTimeout(function () {
			d.loadNode(nodeId, cn);
		}, 100);

	},

	showLibraryChanged: function () {
		var db = at.freebim.db;
		if (db.user.isShowLibrary) {
			jq(".freebim-item[nodeid]").each(function () {
				db.domain.showLibrary(jq(this));
			});
		} else {
			jq(".freebim-library").remove();
		}
		jq(document).trigger("freebimItemDisplayChanged");
	},
	showNodeIdChanged: function () {
		var db = at.freebim.db;
		if (db.user.isShowNodeId) {
			jq(".freebim-item[nodeid]").each(function () {
				db.domain.showId(jq(this));
			});
		} else {
			jq(".freebim-nodeId").remove();
		}
		jq(document).trigger("freebimItemDisplayChanged");
	},
	showFreebimIdChanged: function () {
		var db = at.freebim.db;
		if (db.user.isShowFreebimId) {
			jq(".freebim-item[nodeid]").each(function () {
				db.domain.showFreebimId(jq(this));
			});
		} else {
			jq(".freebim-freebimId").remove();
		}
		jq(document).trigger("freebimItemDisplayChanged");
	},
	showBsddChanged: function () {
		var db = at.freebim.db;
		if (db.user.isShowBsdd) {
			jq(".freebim-item[nodeid]").each(function () {
				db.domain.showBsdd(jq(this));
			});
		} else {
			jq(".freebim-bsdd").remove();
		}
		jq(document).trigger("freebimItemDisplayChanged");
	},
	showPsetChanged: function () {
		var db = at.freebim.db;
		if (db.user.isShowPset) {
			jq(".Parameter.freebim-item[nodeid]").each(function () {
				db.domain.showPset(jq(this));
			});
		} else {
			jq(".freebim-pset").remove();
		}
		jq(document).trigger("freebimItemDisplayChanged");
	},

	isSelectable: function (node) {
		var d = at.freebim.db.domain, nf = d.NodeFields;
		return (node
			&& node[nf.CLASS_NAME] == d.Parameter.className
			|| node[nf.CLASS_NAME] == d.Component.className
			|| node[nf.CLASS_NAME] == d.DataType.className
			|| node[nf.CLASS_NAME] == d.Document.className
			|| node[nf.CLASS_NAME] == d.Measure.className
			|| node[nf.CLASS_NAME] == d.Unit.className
			|| node[nf.CLASS_NAME] == d.ValueList.className
			|| node[nf.CLASS_NAME] == d.ValueListEntry.className);
	},

	updateRelevance: function () {
		var db = at.freebim.db, d = db.domain, cn, i18n = net.spectroom.js.i18n,
			dataTables = ["Component", "Parameter", "ParameterSet", "Measure", "ValueList", "ValueListEntry"],
			auxTables = ["Library", "Phase", "Unit", "DataType", "Discipline", "Document", "Company", "Contributor"],
			i, n,
			f = function (clazz, aux) {
				clazz.init();
				db.post("/" + clazz.path + "/ids", {}, i18n.gA("LOADING", clazz.i18n), function (response) {
					clazz.used = response.result;
					db.logger.info("[" + clazz.used.length + "] nodes relevant for " + clazz.className);
					jq(document).trigger("_used_loaded", [{ cn: clazz.className }]);
					if (aux) {
						db.fetchAux(clazz);
					}
				}, null, null, "GET");
			};
		if (db.user.username) {
			n = dataTables.length;
			for (i = 0; i < n; i++) {
				cn = dataTables[i];
				f(d[cn], false);
			}
			n = auxTables.length;
			for (i = 0; i < n; i++) {
				cn = auxTables[i];
				f(d[cn], true);
			}
		}
	}

};

at.freebim.db.domain.rel = at.freebim.db.domain.rel || {
	rels: {}
};

at.freebim.db.domain.rel.UnitConversion = {

	init: function () {
		var d = at.freebim.db.domain, self = d.rel.UnitConversion, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}

		jq(self).on("at.freebim.db.domain.rel.UnitConversion.edit", function (event, data) {
			// rel is a UnitConversion relation
			var rel = data.rel, okFn = data.okFn, btns = [], dlg = net.spectroom.js.newDiv();
			btns.push({
				text: i18n.getButton("DLG_BTN_CANCEL"),
				click: function () {
					jq(dlg).dialog("close");
				}
			});
			btns.push({
				text: i18n.getButton("DLG_BTN_OK"),
				click: function () {
					var q = jq(dlg).find(".unit-conversion").val();
					if (isNaN(q)) {
						jq(document).trigger("alert", [{
							title: "DLG_TITLE_FREEBIM_INFO",
							content: i18n.g1("NOT_A_REAL_NUMBER", q)
						}]);
						return;
					}
					rel[rf.QUALITY] = q * 1;
					if (okFn) {
						okFn(rel);
					}
					jq(dlg).dialog("close");
				}
			});
			jq(dlg).dialog({
				title: i18n.g("DLG_TITLE_UNIT_CONVERSION"),
				modal: true,
				open: function () {
					d.getOrLoad(rel[rf.FROM_NODE], d.Unit.className, function (srcUnit) {
						d.getOrLoad(rel[rf.TO_NODE], d.Unit.className, function (targetUnit) {
							var msg = i18n.g2("UNIT_CONVERSION", srcUnit[nf.NAME], targetUnit[nf.NAME]);
							jq(dlg).append(msg);
							jq(dlg).find(".unit-conversion").val(rel[rf.QUALITY]);
						});
					});
				},
				close: function () {
					jq(dlg).remove();
				},
				autoOpen: true,
				width: 400,
				height: 200,
				buttons: btns
			}).prev().attr("i18n_dlg", "DLG_TITLE_UNIT_CONVERSION");
		});
		jq(self).on("unitConversionRel", function (event, data) {
			d.getOrLoad(data.rel[rf.FROM_NODE], d.Unit.className, function (srcUnit) {
				d.getOrLoad(data.rel[rf.TO_NODE], d.Unit.className, function (targetUnit) {
					data.okFn("1 " + srcUnit[nf.NAME] + " ≙ " + data.rel[rf.QUALITY] + " " + targetUnit[nf.NAME]);
				});
			});
		});
		self.initialized = true;
	}
};
at.freebim.db.domain.rel.Equals = {

	eqSign: function (rel) {
		var rf = at.freebim.db.domain.RelationFields;
		return ((rel[rf.QUALITY] >= 1) ? " = " : ((rel[rf.QUALITY] <= 0) ? " ≠ " : ((rel[rf.QUALITY] < 0.5) ? " ≉ " : " ≈ ")));
	},

	getOtherNode: function (node, r, callback) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields;
		// r is an Equals relation
		var otherId, otherClass;
		if (r[rf.FROM_NODE] == node[nf.NODEID]) {
			// outgoing relation (node)-[:EQUALS]->(other)
			otherId = r[rf.TO_NODE];
			otherClass = r[rf.TO_CLASS];
		} else {
			// incoming relation (node)<-[:EQUALS]-(other)
			otherId = r[rf.FROM_NODE];
			otherClass = r[rf.FROM_CLASS];
		}
		otherClass = ((otherClass) ? otherClass : node[nf.CLASS_NAME]);
		d.getOrLoad(otherId, otherClass, function (eq) {
			callback(eq);
		});
	},

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, self = d.rel.Equals, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}

		jq(self).on("editEquals", function (event, data) {
			// rel is a Equals relation
			var rel = data.rel, okFn = data.okFn, btns = [], dlg = net.spectroom.js.newDiv();
			btns.push({
				text: i18n.getButton("DLG_BTN_CANCEL"),
				click: function () {
					jq(dlg).dialog("close");
				}
			});
			btns.push({
				text: i18n.getButton("DLG_BTN_OK"),
				click: function () {
					var q = jq(dlg).find(".eqSelect").val();
					rel[rf.QUALITY] = q / 100.;
					if (okFn) {
						okFn();
					}
					jq(dlg).dialog("close");
				}
			});
			jq(dlg).dialog({
				title: i18n.g("DLG_TITLE_SPECIFY_EQ"),
				modal: true,
				autoOpen: true,
				width: 400,
				height: 200,
				close: function (event, ui) {
					jq(dlg).remove();
				},
				open: function () {
					var opt, i, sel = document.createElement("select");
					if (!rel[rf.QUALITY]) {
						rel[rf.QUALITY] = 1;
					}
					for (i = 0; i <= 100; i += 10) {
						opt = document.createElement("option");
						opt.value = i;
						sel.appendChild(opt);
						opt.appendChild(document.createTextNode(i + "%"));
					}

					jq(dlg).append(i18n.get("SPECIFY_EQ1"))
						.append(" ")
						.append(sel)
						.append(" ")
						.append(i18n.get("SPECIFY_EQ2"));

					jq(sel).addClass("eqSelect").val(rel[rf.QUALITY] * 100).change(function () {
						rel[rf.QUALITY] = jq(this).val() / 100;
					});
				},
				buttons: btns
			}).prev().attr("i18n_dlg", "DLG_TITLE_SPECIFY_EQ");
		});
		jq(self).on("equalsRel", function (event, data) {
			var rel = data.rel, okFn = data.okFn;
			okFn(Math.floor(rel[rf.QUALITY] * 100.) + "%");
		});
		self.initialized = true;
	}
};

/**
 * add a handler to update text input field when Component selection changes
 */
jq(document).delegate("input[type='radio'][name='at.freebim.db.domain.selectComponent'][nodeid]", "change", function () {
	var db = at.freebim.db, d = db.domain, i18n = net.spectroom.js.i18n;
	var nodeId = jq(this).parent().find("input[type='radio'][name='at.freebim.db.domain.selectComponent']:checked").attr("nodeid"),
		c = ((nodeId == "null") ? null : d.get(nodeId)), name;
	if (c) {
		name = d.Component.getNameOf(c);
	} else {
		name = i18n.g("DONT_SELECT");
	}
	jq(this).closest(".ui-dialog-content").find("input[type='text']").val(name);
});
at.freebim.db.domain.selectComponent = function (c, finFn) {

	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, js = net.spectroom.js, i18n = js.i18n;

	var btns = [], dlg = js.newDiv(),
		label = document.createElement("label"),
		ip = document.createElement("input"),
		ul = document.createElement("ul"),
		comp = c,
		find = function () {
			var txt = jq(ip).val();
			if (txt.length > 2) {
				// find components by its name
				db.post("/search/find", { searchstring: txt, clazz: "at.freebim.db.domain.Component" }, "", function (response) {
					// response.result is an array of MatchResult objects
					// containing quality q and Component node 
					if (response.result) {
						jq(ul).empty();
						// sort by quality of match
						response.result.sort(function (a, b) {
							return (b.q - a.q);
						});
						jq.each(response.result, function (i, mr) {
							var div = document.createElement("div");
							d.listen(mr.node);
							d.renderNode(mr.node, div);
							jq(ul).append("<li><input type='radio' name='at.freebim.db.domain.selectComponent' nodeid='" + mr.node[nf.NODEID] + "'> ")
								.append(div);
							jq(div).addClass("info-only");
						});
						// add a 'for all' entry
						jq(ul).prepend("<li><input type='radio' name='at.freebim.db.domain.selectComponent' nodeid='null'> " + i18n.g("DONT_SELECT") + "</li>");
						if (comp) {
							jq(ul).find("input[type='radio'][nodeid='" + comp[nf.NODEID] + "']").attr("checked", "true");
						}
					}
				}, null, null, "GET");
			}
		};
	jq(ul).addClass("vlcomp");
	jq(ip).attr("type", "text").css("width", "100%");
	jq(label).html(i18n.g("CLAZZ_COMPONENT"));

	jq(ip).keyup(function () {
		find();
	});

	jq(dlg).append(label)
		.append(ip)
		.append(ul);

	btns.push({
		text: i18n.getButton("DLG_BTN_CANCEL"),
		click: function () {
			jq(dlg).dialog("close");
		}
	});
	btns.push({
		text: i18n.getButton("DLG_BTN_OK"),
		click: function () {
			var nodeId = jq(ul).find("input[type='radio'][name='at.freebim.db.domain.selectComponent']:checked").attr("nodeid"),
				c = ((nodeId == "null") ? null : d.get(nodeId));
			jq(dlg).dialog("close");
			finFn(c);
		}
	});
	jq(dlg).dialog({
		title: i18n.g("DLG_TITLE_SPECIFY_COMPONENT"),
		modal: true,
		autoOpen: true,
		width: 600,
		height: 400,
		close: function (event, ui) {
			jq(dlg).remove();
		},
		open: function () {
			jq(ip).val((c) ? c[nf.NAME] : "");
			find();
		},
		buttons: btns
	}).prev().attr("i18n_dlg", "DLG_TITLE_SPECIFY_COMPONENT");
};

at.freebim.db.domain.rel.HasValue = {

	init: function () {
		var db = at.freebim.db, d = db.domain, self = d.rel.HasValue, nf = d.NodeFields, rf = d.RelationFields, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}

		jq(self).on("editComponent", function (event, data) {

			var rel = data.rel, finFn = function (c) {
				if (c) {
					// store uuid of Component in HasValue relation
					rel[rf.COMPONENT] = c[nf.FREEBIM_ID];
				} else {
					rel[rf.COMPONENT] = "";
				}
				if (data.okFn) {
					data.okFn();
				}
			};
			jq(d.rel.HasValue).trigger("componentFromRel", [{
				rel: rel, okFn: function (c) {
					at.freebim.db.domain.selectComponent(c, finFn);
				}
			}]);

		});

		/**
		 * @param data.rel The HasValue relation.
		 * @param data.okFn The ok callback function, to be called with the Component as single parameter: data.okFn(comp). 
		 */
		jq(self).on("componentFromRel", function (event, data) {
			if (data.rel) {
				var uuid = data.rel[rf.COMPONENT];
				if (uuid) {
					db.post("/component/getbyuuid", { u: uuid }, i18n.gA("LOADING", d.Component.title), function (response) {
						if (response.result) {
							data.okFn(response.result);
						}
					}, null, null, "GET");
				}
			}
		});

		self.initialized = true;
	}
};

at.freebim.db.domain.rel.HasParameter = {

	selectedPhase: null,

	init: function () {
		var db = at.freebim.db, d = db.domain, self = d.rel.HasParameter, nf = d.NodeFields, rf = d.RelationFields, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}

		self.initPhases = function (e, data) {
			d.Phase.init();
			if (!d.Phase.arr || d.Phase.arr.length == 0) {
				jq(document).on("Phase_loaded", function (event, data2) {
					jq(self).trigger(e, data);
				});
				jq(document).trigger("Phase_load");
				return true;
			}
			return false;
		};

		jq(self).on("editPhase", function (event, data) {

			if (self.initPhases("editPhase", data)) {
				return;
			}

			// rel is a HasParameter relation
			var rel = data.rel, okFn = data.okFn, btns = [], dlg = net.spectroom.js.newDiv(), ph, phId, sel = document.createElement("select"), opt, i, n = d.Phase.arr.length;
			for (i = 0; i < n; i++) {
				phId = d.Phase.arr[i];
				ph = d.get(phId);
				if (db.time.validNode(ph)) {
					opt = document.createElement("option");
					opt.appendChild(document.createTextNode(ph[nf.CODE] + " " + ph[nf.NAME]));
					if (ph[nf.FREEBIM_ID] == rel[rf.PHASE]) {
						opt.selected = true;
					}
					sel.appendChild(opt);
					jq(opt).val(phId);
				}
			};
			dlg.appendChild(sel);
			if (rel[rf.PHASE]) {
				btns.push({
					text: i18n.getButton("DLG_BTN_CANCEL"),
					click: function () {
						jq(dlg).dialog("close");
					}
				});
			}
			btns.push({
				text: i18n.getButton("DLG_BTN_OK"),
				click: function () {
					var phId = jq(jq(dlg).children()[0]).val(), ph = d.get(phId);
					rel[rf.PHASE] = ph[nf.FREEBIM_ID];
					if (okFn) {
						okFn(ph);
					}
					jq(dlg).dialog("close");
				}
			});
			jq(dlg).dialog({
				title: i18n.g("DLG_TITLE_SPECIFY_PHASE"),
				modal: true,
				autoOpen: true,
				width: 400,
				height: 200,
				close: function (event, ui) {
					jq(dlg).remove();
				},
				buttons: btns
			}).prev().attr("i18n_dlg", "DLG_TITLE_SPECIFY_PHASE");
		});

		jq(self).on("phaseFromRel", function (event, data) {
			if (self.initPhases("phaseFromRel", data)) {
				return;
			}
			var ph = self.phaseForUuid(data.rel[rf.PHASE]);
			if (ph && data.okFn) {
				data.okFn(ph);
			}
		});

		self.phaseForUuid = function (u) {
			if (d.Phase && d.Phase.arr) {
				var ph, phId, i, n = d.Phase.arr.length;
				for (i = 0; i < n; i++) {
					phId = d.Phase.arr[i];
					ph = d.get(phId);
					if (ph[nf.FREEBIM_ID] == u) {
						return ph;
					}
				}
			}
			return null;
		};

		self.initialized = true;
	}
};

at.freebim.db.domain.relationFormField = function (form, i, ip, orderable, relTableTitle, relType, field, relClass, dir, relAdded) {
	var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields;
	d[relClass].init();
	if (!dir) {
		dir = "OUT";
	}
	// add an ul that could be made sortable afterwards:
	var ul = document.createElement("ul"),
		trash = document.createElement("div"),
		addBtn = document.createElement("div"),
		enableSorting = function (enable) {
			if (enable) {
				if (orderable) {
					jq(ul).sortable({
						// entries have been resorted, update ordering ...
						stop: function (event, ui) {
							var c, node, nodeId, k, l, n = ul.childNodes.length, entity = form.entity;
							for (k = 0; k < n; k++) {
								c = ul.childNodes[k];
								nodeId = jq(c).attr("nodeid");
								if (nodeId) {
									node = d.get(nodeId);
									if (node) {
										for (l = 0; l < n; l++) {
											if (dir == "OUT" && entity[field][l][rf.TO_NODE] == node[nf.NODEID]) {
												entity[field][l][rf.ORDERING] = k;
												break;
											} else if (dir == "IN" && entity[field][l][rf.FROM_NODE] == node[nf.NODEID]) {
												entity[field][l][rf.ORDERING] = k;
												break;
											}
										}
									}
								}
							}
							d.addModifiedNode(entity[nf.NODEID]);
						}
					});
				}
			} else {
				if (orderable) {
					jq(ul).sortable("destroy");
				}
			}
		},
		enableTrash = function (enable) {
			if (enable) {
				if (!jq(trash).hasClass("ui-droppable")) {
					jq(trash).droppable({
						accept: "." + relClass,
						hoverClass: "drop-hover",
						tolerance: "touch",
						drop: function (event, ui) {
							var x = jq(ui.draggable), relId = x.attr("relid"), nodeId = x.attr("nodeid");
							d.deleteRelation(i, form, field, ul, ip, relId, nodeId);
						}
					}).css("background-image", "url(\"/resources/trash_empty_open2.png\")");
				}
				jq(ul).find("li." + relClass).draggable({
					stop: function (event, ui) {
						var entity = form.entity;
						form.setEntityValue(i, entity, ip);
					}
				}).off("dblclick").dblclick(function () {
					var x = jq(this), relId = x.attr("relid"), nodeId = x.attr("nodeid");
					d.deleteRelation(i, form, field, ul, ip, relId, nodeId);
					return false;
				});
			} else {
				if (jq(trash).hasClass("ui-droppable")) {
					jq(trash).droppable("destroy").css("background-image", "url(\"/resources/trash_empty_closed2.png\")");
					jq(ul).find("li." + relClass).draggable("destroy").off("dblclick");
				}
			}
		};
	ip.appendChild(ul);
	jq(ip).css("border", "solid 1px lightgray");
	if (!form.fields[i].readOnly) {
		ip.appendChild(trash);
		ip.appendChild(addBtn);
		addBtn.appendChild(document.createTextNode("✚︎"));

		var assign = function (nodeId) {
			var entity = form.entity;
			if (entity && nodeId != undefined) {
				d.addModifiedNode(nodeId);
				var he = {};
				he[rf.ID] = "_" + net.spectroom.js.rndId(); // a temporary ID
				he[rf.TYPE] = relType;
				if (dir == "IN") {
					he[rf.TO_NODE] = entity[nf.NODEID];
					he[rf.FROM_NODE] = nodeId;
				} else {
					he[rf.FROM_NODE] = entity[nf.NODEID];
					he[rf.TO_NODE] = nodeId;
				}
				entity[field] = entity[field] || [];
				he[rf.ORDERING] = entity[field].length;
				if ("Document" == relClass) {
					he[rf.TIMESTAMP] = db.time.now() - db.delta;
				}
				entity[field].push(he);
				if (relAdded) {
					relAdded(he, function () {
						form.setEntityValue(i, entity, ip);
					});
				}
			}
		};

		jq(addBtn).addClass("freebim-button").off("click").click(function () {
			// open a new dialog to drag related items from, or to create a new one:
			var dlg = net.spectroom.js.newDiv();
			jq(dlg).addClass("floating-table").dialog({
				title: relTableTitle,
				modal: true,
				autoOpen: true,
				width: 800,
				height: 300,
				beforeClose: function (event, ui) {
					jq(ip).parents(".ui-dialog").droppable("destroy"); // the form is droppable

					jq(dlg).find(".draggable").off("click").draggable("destroy");
					jq(dlg).find(".content").appendTo(jq("#" + relClass));
				},
				close: function (event, ui) {
					jq(dlg).hide();
					enableSorting(true);
					jq(trash).show();
					jq(addBtn).show();
					var w = jq(ip).closest(".ui-dialog");
					jq(w).position({
						my: "center center",
						at: "center center",
						of: window,
						collision: "none"
					});
					setTimeout(function () {
						jq(dlg).remove();
					}, 500);

				},
				open: function () {
					var u = jq(dlg).parent();
					jq(u).position({
						my: "left bottom ",
						at: "left+7 bottom-7 ",
						of: window,
						collision: "none"
					});
					var w = jq(ip).closest(".ui-dialog");
					jq(w).position({
						my: "right top",
						at: "right-7 top+7",
						of: window,
						collision: "none"
					});
					jq(trash).hide();
					jq(addBtn).hide();
					enableSorting(false);
					jq(ip).parents(".ui-dialog").droppable({ // the form is droppable
						accept: "." + relClass,
						hoverClass: "drop-hover",
						tolerance: "touch",
						drop: function (event, ui) {
							// i.e. ValueListEntry to add ...
							var nodeId, e, entity = form.entity;
							if (ui && ui.draggable) {
								nodeId = jq(ui.draggable).attr("nodeid");
								if (nodeId != undefined) {
									assign(nodeId);
									form.setEntityValue(i, entity, ip);
									e = jq("#" + form.parent.id).find("*[nodeid='" + nodeId + "']")[0];
									if (e) {
										e.scrollIntoView(true);
									}
								}
							}
						}
					});
					jq(dlg).css("overflow", "hidden");
				}
			});
			if (!d[relClass].table.id) {
				jq(d[relClass].table).one(d[relClass].className + "_tableCreated", function (event, data) {
					jq(dlg).find("td.freebim-item." + relClass).draggable({
						helper: "clone",
						appendTo: jq(addBtn).parent(),
						zIndex: 1500
					}).off("click").off("dblclick").dblclick(function () {
						var e, nodeId = jq(this).attr("nodeid");
						if (nodeId != undefined) {
							assign(nodeId);
							form.setEntityValue(i, form.entity, ip);
							e = jq("#" + form.parent.id).find("*[nodeid='" + nodeId + "']")[0];
							if (e) {
								e.scrollIntoView(true);
							}
						}
					});
				});
				jq(document).trigger("_showTable", [{ parent: dlg.id, cn: relClass }]);
			} else {
				//				d[relClass].table.prevParent = jq("#" + d[relClass].table.id + " .content");
				//				d[relClass].table.prevDisp = jq("#" + d[relClass].table.id).css("display");
				jq("#" + d[relClass].table.id).appendTo(dlg);

				jq(dlg).find("td.freebim-item." + relClass).draggable({
					helper: "clone",
					appendTo: jq(addBtn).parent(),
					zIndex: 1500
				}).off("click").off("dblclick").dblclick(function () {
					var e, nodeId = jq(this).attr("nodeid");
					if (nodeId != undefined) {
						assign(nodeId);
						form.setEntityValue(i, form.entity, ip);
						e = jq("#" + form.parent.id).find("*[nodeid='" + nodeId + "']")[0];
						if (e) {
							e.scrollIntoView(true);
						}
					}
				});
			}
			jq(d[relClass].table).on("inserted", function (event, data) {
				var node = data.node;
				jq(dlg).find("td.freebim-item." + relClass + "[nodeid='" + node[nf.NODEID] + "']").draggable({
					helper: "clone",
					appendTo: jq(addBtn).parent(),
					zIndex: 1500
				}).off("click").off("dblclick").dblclick(function () {
					var e, nodeId = jq(this).attr("nodeid");
					if (nodeId != undefined) {
						assign(nodeId);
						form.setEntityValue(i, form.entity, ip);
						e = jq("#" + form.parent.id).find("*[nodeid='" + nodeId + "']")[0];
						if (e) {
							e.scrollIntoView(true);
						}
					}
				});
			});
		});
		jq(trash).addClass("freebim-trash freebim-button").off("click").click(function ($) {
			if (jq(trash).hasClass("ui-droppable")) {
				// disable trash, enable sorting
				jq(addBtn).show();
				enableTrash(false);
				enableSorting(true);
			} else {
				// disable sorting, enable trash
				jq(addBtn).hide();
				enableTrash(true);
				enableSorting(false);
			}
		});
		enableSorting(true);
	}
};

at.freebim.db.domain.setEntityValue = function (clazz, idx, ip, entity, field, relClass, dir, relInfo, relClicked) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
	if (entity) {
		jq(ip).attr("nodeid", entity[nf.NODEID]);
	}
	var ul = ip.childNodes[0], trash = ip.childNodes[1], rf = d.RelationFields;
	clazz.fetchEntries("li", entity, field, relClass, dir, function (entries) {
		var j, n = entries.length, getRel = function (relid) {
			var e, i;
			for (i = 0; i < n; i++) {
				e = entity[field][i];
				if (e[rf.ID] == relid) {
					return e;
				}
			}

		}, relatedStuff = function (el, ro) {
			var relid = jq(el).attr("relid"), rel = getRel(relid);
			if (relClicked && !ro) {
				jq(el).off("click").click(function (event) {
					event.stopPropagation();
					event.preventDefault();
					relClicked(rel);
					return false;
				});
			}
			if (relInfo) {
				relInfo(el, rel);
			}
		};
		jq(ul).empty();
		for (j = 0; j < n; j++) {
			var entry = entries[j], ro = clazz.form.fields[idx].readOnly;
			ul.appendChild(entry);
			jq(entry).addClass(((ro) ? "info-only" : "no-edit"));
			if (relClicked || relInfo) {
				relatedStuff(entry, ro);
			}
			if (ro) {
				jq(entry).removeClass("freebim-contextmenu");
			}
		}
	});
	// entity.entries is an array of HasEntry objects
	if (jq(trash).hasClass("ui-droppable")) {
		jq(ul).find("li." + relClass).draggable({
			stop: function (event, ui) {
				var entity = clazz.form.entity;
				clazz.form.setEntityValue(idx, entity, ip);
			}
		}).off("dblclick").dblclick(function () {
			var x = jq(ui.draggable), relId = x.attr("relid"), nodeId = x.attr("nodeid");
			d.deleteRelation(idx, clazz.form, field, ul, ip, relId, nodeId);
		});
	}
};

at.freebim.db.domain.BaseNode = {

	init: function (clazz, path) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, cn = clazz.className, i18n = net.spectroom.js.i18n;

		db.logger.debug(cn + ": BaseNode.init called");

		clazz.path = path;

		clazz.getNodes = function () {
			var arr = clazz.arr;
			if (arr) {
				var i, n = arr.length, node, res = [];
				for (i = 0; i < n; i++) {
					node = d.get(arr[i]);
					if (node && !node.deleted) {
						res.push(node);
					}
				}
				if (clazz.sort) {
					clazz.sort(res);
				}
				return res;
			} else {
				jq(document).trigger("_load", [{ cn: cn }]);
				return cn + "_loaded";
			}
		};

		clazz.sort = function () {
			var field = ((clazz.sortfield) ? clazz.sortfield : nf.NAME);
			clazz.arr.sort(function (a, b) {
				a = d.get(a)[field];
				b = d.get(b)[field];
				if (a < b)
					return -1;
				else if (a > b)
					return 1;
				else
					return 0;
			});
		};

		clazz.tdWithAbbr = function (nodeId, tr, td, row, col) {
			if (nodeId != undefined) {
				jq(td).attr("abbr", "1");
				d.renderNodeId(nodeId, cn, td, null, null);
			} else {
				return "";
			}
		};

		clazz.tdWithName = function (nodeId, tr, td, row, col) {
			if (nodeId != undefined) {
				d.get(nodeId, cn, function (node) {
					jq(td).attr("nodeid", nodeId).addClass("freebim-item " + cn);
					return clazz.getNameOf(node, td);
				});
			} else {
				return "";
			}
		};

		clazz.getId = function (node) {
			return ((node) ? node[nf.NODEID] : null);
		};

		clazz.getName = clazz.getName || function (node, elem) {
			if (node) {
				return clazz.getNameOf(node, elem);
			}
			return "";
		};
		clazz.getAbbrOf = clazz.getAbbrOf || function (entity, elem) {
			if (entity) {
				if (elem) {
					elem.title = ((db.lang == "en") ? ((entity[nf.NAME_EN]) ? entity[nf.NAME_EN] : "") : ((entity[nf.NAME]) ? entity[nf.NAME] : ""));
				}
				return ((entity && entity[nf.CODE]) ? entity[nf.CODE]
					: ((entity && entity[nf.NAME]) ? entity[nf.NAME] : ""));
			}
			return "";
		};
		clazz.getNameOf = clazz.getNameOf || function (entity, elem) {
			if (entity) {
				if (elem) {
					elem.title = ((db.lang == "en") ? ((entity[nf.DESC_EN]) ? entity[nf.DESC_EN] : "") : ((entity[nf.DESC]) ? entity[nf.DESC] : ""));
				}
				return ((db.lang == "en") ? ((entity[nf.NAME_EN]) ? entity[nf.NAME_EN] : "") : ((entity[nf.NAME]) ? entity[nf.NAME] : ""));
			}
			return "";
		};
		clazz.getDescOf = clazz.getDescOf || function (entity) {
			return ((db.lang == "en") ? ((entity[nf.DESC_EN]) ? entity[nf.DESC_EN] : "") : ((entity[nf.DESC]) ? entity[nf.DESC] : ""));
		};

		clazz.canDelete = function (entity) {
			if (entity) {
				var del = true, ec = entity[nf.CONTRIBUTED_BY];
				if (entity[nf.CLASS_NAME] == "FreebimUser"
					|| entity[nf.CLASS_NAME] == "Contributor"
					|| entity[nf.CLASS_NAME] == "Company") {
					return db.user.usermanager;
				} else if (!d.mayEdit(entity)) {
					return false;
				}
				if (ec && db.contributor && !db.contributor.mayDelete) {
					// test if entity was created by the currently logged in contributor
					del = false;
					var i, n = ec.length, c = null;
					for (i = 0; i < n; i++) {
						c = ec[i]; // c is a ContributedBy relation
						if (c[rf.ct] == d.ContributionType.CREATE && db.contributor.id == c[rf.TO_NODE]) {
							del = true;
							break;
						}
					}
				}
				return ((del) ? ((entity.deleted && entity.deleted == 1) ? false : ((entity[nf.NODEID]) ? true : false)) : false);
			}
		};
		clazz.canSave = function (entity) {
			if (!entity) {
				return false;
			}
			if (clazz.className == "FreebimUser"
				|| clazz.className == "Contributor"
				|| entity[nf.CLASS_NAME] == "Company") {
				if (!db.user.usermanager) {
					return false;
				}
			} else {
				if (!db.contributor || !db.contributor.id) {
					return false;
				}
				if (clazz.className == "Library" && !db.contributor.mayManageLibraries) {
					return false;
				}
				if (entity[nf.STATE] && entity[nf.STATE] == d.State.R) {
					if (!db.contributor.maySetReleaseStatus) {
						return false;
					}
				}
			}
			return ((entity.deleted && entity.deleted == 1) ? false : true);
		};

		clazz.relevantRelCount = function (v, field, clName) {
			var i, c = 0, n;
			if (v && v[field]) {
				n = v[field].length;
				for (i = 0; i < n; i++) {
					var rel = v[field][i], other = ((v[nf.NODEID] == rel[rf.TO_NODE]) ? rel[rf.FROM_NODE] : rel[rf.TO_NODE]);
					other *= 1;
					if (clName) {
						// we know the related clazz, count only used nodes 
						if (d[clName] && d[clName].used && d[clName].used.indexOf(other) >= 0) {
							c++;
						}
					} else {
						// not a unique related clazz, may be somthing ...
						c += ((d.relevantId(d.Component.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.DataType.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Discipline.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Library.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Measure.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Parameter.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.ParameterSet.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Phase.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.Unit.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.ValueList.className, other)) ? 1 : 0);
						c += ((d.relevantId(d.ValueListEntry.className, other)) ? 1 : 0);
					}
				}
			}
			return c;
		};

		clazz.showRelations = function (td, row, field, dir, clName) {
			var v = clazz.table.dataCallback(row); // v is i.e. a ValueListEntry
			jq(td).empty();
			if (v && v[field] && v[field].length > 0) { // v[field] is an array of i.e. HasEntry relations
				var c = 0, cd = 0;
				c = clazz.relevantRelCount(v, field, clName);
				// show only number of HasEntry relations
				if (c > 0) {
					td.appendChild(document.createTextNode(c + " "));
				}
				if (cd > 0) {
					var span = document.createElement("span");
					span.appendChild(document.createTextNode(" " + cd + " "));
					td.appendChild(span);
					jq(span).addClass(d.showDeleted);
				}
				// entries will pop up in tooltip
				td.title = "";
				jq(td).tooltip({
					open: function (event, ui) {
						// remove all other tooltips!
						jq(ui.tooltip).siblings(".ui-tooltip").remove();
					},
					content: function (callback) {
						clazz.fetchEntries("li", v, field, clName, dir, function (entries) {
							var j, title = document.createElement("ul"), n = entries.length, x = jq(title);
							for (j = 0; j < n; j++) {
								x.append(entries[j]);
							}
							// call the passed tooltip function to set the content
							callback(x.html());
						});
					}
				});
			}
		};

		clazz.setFieldStates = function (mayEdit) {
			var i, n = clazz.form.fields.length;
			for (i = 0; i < n; i++) {
				var f = clazz.form.fields[i];
				switch (f.field) {
					case nf.NAME:
					case nf.NAME_EN:
					case nf.DESC:
					case nf.DESC_EN:
					case nf.CODE:
					case nf.PARENTS:
					case nf.PARTS:
					case nf.HAS_PARAMETER:
					case nf.MATERIAL:
					case nf.DISCIPLINE:
					case nf.URL:
					case nf.MEASURES:
					case nf.HP:
					case nf.HEX_COLOR:
					case nf.DEFAULT: // defaultString
					case nf.DATATYPE:
					case nf.HAS_VALUE:
					case nf.HAS_ENTRY:
					case nf.COMMENT:
					case nf.CHILDS:
					case nf.PSETS:
					case nf.OWNERS:
					case nf.PREFIX:
						f.readOnly = !mayEdit;
						break;
				}
			}
		};

		/**
		 * @param {at.freebim.db.domain.ValueList} v The ValueList to fetch all ValueListEntry objects for.
		 * @param {function} doneFn Callback that would be called ater all entries have been fetched. Entries passed as first argument.  
		 */
		clazz.fetchEntries = function (type, v, field, entryClass, dir, doneFn, abbr) {
			if (!v) {
				return;
			}
			var max = 1000, fn = function () {
				var n, i, entries = [], f = v[field], done, add;
				if (max-- < 0) {
					// prevent block on error (if we aren't able to fetch all entries ...)
					return;
				}
				if (f) {
					f.sort(function (a, b) {
						var v1 = a[rf.ORDERING], v2 = b[rf.ORDERING];
						if (v1 === undefined || v2 === undefined) {
							v1 = a[rf.TIMESTAMP], v2 = b[rf.TIMESTAMP];
						}
						return (v1 - v2);
					});
					n = f.length;
					done = n;
					add = function (nid, relid) {
						var div = document.createElement(type);
						d.getOrLoad(nid, entryClass, function (entry) {
							done--;
							jq(div).attr("nodeid", nid)
								.attr("relid", relid)
								.attr("maxLength", "32")
								.addClass("freebim-item" + ((entryClass) ? " " + entryClass : ""));
							if (abbr) {
								jq(div).attr("abbr", "1");
							}
							entries.push(div);
							d.renderNode(entry, div);
							if (done == 0) {
								doneFn(entries);
							}
						});
					};
					for (i = 0; i < n; i++) {
						var rel = f[i], nid = ((dir == "IN") ? rel[rf.FROM_NODE] : ((dir == "OUT") ? rel[rf.TO_NODE] : ((rel[rf.FROM_NODE] == v[nf.NODEID]) ? rel[rf.TO_NODE] : rel[rf.FROM_NODE])));
						add(nid, rel[rf.ID]);
					}
				}
				return;
			};
			// call the fetch function
			fn();
		};


		clazz.saveRelations = function (entity, rels, isInsert, ts, callback, bsddGuidChanged) {
			if (rels != null && jq.isArray(rels) && rels.length > 0) {
				var relData = [], data, i, m, n = rels.length, rel;
				for (i = 0; i < n; i++) {
					rel = rels[i];
					data = rel[rf.DATA];
					if (data) {
						m = data.length;
						for (var j = 0; j < m; j++) {
							var r = data[j];
							if (r[rf.ID] != undefined && isNaN(r[rf.ID]) && r[rf.ID].indexOf("_") == 0) {
								// remove temporary ID
								r[rf.ID] = null;
							}
							switch (rel[rf.DIRECTION]) {
								case "IN": r[rf.TO_NODE] = entity[nf.NODEID]; break;
								case "OUT": r[rf.FROM_NODE] = entity[nf.NODEID]; break;
								case "BOTH":
									if (r[rf.FROM_NODE] == null) {
										r[rf.FROM_NODE] = entity[nf.NODEID];
									} else if (r[rf.TO_NODE] == null) {
										r[rf.TO_NODE] = entity[nf.NODEID];
									}
									break;
							}
						}
					}
					var newData = {};
					newData[rf.CLASS_NAME] = rel[rf.CLASS_NAME];
					newData[rf.TYPE] = rel[rf.TYPE];
					newData[rf.DIRECTION] = rel[rf.DIRECTION];
					newData[rf.RELATIONS] = rel[rf.DATA];
					relData.push(newData);
				}
				db.post("/" + path + "/saveRelations",
					{ nodeId: entity[nf.NODEID], relArray: relData },
					i18n.g("SAVING_RELATIONS"), // "Beziehungen speichern ...",
					function (response) {
						if (response.error != undefined) {
							alert(response.error);
						} else {
							if (response.result) {
								entity = response.result;

								if (isInsert) {
									// is the newly inserted node relevant?
									switch (entity[nf[nf.CLASS_NAME]]) {
										case d.Component.className:
											if (entity[nf.PARENTS] && entity[nf.PARENTS].length > 0) {
												// (:Component)-[:PARENT_OF]->(:Component) exists
												d.Component.used.push(entity[nf.NODEID]);
											}
											break;
										case d.Parameter.className:
											if (entity[nf.HP] && entity[nf.HP].length > 0) {
												// (:Component)-[:HAS_PARAMETER]->(:Parameter) exists
												d.Parameter.used.push(entity[nf.NODEID]);
											}
											break;
									}
								}

								d.listen(entity);
								if (bsddGuidChanged) {
									jq(document).trigger("_bsddGuidChanged", [{ entity: entity }]);
								}
								//								jq(document).trigger(cn + "_saved");
								if (callback) {
									callback(entity);
								}
								if (isInsert) {

									// is the newly inserted node relevant?
									switch (entity[nf[nf.CLASS_NAME]]) {
										case d.Component.className:
											if (entity[nf.PARENTS] && entity[nf.PARENTS].length > 0) {
												// (:Component)-[:PARENT_OF]->(:Component) exists
												d.Component.used.push(entity[nf.NODEID]);
											}
											break;
										case d.Parameter.className:
											if (entity[nf.HP] && entity[nf.HP].length > 0) {
												// (:Component)-[:HAS_PARAMETER]->(:Parameter) exists
												d.Parameter.used.push(entity[nf.NODEID]);
											}
											break;
									}

									jq("table.clazz_" + entity[nf.CLASS_NAME]).trigger("newInserted", [{ entity: entity, ts: ts }]);
									if (entity[nf.CLASS_NAME] == d.Component.className) {
										jq("div.Component.tree-node[nodeid]").trigger("newInserted", [{ entity: entity, ts: ts }]);
									}
									jq(document).trigger("alert", [{
										title: "DLG_TITLE_FREEBIM_INFO",
										content: i18n.gA1("SUCCESSFULLY_INSERTED", clazz.i18n, clazz.getNameOf(entity)),
										autoClose: 1500
									}]);
								} else {
									jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{ entity: entity, ts: ts }]);
									jq(document).trigger("alert", [{
										title: "DLG_TITLE_FREEBIM_INFO",
										content: i18n.gA1("SUCCESSFULLY_SAVED", clazz.i18n, clazz.getNameOf(entity)),
										autoClose: 1500
									}]);
								}
							}
						}
					},
					null, // fail
					null,
					"PUT"
				);
			} else {
				if (isInsert) {
					jq("table.clazz_" + entity[nf.CLASS_NAME]).trigger("newInserted", [{ entity: entity, ts: ts }]);
					jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO",
						content: i18n.gA1("SUCCESSFULLY_INSERTED", clazz.i18n, clazz.getNameOf(entity)),
						autoClose: 1500
					}]);
				} else {
					jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{ entity: entity, ts: ts }]);
					jq(document).trigger("alert", [{
						title: "DLG_TITLE_FREEBIM_INFO",
						content: i18n.gA1("SUCCESSFULLY_SAVED", clazz.i18n, clazz.getNameOf(entity)),
						autoClose: 1500
					}]);
				}
			}
		};
		// BaseNode relations:

		clazz.relations.push(rf.make(nf.EQUALS, d.RelationTypeEnum.EQUALS, "Equals", "BOTH"));

		if (clazz.isContributed || clazz.className === d.Document.className) {
			// UuidIdentifyable relations
			clazz.relations.push(rf.make(nf.REFERENCES, d.RelationTypeEnum.REFERENCES, "References", "OUT"));
			clazz.relations.push(rf.make(nf.LOADED_BSDD, d.RelationTypeEnum.BSDD, "Bsdd", "IN"));
		}

		// ContributedBaseNode relations
		if (clazz.isContributed) {
			clazz.relations.push(rf.make(nf.CONTRIBUTED_BY, d.RelationTypeEnum.CONTRIBUTED_BY, "ContributedBy", "OUT"));
			clazz.relations.push(rf.make(nf.DOCUMENTED, d.RelationTypeEnum.DOCUMENTED_IN, "DocumentedIn", "OUT"));
		}

		// HierarchicalBaseNode relations
		if (clazz.isHierarchical) {
			clazz.relations.push(rf.make(nf.PARENTS, d.RelationTypeEnum.PARENT_OF, "ParentOf", "IN"));
			clazz.relations.push(rf.make(nf.CHILDS, d.RelationTypeEnum.PARENT_OF, "ParentOf", "OUT"));
			clazz.relations.push(rf.make(nf.PSETS, d.RelationTypeEnum.HAS_PARAMETER_SET, "HasParameterSet", "OUT"));
		}

		clazz.initialized = true;
	},

	postInit: function (clazz, noEqualsField) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		if (clazz.table) {
			clazz.table.counter = true;
			clazz.table.cols.push(d.columns.validFrom);
			clazz.table.initialSort = 1;
			/*clazz.table.cellCreatedTimeout = null;
			clazz.table.cellCreated = function (data) {
				var td = data.td;
				if (jq(td).hasClass("freebim-item")) {
					var nodeId = jq(data.td).attr("value") || jq(data.tr).attr("value"), node = d.get(nodeId);
					d.setFreebimItemClasses(node, td);
				}
				if (clazz.table.cellCreatedTimeout) {
					clearTimeout(clazz.table.cellCreatedTimeout);
				}
				clazz.table.cellCreatedTimeout = setTimeout(function() {
					db.user.doShowAbstract();
				}, 200);
			};*/

			clazz.table.rowCreated = function (data) {
				var /*table = data.table, row = data.row, */tr = data.tr, node = data.value; /* node = table.dataCallback(row); */
				if (node) {
					jq(tr).attr("nodeid", node[nf.NODEID]);
					if (node.deleted && node.deleted == 1) {
						jq(tr).addClass(d.showDeleted);
					} else {
						jq(tr).removeClass(d.showDeleted);
					}
					if (node.unused && node.unused == 1) {
						jq(tr).addClass(d.showUnused);
					} else {
						jq(tr).removeClass(d.showUnused);
					}
					if (node[nf.TYPE] && node[nf.TYPE] == 2) {
						jq(tr).addClass("abstract");
					}
				}
			};
			jq(clazz.table).on(clazz.className + "_tableCreated", function (event, data) {
				if (clazz.selectedNodeId) {
					var table = clazz.table, i, n = table.tbody.childNodes.length;
					if (clazz.selectedNodeId) {
						for (i = 0; i < n; i++) {
							var tr = table.tbody.childNodes[i];
							var nodeId = jq(tr).attr("nodeid");
							if (nodeId == clazz.selectedNodeId) {
								jq("#" + clazz.className + " tr.jsTableRow-selected").removeClass("jsTableRow-selected");
								jq(tr).addClass("jsTableRow-selected");
								tr.scrollIntoView(true);
								break;
							}
						}
					}
				}
				jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
			});

			jq(net.spectroom.js.table).on("_tableCount", function (event, data) {
				if (clazz.table && clazz.table.id) {
					if (clazz.table.counterTimer) {
						clearTimeout(clazz.table.counterTimer);
					}
					clazz.table.counterTimer = setTimeout(function () {
						db.logger.debug("on _tableCount " + clazz.table.id);
						var counter = jq("#" + clazz.table.id).find("div.counter");
						if (counter && counter.length > 0) {
							var invisible = jq("#" + clazz.table.id + " .jsTable-data tbody").children(".jsTable-emptyRow, .hide-deleted, .hide-unused, .jsTable-filtered, .hide-abstract").length,
								totalCount = jq("#" + clazz.table.id + " .jsTable-data tbody").children().length,
								visCount = (totalCount - invisible);
							jq(counter).html("" + visCount);
						}
					}, 250);
				}
			});
		}

		// insert ID and freeBIM-ID fields:
		clazz.form.fields.splice(0, 0, d.fields.id, d.fields.freebimID);
		clazz.form.fields.splice(0, 0, {
			label: "",
			type: "hidden",
			field: nf.CLASS_NAME,
			fixed: clazz.className
		});

		// move up bsDD-GUID field (after freeBIM-ID field):
		var idx = clazz.form.fields.indexOf(d.fields.bsDD);
		if (idx >= 0) {
			clazz.form.fields.splice(idx, 1);
			clazz.form.fields.splice(2, 0, d.fields.bsDD);
		}

		if (!noEqualsField) {
			clazz.form.fields.push({
				label: "EQUALITY",
				type: "custom",
				field: nf.EQUALS,
				createField: function (form, i, ip) {
					var orderable = false;
					relAdded = function (rel, okFn) {
						// rel is the just created relation
						d.rel.Equals.init();
						jq(d.rel.Equals).trigger("editEquals", [{ rel: rel, okFn: okFn }]);
					};
					d.relationFormField(form, i, ip, orderable, clazz.title, d.RelationTypeEnum.EQUALS, nf.EQUALS, clazz.className, "BOTH", relAdded);
				},
				setEntityValue: function (form, i, entity, ip) {
					var relClicked = function (rel) {
						var okFn = function () {
							form.setEntityValue(i, entity, ip);
						};
						d.rel.Equals.init();
						jq(d.rel.Equals).trigger("editEquals", [{ rel: rel, okFn: okFn }]);

					}, relInfo = function (el, rel) {
						var okFn = function (eq) {
							var sub = document.createElement("sub");
							sub.appendChild(document.createTextNode(" " + eq));
							jq(sub).appendTo(el);
						};
						d.rel.Equals.init();
						jq(d.rel.Equals).trigger("equalsRel", [{ rel: rel, okFn: okFn }]);
					};
					d.setEntityValue(form.clazz, i, ip, entity, nf.EQUALS, clazz.className, "BOTH", relInfo, relClicked);
				}
			});
		}

	}
}; // at.freebim.db.domain.BaseNode

at.freebim.db.domain.ParameterSetType = {
	className: "ParameterSetType",
	i18n: "CLAZZ_PARAMETERSETTYPE",
	title: net.spectroom.js.i18n.g("CLAZZ_PARAMETERSETTYPE"), // "Parameterset Typ",
	values: [1, 2],
	data: function (id) {
		var i18n = net.spectroom.js.i18n;
		switch (id) {
			case 1:
				return i18n.get("PROPERTY");
			case 2:
				return i18n.get("QUANTITY");
		}
	},
	dataValue: function (id) {
		return id;
	}
};

at.freebim.db.domain.ParameterType = {
	className: "ParameterType",
	i18n: "CLAZZ_PARAMETERTYPE",
	title: net.spectroom.js.i18n.g("CLAZZ_PARAMETERTYPE"), // "Parameter Typ",
	values: [9, 2, 1, 3, 4, 5],
	data: function (id) {
		var i18n = net.spectroom.js.i18n;
		switch (id) {
			default:
			case 9:
				return i18n.get("PTYPE_UNDEFINED");
			case 2:
				return i18n.get("PTYPE_INSTANCE");
			case 1:
				return i18n.get("PTYPE_TYPE");
			case 3:
				return i18n.get("PTYPE_DERIVED");
			case 4:
				return i18n.get("PTYPE_IFC_ATTRIBUTE");
			case 5:
				return i18n.get("PTYPE_IFC_OPTIONAL_ATTRIBUTE");
		}
	},
	dataValue: function (id) {
		return id;
	}
};

at.freebim.db.domain.BigBangNode = {
	className: "BigBangNode",
	i18n: "FREEBIM_TYROL",
	title: "*",
	path: undefined,
	isContributed: true,
	isHierarchical: true,
	getNameOf: function (entity, elem) {
		return "*";
	},
	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.BigBangNode, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [];
		self.base = Object.create(d.BaseNode);
		self.base.init(self, "bbn");

		d.Library.init();

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			{
				label: "Version",
				type: "hidden",
				field: nf.VERSION,
			},
			{
				label: "TAB_LIBRARIES",
				type: "custom",
				field: nf.CHILDS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Library.title, d.RelationTypeEnum.PARENT_OF, nf.CHILDS, "Library", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.CHILDS, "Library", "OUT");
				}
			},
			d.fields.state,
			d.fields.statusComment,
			d.fields.docs
		];
		self.form.fields[3].type = "hidden";
		self.form.fields[4].type = "hidden";
		self.form.fields[5].type = "hidden";
		self.form.fields[6].type = "hidden";
		self.form.fields[7].type = "hidden";
		self.form.fields[10].type = "hidden";
		self.form.fields[11].type = "hidden";
		self.form.fields[12].type = "hidden";

		self.form.create(null, self, self);

		self.base.postInit(self, true);
		self.initialized = true;
	}
};

at.freebim.db.domain.BsddNode = {
	className: "BsddNode",
	i18n: "CLAZZ_BSDDNODE",
	title: net.spectroom.js.i18n.g("CLAZZ_BSDDNODE"),
	isContributed: false,
	isHierarchical: false,
	getNameOf: function (entity, elem) {
		return entity[at.freebim.db.domain.NodeFields.BSDD_GUID];
	},
	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.BsddNode;
		if (!self.initialized) {
			self.relations = [rf.make(nf.NODES, d.RelationTypeEnum.BSDD, "Bsdd", "OUT")];
			self.base = Object.create(d.BaseNode);
			self.base.init(self, "bsdd");
		}
	}
};

at.freebim.db.domain.SimpleNamedNode = {
	className: "SimpleNamedNode",
	i18n: "CLAZZ_SIMPLENAMEDNODE",
	title: net.spectroom.js.i18n.g("CLAZZ_SIMPLENAMEDNODE"), // "Element",
	isContributed: true,
	isHierarchical: true,
	getNameOf: function (entity, elem) {
		var nf = at.freebim.db.domain.NodeFields;
		if (entity) {
			if (elem) {
				elem.title = ((at.freebim.db.lang == "en") ? ((entity[nf.DESC_EN]) ? entity[nf.DESC_EN] : "") : ((entity[nf.DESC]) ? entity[nf.DESC] : ""));
			}
			return ((entity[nf.CODE]) ? entity[nf.CODE] + " - " : "") + ((at.freebim.db.lang == "en") ? ((entity[nf.NAME_EN]) ? entity[nf.NAME_EN] : "") : ((entity[nf.NAME]) ? entity[nf.NAME] : ""));
		}
		return "";
	},
	init: function () {
		at.freebim.db.domain.SimpleNamedNode.initialized = true;
	}
};

at.freebim.db.domain.ComponentType = {
	className: "ComponentType",
	i18n: "CLAZZ_COMPONENTTYPE",
	title: net.spectroom.js.i18n.g("CLAZZ_COMPONENTTYPE"), // "Art",
	values: [1, 2, 3, 4, 5],
	data: function (id) {
		var i18n = net.spectroom.js.i18n;
		switch (id) {
			case 1:
				return i18n.g("COMPTYPE_UNDEFINED"); // "undefiniert";
			case 2:
				return i18n.g("COMPTYPE_ABSTRACT"); // "abstrakt";
			case 3:
				return i18n.g("COMPTYPE_CONCRETE"); // "konkret";
			case 4:
				return i18n.g("COMPTYPE_COMPOSED"); // "zusammengesetzt";
			case 5:
				return i18n.g("COMPTYPE_COMPOSED_VARIABLE"); // "variabel zusammengestzt";
		}
	},
	dataValue: function (id) {
		return id;
	}
};

at.freebim.db.domain.Component = {

	className: "Component",
	i18n: "CLAZZ_COMPONENT",
	title: net.spectroom.js.i18n.g("CLAZZ_COMPONENT"), // "Komponente",
	arr: null,
	isContributed: true,
	isHierarchical: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
			f = d.filters, fmr = f.filterMarkedRelated, self = d.Component;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.HAS_PARAMETER, d.RelationTypeEnum.HAS_PARAMETER, "HasParameter", "OUT"),
		rf.make(nf.PARTS, d.RelationTypeEnum.COMP_COMP, "ComponentComponent", "OUT"),
		rf.make(nf.MATERIAL, d.RelationTypeEnum.OF_MATERIAL, "OfMaterial", "OUT"),
		rf.make(nf.OVERRIDE, d.RelationTypeEnum.VALUE_OVERRIDE, "ValueOverride", "OUT")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "component");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			{
				label: "ASSIGNED_TO",
				type: "custom",
				field: nf.PARENTS,
				createField: function (form, i, ip) {
					var orderable = false;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.PARENT_OF, nf.PARENTS, "Component", "IN");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.PARENTS, "Component", "IN");
				}
			},
			{
				label: "ASSIGNED_COMPONENTS",
				type: "custom",
				field: nf.CHILDS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.PARENT_OF, nf.CHILDS, "Component", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.CHILDS, "Component", "OUT");
				}
			},
			{
				label: "ASSIGNED_PARAMETERS",
				type: "custom",
				field: nf.HAS_PARAMETER,
				createField: function (form, i, ip) {
					var orderable = true,
						relAdded = function (rel, okFn) {
							// rel is the just created relation
							d.rel.HasParameter.init();
							jq(d.rel.HasParameter).trigger("editPhase", [{ rel: rel, okFn: okFn }]);
						};
					d.relationFormField(form, i, ip, orderable, d.Parameter.title, d.RelationTypeEnum.HAS_PARAMETER, nf.HAS_PARAMETER, "Parameter", "OUT", relAdded);
				},
				setEntityValue: function (form, i, entity, ip) {
					var relClicked = function (rel) {
						var okFn = function () {
							form.setEntityValue(i, entity, ip);
						};
						d.rel.HasParameter.init();
						jq(d.rel.HasParameter).trigger("editPhase", [{ rel: rel, okFn: okFn }]);

					}, relInfo = function (el, rel) {
						var okFn = function (ph) {
							var sub = document.createElement("sub");
							jq(sub).attr("abbr", "1").appendTo(el);
							d.renderNode(ph, sub);
							jq(sub).removeClass("freebim-contextmenu");
						};
						d.rel.HasParameter.init();
						jq(d.rel.HasParameter).trigger("phaseFromRel", [{ rel: rel, okFn: okFn }]);
					};
					d.setEntityValue(form.clazz, i, ip, entity, nf.HAS_PARAMETER, "Parameter", "OUT", relInfo, relClicked);
				}
			},
			{
				label: "PARTS", // "Bestandteile",
				type: "custom",
				field: nf.PARTS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.COMP_COMP, nf.PARTS, "Component");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.PARTS, "Component", "OUT");
				}
			},
			{
				label: "MATERIAL",
				type: "custom",
				field: nf.MATERIAL,
				createField: function (form, i, ip) {
					var orderable = false;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.OF_MATERIAL, nf.MATERIAL, "Component");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.MATERIAL, "Component", "OUT");
				}
			},
			{
				label: "PARAMETERSETS", // "Parameter-Sets",
				type: "custom",
				field: nf.PSETS,
				createField: function (form, i, ip) {
					var orderable = false;
					d.relationFormField(form, i, ip, orderable, d.ParameterSet.title, d.RelationTypeEnum.HAS_PARAMETER_SET, nf.PSETS, "ParameterSet");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.PSETS, "ParameterSet", "OUT");
				}
			}, {
				label: "CLAZZ_COMPONENTTYPE",
				type: "select",
				field: nf.TYPE,
				values: d.ComponentType.values,
				data: d.ComponentType.data,
				dataValue: d.ComponentType.dataValue
			},
			d.fields.state,
			d.fields.statusComment,
			d.fields.docs,
			d.fields.bsDD
		];

		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.edit = true;
		self.table.add = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByCodeAndName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					var params = node[nf.HAS_PARAMETER];
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.PARENTS], "IN")) {
						return false;
					}
					if (!fmr(params, "OUT")) {
						return false;
					}
					if (!fmr(node[nf.PARTS], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.PSETS], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.MATERIAL], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.DOCUMENTED], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					if (params) {
						d.rel.HasParameter.init();
						var ph, r, i, n = params.length;
						for (i = 0; i < n; i++) {
							r = params[i];
							ph = d.rel.HasParameter.phaseForUuid(r[rf.PHASE]);
							if (ph && db.isMarked(ph[nf.NODEID])) {
								return false;
							}
						}
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}/*, 
			{ add : function (clazz, filterdiv, filterFn, srcFn) {
					var div = document.createElement("div"), cbBsdd = document.createElement("input"),
						perform = function (row) {
							var node = at.freebim.db.domain.get(clazz.arr[row]);
							var val = jq("input[type='radio'][name='material']:checked").val();
							switch (val) {
							default: return undefined;
							case "ALL": return false;
							case "COMP": return (node[nf.IS_MATERIAL] === true || node[nf.IS_MATERIAL] === "true" || node[nf.IS_MATERIAL] === 1);
							case "MAT": return !(node[nf.IS_MATERIAL] === true || node[nf.IS_MATERIAL] === "true" || node[nf.IS_MATERIAL] === 1);
							}
						};
					jq(cbBsdd).attr("type", "checkbox").change(function () {
						filterFn();
					});
					jq(div).addClass("filter-material").append("<input type='radio' name='material' value='ALL' checked>alles <input type='radio' name='material' value='COMP'>Bauteile <input type='radio' name='material' value='MAT'>Materialien");
					jq(filterdiv).append(div);
					jq("input[type='radio'][name='material']").change(function()  {
						filterFn();
					});
					return perform;
				},
				f : null
			}*/
		];

		/*		self.table.customFilter = function (filterdiv) {
					var ip = document.createElement("input");
					ip.type = "radio";
					ip.name = "ComponentRadio";
					ip.value = "ALL";
					ip.checked = true;
					filterdiv.appendChild(ip);
					filterdiv.appendChild(document.createTextNode("alle"));
					ip = document.createElement("input");
					ip.type = "radio";
					ip.name = "ComponentRadio";
					ip.value = "COMP";
					filterdiv.appendChild(ip);
					filterdiv.appendChild(document.createTextNode("Bauteile"));
					ip = document.createElement("input");
					ip.type = "radio";
					ip.name = "ComponentRadio";
					ip.value = "MAT";
					filterdiv.appendChild(ip);
					filterdiv.appendChild(document.createTextNode("Materialien"));
					jq("input[name=ComponentRadio]:radio").change(function () {
						self.table.doFilter();
					});
				};*/
		self.table.dataCallback = function (row, tr) {
			var id = self.arr[row], node = d.get(id);
			jq(tr).attr("nodeid", id);
			if (node) {
				return node;
			}
			d.getOrLoad(id, d.Component.className);
			return "delayed";
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "code" + self.table.csvDelim
			+ "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim
			+ "bsDD-GUID" + self.table.csvDelim
			+ "type" + self.table.csvDelim
			+ "status" + self.table.csvDelim;
		self.table.cols = [
			d.columns.code,
			{
				label: "NAME", // "Name",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
					if (v[nf.IS_MATERIAL]) {
						jq(td).addClass("material");
					}
				},
				cl: "freebim-item Component",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Component.getNameOf(v);
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
				}
			},
			{
				label: "DESCRIPTION", // "Beschreibung",
				field: nf.DESC,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var nodeId = self.arr[row],
						node = d.get(nodeId),
						desc = d.Component.getDescOf(node);
					jq(td).html(net.spectroom.js.shrinkText(desc, 64, td)).addClass("freebim-desc").attr("nodeid", node[nf.NODEID]);
				},
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Component.getDescOf(v);
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]) + d.csv(v[nf.BSDD_GUID]);
				}
			},
			{
				label: "CLAZZ_COMPONENTTYPE", // "Art",
				field: nf.TYPE,
				type: "callback",
				values: d.ComponentType.values,
				data: d.ComponentType.data,
				dataValue: d.ComponentType.dataValue,
				sort: true
			}, {
				label: "CLAZZ_PARAMETER", // "Parameter",
				field: nf.HAS_PARAMETER,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.HAS_PARAMETER, "OUT", "Parameter");
				},
				cl: "Parameter",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.HAS_PARAMETER, "Parameter");
					}
					return 0;
				},
				csv: false
			}, {
				label: "PARTS", // "Bestandteile",
				field: nf.PARTS,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.PARTS, "OUT", "Component");
				},
				cl: "Component",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.PARTS, "Component");
					}
					return 0;
				},
				csv: false
			},
			d.columns.state
		];

		self.base.postInit(self);
		self.table.initialSort = 2;
	}
}; // at.freebim.db.domain.Component

at.freebim.db.domain.Contributor = {
	className: "Contributor",
	i18n: "CLAZZ_CONTRIBUTOR",
	title: net.spectroom.js.i18n.g("CLAZZ_CONTRIBUTOR"), // "Bearbeiter",
	arr: null,

	getAbbrOf: function (entity, elem) {
		var nf = at.freebim.db.domain.NodeFields;
		if (entity) {
			if (elem) {
				elem.title = entity[nf.CODE] + " - " + entity[nf.FIRSTNAME] + " " + entity[nf.LASTNAME];
			}
			return entity[nf.CODE];
		} else {
			return "";
		}
	},
	getNameOf: function (entity, elem) {
		var nf = at.freebim.db.domain.NodeFields;
		if (entity) {
			if (elem) {
				elem.title = ((entity[nf.CODE]) ? entity[nf.CODE] : "");
			}
			return ((entity[nf.TITLE]) ? entity[nf.TITLE] + " " : "")
				+ ((entity[nf.FIRSTNAME]) ? entity[nf.FIRSTNAME] + " " : "")
				+ ((entity[nf.LASTNAME]) ? entity[nf.LASTNAME] : "");
		} else {
			return "";
		}
	},

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.Contributor, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.RESPONSIBLE, d.RelationTypeEnum.RESPONSIBLE, "Responsible", "OUT"),
		rf.make(nf.WORKS_FOR, d.RelationTypeEnum.WORKS_FOR, "WorksFor", "OUT")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "contributor");


		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code, {
				label: "FIRST_NAME", // "Vorname",
				type: "text",
				field: nf.FIRSTNAME
			}, {
				label: "LAST_NAME", // "Familienname",
				type: "text",
				field: nf.LASTNAME
			}, {
				label: "TITLE", // "Titel",
				type: "text",
				field: nf.TITLE
			}, {
				label: "IFD_USER_EMAIL", // "E-Mail",
				type: "text",
				field: nf.EMAIL
			}, {
				label: "COMPANY", // "Company",
				type: "custom",
				field: nf.WORKS_FOR,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Company.title, d.RelationTypeEnum.WORKS_FOR, nf.WORKS_FOR, "Company", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.WORKS_FOR, "Company", "OUT");
				}
			}, {
				label: "RESPONSIBLE_FOR_LIB", // "Verantwortlich für Bibliothek",
				type: "custom",
				field: nf.RESPONSIBLE,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Library.title, d.RelationTypeEnum.RESPONSIBLE, nf.RESPONSIBLE, "Library", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.RESPONSIBLE, "Library", "OUT");
				}
			}, {
				label: "PERMISSIONS", // "Berechtigungen",
				type: "checkboxgroup",
				field: nf.ROLES,
				values: [{
					label: "PERMISSION_DELETE", // "Löschberechtigung",
					value: "ROLE_DELETE"
				}, {
					label: "PERMISSION_DEMO", // "Demonstrator",
					value: "ROLE_VIEW_EXTENSIONS"
				}, {
					label: "PERMISSION_LIBRARY", // "Bibliotheken verw.",
					value: "ROLE_LIBRARY_REFERENCES"
				}, {
					label: "PERMISSION_STATUS", // "Status ändern",
					value: "ROLE_SET_STATUS"
				}, {
					label: "PERMISSION_RELEASE", // "Freigeben",
					value: "ROLE_SET_RELEASE_STATUS"
				}]
			}
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.dataCallback = function (row) {
			var node = d.get(self.arr[row]);
			if (db.user.usermanager == true) {
				return node;
			} else {
				return ((node && node[nf.RESPONSIBLE] && node[nf.RESPONSIBLE].length > 0) ? node : null);
			}
		};
		self.table.filters = [
			{
				add: f.text,
				f: function (node, filter, opt) {
					if (opt) {
						return (!node || (node[nf.CODE] != filter && node[nf.FIRSTNAME] != filter && node[nf.LASTNAME] != filter));
					}
					filter = ((filter.toLowerCase) ? filter.toLowerCase() : filter);
					var src = ((node) ? ((node[nf.FIRSTNAME]) ? node[nf.FIRSTNAME] + " " : "") + ((node[nf.LASTNAME]) ? node[nf.LASTNAME] + " " : "") + ((node[nf.CODE]) ? node[nf.CODE] : "") : "");
					if (src && src.toLowerCase) {
						src = src.toLowerCase();
						return (src.indexOf(filter) < 0);
					}
				}
			}];
		self.table.idCol = nf.NODEID;
		self.table.cols = [
			{
				label: "CODE", // "Code",
				field: nf.CODE,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("abbr", "1");
					d.renderNode(v, td);
				},
				cl: "freebim-item Contributor",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return ((v != null) ? v[nf.CODE] : "");
				},
				sort: true
			},
			{
				label: "FIRST_NAME", // "Vorname",
				field: nf.FIRSTNAME,
				type: "text",
				sort: true
			},
			{
				label: "LAST_NAME", // "Familienname",
				field: nf.LASTNAME,
				type: "text",
				sort: true
			},
			{
				label: "TITLE", // "Titel",
				field: nf.TITLE,
				type: "text",
				sort: false
			}];
		if (db.user.usermanager == true) {
			self.table.edit = true;
			self.table.add = true;
			self.table.cols.push({
				label: "RESPONSIBLE", // "Verantwortlich",
				field: nf.RESPONSIBLE,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row); // v is a Contributor
					jq(td).empty();
					if (v && v[nf.RESPONSIBLE] && v[nf.RESPONSIBLE].length > 0) { // v[nf.RESPONSIBLE] is an array of Responsible relations
						self.fetchEntries("div", v, nf.RESPONSIBLE, "Library", "OUT", function (responsible) {
							jq(td).empty();
							var j;
							for (j = 0; j < responsible.length; j++) {
								td.appendChild(responsible[j]);
							}
							jq(self.table).trigger("cellChanged", [{}]);
						});
					}
				},
				sort: false
			});
			self.table.cols.push({
				label: "PERMISSION_DEL", // "löschen",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_DELETE") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			});
			self.table.cols.push({
				label: "PERMISSION_DEMO", // "Demonstrator",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_VIEW_EXTENSIONS") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			});
			self.table.cols.push({
				label: "PERMISSION_LIB", // "Bib. verw.",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_LIBRARY_REFERENCES") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			});
			self.table.cols.push({
				label: "PERMISSION_STATUS", // "Status ändern",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_SET_STATUS") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			});
			self.table.cols.push({
				label: "PERMISSION_RELEASE", // "Freigeben",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_SET_RELEASE_STATUS") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			});
		} else {
			self.table.cols.push({
				label: "IFD_USER_EMAIL", // "E-Mail",
				field: nf.EMAIL,
				type: "text",
				sort: true,
				href: function (row, col) {
					var entity = d.get(self.arr[row]);
					document.location.href = "mailto:" + entity[nf.EMAIL] + "?subject=freeBIM - Datenbank";
				}
			});
			self.table.cols.push({
				label: "COMPANY", // "Unternehmen",
				field: nf.COMPANY,
				type: "custom",
				maxlen: 32,
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row); // v is a Contributor
					jq(td).empty();
					if (v && v[nf.WORKS_FOR] && v[nf.WORKS_FOR].length > 0) { // v[nf.WORKS_FOR] is an array of WorksFor relations
						self.fetchEntries("div", v, nf.WORKS_FOR, "Company", "OUT", function (c) {
							jq(td).empty();
							var j;
							for (j = 0; j < c.length; j++) {
								td.appendChild(c[j]);
							}
							jq(self.table).trigger("cellChanged", [{}]);
						});
					}
				},
				sort: false
			});
		}
		self.sortfield = nf.CODE;
		self.base.postInit(self, true);
		self.table.initialSort = 3;
	}
}; // at.freebim.db.domain.Contributor

at.freebim.db.domain.Company = {
	className: "Company",
	i18n: "COMPANY",
	title: net.spectroom.js.i18n.g("COMPANY"), // "Company",
	arr: null,

	init: function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, f = d.filters,
			fmr = f.filterMarkedRelated, self = d.Company, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.WORKS_FOR, d.RelationTypeEnum.WORKS_FOR, "WorksFor", "IN"),
		rf.make(nf.COMPANY_COMPANY, d.RelationTypeEnum.COMPANY_COMPANY, "CompanyCompany", "BOTH")];
		self.base = Object.create(d.BaseNode);
		self.base.init(self, "company");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.code,
			d.fields.name,
			{
				label: "URL",
				type: "text",
				field: nf.URL
			}, {
				label: "LOGO",
				type: "custom",
				field: nf.LOGO,
				createField: function (form, i, ip) {
					// create a 'dropzone.js' image upload 
					var img = document.createElement("img");
					jq(img).appendTo(ip).dropzone({
						url: "/company/upload",
						thumbnailHeight: "130px",
						clickable: true,
						acceptedFiles: "image/*",
						maxFilesize: 2,
						init: function () {
							this.on("success", function (file, response) {
								form.entity[nf.LOGO] = response.result;
								jq(img).attr("src", "company/logo/" + form.entity[nf.LOGO])
									.attr("alt", form.entity[nf.NAME])
									.attr("height", "40px")
									.css("width", "")
									.css("height", "");
							});
							this.on("complete", function (file, response) {
								console.log(response);
							});
							this.on("error", function (response, error, xhr) {
								console.log(response);
							});
						}
					})
						.css("width", "100%")
						.css("height", "30px");
				},
				setEntityValue: function (form, i, entity, ip) {
					jq(ip).attr("nodeid", entity[nf.NODEID]);
					if (entity[nf.LOGO] && entity[nf.LOGO].length > 0) {
						// show assigned image
						var btn = document.createElement("input"), img = jq(ip).find("img");
						jq(img).attr("src", "company/logo/" + entity[nf.LOGO])
							.attr("alt", entity[nf.NAME])
							.attr("height", "40px")
							.css("width", "")
							.css("height", "");
						// add 'remove image' button:
						jq(btn).appendTo(ip)
							.attr("type", "button")
							.css("color", "red")
							.attr("value", "✗")
							.attr("title", i18n.g("BSDD_REMOVE"))
							.addClass("std-button")
							.click(function () {
								entity[nf.LOGO] = null;
								jq(img).attr("src", "company/logo/nd.png");
							});
					}
				}
			},
			{
				label: "CLAZZ_CONTRIBUTOR",
				type: "custom",
				field: nf.WORKS_FOR,
				createField: function (form, i, ip) {
					d.relationFormField(form, i, ip, false, d.Contributor.title, d.RelationTypeEnum.WORKS_FOR, nf.WORKS_FOR, "Contributor", "IN");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.WORKS_FOR, "Contributor", "IN");
				}
			},
			{
				label: "COMPANY",
				type: "custom",
				field: nf.COMPANY_COMPANY,
				createField: function (form, i, ip) {
					d.relationFormField(form, i, ip, false, d.Company.title, d.RelationTypeEnum.COMPANY_COMPANY, nf.COMPANY_COMPANY, "Company", "BOTH");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.COMPANY_COMPANY, "Company", "BOTH");
				}
			}
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByCodeAndName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.WORKS_FOR], "IN")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					if (!fmr(node[nf.COMPANY_COMPANY], "BOTH")) {
						return false;
					}
					return true;
				}
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.edit = true;
		self.table.add = true;
		self.table.idCol = nf.NODEID;
		self.table.cols = [{
			label: "CODE",
			field: nf.CODE,
			type: "text",
			maxlen: 32,
			sort: true
		},
		{
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row),
					name = d.Company.getNameOf(v, td);
				jq(td).html(net.spectroom.js.shrinkText(name, 32, td));
				d.setFreebimItemClasses(v, td);
			},
			cl: "freebim-item Company",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.Company.getNameOf(v);
			},
			sort: true
		}, {
			label: "LOGO",
			field: nf.LOGO,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var entity = self.table.dataCallback(row), img = document.createElement("img");
				jq(img).appendTo(td)
					.attr("src", "company/logo/" + entity[nf.LOGO])
					.attr("alt", entity[nf.NAME])
					.attr("height", "40px")
					.css("width", "")
					.css("height", "");
			},
			sort: false
		},
		{
			label: "URL",
			field: nf.URL,
			type: "text",
			maxlen: 32,
			sort: true
		},
		{
			label: "CLAZZ_CONTRIBUTOR",
			field: nf.WORKS_FOR,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.WORKS_FOR, "IN", "Contributor");
			},
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.WORKS_FOR, "Contributor");
				}
				return 0;
			}
		}];
		self.sortfield = nf.NAME;
		self.base.postInit(self, true);
		self.table.initialSort = 2;
	},
	getAbbrOf: function (entity, elem) {
		return self.getNameOf(entity, elem);
	},
	getNameOf: function (entity, elem) {
		if (entity) {
			return entity[at.freebim.db.domain.NodeFields.NAME];
		} else {
			return "";
		}
	}

}; // at.freebim.db.domain.Company

at.freebim.db.domain.MessageNode = {
	className: "MessageNode",
	i18n: "CLAZZ_MESSAGENODE",
	title: net.spectroom.js.i18n.g("CLAZZ_MESSAGENODE"), // "MessageNode",
	arr: null,

	init: function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, f = d.filters, self = d.MessageNode, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.USERNAME, d.RelationTypeEnum.MESSAGE_SEEN, "MessageSeen", "OUT"),
		rf.make(nf.LASTNAME, d.RelationTypeEnum.MESSAGE_CLOSED, "MessageClosed", "OUT")];
		self.base = Object.create(d.BaseNode);
		self.base.init(self, "messages");
		self.canDelete = function (entity) {
			return true;
		};
		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo, {
				label: "TITLE",
				type: "text",
				field: nf.TITLE
			}, {
				label: "MESSAGE",
				type: "markdown",
				field: nf.NAME
			}, {
				label: "MESSAGE_INTL",
				type: "markdown",
				field: nf.NAME_EN
			}, {
				label: "MESSAGE_TYPE",
				type: "select",
				field: nf.TYPE,
				values: ["HAPPY", "INFO", "WARNING"],
				dataValue: function (v) { return v; },
				data: function (v) { return v; }
			}, {
				label: "SHOW_FROM",
				type: "datetime",
				field: nf.SHOW_FROM,
				callBack: function (v) {
					return ((v) ? v * 1 + at.freebim.db.delta : new Date().getTime());
				},
				value: function (v) {
					v = ((v) ? v * 1 : new Date().getTime())
					return v - at.freebim.db.delta;
				}
			}, {
				label: "SHOW_UNTIL",
				type: "datetime",
				field: nf.SHOW_UNTIL,
				callBack: function (v) {
					return ((v) ? v * 1 + at.freebim.db.delta : new Date().getTime());
				},
				value: function (v) {
					v = ((v) ? v * 1 : new Date().getTime())
					return v - at.freebim.db.delta;
				}
			}, {
				label: "PERMISSIONS", // "Berechtigungen",
				type: "checkboxgroup",
				field: nf.ROLES,
				values: [{
					label: "PERMISSION_ANONYMOUS",
					value: "ROLE_ANONYMOUS"
				}, {
					label: "PERMISSION_GUEST", // "Gast",
					value: "ROLE_GUEST"
				}, {
					label: "PERMISSION_EDIT", // "Schreibrecht",
					value: "ROLE_EDIT"
				}, {
					label: "PERMISSION_USERMANAGER", // "Benutzerverwalter",
					value: "ROLE_USERMANAGER"
				}, {
					label: "PERMISSION_WS_READ", // "Webservice (lesen)",
					value: "ROLE_WEBSERVICE_READ"
				}, {
					label: "PERMISSION_WS_WRITE", // "Webservice (schreiben)",
					value: "ROLE_WEBSERVICE_WRITE"
				}, {
					label: "PERMISSION_ADMINISTRATOR", // "Administrator",
					value: "ROLE_ADMIN"
				}]
			}
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.filters = [
			{
				add: f.text,
				f: function (node, filter, opt) {
					if (opt) {
						return (!node || node[nf.NAME] != filter);
					}
					filter = ((filter.toLowerCase) ? filter.toLowerCase() : filter);
					var src = ((node) ? ((node[nf.NAME]) ? node[nf.NAME] : "") : "");
					if (src && src.toLowerCase) {
						src = src.toLowerCase();
						return (src.indexOf(filter) < 0);
					}
				}
			}];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.edit = true;
		self.table.add = true;
		self.table.idCol = nf.ID;
		self.table.cols = [
			{
				label: "TITLE",
				type: "custom",
				field: nf.TITLE,
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
					td.title = "";
					jq(td).tooltip({
						open: function (event, ui) {
							// remove all other tooltips!
							jq(ui.tooltip).siblings(".ui-tooltip").remove();
						},
						content: function (callback) {
							callback(v[nf.NAME]);
						}
					});
				},
				cl: "freebim-item MessageNode",
				sort: true
			}, {
				label: "SHOW_FROM",
				field: nf.SHOW_FROM,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row),
						txt = "";
					v = ((v && v[nf.SHOW_FROM]) ? v[nf.SHOW_FROM] : at.freebim.db.time.now() - at.freebim.db.delta)
					txt = at.freebim.db.time.formatISO(v);
					jq(td).append(txt);
				},
				sort: true
			}, {
				label: "SHOW_UNTIL",
				field: nf.SHOW_UNTIL,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row),
						txt = "";
					v = ((v && v[nf.SHOW_UNTIL]) ? v[nf.SHOW_UNTIL] : at.freebim.db.time.now() - at.freebim.db.delta)
					txt = at.freebim.db.time.formatISO(v);
					jq(td).append(txt);
				},
				sort: true
			},
			{
				label: "MESSAGE_SEEN",
				field: nf.USERNAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.USERNAME, "OUT", "FreebimUser");
				},
				cl: "FreebimUser",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.USERNAME, "FreebimUser");
					}
					return 0;
				},
				csv: false
			},
			{
				label: "MESSAGE_READ",
				field: nf.LASTNAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.LASTNAME, "OUT", "FreebimUser");
				},
				cl: "FreebimUser",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.LASTNAME, "FreebimUser");
					}
					return 0;
				},
				csv: false
			},
			{
				label: "PERMISSION_ANONYMOUS",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_ANONYMOUS") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_GUEST",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_GUEST") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_ADMIN", // "Admin",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_ADMIN") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_EDIT",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_EDIT") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_USERMANAGER",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_USERMANAGER") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_WS_READ",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_WEBSERVICE_READ") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_WS_WRITE",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_WEBSERVICE_WRITE") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			}
		];

		self.sortfield = nf.NAME;
		self.base.postInit(self, true);
	},
	getAbbrOf: function (entity, elem) {
		return self.getNameOf(entity, elem);
	},
	getNameOf: function (entity, elem) {
		if (entity) {
			return entity[at.freebim.db.domain.NodeFields.TITLE];
		} else {
			return "";
		}
	}

}; // at.freebim.db.domain.MessageNode

at.freebim.db.domain.FreebimUser = {
	className: "FreebimUser",
	i18n: "CLAZZ_USER",
	title: net.spectroom.js.i18n.g("CLAZZ_USER"), // "Benutzer",
	arr: null,

	init: function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, f = d.filters, self = d.FreebimUser, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [];
		self.base = Object.create(d.BaseNode);
		self.base.init(self, "user");

		d.Contributor.init();

		self.form = Object.create(net.spectroom.js.form);
		self.form.afterEntitySet = function (entity) {
			self.form.pwdChanged = false;
		};
		self.form.beforeSave = function (entity, okFn) {
			if (self.form.pwdChanged) {
				var dlg = net.spectroom.js.newDiv();
				jq(dlg).dialog({
					title: i18n.g("DLG_TITLE_FREEBIM_PWD_REP"), // "freeBIM - Passwort wiederholen",
					modal: true,
					open: function () {
						jq(dlg).append("<table><tbody><tr><td><label for='pwd'>" + i18n.get("DLG_PASSWORD") + ":</label></td><td><input class='pwd' name='pwd' type='password' /></td></tr></tbody>");
					},
					buttons: [{
						text: i18n.getButton("DLG_BTN_CANCEL"), // "Abbrechen",
						click: function () {
							jq(dlg).dialog("close");
							okFn(false);
						}
					}, {
						text: i18n.getButton("DLG_BTN_OK"), // "Ok",
						click: function (form, suffix) {

							var pwd = jq("#" + dlg.id + " .pwd").val();
							if (pwd == entity[nf.PASSWORD]) {
								okFn(true);
							} else {
								jq(document).trigger("alert", [{
									title: "DLG_TITLE_FREEBIM_ERROR",
									content: i18n.get("PWD_REP_ERROR"),
									okFn: function (val) { okFn(false); }
								}]);
							}
							jq(dlg).dialog("close");
						}
					}]
				}).prev().attr("i18n_dlg", "DLG_TITLE_FREEBIM_PWD_REP");
			} else {
				okFn(true);
			}
		};
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo, {
				label: "DLG_USERNAME", // "Benutzername",
				type: "text",
				field: nf.USERNAME
			}, {
				label: "DLG_PASSWORD", // "Passwort",
				type: "password",
				field: nf.PASSWORD,
				onChange: function () {
					if (!self.form.entity[nf.NODEID]) {
						var val = jq(this).val();
						if (val && val.length >= 32) {
							alert("Passwort zu lang, max. 31 Zeichen.");
						}
					}
					self.form.pwdChanged = true;
				}
			}, {
				label: "ASSIGNED_CONTRIBUTOR", // "zugeordneter Bearbeiter",
				type: "select",
				field: nf.CONTRIBUTOR_ID,
				callBack: function (v) { return v; }, // v is nodeId of contributor
				values: d.Contributor.getNodes, 	/* all nodes of type Contributor */
				data: d.Contributor.getName, 		/* what's shown in the option's text */
				dataValue: d.Contributor.getId, 	/* the option's value */
				value: function (v) { return v; }
			}, {
				label: "PERMISSIONS", // "Berechtigungen",
				type: "checkboxgroup",
				field: nf.ROLES,
				values: [{
					label: "PERMISSION_GUEST", // "Gast",
					value: "ROLE_GUEST"
				}, {
					label: "PERMISSION_EDIT", // "Schreibrecht",
					value: "ROLE_EDIT"
				}, {
					label: "PERMISSION_USERMANAGER", // "Benutzerverwalter",
					value: "ROLE_USERMANAGER"
				}, {
					label: "PERMISSION_WS_READ", // "Webservice (lesen)",
					value: "ROLE_WEBSERVICE_READ"
				}, {
					label: "PERMISSION_WS_WRITE", // "Webservice (schreiben)",
					value: "ROLE_WEBSERVICE_WRITE"
				}, {
					label: "PERMISSION_ADMINISTRATOR", // "Administrator",
					value: "ROLE_ADMIN"
				}]
			}
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.filters = [
			{
				add: f.text,
				f: function (node, filter, opt) {
					if (opt) {
						return (!node || node[nf.USERNAME] != filter);
					}
					filter = ((filter.toLowerCase) ? filter.toLowerCase() : filter);
					var src = ((node) ? ((node[nf.USERNAME]) ? node[nf.USERNAME] : "") : "");
					if (src && src.toLowerCase) {
						src = src.toLowerCase();
						return (src.indexOf(filter) < 0);
					}
				}
			}];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.edit = true;
		self.table.add = true;
		self.table.idCol = nf.NODEID;
		self.table.cols = [
			{
				label: "DLG_USERNAME",
				field: nf.USERNAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					d.renderNode(v, td);
				},
				cl: "freebim-item FreebimUser",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.FreebimUser.getNameOf(v);
				},
				sort: true
			},
			{
				label: "PERMISSION_GUEST",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_GUEST") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_ADMIN", // "Admin",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_ADMIN") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_EDIT",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_EDIT") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_USERMANAGER",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_USERMANAGER") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_WS_READ",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_WEBSERVICE_READ") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "PERMISSION_WS_WRITE",
				field: nf.ROLES,
				type: "callback",
				data: function (v, td) {
					return ((v.indexOf("ROLE_WEBSERVICE_WRITE") >= 0) ? "✔︎" : " ");
				},
				sort: true,
				cl: "permission"
			},
			{
				label: "CLAZZ_CONTRIBUTOR", // "Bearbeiter",
				field: nf.CONTRIBUTOR_ID,
				type: "callback",
				data: d.Contributor.tdWithAbbr,
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Contributor.getAbbrOf(d.get(v[nf.CONTRIBUTOR_ID]));
				},
				cl: "freebim-item Contributor"
			}];

		self.sortfield = nf.USERNAME;
		self.base.postInit(self, true);
	},
	getAbbrOf: function (entity, elem) {
		return self.getNameOf(entity, elem);
	},
	getNameOf: function (entity, elem) {
		if (entity) {
			return entity[at.freebim.db.domain.NodeFields.USERNAME];
		} else {
			return "";
		}
	}

}; // at.freebim.db.domain.FreebimUser

at.freebim.db.domain.Measure = {
	className: "Measure",
	i18n: "CLAZZ_MEASURE",
	title: net.spectroom.js.i18n.g("CLAZZ_MEASURE"), // "Bemessung",
	arr: null,
	isContributed: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.Measure, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.UNIT, d.RelationTypeEnum.OF_UNIT, "OfUnit", "OUT"),
		rf.make(nf.DATATYPE, d.RelationTypeEnum.OF_DATATYPE, "OfDataType", "OUT"),
		rf.make(nf.HAS_VALUE, d.RelationTypeEnum.HAS_VALUE, "HasValue", "OUT"),
		rf.make(nf.PARAMS, d.RelationTypeEnum.HAS_MEASURE, "HasMeasure", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "measure");

		d.Unit.init();
		d.DataType.init();
		d.ValueList.init();
		d.Parameter.init();

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn, {
				label: "PREFIX",
				type: "text",
				field: nf.PREFIX
			}, {
				label: "CLAZZ_UNIT",
				type: "select",
				field: nf.UNIT,
				callBack: function (val) { // val is an array of OfUnit relations
					if (val && jq.isArray(val)) {
						var i, n = val.length, e;
						for (i = 0; i < n; i++) {
							e = d.get(val[i][rf.TO_NODE]);
							if (db.time.validNode(e)) {
								return val[i][rf.TO_NODE];
							}
						}
					}
				},
				cl: "Unit",
				values: d.Unit.getNodes,
				data: d.Unit.getName,
				dataValue: d.Unit.getId,
				value: function (nodeId, val) {
					if (val && jq.isArray(val) && val.length > 0) {
						val[0][rf.TO_NODE] = nodeId;
						return val;
					} else {
						// create a new OfUnit relation
						var rel = {};
						rel[rf.TYPE] = d.RelationTypeEnum.OF_UNIT;
						rel[rf.FROM_NODE] = self.form.entity[nf.NODEID];
						rel[rf.TO_NODE] = nodeId;
						val = [rel];
						return val;
					}
				}
			}, {
				label: "CLAZZ_DATATYPE",
				type: "select",
				field: nf.DATATYPE,
				callBack: function (val) { // val is an array of OfDataType relations
					if (val && jq.isArray(val)) {
						var i, n = val.length, e;
						for (i = 0; i < n; i++) {
							e = d.get(val[i][rf.TO_NODE]);
							if (db.time.validNode(e)) {
								return val[i][rf.TO_NODE];
							}
						}
					}
				},
				cl: "DataType",
				values: d.DataType.getNodes,
				data: d.DataType.getName,
				dataValue: d.DataType.getId,
				value: function (nodeId, val) {
					if (val && jq.isArray(val) && val.length > 0) {
						val[0][rf.TO_NODE] = nodeId;
						return val;
					} else {
						// create a new OfDataType relation
						var rel = {};
						rel[rf.TYPE] = d.RelationTypeEnum.OF_DATATYPE;
						rel[rf.FROM_NODE] = self.form.entity[nf.NODEID];
						rel[rf.TO_NODE] = nodeId;
						val = [rel];
						return val;
					}
				}
			}, {
				label: "CLAZZ_VALUELIST",
				type: "custom",
				field: nf.HAS_VALUE,
				createField: function (form, i, ip) {
					var orderable = false,
						relAdded = function (rel, okFn) {
							// rel is the just created relation
							d.rel.HasValue.init();
							jq(d.rel.HasValue).trigger("editComponent", [{ rel: rel, okFn: okFn }]);
						};
					d.relationFormField(form, i, ip, orderable, d.ValueList.title, d.RelationTypeEnum.HAS_VALUE, nf.HAS_VALUE, "ValueList", "OUT", relAdded);
				},
				setEntityValue: function (form, i, entity, ip) {
					var relClicked = function (rel) {
						var okFn = function () {
							form.setEntityValue(i, entity, ip);
						};
						d.rel.HasValue.init();
						jq(d.rel.HasValue).trigger("editComponent", [{ rel: rel, okFn: okFn }]);

					}, relInfo = function (el, rel) {
						var okFn = function (c) {
							var sub = document.createElement("sub");
							jq(sub).attr("maxLength", 20).appendTo(el);
							d.renderNode(c, sub);
							jq(sub).removeClass("freebim-contextmenu");
						};
						d.rel.HasValue.init();
						jq(d.rel.HasValue).trigger("componentFromRel", [{ rel: rel, okFn: okFn }]);
					};
					d.setEntityValue(form.clazz, i, ip, entity, nf.HAS_VALUE, "ValueList", "OUT", relInfo, relClicked);
				}
			},
			d.fields.docs,
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					var fmr = f.filterMarkedRelated;
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.UNIT], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.DATATYPE], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.HAS_VALUE], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.DOCUMENTED], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.PARAMS], "IN")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "prefix" + self.table.csvDelim
			+ "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim
			+ "bsDD-GUID" + self.table.csvDelim;
		self.table.cols = [{
			label: "PREFIX",
			field: nf.PREFIX,
			type: "text",
			maxlen: 32,
			sort: true
		}, {
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row);
				jq(td).attr("maxLength", "32");
				d.renderNode(v, td);
			},
			cl: "freebim-item Measure",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.Measure.getNameOf(v);
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
			}
		}, {
			label: "DESCRIPTION",
			field: nf.DESC,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var nodeId = self.arr[row],
					node = d.get(nodeId),
					desc = d.Measure.getDescOf(node);
				jq(td).html(net.spectroom.js.shrinkText(desc, 64, td)).addClass("freebim-desc").attr("nodeid", node[nf.NODEID]);
			},
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.Measure.getDescOf(v);
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]) + d.csv(v[nf.BSDD_GUID]);
			}
		},
		{
			label: "CLAZZ_UNIT",
			field: nf.UNIT,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row); // v is a Measure
				jq(td).empty();
				if (v && v[nf.UNIT] && v[nf.UNIT].length > 0) { // v[nf.UNIT] is an array of OfUnit relations
					self.fetchEntries("div", v, nf.UNIT, "Unit", "OUT", function (unit) {
						jq(td).empty();
						var j;
						for (j = 0; j < unit.length; j++) {
							td.appendChild(unit[j]);
						}
						jq(self.table).trigger("cellChanged", [{}]);
					});
				}
			},
			sort: false,
			csv: false
		},
		{
			label: "CLAZZ_DATATYPE",
			field: nf.DATATYPE,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row); // v is a Measure
				jq(td).empty();
				if (v && v[nf.DATATYPE] && v[nf.DATATYPE].length > 0) { // v[nf.DATATYPE] is an array of OfDataType relations
					self.fetchEntries("div", v, nf.DATATYPE, "DataType", "OUT", function (dataType) {
						jq(td).empty();
						var j;
						for (j = 0; j < dataType.length; j++) {
							td.appendChild(dataType[j]);
						}
						jq(self.table).trigger("cellChanged", [{}]);
					});
				}
			},
			sort: false,
			csv: false
		},
		{
			label: "CLAZZ_VALUELIST",
			field: nf.HAS_VALUE,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.HAS_VALUE, "IN", "ValueList");
			},
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.HAS_VALUE, "ValueList");
				}
				return 0;
			},
			csv: false
		},
		{
			label: "CLAZZ_PARAMETER",
			field: nf.PARAMS,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.PARAMS, "IN", "Parameter");
			},
			cl: "Parameter",
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.PARAMS, "Parameter");
				}
				return 0;
			},
			csv: false
		}
		];
		self.base.postInit(self);
		self.table.initialSort = 2;
	}
}; // at.freebim.db.domain.Measure 

at.freebim.db.domain.DataType = {
	className: "DataType",
	i18n: "CLAZZ_DATATYPE",
	title: net.spectroom.js.i18n.g("CLAZZ_DATATYPE"), // "Datentyp",
	arr: null,
	isContributed: true,
	aux: true,

	init: function () {
		var d = at.freebim.db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.DataType;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.MEASURES, d.RelationTypeEnum.OF_DATATYPE, "OfDataType", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "datatype");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			{
				label: "REG_EXPR",
				type: "text",
				field: nf.REGEXPR
			},
			d.fields.docs,
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim;
		self.table.cols = [{
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row);
				jq(td).attr("maxLength", "32");
				d.renderNode(v, td);
			},
			cl: "freebim-item DataType",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return v[nf.NAME];
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
			}
		}, {
			label: "DESCRIPTION",
			field: nf.DESC,
			type: "text",
			maxlen: 32,
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
			}
		}, {
			label: "REG_EXPR",
			field: nf.REGEXPR,
			type: "text",
			sort: true,
			csv: false
		}, {
			label: "REFERENCED",
			field: nf.MEASURES,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.MEASURES, "IN", "Measure");
			},
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.MEASURES, "Measure");
				}
				return 0;
			},
			csv: false
		}];
		self.base.postInit(self, true);

	}
}; // DataType

at.freebim.db.domain.Discipline = {
	className: "Discipline",
	i18n: "CLAZZ_DISCIPLINE",
	title: net.spectroom.js.i18n.g("CLAZZ_DISCIPLINE"), // "Disziplin",
	arr: null,
	isContributed: true,
	aux: true,

	init: function () {
		var d = at.freebim.db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.Discipline;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.PARAMETER, d.RelationTypeEnum.OF_DISCIPLINE, "OfDiscipline", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "discipline");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			d.fields.docs,
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "code" + self.table.csvDelim
			+ "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim;
		self.table.cols = [
			d.columns.code, {
				label: "NAME",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row)
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
				},
				cl: "freebim-item Discipline",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return v[nf.NAME];
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
				}
			}, {
				label: "DESCRIPTION",
				field: nf.DESC,
				type: "text",
				maxlen: 32,
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
				}
			}, {
				label: "REFERENCED",
				field: nf.PARAMETER,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.PARAMETER, "IN", "Parameter");
				},
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.PARAMETER, "Parameter");
					}
					return 0;
				},
				csv: false
			}];
		self.base.postInit(self, true);

	}
}; // at.freebim.db.domain.Discipline

at.freebim.db.domain.Document = {
	className: "Document",
	i18n: "CLAZZ_DOCUMENT",
	title: net.spectroom.js.i18n.g("CLAZZ_DOCUMENT"), // "Dokument",
	arr: null,
	aux: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.Document;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.NODES, d.RelationTypeEnum.DOCUMENTED_IN, "DocumentedIn", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "document");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.desc,
			d.fields.descEn,
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!f.filterMarkedRelated(node[nf.NODES], "IN")) {
						return false;
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];

		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim;
		self.table.cols = [{
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			cl: "freebim-item Document",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row); // v is a Document
				if (v) {
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
					var name = v[nf.NAME];
					if (name != null && name.indexOf("http") == 0) {
						// it seems to be a link ...
						var a = document.createElement("a");
						a.href = v[nf.NAME];
						a.target = "_blank";
						jq(td).wrap(a);
					}
				}
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]);
			}
		}, {
			label: "DESCRIPTION",
			field: nf.DESC,
			type: "text",
			maxlen: 32,
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
			}
		},
		{
			label: "REFERENCED",
			field: nf.NODES,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.NODES, "IN", undefined);
			},
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row); // v is a Document
				if (v) {
					return self.relevantRelCount(v, nf.NODES, undefined);
				} else {
					return 0;
				}
			},
			csv: false
		}];
		self.base.postInit(self, true);

	}
}; // at.freebim.db.domain.Document

at.freebim.db.domain.Library = {
	className: "Library",
	i18n: "CLAZZ_LIBRARY",
	title: net.spectroom.js.i18n.g("CLAZZ_LIBRARY"), // "Bibliothek",
	arr: null,
	isContributed: true,
	isHierarchical: true,

	getNameOf: function (entity, elem) {
		var nf = at.freebim.db.domain.NodeFields;
		if (entity) {
			if (elem) {
				elem.title = ((entity[nf.DESC]) ? entity[nf.DESC] : "");
			}
			return ((entity[nf.NAME]) ? entity[nf.NAME] : "");
		}
		return "";
	},
	getDescOf: function (entity) {
		var nf = at.freebim.db.domain.NodeFields;
		return ((entity && entity[nf.DESC]) ? entity[nf.DESC] : "");
	},

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.Library;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.RESPONSIBLE, d.RelationTypeEnum.RESPONSIBLE, "Responsible", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "libraries");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name, {
				label: "URL",
				type: "text",
				field: nf.URL
			},
			d.fields.desc, {
				label: "IFD_LANGUAGE",
				type: "text",
				field: nf.LANGUAGE
			}, {
				label: "RESPONSIBLE_CONTRIBUTORS",
				type: "custom",
				field: nf.RESPONSIBLE,
				createField: function (form, i, ip) {
					var orderable = false;
					d.relationFormField(form, i, ip, orderable, d.Contributor.title, d.RelationTypeEnum.RESPONSIBLE, nf.RESPONSIBLE, "Contributor", "IN");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.RESPONSIBLE, "Contributor", "IN");
				}
			}, {
				label: "ASSIGNED_COMPONENTS",
				type: "custom",
				field: nf.CHILDS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.PARENT_OF, nf.CHILDS, "Component", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.CHILDS, "Component", "OUT");
				}
			},
			{
				label: "ASSIGNED_TO",
				type: "custom",
				field: nf.PARENTS,
				createField: function (form, i, ip) {

				},
				setEntityValue: function (form, i, entity, ip) {
					var bbnId = d.bbnId, div = document.createElement("div");
					jq(ip).append(div);
					if (entity) {
						if (entity[nf.PARENTS] && entity[nf.PARENTS].length > 0) {
							bbnId = entity[nf.PARENTS][0][rf.FROM_NODE];
						} else {
							var rel = {};
							rel[rf.TYPE] = d.RelationTypeEnum.PARENT_OF;
							rel[rf.FROM_NODE] = d.bbnId;
							rel[rf.TO_NODE] = entity[nf.NODEID];
							entity[nf.PARENTS] = [rel];
						}
						d.getOrLoad(bbnId, d.BigBangNode.className, function (bbn) {
							d.renderNode(bbn, div);
						});
					}
				}
			},
			d.fields.docs
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name" + self.table.csvDelim
			+ "description" + self.table.csvDelim
			+ "URL" + self.table.csvDelim
			+ "language" + self.table.csvDelim;
		self.table.cols = [
			{
				label: "NAME",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", 32);
					d.renderNode(v, td);
				},
				cl: "freebim-item Library",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Library.getNameOf(v);
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]);
				}
			},
			{
				label: "DESCRIPTION",
				field: nf.DESC,
				type: "text",
				maxlen: 32,
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.DESC]);
				}
			},
			{
				label: "URL",
				field: nf.URL,
				type: "text",
				maxlen: 32,
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.URL]);
				}
			}, {
				label: "IFD_LANGUAGE",
				field: nf.LANGUAGE,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row),
						lc;
					if (v && v[nf.LANGUAGE]) {
						lc = v[nf.LANGUAGE].substr(v[nf.LANGUAGE].length - 2, 2).toUpperCase();
						jq(td).html("<img title='" + v[nf.LANGUAGE] + "' alt='" + v[nf.LANGUAGE] + "' width='16px' src='/resources/flags/64/" + lc + ".png'>");
					}
				},
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.LANGUAGE]);
				}
			},
			{
				label: "UPDATED",
				field: nf.TIMESTAMP,
				type: "callback",
				data: function (v, td) {
					var date = db.time.formatISO(v);
					return date;
				},
				sort: true,
				csv: false
			}];
		self.base.postInit(self, true);
	}
}; // at.freebim.db.domain.Library

at.freebim.db.domain.Phase = {
	className: "Phase",
	i18n: "CLAZZ_PHASE",
	title: net.spectroom.js.i18n.g("CLAZZ_PHASE"), // "Projektphase",
	arr: null,
	isContributed: true,
	aux: true,

	init: function () {
		var d = at.freebim.db.domain, nf = d.NodeFields, self = d.Phase;
		if (self.initialized) {
			return;
		}
		self.relations = [];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "phase");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code, {
				label: "COLOR_VALUE",
				type: "text",
				field: nf.HEX_COLOR
			},
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			d.fields.docs,
			d.fields.bsDD
		];

		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.add = true;
		self.table.edit = true;
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "code" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim
			+ "color" + self.table.csvDelim;
		self.table.cols = [{
			label: "CODE",
			field: nf.CODE,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row);
				jq(td).attr("abbr", "1");
				d.renderNode(v, td);
			},
			cl: "freebim-item Phase",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return v[nf.CODE];
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.CODE]);
			}
		}, {
			label: "NAME",
			field: nf.NAME,
			type: "text",
			maxlen: 32,
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
			}
		}, {
			label: "DESCRIPTION",
			field: nf.DESC,
			type: "text",
			maxlen: 32,
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
			}
		}, {
			label: "COLOR_VALUE",
			field: nf.HEX_COLOR,
			type: "callback",
			data: function (v, tr, td, row, col) {
				jq(td).css("background-color", "#" + v);
				return v;
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.HEX_COLOR]);
			}
		}];
		self.sortfield = nf.CODE;
		self.base.postInit(self, true);
	}
}; // at.freebim.db.domain.Phase

at.freebim.db.domain.Unit = {
	className: "Unit",
	i18n: "CLAZZ_UNIT",
	title: net.spectroom.js.i18n.g("CLAZZ_UNIT"), // "Einheit",
	arr: null,
	isContributed: true,
	aux: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, self = d.Unit, f = d.filters;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.UNIT_CONVERSION, d.RelationTypeEnum.UNIT_CONVERSION, "UnitConversion", "BOTH"),
		rf.make(nf.MEASURES, d.RelationTypeEnum.OF_UNIT, "OfUnit", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "unit");

		self.form = Object.create(net.spectroom.js.form);

		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo, {
				label: "CODE",
				type: "text",
				field: nf.CODE
			},
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn, {
				label: "CONVERSIONS",
				type: "custom",
				field: nf.UNIT_CONVERSION,
				createField: function (form, i, ip) {
					var orderable = false;
					relAdded = function (rel, okFn) {
						// rel is the just created relation
						d.rel.UnitConversion.init();
						jq(d.rel.Equals).trigger("at.freebim.db.domain.rel.UnitConversion.edit", [{ rel: rel, okFn: okFn }]);
					};
					d.relationFormField(form, i, ip, orderable, self.title, d.RelationTypeEnum.UNIT_CONVERSION, nf.UNIT_CONVERSION, self.className, "BOTH", relAdded);
				},
				setEntityValue: function (form, i, entity, ip) {
					var relClicked = function (rel) {
						var okFn = function () {
							form.setEntityValue(i, entity, ip);
						};
						d.rel.UnitConversion.init();
						jq(d.rel.UnitConversion).trigger("at.freebim.db.domain.rel.UnitConversion.edit", [{ rel: rel, okFn: okFn }]);

					}, relInfo = function (el, rel) {
						var okFn = function (txt) {
							var sub = document.createElement("sub");
							sub.appendChild(document.createTextNode(" " + txt));
							jq(sub).appendTo(el);
						};
						d.rel.UnitConversion.init();
						jq(d.rel.UnitConversion).trigger("unitConversionRel", [{ rel: rel, okFn: okFn, unit: entity }]);
					};
					d.setEntityValue(form.clazz, i, ip, entity, nf.UNIT_CONVERSION, self.className, "BOTH", relInfo, relClicked);
				}
			},
			d.fields.docs,
			d.fields.bsDD
		];

		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);

		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByCodeAndName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node.m, "IN")) {
						return false;
					}
					if (!fmr(node[nf.UNIT_CONVERSION], "BOTH")) {
						return false;
					}
					return true;
				}
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.csv = "code" + self.table.csvDelim
			+ "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "bsDD-GUID" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim;
		self.table.idCol = nf.NODEID;
		self.table.cols = [
			d.columns.code, {
				label: "NAME",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", 32)
						.draggable(db.tree.dragOptions)
						.droppable({
							accept: ".freebim-item.Unit",
							hoverClass: "drop-hover",
							tolerance: "pointer",
							drop: function (event, ui) {
								var srcId = jq(ui.draggable).attr("nodeid"), // id of dragged Unit
									targetId = jq(this).attr("nodeid"), // id of target Unit
									targetUnit = d.get(targetId), // create new UnitConversion relation
									r = {},
									okFn = function (rel) {
										d.addModifiedNode(srcId);
										d.addModifiedNode(targetId);
										if (!targetUnit[nf.UNIT_CONVERSION]) {
											targetUnit[nf.UNIT_CONVERSION] = [];
										}
										targetUnit[nf.UNIT_CONVERSION].push(rel);
										jq(document).trigger("_save", [{ entity: targetUnit }]);
									};
								r[rf.TYPE] = d.RelationTypeEnum.UNIT_CONVERSION;
								r[rf.ID] = null;
								r[rf.QUALITY] = 1;
								r[rf.FROM_NODE] = srcId;
								r[rf.TO_NODE] = targetId;
								d.rel.UnitConversion.init();
								jq(d.rel.UnitConversion).trigger("at.freebim.db.domain.rel.UnitConversion.edit", [{ rel: r, okFn: okFn }]);
							}
						});
					d.renderNode(v, td);
				},
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]) + d.csv(v[nf.BSDD_GUID]);
				},
				cl: "freebim-item Unit",
				sort: true
			}, {
				label: "DESCRIPTION",
				field: nf.DESC,
				type: "text",
				maxlen: 32,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
				},
				sort: true
			}, {
				label: "REFERENCED",
				field: nf.MEASURES,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.MEASURES, "IN", "Measure");
				},
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.MEASURES, "Measure");
					}
					return 0;
				},
				csv: false
			}, {
				label: "CONVERSIONS",
				field: nf.UNIT_CONVERSION,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.UNIT_CONVERSION, "BOTH", "Unit");
				},
				cl: "Unit",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row); // v is a Unit
					if (v) {
						return self.relevantRelCount(v, nf.UNIT_CONVERSION, "Unit");
					}
					return 0;
				},
				csv: false
			}];
		self.base.postInit(self, true);
	}

}; // at.freebim.db.domain.Unit

at.freebim.db.domain.ValueListEntry = {
	className: "ValueListEntry",
	i18n: "CLAZZ_VALUELISTENTRY",
	title: net.spectroom.js.i18n.g("CLAZZ_VALUELISTENTRY"), // "Wert",
	arr: null,
	isContributed: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields,
			f = d.filters, self = d.ValueListEntry;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.LISTS, d.RelationTypeEnum.HAS_ENTRY, "HasEntry", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "valuelistentry");

		d.ValueList.init();

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			d.fields.state,
			d.fields.statusComment, {
				label: "COMMENT",
				type: "text",
				field: nf.COMMENT
			},
			d.fields.docs,
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByName
			},
			{
				add: f.mark,
				f: function (node, filter, opt) {
					var fmr = f.filterMarkedRelated;
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.LISTS], "IN")) {
						return false;
					}
					if (!fmr(node[nf.DOCUMENTED], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];

		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim
			+ "comment" + self.table.csvDelim
			+ "status" + self.table.csvDelim;
		self.table.cols = [{
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row);
				jq(td).attr("maxLength", "32");
				d.renderNode(v, td);
			},
			cl: "freebim-item ValueListEntry",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.ValueListEntry.getNameOf(v);
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
			}
		}, {
			label: "DESCRIPTION",
			field: nf.DESC,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var nodeId = self.arr[row],
					node = d.get(nodeId),
					desc = d.ValueListEntry.getDescOf(node);
				jq(td).html(net.spectroom.js.shrinkText(desc, 64, td)).addClass("freebim-desc").attr("nodeid", node[nf.NODEID]);
			},
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.ValueListEntry.getDescOf(v);
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]);
			}
		}, {
			label: "COMMENT",
			field: nf.COMMENT,
			type: "text",
			maxlen: 32,
			sort: true
		},
		d.columns.state,
		{
			label: "TAB_VALUELISTS",
			field: nf.LISTS,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.LISTS, "IN", "ValueList");
			},
			cl: "ValueList",
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.LISTS, "ValueList");
				}
				return 0;
			},
			csv: false
		}];
		self.base.postInit(self);
	}

}; // at.freebim.db.domain.ValueListEntry

at.freebim.db.domain.ValueList = {
	className: "ValueList",
	i18n: "CLAZZ_VALUELIST",
	title: net.spectroom.js.i18n.g("CLAZZ_VALUELIST"), // "Werteliste",
	arr: null,
	isContributed: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.ValueList;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.HAS_ENTRY, d.RelationTypeEnum.HAS_ENTRY, "HasEntry", "OUT"),
		rf.make(nf.MEASURES, d.RelationTypeEnum.HAS_VALUE, "HasValue", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "valuelist");

		d.ValueListEntry.init();

		self.form = Object.create(net.spectroom.js.form);
		d.ValueList.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.nameEn,
			d.fields.bsDD,
			{
				label: "TAB_VALUELISTENTRIES",
				type: "custom",
				field: nf.HAS_ENTRY,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.ValueListEntry.title, d.RelationTypeEnum.HAS_ENTRY, nf.HAS_ENTRY, "ValueListEntry");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.HAS_ENTRY, "ValueListEntry", "OUT");
				}
			},
			d.fields.state,
			d.fields.statusComment,
			d.fields.docs
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByName
			}, {
				add: f.mark,
				f: function (node, filter, opt) {
					var fmr = f.filterMarkedRelated;
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.HAS_ENTRY], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.MEASURES], "IN")) {
						return false;
					}
					if (!fmr(node[nf.DOCUMENTED], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "status" + self.table.csvDelim
			+ "comment" + self.table.csvDelim;
		self.table.cols = [
			{
				label: "NAME",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
				},
				cl: "freebim-item ValueList",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.ValueList.getNameOf(v);
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
				}
			},
			{
				label: "TAB_VALUELISTENTRIES",
				field: nf.HAS_ENTRY,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.HAS_ENTRY, "OUT", "ValueListEntry");
				},
				cl: "ValueListEntry",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.HAS_ENTRY, "ValueListEntry");
					}
					return 0;
				},
				csv: false
			}, {
				label: "TAB_MEASURES",
				field: nf.MEASURES,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.MEASURES, "IN", "Measure");
				},
				cl: "Measure",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.MEASURES, "Measure");
					}
					return 0;
				},
				csv: false
			},
			d.columns.state,
			{
				label: "STATUS_COMMENT",
				field: nf.STATUS_COMMENT,
				type: "text",
				sort: false
			}
		];
		self.base.postInit(self);

	}
}; // at.freebim.db.domain.ValueList

at.freebim.db.domain.Parameter = {
	className: "Parameter",
	i18n: "CLAZZ_PARAMETER",
	title: net.spectroom.js.i18n.g("CLAZZ_PARAMETER"), // "Parameter",
	arr: null,
	isContributed: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.Parameter, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.DISCIPLINE, d.RelationTypeEnum.OF_DISCIPLINE, "OfDiscipline", "OUT"),
		rf.make(nf.HP, d.RelationTypeEnum.HAS_PARAMETER, "HasParameter", "IN"),
		rf.make(nf.PSETS, d.RelationTypeEnum.CONTAINS_PARAMETER, "ContainsParameter", "IN"),
		rf.make(nf.MEASURES, d.RelationTypeEnum.HAS_MEASURE, "HasMeasure", "OUT")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "param");

		d.Discipline.init();
		d.Measure.init();
		d.Component.init();

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.code,
			d.fields.name,
			d.fields.nameEn,
			d.fields.desc,
			d.fields.descEn,
			{
				label: "DEFAULT",
				type: "text",
				field: nf.DEFAULT
			},
			{
				label: "CLAZZ_DISCIPLINE",
				type: "custom",
				field: nf.DISCIPLINE,
				createField: function (form, i, ip) {
					var orderable = false;
					d.relationFormField(form, i, ip, orderable, d.Discipline.title, d.RelationTypeEnum.OF_DISCIPLINE, nf.DISCIPLINE, "Discipline");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.DISCIPLINE, "Discipline", "OUT");
				}
			},
			{
				label: "TAB_MEASURES",
				type: "custom",
				field: nf.MEASURES,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Measure.title, d.RelationTypeEnum.HAS_MEASURE, nf.MEASURES, "Measure");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.MEASURES, "Measure", "OUT");
				}
			},
			{
				label: "ASSIGNED_TO",
				type: "custom",
				field: nf.HP,
				createField: function (form, i, ip) {
					var orderable = false,
						relAdded = function (rel, okFn) {
							// rel is the just created relation
							d.rel.HasParameter.init();
							jq(d.rel.HasParameter).trigger("editPhase", [{ rel: rel, okFn: okFn }]);
						};
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.HAS_PARAMETER, nf.HP, "Component", "IN", relAdded);
				},
				setEntityValue: function (form, i, entity, ip) {
					var relClicked = function (rel) {
						var okFn = function () {
							form.setEntityValue(i, entity, ip);
						};
						d.rel.HasParameter.init();
						jq(d.rel.HasParameter).trigger("editPhase", [{ rel: rel, okFn: okFn }]);

					}, relInfo = function (el, rel) {
						var okFn = function (ph) {
							var sub = document.createElement("sub");
							jq(sub).attr("abbr", "1").appendTo(el);
							d.renderNode(ph, sub);
							jq(sub).removeClass("freebim-contextmenu");
						};
						d.rel.HasParameter.init();
						jq(d.rel.HasParameter).trigger("phaseFromRel", [{ rel: rel, okFn: okFn }]);
					};
					d.setEntityValue(form.clazz, i, ip, entity, nf.HP, "Component", "IN", relInfo, relClicked);
				}
			},
			{
				label: "IN_PARAMETER_SET",
				type: "custom",
				field: nf.PSETS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Measure.title, d.RelationTypeEnum.CONTAINS_PARAMETER, nf.PSETS, "ParameterSet", "IN");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.PSETS, "ParameterSet", "IN");
				}
			},
			{
				label: "CLAZZ_PARAMETERTYPE",
				type: "select",
				field: nf.PARAM_TYPE,
				values: d.ParameterType.values,
				data: d.ParameterType.data,
				dataValue: d.ParameterType.dataValue
			},
			d.fields.state,
			d.fields.statusComment,
			d.fields.docs,
			d.fields.bsDD
		];

		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByCodeAndName
			}, {
				add: f.mark,
				f: function (node, filter, opt) {
					var fmr = f.filterMarkedRelated, comps = node[nf.HP];
					if (db.isMarked(node[nf.NODEID])) {
						return false;
					}
					if (!fmr(node[nf.DISCIPLINE], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.MEASURES], "OUT")) {
						return false;
					}
					if (!fmr(comps, "IN")) {
						return false;
					}
					if (!fmr(node[nf.PSETS], "IN")) {
						return false;
					}
					if (!fmr(node[nf.DOCUMENTED], "OUT")) {
						return false;
					}
					if (!fmr(node[nf.EQUALS], "BOTH")) {
						return false;
					}
					if (comps && comps.length > 0) {
						d.rel.HasParameter.init();
						var ph, r, i, n = comps.length;
						for (i = 0; i < n; i++) {
							r = comps[i];
							ph = d.rel.HasParameter.phaseForUuid(r[rf.PHASE]);
							if (ph && db.isMarked(ph[nf.NODEID])) {
								return false;
							}
						}
					}
					return true;
				}
			},
			{
				add: f.lib,
				f: f.filterByLib
			},
			{
				add: f.bsdd,
				f: undefined
			},
			{
				add: f.bsddLoaded,
				f: undefined
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "code" + self.table.csvDelim
			+ "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "description-de" + self.table.csvDelim
			+ "description-en" + self.table.csvDelim
			+ "bsDD-GUID" + self.table.csvDelim
			+ "type" + self.table.csvDelim
			+ "status" + self.table.csvDelim;
		self.table.cols = [
			d.columns.code,
			{
				label: "NAME",
				field: nf.NAME,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row);
					jq(td).attr("maxLength", "32");
					d.renderNode(v, td);
				},
				cl: "freebim-item Parameter",
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Parameter.getNameOf(v);
				},
				sort: true,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]) + d.csv(v[nf.NAME_EN]);
				}
			},
			{
				label: "DESCRIPTION",
				field: nf.DESC,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var nodeId = self.arr[row],
						node = d.get(nodeId),
						desc = d.Parameter.getDescOf(node);
					jq(td).html(net.spectroom.js.shrinkText(desc, 32, td)).addClass("freebim-desc").attr("nodeid", node[nf.NODEID]);
				},
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					return d.Parameter.getDescOf(v);
				},
				sort: false,
				csv: function (row, col) {
					var v = self.table.dataCallback(row);
					return d.csv(v[nf.DESC]) + d.csv(v[nf.DESC_EN]) + d.csv(v[nf.BSDD_GUID]);
				}
			},
			{
				label: "CLAZZ_PARAMETERTYPE",
				field: nf.PARAM_TYPE,
				type: "callback",
				data: d.ParameterType.data,
				sort: true
			},
			{
				label: "CLAZZ_DISCIPLINE",
				field: nf.DISCIPLINE,
				type: "custom",
				createCell: function (tr, td, row, col) {
					var v = self.table.dataCallback(row); // v is a Parameter
					if (v && v[nf.DISCIPLINE]) { // v[nf.DISCIPLINE] is an array of OfDiscipline relations
						self.fetchEntries("div", v, nf.DISCIPLINE, "Discipline", "OUT", function (discipline) {
							jq(td).empty();
							var j;
							for (j = 0; j < discipline.length; j++) {
								td.appendChild(discipline[j]);
							}
						}, true);
					}
				},
				sort: true,
				csv: false
			},
			{
				label: "CLAZZ_MEASURE",
				field: nf.MEASURES,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.MEASURES, "OUT", "Measure");
				},
				cl: "Measure",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.MEASURES, "Measure");
					}
					return 0;
				},
				csv: false
			}, {
				label: "TAB_COMPONENTS",
				field: nf.HP,
				type: "custom",
				createCell: function (tr, td, row, col) {
					self.showRelations(td, row, nf.HP, "IN", "Component");
				},
				cl: "Component",
				sort: true,
				sortValue: function (row) {
					var v = self.table.dataCallback(row);
					if (v) {
						return self.relevantRelCount(v, nf.HP, "Component");
					}
					return 0;
				},
				csv: false
			},
			d.columns.state
		];
		self.base.postInit(self);
		self.table.initialSort = 2;
	}

};// at.freebim.db.domain.Parameter

at.freebim.db.domain.ParameterSet = {
	className: "ParameterSet",
	i18n: "CLAZZ_PARAMETERSET",
	title: net.spectroom.js.i18n.g("CLAZZ_PARAMETERSET"), // "Parameter-Set",
	isContributed: true,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, f = d.filters, self = d.ParameterSet;
		if (self.initialized) {
			return;
		}
		self.relations = [rf.make(nf.PARAMS, d.RelationTypeEnum.CONTAINS_PARAMETER, "ContainsParameter", "OUT"),
		rf.make(nf.OWNERS, d.RelationTypeEnum.HAS_PARAMETER_SET, "HasParameterSet", "IN")];

		self.base = Object.create(d.BaseNode);
		self.base.init(self, "pset");

		self.form = Object.create(net.spectroom.js.form);
		self.form.fields = [
			d.fields.nodeId,
			d.fields.validFrom,
			d.fields.validTo,
			d.fields.name,
			d.fields.nameEn, {
				label: "CLAZZ_PARAMETERSETTYPE",
				type: "select",
				field: nf.TYPE,
				values: d.ParameterSetType.values,
				data: d.ParameterSetType.data,
				dataValue: d.ParameterSetType.dataValue
			},
			d.fields.state,
			d.fields.statusComment,
			d.fields.docs, {
				label: "ASSIGNED_PARAMETERS",
				type: "custom",
				field: nf.PARAMS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Parameter.title, d.RelationTypeEnum.CONTAINS_PARAMETER, nf.PARAMS, "Parameter", "OUT");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.PARAMS, "Parameter", "OUT");
				}
			}, {
				label: "REFERENCED",
				type: "custom",
				field: nf.OWNERS,
				createField: function (form, i, ip) {
					var orderable = true;
					d.relationFormField(form, i, ip, orderable, d.Component.title, d.RelationTypeEnum.HAS_PARAMETER_SET, nf.OWNERS, "Component", "IN");
				},
				setEntityValue: function (form, i, entity, ip) {
					d.setEntityValue(form.clazz, i, ip, entity, nf.OWNERS, "Component", "IN");
				}
			},
			d.fields.bsDD
		];
		self.form.create(null, self, self);

		self.table = Object.create(net.spectroom.js.table);
		self.table.add = true;
		self.table.edit = true;
		self.table.filters = [
			{
				add: f.text,
				f: f.filterByName
			}
		];
		self.table.dataCallback = function (row) {
			return d.get(self.arr[row]);
		};
		self.table.idCol = nf.NODEID;
		self.table.csv = "freeBIM-ID" + self.table.csvDelim
			+ "name-de" + self.table.csvDelim
			+ "name-en" + self.table.csvDelim
			+ "bsDD-GUID" + self.table.csvDelim
			+ "type" + self.table.csvDelim
			+ "status" + self.table.csvDelim;
		self.table.cols = [{
			label: "NAME",
			field: nf.NAME,
			type: "custom",
			createCell: function (tr, td, row, col) {
				var v = self.table.dataCallback(row);
				jq(td).attr("maxLength", "32");
				d.renderNode(v, td);
			},
			cl: "freebim-item ParameterSet",
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				return d.ParameterSet.getNameOf(v);
			},
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.FREEBIM_ID]) + d.csv(v[nf.NAME]);
			}
		}, {
			label: "NAME_INTL",
			field: nf.NAME_EN,
			type: "text",
			maxlen: 32,
			sort: true,
			csv: function (row, col) {
				var v = self.table.dataCallback(row);
				return d.csv(v[nf.NAME_EN]) + d.csv(v[nf.BSDD_GUID]);
			}
		}, {
			label: "CLAZZ_PARAMETERSETTYPE",
			field: nf.TYPE,
			type: "callback",
			data: d.ParameterSetType.data,
			sort: true
		}, {
			label: "CLAZZ_PARAMETER",
			field: nf.PARAMS,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.PARAMS, "OUT", "Parameter");
			},
			cl: "Parameter",
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.PARAMS, "Parameter");
				}
				return 0;
			},
			csv: false
		}, {
			label: "TAB_COMPONENTS",
			field: nf.OWNERS,
			type: "custom",
			createCell: function (tr, td, row, col) {
				self.showRelations(td, row, nf.OWNERS, "IN", "Component");
			},
			sort: true,
			sortValue: function (row) {
				var v = self.table.dataCallback(row);
				if (v) {
					return self.relevantRelCount(v, nf.OWNERS, "Component");
				}
				return 0;
			},
			csv: false
		},
		d.columns.state
		];
		self.base.postInit(self);
	}
}; // at.freebim.db.domain.ParameterSet

jq(document).ready(function () {

	at.freebim.db.domain.init();

});