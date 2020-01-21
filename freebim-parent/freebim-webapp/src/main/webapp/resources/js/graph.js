/**
 * 
 */
at.freebim.db.graph = {
		
		links : [],
		nodes : [],
		linkMap : {},
		nodeMap : {},
		force : null,
		node : null,
		link : null,
		libClasses : {},
		stopped : false,
		
		init : function () {
			var db = at.freebim.db, d = db.domain, g = db.graph, nf = d.NodeFields, rf = d.RelationFields, rt = d.RelationType, rte = d.RelationTypeEnum;
			if (g.force) {
				return;
			}
			
			/* Initialize tooltip 
			g.tip = d3.tip()
			  .attr('class', 'd3-tip')
			  .offset([-10, 0])
			  .html(function(d) {
			    return "<strong>" + db.domain[d.t][nf.TITLE] + ":</strong> <span style='color:red'>" + d[nf.NAME] + "</span>";
			  });
			*/
//			jq("#graph_load_options").empty();
//			jq("#graph_view_options").empty();
			g.links = [];
			g.nodes = [];
			g.linkMap = {};
			g.nodeMap = {};

			jq("#graph").empty();
			var width = jq("#graph").width(),
			height = jq("#graph").height();

			g.force = d3.layout.force()
		    .nodes(g.nodes)
		    .links(g.links)
		    .size([width, height])
		    .linkDistance(function (d) { 
		    	return ((rt[d[rf.TYPE]].uiName == "RT_HAS_PARAMETER") ? 20 : 200);
		    })
		    .charge(function (d) { 
		    	return ((d.t == "Parameter") ? -50 : -1000 ); 
		    }) 
/*		    .chargeDistance(function (d) { 
		    	return 30; //((d.t == "Parameter") ? -1000 : -300 ); 
		    }) 
*/		    .linkStrength(function (d) { 
				switch (rt[d[rf.TYPE]].uiName) {
				default: return 1;
				case "RT_HAS_PARAMETER": return 0.5;
				case "RT_EQUALS": return 0.1;
				}
		    })
		    .on("tick", tick);
			
			g.force.drag().on("dragstart", function () {
				var e = d3.event.sourceEvent;
				e.preventDefault();
				e.stopPropagation(); 
				g.stopped = false;
			});

			g.svg = d3.select("#graph").append("svg")
			    .attr("width", width)
			    .attr("height", height)
			    .append("g")
			    .call(d3.behavior.zoom().scaleExtent([0.05, 6]).on("zoom", g.zoom))
			    .append("g")
			    ;
			

	
			// Per-type markers, as they don't inherit styles.
			g.svg.append("defs").selectAll("#graph marker")
			    .data([rt[rte["PARENT_OF"]].uiName/*, rt[rte["HAS_PARAMETER"]].uiName, "EQUALS"*/])
			  .enter().append("marker")
			    .attr("id", function(d) { return d; })
			    .attr("viewBox", "0 -5 10 10")
			    .attr("refX", 22)
			    .attr("refY", 0)
			    .attr("markerWidth", 8)
			    .attr("markerHeight", 8)
			    .attr("orient", "auto")
			    .attr("markerUnits", "userSpaceOnUse")
			    .append("path")
			    .attr("class", "marker")
			    .attr("d", "M0,-5L10,0L0,5")
			   ;
			
			g.svg.append("rect")
		    .attr("class", "overlay")
		    .attr("width", width)
		    .attr("height", height);
			
			g.svg.append("g").attr("class", "svg-libref");
			g.svg.append("g").attr("class", "svg-links");
			g.svg.append("g").attr("class", "svg-nodes");
			g.svg.append("g").attr("class", "svg-text");
	
			g.libref = g.svg.selectAll("#graph g.svg-libref circle");
			g.path = g.svg.selectAll("#graph g.svg-links path");
			g.circle = g.svg.selectAll("#graph g.svg-nodes circle");
			g.text = g.svg.selectAll("#graph g.svg-text text");

			
			// Use elliptical arc or line path segments
			function tick() {
				g.libref.attr("transform", transform);
				g.path.attr("d", linkLine);
				g.circle.attr("transform", transform);
				g.text.attr("transform", transform);

				g.setBBox();

			}
				
			function linkLine(d) {
				  var dx = d.target.x - d.source.x,
				      dy = d.target.y - d.source.y,
				      dr;
					switch (rt[d[rf.TYPE]].uiName) {
					default: 
					  	// an arc
				      	dr = Math.sqrt(dx * dx + dy * dy);
				  		return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;

					case "RT_EQUALS":
					  // a line
					  return "M" + d.source.x + "," + d.source.y + "l" + dx + "," + dy;
					}
				}
	
			function transform(d) {
			  return "translate(" + d.x + "," + d.y + ")";
			}	
			
//			g.svg.call(g.tip);
			
			g.setup();
		},
		
		zoom : function () {
			var db = at.freebim.db, g = db.graph, sw = 0.75;
			if (d3 && d3.event && d3.event.scale) {
				sw /= d3.event.scale;
				g.svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
			}
			g.css["graph-view-path"].style["stroke-width"] = sw + "px";
			g.css["graph-view-onpath"].style["stroke-width"] = sw * 3. + "px";
			g.css["graph-view-text-onpath"].style["font-size"] = ((sw < 1.) ? 2. : (sw * 2.)) + "em";

			
			g.setBBox();
		},
		
		setBBox : function () {
			var r = jq("#graph svg rect"), bb, x = Number.POSITIVE_INFINITY, y = Number.POSITIVE_INFINITY, a = Number.NEGATIVE_INFINITY, b = Number.NEGATIVE_INFINITY;
			
			bb = jq("#graph svg .svg-nodes").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = bb.x; y = bb.y; a = x + bb.width; b = y + bb.height;
			
			bb = jq("#graph svg .svg-text").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = Math.min(x, bb.x); y = Math.min(y, bb.y); a = Math.max(a, bb.x + bb.width); b = Math.max(b, bb.y + bb.height);
			
			bb = jq("#graph svg .svg-links").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = Math.min(x, bb.x); y = Math.min(y, bb.y); a = Math.max(a, bb.x + bb.width); b = Math.max(b, bb.y + bb.height);
			
			bb = jq("#graph svg .svg-libref").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = Math.min(x, bb.x); y = Math.min(y, bb.y); a = Math.max(a, bb.x + bb.width); b = Math.max(b, bb.y + bb.height);

			if (a-x > 0 && b-y > 0) {
				r.attr("x", x - 20)
				.attr("y", y - 20)
				.attr("width", a-x + 40)
				.attr("height", b-y + 40);
			}

		},

		
		setup : function () {
			var db = at.freebim.db, d = db.domain, g = db.graph, nf = d.NodeFields, 
				i, lib, libs = d.Library.used, n = libs.length,
				btn = document.createElement("input"),
				cb, div = document.createElement("div"),
				graphLoadOptions = jq("#graph_load_options"),
				graphViewOptions = jq("#graph_view_options"),
				i18n = net.spectroom.js.i18n;
			

			
			for (i=0; i<n; i++) {
				lib = d.get(libs[i]);
				g.libClasses[lib[nf.NODEID]] = "" + i;
			}

			g.nodes.push({id: d.bbnId, n: "*", t: "BigBangNode"});
			g.nodeMap[d.bbnId] = g.nodes[0];
			
			db.post("/graph/childs_of", 
					{
						parentId: d.bbnId, 
						clazz: "BigBangNode",
						recursive: false, 
						withParams : false,
						equals : false
					}, 
					net.spectroom.js.i18n.g("LOADING_GRAPH"), 
						function (response) {
							db.graph.setLinks(response.result);
						}, 
						function (e) {
							db.logger.error("Error setting parameter links for graph: " + e.message);
						}, null, "GET");

			
			
		},
		
		start : function () {
			var db = at.freebim.db, g = db.graph, rf = db.domain.RelationFields, rt = db.domain.RelationType/*,
			showText = jq("input[name='TOGGLE_TEXT_VIEW']").is(":checked"),
			showLibs = jq("input[name='TOGGLE_LIBRARY_VIEW']").is(":checked"),
			showParams = jq("input[name='TOGGLE_PARAMETER_VIEW']").is(":checked"),
			showEqual = jq("input[name='TOGGLE_EQUAL_VIEW']").is(":checked");*/
			
			g.path = g.path.data(g.links, function(d) { return d[rf.ID]; } );
			g.path.enter()
				.append("path")
				.attr("class", function(d) { 
					return "link " + rt[d[rf.TYPE]].uiName; 
				})
				.attr("marker-end", function(d) { 
					return "url(#" + rt[d[rf.TYPE]].uiName + ")"; 
				})
				/*.attr("style", function (d) {
					var r = "";
					switch (rt[d[rf.TYPE]].uiName) {
					case "RT_HAS_PARAMETER": if (!showParams) { r = "display: none;"; } break;
					case "RT_EQUALS": if (!showEqual) { r = "display: none;"; } break;
					}
					return r;
				})*/
				.attr("linkid", function(d) { 
					return d[rf.ID]; 
				});
			g.path.exit().remove();

			g.circle = g.circle.data(g.nodes, function(d) { return d.id; });
			g.circle.enter()
				.append("circle")
				.attr("r", function(d) { 
					return ((d && d.t && d.t === "Parameter") ? 5 : 10); 
				})
				.attr("class", function(d) { 
					return ((d && d.t) ? "freebim-contextmenu node " + d.t : ""); 
				})
				/*.attr("style", function (d) {
					return ((d && d.t && d.t === "Parameter") ? ((showParams) ? "font-weight: normal;" : "display: none;") : "font-weight: normal;"); 
				})*/
				.attr("nodeid", function(d) { 
					return ((d && d.id) ? d.id : ""); 
				})
				.call(g.force.drag);
			g.circle.exit().remove();

			g.libref = g.libref.data(g.nodes, function(d) { return d.id; });
			g.libref.enter()
				.append("circle")
				.attr("r", 150)
				.attr("class", function(d) { 
					return "libref "  + d.t + ((d.libs && d.libs.length > 0) ? " lib-" + g.libClasses[d.libs[0]] : ""); 
				})
				/*.attr("style", ((showLibs) ? "font-weight: normal;" : "display: none;"))*/
				.attr("nodeid", function(d) { 
					return d.id; 
				});
			g.libref.exit().remove();

			g.text = g.text.data(g.nodes, function(d) { return d.id; });
			g.text.enter()
				.append("text")
				.attr("render-order", "15")
				.attr("x", function (d) {
					return ((d.t == "Parameter") ? 6 : 12);
				})
				.attr("y", ".31em")
				.attr("class", function(d) { 
					var c = "label " + d.t;
					if (db.isMarked(d.id)) {
						c += " freebim-marked";
					}if (db.isSelected(d.id)) {
						c += " freebim-selected";
					}
					return c; 
				})
				.attr("nodeid", function(d) { 
					return d.id; 
				})
				/*.attr("style", function(d) { 
					return ((d.t == "Parameter") ? ((showText && showParams) ? "font-weight: normal;" : "display: none;") : ((showText) ? "font-weight: normal;" : "display: none;"));
				})*/
				.text(function(d) { 
					var txt = d.n;
					if (db.isSelected(d.id)) {
						txt = "✓ " + txt;
					}if (db.isMarked(d.id)) {
						txt = "★ " + txt;
					}
					return txt; 
				});
			g.text.exit().remove();
			
			jq("#graph circle.libref").appendTo("#graph .svg-libref");
			jq("#graph circle.node").appendTo("#graph .svg-nodes");
			jq("#graph path.link").appendTo("#graph .svg-links");
			jq("#graph text.label").appendTo("#graph .svg-text");
			
			g.stopped = false;
			g.force.start();
		},
		
		setLinks : function (graph) {
			
			var db = at.freebim.db, g = db.graph, rf = db.domain.RelationFields,
				id, link, node, i, k, keys = Object.keys(graph.nodes), n = keys.length;
			for (i=0; i<n; i++) {
				k = keys[i];
				if (!g.nodeMap[k]) {
					node = graph.nodes[k];
					//node.x = Math.random();
					//node.y = Math.random();
					node.weight = 1;
					g.nodeMap[k] = node;
					g.nodes.push(node);
				}
			}
			
			// Compute the distinct nodes from the links.
			graph.links.forEach(function(link) {
				if (link) {
					  link.source = g.nodeMap[link[rf.FROM_NODE]];
					  link.target = g.nodeMap[link[rf.TO_NODE]];
				}
			});

			i, n = graph.links.length;
			for (i=0; i<n; i++) {
				link = graph.links[i];
				if (link) {
					id = link[rf.ID];
					if (link.source && link.target) {
						if (!g.linkMap[id]) {
							g.links.push(link);
							g.linkMap[id] = 1;
						}
					}
				}
			}
			
		 
			g.force.nodes(g.nodes);
			g.force.links(g.links);
			g.start();
			

		},
		
		
		markLinks: function (nodeId, toRoot) {
			var db = at.freebim.db, d = db.domain, g = db.graph, rf = d.RelationFields, rt = d.RelationType,
				i, n = g.links.length, link;
			if (toRoot) {
				d3.selectAll(".svg-text text.label[nodeid='" + nodeId + "']").classed("onpath", true);
			}
			for (i=0; i<n; i++) {
				link = g.links[i];
				if (((toRoot) ? link[rf.TO_NODE] : link[rf.FROM_NODE]) == nodeId) {
					switch (rt[link[rf.TYPE]].uiName) {
					case "RT_EQUALS": continue;
					default:
						break;
					}
					d3.selectAll(".svg-links path.link[linkid='" + link[rf.ID] + "']").classed("onpath", true);
					d3.selectAll(".svg-nodes circle[nodeid='" + nodeId + "']").classed("onpath", true);
					g.markLinks(((toRoot) ? link[rf.FROM_NODE] : link[rf.TO_NODE]), toRoot);
				}
			}
			g.setBBox();

		},
		
		load : function (cn, id) {
			var db = at.freebim.db, d = db.domain;
			d.getOrLoad(id, cn, function (node) {
				db.post("/graph/childs_of_node",
					{
						parentId: id, 
						recursive: jq("input[name='RECURSIVE']").is(':checked'), 
						withParams : jq("input[name='CLAZZ_PARAMETER']").is(':checked'),
						withEquals : jq("input[name='EQUALITY']").is(':checked')
					}, 
					net.spectroom.js.i18n.g("LOADING_GRAPH"), 
						function (response) {
							db.graph.setLinks(response.result);
						}, 
						function (e) {
							db.logger.error("Error setting parameter links for graph: " + e.message);
						}, null, "GET");
			});
		},
		
		unload : function (cn, id) {
			
//			console.log("\nunloading id = " + id);
			
			var db = at.freebim.db, d = db.domain, rte = d.RelationTypeEnum, g = db.graph,
			rem = [],
			link, node, i, n = g.nodes.length;
			
			
			for (i=0; i<n; i++) {
				node = g.nodes[i];
				if (node.id == id) {
					g.nodes.splice(i, 1);
					break;
				}
			}
			
			for (i=0; i<g.links.length; i++) {
				link = g.links[i];
				if (link) {
					if (link.source.id === id) {
						
//						console.log("link source found, target = " + link.target.id);
						
						switch (link.t) {
						case rte["PARENT_OF"]:
						case rte["HAS_PARAMETER"]:
							rem.push(link.target.id);
						// no break! we don't want to remove equal nodes, only the relation ...
						case rte["EQUALS"]:
							g.links.splice(i, 1);
							i--;

							if (g.linkMap[link.id]) {
								delete (g.linkMap[link.id]);
							}
							break;
						default: 
							break;
						}
						
					} else if (link.target.id === id) {
						
//						console.log("link target found, source = " + link.source.id);
						
						switch (link.t) {
						case rte["EQUALS"]:
						case rte["PARENT_OF"]:
						case rte["HAS_PARAMETER"]:
							g.links.splice(i, 1);
							i--;
							
							if (g.linkMap[link.id]) {
								delete (g.linkMap[link.id]);
							}
							break;
						default: 
							break;
						}

					} 
				}
			}
			
			if (g.nodeMap[id]) {
				delete (g.nodeMap[id]);
			}

			n = rem.length;
			for (i=0; i<n; i++) {
				g.unload(cn, rem[i]);
			}

		}
};


