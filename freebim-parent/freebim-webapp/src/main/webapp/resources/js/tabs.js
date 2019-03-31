/**
 * 
 */
at.freebim.db.tabs = {

		freebim_account : 		function () { var index = jq('#freebim_account').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_tree : 			function () { var index = jq('#freebim_tree').index(); jq("#tabs").tabs("option", "active", index - 1); }, 
		MessageNode : 			function () { var index = jq('#MessageNode').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		FreebimUser : 			function () { var index = jq('#FreebimUser').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		Contributor : 			function () { var index = jq('#Contributor').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		Company : 				function () { var index = jq('#Company').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_data : 			function () { var index = jq('#freebim_data').index(); jq("#tabs").tabs("option", "active", index - 1); },  
			Component : 			function () { at.freebim.db.tabs.freebim_data(); var index = jq('#Component').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
			ParameterSet : 			function () { at.freebim.db.tabs.freebim_data(); var index = jq('#ParameterSet').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
			Parameter : 			function () { at.freebim.db.tabs.freebim_data(); var index = jq('#Parameter').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
			Measure : 				function () { at.freebim.db.tabs.freebim_data(); var index = jq('#Measure').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
			ValueList : 			function () { at.freebim.db.tabs.freebim_data(); var index = jq('#ValueList').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
			ValueListEntry : 		function () { at.freebim.db.tabs.freebim_data(); var index = jq('#ValueListEntry').index(); jq("#freebim_data").tabs("option", "active", index - 1); }, 
		freebim_aux : 			function () { var index = jq('#freebim_aux').index(); jq("#tabs").tabs("option", "active", index - 1); },  
			Library : 				function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#Library').index(); jq("#freebim_aux").tabs("option", "active", index - 1); }, 
			Unit : 					function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#Unit').index(); jq("#freebim_aux").tabs("option", "active", index - 1); }, 
			Phase : 				function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#Phase').index(); jq("#freebim_aux").tabs("option", "active", index - 1); }, 
			DataType : 				function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#DataType').index(); jq("#freebim_aux").tabs("option", "active", index - 1); }, 
			Discipline : 			function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#Discipline').index(); jq("#freebim_aux").tabs("option", "active", index - 1); },
			Document : 				function () { at.freebim.db.tabs.freebim_aux(); var index = jq('#Document').index(); jq("#freebim_aux").tabs("option", "active", index - 1); },
		freebim_rel : 			function () { var index = jq('#freebim_rel').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_graph : 		function () { var index = jq('#freebim_graph').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_statistic : 	function () { var index = jq('#freebim_statistic').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_problems : 		function () { var index = jq('#freebim_problems').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_search : 		function () { var index = jq('#freebim_search').index(); jq("#tabs").tabs("option", "active", index - 1); },  
		freebim_imprint : 		function () { var index = jq('#freebim_imprint').index(); jq("#tabs").tabs("option", "active", index - 1); },  
			
		init: function () {
			var t = at.freebim.db.tabs;
			jq(at.freebim.db.tabs).on("showTab", function (event, data) {
				if (data && data.tab && t[data.tab]) { 
					t[data.tab]();
				} 
			});
		},
		cl : function () {
			jq(".ui-tooltip").remove();
		},
		activate : function (tabId) {
			var i18n = net.spectroom.js.i18n;
			at.freebim.db.tabs.cl();
			
			switch (tabId) {
			case "freebim_tree":
				if (at.freebim.db.tree.structure == null) {
					at.freebim.db.tree.structure = Object.create(at.freebim.db.tree);
					at.freebim.db.tree.structure.init(at.freebim.db.tree.structure, "root1");
					at.freebim.db.tree.structure.getChildren(null);
				}
				if (jq("#root2") && jq("#root3")) {
					if (at.freebim.db.tree.structure2 == null) {
						at.freebim.db.tree.structure2 = Object.create(at.freebim.db.tree);
						at.freebim.db.tree.structure2.init(at.freebim.db.tree.structure2, "root2");
						at.freebim.db.tree.structure2.getChildren(null);
					}
					if (at.freebim.db.tree.structure3 == null) {
						at.freebim.db.tree.structure3 = Object.create(at.freebim.db.tree);
						at.freebim.db.tree.structure3.init(at.freebim.db.tree.structure3, "root3");
						at.freebim.db.tree.structure3.getChildren(null);
					}
					var draggables = ".freebim-item.Component,.freebim-item.Parameter,.freebim-item.Measure,.freebim-item.ValueList,.freebim-item.ValueListEntry",
						droppables = draggables,
						layout = jq("#freebim_tree").layout({
						
						west__initClosed: true
						,west__size:				.5 // 50% of layout width
						,west__maxSize:				.85 // 90% of layout width
						,west__togglerTip_closed:	i18n.getTitle("TAB_SET_EQUALITY")
						,west__togglerTip_open:		i18n.getTitle("TAB_SET_EQUALITY_END")
						,west__resizerTip:			i18n.getTitle("LAYOUT_RESIZE")
						,west__slidable:			false
						,west__resizable:			true
						,west__onopen : function() {
							at.freebim.db.logger.info("opening 'set Equality' tab.");
							layout.close("east");
							at.freebim.db.tree.structure.dropEquality = true;
							at.freebim.db.tree.structure3.dragEquality = true;
							jq("#root1").find(".tree-node").find(droppables).each(function() {
								jq(this).droppable(at.freebim.db.tree.dropEqualityOptions);
							});
							jq("#root3").find(".tree-node").find(draggables).each(function() {
								var nodeId = jq(this).attr("nodeid"), node = at.freebim.db.domain.get(nodeId);
								if (at.freebim.db.domain.mayEdit(node)) {
									jq(this).draggable(at.freebim.db.tree.dragOptions);
								}
							});
							setTimeout(function () {
								jq("li[aria-controls='freebim_tree'] a").html(i18n.getTitle("TAB_SET_EQUALITY"));
								jq("#root1").prepend("<h3>" + i18n.get("TAB_TARGET_COMPONENTS") + "</h3>");
								jq("#root3").prepend("<h3>" + i18n.get("TAB_SOURCE_COMPONENTS") + "</h3>");
							}, 200);
						}
						,west__onclose : function() { 
							at.freebim.db.logger.info("closing 'set Equality' tab.");
							at.freebim.db.tree.structure.dropEquality = false;
							at.freebim.db.tree.structure3.dragEquality = false;
							jq("li[aria-controls='freebim_tree'] a").html(i18n.get("TAB_STRUCTURE"));
							jq(jq("#root1").children()[0]).remove();
							jq(jq("#root3").children()[0]).remove();
							jq("#root1").find(".tree-node").find(".tree-headline-text.Component.ui-droppable").droppable("destroy");
							jq("#root3").find(".tree-node").find(".tree-headline-text.Component.ui-draggable").draggable("destroy");
						}
						
						,east__initClosed: true
						,east__size:				.5 // 50% of layout width
						,east__maxSize:				.85 // 90% of layout width
						,east__togglerTip_closed:	i18n.getTitle("TAB_MOVE_PARAMETER")
						,east__togglerTip_open:		i18n.getTitle("TAB_MOVE_PARAMETER_END")
						,east__resizerTip:			i18n.getTitle("LAYOUT_RESIZE")
						,east__slidable:			false
						,east__resizable:			true
						,east__onopen : function() { 
							at.freebim.db.logger.info("opening 'move Parameter' tab.");
							layout.close("west");
							at.freebim.db.tree.structure.dropParam = true;
							at.freebim.db.tree.structure2.dragParam = true;
							jq("#root1").find(".tree-node").find(".tree-headline-text.Component").each(function() {
								var nodeId = jq(this).attr("nodeid"), node = at.freebim.db.domain.get(nodeId);
								if (at.freebim.db.domain.mayEdit(node)) {
									jq(this).droppable(at.freebim.db.tree.dropParamOptions);
								}
							});
							jq("#root2").find(".tree-parameters-content").find(".Parameter").each(function() {
								var nodeId = jq(this).attr("nodeid"), node = at.freebim.db.domain.get(nodeId);
								if (at.freebim.db.domain.mayEdit(node)) {
									jq(this).draggable(at.freebim.db.tree.dragOptions);
								}
							});
							setTimeout(function () {
								jq("li[aria-controls='freebim_tree'] a").html(i18n.getTitle("TAB_MOVE_PARAMETER"));
								jq("#root2").prepend("<h3>" + i18n.get("TAB_SOURCE_COMPONENTS") + "</h3>");
								jq("#root1").prepend("<h3>" + i18n.get("TAB_TARGET_COMPONENTS") + "</h3>");
							}, 200);
						}
						,east__onclose : function() { 
							at.freebim.db.logger.info("closing 'move Parameter' tab.");
							jq("li[aria-controls='freebim_tree'] a").html(i18n.get("TAB_STRUCTURE"));
							jq(jq("#root1").children()[0]).remove();
							jq(jq("#root2").children()[0]).remove();
							at.freebim.db.tree.structure.dropParam = false;
							at.freebim.db.tree.structure2.dragParam = false;
							jq("#root1").find(".tree-node").find(".tree-headline-text.Component.ui-droppable").droppable("destroy");
							jq("#root2").find(".tree-parameters-content").find(".Parameter.ui-draggable").draggable("destroy");
						}
					});
				}
				break;
			case "freebim_search":
				if (!at.freebim.db.searches["freebim_search"]) {
					at.freebim.db.searches["freebim_search"] = Object.create(at.freebim.db.search);
					var type = {
							"Named" : true,
							"Described" : true,
							"Coded" : true
						};
					if (at.freebim.db.contributor && at.freebim.db.contributor.id != undefined) {
						type["freebim-id"] = true;
						type["bsdd-guid"] = true;
						type["state"] = true;
					}
					at.freebim.db.searches["freebim_search"].init("freebim_search", type);
				}
				// set focus to input field
				at.freebim.db.searches["freebim_search"].activate();
				break;
			case "freebim_data":
				jq("#freebim_data").tabs({
					create : function(event, ui) {
						at.freebim.db.tabs.activate(jq(ui.panel)[0].id);
					},
					activate : function(event, ui) {
						at.freebim.db.tabs.activate(jq(ui.newPanel)[0].id);
					}
				});
				break;
			case "freebim_aux":
				jq("#freebim_aux").tabs({
					create : function(event, ui) {
						at.freebim.db.tabs.activate(jq(ui.panel)[0].id);
					},
					activate : function(event, ui) {
						at.freebim.db.tabs.activate(jq(ui.newPanel)[0].id);
					}
				});
				break;
			case "Company":
			case "Component":
			case "Contributor":
			case "DataType":
			case "Discipline":
			case "Document":
			case "MessageNode":
			case "FreebimUser":
			case "Library":
			case "Measure":
			case "Parameter": 
			case "ParameterSet":
			case "Phase":
			case "Unit":
			case "ValueList":
			case "ValueListEntry":
				at.freebim.db.domain[tabId].init();
				jq(document).trigger("_showTable", [{parent: tabId, cn: at.freebim.db.domain[tabId].className}]);
				break;
			case "freebim_imprint":
				setTimeout(function () {
					at.freebim.db.imprint.init();
				}, 100);
				break;
			case "freebim_graph":
				setTimeout(function () {
					at.freebim.db.graph.init();
				}, 100);
				break;
			case "freebim_statistic":
				setTimeout(function () {
					at.freebim.db.statistic.init();
				}, 100);
				break;
			case "freebim_problems":
				setTimeout(function () {
					at.freebim.db.problems.init();
				}, 100);
				break;
			case "freebim_rel":
				at.freebim.db.relations.init();
				at.freebim.db.relations.position();
				break;
			};
			jq(document).trigger("freebimItemDisplayChanged");
			if (/wind/.test(navigator.userAgent.toLowerCase()) || /msie/.test(navigator.userAgent.toLowerCase())) {
				setTimeout(function() {
					jq(".ui-tabs-nav li").css("zoom", 1);
				}, 100);
			}
		}

};

at.freebim.db.tabs.init();

jq(document).ready(
		function() {
			jq("#tabs").tabs({
				create : function(event, ui) {
					at.freebim.db.tabs.activate(jq(ui.panel)[0].id);
				},
				activate : function(event, ui) {
					at.freebim.db.tabs.activate(jq(ui.newPanel)[0].id);
				}
			});
		});
