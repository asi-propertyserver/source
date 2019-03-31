/**
 * 
 */
at.freebim.db.problems = {
	
	init: function () {
		at.freebim.db.problems.acc();
	},
	
	accTimeout : null,
	acc : function () {
		var p = at.freebim.db.problems;
		if (p.accTimeout) {
			clearTimeout(p.accTimeout);
		}
		p.accTimeout = setTimeout(function () {
			
			jq("#freebim_problems_acc").accordion({
				header: "h3",
				active: false,
				collapsible: true,
				heightStyle: "fill",
				icons: { "header": "ui-icon-plus", "activeHeader": "ui-icon-minus" },
				activate: function ( event, ui ) {
					
					var problem = jq(ui.newHeader).attr("problem") * 1;
					if (problem) {
						switch (problem) {
							case 1: p.emptyComponents(); 	break;
							case 2: p.missingMeasure(); 	break;
							case 3: p.emptyMeasure();  		break;
							case 4: p.deletedPhase();  		break;
							case 5: p.paramsToMoveUp(); 	break;
							case 6: p.multipleParameterAssignment(); break;
						}
					}
				}
			});
		}, 250);
	},
	
	missingMeasure : function () {
		var db = at.freebim.db, d = db.domain;
		var x = jq("#freebim_problems_no_measure");
		x.empty();
		db.post("/problems/missingMeasure", {}, net.spectroom.js.i18n.g("PROBLEM_LOAD_1"), function (response) {
			if (response && response.result) {
				// response.result is an array of nodeId's of Parameters
				jq.each(response.result, function (i, id) {
					// is this Parameter relevant?
					if (d.relevantId(d.Parameter.className, id)) {
						d.getOrLoad(id, d.Parameter.className, function (node) {
							// is this node deleted?
							if (db.time.validNode(node)) {
								var div = document.createElement("div");
								x.append(div);
								d.renderNode(node, div);
							}
						});
					}
				});
			}
		});
	},
	
	emptyMeasure : function () {
		var db = at.freebim.db, d = db.domain;
		var x = jq("#freebim_problems_empty_measure");
		x.empty();
		db.post("/problems/emptyMeasure", {}, net.spectroom.js.i18n.g("PROBLEM_LOAD_2"), function (response) {
			if (response && response.result) {
				// response.result is an array of nodeId's of Measures
				jq.each(response.result, function (i, id) {
					// is this Measure relevant?
					if (d.relevantId(d.Measure.className, id)) {
						d.getOrLoad(id, d.Measure.className, function (node) {
							// is this node deleted?
							if (db.time.validNode(node)) {
								var div = document.createElement("div");
								x.append(div);
								d.renderNode(node, div);
							}
						});
					}
				});
			}
		});
	},
	
	emptyComponents : function () {
		var db = at.freebim.db, d = db.domain;
		var x = jq("#freebim_problems_no_params");
		x.empty();
		db.post("/problems/emptyComponents", {}, net.spectroom.js.i18n.g("PROBLEM_LOAD_3"), function (response) {
			if (response && response.result) {
				// response.result is an array of nodeId's of Components
				jq.each(response.result, function (i, id) {
					// is this Component relevant?
					if (d.relevantId(d.Component.className, id)) {
						d.getOrLoad(id, d.Component.className, function (node) {
							// is this node deleted?
							if (db.time.validNode(node)) {
								var div = document.createElement("div");
								x.append(div);
								d.renderNode(node, div);
							}
						});
					}
				});
			}
		});
	},
	
	deletedPhase : function () {
		var db = at.freebim.db, d = db.domain;
		var x = jq("#freebim_problems_deleted_phase");
		x.empty();
		db.post("/problems/deletedPhase", {}, net.spectroom.js.i18n.g("PROBLEM_LOAD_4"), function (response) {
			if (response && response.result) {
				// response.result is an array of nodeId's of Parameter
				jq.each(response.result, function (i, id) {
					// is this Parameter relevant?
					if (d.relevantId(d.Parameter.className, id)) {
						d.getOrLoad(id, d.Parameter.className, function (node) {
							// is this node deleted?
							if (db.time.validNode(node)) {
								var div = document.createElement("div");
								x.append(div);
								d.renderNode(node, div);
							}
						});
					}
				});
			}
		});
	},
	
	paramsToMoveUp : function () {
		//
		var db = at.freebim.db, d = db.domain, i, nf = d.NodeFields, n = d.Library.used.length, lib, div,
		fetch = function (xd, libid, libdiv) {
			
			db.post("/problems/paramsMoveUp", {libid: libid}, net.spectroom.js.i18n.g("PROBLEM_LOAD_5"), function (response) {
				if (response && response.result) {
					
					// response.result is an array of IdPair's containing nodeId's and names of Component and Parameter
					response.result.sort(function (p1, p2) {
						if (p1.an < p2.an) {
							return -1;
						} else if (p1.an > p2.an) {
							return 1;
						} else {
							if (p1.bn < p2.bn) {
								return -1;
							} else if (p1.bn > p2.bn) {
								return 1;
							} else {
								return 0;
							}
						}
					});
					
					jq.each(response.result, function (i, pair) {
						var pairdiv = document.createElement("div");
						var cdiv = document.createElement("div");
						var pdiv = document.createElement("div");
						jq(libdiv).append(pairdiv);
						jq(pairdiv).addClass("pair").append(cdiv)
							.append("<span class='freebim-path-rel'> → </span>")
							.append(pdiv);
						// the Component:
						jq(cdiv).append("<span class='freebim-item freebim-contextmenu Component' nodeid='" + pair.a + "'>" + pair.an + "</span>");
						// the Parameter:
						jq(pdiv).append("<span class='freebim-item freebim-contextmenu Parameter' nodeid='" + pair.b + "'>" + pair.bn + "</span>");
					});
				}
				setTimeout(function () {
					jq(xd).accordion( "refresh" );
				}, 50);
			});

		};
		var x = jq("#freebim_problems_params_move_up"), xd = document.createElement("div");
		x.empty().append(xd);
		for (i=0; i<n; i++) {
			lib = d.get(d.Library.used[i]);
			div = document.createElement("div");
			jq(div).addClass("problem-by-library");
			jq(xd).append("<h4 libid='" + lib[nf.NODEID] + "'>" + lib[nf.NAME] + "</h4>").append(div);

		}
		jq(xd).accordion({
			header: "h4",
			active: false,
			collapsible: true,
			heightStyle: "content",
			icons: { "header": "ui-icon-plus", "activeHeader": "ui-icon-minus" },
			activate: function ( event, ui ) {
				
				var libid = jq(ui.newHeader).attr("libid");
				if (libid) {
					fetch(xd, libid, jq(ui.newHeader).next());
					
				}
			}
		});
	},
	
	multipleParameterAssignment : function () {
		var db = at.freebim.db, d = db.domain;
		var x = jq("#freebim_problems_multiple_parameter_assignment");
		x.empty();
		db.post("/problems/multipleParameterAssignment", {}, net.spectroom.js.i18n.g("PROBLEM_LOAD_6"), function (response) {
			if (response && response.result) {
				// response.result is an array of IdTriple's
				response.result.sort(function (t1, t2) {
					if (t1.an < t2.an) {
						return -1;
					} else if (t1.an > t2.an) {
						return 1;
					} else {
						if (t1.bn < t2.bn) {
							return -1;
						} else if (t1.bn > t2.bn) {
							return 1;
						} else {
							if (t1.cn < t2.cn) {
								return -1;
							} else if (t1.cn > t2.cn) {
								return 1;
							} else {
								return 0;
							}
						}
					}
				});
				jq.each(response.result, function (i, pair) {
					var pairdiv = document.createElement("div");
					var c1div = document.createElement("div");
					var pdiv = document.createElement("div");
					var c2div = document.createElement("div");
					jq(x).append(pairdiv);
					jq(pairdiv).addClass("pair")
						.append(c1div)
						.append("<span class='freebim-path-rel'> → </span>")
						.append(pdiv)
						.append("<span class='freebim-path-rel'> ← </span>")
						.append(c2div);
					// the 1st Component:
					jq(c1div).append("<span class='freebim-item freebim-contextmenu Component' nodeid='" + pair.a + "'>" + pair.an + "</span>");
					// the Parameter:
					jq(pdiv).append("<span class='freebim-item freebim-contextmenu Parameter' nodeid='" + pair.c + "'>" + pair.cn + "</span>");
					// the 2nd Component:
					jq(c2div).append("<span class='freebim-item freebim-contextmenu Component' nodeid='" + pair.b + "'>" + pair.bn + "</span>");
				});
			}
		});
	}

};