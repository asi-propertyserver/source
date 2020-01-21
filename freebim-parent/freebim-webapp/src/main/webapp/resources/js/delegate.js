/**
 * 
 */
jq(document).delegate("table.jsTable-data", "newInserted", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, node = data.entity;
	if (!node) {
		return;
	} 
	
	db.update.related(data);
	
	var cn = jq(this).attr("clazz"), clazz;
	if (cn) {
		db.logger.debug("delgate table.jsTable-data newInserted");
		clazz = d[cn];
		if (clazz) { 
			if (clazz.table && clazz.arr && node[nf.CLASS_NAME] == cn) {
				try {
					var idx = clazz.arr.indexOf(node[nf.NODEID]);
					if (idx < 0) {
						// not in array of nodes, add it.
						clazz.arr.push(node[nf.NODEID]);
						clazz.table.rowCount = clazz.arr.length;
						clazz.table.insertRow(clazz.table.rowCount - 1);
					} else {
						// already in array of nodes, update.
						jq("[nodeid]").trigger("newNodeModified", [{entity: entity, ts: data.ts}]);
//						jq("[nodeid='" + node[nf.NODEID] + "']").trigger("nodeModified", [node]);
//						jq(clazz).trigger("modified_" + node[nf.NODEID], data);
					}
				} catch (e) {
					db.logger.error("Error in delgate table.jsTable-data newInserted: " + e.message);
				}
			}
		}
	}
});
jq(document).delegate("div.Component.tree-node[nodeid]", "newInserted", function (event, data) {
	var db = at.freebim.db, node = data.entity;
	if (!node) {
		return;
	} 
	db.update.related(data);
});

jq(document).delegate("div.freebim-item[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.itemDiv(jq(this), data);
});
jq(document).delegate("li.freebim-item[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.itemDiv(jq(this), data);
});
jq(document).delegate("td.freebim-item[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.itemDiv(jq(this), data);
});

jq(document).delegate(".freebim-rel-current div.freebim-item[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.curRelation(jq(this), data);
});

jq(document).delegate("tr[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.tableRow(jq(this), data);
});

jq(document).delegate(".tree-node[nodeid]", "newNodeModified", function (event, data) {
	at.freebim.db.update.treeNode(jq(this), data);
});

at.freebim.db.update = {
		
		timer : null,
		
		performUpdate : function () {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields;
			if (db.update.timer) {
				clearTimeout(db.update.timer);
			}
			db.update.timer = setTimeout(function () {
				var i, n = d.modifiedNodes.length, node;
				for (i=0; i<n; i++) {
					node = d.get(d.modifiedNodes[i]);
					if (node) {
						db.logger.debug("at.freebim.db.performUpdate[" + node[nf.NODEID] + "]");
						jq(document).trigger("getNode", [{nodeId: node[nf.NODEID], cn: node[nf.CLASS_NAME]}]);
					}
				}
				d.modifiedNodes = [];
			}, 1000);
		},
		
		related : function (data) {
			var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, node = data.entity;
			if (node && data.ts != undefined) {
				var clazz = d[node[nf.CLASS_NAME]];
				if (clazz) {
					if (clazz.relations) {
						var now = db.time.now();
						// inserted node may have relations ...
						var i, j, m, r, rels, other, otherId, n = clazz.relations.length;
						for (i=0; i<n; i++) {
							rels = clazz.relations[i];
							if (node[rels[rf.FIELD_NAME]]) {
								m = node[rels[rf.FIELD_NAME]].length;
								for (j=0; j<m; j++) {
									r = node[rels[rf.FIELD_NAME]][j];
									otherId = ((r[rf.FROM_NODE] == node[nf.NODEID]) ? r[rf.TO_NODE] : r[rf.FROM_NODE]);
									other = d.get(otherId);
									if (other && other.updated < data.ts) {
										d.addModifiedNode(otherId);

										db.logger.debug("at.freebim.db.update.related[" + node[nf.NODEID] + " -> " + rels[rf.FIELD_NAME] + " -> " + otherId + "]");
										// fetch the other node to perform an update:
									}
								}
							}
						}
					}
					db.update.performUpdate();
				}
			}
		},
		
		itemDiv : function (x, data) {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, node = data.entity, nodeId = "" + node[nf.NODEID];
			var self = x[0], attr = x.attr("nodeid");
			if (attr == nodeId) {
				db.logger.debug("at.freebim.db.update.itemDiv[" + nodeId + "]");
				if (self.nodeName != "TR") {
					try { 
						// update div for that node:
						d.renderNode(node, self); 
					} catch (e) {
						db.logger.error("Error at.freebim.db.update.itemDiv[" + nodeId + "]: " + e.message);
					} 
				}
				db.update.related(data);
			}
		},
		
		curRelation : function (x, data) {
			var db = at.freebim.db, nf = db.domain.NodeFields, node = data.entity, nodeId = "" + node[nf.NODEID], attr = x.attr("nodeid");
			if (x.hasClass("current") && attr == nodeId) {
				db.logger.debug("at.freebim.db.update.curRelation[" + nodeId + "]");
				// reload current item of 'relations' tab:
				jq(db.relations).trigger("showNode", [{nodeId: nodeId}]);
				db.update.related(data);
			}
		},
		
		tableRow : function (x, data) {
			var db = at.freebim.db, nf = db.domain.NodeFields, node = data.entity, nodeId = "" + node[nf.NODEID];
			var self = x[0], attr = x.attr("nodeid");
			if (attr == nodeId) {
				db.logger.debug("at.freebim.db.update.tableRow[" + nodeId + "]");
				var row = x.attr("row"), table = x.closest("table").data("table");
				if (row != undefined && table) {
					try { 
						// recreate table row for that node:
						table.createRow(row, self);
					} catch (e) {
						db.logger.error("Error in at.freebim.db.update.tableRow[" + nodeId + "]: " + e.message);
					}
					db.update.related(data);
				}
			} 

		},
		
		treeNode : function (x, data) {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, node = data.entity, nodeId = "" + node[nf.NODEID];
			var attr = x.attr("nodeid");
			if (attr == nodeId) {
				db.logger.debug("at.freebim.db.update.treeNode[" + nodeId + "]");
				var tree = db.tree.treeOfElement(x), parentId = x.attr("parentid");
				if (tree && parentId != undefined) {
					try { 
						tree.addSingleNode(nodeId, x[0], parentId);
//						tree.loadChildren(nodeId); 
					} catch (e) {
						db.logger.error("Error in at.freebim.db.update.treeNode[" + nodeId + "]: " + e.message);
					} 
					db.update.related(data);
				} 
			}
		}
};

