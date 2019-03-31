/**
 * 
 */
at.freebim.db.imprint = {
		
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
			var db = at.freebim.db, d = db.domain, g = db.imprint, nf = d.NodeFields, rf = d.RelationFields, rt = d.RelationType, rte = d.RelationTypeEnum;
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
			jq("#freebim_imprint_graph").empty().css("height", "1000px");
			g.links = [];
			g.nodes = [];
			g.linkMap = {};
			g.nodeMap = {};

			jq("#freebim_imprint_graph").empty();
			var width = jq("#freebim_imprint_graph").width(),
			height = jq("#freebim_imprint_graph").height();

			g.force = d3.layout.force()
		    .nodes(g.nodes)
		    .links(g.links)
		    .size([width, height])
		    .linkDistance(function (d) { 
		    	return ((d.t == 24) ? 32 : 80);
		    })
		    .charge(function (d) { 
		    	return ((d.t == "Contributor") ? -200 : -2000); 
		    }) 
		    .linkStrength(function (d) { 
				return 1.;
		    })
		    .on("tick", tick);
			
			g.force.drag().on("dragstart", function () {
				var e = d3.event.sourceEvent;
				e.preventDefault();
				e.stopPropagation(); 
				g.stopped = false;
			});

			g.svg = d3.select("#freebim_imprint_graph").append("svg")
			    .attr("width", width)
			    .attr("height", height)
			    .append("g")
			    .call(d3.behavior.zoom().scaleExtent([0.25, 2]).on("zoom", g.zoom))
			    .append("g")
			    ;
			
//			jq(g.svg).attr("xmlns", "http://www.w3.org/2000/svg")
//				.attr("xmlns:xlink", "http://www.w3.org/1999/xlink")
//
	
			// Per-type markers, as they don't inherit styles.
			g.svg.append("defs").selectAll("#freebim_imprint_graph marker")
			    .data([rt[rte["PARENT_OF"]].uiName])
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
			
			g.svg.append("g").attr("class", "svg-links");
			g.svg.append("g").attr("class", "svg-nodes");
			g.svg.append("g").attr("class", "svg-text");
	
			g.path = g.svg.selectAll("#freebim_imprint_graph g.svg-links path");
			g.circle = g.svg.selectAll("#freebim_imprint_graph g.svg-nodes circle");
			g.image = g.svg.selectAll("#freebim_imprint_graph g.svg-nodes image");
			g.text = g.svg.selectAll("#freebim_imprint_graph g.svg-text text");

			
			// Use elliptical arc or line path segments
			function tick(e) {
				
				g.path.attr("d", linkLine);
				g.circle.attr("transform", transform);
				g.image.attr("transform", transform);
				g.text.attr("transform", transform);

				g.setBBox();

			}
/*				
			function linkLine(d) {
				  var dx = d.target.x - d.source.x,
//				  	  dr,
				      dy = d.target.y - d.source.y;
//					switch (rt[d[rf.TYPE]].uiName) {
//					default: 
//					  	// an arc
//				      	dr = Math.sqrt(dx * dx + dy * dy);
//				  		return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
//
//					case "RT_EQUALS":
//					  // a line
					  return "M" + d.source.x + "," + d.source.y + "l" + dx + "," + dy;
//					}
				}
*/				
			function linkLine(d) {
				  var dx = d.target.x - d.source.x,
				  	  dr,
				      dy = d.target.y - d.source.y;
					  	// an arc
				      	dr = Math.sqrt(dx * dx + dy * dy);
				  		return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
				}
	
			function transform(d) {
			  return "translate(" + d.x + "," + d.y + ")";
			}	
			
//			g.svg.call(g.tip);
			
			g.setup();
		},
		
		zoom : function () {
			var db = at.freebim.db, g = db.imprint, sw = 0.75;
			if (d3 && d3.event && d3.event.scale) {
				sw /= d3.event.scale;
				g.svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
			}
//			g.css["graph-view-path"].style["stroke-width"] = sw + "px";
//			g.css["graph-view-onpath"].style["stroke-width"] = sw * 3. + "px";
//			g.css["graph-view-text-onpath"].style["font-size"] = ((sw < 1.) ? 2. : (sw * 2.)) + "em";

			
			g.setBBox();
		},
		
		setBBox : function () {
			var r = jq("#freebim_imprint_graph svg rect"), bb, x = Number.POSITIVE_INFINITY, y = Number.POSITIVE_INFINITY, a = Number.NEGATIVE_INFINITY, b = Number.NEGATIVE_INFINITY;
			
			bb = jq("#freebim_imprint_graph svg .svg-nodes").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = bb.x; y = bb.y; a = x + bb.width; b = y + bb.height;
			
			bb = jq("#freebim_imprint_graph svg .svg-text").get(0).getBBox();
			if (bb.width > 0 && bb.height > 0)
				x = Math.min(x, bb.x); y = Math.min(y, bb.y); a = Math.max(a, bb.x + bb.width); b = Math.max(b, bb.y + bb.height);
			
			bb = jq("#freebim_imprint_graph svg .svg-links").get(0).getBBox();
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
			var db = at.freebim.db, d = db.domain, g = db.imprint;			

//			g.nodes.push({id: d.bbnId, n: "*", t: "BigBangNode"});
//			g.nodeMap[d.bbnId] = g.nodes[0];
//			
			db.post("/company/graph", 
					{}, 
					net.spectroom.js.i18n.g("LOADING_GRAPH"), 
						function (response) {
							db.imprint.setLinks(response.result);
						}, 
						function (e) {
							db.logger.error("Error setting links for imprint graph: " + e.message);
						});
		},
		
		start : function () {
			var db = at.freebim.db, g = db.imprint, rf = db.domain.RelationFields, rt = db.domain.RelationType;
			
			g.path = g.path.data(g.links, function(d) { return d[rf.ID]; } );
			g.path.enter()
				.append("path")
				.attr("class", function(d) { 
					return "link " + rt[d[rf.TYPE]].uiName; 
				})
				.attr("marker-end", function(d) { 
					return "url(#" + rt[d[rf.TYPE]].uiName + ")"; 
				})
				.attr("linkid", function(d) { 
					return d[rf.ID]; 
				});
			g.path.exit().remove();

			// circle or image?
			var cir = [], img = [];
			jq.each(g.nodes, function (i, n) {
				if (n.t == "Company" && (n.href && n.href.length > 0 && n.href != "nd.png")) {
					img.push(n);
				} else {
					cir.push(n);
				}
			});
			
			g.circle = g.circle.data(cir, function(d) { return d.id; });
			g.circle.enter()
				.append("svg:circle")
				.attr("nodeid", function(d) { 
					return ((d && d.id) ? d.id : ""); 
				})
				.attr("r", function (d) {
					return ((d.t == "Company") ? 4 : 2);
				})
				.attr("class", function (d) {
					return d.t;
				})
				.call(g.force.drag);
			g.circle.exit().remove();

			g.image = g.image.data(img, function(d) { return d.id; });
			g.image.enter()
				.append("svg:image") 
				.attr("x", function (d) {
					return ((d.href && d.href.length > 0) ? "-60px" : "-8px");
				})
				.attr("y", function (d) { 
					return ((d.href && d.href.length > 0) ? "-20px" : "-8px");
				})
				.attr("width", function (d) { 
					return ((d.href && d.href.length > 0) ? "120px" : "16px");
				})
				.attr("height", function (d) { 
					return ((d.href && d.href.length > 0) ? "40px" : "16px");
				})
				.attr("xlink:href", function (d) { 
					return ((d.href && d.href.length > 0) ? "company/logo/" + d.href : "company/logo/nd.png");
				})
				
/*				.attr("x", "-60px")
				.attr("y", "-20px")
				.attr("width", "120px")
				.attr("height", "40px")
				.attr("xlink:href", function (d) { 
					return ((d.href && d.href.length > 0) ? "company/logo/" + d.href : "company/logo/6cd79ab6-dbcb-449a-b8cf-5e8e84d85d9a");
				})
*/
				.call(g.force.drag);
			g.image.exit().remove();

			g.text = g.text.data(g.nodes, function(d) { return d.id; });
			g.text.enter()
				.append("text")
				.attr("render-order", "15")
				.attr("x", 6)
				.attr("y", ".31em")
				.attr("class", function(d) { 
					var c = "label " + d.t;
					return c; 
				})
				.attr("nodeid", function(d) { 
					return d.id; 
				})
				.text(function(d) {
					if (d.href && d.href.length > 0)
						return "";
					var txt = d.n;
					return txt; 
				});
			g.text.exit().remove();
			
			jq("#freebim_imprint_graph image").appendTo("#freebim_imprint_graph .svg-nodes");
			jq("#freebim_imprint_graph circle").appendTo("#freebim_imprint_graph .svg-nodes");
			jq("#freebim_imprint_graph path.link").appendTo("#freebim_imprint_graph .svg-links");
			jq("#freebim_imprint_graph text.label").appendTo("#freebim_imprint_graph .svg-text");
			
			g.stopped = false;
			g.force.start();
		},
		
		setLinks : function (graph) {
			
			var db = at.freebim.db, g = db.imprint, rf = db.domain.RelationFields,
				id, link, node, i, k, keys = Object.keys(graph.nodes), n = keys.length;
			for (i=0; i<n; i++) {
				k = keys[i];
				if (!g.nodeMap[k]) {
					node = graph.nodes[k];
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
			
			setInterval(function () {
				g.force.resume();
			}, 30000);
			
			
		}
		
};
