/**
 * 
 */
at.freebim.db.relations = {

	activeNodeId: null,

	init: function () {
		var db = at.freebim.db, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, rte = d.RelationTypeEnum, i18n = net.spectroom.js.i18n;
		self = db.relations;
		self.nodes = [];

		if (self.initialized) {
			return;
		}
		self.initialized = true;

		jq(document).delegate(".freebim-history-item", "click", function (event, data) {
			if (event.altKey || event.ctrlKey || event.metaKey) {
				return;
			}
			try {
				var x = jq(event.target);
				if (!x.attr("rel-history")) {
					x = x.closest("*[rel-history]");
				}
				var idx = x.attr("rel-history"),
					nodeId = self.historyArr[idx],
					i, newHistory = [];
				for (i = 0; i < idx; i++) {
					newHistory.push(self.historyArr[i]);
				}
				self.historyArr = newHistory;
				self.btnClicked(nodeId);
			} catch (e) {
				db.logger.error("Error in showHistory: " + e.message);
				self.historyArr = [d.bbnId];
				self.showHistory();
			}
		});

		jq(document).delegate(".freebim-related-item", "click", function (event, data) {
			if (event.altKey || event.ctrlKey || event.metaKey) {
				return;
			}
			var nodeId = jq(this).attr("nodeid");
			if (nodeId) {
				self.btnClicked(nodeId);
			}
		});

		jq(db.relations).on("showNode", function (event, data) {
			db.logger.debug("relations: on showNode");
			if (!d.bbnId) {
				jq(db.relations).one("nodeShowing", function (event, data2) {
					self.btnClicked(data.nodeId);
				});
				d.loadBbn();
				return;
			}
			self.btnClicked(data.nodeId);
		});

		self.nodeDetails = function (parent, node) {
			var i, o, key, keys = Object.keys(node), n = keys.length;
			for (i = 0; i < n; i++) {
				key = keys[i];
				o = node[key];
				if (o instanceof Object || o instanceof Function) {
					continue;
				}
				parent.appendChild(document.createElement("br"));
				parent.appendChild(document.createTextNode(key));
				parent.appendChild(document.createTextNode(" = "));
				parent.appendChild(document.createTextNode(o));
			}
		};
		self.historyArr = [];
		self.showHistory = function () {
			var b, i, n = self.historyArr.length, div = document.createElement("div");
			jq(self.history).empty();
			jq(div).append(i18n.get("HISTORY") + ":"); // document.createTextNode("Verlauf: "));
			self.history.appendChild(div);
			for (i = 0; i < n; i++) {
				var node = self.get(self.historyArr[i]),
					name = "";
				b = document.createElement("div");
				jq(b).attr("rel-history", "" + i);
				self.history.appendChild(b);

				d.renderNode(node, b);


				//				if (d.bbnId == node[nf.NODEID]) {
				//	  				name = "☀";
				//	  				jq(b).addClass("BigBangNode no-edit");
				//	  			} else {
				//	  				if (d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].getNameOf) {
				//	  					name = d[node[nf.CLASS_NAME]].getNameOf(node, b);
				//	  				}
				//	  				jq(b).addClass(node[nf.CLASS_NAME]);
				//	  			}
				//				b.appendChild(document.createTextNode(name));
				jq(b).off("click").addClass("no-edit");
				//				d.setFreebimItemClasses(node, b);
				jq(b).addClass("freebim-history-item");
			}
			if (b) {
				b.scrollIntoView(true);
			}
		};

		self.addInfo = function (relDiv, i) {
			if (i != undefined) {
				var sub = document.createElement("sub");
				sub.appendChild(document.createTextNode(" ℹ " + i));
				relDiv.appendChild(sub);
			}
		};

		self.loadRelations = function (nodeId) {
			db.logger.debug("relations: loadRelations: " + nodeId);

			jq(document).trigger("show_progress", [{ key: "Relations_load", msg: i18n.g("LOADING_RELATIONS") }]);
			at.freebim.db.request.get("/relations/load", { nodeId: nodeId }).then((response) => { 
				jq(document).trigger("hide_progress", [{ key: "Relations_load" }]);
				jq(self.left).empty();
				jq(self.right).empty();
				d.rel.rels = {};
				if (response.error != undefined) {
					alert(response.error);
				} else {
					var arr = response.result, // array of BaseNode's
						i, n = arr.length, parent;
					arr.sort(function (a, b) {
						if (a != undefined && b != undefined) {
							if (a.relation != undefined && b.relation != undefined && a.relation[rf.TIMESTAMP] != undefined && b.relation[rf.TIMESTAMP] != undefined) {
								// sort by timestamp
								if (a.relation[rf.TIMESTAMP] != b.relation[rf.TIMESTAMP]) {
									return (a.relation[rf.TIMESTAMP] - b.relation[rf.TIMESTAMP]);
								}
							}
							// first, sort by class name:
							if (a.className < b.className) {
								return -1;
							} else if (a.className > b.className) {
								return 1;
							} else if (a.relation != undefined && b.relation != undefined) {
								// equal class name, sort by relation type:
								if (a.relation[rf.TYPE] < b.relation[rf.TYPE]) {
									return -1;
								} else if (a.relation[rf.TYPE] > b.relation[rf.TYPE]) {
									return 1;
								} else {
									// equal class name, equal relation type, sort by ordering (if any)
									var o1 = a.relation[rf.ORDERING], o2 = b.relation[rf.ORDERING];
									if (o1 != undefined && o2 != undefined) {
										// equal class name, equal relation type, sort by ordering
										if (o1 != o2) {
											return (o1 - o2);
										} else {
											// equal class name, equal relation type, equal ordering, sort by name
											d[a.className].init();
											d[b.className].init();
											var aName = d[a.className].getNameOf(a.node),
												bName = d[b.className].getNameOf(b.node);
											if (aName < bName) {
												return -1;
											} else if (aName > bName) {
												return 1;
											} else {
												return 0;
											}
										}
									}
								}
							}
						}
					});
					// set the relations
					for (i = 0; i < n; i++) {
						var res = arr[i], dir = res.dir;
						if (res) {
							var bn = res.node, t, target, target2,
								b = document.createElement("div"),
								relDiv = document.createElement("div"),
								d1 = document.createElement("div"),
								addOrdering = function (res, relDiv) {
									if (res.relation[rf.ORDERING] != undefined) {
										var sub = document.createElement("sub");
										sub.appendChild(document.createTextNode(" " + res.relation[rf.ORDERING]));
										relDiv.appendChild(sub);
									}
								};
							//db.logger.debug("JUMPED OVER LISTEN");
							self.listen(bn);
							if (res.relation) {
								t = res.relation[rf.TYPE];
							}
							else {
								continue;
							}
							d.rel.rels[res.relation[rf.ID]] = res.relation;

							switch (dir) {
								case "IN": target = self.left; target2 = self.right; break;
								case "OUT": target = self.right; target2 = self.left; break;
							}
							switch (t) {
								default:
								case rte.VALUE_OVERRIDE:
									continue;
								case rte.COMPANY_COMPANY:
								case rte.WORKS_FOR:
								case rte.RESPONSIBLE:
									if (!db.contributor || !db.contributor.id) {
										continue;
									}
									parent = target;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									addOrdering(res, relDiv);
									break;
								case rte.REFERENCES:
									if (!db.contributor || !db.contributor.id) {
										continue;
									}
									parent = target;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									if (res.relation[rf.REF_ID_NAME] != undefined && res.relation[rf.REF_ID] != undefined) {
										var sub = document.createElement("sub");
										sub.appendChild(document.createTextNode(" " + res.relation[rf.REF_ID_NAME] + "[" + res.relation[rf.REF_ID] + "]"));
										relDiv.appendChild(sub);
									}
									break;
								case rte.PARENT_OF:
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									addOrdering(res, relDiv);
									parent = target;
									break;
								case rte.HAS_PARAMETER:
									parent = target;
									jq(relDiv).append("<span> → </span>")
										.append(i18n.get_0(d.RelationType[t].uiName));
									addOrdering(res, relDiv);
									var phInfo = document.createElement("sub");
									relDiv.appendChild(phInfo);
									self.addPhaseInfo(phInfo, res.relation);
									break;
								case rte.DOCUMENTED_IN:
									if (!db.contributor || !db.contributor.id) {
										continue;
									}
									parent = target;
									jq(relDiv).append("<span> → ✍</span>");
									if (res.relation[rf.TIMESTAMP] != undefined) {
										var sub = document.createElement("sub");
										sub.appendChild(document.createTextNode(" " + db.time.formatISO(res.relation[rf.TIMESTAMP])));
										relDiv.appendChild(sub);
									}
									break;
								case rte.CONTRIBUTED_BY:
									if (!db.contributor || !db.contributor.id) {
										continue;
									}
									parent = target;
									var ct;
									if (res.relation[rf.CONTRIBUTION_TYPE] != undefined) {
										switch (res.relation[rf.CONTRIBUTION_TYPE]) {
											default:
											case d.ContributionType.UNDEFINED: ct = "?"; break;
											case d.ContributionType.CREATE: ct = "✚"; break;
											case d.ContributionType.MODIFY: ct = "✎"; break;
											case d.ContributionType.DELETE: ct = "☓"; break;
										}
									}
									jq(relDiv).append("<span> → " + ct + "</span>")
									if (res.relation[rf.TIMESTAMP] != undefined) {
										var sub = document.createElement("sub");
										sub.appendChild(document.createTextNode(" " + db.time.formatISO(res.relation[rf.TIMESTAMP])));
										relDiv.appendChild(sub);
									}
									break;
								case rte.HAS_ENTRY:
									parent = target;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									addOrdering(res, relDiv);
									break;
								case rte.COMP_COMP:
								case rte.HAS_MEASURE:
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									addOrdering(res, relDiv);
									parent = target;
									break;
								case rte.HAS_VALUE:
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									if (res.relation[rf.COMPONENT] != undefined) {
										var compInfo = document.createElement("sub");
										relDiv.appendChild(compInfo);
										self.addComponentInfo(compInfo, res.relation);
									}
									parent = target;
									break;
								case rte.HAS_PARAMETER_SET:
								case rte.OF_DATATYPE:
								case rte.OF_DISCIPLINE:
								case rte.OF_MATERIAL:
								case rte.OF_UNIT: // OF_UNIT
								case rte.VALUELIST_OF_COMPONENT:
								case rte.BELONGS_TO:
								case rte.CONTAINS_PARAMETER:
									parent = target;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									break;
								case rte.EQUALS:
									parent = self.center;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									if (res.relation[rf.QUALITY] != undefined) {
										var sub = document.createElement("sub");
										sub.appendChild(document.createTextNode(" " + Math.floor(res.relation[rf.QUALITY] * 100.) + "%"));
										relDiv.appendChild(sub);
									}
									break;
								case rte.UNIT_CONVERSION:
									parent = self.center;
									jq(relDiv).append("<span> → </span>").append(i18n.get_0(d.RelationType[t].uiName));
									if (res.relation[rf.QUALITY] != undefined) {
										var okFn = function (txt) {
											var sub = document.createElement("sub");
											sub.appendChild(document.createTextNode(" " + txt));
											jq(sub).appendTo(relDiv);
										};
										d.rel.UnitConversion.init();
										jq(d.rel.UnitConversion).trigger("unitConversionRel", [{ rel: res.relation, okFn: okFn }]);
									}
									break;
							}

							// relation may contain additional ℹ information:︎
							self.addInfo(relDiv, res.relation[rf.INFO]);

							d1.appendChild(relDiv);
							d1.appendChild(b);
							if (parent == self.left) {
								jq(relDiv).addClass("rel-left");
							} else if (parent == self.center) {
								jq(relDiv).addClass("rel-center");
							} else {
								jq(relDiv).addClass("rel-right");
							}
							jq(relDiv).addClass("freebim-relation" + " type-" + t)
								.attr("relid", res.relation[rf.ID])
								.append("<span> → </span>");
							parent.appendChild(d1);
							jq(d1).addClass("rel");
							jq(b).addClass("no-edit freebim-related-item").attr("maxLength", 42);
							d.renderNode(bn, b, relDiv);
						}
					}
					self.showHistory();
				}
				self.position();
				jq(db.relations).trigger("nodeShowing", [{ nodeId: nodeId }]);
			}
			).catch((error) => {
				jq(document).trigger("hide_progress", [{ key: "Relations_load" }]);
				var msg = ((error) ? ((error.status) ? error.status + " " : "") + ((error.statusText) ? error.statusText + " " : "") : i18n.g("ERROR"));
				alert(msg);
			});
		};

		self.addPhaseInfo = function (phDiv, rel) {
			var okFn = function (ph) {
				jq(phDiv).attr("abbr", "1");
				d.renderNode(ph, phDiv);
				jq(phDiv).addClass("info-only");
			};
			d.rel.HasParameter.init();
			jq(d.rel.HasParameter).trigger("phaseFromRel", [{ rel: rel, okFn: okFn }]);
		};

		self.addComponentInfo = function (cDiv, rel) {
			var okFn = function (c) {
				d.renderNode(c, cDiv);
				jq(cDiv).addClass("info-only");
			};
			d.rel.HasValue.init();
			jq(d.rel.HasValue).trigger("componentFromRel", [{ rel: rel, okFn: okFn }]);
		};

		self.btnClicked = function (nodeId) {
			db.logger.debug("relations: btnClicked: " + nodeId);

			var b = document.createElement("div"),
				node = self.get(nodeId);

			if (node == null) {
				node = d.get(nodeId);
			}

			// set the center (the node that has been clicked)
			if (self.historyArr) {
				if (self.historyArr.length == 0 || self.historyArr[self.historyArr.length - 1] != nodeId) {
					self.historyArr.push(nodeId);
				}
			}
			jq(self.center).empty();
			jq(b).addClass("current freebim-item freebim-contextmenu").attr("nodeid", node[nf.NODEID]);
			if (db.isMarked(node[nf.NODEID])) {
				jq(b).addClass("freebim-marked");
			}
			self.center.appendChild(b);
			if (d[node[nf.CLASS_NAME]] && d[node[nf.CLASS_NAME]].i18n) {
				self.center.appendChild(document.createTextNode(i18n.g(d[node[nf.CLASS_NAME]].i18n)));
			}

			d.renderNode(node, b);

			self.loadRelations(nodeId);
		};

		self.position = function () {
			if (self.positionTimer) {
				clearTimeout(self.positionTimer);
			}
			self.positionTimer = setTimeout(function () {

				var tab = jq("#freebim_rel"), relData = jq(".freebim-rel-data"), rels = jq(".freebim-rels");
				jq(self.center).position({
					my: "center center",
					at: "center center",
					of: rels
				});
				jq(self.left).position({
					my: "right center",
					at: "left-5 center",
					of: self.center
				});
				jq(self.right).position({
					my: "left center",
					at: "right+5 center",
					of: self.center
				});

				//				self.center.scrollIntoView(true);
			}, 100);
		};

		self.listen = function (e) {
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
				self.nodes["" + e[nf.NODEID]] = e;
				jq("#status").trigger("_update");
				d.cleanupNode(e);
			}
		};

		self.get = function (nodeId) {
			return self.nodes["" + nodeId];
		},

		self.setup = function () {
			var tab = jq("#freebim_rel"),
				relData = document.createElement("div"),
				rels = document.createElement("div");

			tab.empty();
			self.left = document.createElement("div");
			self.center = document.createElement("div");
			self.right = document.createElement("div");
			self.history = document.createElement("div");
			jq(self.history).addClass("freebim-rel-history");
			tab.append(self.history);
			tab.append(relData);
			jq(relData).append(rels).addClass("freebim-rel-data");
			jq(self.left).addClass("freebim-rel-incoming");
			jq(self.center).addClass("freebim-rel-current").css("text-align", "center");
			jq(self.right).addClass("freebim-rel-outgoing");
			jq(rels).append(self.left).append(self.center).append(self.right).addClass("freebim-rels");
		};

		self.setup();
		self.btnClicked(d.bbnId);
	}

};