// enable editPhase in parameterlist
jq(document).delegate("td[phase]", "mouseover", function (event, data) {
	if (at.freebim.db.contributor) {
		jq(this).css('cursor', 'pointer');
	}
});
jq(document).delegate("td[phase]", "mouseout", function (event, data) {
	if (at.freebim.db.contributor) {
		jq(this).css('cursor', 'default');
	}
});
jq(document).delegate("td[phase]", "click", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields;
	if (db.contributor) {
		var p = jq(this).next().attr("nodeid"), r = jq(this).attr("rel"), i, n, comps;
		p = d.get(p);
		comps = p[nf.HP];
		n = comps.length;
		for (i=0; i<n; i++) {
			if (comps[i][rf.ID] == r) {
				r = comps[i];
				break;
			}
		}
		if (p && r) {
			jq(d.rel.HasParameter).trigger("editPhase", [{rel: r, okFn: function () {
				jq(document).trigger("_save", [{entity:p}]);
			}}]);
		}
	}
});
jq(document).delegate("tr[nodeid][level]", "newNodeModified", function (event, data) {
	var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields;
	if (db.contributor) {
		var tr = jq(this)[0], td = jq(this).children()[1], p = jq(this).attr("nodeid"), e = jq(this).attr("e"), i, r = null, n, comps;
		p = d.get(p);
		if (p) {
			comps = p[nf.HP];
			n = comps.length;
			for (i=0; i<n; i++) {
				if (comps[i][rf.FROM_NODE] == e) {
					r = comps[i];
					break;
				}
			}
			if (r) {
				d.parameterlist.phaseTd(tr, td, p, r);
			}
		}
	}
});

// toggle visibility of components in parameterlist
jq(document).delegate(".pl-component-toggle, .pl-material-toggle, .pl-part-toggle", "click", function (event, data) {
	var s = jq(this).siblings("table");
	s.toggle();
	if (s.is(':visible')) {
		jq(this).html("-");
	} else {
		jq(this).html("+");
	}
});

// select items, edit items, mark items
jq(document).delegate(".freebim-item", "click", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, x = jq(this), nodeId = x.attr("nodeid"), done = false;
	if (db.contributor) {
		if (event.altKey) {
			if (nodeId != undefined) {
				var node = d.get(nodeId);
				if (d.isSelectable(node)) {
					db.select(x);
				}
			}
			done = true;
		}
		if (event.ctrlKey || event.metaKey) {
			if (nodeId != undefined) {
				db.mark(x);
			}
			done = true;
		}
		if (x.closest(".floating-table").length > 0) {
			done = true;
		}
	}
	if (done) {
		event.preventDefault();
		event.stopPropagation();
	} else {
		if (!x.hasClass("no-edit") 
				&& !x.hasClass("info-only") 
				&& !x.hasClass("freebim-history-item") 
				&& !x.hasClass("freebim-related-item")) {
			var node = d.get(nodeId);
			if (node) {
				if (!d[node[nf.CLASS_NAME]].initialized) {
					d[node[nf.CLASS_NAME]].init();
				}
				jq(document).trigger("_editEvent", [{entity: node}]);
			}
		}
	}
});