jq(document).delegate("circle.Component", "click", function (event, data) {
	if (event.ctrlKey || event.metaKey || event.altKey) {
		var db = at.freebim.db, d = db.domain, g = db.graph, x = jq(this);
		event.preventDefault();
		event.stopPropagation();
		if (event.altKey) {

			g.unload(d.Component.className, x.attr("nodeid") * 1);
			g.start();
		} else {
			g.load(d.Component.className, x.attr("nodeid") * 1);
		}
	}
});
jq(document).delegate("circle.Parameter", "click", function (event, data) {
	if (/*event.ctrlKey || event.metaKey || */event.altKey) {
		var db = at.freebim.db, d = db.domain, g = db.graph, x = jq(this);
		event.preventDefault();
		event.stopPropagation();
		g.unload(d.Parameter.className, x.attr("nodeid") * 1);
		g.start();
	}
});

jq(document).delegate("circle.Library", "click", function (event, data) {
	// (event.altKey),  (event.shiftKey)
	if (event.ctrlKey || event.metaKey) {
		var db = at.freebim.db, d = db.domain, x = jq(this);
		event.preventDefault();
		event.stopPropagation();
		db.graph.load(d.Library.className, x.attr("nodeid") * 1);
	}
});

jq(document).delegate("circle.node.Component, circle.node.Parameter", "mouseover", function (event) {
	var db = at.freebim.db, g = db.graph, 
		x = jq(this), nodeId = x.attr("nodeid");
	if (event.shiftKey || event.altKey) {
		d3.selectAll(".onpath").classed("onpath", false);
		if (event.altKey) {
			d3.selectAll(".svg-text text.label[nodeid='" + nodeId + "']").classed("onpath", true);
		}
		g.markLinks(nodeId, (event.shiftKey));
	} // event.ctrlKey || event.metaKey || event.altKey) 
	else {
//		g.tip.show();
	}
});

