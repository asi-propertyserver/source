/**
 * 
 */
at.freebim.db.searches = {};

at.freebim.db.search = {
		
		
	create : function () {
		
		/*
		<input type="radio" name="searchtype" value="Named" checked="true"> Name<br />
		<input type="radio" name="searchtype" value="Described"> Beschreibung<br />
		<input type="radio" name="searchtype" value="Coded"> Code<br />
		<input type="text" id="searchfield" name="searchfield"> <button id="searchbutton" >Suchen</button>
		 */
		var self = this, parent = jq("#" + self.mainDivId)[0], 
			ip, i18n = net.spectroom.js.i18n;
		jq(parent).empty();

		self.activate = function () {
			if (self.searchField) {
				self.searchField.focus();
			}
		};

		if (self.type["Named"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "Named");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("NAME"))
				.append(document.createElement("br"));
			self.type["Named"] = ip;
		}
		if (self.type["Described"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "Described");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("DESCRIPTION"))
				.append(document.createElement("br"));
			self.type["Described"] = ip;
		}
		if (self.type["Coded"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "Coded");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("CODE"))
				.append(document.createElement("br"));
			self.type["Coded"] = ip;
		}
		if (self.type["freebim-id"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "freebim-id");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("FREEBIM_ID"))
				.append(document.createElement("br"));
			self.type["freebim-id"] = ip;
		}
		if (self.type["bsdd-guid"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "bsdd-guid");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("BSDD_GUID"))
				.append(document.createElement("br"));
			self.type["bsdd-guid"] = ip;
		}
		if (self.type["state"]) { 
			ip = document.createElement("input");
			jq(ip).attr("type", "radio")
				.attr("name", "searchtype")
				.attr("value", "state");
			jq(parent).append(ip)
				.append(" ")
				.append(i18n.get("STATUS"))
				.append(" ");
			self.type["state"] = ip;
			ip = document.createElement("select");
			ip.id = self.mainDivId + "_statesearch";
			if (at.freebim.db.domain.State) {
				var i, n = at.freebim.db.domain.State.values.length;
				for (i=0; i<n; i++) {
					var opt = document.createElement("option");
					jq(opt).attr("value", at.freebim.db.domain.State.values[i])
						.append(at.freebim.db.domain.State.data(at.freebim.db.domain.State.values[i]))
						.appendTo(ip);
				}
			}
			parent.appendChild(ip);
			jq(ip).hide();
		}
		jq("#" + self.mainDivId + " input:radio[name=searchtype]").change(function() {
			var val = jq(this).val();
			switch (val) {
			case "state" : 
				jq("#" + self.mainDivId + "_statesearch").show();
				jq(self.searchField).hide();
				break;
			default :
				jq("#" + self.mainDivId + "_statesearch").hide();
				jq(self.searchField).show();
				self.activate();
				break;
			}
		});
		
		if (self.type["Named"])
			self.type["Named"].checked = true;
		else if (self.type["Described"])
			self.type["Described"].checked = true;
		else if (self.type["Coded"])
			self.type["Coded"].checked = true;

		parent.appendChild(document.createElement("br"));

		self.searchField = document.createElement("input");
		self.searchField.setAttribute("type", "text");
		jq(self.searchField).addClass("searchfield");
		self.searchField.setAttribute("name", "searchfield");
		parent.appendChild(self.searchField);
		jq(self.searchField).keyup(function(event) {
			// console.log(event.keyCode);
			if (event.keyCode == 13) // start search when user hits ENTER 
				self.search();
		});
		
		ip = document.createElement("button");
		jq(ip).addClass("searchbutton")
			.appendTo(parent)
			.append(i18n.get("SEARCH"))
			.off("click")
			.click(function () {
				self.search();
			});
		
		ip = document.createElement("div");
		jq(ip).attr("id", self.mainDivId + "_searchresult")
			.addClass("searchresult")
			.appendTo(parent);
	},
	
	init : function (mainDivId, type) {
		
		var self = this, rf = at.freebim.db.domain.RelationFields, nf = at.freebim.db.domain.NodeFields, i18n = net.spectroom.js.i18n;
		if (self.initialized) {
			return;
		}
		self.type = type || { Named: true, Described : true, Coded : true };
		
		self.setType = function (type) {
			self.type = type;
			self.create();
		};
		self.mainDivId = mainDivId;
		
		self.create();
		
		self.search = function search() {
			var s = jq("#" + self.mainDivId + " .searchfield").val(), d = jq("#" + self.mainDivId + " .searchresult")[0];
			var msg = i18n.g1("SEARCHING", s);
			var searchtype = jq("#" + self.mainDivId + " input:radio[name=searchtype]:checked").val();
			if (searchtype == "state") {
				s = jq("#" + self.mainDivId + "_statesearch").val();
				msg = i18n.g1("SEARCHING", at.freebim.db.domain.State.data(s * 1));
			}
			jq(document).trigger("show_progress", [{key:"Search_load", msg: msg}]);
			jq(d).html(msg);

			at.freebim.db.request.get("/search/search", { searchstring: s, searchtype: searchtype}).then((response) => {
						jq(document).trigger("hide_progress", [{key:"Search_load"}]);
						jq("#" + self.mainDivId + " .searchresult").empty();
						var d = jq("#" + self.mainDivId + " .searchresult")[0];
						if (response.result) {
							switch (searchtype) {
							case "bsdd-guid":
								if (response.result.length && response.result.length > 0) {
									// response.result is an array of BaseNode 
									var i, node, div, nodes = response.result;
									jq(d).empty();
									for (i=0; i<nodes.length;i++) {
										node = nodes[i];
										at.freebim.db.domain.listen(node);
										div = document.createElement("div");
										jq(d).append(div);
										div.appendChild(document.createTextNode(at.freebim.db.domain[node[nf.CLASS_NAME]].getNameOf(node)));
										at.freebim.db.domain.setFreebimItemClasses(node, div);
									}
								} else {
									// nothing found
									jq(d).html(i18n.get("NOTHING_FOUND"));
								}
								return;
							case "freebim-id" : 
								// response.result is a UuidIdentifyable
								var node = response.result;
								at.freebim.db.domain.listen(node);
								var div = document.createElement("div");
								jq(d).empty().append(div);
								div.appendChild(document.createTextNode(at.freebim.db.domain[node[nf.CLASS_NAME]].getNameOf(node)));
								at.freebim.db.domain.setFreebimItemClasses(node, div);
								return;
							case "state":
								// response.result is an ArrayList<StatedBaseNode>
								if (response.result.length == 0) {
									// nothing found
									jq(d).html(i18n.get("NOTHING_FOUND"));
									return;
								}
								self.createStateResultTable(s, d, response);
								return;
							default: 
								break;
							}
							// response.result is a List<MatchResult>
							if (response.result.length == 0) {
								// nothing found
								jq(d).html(i18n.get("NOTHING_FOUND"));
								return;
							}
							self.createMatchResultTable(d, response);
						} else {
							// nothing found
							jq(d).html(i18n.get("NOTHING_FOUND"));
						}
//						jq("#" + self.mainDivId + " .searchresult").html(response.result);	
						jq("#" + self.mainDivId + " .breadcrump").off("click").click(function (event) {
//				  			console.log(self.mainDivId + " breadcrump: " + event.target.id);
				  		});
						
/*						jq("div").each(function() {
							var nId = jq(this)[0].id;
							nId = nId.replace("breadcrump_", "");
							jq(this).data("nodeid", nId);
						});
						if (at.freebim.db.user.isShowNodeId) {
							jq(".freebim-nodeId").remove();
							jq(".freebim-item[nodeid]").each(function() {
								at.freebim.db.domain.showId(jq(this));
							});
						}
						if (at.freebim.db.user.isShowFreebimId) {
							jq(".freebim-freebimId").remove();
							jq(".freebim-item[nodeid]").each(function() {
								at.freebim.db.domain.showFreebimId(jq(this));
							});
						}
						if (at.freebim.db.user.isShowBsdd) {
							jq(".freebim-bsdd").remove();
							jq(".freebim-item[nodeid]").each(function() {
								at.freebim.db.domain.showBsdd(jq(this));
							});
						}
						if (at.freebim.db.user.isShowLibrary) {
							jq(".freebim-library").remove();
							jq(".freebim-item[nodeid]").each(function() {
								at.freebim.db.domain.showLibrary(jq(this));
							});
						}
*/					}
					
			).catch((error) => {
				jq(document).trigger("hide_progress", [{key:"Search_load"}]);
				alert("error: " + error);
			});
		};
		
		self.createStateResultTable = function(s, d, response) {
			var t, i, n = response.result.length;
			for (i=0; i<n; i++) {
				var node = response.result[i];
				at.freebim.db.domain.listen(node);
			}
			t = Object.create(net.spectroom.js.table);
			t.initialSort = 2;
			t.dataCallback = function(row, tr) {
				var node = response.result[row];
				if (!node || node[nf.CLASS_NAME] == "BigBangNode" || node[nf.CLASS_NAME] == "Library") {
					return null;
				}
				return node;
			};
			t.idCol = "id";
			t.rowCount = response.result.length;
			t.initialSort = -1;
			t.cols = [{
				label : "PTYPE_TYPE",
				field : nf.CLASS_NAME,
				type : "callback",
				data : function (v, tr, td, row, col) { 
					if (v) {
						if (at.freebim.db.domain[v]) {
							return at.freebim.db.domain[v].title;
						}
					}
					return ""; 
				},
				sort : true
			},{
				label : "CODE",
				field : nf.CODE,
				type : "callback",
				data : function (v, tr, td, row, col) { return ((v) ? v : ""); },
				sort : true
			},
			{
				label : "NAME",
				field : "id",
				type : "custom",
				createCell : function(tr, td, row, col) {
					var v = t.dataCallback(row); // v is a StatedBaseNode
					jq(td).empty();
					if (v) { 
						var div = document.createElement("div");
						td.appendChild(div);
						div.appendChild(document.createTextNode(at.freebim.db.domain[v[nf.CLASS_NAME]].getNameOf(v)));
						at.freebim.db.domain.setFreebimItemClasses(v, div, tr);
						at.freebim.db.domain.State.css(s, td);
					}
				},
				sortValue : function(row) {
					var v = t.dataCallback(row); // v is a StatedBaseNode
					if (v) {
						return at.freebim.db.domain[v[nf.CLASS_NAME]].getNameOf(v);
					}
					return "";
				},
				sort : true
			},
			{
				label : "CLAZZ_LIBRARY",
				field : "id",
				type : "custom",
				createCell : function(tr, td, row, col) {
					var v = t.dataCallback(row); // v is a StatedBaseNode
					jq(td).empty();
					if (v && v[nf.REFERENCES] && v[nf.REFERENCES].length > 0) { // v[nf.REFERENCES] is an array of References relations
						at.freebim.db.domain[v[nf.CLASS_NAME]].init();
						at.freebim.db.domain[v[nf.CLASS_NAME]].fetchEntries("div", v, nf.REFERENCES, "Library", "OUT", function(libs) {
							jq(td).empty();
							var j;
							for (j=0; j<libs.length; j++) {
								td.appendChild(libs[j]);
								break;
							}
							jq(self.table).trigger("cellChanged", [{}]);
						});
					}
				},
				sortValue : function(row) {
					var v = t.dataCallback(row); // v is a MatchResult
					if (v && v[nf.REFERENCES] && v[nf.REFERENCES].length > 0) { // v[nf.REFERENCES] is an array of References relations
						var lib = at.freebim.db.domain.get(v[nf.REFERENCES][0][rf.TO_NODE]);
						if (lib) {
							return lib[nf.NAME];
						}
						return "";
					}
				},
				sort : true
			},
			{
				label : "DESCRIPTION",
				field : nf.DESC,
				type : "callback",
				data : function (v, tr, td, row, col) { 
					var s = ((v) ? v : "");
					s = net.spectroom.js.shrinkText(s, 64, td);
					return s;
				},
				sort : true
			}, {
				label : "STATUS_COMMENT",
				field : nf.STATUS_COMMENT,
				type : "callback",
				data : function (v, tr, td, row, col) { 
					var s = ((v) ? v : "");
					s = net.spectroom.js.shrinkText(s, 64, td);
					return s;
				},
				sort : true
			}
			];
			t.cellCreatedTimeout = null;
			jq(document).on("freebimItemDisplayChanged", function (event, data) { jq(t).trigger("cellChanged"); });
			t.create(t, d, at.freebim.db.search, at.freebim.db.search);
		};
		
		self.createMatchResultTable = function(d, response) {
			var t, i, n = response.result.length;
			for (i=0; i<n; i++) {
				var r = response.result[i], node = r.node;
				at.freebim.db.domain.listen(node);
			}
			t = Object.create(net.spectroom.js.table);
			t.initialSort = 2;
			t.dataCallback = function(row, tr) {
				return response.result[row];
			};
			t.idCol = "id";
			t.rowCount = response.result.length;
			t.initialSort = -1;
			t.cols = [{
				label : "MATCH",
				field : "q",
				type : "callback",
				data : function (v, tr, td, row, col) { 
					return Math.round(v*1000) / 10 + "%"; 
				},
				sortValue : function(row) {
					return response.result[row].q;
				},
				sort : true
			}, {
				label : "PATH",
				field : "node",
				type : "custom",
				createCell : function(tr, td, row, col) {
					// entries will pop up in tooltip
					td.title = "";
					var v = t.dataCallback(row); // v is a MatchResult
					jq(td).html("→");
					if (v && v.node) {
						jq(td).tooltip({
								open: function(event, ui) {
								// remove all other tooltips!
							    jq(ui.tooltip).siblings(".ui-tooltip").remove();
							},
							content: function(callback) {
								// don't show more than 100 paths in tooltip ...
								at.freebim.db.request.post("/relations/getAllPaths", {nodeId: v.node[nf.NODEID], onlyValid: true, max: 100}).then((response) => {
									if (response && response.result) {
										var i, j, m, n = response.result.length, txt, paths = [];
										for (i=0; i<n; i++) {
											var path = response.result[i]; // path is a SimplePathResult
											txt = "";
											if (!path || !path.names || path.names.length == 0) {
												continue;
											}
											txt += "<li class='searchresult-path' style='white-space: normal;'>";

											m = path.names.length;
											for (j=1; j<m; j++) { // path[0] is the BigBangNode
												if (j > 1) {
													txt += "<span class='freebim-path-rel'> → </span>";
												}
												var name = path.names[j];
												txt += "<span ";
												if (path.clNames && path.clNames[j]) {
													txt += "class='freebim-item " + path.clNames[j] + " path'";
												}
												txt += ">";
												txt += name;
												txt += "</span>";
											}
											txt += "</li>";
											paths.push(txt);
										}
										paths.sort(function (a, b) {
											if (a < b) { 
												return -1;
											} else if (a > b) {
												return 1;
											} else {
												return 0;
											}
										});
										txt = "<ul>"
										for (i=0; i<paths.length; i++) {
											txt += paths[i];
										}
										txt += "</ul>";
										
										callback(txt);
									}
								}).catch((error) => {
									clazz.loading = false;
								});
							}
						});
					}
				},
				sort : false
			}, {
				label : "PTYPE_TYPE",
				field : "node",
				type : "callback",
				data : function (v, tr, td, row, col) { 
					return ((v[nf.CLASS_NAME]) ? at.freebim.db.domain[v[nf.CLASS_NAME]].title : ""); 
				},
				sort : true
			},{
				label : "CODE",
				field : "node",
				type : "callback",
				data : function (v, tr, td, row, col) { return ((v[nf.CODE]) ? v[nf.CODE] : ""); },
				sort : true
			},
			{
				label : "NAME",
				field : "node",
				type : "custom",
				createCell : function(tr, td, row, col) {
					var v = t.dataCallback(row); // v is a MatchResult
					jq(td).empty();
					if (v && v.node) { 
						var div = document.createElement("div");
						td.appendChild(div);
						at.freebim.db.domain.renderNode(v.node, div);
					}
				},
				sort : true,
				sortValue : function(row) {
					var v = t.dataCallback(row); // v is a MatchResult
					if (v && v.node) { 
						return v.node[nf.NAME];
					}
				}
			},
			{
				label : "CLAZZ_LIBRARY",
				field : "node",
				type : "custom",
				createCell : function(tr, td, row, col) {
					var v = t.dataCallback(row); // v is a MatchResult
					jq(td).empty();
					if (v && v.node && v.node[nf.REFERENCES] && v.node[nf.REFERENCES].length > 0) { // v.node[nf.REFERENCES] is an array of References relations
						at.freebim.db.domain[v.node[nf.CLASS_NAME]].init();
						at.freebim.db.domain[v.node[nf.CLASS_NAME]].fetchEntries("div", v.node, nf.REFERENCES, "Library", "OUT", function(libs) {
							jq(td).empty();
							var j;
							for (j=0; j<libs.length; j++) {
								td.appendChild(libs[j]);
								break;
							}
							jq(self.table).trigger("cellChanged", [{}]);
						});
					}
				},
				sortValue : function(row) {
					var v = t.dataCallback(row); // v is a MatchResult
					if (v && v.node && v.node[nf.REFERENCES] && v.node[nf.REFERENCES].length > 0) { // v.node[nf.REFERENCES] is an array of References relations
						var lib = at.freebim.db.domain.get(v.node[nf.REFERENCES][0][rf.TO_NODE]);
						if (lib) {
							return lib[nf.NAME];
						}
						return "";
					}
				},
				sort : true
			},
			{
				label : "DESCRIPTION",
				field : "node",
				type : "callback",
				data : function (v, tr, td, row, col) { 
					var s = ((v && v[nf.DESC]) ? v[nf.DESC] : "");
					s = net.spectroom.js.shrinkText(s, 64, td);
					return s;
				},
				sort : true
			}
			];
			t.cellCreatedTimeout = null;
			jq(document).on("freebimItemDisplayChanged", function (event, data) { jq(t).trigger("cellChanged"); });
			t.create(t, d, at.freebim.db.search, at.freebim.db.search);
		};
		
		jq(at.freebim.db.search).on("find", function (event, data) {
			jq("#" + self.mainDivId + " .searchfield").val(data.searchString);
			jq("#" + self.mainDivId + " input:radio[name=searchtype][value='Named']").prop('checked', true);
			self.search();
		});
		self.initialized = true;
	}
};