// update status div
jq(document).delegate("#status", "_update", function (event, data) {
	var db = at.freebim.db;
	if (db.statusUpdateTimer) {
		clearTimeout(db.statusUpdateTimer);
	}
	db.statusUpdateTimer = setTimeout(function () {
		var txt = "", m = db.marked.length, s = db.selected.length, n = Object.keys(db.nodes).length, i18n = net.spectroom.js.i18n;
		txt += m;
		txt += " ";
		txt += i18n.get_n("STATUS_MARKED", m);
		txt += ", ";
		txt += s;
		txt += " ";
		txt += i18n.get_n("STATUS_SELECTED", s);
		txt += ", ";
		txt += n;
		txt += " ";
		txt += i18n.get_n("STATUS_LOADED", n);
		txt += " " + i18n.get("STATUS_ELEMENTS");
		jq("#status_info").html(txt);
	}, 250);
});

jq(document).on("_editEvent", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
	db.logger.debug(cn + ": on _editEvent");
	if (!data || !data.entity || !data.entity[nf.NODEID]) {
		return;
	}
	var cn = data.entity[nf.CLASS_NAME], clazz = d[cn];
	clazz.init();
	if (cn == "FreebimUser" || cn == "Contributor" || cn == "Company") {
		if (!db.user.usermanager) {
			return;
		} 
	} else {
		if (!db.contributor || !db.contributor.id) {
			if (cn == "Component") {
				jq(document).trigger("show-parameterlist", [{nodeId: data.entity[nf.NODEID]}]);
			} 
			return;
		}
	}
	jq(event.target).one("newNodeModified", function (event, data) {
		var entity = data.entity;
		db.logger.debug(cn + "_editEvent: newNodeModified: " + entity[nf.NODEID]);
		if (entity) {
			if (entity[nf.STATE] && entity[nf.STATE] == d.State.R) {
				// released entity!
				jq(document).trigger("alert", [{
					title: "DLG_TITLE_FREEBIM_INFO", 
					content: i18n.gA1("CANT_EDIT_IS_RELEASED", clazz.title, clazz.getNameOf(entity)) 
				}]);
				return;
			}
			if (cn != "FreebimUser" && cn != "Contributor" && cn != "Library") {
				// set read-only to fields if current contributor is not responsible for that entity
				if (db.contributor && db.contributor[nf.RESPONSIBLE] && entity[nf.REFERENCES]) {
					var mayEdit = d.mayEdit(entity);
					clazz.setFieldStates(mayEdit);
				}
			}
			if (entity.deleted && entity.deleted == 1) {
				clazz.form.show(i18n.gA1("DLG_TITLE_EDIT_DELETED", clazz.i18n, clazz.getNameOf(entity)));
			} else {
				if (entity[nf.STATE] && entity[nf.STATE] == d.State.R && !db.contributor.maySetReleaseStatus) {
					clazz.form.show(i18n.gA1("DLG_TITLE_EDIT_RELEASED", clazz.i18n, clazz.getNameOf(entity)));
				} else {
					clazz.form.show(i18n.gA1("DLG_TITLE_EDIT", clazz.i18n, clazz.getNameOf(entity)));
				}
			}

			clazz.form.setEntity(entity);
			jq("#" + clazz.className + " tr.jsTableRow-selected").removeClass("jsTableRow-selected");
			jq("#" + clazz.className + " tr[value='" + entity[nf.NODEID] + "']").addClass("jsTableRow-selected");
			if (!db.user.isShowFreebimId) {
				jq(".freebim-freebimId").hide();
			}
			if (!db.user.isShowNodeId) {
				jq(".freebim-nodeId").hide();
			}
			// add Parameter-List button for Components only
			if (entity[nf.CLASS_NAME] == d.Component.className) {
				var div = document.createElement("div"), dlgId = jq(clazz.form.parent).attr("id");
				jq(div).html("<input type=\"button\" i18n='BUTTON_SHOW_PARAM_LIST' value=\"" + i18n.g("BUTTON_SHOW_PARAM_LIST") + "\" class=\"parameterlist-btn\" >");
				jq(clazz.form.parent).find(".content").append(div);
				jq("#" + dlgId + " .parameterlist-btn").click(function () {
					jq(document).trigger("show-parameterlist", [{nodeId: entity[nf.NODEID]}]);
				});
			}
		}
		jq(document).trigger("filter", [{}]);
	});
	jq(document).trigger("getNode", [{nodeId: data.entity[nf.NODEID], cn: cn}]);
});