jq(document).delegate("circle.node.BigBangNode", "mouseover", function (event) {
	d3.selectAll(".onpath").classed("onpath", false);
});

/*jq(document).delegate("circle.node.Component, circle.node.Parameter", "mouseout", function (event) {
	var db = at.freebim.db, d = db.domain, g = db.graph, 
		x = jq(this), nodeId = x.attr("nodeid");
	
//	g.tip.hide();
	
});*/


jq(document).delegate("input[type='button'].graph-reset-btn", "click", function () {
	var g = at.freebim.db.graph;
	g.force = null;
	g.init();
	g.zoom();
});
		
jq(document).delegate("input[type='button'].graph-freeze-btn", "click", function () {
	var g = at.freebim.db.graph;
	if (!g.stopped)
		g.force.stop();
	else
		g.force.start();
	g.stopped = !g.stopped;
});

jq(document).delegate("input[type='checkbox'][name='TOGGLE_PARAMETER_VIEW']", "change", function () {
	var g = at.freebim.db.graph, val = jq(this).is(":checked");
	if (val) {
		g.css["graph-view-params"].style.display = "block";
	} else {
		g.css["graph-view-params"].style.display = "none";
	}
});

jq(document).delegate("input[type='checkbox'][name='TOGGLE_TEXT_VIEW']", "change", function () {
	var g = at.freebim.db.graph, val = jq(this).is(":checked");
	if (val) {
		g.css["graph-view-text"].style.display = "block";
	} else {
		g.css["graph-view-text"].style.display = "none";
	}
});


