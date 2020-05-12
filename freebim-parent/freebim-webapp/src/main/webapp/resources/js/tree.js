/**
 * 
 */

at.freebim.db.tree = {
		
	initialized : false,
	
	trees : {},
	
	treeOfElement : function (x) {
		var treeId = jq(x).closest(".ui-layout-pane").attr("id");
		return at.freebim.db.tree.trees[treeId];
	},
		
	init : function (p, rootId) {
		var self = p, db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
		self.rootId = rootId;
		
		db.tree.trees[rootId] = self;
		
		if (!db.tree.initialized && db.contributor) {
			db.tree.initialized = true;
			jq(document).delegate(".measure-toggle", "click", function (event, data) {
				var x = jq(this), s = x.html(), next = x.next(), tree = db.tree.treeOfElement(x);
				if (s == "+") {
					// show measures
					x.html("-");
					next.show();
					var r, i, pId = x.closest("[nodeid]").attr("nodeid"), p = d.get(pId), n = p[nf.MEASURES].length,
						addM = function (mId, mdiv) {
							var mItemDiv = document.createElement("div");
							jq(mdiv).append(mItemDiv);
							jq(mdiv).attr("title", i18n.g("CLAZZ_MEASURE")).attr("i18n_title", "CLAZZ_MEASURE").css("clear", "both");
							if (tree.dropEquality) {
								jq(mItemDiv).droppable(db.tree.dropEqualityOptions);	
							}
							if (tree.dragEquality) {
								jq(mItemDiv).draggable(db.tree.dragOptions);	
							}
							d.renderNodeId(mId, d.Measure.className, mItemDiv, null, function(m) {
								if (m[nf.HAS_VALUE]) {
									var vltdiv = document.createElement("div"),
										vldiv = document.createElement("div");
									jq(mdiv).append(vltdiv);
									jq(mdiv).append(vldiv);
									jq(vldiv).addClass("child");
									jq(vltdiv).html("+").addClass("vl-toggle");
								}
							});
						};
					for (i=0; i<n; i++) {
						r = p[nf.MEASURES][i]; // r is a HasMeasure relation
						var mId = r[rf.TO_NODE], mdiv = document.createElement("div");
						next.append(mdiv);
						addM(mId, mdiv);
					}
					
				} else if (s == "-") {
					x.html("+");
					next.hide().empty();
				}
			});
			jq(document).delegate(".vl-toggle", "click", function (event, data) {
				var x = jq(this), s = x.html(), next = x.next(), tree = db.tree.treeOfElement(x);
				if (s == "+") {
					// show valuelist
					x.html("-");
					next.show();
					var r, i, mId = x.prev().attr("nodeid"), m = d.get(mId), n = m[nf.HAS_VALUE].length
					addList = function (r) {
						var vlId = r[rf.TO_NODE], 
							vldiv = document.createElement("div"),
							vlesdivs = document.createElement("div"),
							vlesdiv = document.createElement("div");
						jq(vlesdiv).addClass("child").attr("rid", r[rf.ID]);
						jq(vlesdivs).attr("title", i18n.g("CLAZZ_VALUELISTENTRY"))
							.attr("i18n_title", "CLAZZ_VALUELISTENTRY")
							.append(vldiv)
							.append(vlesdiv);
						next.append(vlesdivs);
						
						jq(vldiv).attr("title", i18n.g("CLAZZ_VALUELIST")).attr("i18n_title", "CLAZZ_VALUELIST").css("clear", "both");
						
//						next.append(vlesdiv);
						d.renderNodeId(vlId, d.ValueList.className, vldiv, null, function (vl) {
							if (vl && vl[nf.HAS_ENTRY]) {
								
								var addChilds = function (vl) {
										var rel, vleId,
											j, cnt = vl[nf.HAS_ENTRY].length;
										for (j=0; j<cnt; j++) {
											var vlediv = document.createElement("div");
											jq(".child[rid='" + r[rf.ID] + "']").append(vlediv);
											if (tree.dropEquality) {
												jq(vlediv).droppable(db.tree.dropEqualityOptions);	
											}
											if (tree.dragEquality) {
												jq(vlediv).draggable(db.tree.dragOptions);	
											}
											rel = vl[nf.HAS_ENTRY][j]; // r is a HasEntry relation
											vleId = rel[rf.TO_NODE];
											d.renderNodeId(vleId, d.ValueListEntry.className, vlediv, null, null);
										}

									}; 
//								jq(vldiv).html(vl[nf.NAME]);
								if (tree.dropEquality) {
									jq(vldiv).droppable(db.tree.dropEqualityOptions);	
								}
								if (tree.dragEquality) {
									jq(vldiv).draggable(db.tree.dragOptions);	
								}
								d.renderNode(vl, vldiv);
								vl[nf.HAS_ENTRY].sort(function(r1, r2) { 
									return (r1[rf.ORDERING] - r2[rf.ORDERING]); 
								});
								
								var okFn = function (c) {
									var sub = document.createElement("sub");
									d.renderNode(c, sub);
									jq(sub).appendTo(vldiv).addClass("info-only");
								};
								d.rel.HasValue.init();
								jq(d.rel.HasValue).trigger("componentFromRel", [{rel: r, okFn: okFn}]);

								addChilds(vl);
							}
						});
					};
					for (i=0; i<n; i++) {
						r = m[nf.HAS_VALUE][i]; // r is a HasValue relation
						addList(r);
					}
				} else if (s == "-") {
					x.html("+");
					next.hide().empty();
				}
			});
		}

		jq(db.tree).on("getParameters", function (event, data) {
			if (self.rootId != data.rootId) {
				return;
			}
			var id = data.nodeId;
			if (id != undefined) {
				net.spectroom.js.cookie.set(self.rootId + "_param_" + id, "1", 14);	
				d.getOrLoad(id, d.Component.className, function (c) {
					self.createParameterTable("" + id);
					if (data.callback) {
						data.callback();
					}
				});
			}
		});
		
		self.addChildren = function (parent, node, cn) {
			var div, i, n = ((node[nf.CHILDS]) ? node[nf.CHILDS].length : 0), childId;
			if (n > 1) {
				node[nf.CHILDS].sort(function (a, b) {
					return (a[rf.ORDERING] - b[rf.ORDERING]);
				});
			}
			for (i=0; i<n; i++) {
				childId = node[nf.CHILDS][i][rf.TO_NODE]; // childs[i] is an outgoing ParentOf relation
				div = document.createElement("div"), // the complete <div>
				jq(parent).append(div);
				jq(div).attr("nodeid", childId).addClass(cn);
				self.addOneChild(childId, div, node[nf.NODEID]);
			}
		};
		
		self.addOneChild = function (childId, div, parentId) {
			var cn = ((parentId == d.bbnId) ? "Library" : "Component");
			d.getOrLoad(childId, cn, function (child) {
				self.addSingleNode(childId, div, parentId);
			});
		};

		self.addSingleNode = function (nodeId, div, parentId) {

			var node = child = d.get(nodeId),
				h = document.createElement("div"),  // the complete headline: '+ Name [...]'
				codeDiv = document.createElement("div"),
				ht = document.createElement("div"),  // the headline's text:  'Name'
				he = document.createElement("div"),  // the headline's equal nodes 
				c = document.createElement("div"), // the children (content)
				mayEdit = d.mayEdit(node),
//				deletedClass = "",
				childCount = ((node && node[nf.CHILDS]) ? node[nf.CHILDS].length : 0);
			jq(div).empty();
			if (node && node.deleted) {
				jq(div).addClass(d.showDeleted);
			} else {
				jq(div).removeClass(d.showDeleted);
			}
			if (node && node.unused) {
				jq(div).addClass(d.showUnused);
			} else {
				jq(div).removeClass(d.showUnused);
			}

			div.appendChild(h);
			self.addNavSign(h, childCount, nodeId);
			
			h.appendChild(codeDiv);
			jq(codeDiv).addClass("freebim-code");
			h.appendChild(ht);
			jq(h).addClass("tree-headline");
			div.appendChild(c);
			
			jq(div).addClass("tree-node" + ((mayEdit) ? " tree-node-contextmenu" : ""))
				.attr("nodeid", nodeId)
				.attr("parentId", parentId);
			jq(c).addClass("tree-content");
			
			c.id = "div_" + nodeId;

			jq(ht).addClass("tree-headline-text");
			if (node) {
				div.title = node[nf.NAME];
				if (node[nf.CODE] != undefined) {
					codeDiv.appendChild(document.createTextNode(node[nf.CODE]));
				}
				d.renderNode(node, ht);
//				ht.appendChild(document.createTextNode(d[node[nf.CLASS_NAME]].getNameOf(node, ht) + " "));
//				d.setFreebimItemClasses(node, ht);
			}
			if (self.dropParam && mayEdit) {
				jq(ht).droppable(db.tree.dropParamOptions);	
			}
			if (self.dropEquality) {
				jq(ht).droppable(db.tree.dropEqualityOptions);	
			}
			if (self.dragEquality) {
				jq(ht).draggable(db.tree.dragOptions);	
			}
			if (childCount == 0) {
				// no children
				jq(div).addClass("tree-leaf");
				if (node)
					jq(div).addClass("tree-" + node[nf.CLASS_NAME]);
			}

			var btn = document.createElement("div");  // the headline's parameters: '[+]'
			jq(btn).addClass("tree-parameters").attr("title", i18n.g("TOGGLE_PARAMETER_VIEW")).attr("i18n_title", "TOGGLE_PARAMETER_VIEW");
			h.appendChild(btn);
			
			self.addParamBtn(btn, node);
			
			// show any equal nodes here
			h.appendChild(he);
			self.getEquals(he, node);
			
			if (net.spectroom.js.cookie.get(self.rootId + "_tree_" + nodeId) == "1") {
				self.getChildren(nodeId, true);
			}
			
			if (net.spectroom.js.cookie.get(self.rootId + "_param_" + nodeId) == "1") {
				jq(db.tree).trigger("getParameters", [{nodeId : nodeId, rootId: self.rootId}]);
			}
			
		};

		self.loadChildren = function (nodeId) {
			var parent = self.getNodeDiv(nodeId), cn = ((nodeId == null) ? "Library" : "Component");
			if (parent) {
				jq(parent).children(".tree-node").remove();
				if (nodeId == null) {
					nodeId = d.bbnId;
				}
				navSign = jq(parent).children(".tree-headline").children(".tree-navigation");
				d[cn].init();
				d.getOrLoad(nodeId, cn, function (node) {
					self.addChildren(parent, node, cn);
				});
			}
		};
		
		
		self.addParamBtn = function (btn, node) {
			var params = node[nf.HAS_PARAMETER];
			if (node && params && params.length > 0) {
				var id = node[nf.NODEID];
				jq(btn).show().html("+").off("click").click(function(event) {
					var c = jq(this).html();
					if (c == "-") {
						// parameter view is open now, close it
						net.spectroom.js.cookie.clear(self.rootId + "_param_" + id);
						jq(this).html("+");
						var div = jq(self.getNodeDiv(id)).children(".tree-parameters-content");
						jq(div).remove();
						return;
					} else {
						// parameter view is closed now, open it
						jq(db.tree).trigger("getParameters", [{nodeId : id, rootId: self.rootId}]);
					}
				});
				if (node.deleted && node.deleted == 1) {
					jq(btn).addClass(d.showDeleted);
				}
			} else {
				jq(btn).hide();
			}
		};
		
		self.addNavSign = function (h, childCount, id) { // h ... the complete headline div
			var hs = document.createElement("div"); // the '+' or '-' sign
			h.appendChild(hs);
			jq(hs).addClass("tree-navigation");
//			hs.id = self.rootId + "_" + "h_" + id;
			if (childCount > 0) {
				// has children
				hs.appendChild(document.createTextNode("►"));
				jq(hs).off("click").click(function(event) {
					self.getChildren(id);
				});
			} else {
				// no children
				hs.appendChild(document.createTextNode(" "));
			}
		};
		
		self.getChildren = function (nodeId, forceOpen) {
			if (nodeId) {
				var parent = self.getNodeDiv(nodeId), navSign;
				if (parent) {
					navSign = jq(parent).children(".tree-headline").children(".tree-navigation");
					if (navSign) {
						if (forceOpen && jq(navSign).html() != " ") {
							jq(navSign).html("►");
						}
						if (jq(navSign).html() == "▼") {
							// it's open now, close it ...
							jq(navSign).html("►");
							jq(parent).children(".tree-node").remove();
							net.spectroom.js.cookie.clear(self.rootId + "_tree_" + nodeId);
							return;
						} else if (jq(navSign).html() == "►") {
							// it's closed now, open it ...
							net.spectroom.js.cookie.set(self.rootId + "_tree_" + nodeId, "1", 14);
							jq(navSign).html("▼");
							self.loadChildren(nodeId);
						}
					}
				}
			} else {
				// load children of BigBangNode ...
				self.loadChildren(null);
			}
		};
		
		self.getNodeDiv = function(nodeId) {
			var selector = "#" + self.rootId;
			if (nodeId) {
				return jq(selector).find(".tree-node[nodeid=\"" + nodeId + "\"]");
			} else {
				return jq(selector);
			}
		};
		
		self.getParameterBtn = function(nodeId) {
			var headline, btn, nodeDiv = self.getNodeDiv(nodeId);
			headline = jq(nodeDiv).children(".tree-headline");
			btn = jq(headline).children(".tree-parameters");
			return btn;
		};
		
		self.createParameterTable = function (nodeId) {

			d.Parameter.init();
			var node = d.get(nodeId),
				div = document.createElement("div"), 
				nodeDiv = self.getNodeDiv(nodeId), 
				headline = jq(nodeDiv).children(".tree-headline"),
				params = ((node) ? node[nf.HAS_PARAMETER] : null),
				btn = self.getParameterBtn(nodeId);
			
			var old = jq(nodeDiv).children(".tree-parameters-content");
			if (old) {
				jq(old).remove();
			}
			if (node && params && params.length > 0) {
				d.rel.HasParameter.init();
				jq(btn).html("-");
				jq(div).addClass(".tree-parameters-content");
				
				jq(headline).after(div);

				var ptableDiv = document.createElement("div");
				div.appendChild(ptableDiv);
				ptableDiv.id = self.rootId + "_parameters_table_" + nodeId;
				jq(ptableDiv).addClass("tree-parameters-content").attr("nodeid", nodeId);
				jq("#" + self.rootId).find("#div_" + nodeId).before(ptableDiv);
				if (params.length > 0) {
					
					var t = Object.create(net.spectroom.js.table);
					t.initialSort = 2;
					t.dataCallback = function(row, tr) {
						if (!params || !params[row]) return;
						var rowNodeId = params[row][rf.TO_NODE], rowNode = d.get(rowNodeId);
						if (rowNode) {
							rowNode[rf.ORDERING] = params[row][rf.ORDERING] * 1;
							return rowNode;
						} else {
							jq(tr).attr("nodeid", rowNodeId);
							jq(document).trigger("getNode", [{nodeId: rowNodeId, cn: "Parameter"}]);
							return null;
						}
					};
					t.idCol = "id";
					t.rowCount = params.length;
					t.initialSort = 1;
					t.cols = [{
								label : "PLIST_NR",
								field : rf.ORDERING,
								type : "text",
								sort : true
							}/*, {
								label : "CODE",
								field : nf.CODE,
								type : "text",
								sort : true
							}*/,
							{
								label : "NAME",
								field : "name",
								type : "custom",
								createCell : function (tr, td, row, col) {
									var rel = params[row], okFn = function (ph) {
										var rowNodeId = params[row][rf.TO_NODE], 
											rowNode = d.get(rowNodeId),
											div = document.createElement("div"),
											s = ((rowNode && rowNode[nf.MEASURES] && rowNode[nf.MEASURES].length > 0) ? "+" : "");
										jq(td).empty().append(div);
										
										
										var sub = document.createElement("sub");
										jq(sub).attr("abbr", "1");
										d.renderNode(ph, sub);
										jq(sub).removeClass("freebim-contextmenu");
										
										
										
										d.renderNode(rowNode, div, tr);
										div.appendChild(sub);
										if (self.dragParam && d.mayEdit(rowNode)) {
											jq(div).draggable(db.tree.dragOptions);
										}
										if (self.dropEquality) {
											jq(div).droppable(db.tree.dropEqualityOptions);	
										}
										if (self.dragEquality) {
											jq(div).draggable(db.tree.dragOptions);	
										}
										if (db.contributor) {
											if (s == "+") {
												// show measures
												var mtdiv = document.createElement("div"),
													mdiv = document.createElement("div");
												jq(td).append(mtdiv);
												jq(td).append(mdiv);
												jq(mdiv).addClass("child");
												jq(mtdiv).html(s).addClass("measure-toggle");
											}
										}
									};
									jq(d.rel.HasParameter).trigger("phaseFromRel", [{rel: rel, okFn: okFn}]);
								},
								sortValue : function (row) {
									var v = t.dataCallback(row);
									return d.Parameter.getNameOf(v);
								},
								sort : true
							},
							{
								label : "DESCRIPTION",
								field : nf.DESC,
								type : "custom",
								createCell : function (tr, td, row, col) {
									var rel = params[row], rowNodeId = rel[rf.TO_NODE], 
									rowNode = d.get(rowNodeId),
									desc = d.Parameter.getDescOf(rowNode);
									jq(td).html(net.spectroom.js.shrinkText(desc, 64, td)).addClass("freebim-desc").attr("nodeid", rowNode[nf.NODEID]);
								},
								maxlen : 64,
								sort : false
							},
							{
								label : "CLAZZ_PARAMETERTYPE",
								field : nf.PARAM_TYPE,
								type : "callback",
								data : d.ParameterType.data,
								sort : true
							},
							d.columns.state
						];
					t.cellCreatedTimeout = null;
					t.cellCreated = function (data) {
						var td = data.td;
						if (jq(td).hasClass("freebim-item")) {
							var nodeId = jq(data.td).attr("nodeid") || jq(data.tr).attr("nodeid"),
								node = d.get(nodeId);
							d.setFreebimItemClasses(node, td);
						}
						if (t.cellCreatedTimeout) {
							clearTimeout(t.cellCreatedTimeout);
						}
						t.cellCreatedTimeout = setTimeout(function() {
							db.user.doShowAbstract();
						}, 200);
					};
					jq(document).on("freebimItemDisplayChanged", function (event, data) { jq(t).trigger("cellChanged"); });
					t.rowCreated = function (data) {
						var table = data.table, row = data.row, tr = data.tr, node = table.dataCallback(row);
						if (node) {
							jq(tr).attr("nodeid", node[nf.NODEID]);
//							jq(document).off("Parameter_got_" + node[nf.NODEID]).one("Parameter_got_" + node[nf.NODEID], function(event, data) {
//								db.logger.debug("tree one Parameter_got_" + node[nf.NODEID]);
//								table.createRow(row, tr); 
//							});
							if (node.deleted && node.deleted == 1) {
								jq(tr).addClass(d.showDeleted);
							}
							if (node[nf.TYPE] && node[nf.TYPE] == 2) {
								jq(tr).addClass("abstract");
							}
						}
					};
					t.create(t, ptableDiv, d.Parameter, d.Parameter);
				}

				jq("#" + self.rootId + "_parameters_table_" + nodeId).addClass("vis").addClass("loaded");
			} else {
				jq(btn).remove();
			}
		};
		
		self.renderEqualNode = function (otherId, div, eqSign) {
			d.getOrLoad(otherId, d.Component.className, function (node) {
				var htName,
					esDiv = document.createElement("div"),
					nodeDiv = document.createElement("div");
				jq(div).empty();
				jq(div).append(esDiv).append(nodeDiv);
				jq(esDiv).append(document.createTextNode(eqSign)).addClass("equalSign");
				if (node.deleted) {
					jq(div).addClass(d.showDeleted);
				} else {
					jq(div).removeClass(d.showDeleted);
				}
				d.renderNode(node, nodeDiv);
			});
		};
				
		self.getEquals = function (eqDiv, srcNode) {
			jq(eqDiv).addClass("tree-equals");
			if (srcNode && srcNode[nf.EQUALS]) {
				var i, rel, otherId, eqSign, div, n = srcNode[nf.EQUALS].length;
				for (i=0; i<n; i++) {
					div = document.createElement("div");
					rel = srcNode[nf.EQUALS][i];
					otherId = ((rel[rf.FROM_NODE] == srcNode[nf.NODEID]) ? rel[rf.TO_NODE] : rel[rf.FROM_NODE]);
					eqSign = d.rel.Equals.eqSign(rel);
					jq(div).addClass("tree-equal");
					jq(eqDiv).append(div);
					self.renderEqualNode(otherId, div, eqSign);
				}
			}
		};
	},
	
	dropEqualityOptions : {
		accept: ".Component,.Parameter,.Measure,.ValueList,.ValueListEntry",
		hoverClass: "drop-hover",
		tolerance: "pointer",
		drop: function(event, ui) {
//			console.log("dropped");
			var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n
				srcCompId = jq(ui.draggable).attr("nodeid"), // id of dragged node
				srcComp = d.get(srcCompId),
				compId = jq(this).attr("nodeid"), // id of target node
				comp = d.get(compId), msg = "";
			if (srcCompId == compId) {
				jq(document).trigger("alert", [{ title: "DLG_TITLE_FREEBIM_INFO", content: i18n.get("SELECTED_ELEMENTS_ARE_EQ") }]);
				return;
			}
			if (srcComp && comp) {
				var areEqual = false;
				if (srcComp[nf.EQUALS]) {
					var r, i, n = srcComp[nf.EQUALS].length;
					for (i=0; i<n; i++) {
						r = srcComp[nf.EQUALS][i]; // r is an Equals relation
						if ((r[rf.FROM_NODE] == srcCompId && r[rf.TO_NODE] == compId)
								|| (r[rf.TO_NODE] == srcCompId && r[rf.FROM_NODE] == compId)) {
							areEqual = true;
							break;
						}
					}
				}
				if (!areEqual) {
					if (comp[nf.EQUALS]) {
						var r, i, n = comp[nf.EQUALS].length;
						for (i=0; i<n; i++) {
							r = comp[nf.EQUALS][i]; // r is an Equals relation
							if ((r[rf.FROM_NODE] == srcCompId && r[rf.TO_NODE] == compId)
									|| (r[rf.TO_NODE] == srcCompId && r[rf.FROM_NODE] == compId)) {
								areEqual = true;
								break;
							}
						}
					}
				}
				if (areEqual) {
					jq(document).trigger("alert", [{ title: "DLG_TITLE_FREEBIM_INFO", content: i18n.get("SELECTED_ELEMENTS_ARE_EQ") }]);
					return;
				}
				
				// ask before save ...
				msg = i18n.get("CONFIRM_SET_EQ");
//				msg += "(" + d[srcComp[nf.CLASS_NAME]].title + ") ";
//				msg += "";
				msg += "<div class='freebim-item info-only " + srcComp[nf.CLASS_NAME] + "'>" + srcComp[nf.NAME] + "</div>";
//				msg += "(" + d[comp[nf.CLASS_NAME]].title + ") ";
				msg += "<div class='freebim-item info-only " + comp[nf.CLASS_NAME] + "'>" + comp[nf.NAME] + "</div>";
				
				jq(document).trigger("confirm", [{
					msg: msg,
					yes: function() {
						d.addModifiedNode(srcCompId);
						d.addModifiedNode(compId);
						if (srcComp) {
							if (!comp[nf.EQUALS]) {
								comp[nf.EQUALS] = [];
							}
							// create new Equals relation
							r = {};
							r[rf.TYPE] = d.RelationTypeEnum.EQUALS;
							r[rf.ID] = null;
							r[rf.FROM_NODE] = srcCompId;
							r[rf.TO_NODE] = compId;
							r[rf.QUALITY] = 1;
							r[rf.FROM_CLASS] = ((srcComp[nf.CLASS_NAME] == comp[nf.CLASS_NAME]) ? undefined : srcComp[nf.CLASS_NAME]); // crossClass
							r[rf.TO_CLASS] = ((srcComp[nf.CLASS_NAME] == comp[nf.CLASS_NAME]) ? undefined : comp[nf.CLASS_NAME]); // crossClass
							
							comp[nf.EQUALS].push(r);
							// save the target component:
							jq(document).trigger("_save", [{entity: comp}]);
						}
					}
				}]);
			}
		}
	},
	
	dropParamOptions : {
		accept: ".Parameter",
		hoverClass: "drop-hover",
		tolerance: "pointer",
		drop: function(event, ui) {
//			console.log("dropped");
			var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n,
				paramId = jq(ui.draggable).attr("nodeid"), // id of dragged Parameter
				param = d.get(paramId),
				compId = jq(this).attr("nodeid"), // id of target Component
				comp = d.get(compId), msg = "";
			if (param && comp) {
				// ask before save ...
				
				msg += i18n.get("MOVE_PARAMETER_1"); // <p>wollen Sie den Parameter</p>";
				msg += "<div class='freebim-item info-only Parameter'>" + param[nf.NAME] + "</div>";
				msg += i18n.get("MOVE_PARAMETER_2"); // "<p>wirklich der Komponente</p>";
				msg += "<div class='freebim-item info-only Component'>" + comp[nf.NAME] + "</div>";
				msg += i18n.get("MOVE_PARAMETER_3"); // "<p>zuweisen?</p>";
				
				jq(document).trigger("confirm", [{
					msg: msg,
					yes: function() {
						var srcComponentId = jq("#root2").find(".Parameter[nodeid='" + paramId + "']").parentsUntil(".tree-node").parent().attr("nodeid"),
						srcComponent = d.get(srcComponentId), params = srcComponent[nf.HAS_PARAMETER];
						
						d.addModifiedNode(srcComponentId);
						d.addModifiedNode(compId);
						
						// remove parameter from source component:
						if (srcComponent && params) {
							var p = "", r, i, n = params.length, rn = []; // rn ... new parameter relations
							for (i=0; i<n; i++) {
								r = params[i];
								if (r[rf.TO_NODE] == paramId) {
									// ignore dragged parameter relation
									p = r[rf.PHASE]; // remember phase assignment
									continue;
								}
								// add existing relation
								rn.push(r);
							}
							srcComponent[nf.HAS_PARAMETER] = rn;
							
							// add parameter to target component:
							params = comp[nf.HAS_PARAMETER];
							if (!params) {
								params = [];
							}
							// create new parameter relation
							r = {};
							r[rf.TYPE] = d.RelationTypeEnum.HAS_PARAMETER;
							r[rf.ID] = null;
							r[rf.FROM_NODE] = compId;
							r[rf.TO_NODE] = paramId;
							r[rf.ORDERING] = params.length;
							r[rf.PHASE] = p;
							params.push(r);
							comp[nf.HAS_PARAMETER] = params;
							
							// save the target component:
							jq(document).trigger("_save", [{entity: comp}]);
							
							// save the source component:
							jq(document).trigger("_save", [{entity: srcComponent}]);
						}
					}
				}]);
			}
		}
	},
	
	dragOptions : {
		stop : function ( event, ui ) {
//			console.log("drag stopped.");
		},
		scroll: false,
		helper: function (e,ui) {
	      	return jq(this).clone().appendTo('body').css('zIndex',5).show();
	   	}
	},
	
	openInTreeTimeout : null,
	
	openInTree : function (node, src, parentIds, level) {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields;
		if (!node || !db.tree.structure) {
			return;
		}
		if (node[nf.CLASS_NAME] == d.Parameter.className) {
			var rels = node[nf.HP], i, n = rels.length;
			for (i=0; i<n; i++) {
				var r = rels[i], // r is a HasParameter relation
					compId = r[rf.FROM_NODE];
				d.getOrLoad(compId, d.Component.className, function (comp) {
					db.tree.structure.openInTree(comp, src);
				});
			}
			return;
		}
		if (!parentIds) {
			parentIds = {};
		}
		if (!level) {
			level = 0;
		}
		var loadParent = function (parentId) {
			d.getOrLoad(parentId, node[nf.CLASS_NAME], function (parent) {
				db.tree.openInTree(parent, src, parentIds, level + 1);
			});
		};

		jq(".tree-node").css("border", "");
		jq("#" + db.tree.structure.rootId + " .jsTableRow-selected").removeClass("jsTableRow-selected");
		var r = node[nf.PARENTS];
		if (r && r.length > 0) {
			// r is an array of ChildOf relations
			var parentId = r[0][rf.FROM_NODE];
			parentIds["" + level] = parentId;
			loadParent(parentId);
		} else {
			// no more parents --> BigBangNode ...
			if (db.tree.openInTreeTimeout) {
				clearTimeout(db.tree.openInTreeTimeout);
			}
			db.tree.openInTreeTimeout = setTimeout(function () {
				var i, k, keys = Object.keys(parentIds), n = keys.length;
				for (i=n-1; i>=0; i--) {
					k = keys[i];
					db.tree.structure.getChildren(parentIds[k], true);
				}
				var e = null;
				if (src[nf.CLASS_NAME] == d.Parameter.className) {
					var rels = src[nf.HP], i, n = rels.length;
					for (i=0; i<n; i++) {
						var r = rels[i], // r is a HasParameter relation
							compId = r[rf.FROM_NODE];
						jq(db.tree).trigger("getParameters", [{nodeId : compId, rootId: db.tree.structure.rootId}]);
						e = jq("#" + db.tree.structure.rootId + " div[nodeid='" +  + compId + "'].tree-node");
						if (e) { 
							e.css("border", "solid 2px orange");
						}
					}
				} else {
					e = jq("#" + db.tree.structure.rootId + " div[nodeid='" +  + src[nf.NODEID] + "'].tree-node");
					if (e) { 
						e.css("border", "solid 2px orange");
					}
				}
				if (e && e.length > 0) { 
					e[0].scrollIntoView();
				}
				db.tree.openInTreeTimeout = null;
			}, 200);
		}
		
		
	}

};