jq(document).on("_delete", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
	if (!(data && data.entity && data.entity[nf.NODEID])) {
		return;
	}
	var cn = data.entity[nf.CLASS_NAME], clazz = d[cn], ts = db.time.now();
	clazz.init();
	db.logger.debug(cn + ": on _delete");
	if (!data.confirmed && d.confirmDelete) {
		jq(document).trigger("confirm", [{
			msg: i18n.g("CONFIRM_DELETE"),
			yes: function() {
				data.confirmed = true;
				jq(document).trigger("_delete", data);
			}
		}]);
		return;
	}
	
	db.post("/" + clazz.path + "/delete",
		{ nodeId : data.entity[nf.NODEID] },
		i18n.gA1("DELETING", clazz.i18n, data.entity[nf.NAME]),
		function (response) {
			if (response.result) {
				var entity = response.result;
				d.listen(entity);
				if (data.request) {
					// make the entity accessible for future requests of the same type
					var keys = Object.keys(data.request), key = keys[0];
					try {
						d.listen(entity);
						jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{entity: entity, ts: ts}]);
					} catch (e) {
						db.logger.error(key + " response: " + e.message);
					}
				} else {
					jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{entity: entity, ts: ts}]);
				}
				jq(document).trigger("alert", [{
					title: "DLG_TITLE_FREEBIM_INFO", 
					content: i18n.gA1("DELETE_SUCCESS", clazz.i18n, clazz.getNameOf(entity)),
					autoClose : 1500 
				}]);

			} else {
				at.freebim.db.request.logout();
				return;
			}
		},
		function (error) {
			clazz.loading = false;
		},
		null,
		"DELETE"
	);
});

jq(document).on("_load", function (event, data) {
	setTimeout(function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, cn = data.cn, clazz = d[cn], i18n = net.spectroom.js.i18n;
		db.logger.debug(cn + ": on _load");
		clazz.init();

		if (clazz.loading) {
			return;
		}
		clazz.loading = true;
		
		db.post("/" + clazz.path + "/list",
			{clean : false}, 
			i18n.gA("LOADING", clazz.i18n),
			function (response) {
				var i, entities = response.result, // Array of i.e. Contributor's
				n = entities.length;
				clazz.arr = [];
				for (i = 0; i < n; i++) {
					var entity = entities[i];
					d.listen(entity);
					clazz.arr.push(entity[nf.NODEID]);
				}
				
				clazz.sort();

				jq(document).trigger(cn + "_loaded", [ { arr : clazz.arr } ]);

				clazz.loading = false;
				if (data.callback) {
					data.callback();
				}
			},
			function (error) {
				clazz.loading = false;
			}, null, "GET"
		);
	}, 1);
});

jq(document).on("_showTable", function(event, data) {
	var db = at.freebim.db, d = db.domain, cn = data.cn, clazz = d[cn], i18n = net.spectroom.js.i18n;
	clazz.init();
	
	db.logger.debug(cn + "_showTable");
	if (clazz.arr) {
		// already loaded
		if (jq("#" + clazz.className + " .content").length > 0) {
			// content exists
			jq(clazz.table).trigger(clazz.className + "_tableCreated", data);
			return;
		}
		// content doesn't exist
		jq(clazz.table).one(clazz.className + "_tableCreated", function(event, data) {
			jq(document).trigger("hide_progress", [{ key : cn + "_showTable" }]);
		});
		jq(document).trigger("show_progress", [{ key : cn + "_showTable", msg : i18n.gA("SHOW_TABLE", clazz.i18n) }]);

		clazz.table.rowCount = clazz.arr.length;
		var content = document.createElement("div");
		jq(content).appendTo("#" + data.parent).addClass("content");
		clazz.table.create(clazz.table, content, clazz, clazz);
	} else {
		// not loaded yet
		jq(document).one(cn + "_loaded", function(e, data2) {
			db.logger.debug(cn + "_loaded");
			jq(document).trigger("_showTable", [{ parent : data.parent, cn: cn }]);
		});
		jq(document).trigger("_load", [{cn: cn}]);
	}
});