jq(document).delegate("input[type='checkbox'][name='TOGGLE_LIBRARY_VIEW']", "change", function () {
	var g = at.freebim.db.graph, val = jq(this).is(":checked");
	if (val) {
		g.css["graph-view-libref"].style.display = "block";
	} else {
		g.css["graph-view-libref"].style.display = "none";
	}
});

jq(document).delegate("input[type='checkbox'][name='TOGGLE_EQUAL_VIEW']", "change", function () {
	var g = at.freebim.db.graph, val = jq(this).is(":checked");
	if (val) {
		g.css["graph-view-equals"].style.display = "block";
	} else {
		g.css["graph-view-equals"].style.display = "none";
	}
});

jq(document).on("_at.freebim.db.marked.changed", function (event, data) {
	if (data) {
		var x = jq(".svg-text text[nodeid='" + data.nodeId + "']");
		if (x.length > 0) {
			if (at.freebim.db.isSelected(data.nodeId)) {
				x.html("✓ " + x.html()).addClass("freebim-selected");
			} else {
				x.html(x.html().replace("✓", "").trim()).removeClass("freebim-selected");
			}
			if (at.freebim.db.isMarked(data.nodeId)) {
				x.html("★ " + x.html()).addClass("freebim-marked");
			} else {
				x.html(x.html().replace("★", "").trim()).removeClass("freebim-marked");
			}
		}
	} else {
		jq(".svg-text text.freebim-marked, .svg-text text.freebim-selected").each(function () {
			var x = jq(this), nodeId = x.attr("nodeid");
			if (!at.freebim.db.isSelected(nodeId)) {
				x.html(x.html().replace("✓", "").trim()).removeClass("freebim-selected");
			}
			if (!at.freebim.db.isMarked(nodeId)) {
				x.html(x.html().replace("★", "").trim()).removeClass("freebim-marked");
			}
		});
	}
});

jq(document).ready(function () {
	var g = at.freebim.db.graph,
	css = net.spectroom.js.css.init("graph.css");
	g.css = {};
	g.css["graph-view-params"] = css.addRule("#graph circle.Parameter, #graph path.RT_HAS_PARAMETER, #graph text.label.Parameter", "{display:block;}");
	g.css["graph-view-text"] = css.addRule("#graph g.svg-text", "{display:block;}");
	g.css["graph-view-libref"] = css.addRule("#graph g.svg-libref", "{display:block;}");
	g.css["graph-view-equals"] = css.addRule("#graph path.link.RT_EQUALS", "{display:block;}");
	g.css["graph-view-path"] = css.addRule("#graph g.svg-nodes circle, #graph g.svg-links path.link", "{stroke-width:0.75px;}");
	g.css["graph-view-onpath"] = css.addRule("#graph g.svg-nodes circle.onpath, #graph g.svg-links path.link.onpath", "{stroke-width:2.25px;}");
	g.css["graph-view-text-onpath"] = css.addRule("#graph g.svg-text text.onpath", "{font-size:3.0em;}");
	
});