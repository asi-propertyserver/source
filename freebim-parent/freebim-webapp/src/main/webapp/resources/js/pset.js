/**
 * 
 */
at.freebim.db.bsdd.pset = {
		
	timers : {},
	
	processTree : function (node, libId) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, ps = at.freebim.db.bsdd.pset, fa = net.spectroom.js.forAll, i18n = net.spectroom.js.i18n;
		
		// for all Components of Ifc4 Library
		fa (node[nf.CHILDS], function (r, i) {
		
			// r is a PARENT_OF relation
			d.getOrLoad(r[rf.TO_NODE], d.Component.className, function (comp) {
				
				ps.processTree(comp, libId);
				
				// comp is a Component
				if (db.bsdd.isValidGuid(comp[nf.BSDD_GUID])) {
					
					// fetch children of that component
					jq(document).trigger("IfdConcept.children", [{guid: comp[nf.BSDD_GUID], silent: true, callback: function (childs) {
						
						// for all childs
						fa(childs.IfdConceptInRelationship, function (child, i) {
							switch (r.relationshipType) {
							case ifd.IfdRelationshipTypeEnum.NEST:
								var names = ps.getNames(child);
								if (names.ifc.indexOf("Pset_") == 0) {
									// it's a property set!
									
									// do we have it already?
									db.post("/search/search", {searchstring: child.guid, searchtype: "bsdd-guid"}, i18n.g("BSDD_FIND_GUID"), function (resp) {
										var pset = null;
										if (!resp.result || resp.result.length == 0) {
											// no, create a new one
											var ref = {};
											ref[rf.TYPE] = d.RelationTypeEnum.REFERENCES;
											ref[rf.TO_NODE] = libId;
											pset = {
													needsSave: true
											};
											pset[nf.BSDD_GUID] = child.guid;
											pset[nf.CLASS_NAME] = d.ParameterSet.className;
											pset[nf.OWNERS] = [];
											pset[nf.REFERENCES] = [ref]; // REFERENCES relation
											pset[nf.NAME] = names.name;
											pset[nf.NAME_EN] = names.nameEn;
											pset[nf.DESC] = names.desc;
											pset[nf.DESC_EN] = names.descEn;
											pset[nf.TYPE] = 1; // ParameterSetType.PROPERTYSET
											
										} else {
											// yes, take it
											pset = resp.result[0];
										}
										if (!pset[nf.OWNERS]) {
											pset[nf.OWNERS] = [];
										}
										var found = false;
										for (var i=0; i<pset[nf.OWNERS].length; i++) {
											if (pset[nf.OWNERS][i][rf.FROM_NODE] === comp[nf.NODEID]) {
												found = true;
												break;
											}
										}
										if (!found) {
											// create a new relation
											// (:Component)-[:HAS_PARAMETER_SET]->(:ParameterSet)
											var ref = {};
											ref[rf.TYPE] = d.RelationTypeEnum.HAS_PARAMETER_SET;
											ref[rf.TO_NODE] = pset[nf.NODEID]; // id of existing set, or id of newly created pset which is undefined for now
											ref[rf.FROM_NODE] = comp[nf.NODEID];
											pset.needsSave = true;
											pset[nf.OWNERS].push(ref);
										}
										
										ps.checkPropertiesOfPset(pset, child, libId);
										
									}, null, null, "GET");
								}
								break;
							}
						});
					} }]);
				}
			});
		});

	},
		
	init : function () {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, ps = at.freebim.db.bsdd.pset, fa = net.spectroom.js.forAll,
			lib = null, libId = null;
		
		d.ParameterSet.init();
		
		fa(d.Library.arr, function (id, i) {
			lib = d.get(id);
			if (lib[nf.NAME] === "Ifc4") {
				libId = id;
				return false; // this will break the forAll loop
			}
		});
		
		ps.processTree(lib, libId);
		
	},
	
	checkPropertiesOfPset : function (pset, ifdConcept, libId) {
		var db = at.freebim.db, d = db.domain, nf = d.NodeFields, rf = d.RelationFields, ps = at.freebim.db.bsdd.pset, fa = net.spectroom.js.forAll, i18n = net.spectroom.js.i18n;
		
		// get children, that would be i.e. property 'loadBearing' for 'PSet_WallCommon' ...
		jq(document).trigger("IfdConcept.children", [{guid: ifdConcept.guid, silent: true, callback: function (childs) {
			
			// got all children
			fa (childs.IfdConceptInRelationship, function (p, k) {
				// p is a single child
				switch (p.conceptType) {
				case ifd.IfdConceptTypeEnum.PROPERTY:
					// child is a property, do we have it already?
					db.post("/search/search", {searchstring: p.guid, searchtype: "bsdd-guid"}, i18n.g("BSDD_FIND_GUID"), function (resp) {
						var prop = null;
						if (resp && resp.result && resp.result.length > 0) {
							// already present in our database
							prop = resp.result[0];
						} else {
							var names = ps.getNames(p), ref = {};
							ref[rf.TYPE] = d.RelationTypeEnum.REFERENCES; 
							ref[rf.TO_NODE] = libId;
							prop = {};
							prop[nf.BSDD_GUID] = p.guid;
							prop[nf.CLASS_NAME] = d.Parameter.className;
							prop[nf.REFERENCES] = [ref];
							prop[nf.NAME] = names.name;
							prop[nf.NAME_EN] = names.nameEn;
							prop[nf.DESC] = names.desc;
							prop[nf.DESC_EN] = names.descEn;
							
							jq(document).trigger("_save", [{entity: prop, callback: function (prop) {
								if (!pset[nf.PARAMS]) {
									pset[nf.PARAMS] = [];
								}
								pset.needsSave = true;
								var rel = {};
								rel[rf.TYPE] = d.RelationTypeEnum.CONTAINS_PARAMETER;
								rel[rf.ORDERING] = 0;
								rel[rf.FROM_NODE] = pset[nf.NODEID];
								rel[rf.TO_NODE] = prop[nf.NODEID];
								pset[nf.PARAMS].push(rel);
								at.freebim.db.bsdd.pset.save(pset);
							}}]);
						}
					}, null, null, "GET");
					break;
				}
			});
		}}]);

	},
	
	getNames : function (ifdConcept) {
		var db = at.freebim.db, fa = net.spectroom.js.forAll,
			res = {
			name : "",
			nameEn : "",
			desc : "",
			descEn : "",
			ifc: ""
		}, l = db.bsdd.languages;
		fa(ifdConcept.fullNames, function (n, j) {
			if (n.language.guid === l["de-DE"].guid) {
				res.name = n.name;
			} else if (!name.ifc && n.language.guid === l["ifc-2X4"].guid) {
				res.ifc = n.name;
			} else if (n.language.guid === l["en"].guid) {
				res.nameEn = n.name;
			}
		});
		if (!res.name) {
			res.name = res.nameEn;
		}
		fa(ifdConcept.definitions, function (n, j) {
			if (n.language.guid === l["de-DE"].guid) {
				res.desc = n.description;
			} else if (n.language.guid === l["ifc-2X4"].guid) {
				res.descEn = n.description;
			} else if (n.language.guid === l["en"].guid) {
				res.descEn = n.description;
			}
		});
		return res;
	},
	
	save : function (pset) {
		if (at.freebim.db.bsdd.pset.timers[pset.b]) {
			clearTimeout(at.freebim.db.bsdd.pset.timers[pset.b]);
		}
		at.freebim.db.bsdd.pset.timers[pset.b] = setTimeout(function () {
			if (!pset.needsSave) {
				return;
			}
			jq(document).trigger("_save", [{entity: pset}]);
		}, 5000);
	}
};

jq(document).delegate("#create-pset-btn", "click", function (event, data) {
	at.freebim.db.bsdd.pset.init();
});
	