jq(document).on("_save", function (e, data) {
	var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, entity = data.entity, 
		cn = entity[nf.CLASS_NAME], 
		clazz = d[cn], 
		isInsert = (!entity[nf.NODEID] || (entity[nf.NODEID] + "").length == 0), 
		rels = [],
		ts = db.time.now(),
		i18n = net.spectroom.js.i18n;
	
	db.logger.debug(cn + "_save");
	
	if (db.bsdd && db.bsdd.currentIfdConcept) {
		var c = db.bsdd.currentIfdConcept;
		db.bsdd.currentIfdConcept = undefined;
		if (net.spectroom.js.cookie.get(db.bsdd.cookie)) {
			db.bsdd.upload(c, function (guid) {
				data.entity[nf.BSDD_GUID] = guid;
				jq(document).trigger("_save", data);
				return;
			});
			return;
		}
	}

	// remove any existing Bsdd relations
	// if there is a bsDD-Guid set for that node
	if (db.bsdd.isValidGuid(entity[nf.BSDD_GUID])) {
		entity[nf.LOADED_BSDD] = [];
	}
	
	if (clazz.relations) {
		var i, n = clazz.relations.length; // [{field: "childs", t: 4, cn: "Component", dir: "IN" }];
		for (i=0; i<n; i++) {
			if (clazz.relations[i][rf.TYPE] == d.RelationTypeEnum.CONTRIBUTED_BY) {
				// prevent save of ContributedBy relations
				// Server has to manage them!
				continue;
			}
			var rel = {};
			rel[rf.DATA] = entity[clazz.relations[i][rf.FIELD_NAME]];
			rel[rf.TYPE] = clazz.relations[i][rf.TYPE];
			rel[rf.DIRECTION] = clazz.relations[i][rf.DIRECTION];
			rel[rf.CLASS_NAME] = clazz.relations[i][rf.CLASS_NAME];
			rels.push(rel);
		}
	}
	if (isInsert) {
		entity[nf.VALID_FROM] = null;
		entity[nf.VALID_TO] = null;
		if (!entity[nf.REFERENCES] || entity[nf.REFERENCES].length == 0) {
			if (db.contributor 
					&& db.contributor.currentLib != undefined 
					&& db.contributor[nf.RESPONSIBLE] 
					&& db.contributor[nf.RESPONSIBLE].length > 0 
					&& !(cn == "FreebimUser" || cn == "Contributor" || cn == "Library")
					&& (clazz.isContributed || clazz.className === d.Document.className)) {
				var rel = {}, relData = {};
				relData[rf.FROM_NODE] = null;
				relData[rf.TO_NODE] = db.contributor.currentLib;
				relData[rf.TYPE] = d.RelationTypeEnum.REFERENCES;
				relData[rf.TIMESTAMP] = ts - db.delta;
				
				rel[rf.DATA] = [relData];
				rel[rf.TYPE] = d.RelationTypeEnum.REFERENCES;
				rel[rf.DIRECTION] = "OUT";
				rel[rf.CLASS_NAME] = "References";

				rels.push(rel);
			}
		}
	}
	entity[nf.NODEID] = ((entity[nf.NODEID] == "") ? null : entity[nf.NODEID]);
	
	db.post("/" + clazz.path + "/save",
		entity,
		i18n.gA1("SAVING", clazz.i18n, clazz.getNameOf(entity)),
		function(response) {
			jq(document).trigger("hide_progress", [ { key : cn + "_save" } ]);
			if (response.error != undefined) {
				alert(response.error);
			} else {
				if (response.result) {
					var entity = response.result;
					d.listen(entity);
					if (rels) {
						try {
							clazz.saveRelations(entity, rels, isInsert, ts, data.callback, response.bsddGuidChanged);
						} catch (e) {
							db.logger.error("Error in " + cn + "_save saveRelations: " + e.message);
						}
					} else {
						if (data.callback) {
							data.callback(entity);
						}
//						jq(document).trigger(cn + "_saved");
						if (isInsert) {
							jq("table.clazz_" + entity[nf.CLASS_NAME]).trigger("newInserted", [{entity: entity, ts: ts}]);
						} else {
							jq("[nodeid='" + entity[nf.NODEID] + "']").trigger("newNodeModified", [{entity: entity, ts: ts}]);
						}
					}
				}
			}
			jq(document).trigger("filter", [{}]);

		},
		null, // fail
		null,
		"POST"
	);
});

jq(document).on("_addEvent", function (e, data) {
	var db = at.freebim.db, d = db.domain, cn = data.cn, clazz = d[cn], entity, i18n = net.spectroom.js.i18n;
	try {
		clazz.setFieldStates(true);
		clazz.form.show(i18n.gA("DLG_TITLE_EDIT_ADD", clazz.i18n));
		entity = ((data && data.entity) ? data.entity : clazz.form.newEntity());
		clazz.form.setEntity(entity);
	} catch(e) {
		db.logger.error("Error in " + clazz.className + "_addEvent" + e.message);
	}
});

