/**
 * 
 */
at.freebim.db.ctxMenu = {
		
		bsddFetched : {},
		
		changeState : function (node, newState) {
			var nf = at.freebim.db.domain.NodeFields;
			if (node) {
				if (node[nf.STATE] != newState) {
			    	node[nf.STATE] = newState;
			    	jq(document).trigger("_save", [{entity: node}]);
				}
/*		    	
		    	var dlg = net.spectroom.js.newDiv();
				jq(dlg).dialog({
					width: 400,
					height: 300,
					title : "freeBIM - Status",
					open : function () {
						jq(dlg).append("<label for 'sc' class='jsForm-label'>Status-Kommentar:</label><textarea name='sc' class='statusComment jsForm-field'>" + ((node[nf.STATUS_COMMENT]) ? node[nf.STATUS_COMMENT] : "") + "</textarea>");
					},
					close : function () {
						jq(dlg).remove();
					},
					buttons: [{
							text : "Abbrechen",
							click : function() {
								jq(document).trigger("_save", [{entity: node}]);
								jq(dlg).dialog("close");
							}
						}, {
							text : "Speichern",
							click : function() {
								var txt = jq(dlg).find(".statusComment").val();
								node[nf.STATUS_COMMENT] = txt;
								jq(document).trigger("_save", [{entity: node}]);
								jq(dlg).dialog("close");
							}
						}
					]
				});*/
			}
		},
		
		getTarget : function (ui) {
			var x = jq(ui.target);
			if (!x.hasClass("freebim-contextmenu")) {
				x = x.closest(".freebim-contextmenu");
			}
			return x;
		},
		
		beforeOpen : function (event, ui) {
			var x = at.freebim.db.ctxMenu.getTarget(ui);
	        var db = at.freebim.db, d = db.domain, nf = d.NodeFields, nodeId = x.attr("nodeid"), 
	        	node = d.get(nodeId),
	        	c = db.contributor,
	        	bsdd = false, bsddCl = false, edit = false, tnedit = false, status = true, list = (c != undefined && c.id != undefined),
	        	i18n = net.spectroom.js.i18n;
			
	        if (!node && nodeId != undefined) {
	        	db.post("/relations/get", { nodeId: nodeId }, "", function (response) {
	        		d.listen(response.result);
	            	at.freebim.db.ctxMenu.beforeOpen (event, ui);
	        	}, null , null, "GET");
	        	return;
	        }
	        
	        switch (node[nf.CLASS_NAME]) {
	        case "Company" :
	        case "FreebimUser" :
	        	list = (list && db.user && db.user.usermanager == true);
	        	status = false;
	        	break;
	        case "Contributor" :
	        	status = false;
	        	break;
	        case "ValueList" :
	        	break;
	        case "Library" :
	        	list = (c != undefined && c.id != undefined && c.mayManageLibraries); 
	        	status = false;
	        	break;
	        default: 
	        	bsddCl = true;
        		break;
	        }
	        
	        jq(document).contextmenu("setEntry", "all-marked", db.marked.length + " " + i18n.g_n("CTX_M_MARKED_ELEMENT", db.marked.length));
	        jq(document).contextmenu("setEntry", "all-selected", db.selected.length + " " + i18n.g_n("CTX_M_SELECTED_ELEMENT", db.selected.length));
	        jq(document).contextmenu("showEntry", "show-parameterlist", false);
	        jq(document).contextmenu("showEntry", "divider-1", ((db.marked.length + db.selected.length) > 0));
	        jq(document).contextmenu("showEntry", "all-marked", db.marked.length > 0);
	        jq(document).contextmenu("showEntry", "all-selected", db.selected.length > 0);
	        
	        jq(document).contextmenu("showEntry", "selected-merge", false);
	        if (db.isSelected(x)) {
	        	// current node will be the remaining merged node.
	        	if (db.selected.length > 1) {
		        	var merge = true, i, n = db.selected.length, a = node;
		        	for (i=0; i<n; i++) {
		        		var b = d.get(db.selected[i]);
		        		if (!a || !b || !(a[nf.CLASS_NAME] == b[nf.CLASS_NAME])) {
		        			// merge only nodes of same class!
		        			merge = false;
		        			break;
		        		}
		        	}
        			jq(document).contextmenu("showEntry", "selected-merge", merge);
		        }
	        	jq(document).contextmenu("setEntry", "select", "✗ " + i18n.g("CTX_M_SELECTED_UNSELECT"));
	        } else {
	        	jq(document).contextmenu("setEntry", "select", "✓ " + i18n.g("CTX_M_SELECT"));
	        }
	        if (db.isMarked(x)) {
		        jq(document).contextmenu("setEntry", "mark", "☆ " + i18n.g("CTX_M_MARKED_UNMARK"));
	        } else {
	        	jq(document).contextmenu("setEntry", "mark", "★ " + i18n.g("CTX_M_MARK"));
	        }
	        jq(document).contextmenu("showEntry", "select", false);
	        
        	// may edit phase for all selected nodes?
	        if (db.selected.length > 0) {
	        	var i, n = db.selected.length, p = true;
	        	for (i=0; i<n; i++) {
	        		var e = d.get(db.selected[i]);
	        		if ((!e) || (e[nf.CLASS_NAME] != d.Parameter.className)) {
	        			p = false;
	        			break;
	        		} 
	        	}
	        	jq(document).contextmenu("showEntry", "selected-edit-phase", p);
	        }
	        
	        if (nodeId != undefined && node) {
	        	jq(document).contextmenu("showEntry", "select", d.isSelectable(node));

	        	if (d.mayEdit(node)) {
		        	edit = true;
	        		if (node[nf.CLASS_NAME] == "Component" ) {
	            		tnedit = true;
	        		}
	        	}
	        	bsdd = ((db.bsdd && node[nf.BSDD_GUID] && node[nf.BSDD_GUID].length > 0) ? true : false);
	        	jq(document).contextmenu("showEntry", "show-parameterlist", node[nf.CLASS_NAME] == "Component");

	        	// Status menu items
	        	if (status && ((c && c.maySetStatus && node[nf.STATE] && node[nf.STATE] != d.State.R) 
	        			|| (c && c.maySetReleaseStatus && node[nf.STATE] == d.State.R))) {
	        		jq(document).contextmenu("showEntry", "status", true);
	        		var s, i, n = d.State.values.length;
	        		// menuitem for current state will be disabled,
	        		// so we can only select other states ...
	    			for (i=0; i<n; i++) {
	    				s = d.State.values[i];
	    				jq(document).contextmenu("enableEntry", "setState_" + s, ((s == node[nf.STATE]) ? false : true));
	    			}
	        	} else {
	        		jq(document).contextmenu("showEntry", "status", false);
	        	}

	        }
	        jq(document).contextmenu("showEntry", "bsdd", (bsdd && bsddCl));
	        jq(document).contextmenu("showEntry", "bsdd-fetch", ((c && bsddCl && (!node[nf.LOADED_BSDD] || node[nf.LOADED_BSDD].length < 1)) ? true : false));
	        jq(document).contextmenu("showEntry", "bsdd-fetch-and-save", ((c && bsddCl && (!node[nf.LOADED_BSDD] || node[nf.LOADED_BSDD].length < 1)) ? true : false));
	        jq(document).contextmenu("showEntry", "bsdd-fetch-clear", ((c && bsddCl && node[nf.LOADED_BSDD] && node[nf.LOADED_BSDD].length > 0) ? true : false));
	        jq(document).contextmenu("showEntry", "bsdd-find-dup", (!bsdd && bsddCl));
	        jq(document).contextmenu("showEntry", "bsdd-find-text", bsddCl);
        	jq(document).contextmenu("showEntry", "edit", edit);
        	jq(document).contextmenu("showEntry", "list", list);
        	jq(document).contextmenu("showEntry", "marked_delete", edit);
        	jq(document).contextmenu("showEntry", "newComponent", edit);
        	jq(document).contextmenu("showEntry", "newParameter", edit);
        	jq(document).contextmenu("showEntry", "newComponent", tnedit);
        	jq(document).contextmenu("showEntry", "newParameter", tnedit);
	        jq(document).contextmenu("showEntry", "show-in-tree", ((node && (node[nf.CLASS_NAME] == d.Component.className || node[nf.CLASS_NAME] == d.Parameter.className)) ? true : false));
	        if (!c) {
	        	jq(document).contextmenu("showEntry", "mark", false);
	        	jq(document).contextmenu("showEntry", "select", false);
	        }
        },
		
		selectedChangeState : function (newState) {
			var i, db = at.freebim.db, d = db.domain, nf = d.NodeFields, n = db.selected.length;
        	for (i=0; i<n; i++) {
        		var e = d.get(db.selected[i]);
        		if (e && e[nf.STATE] != newState) {
        			e[nf.STATE] = newState;
        			jq(document).trigger("_save", [{entity: e}]);
        		}
        	}
		},
		
		bsddFetchClear : function (node) {
			// remove all Bsdd relations from passed node
			var d = at.freebim.db.domain, rf = d.RelationFields, nf = d.NodeFields, rels = node[nf.LOADED_BSDD].splice();
			node[nf.LOADED_BSDD] = [];
			jq(document).trigger("_save", [{entity: node, callback: function (e) {
				// e is just the saved node
				jq.each(rels, function (i, r) {
					// r is a Bsdd relation
					d.getOrLoad(r[rf.FROM_NODE], d.Bsdd.className, function (bsddNode) {
						if (!bsddNode[nf.NODES] || bsddNode[nf.NODES].length == 0) {
							// delete that BsddNode entity since it's not referenced anymore
							jq(document).trigger("_delete", [{entity: bsddNode, confirmed: true}]);
						}
					});
				});
			}}]);
		},
		
		bsddFetch : function (node, doSave, rec, first) {
			
			if (!node) {
				return;
			}
			
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, i18n = net.spectroom.js.i18n;
			
			if (db.ctxMenu.bsddFetched[node[nf.NODEID]] != undefined) {
				return;
			}
			db.ctxMenu.bsddFetched[node[nf.NODEID]] = node[nf.CLASS_NAME];
			
			setTimeout(function () {
				db.ctxMenu.bsddFetchSingleNode(node, first, function (count) {
					
					if (count === 0 
							&& doSave === true 
							&& !db.bsdd.isValidGuid(node[nf.BSDD_GUID])
							&& (!node[nf.LOADED_BSDD] || node[nf.LOADED_BSDD].length === 0)
							&& (node[nf.CLASS_NAME] === d.Parameter.className) 
							&& (node[nf.STATE] && (node[nf.STATE] === d.State.P || node[nf.STATE] === d.State.R)) // state is CHECKED or RELEASED
							) {
						// did fetch, but result was empty
						// If that node was a Parameter
						// and user triggered a 'fetch and save' command
						// create a new IfdConcept and force an upload to buildingsmart.org!
						var c = db.bsdd.createIfdConceptForNode(node);
						db.bsdd.upload(c, function (guid) {
							node[nf.BSDD_GUID] = guid;
							jq(document).trigger("_save", [{entity: node}]);
						});
						
					} else {
						// done. Either a BsddNode is saved (if guids found),
						// or nothing was found and nothing was saved.
						
						// prepare to store fetched info state to involved nodes.
						if (db.ctxMenu.bsddFetchedTimer) {
							clearTimeout(db.ctxMenu.bsddFetchedTimer);
						}
						db.ctxMenu.bsddFetchedTimer = setTimeout(function () {
							
							var i, k, keys = Object.keys(db.ctxMenu.bsddFetched), n;

							db.post("/bsdd/spreadToEqualNodes", {}, i18n.g("REFRESH_EQ_RELATIONS"), function (affectedNodes) {
								jq.each(affectedNodes.result, function (idx, id) {
									id = "" + id;
									if (keys.indexOf(id) < 0) {
										var relatedNode = d.get(id);
										if (relatedNode) {
											keys.push(id);
											db.ctxMenu.bsddFetched[id] = relatedNode[nf.CLASS_NAME];
										}
									}
								});
								// Load the nodes ...
								n = keys.length;
								for (i=0; i<n; i++) {
									k = keys[i];
									d.loadNode(k, db.ctxMenu.bsddFetched[k], function () {
									});
								}
								db.ctxMenu.bsddFetched = {};
							});
							
						}, 1000);
					}
					
					
						
					//  ... proceed with related nodes.
					if (node[nf.CLASS_NAME] === d.Component.className) {
						var rels = node[nf.EQUALS];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an Equals relation
								if (r[rf.QUALITY] >= 1) {
									d.rel.Equals.getOtherNode(node, r, function (eq) {
										// use the equal node to get some more possible guids,
										// but don't traverse the tree down for that equal node.
										db.ctxMenu.bsddFetch(eq, doSave, false, false);
									});
								}
							});
						}
						rels = node[nf.CHILDS];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing ParentOf relation
								d.getOrLoad(r[rf.TO_NODE], d.Component.className, function (child) {
									db.ctxMenu.bsddFetch(child, doSave, rec, false);
								});
							});
						}
						rels = node[nf.HAS_PARAMETER];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing HasParameter relation
								d.getOrLoad(r[rf.TO_NODE], d.Parameter.className, function (param) {
									db.ctxMenu.bsddFetch(param, doSave, rec, false);
								});
							});
						}
					} else if (node[nf.CLASS_NAME] === d.Parameter.className) {
						rels = node[nf.MEASURES];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing HasMeasure relation
								d.getOrLoad(r[rf.TO_NODE], d.Measure.className, function (m) {
									db.ctxMenu.bsddFetch(m, doSave, rec, false);
								});
							});
						}
					} else if (node[nf.CLASS_NAME] === d.Measure.className) {
						rels = node[nf.UNIT];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing OfUnit relation
								d.getOrLoad(r[rf.TO_NODE], d.Unit.className, function (unit) {
									db.ctxMenu.bsddFetch(unit, doSave, rec, false);
								});
							});
						}
						rels = node[nf.HAS_VALUE];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing HasValue relation
								d.getOrLoad(r[rf.TO_NODE], d.ValueList.className, function (vl) {
									db.ctxMenu.bsddFetch(vl, doSave, rec, false);
								});
							});
						}
					} else if (node[nf.CLASS_NAME] === d.ValueList.className) {
						rels = node[nf.HAS_ENTRY];
						if (rels) {
							jq.each(rels, function (i, r) {
								// r is an outgoing HasEntry relation
								d.getOrLoad(r[rf.TO_NODE], d.ValueListEntry.className, function (vle) {
									db.ctxMenu.bsddFetch(vle, doSave, rec, false);
								});
							});
						}
					}
						
					
				});
			}, 10);
		},
		bsddFetchSingleNode : function (node, first, callback) {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields;
			// fetch BsDD for duplicates and save the retrieved guids as BsddNode entities
			// to be assigned later by a contributor.
			if (db.bsdd 
					&& node 
					&& !node[nf.BSDD_GUID] // no bsDD-Guid assigned
					&& (!node[nf.LOADED_BSDD] || node[nf.LOADED_BSDD].length < 1) // no possible bsDD matches
					&& d.relevantId(node[nf.CLASS_NAME], node[nf.NODEID])
				) {

	    		var s = [], t = db.bsdd.getConceptTypeFromClassName(node[nf.CLASS_NAME]), l = [],
	    		getBsddNode = function (value, forNode, doneFn) {
					db.post("/bsdd/getByGuid",
							{ guid: value },
							"", function (response) {
								var bsddNode = null, i, n, exists = false;
								if (response && response.result) {
									// got existing BsddNode
									bsddNode = response.result;
								} else {
									// create new BsddNode
									bsddNode = {};
									bsddNode[nf.CLASS_NAME] = d.BsddNode.className;
									bsddNode[nf.BSDD_GUID] = value;
								}
								bsddNode[nf.NODES] = bsddNode[nf.NODES] || [];
								n = bsddNode[nf.NODES].length;
								// is there a relation (bsddNode)-[:Bsdd]->(forNode)
								for (i=0; i<n; i++) {
									if (bsddNode[nf.NODES][i][rf.TO_NODE] == forNode[nf.NODEID]) {
										exists = true;
										break;
									}
								}
								if (!exists) {
									// create a new Bsdd relation
									var bsddRel = {};
									bsddRel[rf.ID] = null;
									bsddRel[rf.TYPE] = d.RelationTypeEnum.BSDD;
									bsddRel[rf.FROM_NODE] = bsddNode[nf.NODEID];
									bsddRel[rf.TO_NODE] = forNode[nf.NODEID];
									bsddNode[nf.NODES].push(bsddRel);
									
									d.addModifiedNode(forNode[nf.NODEID]);
									jq(document).trigger("_save", [{entity: bsddNode, callback: function (e) {
										doneFn();
									}}]);
								}
							},
							function (error) {
								doneFn();
							}, null, "GET");
	    		};

	    		if (t) {
	    			net.spectroom.js.cookie.set(db.bsdd.lastSearchType, t, 14);
	    			s.push(node[nf.NAME]);
	    			l.push("GERMAN");
    			
    				if (node[nf.NAME_EN] && node[nf.NAME_EN].length > 0) {
    					s.push(node[nf.NAME_EN]);
    					l.push("ENGLISH");
    				}
    				d.BsddNode.init();
    				jq(document).trigger("IfdConcept.fetch", [{ 
    					type: t, 
    					search: s, 
    					lang: l, 
    					freebimId: node[nf.FREEBIM_ID], 
    					callback: function (guids) {
    						// create and save BsddNode entities ...
    						if (guids && guids.length > 0) {
    							var count = guids.length, 
    							fin = function (n) {
									setTimeout(function () {
										callback(n);
									}, 2000);
								};
    							jq.each(guids, function (index, value) {
    								// get the BsddNode for that guid from database ...
    								getBsddNode(value, node, function () {
    									count--;
    									if (count === 0) {
											fin(guids.length);
										}
    								});
	    						});
    						} else {
    							callback(0);
    						}
    					}
    				}]); // trigger fetch
	    			return;
	    		}
			} 
			callback(undefined);
		},
		
		bsddSearchCallback : function (node, guid) {
			var d = at.freebim.db.domain, rf = d.RelationFields, nf = d.NodeFields;
			node[nf.BSDD_GUID] = guid;
			jq(document).trigger("_save", [{entity: node}]);
			if (node[nf.EQUALS]) {
				var i, otherId, n = node[nf.EQUALS].length, r, eq;
				for (i=0; i<n; i++) {
					r = node[nf.EQUALS][i];
					otherId = ((r[rf.FROM_NODE] == node[nf.NODEID]) ? r[rf.TO_NODE] : r[rf.FROM_NODE]);
					eq = at.freebim.db.nodes["" + otherId];
					if (eq) {
						eq[nf.BSDD_GUID] = guid;
						jq(document).trigger("_save", [{entity: eq}]);
					} else {
						jq(document).one("newNodeModified", function (event, data) {
							var entity = data.entity;
							if (entity && entity[nf.NODEID] == otherId) {
								entity[nf.BSDD_GUID] = guid;
								jq(document).trigger("_save", [{entity: entity}]);
							}
						});
						jq(document).trigger("getNode", [{nodeId: otherId, cn: node[nf.CLASS_NAME]}]);
					}
				}
			}
		},
		bsddFindDup : function (node) {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
			if (db.bsdd) {
	    		var s = [], t = db.bsdd.getConceptTypeFromClassName(node[nf.CLASS_NAME]), l = [];

	    		if (t) {
	    			net.spectroom.js.cookie.set(db.bsdd.lastSearchType, t, 14);
	    			s.push(node[nf.NAME]);
	    			l.push("GERMAN");
	    			if (s) {
	    				if (node[nf.NAME_EN] && node[nf.NAME_EN].length > 0) {
	    					s.push(node[nf.NAME_EN]);
	    					l.push("ENGLISH");
	    				}
	    				jq(document).trigger("IfdConcept.searchForDuplicates", [{ 
	    					type: t, 
	    					search: s, 
	    					lang: l, 
	    					freebimId: node[nf.FREEBIM_ID], 
	    					callback: function (guid) {
	    						db.ctxMenu.bsddSearchCallback(node, guid);
	    					}
	    				}]);
	    			}
	    		} else {
	    			alert(i18n.g("BSDD_FIND_DUP_OF_NIY", node[nf.CLASS_NAME])); // "Such nach Duplikaten des Typs '" + node[nf.CLASS_NAME] + "' noch nicht implementiert.");
	    		}
			}
		},
		
		bsddFindText : function (node) {
			var db = at.freebim.db, nf = db.domain.NodeFields, i18n = net.spectroom.js.i18n;
			if (db.bsdd) {
				var dlg = net.spectroom.js.newDiv(), 
					type = db.bsdd.getConceptTypeFromClassName(node[nf.CLASS_NAME]), x = jq(dlg);

				x.dialog({
					title : i18n.g("DLG_TITLE_BSDD_FIND"),
					width: 400,
					height: 300,
					modal : true,
					open : function () {
						var t = "";
						t += "Type:<select class=\"bsdd-find-type\">";
						t += "<option value=\"ACTOR\">ACTOR</option>";
						t += "<option value=\"ACTIVITY\">ACTIVITY</option>";
						t += "<option value=\"DOCUMENT\">DOCUMENT</option>";
						t += "<option value=\"PROPERTY\">PROPERTY</option>";
						t += "<option value=\"SUBJECT\">SUBJECT</option>";
						t += "<option value=\"UNIT\">UNIT</option>";
						t += "<option value=\"MEASURE\">MEASURE</option>";
						t += "<option value=\"VALUE\">VALUE</option>";
						t += "<option value=\"NEST\">NEST</option>";
						t += "<option value=\"BAG\">BAG</option>";
						t += "<option value=\"CLASSIFICATION\">CLASSIFICATION</option>";
						t += "</select>";
						t += "<br/>";
						t += "Text (deutsch): <input type=\"text\" class=\"bsdd-find-text-de\">";
						t += "<br/>";
						t += "Optional Text (englisch): <input type=\"text\" class=\"bsdd-find-text-en\">";
						t += "<br/>";
						t += "Optional Text (IFC): <input type=\"text\" class=\"bsdd-find-text-ifc\">";
						t += "<br/>";
						t += "<input type=\"button\" class=\"bsdd-simplify-btn\" i18n='BSDD_FIND_SIMPLIFY' value=\"" + i18n.g("BSDD_FIND_SIMPLIFY") + "\" >";
						t += "<input type=\"button\" class=\"bsdd-copy-btn\" value=\"de --> en\" >";
						x.html(t);
						jq("#" + dlg.id + " .bsdd-simplify-btn").click(function () {
						    var simplify = function(id) {
						    	var x = jq(id), txt = x.val();
						    	txt = db.bsdd.simplifyText(txt);
						    	x.val(txt);
						    };
						    simplify("#" + dlg.id + " .bsdd-find-text-de");
						    simplify("#" + dlg.id + " .bsdd-find-text-en");
						    simplify("#" + dlg.id + " .bsdd-find-text-ifc");
						});
						jq("#" + dlg.id + " .bsdd-copy-btn").click(function () {
							var txt = jq("#" + dlg.id + " .bsdd-find-text-de").val();
							jq("#" + dlg.id + " .bsdd-find-text-en").val(txt);
						});
						if (type) {
							jq("#" + dlg.id + " .bsdd-find-type").val(type);
						}
						if (node[nf.NAME]) {
							jq("#" + dlg.id + " .bsdd-find-text-de").val(node[nf.NAME]);
						}
						if (node[nf.NAME_EN]) {
							jq("#" + dlg.id + " .bsdd-find-text-en").val(node[nf.NAME_EN]);
						}
					},
					close : function () {
						x.remove();
					},
					buttons : [{
						text : i18n.getButton("DLG_BTN_CANCEL"), // "Abbrechen",
						click : function() {
							jq(dlg).dialog("close");
						}
					}, {
						text : i18n.getButton("DLG_BTN_FIND_IN_BSDD"), // Suche in bsDD",
						click : function (form, suffix) {
						    var de = jq("#" + dlg.id + " .bsdd-find-text-de").val(),
				    			en = jq("#" + dlg.id + " .bsdd-find-text-en").val(),
				    			ifc = jq("#" + dlg.id + " .bsdd-find-text-ifc").val(),
					    		s = [], l = [];
						    type = jq("#" + dlg.id + " .bsdd-find-type").val();
						    net.spectroom.js.cookie.set(db.bsdd.lastSearchType, type, 14);
						    if (de && de.length > 0) {
						    	s.push(de);
						    	l.push("GERMAN");
						    }
					    	if (en && en.length > 0) {
						    	s.push(en);
						    	l.push("ENGLISH");
					    	}
					    	if (ifc && ifc.length > 0) {
						    	s.push(ifc);
						    	l.push("IFC");
					    	}
					    	if (s.length > 0) {
						    	jq(document).trigger("IfdConcept.searchForDuplicates", [{ 
						    		type: type, 
						    		search: s, 
						    		lang: l, 
						    		strict: true,
						    		freebimId: node[nf.FREEBIM_ID], 
		        					callback: function (guid) {
		        						db.ctxMenu.bsddSearchCallback(node, guid);
		        					}
		        				}]);
					    	} else {
						    	jq(document).trigger("alert", [{
									title: "DLG_TITLE_FREEBIM_INFO", 
									content: i18n.g("BSDD_NO_TEXT_SPECIFIED"), // "kein Suchtext angegeben.",
									autoClose : 1500
								}]);
						    	return;
						    }
						    jq(dlg).dialog("close");
						}
					}]
				}).prev().attr("i18n_dlg", "DLG_TITLE_BSDD_FIND");	
			}
		},
		
		handle : function(ui, node) {
			var db = at.freebim.db, m = db.ctxMenu, d = db.domain, rf = d.RelationFields, nf = d.NodeFields, i18n = net.spectroom.js.i18n;
			if (!node) {
				return;
			}
		    if (node) {
		        switch (ui.cmd) {
		        case "edit" : 
		        	jq(document).trigger("_editEvent", [{entity : node}]); 
		        	break;
		        case "find" : 
		        	db.tabs.freebim_search();
		        	jq(db.search).trigger("find", [{searchString: node[nf.NAME]}]); 
		        	break;
		        case "list" : 
		        	d[node[nf.CLASS_NAME]].selectedNodeId = node[nf.NODEID];
		        	jq(db.tabs).trigger("showTab", [{tab : node[nf.CLASS_NAME]}]);
		        	break;
		        case "bsdd" :
		       		jq(document).trigger("IfdConcept/GET", [{guid: node[nf.BSDD_GUID]}]);
		        	break;
		        case "bsdd-fetch":
		        	m.bsddFetched = {};
		        	m.bsddFetch(node, false, true, true);
		        	break;
		        case "bsdd-fetch-and-save":
		        	m.bsddFetched = {};
		        	m.bsddFetch(node, true, true, true);
		        	break;
		        case "bsdd-fetch-clear":
		        	m.bsddFetchClear(node);
		        	break;
		        case "bsdd-find-dup":
		        	m.bsddFindDup(node);
		        	break;
		        case "bsdd-find-text":
		        	m.bsddFindText(node);
		        	break;
		        case "mark" : 
		        	db.mark(at.freebim.db.ctxMenu.getTarget(ui));
		        	jq(document).trigger("filter", [{}]);
		        	break;
		        case "marked_unmark" :
		        	db.unmarkAll();
		        	jq(document).trigger("filter", [{}]);
		        	break;
		        case "rel" : 
		       		db.relations.init();
		        	jq(db.relations).trigger("showNode", [{nodeId: node[nf.NODEID]}]);
		        	jq(db.tabs).trigger("showTab", [{tab: "freebim_rel"}]);
		        	break;
			    case "newComponent" : 
			    	d.Component.init();
			    	if (node[nf.NODEID]) {
			    		var entity;
			    		try {
			    			entity = d.Component.form.newEntity();
			    		} catch (e) {
			    			db.logger.error("Error creating new Component: " + e.message);
			    			return; 
			    		}
			    		if (entity) {
			    			// create a new PARENT_OF relation
			    			var rel = {};
		    				rel[rf.ID] = null;
		    				rel[rf.TYPE] = d.RelationTypeEnum.PARENT_OF; 
		    				rel[rf.TO_NODE] = null; // this will be the id of the new Component ...
		    				rel[rf.FROM_NODE] = node[nf.NODEID]; 
		    				rel[rf.ORDERING] = ((node[nf.CHILDS]) ? node[nf.CHILDS].length : 0); // add new Component at the end

			    			entity[nf.PARENTS] = [rel];
			    		}
			    		d.addModifiedNode(node[nf.NODEID]);
			    		jq(document).trigger("_addEvent", [{entity : entity, cn: d.Component.className}]);
			    	}
			    	break;
			    case "newParameter" :
			    	d.Parameter.init();
			    	if (node[nf.NODEID]) {
			    		var entity;
			    		try {
			    			entity = d.Parameter.form.newEntity();
			    		} catch (e) {
			    			db.logger.error("Error creating new Parameter: " + e.message);
			    			return; 
			    		}
			    		if (entity) {
			    			// create a new HAS_PAREMETER relation
			    			var rel = {};
			    			rel[rf.ID] = null;
			    			rel[rf.TYPE] = d.RelationTypeEnum.HAS_PARAMETER;
			    			rel[rf.FROM_NODE] = node[nf.NODEID];
			    			rel[rf.TO_NODE] = null; // this will be the id of the new Parameter ...
			    			rel[rf.PHASE] = "";
			    			rel[rf.ORDERING] = ((node[nf.HAS_PARAMETER]) ? node[nf.HAS_PARAMETER].length : 0); // add new Parameter at the end
							
							// rel is the just created relation
							d.rel.HasParameter.init();
							jq(d.rel.HasParameter).trigger("editPhase", [{rel: rel, okFn: function() {
			        			entity[nf.HP] = [rel];
				        		jq(document).trigger("_addEvent", [{entity : entity, cn: d.Parameter.className}]);
							}}]);
			    		}
			    	}
			    	break;
			    case "show-parameterlist":
			    	jq(document).trigger("show-parameterlist", [{nodeId: node[nf.NODEID]}]);
			    	break;
			    	
			    case "setState_0": m.changeState(node, 0); break;
			    case "setState_1": m.changeState(node, 1); break;
			    case "setState_2": m.changeState(node, 2); break;
			    case "setState_3": m.changeState(node, 3); break;
			    case "setState_4": m.changeState(node, 4); break;
			    case "setState_5": m.changeState(node, 5); break;
			    case "setState_6": m.changeState(node, 6); break;
			    
			    case "show-in-tree":
			    	jq(db.tabs).trigger("showTab", [{tab: "freebim_tree" }]);
			    	db.tree.openInTree(node, node); 
			    	break;

			    case "select":
					db.select(at.freebim.db.ctxMenu.getTarget(ui));
			    	break;
			    case "selected-setState_0": m.selectedChangeState(0); break;
			    case "selected-setState_1": m.selectedChangeState(1); break;
			    case "selected-setState_2": m.selectedChangeState(2); break;
			    case "selected-setState_3": m.selectedChangeState(3); break;
			    case "selected-setState_4": m.selectedChangeState(4); break;
			    case "selected-setState_5": m.selectedChangeState(5); break;
			    case "selected-setState_6": m.selectedChangeState(6); break;
				case "selected-unselect":
		        	db.unselectAll();
					break;
				case "selected-merge" :
					var b, i, n = db.selected.length, msg1 = "", msg2 = "";
					for (i=0; i<n; i++) {
						b = d.get(db.selected[i]);
						msg1 += "<div class='freebim-item info-only " + b[nf.CLASS_NAME] + "'>" + b[nf.NAME] + "</div>";
					}
					msg2 += "<div class='freebim-item info-only " + node[nf.CLASS_NAME] + "'>" + node[nf.NAME] + "</div>";
					jq(document).trigger("confirm", [{
						msg: i18n.g2("MERGE_ELEMENTS_CONFIRM", msg1, msg2),
						yes: function() {
							if (n > 1) {
								for (i=0; i<n; i++) {
					        		b = d.get(db.selected[i]);
					        		if (b[nf.NODEID] == node[nf.NODEID]) {
					        			continue;
					        		}
									db.merge.merge(node, b);
						        	jq(document).trigger("_delete", [{entity: b, confirmed: true}]);
					        	}
					        	jq(document).trigger("_save", [{entity: node}]);
							}
						}
					}]);
					break;
				case "selected-edit-phase":
					if (db.selected.length > 0) {
			        	var r = null, i, n = db.selected.length;
			        	for (i=0; i<n; i++) {
			        		var e = d.get(db.selected[i]);
			        		if (e[nf.CLASS_NAME] == d.Parameter.className) {
			        			if (e[nf.HP].length > 0) {
			        				r = e[nf.HP][0];
			        			}
			        		} 
			        		if (r) {
			        			break;
			        		}
			        	}
			        	if (r) {
		        			// r is a HasParameter relation
			        		d.rel.HasParameter.init();
			        		jq(d.rel.HasParameter).trigger("editPhase", [{rel: r, okFn: function (ph) {
			        			for (i=0; i<n; i++) {
					        		var j, mc, e = d.get(db.selected[i]);
					        		if (e[nf.CLASS_NAME] == d.Parameter.className) {
					        			mc = e[nf.HP].length;
					        			for (j=0; j<mc; j++) {
					        				e[nf.HP][j][rf.PHASE] = ph[nf.FREEBIM_ID];
					        			}
					        		}
				        			jq(document).trigger("_save", [{entity:e}]);
					        	}
			        		}}]);
		        		}
					}
					break;
		        } // switch(cmd)
		    } // if(node)
		},
		
		init : function () {
			var db = at.freebim.db, d = db.domain, nf = d.NodeFields, s, stateChilds = [], stateChilds2 = [], i, n = d.State.values.length, i18n = net.spectroom.js.i18n;
			for (i=0; i<n; i++) {
				s = d.State.values[i];
				if (s == d.State.R && (!db.contributor || !db.contributor.maySetReleaseStatus)) {
					continue;
				}
				stateChilds.push({cmd: "setState_" + s, title: d.State.data(s)});
				stateChilds2.push({cmd: "selected-setState_" + s, title: d.State.data(s)});
			}
			jq(document).contextmenu({
				delegate: '.freebim-contextmenu', 
			    menu: [
					{cmd: "newComponent", title: "✚︎ " + i18n.get("CTX_M_NEW_COMPONENT")},
					{cmd: "newParameter", title: "✚︎ " + i18n.get("CTX_M_NEW_PARAMETER")},
			        {cmd: "edit", title: "✎ " + i18n.get("CTX_M_EDIT")},
			        {cmd: "find", title: "～ " + i18n.get("CTX_M_FIND_SIMILAR")},
			        {cmd: "rel", title: "⇄ " + i18n.get("CTX_M_RELATIONS")},
			        {cmd: "list", title: "≣ " + i18n.get("CTX_M_LIST")},
			        {cmd: "show-parameterlist", title: "∑ " + i18n.get("CTX_M_PARAM_LIST")},
			        {cmd: "show-in-tree", title: "├ " + i18n.get("CTX_M_SHOW_IN_STRUCTURE")},
		            {cmd: "status", title: "± " + i18n.get("CTX_M_STATUS") + " ", children : stateChilds },
			        {cmd: "bsdd", title: i18n.get("CTX_M_BSDD_OBJECT"), uiIcon: "ifc-logo"  },
			        {cmd: "bsdd-fetch", title: i18n.get("CTX_M_BSDD_LOAD"), uiIcon: "ifc-logo"  },
			        {cmd: "bsdd-fetch-and-save", title: i18n.get("CTX_M_BSDD_LOAD_SAVE"), uiIcon: "ifc-logo"  },
			        {cmd: "bsdd-fetch-clear", title: i18n.get("CTX_M_BSDD_REMOVE_LOADED"), uiIcon: "ifc-logo"  },
			        {cmd: "bsdd-find-dup", title: i18n.get("CTX_M_BSDD_FIND"), uiIcon: "ifc-logo"  },
			        {cmd: "bsdd-find-text", title: i18n.get("CTX_M_BSDD_FIND_TEXT"), uiIcon: "ifc-logo"  },
			        {cmd: "mark", title: "★ " + i18n.g("CTX_M_MARK") + " <kbd>[CMD-click]</kbd>" },
                    {cmd: "select", title: "✓ " + i18n.g("CTX_M_SELECT") + " <kbd>[ALT-click]</kbd>"  },
			        {cmd: "divider-1", title: "---------"},
			        {cmd: "all-marked", title: "★ " + i18n.g("CTX_M_MARKED_ELEMENTS"), 
		                children: [
		                    {cmd: "marked_unmark", title: "☆ " + i18n.get("CTX_M_MARKED_UNMARK")}/*,
		                    {cmd: "marked_delete", title: "☓ Löschen"}*/
		                ]
		            },
			        {cmd: "all-selected", title: "✓ " + i18n.g("CTX_M_SELECTED_ELEMENTS"), 
		                children: [
				                    {cmd: "selected-unselect", title: "✗ " + i18n.get("CTX_M_SELECTED_UNSELECT") + " "},
				                    {cmd: "selected-edit-phase", title: "♺ " +i18n.get("CTX_M_EDIT_PHASE") + " "},
				                    {cmd: "selected-state", title: "± " + i18n.get("CTX_M_STATUS") + " ", children : stateChilds2 },
				                    {cmd: "selected-merge", title: "⋃ " + i18n.get("CTX_M_MERGE") + " "}
		                ]
		            }

			    ],
		        beforeOpen: function (event, ui) {
		        	at.freebim.db.ctxMenu.beforeOpen (event, ui);
		        },
			    select: function(event, ui) {
//			    	event.preventDefault();
//			    	event.stopPropagation();
//			    	jq(document).contextmenu("close");
			    	var x = at.freebim.db.ctxMenu.getTarget(ui);
			        var nodeId = x.attr("nodeid"), node = d.get(nodeId);
			        if (nodeId && !node) {
			        	var c = x.attr("c");
			        	if (c) {
			        		if (d[c]) {
			        			d[c].init();
			        		}
			        		jq("." + c + "[nodeid='" + nodeId + "']").one("newNodeModified", function(e, data) {
				        		try {
				        			db.ctxMenu.handle(ui, d.get(nodeId));
				        		} catch (e) {
				        			db.logger.error("Error in db.ctxMenu.handle: " + e.message);
				        		}
				        	});
				        	jq(document).trigger("getNode", [{nodeId: nodeId, cn: c}]);
			        	}
			        }
			        try {
			        	db.ctxMenu.handle(ui, node);
			        } catch (e) {
			        	db.logger.error("Error in db.ctxMenu.handle: " + e.message);
			        }
			    }
			});
			
/*			jq("body").contextmenu({
				delegate: "form p label",
				menu: [
				        {cmd: "bsdd", title: "bsDD - Objekt", uiIcon: "ifc-logo"  },
				        {cmd: "bsdd-find-dup", title: "bsDD - Suche", uiIcon: "ifc-logo"  },
				        {cmd: "bsdd-find-text", title: "bsDD - Suche Text", uiIcon: "ifc-logo"  },
				        {cmd: "convert", title: "GUID konvertieren", uiIcon: "ifc-logo"  },
				        {cmd: "save-to-bsdd", title: "in bsDD übernehmen", uiIcon: "ifc-logo"  }
				       ],
				beforeOpen: function(event, ui) {
					var x = jq(ui.target).parent().siblings("input[name='nodeId']"), nodeId = x.val(), 
		        		node = d.get(nodeId);
					jq("body").contextmenu("showEntry", "bsdd", false);
					jq("body").contextmenu("showEntry", "convert", false);
					jq("body").contextmenu("showEntry", "bsdd-find-dup", false);
					jq("body").contextmenu("showEntry", "bsdd-find-text", false);
					jq("body").contextmenu("showEntry", "save-to-bsdd", false);
					if (node) {
						var p = jq(ui.target).parent(), bsdd = ((node[nf.BSDD_GUID] && node[nf.BSDD_GUID].length > 1) ? true : false);
						if (jq(p).hasClass("GUID")) {
							jq("body").contextmenu("showEntry", "bsdd", bsdd);
							jq("body").contextmenu("showEntry", "convert", bsdd);
							jq("body").contextmenu("showEntry", "bsdd-find-dup", !bsdd);
							jq("body").contextmenu("showEntry", "bsdd-find-text", !bsdd);
						} else {
							if (bsdd) {
								if (jq(p).hasClass("NAME") 
										|| jq(p).hasClass("NAME-EN")
										|| jq(p).hasClass("DESC")
										|| jq(p).hasClass("DESC-EN")
										|| jq(p).hasClass("CODE")
										) {
									jq("body").contextmenu("showEntry", "save-to-bsdd", true);
								}
							}
						}
					}
				},
				select: function(event, ui) {
					var x = jq(ui.target).parent().siblings("input[name='nodeId']"), nodeId = x.val(), 
			    		node = d.get(nodeId);
					if (node) {
						switch (ui.cmd) {
				        case "bsdd":
				        	if (db.bsdd) {
					        	db.bsdd.loadByGuid(node[nf.BSDD_GUID]);
				        	}
				        	break;
				        case "convert" :
				        	var val = jq(ui.target).next().val();
							val = com.muigg.ifc.IfcGuid.convert(val);
							jq(ui.target).next().val(val);
				        	break;
				        case "bsdd-find-dup":
				        	db.ctxMenu.bsddFindDup(node);
				        	break;
				        case "bsdd-find-text":
				        	db.ctxMenu.bsddFindText(node);
				        	break;
				        case "save-to-bsdd":
				        	var t = jq(ui.target).parent(), type = ifd.IfdNameTypeEnum.FULLNAME, lang;
				        	if (t.hasClass("NAME")) {
				        		type = ifd.IfdNameTypeEnum.FULLNAME;
				        		lang = "de-AT";
				        	} else if (t.hasClass("CODE")) {
				        		type = ifd.IfdNameTypeEnum.SHORTNAME;
				        		lang = "de-AT";
				        	} else if (t.hasClass("NAME-EN")) {
				        		type = ifd.IfdNameTypeEnum.FULLNAME;
				        		lang = "en";
				        	} else if (t.hasClass("DESC")) {
				        		type = ifd.IfdDescriptionTypeEnum.DEFINITION;
				        		lang = "de-AT";
				        	} else if (t.hasClass("DESC-EN")) {
				        		type = ifd.IfdDescriptionTypeEnum.DEFINITION;
				        		lang = "en";
				        	}
				        	db.bsdd.loadByGuid(node[nf.BSDD_GUID], function (ifdConcept) {
			    				var txt = jq(ui.target).next().val();
			    				jq(document).trigger("confirm", [{
			    					msg: "<p>wollen Sie den Text '" + txt + "'<br/>als '" + type + "'<br/>in der Sprache '" + lang + "'<br/> ins bsDD übertragen?</p>",
			    					yes: function() {
		    							var langGuid = db.bsdd.languages[lang].guid,
		    								guid = node[nf.BSDD_GUID]; //com.muigg.ifc.IfcGuid.convert(node[nf.BSDD_GUID]);
		    							switch (type) {
		    							case ifd.IfdNameTypeEnum.SHORTNAME:
		    							case ifd.IfdNameTypeEnum.FULLNAME:
		    								jq(document).trigger("IfdConcept/guid/name", [{guid: guid, languageGuid: langGuid, name: txt, nameType: type, callback: function (ifdBase) {
		    									jq(document).trigger("alert", [{
		    										title: "DLG_TITLE_FREEBIM_INFO", 
		    										content: "... erfolgreich gespeichert."
		    									}]);
		    									db.bsdd.loadByGuid(node[nf.BSDD_GUID]);
		    								}}]);
		    								break;
		    							case ifd.IfdDescriptionTypeEnum.DEFINITION:
		    								jq(document).trigger("IfdConcept/guid/definition", [{guid: guid, languageGuid: langGuid, definition: txt, callback: function (ifdBase) {
		    									jq(document).trigger("alert", [{
		    										title: "DLG_TITLE_FREEBIM_INFO", 
		    										content: "... erfolgreich gespeichert."
		    									}]);
		    									db.bsdd.loadByGuid(node[nf.BSDD_GUID]);
		    								}}]);
		    								break;
		    							}
		    						}
			    				}]);
			        		});
				        	break;
						}
					}
				}
			});
*/			
/*	
			jq("#freebim_tree").contextmenu({
				delegate: ".ui-layout-pane",
				menu: [
				        {cmd: "collapse-all", title: "├ Struktur schließen", uiIcon: ""  }
				       ],
				beforeOpen: function(event, ui) {
					var x = jq(ui.target), id = x.attr("id");
					if (id) {
						switch (id) {
						default:
							return false;
							
						case "root1":
						case "root2":
						case "root3":
							return true;
						}
					}
					return false;
				},
				select: function(event, ui) {}
				}
			);
*/
		}
		
};

jq(document).ready(
	function() {
		try {
			at.freebim.db.ctxMenu.init();
		} catch (e) {
			at.freebim.db.logger.error("Error in at.freebim.db.ctxMenu.init: " + e.message);
		}
	}
);