jq(document).on("_bsddGuidChanged", function (event, data) {
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, entity = data.entity;
	net.spectroom.js.forAll(entity[nf.EQUALS], function (r, i) {
		// r is an Equal relation
		d.rel.Equals.getOtherNode(entity, r, function (eq) {
			eq[nf.BSDD_GUID] = entity[nf.BSDD_GUID];
			jq(document).trigger("_save", [{ entity: eq }]);
		});
	});
});

jq(document).on("_sortBegin", function (event, data) {
	var cn = data.cn, clazz = at.freebim.db.domain[cn], i18n = net.spectroom.js.i18n;
	jq(document).trigger("show_progress", [ { key : cn + "_sort", msg : i18n.gA("SORTING", clazz.i18n) } ]);
});

jq(document).on("_sortEnd", function (event, data) {
	jq(document).trigger("hide_progress", [ { key : data.cn + "_sort" } ]);
});

jq(document).on("_csvBegin", function (event, data) {
	var cn = data.cn, clazz = at.freebim.db.domain[cn], i18n = net.spectroom.js.i18n;
	jq(document).trigger("show_progress", [ { key : cn + "_csv", msg : i18n.gA("SORTING", clazz.i18n) } ]);
});

jq(document).on("_csvEnd", function (event, data) {
	jq(document).trigger("hide_progress", [ { key : data.cn + "_csv" } ]);
});

jq(document).on("_filterBegin", function (event, data) {
	var cn = data.cn, clazz = at.freebim.db.domain[cn], i18n = net.spectroom.js.i18n;
	jq(document).trigger("show_progress", [ { key : cn + "_filter", msg : i18n.gA("FILTERING", clazz.i18n) } ]);
});

jq(document).on("_filterEnd", function (event, data) {
	jq(document).trigger("hide_progress", [ { key : data.cn + "_filter" } ]);
	jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
});

jq(document).on("_cancel", function (event, data) {
	var db = at.freebim.db, nf = db.domain.NodeFields;
	if (db.bsdd && db.bsdd.currentIfdConcept) {
		db.bsdd.currentIfdConcept = undefined;
	}
	if (!data || !data.entity || !data.entity[nf.NODEID]) {
		return;
	}
	db.logger.debug("on _cancel: " + data.entity[nf.NODEID]);
	jq(document).trigger("getNode", [{nodeId: data.entity[nf.NODEID], cn: data.entity[nf.CLASS_NAME]}]);
});

jq(document).on("getNode", function (event, data) {
	var db = at.freebim.db;
	if (data.nodeId) {
		if (data.nodeId == db.domain.bbnId) {
			// no need to load the BigBangNode
			jq("[nodeid='" + data.nodeId + "']").trigger("newNodeModified", [{entity: db.domain.bbn}]);
			return;
		}
		db.logger.debug("on getNode(" + data.nodeId + ")");
		db.domain.delayedLoadNode("" + data.nodeId, data.cn);
	} else {
		// perform specified request
	}
});

jq(document).on("getNodeWithCallback", function (event, data) {
	var db = at.freebim.db;
	if (data.nodeId) {
		db.logger.debug("on getNode(" + data.nodeId + ")");
		db.domain.loadNode("" + data.nodeId, data.cn, data.callback);
	} else {
		// perform specified request
	}
});

jq(document).on("_at.freebim.db.marked.changed", function (event, data) {
	if (at.freebim.db.marked.length > 0) {
		jq(".filter-marked").show();
	} else {
		jq(".filter-marked").hide();
	}
});

jq(document).delegate("#refresh-relevant", "click", function (event, data) {
	at.freebim.db.domain.updateRelevance();
});

jq(document).delegate(".GUID input", "keydown", function (event, data) {
	if (event.altKey) {
		var x = jq(this);
		if (at.freebim.db.bsdd.isValidGuid(x.val())) {
			var val = x.val();
			val = com.muigg.ifc.IfcGuid.convert(val);
			x.val(val);
		}
	}
});

jq(document).on("i18n_translate", function (event, data) {
	var db = at.freebim.db, d = db.domain, i18n = net.spectroom.js.i18n;

	d.BsddNode.title = 			i18n.g("CLAZZ_BSDDNODE");
	d.Component.title = 		i18n.g("CLAZZ_COMPONENT");
	d.ComponentType.title = 	i18n.g("CLAZZ_COMPONENTTYPE");
	d.Contributor.title = 		i18n.g("CLAZZ_CONTRIBUTOR");
	d.DataType.title = 			i18n.g("CLAZZ_DATATYPE");
	d.Discipline.title = 		i18n.g("CLAZZ_DISCIPLINE");
	d.Document.title = 			i18n.g("CLAZZ_DOCUMENT");
	d.FreebimUser.title = 		i18n.g("CLAZZ_USER");
	d.Library.title = 			i18n.g("CLAZZ_LIBRARY");
	d.Measure.title = 			i18n.g("CLAZZ_MEASURE");
	d.Parameter.title = 		i18n.g("CLAZZ_PARAMETER");
	d.ParameterSet.title = 		i18n.g("CLAZZ_PARAMETERSET");
	d.ParameterSetType.title = 	i18n.g("CLAZZ_PARAMETERSETTYPE");
	d.ParameterType.title = 	i18n.g("CLAZZ_PARAMETERTYPE");
	d.Phase.title = 			i18n.g("CLAZZ_PHASE");
	d.SimpleNamedNode.title = 	i18n.g("CLAZZ_SIMPLENAMEDNODE");
	d.Unit.title = 				i18n.g("CLAZZ_UNIT");
	d.ValueList.title = 		i18n.g("CLAZZ_VALUELIST");
	d.ValueListEntry.title = 	i18n.g("CLAZZ_VALUELISTENTRY");
	
});

jq(document).ready(function () {
	
	var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;

	jq("#show-deleted").change(function() {
		var checked = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-deleted", checked, 14);
		d.showDeleted = ((checked) ? "show-deleted" : "hide-deleted");
		if (checked) {
			jq(".hide-deleted").addClass("show-deleted").removeClass("hide-deleted");
		} else {
			jq(".show-deleted").addClass("hide-deleted").removeClass("show-deleted");
		}
		jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
		jq(document).trigger("freebimItemDisplayChanged");
	});
	jq("#show-unused").change(function() {
		var checked = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-unused", checked, 14);
		d.showUnused = ((checked) ? "show-unused" : "hide-unused");
		if (checked) {
			jq(".hide-unused").addClass("show-unused").removeClass("hide-unused");
		} else {
			jq(".show-unused").addClass("hide-unused").removeClass("show-unused");
		}
		jq(net.spectroom.js.table).trigger("_tableCount", [{}]);
		jq(document).trigger("freebimItemDisplayChanged");
	});
	jq("#show-abstract").change(function() {
		db.user.isShowAbstract = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-abstract", db.user.isShowAbstract, 14);
		db.user.doShowAbstract();
		jq(document).trigger("freebimItemDisplayChanged");
	});
	jq("#show-library").change(function() {
		db.user.isShowLibrary = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-library", db.user.isShowLibrary, 14);
		d.showLibraryChanged();
	});
	jq("#show-nodeId").change(function() {
		db.user.isShowNodeId = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-nodeId", db.user.isShowNodeId, 14);
		d.showNodeIdChanged();
	});
	jq("#omit-server-prompt").change(function() {
		db.user.omitServerPrompt = jq(this).is(':checked');
		net.spectroom.js.cookie.set("omit-server-prompt", db.user.omitServerPrompt, 14);
	});
	jq("#show-freebimId").change(function() {
		db.user.isShowFreebimId = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-freebimId", db.user.isShowFreebimId, 14);
		d.showFreebimIdChanged();
	});
	jq("#show-bsdd").change(function() {
		db.user.isShowBsdd = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-bsdd", db.user.isShowBsdd, 14);
		d.showBsddChanged();
	});
	jq("#show-pset").change(function() {
		db.user.isShowPset = jq(this).is(':checked');
		net.spectroom.js.cookie.set("show-pset", db.user.isShowPset, 14);
		d.showPsetChanged();
	});
	jq("#confirm-deletion").change(function() {
		d.confirmDelete = jq(this).is(':checked');
		net.spectroom.js.cookie.set("confirm-deletion", db.user.confirmDelete, 14);
	});
	jq("input[type='radio'][name='at.freebim.db.lang']").change(function () {
		d.Component.init();
		d.Measure.init();
		d.Parameter.init();
		d.ValueList.init();
		d.ValueListEntry.init();
		db.lang = jq("input[type='radio'][name='at.freebim.db.lang']:checked").val();
		net.spectroom.js.cookie.set("at.freebim.db.lang", db.lang, 14);
		jq(".freebim-item[nodeid]").each(function () {
			var name, x = jq(this), nodeId = x.attr("nodeid"), node = d.get(nodeId);
			if (node && d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].getNameOf) {
				try {
					name = d[node[nf.CLASS_NAME]].getNameOf(node, x.get(0));
					name = net.spectroom.js.shrinkText(name, 32, x.get(0));
					x.html(name);
				} catch (ex) {
					db.logger.error("at.freebim.db.lang changed (" + nodeId + ")");
				}
			}
		});
		jq(".freebim-desc[nodeid]").each(function () {
			var x = jq(this), nodeId = x.attr("nodeid"), node = d.get(nodeId);
			if (node && d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].getDescOf) {
				var desc = d[node[nf.CLASS_NAME]].getDescOf(node);
				x.html(net.spectroom.js.shrinkText(desc, 64, x.get(0)));
			}
		});
	});
	
	jq(document).on("show_progress", function(e, data) {
		setTimeout(function () {
			var n = 0, i18n = net.spectroom.js.i18n;
			if (data && data.msg && data.key) {
				db.progress.msg[data.key] = data.msg;
				var i, key, keys = Object.keys(db.progress.msg), m, msg = "<ul>";
				n = keys.length;
				for (i = 0; i < n; i++) {
					key = keys[i];
					m = db.progress.msg[key];
					msg += "<li>" + m + "</li>";
				}
				msg += "</ul><p>" + i18n.get("PLEASE_WAIT") + "</p>";
				jq("#freebim-progress-msg").html(msg);
			}
			if (n > 0) {
				if (db.progress.timer) {
					clearTimeout(db.progress.timer);
				}
				db.progress.timer = setTimeout(function() {
					var isOpen = jq("#freebim-progress").dialog("isOpen");
					if (!isOpen) {
						jq("#freebim-progress").dialog("open");
					}
				}, 250);

			}
		}, 1);
	});

	jq(document).on("hide_progress", function(e, data) {
		setTimeout(function () {
			if (data && data.key) {
				if (db.progress.msg[data.key]) {
					delete db.progress.msg[data.key];
				}
			}
			var n = Object.keys(db.progress.msg).length;
			if (n == 0) {
				if (db.progress.timer) {
					clearTimeout(db.progress.timer);
				}
				jq("#freebim-progress").dialog("close");
			}
		}, 1);
	});

	jq("#freebim-progress").dialog({
		title : i18n.g("DLG_TITLE_FREEBIM_PROCESS"),
		modal : true,
		autoOpen : false,
		width : 300,
		height : 185,
		show : "slideDown",
		hide : 'slideUp'
	}).prev().attr("i18n_dlg", "DLG_TITLE_FREEBIM_PROCESS");
	
	d.showLibraryInfo();
	
	jq(document).on("confirm", function (event, data) {
		var dlg = net.spectroom.js.newDiv();
		jq(dlg).dialog({
			title : i18n.g("DLG_TITLE_FREEBIM_CONFIRM"),
			modal: true,
			open : function () {
				jq(dlg).append(data.msg);
			},
			close: function () {
				jq(dlg).remove();
			},
			buttons : [{
				text : i18n.getButton("DLG_BTN_NO"),
				click : function() {
					jq(dlg).dialog("close");
					if (data.no) {
						data.no();
					}
				} 
			}, {
				text : i18n.getButton("DLG_BTN_YES"),
				click : function() {
					jq(dlg).dialog("close");
					if (data.yes) {
						data.yes();
					}
				}
			}]
		}).prev().attr("i18n_dlg", "DLG_TITLE_FREEBIM_CONFIRM"); 
	});

						
	/**
	 * Show an alert dialog.<br/>
	 * 
	 * This dialog will have a single 'ok' button. If <code>data.autoClose</code> is set 
	 * and user setting <code>at.freebim.db.user.omitServerPrompt</code> is <code>true</code>,
	 * this dialog won't show up.
	 * 
	 * @param event
	 * @param data.autoClose
	 *            Optinally close this dialog automatically after specified
	 *            amount of milli seconds.
	 * @param data.title
	 *            net.spectroom.js.i18n resource-ID of dialog title.
	 * @param data.content
	 *            The message to be shown.
	 * @param data.okFn
	 *            Optional pointer to a function that should be called when user
	 *            hits the 'ok' button. 'okFn' will be called with one
	 *            parameter: 'true'
	 */
	jq(document).on("alert", function (event, data) {
		if (data.autoClose && db.user.omitServerPrompt) {
			return;
		} 
		var dlg = net.spectroom.js.newDiv(), timer = null, i18n = net.spectroom.js.i18n;
		jq(dlg).dialog({
			title : i18n.g(data.title), // data.title is i18n resource ID!
			modal : true,
			open : function () {
				jq(dlg).append(data.content);
			},
			close : function () {
				if (timer) {
					clearTimeout(timer);
					timer = null;
				}
				if (data.okFn) {
			    	data.okFn(true);
				}
				jq(dlg).remove();
			},
			buttons : [{
				text : i18n.getButton("DLG_BTN_OK"),
				click : function (form, suffix) {
				    jq(dlg).dialog("close");
				}
			}]
		}).prev().attr("i18n_dlg", data.title);	
		if (data.autoClose) {
			timer = setTimeout(function () {
				timer = null;
				jq(dlg).dialog("close");
			}, data.autoClose);
		}
	});
	
	jq(document).delegate("input[type='submit'][name='logoutaction']", "click", function (event, data) {
		jq(document).trigger("do-bsdd-logout", [{}]);
	});
});
